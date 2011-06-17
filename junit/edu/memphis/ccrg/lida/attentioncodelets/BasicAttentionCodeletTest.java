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

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockWorkspaceBufferImpl;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;

public class BasicAttentionCodeletTest {

	private BasicAttentionCodelet codelet;
	private MockWorkspaceBufferImpl csm;
	private MockGlobalWorkspaceImpl globalWorkspace;
	private NodeStructure csmContent, soughtContent; 
	private Node node1, node2;
	private Link link1;
	private static ElementFactory factory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		factory = ElementFactory.getInstance();
	}

	@Before
	public void setUp() throws Exception {
		codelet = new BasicAttentionCodelet();
		csm = new MockWorkspaceBufferImpl();
		globalWorkspace = new MockGlobalWorkspaceImpl();
		
		codelet.setAssociatedModule(csm, "");
		codelet.setAssociatedModule(globalWorkspace, "");
		
		csmContent = new NodeStructureImpl();
		soughtContent = new NodeStructureImpl();
		node1 = factory.getNode();
		node2 = factory.getNode();
		link1 = factory.getLink(node1, node2, PerceptualAssociativeMemoryImpl.NONE);
	}
	
	@Test
	public void testHasSoughtContent() {
		codelet.setSoughtContent(soughtContent);
		csm.addBufferContent((WorkspaceContent) csmContent);
		
		assertTrue(codelet.bufferContainsSoughtContent(csm));
		
		soughtContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node1);
		csm.addBufferContent((WorkspaceContent) csmContent);
		
		assertTrue(csm.getBufferContent(new HashMap<String, Object>()).containsNode(node1));
		assertTrue(codelet.bufferContainsSoughtContent(csm));
		
		soughtContent.addDefaultNode(node2);
		assertFalse(codelet.bufferContainsSoughtContent(csm));
		
		csmContent.addDefaultNode(node2);
		csm.addBufferContent((WorkspaceContent) csmContent);
		assertTrue(codelet.bufferContainsSoughtContent(csm));
		
		soughtContent.addDefaultLink(link1);
		assertFalse(codelet.bufferContainsSoughtContent(csm));
		
		csmContent.addDefaultLink(link1);
		csm.addBufferContent((WorkspaceContent) csmContent);
		assertTrue(codelet.bufferContainsSoughtContent(csm));
		
		csmContent.addDefaultNode(factory.getNode());
		csm.addBufferContent((WorkspaceContent) csmContent);
		assertTrue(codelet.bufferContainsSoughtContent(csm));
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

}