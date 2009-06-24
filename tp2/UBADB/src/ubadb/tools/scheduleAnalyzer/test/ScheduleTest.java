package ubadb.tools.scheduleAnalyzer.test;

import junit.framework.TestCase;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityResult;
import ubadb.tools.scheduleAnalyzer.common.results.SerialResult;
import ubadb.tools.scheduleAnalyzer.common.results.SerializabilityResult;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingAction;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingActionType;
import ubadb.tools.scheduleAnalyzer.nonLocking.NonLockingSchedule;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityType;

public class ScheduleTest extends TestCase
{
	/*//Reeemplazado por el TestSuit
	public void testAll()
	{
		isSerial();
		isSerializable();
		isNotSerializable1();
	}*/	
	
	public void testIsSerial()
	{
		System.out.println("----Test Serial-----");
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
			e.printStackTrace();
		}
		SerialResult result = sch.analyzeSeriality();
		System.out.println(result.isSerial());
		System.out.println(result.getNonSerialTransaction());
	}

	public void testIsSerializable(){
		System.out.println("----Test is Serializable-----");
		Schedule sch = new NonLockingSchedule();
		
		
		try 
		{
			sch.addTransaction("T1");
			sch.addTransaction("T2");
			sch.addTransaction("T3");
			sch.addTransaction("T4");
			sch.addTransaction("T5");
			
			sch.addItem("A");
			sch.addItem("B");
			sch.addItem("C");
			sch.addItem("D");
			
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","B"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","C"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T5","B"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T4","B"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T5","C"));
		} 
		catch (ScheduleException e) 
		{
			e.printStackTrace();
		}
		
		//sch.showGraph();
		SerializabilityResult sr = sch.analyzeSerializability();
		System.out.println(sr.toString());
		assertTrue(sr.isSerializable());
	}
	
	public void testIsNotSerializable1(){
		System.out.println("----Test is not Serializable 1-----");
		Schedule sch = new NonLockingSchedule();
		
		try 
		{
			sch.addTransaction("T1");
			sch.addTransaction("T2");
			sch.addTransaction("T3");
			
			sch.addItem("A");
			
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T3","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
		} 
		catch (ScheduleException e) 
		{
			e.printStackTrace();
		}
		
		//sch.showGraph();
		SerializabilityResult sr = sch.analyzeSerializability();
		System.out.println(sr.toString());
		assertTrue(!sr.isSerializable());
	}
	
	public void testIsNotSerializable2(){
		System.out.println("----Test is not Serializable 2-----");
		Schedule sch = new NonLockingSchedule();
		
		try 
		{
			//Agrega la cantidad de transacciones 
			int numTnx = 5;
			for (int i = 1; i <= numTnx; i++){
				sch.addTransaction("T" + i);
			}
			
			//Agrega la cantidad de items
			int numItems = 4;
			for (int i = 65; i <= numItems; i++){
				sch.addItem((char)i + "");
			}
			
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
		} 
		catch (ScheduleException e) 
		{
			e.printStackTrace();
		}
		
		//sch.showGraph();
		SerializabilityResult sr = sch.analyzeSerializability();
		System.out.println(sr.toString());
		assertTrue(!sr.isSerializable());
	}
	
	public void testAnalyzeRecoverabilityRC(){
		System.out.println("----Test analyzeRecoverability RC-----");
		Schedule sch = new NonLockingSchedule();
		
		try 
		{
			//Agrega la cantidad de transacciones 
			int numTnx = 3;
			for (int i = 1; i <= numTnx; i++){
				sch.addTransaction("T" + i);
			}
			
			//Agrega la cantidad de items
			int numItems = 1;
			for (int i = 65; i <= numItems; i++){
				sch.addItem((char)i + "");
			}
			
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T1","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.WRITE,"T2","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T3","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
			sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T3"));
		} 
		catch (ScheduleException e) 
		{
			e.printStackTrace();
		}
		
		//sch.showGraph();
		RecoverabilityResult rr = sch.analyzeRecoverability();
		System.out.println(rr.toString());
		assertTrue(rr.getType().equals(RecoverabilityType.RECOVERABLE));
	}
	
	public void testAnalyzeRecoverabilityRC2(){
		System.out.println("----Test analyzeRecoverability RC 2-----");
		Schedule sch = new NonLockingSchedule();
		
		try 
		{
			//Agrega la cantidad de transacciones 
			int numTnx = 2;
			for (int i = 1; i <= numTnx; i++){
				sch.addTransaction("T" + i);
			}
			
			//Agrega la cantidad de items
			int numItems = 1;
			for (int i = 65; i <= numItems; i++){
				sch.addItem((char)i + "");
			}
			
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T1","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.READ,"T2","A"));
			sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T2"));
			sch.addAction(new NonLockingAction(NonLockingActionType.COMMIT,"T1"));
		} 
		catch (ScheduleException e) 
		{
			e.printStackTrace();
		}
		
		//sch.showGraph();
		RecoverabilityResult rr = sch.analyzeRecoverability();
		System.out.println(rr.toString());
		assertTrue(rr.getType().equals(RecoverabilityType.STRICT));
	}
}