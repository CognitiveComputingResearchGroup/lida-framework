/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.attentioncodelets.NeighborhoodAttentionCodelet;
import edu.memphis.ccrg.lida.framework.mockclasses.ExecutingMockTaskSpawner;
import edu.memphis.ccrg.lida.framework.mockclasses.MockBroadcastListener;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;

/**
 * This is a JUnit class which can be used to test methods of the
 * GlobalWorkspaceImpl class
 * 
 * @author Siminder Kaur
 * @author Ryan J. McCall
 */

public class GlobalWorkspaceImplTest {

	private static final ElementFactory factory = ElementFactory.getInstance();
	private GlobalWorkspaceImpl gw;
	private Coalition coalition, coalition2, coalition3;
	private NodeStructureImpl ns = new NodeStructureImpl();
	private NodeStructureImpl ns2 = new NodeStructureImpl();
	private NodeImpl n1, n2, n3;
	private LinkImpl l;
	private ExecutingMockTaskSpawner ts;
	private MockBroadcastListener listener;

	@Before
	public void setUp() throws Exception {
		gw = new GlobalWorkspaceImpl();
		ts = new ExecutingMockTaskSpawner();
		gw.setAssistingTaskSpawner(ts);
		listener = new MockBroadcastListener();

		n1 = new NodeImpl();
		n2 = new NodeImpl();
		n3 = new NodeImpl();
		n1.setId(1);
		n2.setId(2);
		n3.setId(3);
		l = (LinkImpl) factory.getLink(n1, n2, new PamNodeImpl());

		ns.addDefaultNode(n1);
		ns.addDefaultNode(n2);
		ns.addDefaultNode(n3);
		ns.addDefaultLink(l);

		ns2.addDefaultNode(n1);
		ns2.addDefaultNode(n2);
		ns2.addDefaultLink(l);

		AttentionCodelet codelet = new NeighborhoodAttentionCodelet();
		codelet.setBaseLevelActivation(0.8);
		coalition = new CoalitionImpl(ns, codelet);
		codelet = new NeighborhoodAttentionCodelet();
		codelet.setBaseLevelActivation(0.9);
		coalition2 = new CoalitionImpl(ns2, codelet);
		codelet = new NeighborhoodAttentionCodelet();
		codelet.setBaseLevelActivation(0.1);
		coalition3 = new CoalitionImpl(ns, codelet);

		ElementFactory factory = ElementFactory.getInstance();
		double threshold = 0.0;
		DecayStrategy ds = factory.getDefaultDecayStrategy();
		gw.setCoalitionDecayStrategy(ds);
		gw.setCoalitionRemovalThreshold(threshold);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetModuleContent() {
		gw.addCoalition(coalition);
		gw.addCoalition(coalition2);
		Collection<Coalition> content = (Collection<Coalition>) gw
				.getModuleContent();

		assertTrue(content.contains(coalition));
		assertTrue(content.contains(coalition2));
	}

	private static final double epsilon = 10e-9;

	@Test
	public void testInit() {
		gw.init();
		assertEquals(1, ts.getTasks().size());
		assertEquals(ElementFactory.getInstance().getDefaultDecayStrategy(), gw
				.getCoalitionDecayStrategy());
		assertEquals(0.0, gw.getCoalitionRemovalThreshold(), epsilon);
		assertEquals(40, gw.getRefractoryPeriod());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDecayModule() {
		coalition.setActivation(0.8);
		gw.addCoalition(coalition);
		coalition3.setActivation(0.00001);
		gw.addCoalition(coalition3);

		gw.taskManagerDecayModule(1);

		assertTrue(coalition.getActivation() < 0.8);
		Collection<Coalition> content = (Collection<Coalition>) gw
				.getModuleContent();
		assertEquals(1, content.size());
		assertTrue(content.contains(coalition));

		gw.taskManagerDecayModule(1000);

		content = (Collection<Coalition>) gw.getModuleContent();
		assertTrue(content.isEmpty());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddCoalition() {
		MockBroadcastTrigger trigger = new MockBroadcastTrigger();
		gw.addBroadcastTrigger(trigger);

		boolean result = gw.addCoalition(coalition);
		assertTrue(result);

		Collection<Coalition> content = (Collection<Coalition>) gw
				.getModuleContent();
		assertEquals(1, content.size());

		Coalition actual = trigger.coalitions.iterator().next();
		assertEquals(coalition, actual);
		assertEquals(1, trigger.coalitions.size());
		assertEquals(0.0, coalition.getActivatibleRemovalThreshold(), epsilon);
		assertEquals(ElementFactory.getInstance().getDefaultDecayStrategy(),
				coalition.getDecayStrategy());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testTriggerBroadcast() {
		MockBroadcastTrigger trigger = new MockBroadcastTrigger();
		gw.addBroadcastTrigger(trigger);

		MockBroadcastTrigger trigger2 = new MockBroadcastTrigger();
		gw.addBroadcastTrigger(trigger2);

		coalition.setActivation(0.8);
		coalition2.setActivation(0.9);
		coalition3.setActivation(0.1);
		gw.addCoalition(coalition);
		gw.addCoalition(coalition2);
		gw.addCoalition(coalition3);

		gw.addBroadcastListener(listener);

		//
		gw.triggerBroadcast(trigger);

		assertTrue(NodeStructureImpl.compareNodeStructures(
				(NodeStructure) coalition2.getContent(),
				(NodeStructure) listener.content));
		assertTrue(trigger.wasReset);
		assertTrue(trigger2.wasReset);

		Collection<Coalition> content = (Collection<Coalition>) gw
				.getModuleContent();
		assertEquals(2, content.size());
		assertFalse(content.contains(coalition2));
	}

}
