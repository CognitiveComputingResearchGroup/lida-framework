/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockWorkspaceBufferImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;

public class BasicStructureBuildingCodeletTest {

	private BasicStructureBuildingCodelet codelet;
	private MockWorkspaceBufferImpl readableBuffer, writeableBuffer;
	private NodeStructure readableContent, soughtContent;
	private Node node1, node2;
	private Link link1;

	private static ElementFactory factory;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = ElementFactory.getInstance();
	}

	@Before
	public void setUp() throws Exception {
		codelet = new BasicStructureBuildingCodelet();
		readableBuffer = new MockWorkspaceBufferImpl();
		writeableBuffer = new MockWorkspaceBufferImpl();

		readableContent = new NodeStructureImpl();
		soughtContent = new NodeStructureImpl();
		node1 = factory.getNode();
		node2 = factory.getNode();
		link1 = factory.getLink(node1, node2, new PamNodeImpl());
	}

	@Test
	public void testRunThisFrameworkTask() {
		codelet.setAssociatedModule(readableBuffer, ModuleUsage.TO_READ_FROM);
		codelet.setAssociatedModule(writeableBuffer, ModuleUsage.TO_WRITE_TO);

		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		codelet.setSoughtContent(soughtContent);

		codelet.runThisFrameworkTask();

		NodeStructure ns = writeableBuffer.content;
		assertNotNull(ns);
		assertEquals(0, ns.getLinkableCount());

		soughtContent.addDefaultNode(node1);
		codelet.setSoughtContent(soughtContent);
		writeableBuffer.content = null;

		codelet.runThisFrameworkTask();

		assertNull(writeableBuffer.content);

		readableContent.addDefaultNode(node1);
		readableContent.addDefaultNode(node2);
		readableContent.addDefaultLink(link1);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		writeableBuffer.content = null;

		codelet.runThisFrameworkTask();

		assertNotNull(ns);
		assertEquals(3, ns.getLinkableCount());
		assertTrue(ns.containsLink(link1));
		assertTrue(ns.containsNode(node1));
		assertTrue(ns.containsNode(node2));

		soughtContent.addDefaultNode(node2);
		soughtContent.addDefaultLink(link1);
		codelet.setSoughtContent(soughtContent);
		writeableBuffer.content = null;

		codelet.runThisFrameworkTask();

		assertNotNull(ns);
		assertEquals(3, ns.getLinkableCount());
		assertTrue(ns.containsLink(link1));
		assertTrue(ns.containsNode(node1));
		assertTrue(ns.containsNode(node2));
	}

	@Test
	public void testHasSoughtContent() {
		codelet.setAssociatedModule(readableBuffer, ModuleUsage.TO_READ_FROM);

		codelet.setSoughtContent(soughtContent);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);

		assertTrue(codelet.bufferContainsSoughtContent(readableBuffer));

		soughtContent.addDefaultNode(node1);
		codelet.setSoughtContent(soughtContent);
		readableContent.addDefaultNode(node1);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);

		assertTrue(readableBuffer.getBufferContent(
				new HashMap<String, Object>()).containsNode(node1));
		assertTrue(codelet.bufferContainsSoughtContent(readableBuffer));

		soughtContent.addDefaultNode(node2);
		codelet.setSoughtContent(soughtContent);
		assertFalse(codelet.bufferContainsSoughtContent(readableBuffer));

		readableContent.addDefaultNode(node2);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		assertTrue(codelet.bufferContainsSoughtContent(readableBuffer));

		soughtContent.addDefaultLink(link1);
		assertFalse(codelet.bufferContainsSoughtContent(readableBuffer));

		readableContent.addDefaultLink(link1);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		assertTrue(codelet.bufferContainsSoughtContent(readableBuffer));

		readableContent.addDefaultNode(factory.getNode());
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		assertTrue(codelet.bufferContainsSoughtContent(readableBuffer));
	}

	@Test
	public void testRetrieveWorkspaceContent() {
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);

		NodeStructure ns = codelet.retrieveWorkspaceContent(readableBuffer);
		assertNotNull(ns);
		assertEquals(0, ns.getLinkableCount());
		assertEquals(0, readableContent.getLinkableCount());
		assertTrue(NodeStructureImpl.compareNodeStructures(readableContent, ns));

		readableContent.addDefaultNode(node1);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);

		ns = codelet.retrieveWorkspaceContent(readableBuffer);
		assertNotNull(ns);
		assertEquals(1, ns.getNodeCount());
		assertEquals(1, readableContent.getNodeCount());
		assertTrue(NodeStructureImpl.compareNodeStructures(readableContent, ns));
	}

}
