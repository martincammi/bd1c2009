package ubadb.tools.scheduleAnalyzer.test;

import java.util.ArrayList;
import junit.framework.TestCase;
import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.results.SerializabilityResult;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingAction;
import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingActionType;
import ubadb.tools.scheduleAnalyzer.binaryLocking.BinaryLockingSchedule;

public class BinaryLockingScheduleTest extends TestCase {
	
	private BinaryLockingSchedule sch;
	
	public void testPlan0()
	{
		sch = new BinaryLockingSchedule();
		try {
			sch.addTransaction("T1");
			sch.addItem("X");
			sch.addItem("Y");
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","X"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","X"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","Y"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","Y"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1",""));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","Y"));
		} 
		catch (ScheduleException e) 
		{
			e.printStackTrace();
		}

		ScheduleGraph grph = sch.buildScheduleGraph();
		System.out.println("1 Transaccion");
		System.out.println(grph.getTransactions());
		System.out.println(grph.getArcs());
		assertTrue(grph.getArcs().isEmpty());
		ArrayList<String> trans = (ArrayList<String>)grph.getTransactions();
		assertTrue("No contiene a T1", trans.contains("T1"));
		trans.remove("T1");
		assertTrue("Contiene otras cosas ademas de T1: ".concat(trans.toString()),trans.isEmpty());
//		SerializabilityResult resultado = sch.analyzeSerializability();
//		assertTrue(resultado.getMessage(), resultado.isSerializable());
//		grph.mostrar();
//		LegalResult lr = sch.analyzeLegality();
		//Es ilegal porque hace un Lock después de commit 
//		assertFalse(lr.getMessage(), lr.isLegal());
//		assertEquals(lr.getIllegalTransaction(), "T1");

	}
	
	public void testPlan1()
	{
		sch = new BinaryLockingSchedule();
			try {
				sch.addTransaction("T1");
				sch.addTransaction("T2");
				sch.addTransaction("T3");
				sch.addTransaction("T4");
				sch.addItem("X");
				sch.addItem("Y");
				sch.addItem("Z");
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","X"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","X"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","Y"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","Y"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","Z"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","X"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","Z"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","X"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T3","X"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","Y"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","Y"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","Z"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T3","X"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","Z"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T3","Z"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T3","Z"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","Z"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","Z"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1",""));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","X"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","X"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T3","X"));
				sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T3","X"));
			} 
			catch (ScheduleException e) 
			{
				e.printStackTrace();
			}

			ScheduleGraph grph = sch.buildScheduleGraph();
			System.out.println("4 Transacciones, y grafo completo entre 1,2 y 3 excepto por arista de 2 a 1");
			System.out.println(grph.getTransactions());
			System.out.println(grph.getArcs());
//			SerializabilityResult resultado = sch.analyzeSerializability();
//			assertFalse(resultado.getMessage(), resultado.isSerializable());
//			grph.mostrar();
//			LegalResult lr = sch.analyzeLegality();
//			assertTrue(lr.getMessage(), lr.isLegal());
	}

	public void testPlan2()
	{
		sch = new BinaryLockingSchedule();
		try {
			sch.addTransaction("T1");
			sch.addTransaction("T2");
			sch.addTransaction("T3");
			sch.addTransaction("T4");
			sch.addItem("X");
			sch.addItem("Y");
			sch.addItem("Z");
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","X"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","X"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T1","Y"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T1","Y"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","Z"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","Z"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T3","X"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","Y"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","Y"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T2","Z"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T3","X"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T3","Z"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.COMMIT,"T1",""));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T2","Y"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T3","Z"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T3","Y"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.UNLOCK,"T3","Y"));
			sch.addAction(new BinaryLockingAction(BinaryLockingActionType.LOCK,"T4","Y"));
		} 
		catch (ScheduleException e) 
		{
			e.printStackTrace();
		}

		ScheduleGraph grph = sch.buildScheduleGraph();
		System.out.println("4 Transacciones, y arcos: 1-2, 1-3, 2-3, 3-4");
		System.out.println(grph.getTransactions());
		System.out.println(grph.getArcs());
//		SerializabilityResult resultado = sch.analyzeSerializability();
//		assertTrue(resultado.getMessage(), resultado.isSerializable());
//		grph.mostrar();
//		LegalResult lr = sch.analyzeLegality();
		//Es ilegal porque en el paso 10 T2 hace un Lock sobre Z y antes de que haga el unlock,
		// en el paso 12, T3 hace un Lock sobre Z tambien, por esto digo que T3 es ilegal 
//		assertFalse(lr.getMessage(), lr.isLegal());
//		assertEquals(lr.getIllegalTransaction(), "T3");

	}
	
}
