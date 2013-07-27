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
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.ns.ProceduralMemoryImpl;

public class BasicActionSelectionTest {

	private static final ElementFactory factory = ElementFactory.getInstance();
	private static final double EPSILON = 1e-5;
	private BasicActionSelection as;
	private Behavior behav1, behav2;
	private MockSensoryMotorMemory smm = new MockSensoryMotorMemory();
	private MockTaskSpawner ts = new MockTaskSpawner();
	private Scheme scheme1, scheme2;

	private Action action1 = new ActionImpl();
	private Action action2 = new ActionImpl();

	@Before
	public void setUp() throws Exception {
		ProceduralMemoryImpl pm = new ProceduralMemoryImpl();
		scheme1 = pm.getNewScheme(action1);
		scheme1.setLabel("scheme1");
		scheme2 = pm.getNewScheme(action2);
		scheme2.setLabel("scheme2");
		as = new BasicActionSelection();
		as.setAssistingTaskSpawner(ts);
		as.init();
		behav1 = factory.getBehavior(scheme1);
		behav2 = factory.getBehavior(scheme2);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetModuleContent() {
		as.receiveBehavior(behav1);
		Collection<Behavior> content = (Collection<Behavior>) as
				.getModuleContent("behaviors");

		assertTrue("Problem with GetModuleContent", content.size() == 1);
		assertTrue("Problem with GetModuleContent", content.contains(behav1));
		assertTrue("Problem with GetModuleContent", !content.contains(behav2));
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReceiveBehavior() {
		as.receiveBehavior(behav1);
		as.receiveBehavior(behav2);
		Collection<Behavior> content = (Collection<Behavior>) as
				.getModuleContent("behaviors");

		assertTrue("Problem with ReceiveBehavior", content.size() == 2);
		assertTrue("Problem with ReceiveBehavior", content.contains(behav1));
		assertTrue("Problem with ReceiveBehavior", content.contains(behav2));
	}

	@Test
	public void testSelectAction() {
		as.addListener(smm);
		behav1.setActivation(0.78);
		behav2.setActivation(0.79);

		as.receiveBehavior(behav1);
		as.receiveBehavior(behav2);

		assertNull(smm.action);
		assertFalse(smm.actionReceived);

		as.selectBehavior(as.getBehaviors(), as.getThreshold());

		assertNull(smm.action);
		assertFalse(smm.actionReceived);

		behav2.setActivation(0.80);
		Behavior res = as.selectBehavior(as.getBehaviors(), as.getThreshold());

		assertEquals(behav2, res);
	}

	@Test
	public void testDecayModule() {
		scheme1.setBaseLevelActivation(0.1);
		scheme2.setBaseLevelActivation(0.5);
		as.receiveBehavior(behav1);
		as.receiveBehavior(behav2);

		as.decayModule(1);

		assertEquals(0.0, behav1.getActivation(), EPSILON);
		assertEquals(0.4, behav2.getActivation(), EPSILON);
	}

}