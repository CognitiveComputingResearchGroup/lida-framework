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

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.framework.mockclasses.MockActionSelectionImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

/**
 * @author Siminder
 *
 */
public class NoActionSelectionOccurringTriggerTest {
	LidaTaskManager tm;
	NoActionSelectionOccurringTrigger trigger;
	Map<String, Object> parameters;
	ActionSelection as;
	
	//Second trigger
	Set<Behavior> setOfBehav;
	Scheme schemeA;
	Scheme schemeB;
	
	Behavior behavA;
	Behavior behavB;
	
	IndividualBehaviorActivationTrigger trigger2;
	
	/**
	 * @throws java.lang.Exception e
	 */
	@Before
	public void setUp() throws Exception {
		tm = new LidaTaskManager(200,50);
		
		trigger = new NoActionSelectionOccurringTrigger();
		as = new MockActionSelectionImpl();
		parameters = new HashMap<String, Object>();
		
		//Second trigger
		setOfBehav = new HashSet<Behavior>();
		schemeA = new SchemeImpl("Scheme1",1,1);
		schemeB = new SchemeImpl("Scheme2",2,2);
			
		trigger2 = new IndividualBehaviorActivationTrigger();
		
		schemeA.setActivation(0.8);
		schemeB.setActivation(0.2);	
		
		behavA = schemeA.getBehavior();
		behavB = schemeB.getBehavior();
		
		setOfBehav.add(behavA);
		setOfBehav.add(behavB);
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
	public void testSetUpMapOfStringObjectActionSelectionActionSelectionDriver() {
		trigger.setLidaTaskManager(tm);	
		parameters.put("name", "abc");	
		parameters.put("delay", 100);	
		trigger.setUp(parameters, as);
		
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", 100,trigger.delay);
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", "abc",trigger.name);
		assertEquals("Problem with SetUpMapOfStringObjectActionSelection", as,trigger.as);    
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger#start()}.
	 */
	@Test
	public void testStart() {
		
		System.out.println("Testing start method");
		
		trigger.setLidaTaskManager(tm);
		parameters.put("name", "abc");	
		parameters.put("delay", 15);	
		trigger.setUp(parameters, as);
		
		trigger.start();
		tm.resumeTasks();
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}	
	}
	
	/**
	 * Test method for {@link edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger#reset()}.
	 */
	@Test
	public void testReset() {
		
		System.out.println("Testing reset method");
		
		trigger.setLidaTaskManager(tm);
		parameters.put("name", "abc");	
		parameters.put("delay", 15);	
		trigger.setUp(parameters, as);
		
		System.out.println("First trigger started with delay of 15 ticks.");
		trigger.start();		
		tm.resumeTasks();
		
		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}*/
				
		//second trigger
		System.out.println("Second trigger started");
		trigger2.as=as;
		trigger2.threshold=0.5;
		trigger2.checkForTrigger(setOfBehav);			
		
		System.out.println("First trigger reset");
		trigger.reset();
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}		
		
	}

}
