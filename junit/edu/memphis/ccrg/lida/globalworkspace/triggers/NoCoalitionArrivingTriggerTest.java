package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

/**
 * This is a JUnit class which can be used to test methods of the NoCoalitionArrivingTrigger class
 * @author Siminder Kaur
 */

public class NoCoalitionArrivingTriggerTest {
	
	TaskManager tm;
	NoCoalitionArrivingTrigger trigger;
	Map<String, Object> parameters;
	GlobalWorkspace gw;
	MockTaskSpawner ts;

	@Before
	public void setUp() throws Exception {
		ts = new MockTaskSpawner();
		
		tm = new TaskManager(200,50);		
		trigger = new NoCoalitionArrivingTrigger();
		gw = new MockGlobalWorkspaceImpl();		
		parameters = new HashMap<String, Object>();	
	}

	@Test
	public void testCheckForTriggerCondition() {
		System.out.println("Testing CheckForTriggerCondition method. See console...");
		
		gw.setAssistingTaskSpawner(ts);	
		
		trigger.setTaskManager(tm);		
		parameters.put("name", "StartTask");	
		parameters.put("delay", 20);	
		trigger.init(parameters, gw);
		
		trigger.checkForTriggerCondition(null);
	}

}
