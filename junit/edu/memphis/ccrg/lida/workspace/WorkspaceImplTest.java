/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.workspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.mockclasses.MockWorkspaceBufferImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl;

/**
 * This class is the JUnit test for <code>WorkspaceImpl</code> class.
 * 
 * @author Rodrigo Silva-Lugo
 * @author Daqi Dong
 * @author Ryan J. McCall
 */
public class WorkspaceImplTest {

	private static final ElementFactory factory = ElementFactory.getInstance();

	private WorkspaceImpl workspace;
	private MockWorkspaceBufferImpl eBuffer;
	private NodeStructure content;
	private Node node1, node2;

	/**
     *
     */
	@Before
	public void setUp() {
		workspace = new WorkspaceImpl();
		eBuffer = new MockWorkspaceBufferImpl();
		eBuffer.setModuleName(ModuleName.EpisodicBuffer);
		content = new NodeStructureImpl();
		node1 = factory.getNode();
		node1.setId(8);
		node2 = factory.getNode();
	}

	/**
	 * Test of addListener method, of class WorkspaceImpl.
	 */
	@Test
	public void testAddListener() {
		// workspace listener
		MockWorkspaceListener listener = new MockWorkspaceListener();
		workspace.addListener(listener);
		workspace.addSubModule(eBuffer);
		content.addDefaultNode(node1);

		workspace.receiveLocalAssociation(content);

		assertEquals(ModuleName.EpisodicBuffer, listener.originatingBuffer);
		assertTrue(listener.content.containsNode(node1));

		// cue listener
		MockCueListener cueListener = new MockCueListener();
		workspace.addListener(cueListener);
		content.addDefaultNode(node2);

		workspace.cueEpisodicMemories(content);

		NodeStructure ns = cueListener.ns;
		assertNotNull(ns);
		assertTrue(ns.containsNode(node1));
		assertTrue(ns.containsNode(node2));
	}

	/**
	 * Test of addCueListener method, of class WorkspaceImpl.
	 */
	@Test
	public void testAddCueListener() {
		MockCueListener cueListener = new MockCueListener();
		workspace.addCueListener(cueListener);
		content.addDefaultNode(node2);

		workspace.cueEpisodicMemories(content);

		NodeStructure ns = cueListener.ns;
		assertNotNull(ns);
		assertTrue(ns.containsNode(node2));
	}

	/**
	 * Test of addWorkspaceListener method, of class WorkspaceImpl.
	 */
	@Test
	public void testAddWorkspaceListener() {
		MockWorkspaceListener listener = new MockWorkspaceListener();
		workspace.addListener(listener);
		workspace.addSubModule(eBuffer);
		content.addDefaultNode(node1);

		workspace.receiveLocalAssociation(content);

		assertEquals(ModuleName.EpisodicBuffer, listener.originatingBuffer);
		assertTrue(listener.content.containsNode(node1));
	}

	/**
	 * Test of cueEpisodicMemories method, of class WorkspaceImpl.
	 */
	@Test
	public void testCueEpisodicMemories() {
		MockCueListener cueListener = new MockCueListener();
		workspace.addCueListener(cueListener);
		content.addDefaultNode(node1);
		content.addDefaultNode(node2);

		workspace.cueEpisodicMemories(content);

		NodeStructure ns = cueListener.ns;
		assertNotNull(ns);
		assertTrue(ns.containsNode(node1));
		assertTrue(ns.containsNode(node2));
	}

	/**
	 * Test of receiveBroadcast method, of class WorkspaceImpl.
	 */
	@Test
	public void testReceiveBroadcast() {
		MockBroadcastQueueImpl broadcastQueue = new MockBroadcastQueueImpl();
		broadcastQueue.setModuleName(ModuleName.BroadcastQueue);
		workspace.addSubModule(broadcastQueue);

		content.addDefaultNode(node2);
		Coalition c = new CoalitionImpl(content, null);
		workspace.receiveBroadcast(c);
		NodeStructure actual = (NodeStructure) broadcastQueue.broadcastContent;
		assertNotNull(actual);
		assertTrue(actual.containsNode(node2));
		assertTrue(NodeStructureImpl.compareNodeStructures(content, actual));
	}

