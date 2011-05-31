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
import edu.memphis.ccrg.lida.attentioncodelets.BasicAttentionCodelet;
import edu.memphis.ccrg.lida.framework.mockclasses.MockBroadcastListener;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;

/**
 * This is a JUnit class which can be used to test methods of the GlobalWorkspaceImpl class
 * @author Siminder Kaur
 */

public class GlobalWorkspaceImplTest {

	private GlobalWorkspaceImpl gw;
	private Coalition coalition, coalition2, coalition3;
	private NodeStructureImpl ns = new NodeStructureImpl();
	private NodeStructureImpl ns2 = new NodeStructureImpl();
	private NodeImpl n1, n2, n3;
	private LinkImpl l;
	private MockTaskSpawner ts;
	private MockBroadcastListener listener;
	private AttentionCodelet codelet = new BasicAttentionCodelet();

	@Before
	public void setUp() throws Exception {
		gw = new GlobalWorkspaceImpl();
		ts = new MockTaskSpawner();
		listener = new MockBroadcastListener();
		gw.setAssistingTaskSpawner(ts);

		n1 = new NodeImpl();
		n2 = new NodeImpl();
		n3 = new NodeImpl();
		n1.setId(1);
		n2.setId(2);
		n3.setId(3);
		l = new LinkImpl(n1, n2, new PamNodeImpl());

		ns.addDefaultNode(n1);
		ns.addDefaultNode(n2);
		ns.addDefaultNode(n3);
		ns.addDefaultLink(l);

		ns2.addDefaultNode(n1);
		ns2.addDefaultNode(n2);
		ns2.addDefaultLink(l);

		coalition = new CoalitionImpl(ns, 0.8,codelet);
		coalition2 = new CoalitionImpl(ns2, 0.9,codelet);
		coalition3 = new CoalitionImpl(ns, 0.1,codelet);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetModuleContent() {
		gw.addCoalition(coalition);
		gw.addCoalition(coalition2);
		Collection<Coalition> content = (Collection<Coalition>) gw.getModuleContent();

		assertTrue(content.contains(coalition));
		assertTrue(content.contains(coalition2));
	}

	@Test
	public void testInit() {
		gw.init();

		assertEquals(1, ts.getRunningTasks().size());
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
		Collection<Coalition> content = (Collection<Coalition>) gw.getModuleContent();
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
				
		Collection<Coalition> content = (Collection<Coalition>) gw.getModuleContent();
		assertEquals(1, content.size());

		assertEquals(coalition, trigger.coalitions.iterator().next());
		assertEquals(1, trigger.coalitions.size());
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
		
		assertTrue(NodeStructureImpl.compareNodeStructures((NodeStructure)coalition2.getContent(), 
								(NodeStructure)listener.content));
		assertTrue(trigger.wasReset);
		assertTrue(trigger2.wasReset);
		
		Collection<Coalition> content = (Collection<Coalition>) gw.getModuleContent();
		assertEquals(2, content.size());
		assertFalse(content.contains(coalition2));
	}

}
