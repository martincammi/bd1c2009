package ubadb.tools.scheduleAnalyzer.test;

import junit.framework.TestCase;
import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingAction;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingActionType;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingSchedule;

public class NonLockingScheduleTest extends TestCase{
	
	private NonLockingSchedule sch;
	
	public void testGraphGeneration()
	{
		sch = new NonLockingSchedule();
		try {
			sch.addTransaction("T1");
			sch.addTransaction("T2");
			sch.addTransaction("T3");
			sch.addItem("X");
			sch.addItem("Y");
			sch.addItem("Z");
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","X"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","X"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","Y"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","Y"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","Y"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","Z"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","Z"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","Y"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","X"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","X"));
		} catch (ScheduleException e) {
			e.printStackTrace();
		}

		ScheduleGraph grph = sch.buildScheduleGraph();
		
		System.out.println(grph.getTransactions());
		System.out.println(grph.getArcs());
		
	}
	
	public void testLegalidad(){
		NonLockingSchedule schIlegal = new NonLockingSchedule();
		try {
			schIlegal.addTransaction("T1");
			schIlegal.addTransaction("T2");
			schIlegal.addTransaction("T3");
			schIlegal.addItem("X");
			schIlegal.addItem("Y");
			schIlegal.addItem("Z");
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","X"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","X"));
			sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3","Y"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","Y"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","Y"));
			sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2","Z"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","Z"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","Y"));
			sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3","X"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","X"));
		} catch (ScheduleException e) {
			e.printStackTrace();
		}
		LegalResult resultado = schIlegal.analyzeLegality();
		assertFalse(resultado.getMessage(), resultado.isLegal() && !resultado.getIllegalTransaction().equals("T3"));
	}
}
