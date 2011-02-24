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
	NodeStructureImpl ns1,ns2,nodeStructure3;
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
		linktype1 = LinkCategoryNode.PARENT ;
		linktype2 = LinkCategoryNode.CHILD ;			 
		ns1 = new NodeStructureImpl();	
		ns2 = new NodeStructureImpl();
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
		ns1.addDefaultNode(node1);	
		ns1.addDefaultNode(node2);	
		ns1.addDefaultLink(link1);
		assertTrue("Problem with addLink", ns1.containsLink(link1));
		
		ns1.addDefaultNode(node3);	
		ns1.addDefaultLink(link2);
		assertTrue("Problem with addLink", ns1.containsLink(link2));
	}

	/**
	 * This method is used to test the NodeStructureImpl.addLinks() method
	 */
	@Test
	public void testAddLinks() {
		ns2.addDefaultNodes(ns1.getNodes());		
		ns2.addDefaultLinks(ns1.getLinks());		
		assertTrue("Problem with addLinks", ns2.getLinks().containsAll(ns1.getLinks()));		
	}

	/**
	 * This method is used to test the NodeStructureImpl.addNode() method
	 */
	@Test
	public void testAddNode() {	
		ns1.addDefaultNode(node1);	
		assertTrue("Problem with addNode", ns1.containsNode(node1));
		
		ns1.addDefaultNode(node2);	
		assertTrue("Problem with addNode", ns1.containsNode(node2));
		
		ns1.addDefaultNode(node3);
		assertTrue("Problem with addNode", ns1.containsNode(node3));	

		node2.setActivation(3);
		ns1.addDefaultNode(node2);
		assertTrue("Problem with addNode", (ns1.containsNode(node2) && node2.getActivation()==3));		
	}

	/**
	 * This method is used to test the NodeStructureImpl.addNodes() method
	 */
	@Test
	public void testAddNodes() {	
		ns2.addDefaultNodes(ns1.getNodes());
		assertTrue("Problem with addNodes", ns2.getNodes().containsAll(ns1.getNodes()));				
	}

	/**
	 * This method is used to test the NodeStructureImpl.deleteLink() method
	 */
	@Test
	public void testDeleteLink() {
		ns1.addDefaultNode(node1);	
		ns1.addDefaultNode(node2);	
		ns1.addDefaultNode(node3);	
		
		ns1.addDefaultLink(link1);					
		ns1.addDefaultLink(link2);
		
		ns1.removeLink(link1);
		assertTrue("Problem with deleteLink", !ns1.containsLink(link1));		
	}

	/**
	 * This method is used to test the NodeStructureImpl.deleteNode() method
	 */
	@Test
	public void testDeleteNode() {
		ns1.addDefaultNode(node1);	
		ns1.addDefaultNode(node2);	
		ns1.addDefaultNode(node3);
		
		ns1.removeNode(node3);
		assertTrue("Problem with deleteNode", !ns1.containsNode(node3));	
	}

	/**
	 * This method is used to test the NodeStructureImpl.copy() method
	 */
	@Test
	public void testCopy() {
		nodeStructure3.addDefaultNode(node1);
		nodeStructure3.addDefaultNode(node2);
		nodeStructure3.addDefaultLink(link1);		
		
		ns = nodeStructure3.copy();		
		
		
		assertTrue("Problem with copy", NodeStructureImpl.compareNodeStructures(nodeStructure3, ns));	
		
	}	

	/**
	 * This method is used to test the NodeStructureImpl.getLinksByType() method
	 */
	@Test
	public void testGetLinksByType() {		
		links = ns1.getLinksByCategory(linktype1);
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
		
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultLink(link1);
		ns1.addDefaultLink(link2);
		
		ns2.addDefaultNode(node1);
		ns2.addDefaultNode(node2);
		ns2.addDefaultNode(node3);
		ns2.addDefaultLink(link1);
		ns2.addDefaultLink(link2);
					
		assertTrue(NodeStructureImpl.compareNodeStructures(ns1, ns2));			
	}	
}
