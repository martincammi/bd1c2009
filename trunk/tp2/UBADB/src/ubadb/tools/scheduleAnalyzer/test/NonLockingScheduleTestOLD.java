package ubadb.tools.scheduleAnalyzer.test;

import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingAction;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingActionType;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingSchedule;

public class NonLockingScheduleTestOLD {
	
	private static	NonLockingSchedule sch;

	public static void main(String[] args) {
		testAll();
	}
	
	public static void testAll()
	{
		inicio();
		testGraphGeneration();
		testLegalidad();
	}	
	
	private static void inicio(){
		sch = new NonLockingSchedule();
		try {
			sch.addTransaction("T1");
			sch.addTransaction("T2");
			sch.addTransaction("T3");
//			sch.addTransaction("T4");
//			sch.addTransaction("T5");
//			sch.addTransaction("T6");
//			sch.addTransaction("T7");
//			sch.addTransaction("T8");
			sch.addItem("X");
			sch.addItem("Y");
			sch.addItem("Z");
			NonLockingAction A0 = new NonLockingAction(NonLockingActionType.READ,"T1","X");
			NonLockingAction A1 = new NonLockingAction(NonLockingActionType.WRITE,"T2","X");
			NonLockingAction A2 = new NonLockingAction(NonLockingActionType.WRITE,"T3","Y");
			NonLockingAction A3 = new NonLockingAction(NonLockingActionType.READ,"T2","Y");
			NonLockingAction A4 = new NonLockingAction(NonLockingActionType.READ,"T1","Y");
			NonLockingAction A5 = new NonLockingAction(NonLockingActionType.READ,"T2","Z");
			NonLockingAction A6 = new NonLockingAction(NonLockingActionType.READ,"T3","Z");
			NonLockingAction A7 = new NonLockingAction(NonLockingActionType.READ,"T3","Y");
			NonLockingAction A8 = new NonLockingAction(NonLockingActionType.READ,"T3","X");
			NonLockingAction A9 = new NonLockingAction(NonLockingActionType.WRITE,"T1","X");
			sch.addAction(A0);
			sch.addAction(A1);
			sch.addAction(A2);
			sch.addAction(A3);
			sch.addAction(A4);
			sch.addAction(A5);
			sch.addAction(A6);
			sch.addAction(A7);
			sch.addAction(A8);
			sch.addAction(A9);
		} catch (ScheduleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void testGraphGeneration()
	{
		ScheduleGraph grph = sch.buildScheduleGraph();
		
		System.out.println(grph.getTransactions());
		System.out.println(grph.getArcs());
		
	}
	
	public static void testLegalidad(){
		NonLockingSchedule schIlegal = new NonLockingSchedule();
		try {
			schIlegal.addTransaction("T1");
			schIlegal.addTransaction("T2");
			schIlegal.addTransaction("T3");
//			sch.addTransaction("T4");
//			sch.addTransaction("T5");
//			sch.addTransaction("T6");
//			sch.addTransaction("T7");
//			sch.addTransaction("T8");
			schIlegal.addItem("X");
			schIlegal.addItem("Y");
			schIlegal.addItem("Z");
			NonLockingAction A0 = new NonLockingAction(NonLockingActionType.READ,"T1","X");
			NonLockingAction A1 = new NonLockingAction(NonLockingActionType.WRITE,"T2","X");
			NonLockingAction A2 = new NonLockingAction(NonLockingActionType.COMMIT,"T3","Y");
			NonLockingAction A3 = new NonLockingAction(NonLockingActionType.READ,"T2","Y");
			NonLockingAction A4 = new NonLockingAction(NonLockingActionType.READ,"T1","Y");
			NonLockingAction A5 = new NonLockingAction(NonLockingActionType.COMMIT,"T2","Z");
			NonLockingAction A6 = new NonLockingAction(NonLockingActionType.READ,"T3","Z");
			NonLockingAction A7 = new NonLockingAction(NonLockingActionType.READ,"T3","Y");
			NonLockingAction A8 = new NonLockingAction(NonLockingActionType.COMMIT,"T3","X");
			NonLockingAction A9 = new NonLockingAction(NonLockingActionType.WRITE,"T1","X");
			schIlegal.addAction(A0);
			schIlegal.addAction(A1);
			schIlegal.addAction(A2);
			schIlegal.addAction(A3);
			schIlegal.addAction(A4);
			schIlegal.addAction(A5);
			schIlegal.addAction(A6);
			schIlegal.addAction(A7);
			schIlegal.addAction(A8);
			schIlegal.addAction(A9);
		} catch (ScheduleException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LegalResult resultado = schIlegal.analyzeLegality();
		System.out.println(resultado.isLegal());
		System.out.println(resultado.getIllegalTransaction());
		System.out.println(resultado.getMessage());
	}
}
