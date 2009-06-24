package ubadb.tools.scheduleAnalyzer.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * 
 * @author martin.cammi
 * Clase TestSuit para correr todos los tests del Scheduler.
 */
public class ScheduleTestSuit extends TestCase
{
	public static Test suite() {
		TestSuite suite = new TestSuite();
		suite.addTestSuite(ScheduleTest.class);
		return suite;
	}
 
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
}