package edu.memphis.ccrg.lida.attentioncodelets;

import static org.junit.Assert.*;

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
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
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
		codelet.setAssociatedModule(csm, "");
		codelet.setAssociatedModule(globalWorkspace, "");
		
		csm = new MockWorkspaceBufferImpl();
		globalWorkspace = new MockGlobalWorkspaceImpl();
		
		csmContent = new NodeStructureImpl();
		soughtContent = new NodeStructureImpl();
		node1 = factory.getNode();
		node2 = factory.getNode();
		link1 = factory.getLink(node1, node2, new PamNodeImpl());
	}
	
	@Test
	public void testHasSoughtContent() {
		codelet.setSoughtContent(soughtContent);
		csm.addBufferContent((WorkspaceContent) csmContent);
		
		assertTrue(codelet.hasSoughtContent(csm));
		
		soughtContent.addDefaultNode(node1);
		csmContent.addDefaultNode(node1);
		csm.addBufferContent((WorkspaceContent) csmContent);
		
		assertTrue(csm.getBufferContent(new HashMap<String, Object>()).containsNode(node1));
		assertTrue(codelet.hasSoughtContent(csm));
		
		soughtContent.addDefaultNode(node2);
		assertFalse(codelet.hasSoughtContent(csm));
		
		csmContent.addDefaultNode(node2);
		csm.addBufferContent((WorkspaceContent) csmContent);
		assertTrue(codelet.hasSoughtContent(csm));
		
		soughtContent.addDefaultLink(link1);
		assertFalse(codelet.hasSoughtContent(csm));
		
		csmContent.addDefaultLink(link1);
		csm.addBufferContent((WorkspaceContent) csmContent);
		assertTrue(codelet.hasSoughtContent(csm));
		
		csmContent.addDefaultNode(factory.getNode());
		csm.addBufferContent((WorkspaceContent) csmContent);
		assertTrue(codelet.hasSoughtContent(csm));
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
		assertEquals(1, ns.getNodeCount());
		assertEquals(1, csmContent.getNodeCount());
		assertTrue(NodeStructureImpl.compareNodeStructures(csmContent, ns));
	}

}