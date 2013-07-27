/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspacebuffers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;

public class BroadcastQueueImplTest {

	private BroadcastQueueImpl broadcastQueue;
	private NodeStructureImpl content;
	private NodeStructureImpl content2;

	private Node n1;
	private Node n2;
	private Node n3;
	private Node n4;

	private static ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		broadcastQueue = new BroadcastQueueImpl();
		content = new NodeStructureImpl();
		content2 = new NodeStructureImpl();

		n1 = factory.getNode();
		n1.setActivation(0.1);
		content.addDefaultNode(n1);
		n2 = factory.getNode();
		n2.setActivation(0.2);
		content.addDefaultNode(n2);

		n3 = factory.getNode();
		n3.setActivation(0.3);
		content2.addDefaultNode(n3);
		n4 = factory.getNode();
		n4.setActivation(0.4);
		content2.addDefaultNode(n4);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetModuleContent() {
		broadcastQueue.addBufferContent(content);
		broadcastQueue.addBufferContent(content2);

		List<NodeStructure> c = (List<NodeStructure>) broadcastQueue
				.getModuleContent((Object[]) null);
		assertTrue(NodeStructureImpl.compareNodeStructures(c.get(0), content));
		assertTrue(NodeStructureImpl.compareNodeStructures(c.get(1), content2));
	}

	@Test
	public void testDecayModule() {
		broadcastQueue.addBufferContent(content);
		broadcastQueue.addBufferContent(content2);
		Map<String, Object> params = new HashMap<String, Object>();

		broadcastQueue.decayModule(1);

		params.put("position", 1);
		NodeStructure ns1 = broadcastQueue.getBufferContent(params);
		params.put("position", 0);
		NodeStructure ns2 = broadcastQueue.getBufferContent(params);

		Node resN1 = ns1.getNode(n1.getId());
		assertNull(resN1);

		Node resN2 = ns1.getNode(n2.getId());
		assertTrue(resN2.getActivation() < 0.2);

		Node resN3 = ns2.getNode(n3.getId());
		assertTrue(resN3.getActivation() < 0.3);

		Node resN4 = ns2.getNode(n4.getId());
		assertTrue(resN4.getActivation() < 0.4);

		broadcastQueue.decayModule(1);

		params.put("position", 1);
		ns1 = broadcastQueue.getBufferContent(params);
		assertNull(ns1);

		params.put("position", 0);
		ns2 = broadcastQueue.getBufferContent(params);
		assertNotNull(ns2);

		resN3 = ns2.getNode(n3.getId());
		assertTrue(resN3.getActivation() < 0.2);

		resN4 = ns2.getNode(n4.getId());
		assertTrue(resN4.getActivation() < 0.3);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReceiveBroadcast() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("workspace.broadcastQueueCapacity", -2);
		broadcastQueue.init(params);

		Coalition c = new CoalitionImpl(content, null);
		broadcastQueue.receiveBroadcast(c);

		NodeStructure ns = broadcastQueue.getPositionContent(0);
		assertNotNull(ns);
		assertEquals(2, ns.getNodeCount());

		List<NodeStructure> queue = (List<NodeStructure>) broadcastQueue
				.getModuleContent(0);
		assertEquals(1, queue.size());
		NodeStructure actual = queue.get(0);
		assertTrue(actual.containsNode(n1));
		assertTrue(actual.containsNode(n2));
		assertEquals(2, actual.getLinkableCount());

		params.put("workspace.broadcastQueueCapacity", 2);
		broadcastQueue.init(params);

		c = new CoalitionImpl(content2, null);
		broadcastQueue.receiveBroadcast(c);

		queue = (List<NodeStructure>) broadcastQueue.getModuleContent(0);
		assertEquals(2, queue.size());
		actual = queue.get(0);
		assertTrue(actual.containsNode(n3));
		assertTrue(actual.containsNode(n4));
		assertEquals(2, actual.getLinkableCount());

		c = new CoalitionImpl(new NodeStructureImpl(), null);
		broadcastQueue.receiveBroadcast(c);

		queue = (List<NodeStructure>) broadcastQueue.getModuleContent(0);
		assertEquals(2, queue.size());
		actual = queue.get(0);
		assertEquals(0, actual.getLinkableCount());

		actual = queue.get(1);
		assertTrue(actual.containsNode(n3));
		assertTrue(actual.containsNode(n4));
		assertEquals(2, actual.getLinkableCount());
	}

	@Test
	public void testGetBufferContent() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("position", 0);
		Object o = broadcastQueue.getBufferContent(params);
		assertNull(o);

		broadcastQueue.addBufferContent(content);// 1
		broadcastQueue.addBufferContent(content2);// 0

		o = broadcastQueue.getBufferContent(null);
		assertNull(o);

		params = new HashMap<String, Object>();
		o = broadcastQueue.getBufferContent(params);
		assertNull(o);

		params.put("position", "position");
		o = broadcastQueue.getBufferContent(params);
		assertNull(o);

		params.put("position", -1);
		o = broadcastQueue.getBufferContent(params);
		assertNull(o);

		params.put("position", 2);
		o = broadcastQueue.getBufferContent(params);
		assertNull(o);

		params.put("position", 0);
		o = broadcastQueue.getBufferContent(params);
		assertTrue(NodeStructureImpl.compareNodeStructures(content,
				(NodeStructure) o));

		params.put("position", 1);
		o = broadcastQueue.getBufferContent(params);
		assertTrue(NodeStructureImpl.compareNodeStructures(content2,
				(NodeStructure) o));
	}
}
