/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.triggers;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.framework.mockclasses.ActionSelectionImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * @author Siminder
 *
 */
public class NoActionSelectionOccurringTriggerTest {
	LidaTaskManager tm;
	NoActionSelectionOccurringTrigger trigger;
	Map<String, Object> parameters;
	ActionSelection as;
	ActionSelectionDriver asd;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		tm = new LidaTaskManager(200,50);
		trigger = new NoActionSelectionOccurringTrigger();
		as = new ActionSelectionImpl();
		asd = new ActionSelectionDriver();
		parameters = new HashMap<String, Object>();
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger#getLidaTaskManager()}.
	 */
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

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger#setUp(java.util.Map, edu.memphis.ccrg.lida.actionselection.ActionSelection)}.
	 */
	@Test
	public void testSetUpMapOfStringObjectActionSelection() {
		trigger.setLidaTaskManager(tm);	
		parameters.put("name", "abc");	
		parameters.put("delay", 100);	
		trigger.setUp(parameters, as);
		
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", 100,trigger.delay);
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", "abc",trigger.name);
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", as,trigger.as);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger#setUp(java.util.Map, edu.memphis.ccrg.lida.actionselection.ActionSelection, edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver)}.
	 */
	@Test
	public void testSetUpMapOfStringObjectActionSelectionActionSelectionDriver() {
		trigger.setLidaTaskManager(tm);	
		parameters.put("name", "abc");	
		parameters.put("delay", 100);	
		trigger.setUp(parameters, as,asd);
		
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", 100,trigger.delay);
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", "abc",trigger.name);
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", as,trigger.as);
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", asd,trigger.asd);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger#start()}.
	 */
	@Test
	public void testStart() {
		trigger.setLidaTaskManager(tm);
		asd.setTaskManager(tm);
		parameters.put("name", "abc");	
		parameters.put("delay", 100);	
		trigger.setUp(parameters, as,asd);
		
		trigger.start();
		
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger#reset()}.
	 */
	@Test
	public void testReset() {
		trigger.setLidaTaskManager(tm);
		asd.setTaskManager(tm);
		parameters.put("name", "abc");	
		parameters.put("delay", 100);	
		trigger.setUp(parameters, as,asd);
		
		trigger.reset();
	}

}
