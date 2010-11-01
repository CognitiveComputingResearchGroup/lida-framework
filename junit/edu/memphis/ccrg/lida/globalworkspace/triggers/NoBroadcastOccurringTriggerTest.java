package edu.memphis.ccrg.lida.globalworkspace.triggers;

import java.util.HashMap;

import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;



public class NoBroadcastOccurringTriggerTest {
	
	LidaTaskManager tm;
	NoBroadcastOccurringTrigger trigger;
	Map<String, Object> parameters;
	GlobalWorkspace gw;
	
	//Second trigger
	Set<Coalition> setOfCoalition;
	Coalition coalitionA;
	Coalition coalitionB;
	IndividualCoaltionActivationTrigger trigger2;

	@Before
	public void setUp() throws Exception {
		tm = new LidaTaskManager(200,50);		
		trigger = new NoBroadcastOccurringTrigger();
		gw = new MockGlobalWorkspaceImpl();		
		parameters = new HashMap<String, Object>();		
	}	

	@Test
	public void testStart() {
		System.out.println("Testing start method");
		trigger.setLidaTaskManager(tm);
		gw.setTaskManager(tm);
		parameters.put("name", "abc");	
		parameters.put("delay", 15);	
		trigger.setUp(parameters, gw);		
		
		trigger.start();
		tm.resumeSpawnedTasks();
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}	
	}

}
