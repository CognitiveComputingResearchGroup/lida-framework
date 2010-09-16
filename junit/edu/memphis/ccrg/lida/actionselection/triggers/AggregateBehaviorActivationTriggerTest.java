/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.triggers;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.framework.mockclasses.ActionSelectionImpl;

import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

/**
 * @author Siminder
 *
 */
public class AggregateBehaviorActivationTriggerTest extends TestCase{
	Queue<Scheme> queueOfSchemes;
	
	Scheme schemeA;
	Scheme schemeB;
	AggregateBehaviorActivationTrigger trigger;
	ActionSelection as;
	Map<String, Object> parameters;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		queueOfSchemes = new ConcurrentLinkedQueue<Scheme>();
		schemeA = new SchemeImpl("Scheme1",1,1);
		schemeB = new SchemeImpl("Scheme2",2,2);
		parameters = new HashMap<String, Object>();
		as = new ActionSelectionImpl();
		schemeA.setActivation(0.2);
		schemeB.setActivation(0.4);
		
		queueOfSchemes.add(schemeA);
		queueOfSchemes.add(schemeB);
		
		parameters.put("threshold", 0.5);		
		
		trigger = new AggregateBehaviorActivationTrigger();
		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.AggregateBehaviorActivationTrigger#checkForTrigger(java.util.Queue)}.
	 */
	@Test
	public void testCheckForTrigger() {
		trigger.threshold=0.5;
		trigger.as=as;
		trigger.checkForTrigger(queueOfSchemes);
		
	}


	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.AggregateBehaviorActivationTrigger#setUp(java.util.Map, edu.memphis.ccrg.lida.actionselection.ActionSelection)}.
	 */
	@Test
	public void testSetUp() {
		trigger.setUp(parameters, as);
		assertEquals("Problem with SetUp", 0.5, trigger.threshold);
	}

	

}