	/**
	 * Test of receiveLocalAssociation method, of class WorkspaceImpl.
	 */
	@Test
	public void testReceiveLocalAssociation() {
		content.addDefaultNode(node1);
		eBuffer.addBufferContent((WorkspaceContent) content);
		workspace.addSubModule(eBuffer);

		MockWorkspaceListener listener = new MockWorkspaceListener();
		workspace.addWorkspaceListener(listener);

		NodeStructure localAssociation = new NodeStructureImpl();
		Node node3 = factory.getNode();
		node3.setId(9);
		localAssociation.addDefaultNode(node3);

		workspace.receiveLocalAssociation(localAssociation);

		assertEquals(ModuleName.EpisodicBuffer, listener.originatingBuffer);
		assertTrue(listener.content.containsNode(node1));
		assertTrue(listener.content.containsNode(node3));

		assertTrue(eBuffer.content.containsNode(node3));
		// System.out.println(eBuffer.content.toString());
		// System.out.println(listener.content.toString());
	}

	@Test
	public void testReceiveNodeStructurePercept() {
		MockWorkspaceBufferImpl buffer = new MockWorkspaceBufferImpl();
		buffer.setModuleName(ModuleName.PerceptualBuffer);
		workspace.addSubModule(buffer);
		content.addDefaultNode(node1);
		assertEquals(0, buffer.getBufferContent(null).getLinkableCount());

		workspace.receivePercept(content);

		NodeStructure ns = buffer.getBufferContent(null);
		assertTrue(ns.containsNode(node1));
		assertEquals(1, ns.getLinkableCount());
		assertEquals(1, ns.getNodeCount());
		assertEquals(0, ns.getLinkCount());
	}

	@Test
	public void testReceivePerceptNode() {
		MockWorkspaceBufferImpl buffer = new MockWorkspaceBufferImpl();
		buffer.setModuleName(ModuleName.PerceptualBuffer);
		workspace.addSubModule(buffer);
		assertEquals(0, buffer.getBufferContent(null).getLinkableCount());

		workspace.receivePercept(node2);

		NodeStructure ns = buffer.getBufferContent(null);
		assertTrue(ns.containsNode(node2));
		assertEquals(1, ns.getLinkableCount());
		assertEquals(1, ns.getNodeCount());
		assertEquals(0, ns.getLinkCount());
	}

	@Test
	public void testReceivePerceptLink() {
		MockWorkspaceBufferImpl buffer = new MockWorkspaceBufferImpl();
		buffer.setModuleName(ModuleName.PerceptualBuffer);
		workspace.addSubModule(buffer);
		assertEquals(0, buffer.getBufferContent(null).getLinkableCount());

		workspace.receivePercept(node1);
		workspace.receivePercept(node2);

		NodeStructure ns = buffer.getBufferContent(null);
		assertTrue(ns.containsNode(node1));
		assertTrue(ns.containsNode(node2));
		assertEquals(2, ns.getLinkableCount());
		assertEquals(2, ns.getNodeCount());
		assertEquals(0, ns.getLinkCount());

		Link l12 = factory.getLink(node1, node2,
				PerceptualAssociativeMemoryNSImpl.FEATURE);
		workspace.receivePercept(l12);

		ns = buffer.getBufferContent(null);
		assertTrue(ns.containsLink(l12));
		assertTrue(ns.containsNode(node1));
		assertTrue(ns.containsNode(node2));
		assertEquals(3, ns.getLinkableCount());
		assertEquals(2, ns.getNodeCount());
		assertEquals(1, ns.getLinkCount());
	}

	@Test
	public void testGetModuleContent() {
	}

	@Test
	public void testLearn() {
	}

	@Test
	public void testInit() {
	}
}

class MockCueListener implements CueListener {
	public NodeStructure ns;

	@Override
	public void receiveCue(NodeStructure cue) {
		this.ns = cue;
	}
}

class MockWorkspaceListener implements WorkspaceListener {
	public ModuleName originatingBuffer;
	public WorkspaceContent content;

	@Override
	public void receiveWorkspaceContent(ModuleName originatingBuffer,
			WorkspaceContent content) {
		this.originatingBuffer = originatingBuffer;
		this.content = content;
	}
}