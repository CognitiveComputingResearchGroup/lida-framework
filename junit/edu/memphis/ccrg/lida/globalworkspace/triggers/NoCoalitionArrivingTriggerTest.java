package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.HashMap;
import java.util.Map;
import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

public class NoCoalitionArrivingTriggerTest {
	
	LidaTaskManager tm;
	NoCoalitionArrivingTrigger trigger;
	Map<String, Object> parameters;
	GlobalWorkspace gw;
	MockTaskSpawner ts;

	@Before
	public void setUp() throws Exception {
		ts = new MockTaskSpawner();
		
		tm = new LidaTaskManager(200,50);		
		trigger = new NoCoalitionArrivingTrigger();
		gw = new MockGlobalWorkspaceImpl();		
		parameters = new HashMap<String, Object>();	
	}

	@Test
	public void testCheckForTriggerCondition() {
		System.out.println("Testing CheckForTriggerCondition method. See console...");
		
		gw.setAssistingTaskSpawner(ts);	
		
		trigger.setLidaTaskManager(tm);		
		parameters.put("name", "StartTask");	
		parameters.put("delay", 20);	
		trigger.init(parameters, gw);
		
		trigger.checkForTriggerCondition(null);
	}

}
