/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attentioncodelets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.GlobalInitializer;
import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockWorkspaceBufferImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;

public class NeighborhoodAttentionCodeletTest {

	private NeighborhoodAttentionCodelet codelet;
	private MockWorkspaceBufferImpl csm;
	private MockGlobalWorkspaceImpl globalWorkspace;
	private NodeStructure csmContent, soughtContent;
	private Node node1, node2;
	private Link link1;
	private static ElementFactory factory;
	private GlobalInitializer gInit = GlobalInitializer.getInstance();
	private Map<String, Object> params = new HashMap<String, Object>();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = ElementFactory.getInstance();
	}

	@Before
	public void setUp() throws Exception {
		codelet = new NeighborhoodAttentionCodelet();
		csm = new MockWorkspaceBufferImpl();
		globalWorkspace = new MockGlobalWorkspaceImpl();

		codelet.setAssociatedModule(csm, "");
		codelet.setAssociatedModule(globalWorkspace, "");

		csmContent = new NodeStructureImpl();
		soughtContent = new NodeStructureImpl();
		node1 = factory.getNode();
		node1.setLabel("node1");
		node2 = factory.getNode();
		node2.setLabel("node2");
		link1 = factory.getLink(node1, node2,
				PerceptualAssociativeMemoryNSImpl.NONE);

		gInit.setAttribute("node1", node1);
		gInit.setAttribute("node2", node2);
	}

	@Test
	public void testHasSoughtContent() {
		params.put("nodes", "");
		codelet.init(params);
		csm.addBufferContent((WorkspaceContent) csmContent);

		assertTrue(codelet.bufferContainsSoughtContent(csm));
	}

	@Test
	public void testHasSoughtContent1() {
		params.put("nodes", "node1");
		codelet.init(params);

		csmContent.addDefaultNode(node1);
		csm.addBufferContent((WorkspaceContent) csmContent);

		assertTrue(codelet.bufferContainsSoughtContent(csm));
	}

	@Test
	public void testHasSoughtContent2() {
		params.put("nodes", "node2");
		codelet.init(params);

		assertFalse(codelet.bufferContainsSoughtContent(csm));
	}

	@Test
	public void testRetrieveWorkspaceContent() {
		csm.addBufferContent((WorkspaceContent) csmContent);

		NodeStructure ns = codelet.retrieveWorkspaceContent(csm);
		assertNotNull(ns);
		assertEquals(0, ns.getLinkableCount());
		assertEquals(0, csmContent.getLinkableCount());
		assertTrue(NodeStructureImpl.compareNodeStructures(csmContent, ns));

		csmContent.addDefaultNode(node1);
		csm.addBufferContent((WorkspaceContent) csmContent);

		ns = codelet.retrieveWorkspaceContent(csm);
		assertNotNull(ns);
		assertEquals(0, ns.getNodeCount());
		assertEquals(1, csmContent.getNodeCount());
		assertFalse(NodeStructureImpl.compareNodeStructures(csmContent, ns));
	}

	@Test
	public void testRetrieveWorkspaceContent1() {
		params.put("nodes", "node1");
		params.put("retrievalDepth", 1);
		codelet.init(params);

		csmContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node2);
		csmContent.addDefaultLink(link1);
		csm.addBufferContent((WorkspaceContent) csmContent);

		NodeStructure ns = codelet.retrieveWorkspaceContent(csm);
		assertNotNull(ns);
		assertEquals(3, ns.getLinkableCount());
		assertEquals(1, ns.getLinkCount());
		assertEquals(2, ns.getNodeCount());
	}

	@Test
	public void testRetrieveWorkspaceContent2() {
		params.put("nodes", "node2");
		params.put("retrievalDepth", 1);
		codelet.init(params);

		csmContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node2);
		csmContent.addDefaultLink(link1);
		csm.addBufferContent((WorkspaceContent) csmContent);

		NodeStructure ns = codelet.retrieveWorkspaceContent(csm);
		assertNotNull(ns);
		assertEquals(3, ns.getLinkableCount());
		assertEquals(1, ns.getLinkCount());
		assertEquals(2, ns.getNodeCount());
	}

}