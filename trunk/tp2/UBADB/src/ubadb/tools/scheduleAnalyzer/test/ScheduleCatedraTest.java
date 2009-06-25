package ubadb.tools.scheduleAnalyzer.test;

import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingAction;
import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingActionType;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityResult;
import ubadb.tools.scheduleAnalyzer.common.results.SerialResult;
import ubadb.tools.scheduleAnalyzer.common.results.SerializabilityResult;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingAction;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingActionType;
import ubadb.tools.scheduleAnalyzer.ternaryLocking.TernaryLockingAction;
import ubadb.tools.scheduleAnalyzer.ternaryLocking.TernaryLockingActionType;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityType;

/**
 * 
 * @author martin.cammi
 * Casos de test de la cátedra.
 */
public class ScheduleCatedraTest extends TestCaseBase{
	
	/**
	 * Objetivo: Legalidad.
	 * Tipo: Sin Locking.
	 * Resultado: No es legal. T2 tiene dos commits.
	 */
	public void testCaso1(){
		show("Caso1");
		Schedule sch = getNonLockingSchedule(3);
 
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","F"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","E"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","R"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","T"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","O"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));

		LegalResult res = sch.analyzeLegality();
		System.out.println(res.toString());
		assertFalse(res.isLegal());
	}

	/**
	 * Objetivo: Legalidad.
	 * Tipo: Locking Binario..
	 * Resultado: No es legal. T1 no realiza un ul(d).
	 */
	public void testCaso2(){
		show("Caso2");
		Schedule sch = getBinaryLockingSchedule(2);
 
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","D"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","I"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","E"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","G"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","O"));
		
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","O"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","E"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","I"));
		
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T2"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","G"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1"));

		LegalResult res = sch.analyzeLegality();
		System.out.println(res.toString());
		assertFalse(res.isLegal());
	}
	
	/**
	 * Objetivo: Legalidad.
	 * Tipo: Locking Binario.
	 * Resultado: No es legal. T2 hace ul(v) sin efectuar previamente l(v).
	 */
	public void testCaso3(){
		show("Caso3");
		Schedule sch = getBinaryLockingSchedule(2);
 
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","F"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","P"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","V"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","P"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","P"));
		
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T2"));

		LegalResult res = sch.analyzeLegality();
		System.out.println(res.toString());
		assertFalse(res.isLegal());
	}
	
