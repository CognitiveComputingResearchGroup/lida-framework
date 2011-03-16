package edu.memphis.ccrg.lida.globalworkspace.triggers;

import static org.junit.Assert.*;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

/**
 * This is a JUnit class which can be used to test methods of the NoBroadcastOccurringTrigger class
 * @author Siminder Kaur
 */

public class NoBroadcastOccurringTriggerTest {
	
	LidaTaskManager tm;
	NoBroadcastOccurringTrigger trigger;
	Map<String, Object> parameters;
	GlobalWorkspace gw;
	MockTaskSpawner ts;


	@Before
	public void setUp() throws Exception {
		ts = new MockTaskSpawner();
		
		tm = new LidaTaskManager(200,50);		
		trigger = new NoBroadcastOccurringTrigger();
		gw = new MockGlobalWorkspaceImpl();		
		parameters = new HashMap<String, Object>();	
	}	

	@Test
	public void testStart() {
		System.out.println("Testing start method. See console...");
				
		gw.setAssistingTaskSpawner(ts);	
		
		trigger.setLidaTaskManager(tm);		
		parameters.put("name", "StartTask");	
		parameters.put("delay", 20);	
		trigger.init(parameters, gw);		
		
		trigger.start();		
	}
	
	@Test
	public void testReset() {
		
		System.out.println("Testing reset() method. See console...");
		
		gw.setAssistingTaskSpawner(ts);	
		
		trigger.setLidaTaskManager(tm);		
		parameters.put("name", "ResetTask");	
		parameters.put("delay", 25);	
		trigger.init(parameters, gw);
		trigger.start();
				
		System.out.println("trigger is being reset");
		trigger.reset();
				
	}
	
	@Test
	public void testGetLidaTaskManager() {
		trigger.setLidaTaskManager(tm);		
		assertEquals("Problem with GetLidaTaskManager", tm, trigger.getLidaTaskManager());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger#setLidaTaskManager(edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager)}.
	 */
	@Test
	public void testSetLidaTaskManager() {
		trigger.setLidaTaskManager(tm);		
		assertEquals("Problem with GetLidaTaskManager", tm, trigger.getLidaTaskManager());
	}

}
