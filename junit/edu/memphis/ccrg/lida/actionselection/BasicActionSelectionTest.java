/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockSensoryMotorMemory;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

public class BasicActionSelectionTest {
	
	private static final double EPSILON = 1e-5;
	private BasicActionSelection as;
	private Behavior behav1,behav2;	
	private MockSensoryMotorMemory smm = new MockSensoryMotorMemory();
	private Scheme scheme1,scheme2 ;
		
	private AgentAction action1 = new AgentActionImpl();
	
	private AgentAction action2 = new AgentActionImpl();
	
	@Before
	public void setUp() throws Exception {			
		scheme1= new SchemeImpl("scheme1",action1);
		scheme2= new SchemeImpl("scheme2",action2);
		
		as = new BasicActionSelection();
						
		behav1 = scheme1.getInstantiation();
		behav2 = scheme2.getInstantiation();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetModuleContent() {
		as.receiveBehavior(behav1);
		Collection<Behavior> content = (Collection<Behavior>) as.getModuleContent("behaviors");
		
		assertTrue("Problem with GetModuleContent", content.size()==1);
		assertTrue("Problem with GetModuleContent", content.contains(behav1));
		assertTrue("Problem with GetModuleContent", !content.contains(behav2));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReceiveBehavior() {
		as.receiveBehavior(behav1);
		as.receiveBehavior(behav2);
		Collection<Behavior> content = (Collection<Behavior>) as.getModuleContent("behaviors");
		
		assertTrue("Problem with ReceiveBehavior", content.size()==2);
		assertTrue("Problem with ReceiveBehavior", content.contains(behav1));
		assertTrue("Problem with ReceiveBehavior", content.contains(behav2));
	}

	@Test
	public void testSelectAction() {
		behav1.setActivation(0.1);
		behav2.setActivation(0.2);
		as.receiveBehavior(behav1);
		as.receiveBehavior(behav2);
		as.addListener(smm);		
		
		assertNull(smm.action);
		assertFalse(smm.actionReceived);
		
		AgentAction action = as.selectAction();
		
		assertTrue(smm.actionReceived);
		assertEquals(action2,smm.action);
		assertEquals(action2,action);
		
	}
	@Test
	public void testDecayModule() {
		behav1.setActivation(0.1);
		behav2.setActivation(0.5);
		as.receiveBehavior(behav1);
		as.receiveBehavior(behav2);

		as.decayModule(1);
		
		assertEquals(0.0,behav1.getActivation(),EPSILON);
		assertEquals(0.4,behav2.getActivation(),EPSILON);		
	}

	
//	@Test
//	public void testGetState() {
//		as.receiveBehavior(behav1);
//		as.receiveBehavior(behav2);
//		
//		Object[] state = (Object[])as.getState();
//		Collection<Behavior> content = (Collection<Behavior>)state[0];
//		
//		assertTrue("Problem with GetState", content.size()==2);
//	}

}