	/**
	 * Objetivo: Legalidad.
	 * Tipo: Locking Binario.
	 * Resultado: No es legal. T2 hace l(f) sin que T1 realice un ul(f).
	 */
	public void testCaso4(){
		show("Caso4");
		Schedule sch = getBinaryLockingSchedule(2);
 
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","F"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","F"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","F"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","F"));
		
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T2"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1"));

		LegalResult res = sch.analyzeLegality();
		System.out.println(res.toString());
		assertFalse(res.isLegal());
	}

	/**
	 * Objetivo: Legalidad.
	 * Tipo: Locking Ternario.
	 * Resultado: No es legal. T2 no realiza un ul(b).
	 */
	public void testCaso5(){
		show("Caso5");
		Schedule sch = getTernaryLockingSchedule(2);
 
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","B"));
		
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T2"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T1"));
		
		LegalResult res = sch.analyzeLegality();
		System.out.println(res.toString());
		assertFalse(res.isLegal());
	}
	
	/**
	 * Objetivo: Legalidad.
	 * Tipo: Locking Ternario.
	 * Resultado: No es legal. T1 realiza un l(b) después de un commit.
	 */
	public void testCaso6(){
		show("Caso6");
		Schedule sch = getTernaryLockingSchedule(3);
 
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T2","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T3","I"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","I"));
		
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T3"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T2"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T1"));
		
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","B"));
		
		LegalResult res = sch.analyzeLegality();
		System.out.println(res.toString());
		assertFalse(res.isLegal());		
	}

	/**
	 * Objetivo: Plan Serial
	 * Tipo: Sin Locking.
	 * Resultado: Es Serial.
	 */
	public void testCaso7(){
		show("Caso7");
		Schedule sch = getNonLockingSchedule(3);
 
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","P"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","E"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","O"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","N"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3"));
		
		SerialResult res = sch.analyzeSeriality();
		System.out.println(res.toString());
		assertTrue(res.isSerial());		
	}
	
	/**
	 * Objetivo: Plan Serial
	 * Tipo: Sin Locking.
	 * Resultado: No es serial. Ninguna transacción es ejecutada serialmente.
	 */
	public void testCaso8(){
		show("Caso8");
		Schedule sch = getNonLockingSchedule(3);
 
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","P"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","E"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","R"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","O"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","N"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","S"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		
		SerialResult res = sch.analyzeSeriality();
		System.out.println(res.toString());
		assertFalse(res.isSerial());	
	}
	
	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Sin Locking.
	 * Resultado: Hay ciclos. Luego, no es serializable.
	 */
	public void testCaso9(){
		show("Caso9");
		Schedule sch = getNonLockingSchedule(3);
 
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		
		SerializabilityResult res = sch.analyzeSerializability();
		System.out.println(res.toString());
		assertFalse(res.isSerializable());
		//TODO: No deberia agregar a T3 en el ciclo. El grafo está OK.
		//assertFalse(true); 
	}
	
	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Sin Locking.
	 * Resultado: Sin ciclos.
	 */
	public void testCaso10(){
		show("Caso10");
		Schedule sch = getNonLockingSchedule(3);
 
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","B"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","U"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","Y"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		
		SerializabilityResult res = sch.analyzeSerializability();
		System.out.println(res.toString());
		assertTrue(res.isSerializable());
	}

	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Locking Binario.
	 * Resultado: Sin ciclos.
	 */
	public void testCaso11(){
		show("Caso11");
		Schedule sch = getBinaryLockingSchedule(5);
 
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","B"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","B"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T3","G"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T4","D"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T4","D"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T4","H"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T4","H"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T5","W"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T5","W"));		
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T5"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T4","W"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T4","W"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T4"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T3","W"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T3","W"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T3","G"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","W"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","W"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T2"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","W"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","W"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1"));
		sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T3"));
		
		SerializabilityResult res = sch.analyzeSerializability();
		System.out.println(res.toString());
		assertTrue(res.isSerializable());
	}
	
	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Locking Ternario.
	 * Resultado: Sin ciclos. Órdenes: T1,T2 y T2,T1.
	 */
	public void testCaso12(){
		show("Caso12");
		Schedule sch = getTernaryLockingSchedule(2);
 
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","C"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T2","I"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","I"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","C"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","T"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","T"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T2"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T1"));
	
		
		SerializabilityResult res = sch.analyzeSerializability();
		System.out.println(res.toString());
		assertTrue(res.isSerializable());
	}
	
	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Locking Ternario.
	 * Resultado: Tiene ciclos. No es serializable.
	 */
	public void testCaso13(){
		show("Caso13");
		Schedule sch = getTernaryLockingSchedule(4);
 
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T3","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T4","J"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T4","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","J"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","J"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","J"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T1"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T2"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T1"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T3"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T4"));
		
		SerializabilityResult res = sch.analyzeSerializability();
		System.out.println(res.toString());
		assertFalse(res.isSerializable());
		//TODO: martin.cammi Ver porque el ciclo no queda en el mismo orden. 
		//Creo que saca otros arcos transitivos ver
	}
	
	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Locking Ternario.
	 * Resultado: Sin Ciclos. 
	 */
	public void testCaso14(){
		show("Caso14");
		Schedule sch = getTernaryLockingSchedule(4);
 
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","C"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T2","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","C"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T3","T"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","T"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T3"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T4","T"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","T"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T1"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T2"));
		//TODO: ¿Falta commit t4? sch.addAction(new TernaryLockingAction(TernaryLockingActionType.COMMIT,"T4"));
		
		SerializabilityResult res = sch.analyzeSerializability();
		System.out.println(res.toString());
		assertTrue(res.isSerializable());
	}
	
	/**
	 * Objetivo: Serializabilidad
	 * Tipo: Locking Ternario.
	 * Resultado: Sin Ciclos.
	 */
	public void testCaso15(){
		show("Caso15");
		Schedule sch = getTernaryLockingSchedule(4);
 
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T4","D"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T2","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","D"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T3","C"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","C"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T4","E"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","C"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","E"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T2","E"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","A"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T3","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","C"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","B"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","D"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","E"));
		sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","D"));
		
		SerializabilityResult res = sch.analyzeSerializability();
		System.out.println(res.toString());
		assertTrue(res.isSerializable());
	}
	
	/**
	 * Objetivo: Nivel de Recuperabilidad
	 * Tipo: Sin Locking.
	 * Resultado: Recuperable.
	 */
	public void testCaso16(){
		show("Caso16");
		Schedule sch = getNonLockingSchedule(3);
 
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","X"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","Y"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","X"));
		sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","Z"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","X"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","Y"));
		sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","Z"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
		sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
				
		RecoverabilityResult res = sch.analyzeRecoverability();
		System.out.println(res.toString());
		assertTrue(RecoverabilityType.RECOVERABLE.equals(res.getType()));
	}
	/*
	public void testCaso17(){}
	public void testCaso18(){}
	public void testCaso19(){}
	public void testCaso20(){}
	*/
	
}
