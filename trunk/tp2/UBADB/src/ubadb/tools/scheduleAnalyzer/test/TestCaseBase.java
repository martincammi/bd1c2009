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
 * 
 * @author martin.cammi
 * Clase Base de todos los tests.
 */
public class TestCaseBase extends TestCase{
	
	private NonLockingSchedule nonLckSch;
	private BinaryLockingSchedule binLckSch;
	private TernaryLockingSchedule terLckSch;
	
	/*Non Locking Methods*/
	protected NonLockingSchedule getNonLockingSchedule() {
		nonLckSch = new NonLockingSchedule(); 
		setCantTnxNonLocking(10);
		return nonLckSch; 
	}
	protected NonLockingSchedule getNonLockingSchedule(int numTnx, int numItems) {
		nonLckSch = new NonLockingSchedule();
		setCantTnxNonLocking(numTnx);
		setCantItemsNonLocking(numItems);
		return nonLckSch; 
	}
	protected NonLockingSchedule getNonLockingSchedule(int numTnx) {
		nonLckSch = new NonLockingSchedule();
		setCantTnxNonLocking(numTnx);
		setCantItemsNonLocking(26);
		return nonLckSch; 
	}
	protected void setCantTnxNonLocking(int numTnx){
		setCantTnx(nonLckSch,numTnx);
	}
	protected void setCantItemsNonLocking(int numItems){
		setCantItems(nonLckSch,numItems);
	}
	
	/*Binary Locking Methods*/
	protected BinaryLockingSchedule getBinaryLockingSchedule() {
		binLckSch = new BinaryLockingSchedule();
		setCantTnxBinaryLocking(10);
		return binLckSch ;
	}
	protected BinaryLockingSchedule getBinaryLockingSchedule(int numTnx) {
		binLckSch = new BinaryLockingSchedule();
		setCantTnxBinaryLocking(numTnx);
		setCantItemsBinaryLocking(26);
		return binLckSch ;
	}
	protected BinaryLockingSchedule getBinaryLockingSchedule(int numTnx, int numItems) {
		binLckSch = new BinaryLockingSchedule();
		setCantTnxBinaryLocking(numTnx);
		setCantItemsBinaryLocking(numItems);
		return binLckSch ;
	}
	protected void setCantTnxBinaryLocking(int numTnx){
		setCantTnx(binLckSch,numTnx);
	}
	protected void setCantItemsBinaryLocking(int numItems){
		setCantItems(binLckSch,numItems);
	}
	
	/*Ternary Locking Methods*/
	protected TernaryLockingSchedule getTernaryLockingSchedule() {
		terLckSch = new TernaryLockingSchedule();
		setCantTnxTernaryLocking(10);
		return terLckSch;
	}
	protected TernaryLockingSchedule getTernaryLockingSchedule(int numTnx, int numItems) {
		terLckSch = new TernaryLockingSchedule();
		setCantTnxTernaryLocking(numTnx);	
		setCantItemsTernaryLocking(numItems);
		return terLckSch;
	}
	protected TernaryLockingSchedule getTernaryLockingSchedule(int numTnx) {
		terLckSch = new TernaryLockingSchedule();
		setCantTnxTernaryLocking(numTnx);	
		setCantItemsTernaryLocking(26);
		return terLckSch;
	}
	protected void setCantTnxTernaryLocking(int numTnx){
		setCantTnx(terLckSch,numTnx);
	}
	protected void setCantItemsTernaryLocking(int numItems){
		setCantItems(terLckSch,numItems);
	}
	
	/*Others*/
	private void setCantTnx(Schedule sch, int numTnx){
		try {
			for (int i = 1; i <= numTnx; i++){
					sch.addTransaction("T" + i);
			}
		} catch (ScheduleException e) {
			e.printStackTrace();
		}
	}
	
	private void setCantItems(Schedule sch, int numItems){
		try {
			for (int i = 65; i <= numItems; i++){
				sch.addItem((char)i + "");
			}
		} catch (ScheduleException e) {
			e.printStackTrace();
		}
	}
	
	public static void show(String mensaje){
		System.out.println("");
		System.out.println(mensaje);
	}
	
}