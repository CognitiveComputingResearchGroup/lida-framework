/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * This is a JUnit class which can be used to test methods of the NodeStructureImpl class
 * @author Siminder Kaur
 * 
 */

public class NodeStructureImplTest extends TestCase{
	NodeImpl node1,node2,node3,node4;
	LinkImpl link1,link2,link3;	
	Map<Long, Node> nodes;
	LinkCategory linktype1,linktype2;	
	NodeStructureImpl nodeStructure1,nodeStructure2,nodeStructure3;
	NodeStructure ns;
	Set<Link> links;

	/**
	 * This method is called before running each test case to initialize the objects
	 * 
	 */
	@Override
	@Before
	public void setUp() throws Exception {	
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();
		node4 = new NodeImpl();
		link1 = new LinkImpl();
		link2 = new LinkImpl();
		link3 = new LinkImpl();
		linktype1 = LinkCategory.Parent ;
		linktype2 = LinkCategory.Child ;			 
		nodeStructure1 = new NodeStructureImpl();	
		nodeStructure2 = new NodeStructureImpl();
		nodeStructure3 = new NodeStructureImpl();
		links = new HashSet<Link>();

		node1.setId(1);		
		node1.setLabel("red");
		node1.setActivation(1);
//		node1.setImportance(1);

		node2.setId(2);
		node2.setLabel("blue");
		node2.setActivation(2);
//		node2.setImportance(2);

		node3.setId(3);
		node2.setLabel("purple");
		node2.setActivation(3);
//		node2.setImportance(3);
		
		node4.setId(3);
		
		link1.setSource(node1);
		link1.setSink(node2);
		link1.setCategory(linktype1);
			
		link2.setSource(node2);
		link2.setSink(node3);
		link2.setCategory(linktype2);
		
		link3.setSource(node2);
		link3.setSink(node4);
		link3.setCategory(linktype2);
	}

	/**
	 * This method is called after running each test case
	 */
	@Override
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * This method is used to test the NodeStructureImpl.addLink() method
	 */
	@Test
	public void testAddLink() {	
		nodeStructure1.addNode(node1);	
		nodeStructure1.addNode(node2);	
		nodeStructure1.addLink(link1);
		assertTrue("Problem with addLink", nodeStructure1.containsLink(link1));
		
		nodeStructure1.addNode(node3);	
		nodeStructure1.addLink(link2);
		assertTrue("Problem with addLink", nodeStructure1.containsLink(link2));
	}

	/**
	 * This method is used to test the NodeStructureImpl.addLinks() method
	 */
	@Test
	public void testAddLinks() {
		nodeStructure2.addNodes(nodeStructure1.getNodes());		
		nodeStructure2.addLinks(nodeStructure1.getLinks());		
		assertTrue("Problem with addLinks", nodeStructure2.getLinks().containsAll(nodeStructure1.getLinks()));		
	}

	/**
	 * This method is used to test the NodeStructureImpl.addNode() method
	 */
	@Test
	public void testAddNode() {	
		nodeStructure1.addNode(node1);	
		assertTrue("Problem with addNode", nodeStructure1.containsNode(node1));
		
		nodeStructure1.addNode(node2);	
		assertTrue("Problem with addNode", nodeStructure1.containsNode(node2));
		
		nodeStructure1.addNode(node3);
		assertTrue("Problem with addNode", nodeStructure1.containsNode(node3));	

		node2.setActivation(3);
		nodeStructure1.addNode(node2);
		assertTrue("Problem with addNode", (nodeStructure1.containsNode(node2) && node2.getActivation()==3));		
	}

	/**
	 * This method is used to test the NodeStructureImpl.addNodes() method
	 */
	@Test
	public void testAddNodes() {	
		nodeStructure2.addNodes(nodeStructure1.getNodes());
		assertTrue("Problem with addNodes", nodeStructure2.getNodes().containsAll(nodeStructure1.getNodes()));				
	}

	/**
	 * This method is used to test the NodeStructureImpl.deleteLink() method
	 */
	@Test
	public void testDeleteLink() {
		nodeStructure1.addNode(node1);	
		nodeStructure1.addNode(node2);	
		nodeStructure1.addNode(node3);	
		
		nodeStructure1.addLink(link1);					
		nodeStructure1.addLink(link2);
		
		nodeStructure1.removeLink(link1);
		assertTrue("Problem with deleteLink", !nodeStructure1.containsLink(link1));		
	}

	/**
	 * This method is used to test the NodeStructureImpl.deleteNode() method
	 */
	@Test
	public void testDeleteNode() {
		nodeStructure1.addNode(node1);	
		nodeStructure1.addNode(node2);	
		nodeStructure1.addNode(node3);
		
		nodeStructure1.removeNode(node3);
		assertTrue("Problem with deleteNode", !nodeStructure1.containsNode(node3));	
	}

//	/**
//	 * This method is used to test the NodeStructureImpl.clearNodes() method
//	 */
//	@Test
//	public void testClearNodes() {
//		nodeStructure1.addNode(node1);	
//		nodeStructure1.addNode(node2);	
//		nodeStructure1.addNode(node3);	
//		
//		nodeStructure1.addLink(link1);					
//		nodeStructure1.addLink(link2);
//		
//		nodeStructure1.clearNodes();
//		assertTrue("Problem with clearNodes", nodeStructure1.getNodeCount()==0);	
//	}	

	/**
	 * This method is used to test the NodeStructureImpl.copy() method
	 */
	@Test
	public void testCopy() {
		ns = null;		
		nodeStructure3.addNode(node1);
		nodeStructure3.addNode(node2);
		nodeStructure3.addLink(link1);		
		
		ns = nodeStructure3.copy();		
			
		assertEquals("Problem with copy", ns, nodeStructure3);	
		
	}	

	/**
	 * This method is used to test the NodeStructureImpl.getLinksByType() method
	 */
	@Test
	public void testGetLinksByType() {		
		links = nodeStructure1.getLinksByType(linktype1);
		for (Link l : links) {
			assertEquals("Problem with GetLinksByType", l.getCategory(),linktype1);			
			break;				
		}	
	}
	
	/**
	 * This method is used to test the NodeStructureImpl.equals() method
	 */
	@Test
	public void testEquals() {
		
		nodeStructure1.addNode(node1);
		nodeStructure1.addNode(node2);
		nodeStructure1.addNode(node3);
		nodeStructure1.addLink(link1);
		nodeStructure1.addLink(link2);
		
		nodeStructure2.addNode(node1);
		nodeStructure2.addNode(node2);
		nodeStructure2.addNode(node3);
		nodeStructure2.addLink(link1);
		nodeStructure2.addLink(link2);
					
		assertEquals("Problem with equals", nodeStructure1, nodeStructure2);			
	}	
}
