package ubadb.tools.scheduleAnalyzer.test;

import junit.framework.TestCase;
import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingAction;
import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingActionType;
import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingSchedule;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityResult;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityType;
import ubadb.tools.scheduleAnalyzer.common.results.SerialResult;
import ubadb.tools.scheduleAnalyzer.common.results.SerializabilityResult;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingAction;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingActionType;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingSchedule;
import ubadb.tools.scheduleAnalyzer.ternaryLocking.TernaryLockingAction;
import ubadb.tools.scheduleAnalyzer.ternaryLocking.TernaryLockingActionType;
import ubadb.tools.scheduleAnalyzer.ternaryLocking.TernaryLockingSchedule;

/**
 * @author Grupo4
 * Casos de test propios. 
 *
 */
public class ScheduleTest extends TestCaseBase
{

	/**
	 *  Evalua				Casos de Test	
	 *  Legalidad:  		   1 - 5
	 *  Serial: 			     6
	 *  Serializabilidad:      7 - 9
	 *  Recuperabilidad:	   10 -13	   
	 */
	
	/**
	 * Cobertura de los Tests
	 * 
	 *  	Objetivo		NonLocking		 BinaryLocking		 TernaryLocking
	 *	   Legalidad  		   1				2,3,4				   5
	 *       Serial 		   6	   	   (mismo para todas)    (mismo para todas)
	 *  Serializabilidad      7,8,9				 --				  		--
	 *  Recuperabilidad	   10,11,12,13	   (mismo para todas)   (mismo para todas)
	 */
	
	/**
	 * Objetivo: Legalidad.
	 * Tipo: Sin Locking.
	 * Resultado: Es legal.
	 */
	public void testCaso1(){
		show("Caso9");
		Schedule sch = getNonLockingSchedule(2);
		
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		
		LegalResult res = sch.analyzeLegality();
		show(res.toString());
		assertTrue(res.isLegal());
	}
	
	/**
	 * Objetivo: Legalidad.
	 * Tipo: Binary Locking.
	 * Resultado: No es legal. T1 intenta hacer un lock de B cuando T2 no hizo el Unlock.
	 */
	public void testCaso2(){
		show("Caso10");
		Schedule sch = getBinaryLockingSchedule(2);
		
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","B"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","B"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","B"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T2"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1"));
 
		LegalResult res = sch.analyzeLegality();
		show(res.toString());
		assertFalse(res.isLegal());
	}
	
	/**
	 * Objetivo: Legalidad.
	 * Tipo: Binary Locking.
	 * Resultado: No es legal. T1 no hace UNLOCK de A
	 */
	public void testCaso3(){
		show("Caso11");
		Schedule sch = getBinaryLockingSchedule(2);
		
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","B"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","B"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T2"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1"));
 
		LegalResult res = sch.analyzeLegality();
		show(res.toString());
		assertFalse(res.isLegal());
	}
	
	/**
	 * Objetivo: Legalidad.
	 * Tipo: Binary Locking.
	 * Resultado: No es legal. T1 hace lock de A cuando T2 no hizo UNLOCK todavia.
	 */
	public void testCaso4(){
		show("Caso12");
		Schedule sch = getBinaryLockingSchedule(2);
		
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T2"));

		LegalResult res = sch.analyzeLegality();
		show(res.toString());
		assertFalse(res.isLegal());
	}

	/**
	 * Objetivo: Legalidad.
	 * Tipo: Locking Ternario.
	 * Resultado: Ess legal.
	 */
	public void testCaso5(){
		show("Caso13");
		Schedule sch = getTernaryLockingSchedule(2);

		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T2","A"));

		LegalResult res = sch.analyzeLegality();
		show(res.toString());
		assertTrue(res.isLegal());
	}
	
	/**
	 * Objetivo: Plan Serial
	 * Tipo: Sin Locking.
	 * Resultado: Es Serial.
	 */
	public void testCaso6()	{
		show("Caso1");
		Schedule sch = getNonLockingSchedule(3);
		
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","X"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","X"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","Y"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","Y"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","Y"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","Z"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","Z"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","Y"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","X"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","X"));
		
		SerialResult res = sch.analyzeSeriality();
		show(res.toString());
		assertTrue(res.isSerial());
	}

	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Sin Locking.
	 * Resultado: No hay ciclos. Luego, es serializable.
	 */
	public void testCaso7(){
		show("Caso2");
		Schedule sch = getNonLockingSchedule(5);
		
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","C"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T5","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T4","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T5","C"));
		
		SerializabilityResult res = sch.analyzeSerializability();
		show(res.toString());
		assertTrue(res.isSerializable());
	}
	
	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Sin Locking.
	 * Resultado: Hay ciclos. Luego, no es serializable.
	 */
	public void testCaso8(){
		show("Caso3");
		Schedule sch = getNonLockingSchedule(3);
		
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
		
		SerializabilityResult res = sch.analyzeSerializability();
		show(res.toString());
		assertFalse(res.isSerializable());
	}
	
	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Sin Locking.
	 * Resultado: Hay ciclos. Luego, no es serializable.
	 */
	public void testCaso9(){
		show("Caso4");
		Schedule sch = getNonLockingSchedule(5);
		
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","C"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","C"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T5","C"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T4","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T4","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T5","D"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T4","D"));
		
		SerializabilityResult res = sch.analyzeSerializability();
		show(res.toString());
		assertFalse(res.isSerializable());
	}
	
	/**
	 * Objetivo: Nivel de Recuperabilidad
	 * Tipo: Sin Locking.
	 * Resultado: Recuperable.
	 * Conflicto: T2 lee A, un dato que escribió T1, y T1 todavía no comiteó.
	 */
	public void testCaso10(){
		show("Caso5");
		Schedule sch = getNonLockingSchedule(3);
		
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","A")); //NO CUMPLE ACA.
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3"));
		
		RecoverabilityResult res = sch.analyzeRecoverability();
		show(res.toString());
		assertTrue(RecoverabilityType.RECOVERABLE.equals(res.getType()));
	}
	
	/**
	 * Objetivo: Nivel de Recuperabilidad
	 * Tipo: Sin Locking.
	 * Resultado: Estricto.
	 */
	public void testCaso11(){
		show("Caso6");
		Schedule sch = getNonLockingSchedule(2);
		
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));

		RecoverabilityResult res = sch.analyzeRecoverability();
		show(res.toString());
		assertTrue(RecoverabilityType.STRICT.equals(res.getType()));
	}
	
	/**
	 * Objetivo: Nivel de Recuperabilidad
	 * Tipo: Sin Locking.
	 * Resultado: Recuperable.
	 * Conflicto: T2 lee A, un dato que escribió T1, y T1 todavía no comiteó.
	 */
	public void testCaso12(){
		show("Caso7");
		Schedule sch = getNonLockingSchedule(4);
		
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","A")); //NO CUMPLE ACA.
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T4","B")); //NO CUMPLE ESTRICTO.
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3"));
	
		RecoverabilityResult res = sch.analyzeRecoverability();
		show(res.toString());
		assertTrue(RecoverabilityType.RECOVERABLE.equals(res.getType()));
	}

	/**
	 * Objetivo: Nivel de Recuperabilidad
	 * Tipo: Sin Locking.
	 * Resultado: Estricto.
	 */
	public void testCaso13(){
		show("Caso8");
		Schedule sch = getNonLockingSchedule(2);
		
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		
		RecoverabilityResult res = sch.analyzeRecoverability();
		show(res.toString());
		assertTrue(RecoverabilityType.STRICT.equals(res.getType()));
	}
	
	


}