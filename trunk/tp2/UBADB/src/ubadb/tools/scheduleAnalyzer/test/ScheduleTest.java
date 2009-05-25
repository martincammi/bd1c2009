package ubadb.tools.scheduleAnalyzer.test;

import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.results.SerialResult;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingAction;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingActionType;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingSchedule;

public class ScheduleTest 
{
	
	public static void main(String[] args) {
		testAll();
	}
	
	public static void testAll()
	{
		testIsSerial();
	}	
	
	public static void testIsSerial()
	{
		NonLockingSchedule sch = new NonLockingSchedule();
		try 
		{
			sch.addTransaction("T1");
			sch.addTransaction("T2");
			sch.addTransaction("T3");
			sch.addItem("X");
			sch.addItem("Y");
			sch.addItem("Z");
			NonLockingAction A0 = new NonLockingAction(NonLockingActionType.READ,"T1","X");
			NonLockingAction A1 = new NonLockingAction(NonLockingActionType.WRITE,"T1","X");
			NonLockingAction A2 = new NonLockingAction(NonLockingActionType.WRITE,"T1","Y");
			NonLockingAction A3 = new NonLockingAction(NonLockingActionType.READ,"T2","Y");
			NonLockingAction A4 = new NonLockingAction(NonLockingActionType.READ,"T2","Y");
			NonLockingAction A5 = new NonLockingAction(NonLockingActionType.READ,"T2","Z");
			NonLockingAction A6 = new NonLockingAction(NonLockingActionType.READ,"T3","Z");
			NonLockingAction A7 = new NonLockingAction(NonLockingActionType.READ,"T3","Y");
			NonLockingAction A8 = new NonLockingAction(NonLockingActionType.READ,"T3","X");
			NonLockingAction A9 = new NonLockingAction(NonLockingActionType.WRITE,"T3","X");
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
		} 
		catch (ScheduleException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		SerialResult result = sch.analyzeSeriality();
		System.out.println(result.isSerial());
		System.out.println(result.getNonSerialTransaction());
	}
}