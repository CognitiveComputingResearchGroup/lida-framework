package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import static org.junit.Assert.*;

import java.util.HashMap;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockWorkspaceBufferImpl;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
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
	public void testRunThisFrameworkTask(){
		fail();
		//TODO init codelet with buffers
		//TODO add to sought content, readable content
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);

		codelet.setSoughtContent(soughtContent);
		codelet.runThisFrameworkTask();
		
		//TODO assert that writeable buffer has the content of the readable buffer.
		
	}
	
	@Test
	public void testHasSoughtContent() {
		codelet.setAssociatedModule(readableBuffer, ModuleUsage.TO_READ_FROM);
		
		codelet.setSoughtContent(soughtContent);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		
		assertTrue(codelet.hasSoughtContent(readableBuffer));
		
		soughtContent.addDefaultNode(node1);
		codelet.setSoughtContent(soughtContent);
		readableContent.addDefaultNode(node1);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		
		assertTrue(readableBuffer.getBufferContent(new HashMap<String, Object>()).containsNode(node1));
		assertTrue(codelet.hasSoughtContent(readableBuffer));
		
		soughtContent.addDefaultNode(node2);
		codelet.setSoughtContent(soughtContent);
		assertFalse(codelet.hasSoughtContent(readableBuffer));
		
		readableContent.addDefaultNode(node2);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		assertTrue(codelet.hasSoughtContent(readableBuffer));
		
		soughtContent.addDefaultLink(link1);
		assertFalse(codelet.hasSoughtContent(readableBuffer));
		
		readableContent.addDefaultLink(link1);
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		assertTrue(codelet.hasSoughtContent(readableBuffer));
		
		readableContent.addDefaultNode(factory.getNode());
		readableBuffer.addBufferContent((WorkspaceContent) readableContent);
		assertTrue(codelet.hasSoughtContent(readableBuffer));
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
