package ubadb.tools.scheduleAnalyzer.test;

import junit.framework.TestCase;
import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.results.SerializabilityResult;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.ternaryLocking.TernaryLockingAction;
import ubadb.tools.scheduleAnalyzer.ternaryLocking.TernaryLockingActionType;
import ubadb.tools.scheduleAnalyzer.ternaryLocking.TernaryLockingSchedule;

public class TernaryLockingScheduleTest extends TestCase{
	
	private TernaryLockingSchedule sch;
	
	//corresponde al ejercicio 1.5 de la practica, en donde se ve que la historia es serializable
	public void testSerializable() throws InterruptedException
	{
		sch = new TernaryLockingSchedule();
		try {
			sch.addTransaction("T1");
			sch.addTransaction("T2");
			sch.addTransaction("T3");
			sch.addTransaction("T4");
			sch.addItem("A");
			sch.addItem("B");
			sch.addItem("C");
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T2","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T3","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T3","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T4","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","C"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T4","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","C"));
		} catch (ScheduleException e) {
			e.printStackTrace();
		}

		ScheduleGraph grph = sch.buildScheduleGraph();
		
		System.out.println("Ejercicio 1.5");
		System.out.println(grph.getTransactions());
		System.out.println(grph.getArcs());
		System.out.println();
		
		SerializabilityResult resultado = sch.analyzeSerializability();
//		assertTrue(resultado.getMessage(), resultado.isSerializable());
//		grph.mostrar();
	}

	//corresponde al ejercicio 1.6 de la practica, en donde se ve que la historia NO es serializable
	public void testNonSerializable() throws InterruptedException
	{
		sch = new TernaryLockingSchedule();
		try {
			sch.addTransaction("T1");
			sch.addTransaction("T2");
			sch.addTransaction("T3");
			sch.addTransaction("T4");
			sch.addTransaction("T5");
			sch.addItem("A");
			sch.addItem("B");
			sch.addItem("C");
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T2","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T3","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T5","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T5","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T2","C"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T3","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T4","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","D"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","D"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T4","D"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","D"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T5","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T5","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","C"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","B"));
		} catch (ScheduleException e) {
			e.printStackTrace();
		}

		ScheduleGraph grph = sch.buildScheduleGraph();
		
		System.out.println("Ejercicio 1.6");
		System.out.println(grph.getTransactions());
		System.out.println(grph.getArcs());
		System.out.println("");
		
		SerializabilityResult resultado = sch.analyzeSerializability();
		assertFalse(resultado.getMessage(), resultado.isSerializable());
//		grph.mostrar();
	}

	//corresponde al ejemplo7 del apunte de la practica, en donde se ve que la historia NO es serializable
	public void testNonSerializableFromTeorica() throws InterruptedException
	{
		sch = new TernaryLockingSchedule();
		try {
			sch.addTransaction("T1");
			sch.addTransaction("T2");
			sch.addTransaction("T3");
			sch.addTransaction("T4");
			sch.addItem("A");
			sch.addItem("B");
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T3","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T4","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T1","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T3","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T2","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T3","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T1","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.WLOCK,"T4","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T1","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.RLOCK,"T2","B"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T4","A"));
			sch.addAction(new TernaryLockingAction(TernaryLockingActionType.UNLOCK,"T2","B"));
		} catch (ScheduleException e) {
			e.printStackTrace();
		}

		ScheduleGraph grafo = sch.buildScheduleGraph();

		System.out.println("Ejemplo 7");
		System.out.println(grafo.getTransactions());
		System.out.println(grafo.getArcs());
		System.out.println("");
		
		SerializabilityResult resultado = sch.analyzeSerializability();
		assertFalse(resultado.getMessage(), resultado.isSerializable());
		
//		grafo.mostrar();
	}
}
