/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
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
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.mockclasses.MockActionSelectionImpl;

import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

/**
 * @author Siminder
 *
 */
public class AggregateBehaviorActivationTriggerTest extends TestCase{
	Queue<Behavior> queueOfSchemes;
	
	Scheme schemeA;
	Scheme schemeB;
	AggregateBehaviorActivationTrigger trigger;
	ActionSelection as;
	ActionSelectionDriver asd;
	Map<String, Object> parameters;
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		queueOfSchemes = new ConcurrentLinkedQueue<Behavior>();
		schemeA = new SchemeImpl("Scheme1",1,1);
		schemeB = new SchemeImpl("Scheme2",2,2);
		parameters = new HashMap<String, Object>();
		as = new MockActionSelectionImpl();
		asd=new ActionSelectionDriver();
		schemeA.setActivation(0.3);
		schemeB.setActivation(0.3);
		
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
		trigger.setUp(parameters, as, asd);		
		trigger.checkForTrigger(queueOfSchemes);		
	}


	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.AggregateBehaviorActivationTrigger#setUp(java.util.Map, edu.memphis.ccrg.lida.actionselection.ActionSelection)}.
	 */
	@Test
	public void testSetUp() {
		trigger.setUp(parameters, as,asd);
		assertEquals("Problem with SetUp", 0.5, trigger.threshold);
	}
}
