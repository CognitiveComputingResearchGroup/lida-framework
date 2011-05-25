/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class BroadcastQueueImplTest {

	private BroadcastQueueImpl queue;
	private NodeStructureImpl content;
	private NodeStructureImpl content2;
	
	private Node n1;
	private Node n2;
	private Node n3;
	private Node n4;
	@Before
	public void setUp() throws Exception {
		queue = new BroadcastQueueImpl();
		content = new NodeStructureImpl();
		content2 = new NodeStructureImpl();
		
		n1 = new NodeImpl();
		n1.setId(1);
		n1.setActivation(0.1);
		content.addDefaultNode(n1);
		n2 = new NodeImpl();
		n2.setId(2);
		n2.setActivation(0.2);
		content.addDefaultNode(n2);
		
		n3 = new NodeImpl();
		n3.setId(3);
		n3.setActivation(0.3);
		content2.addDefaultNode(n3);
		n4 = new NodeImpl();
		n4.setId(4);
		n4.setActivation(0.4);
		content2.addDefaultNode(n4);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetModuleContent() {
		queue.addBufferContent(content);
		queue.addBufferContent(content2);
		
		List<NodeStructure> c = (List<NodeStructure>) queue.getModuleContent((Object[])null);
		assertTrue(NodeStructureImpl.compareNodeStructures(c.get(0), content));
		assertTrue(NodeStructureImpl.compareNodeStructures(c.get(1), content2));
	}

	@Test
	public void testDecayModule() {
		queue.addBufferContent(content);
		queue.addBufferContent(content2);
		
		queue.decayModule(1);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("position", 0);
		NodeStructure ns1 = queue.getBufferContent(params);
		params.put("position", 1);
		NodeStructure ns2 = queue.getBufferContent(params);
		
		Node resN1 = ns1.getNode(1);
		assertNull(resN1);
		
		Node resN2 = ns1.getNode(2);
		assertTrue(resN2.getActivation() < 0.2);
		
	}

	@Test
	public void testBroadcastQueueImpl() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveBroadcast() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddBufferContent() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetBufferContent() {
		fail("Not yet implemented");
	}

	@Test
	public void testLearn() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddListener() {
		fail("Not yet implemented");
	}

}
