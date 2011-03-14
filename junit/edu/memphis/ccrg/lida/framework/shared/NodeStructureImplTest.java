/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

/**
 * This is a JUnit class which can be used to test methods of the NodeStructureImpl class
 * @author Ryan McCall, Siminder Kaur
 * 
 */

public class NodeStructureImplTest extends TestCase{
	
	private NodeImpl node1,node2,node3,node4;
	private LinkImpl link1,link2,link3;	
	private LinkCategory category1,category2;	
	private NodeStructureImpl ns1,ns2,ns3;
	private Set<Link> links;

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
		links = new HashSet<Link>();
		
		category1 = LinkCategoryNode.PARENT ;
		category2 = LinkCategoryNode.CHILD ;	
		
		ns1 = new NodeStructureImpl();	
		ns2 = new NodeStructureImpl();
		ns3 = new NodeStructureImpl();

		node1.setId(1);		
		node1.setLabel("red");
		node1.setActivation(0.1);

		node2.setId(2);
		node2.setLabel("blue");
		node2.setActivation(0.2);

		node3.setId(3);
		node2.setLabel("purple");
		node2.setActivation(0.3);
		
		node4.setId(4);
		
		link1.setSource(node1);
		link1.setSink(node2);
		link1.setCategory(category1);
			
		link2.setSource(node2);
		link2.setSink(node3);
		link2.setCategory(category2);
		
		link3.setSource(node2);
		link3.setSink(node4);
		link3.setCategory(category2);
		
	}
	
	/**
	 * basic test of adding default node
	 */
	@Test
	public void testAddDefaultNode() {	
		ns1.addDefaultNode(node1);	
		assertTrue("Problem with addNode", ns1.containsNode(node1));
		assertTrue(ns1.getNodeCount() == 1);
		assertTrue(ns1.containsNode(node1));
		
		Node stored = ns1.getNode(node1.getId());
		String defaultType = ns1.getDefaultNodeType();
		String actualType = stored.getClass().getSimpleName();
		assertTrue(defaultType.equals(actualType));
		
		ns1.addDefaultNode(node2);	
		assertTrue("Problem with addNode", ns1.containsNode(node2));
		assertTrue(ns1.getNodeCount() == 2);
		assertTrue(ns1.containsNode(node2));
		
		ns1.addDefaultNode(node3);
		assertTrue("Problem with addNode", ns1.containsNode(node3));
		assertTrue(ns1.getNodeCount() == 3);
		assertTrue(ns1.containsNode(node3));		
	}

	/**
	 * Add same node 2x with different activation
	 */
	@Test
	public void testAddDefaultNode2() {
		node1.setActivation(0.0);
		node1.setId(9);
		
		node2.setActivation(0.5);
		node2.setId(9);
		
		ns1.addDefaultNode(node1);
		assertTrue(ns1.containsNode(node1));
		assertTrue(ns1.getNode(9).getActivation() == 0.0);
		
		ns1.addDefaultNode(node2);
		assertTrue(ns1.containsNode(node2));
		assertTrue(ns1.getNode(9).getActivation() == 0.5);
		
		ns1.addDefaultNode(node1);
		assertTrue(ns1.containsNode(node1));
		//TODO
//		assertTrue(ns1.getNode(9).getActivation() == 0.0);			
	}
	
	public void testAddNodesPlural(){
		Collection<Node> nodes = new ArrayList<Node>();
		nodes.add(node1);
		nodes.add(node4);		
		
		ns1.addDefaultNodes(nodes);
		
		assertTrue(ns1.getNodes().containsAll(nodes));
		assertTrue(nodes.containsAll(ns1.getNodes()));
	}

	/**
	 * This method is used to test the NodeStructureImpl.addLink() method
	 */
	@Test
	public void testAddLink() {	
		ns1.addDefaultNode(node1);	
		ns1.addDefaultNode(node2);	
		Link stored = ns1.addDefaultLink(link1);
		assertTrue("Problem with addLink", ns1.containsLink(link1));
		assertTrue(ns1.getLinkCount() == 1);
		
		String defaultType = ns1.getDefaultLinkType();
		String actualType = stored.getClass().getSimpleName();
		assertTrue(defaultType.equals(actualType));
		
		ns1.addDefaultNode(node3);	
		ns1.addDefaultLink(link2);
		assertTrue("Problem with addLink", ns1.containsLink(link2));
		assertTrue(ns1.getLinkCount() == 2);
	}

	/**
	 * This method is used to test the NodeStructureImpl.addLinks() method
	 */
	@Test
	public void testAddLinks() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultNode(node4);
		
		Collection<Link> links = new ArrayList<Link>(); 
		links.add(link1);
		links.add(link2);
		links.add(link3);
				
		ns1.addDefaultLinks(links);
		
		assertTrue(ns1.getLinks().containsAll(links));
		assertTrue(links.containsAll(ns1.getLinks()));
		assertTrue(ns1.getLinkCount() == 3);
	}
	
	public void testAddLinkFail(){
		link1.setActivation(0.0);
		
		Link l = ns1.addDefaultLink(link1);
		assertTrue(l == null);
		
		ns1.addDefaultNode(node1);
		l = ns1.addDefaultLink(link1);
		assertTrue(l == null);
		
		ns1.addDefaultNode(node3);
		l = ns1.addDefaultLink(link1);
		assertTrue(l == null);
		
		ns1.addDefaultNode(node2);
		l = ns1.addDefaultLink(link1);
		assertTrue(l != null);
		assertTrue(l.getCategory() == category1);
		assertTrue(l.getSource().equals(node1) && l.getSink().equals(node2));
		
		link1.setActivation(0.5);
		l = ns1.addDefaultLink(link1);
		assertTrue(ns1.getLink(link1.getExtendedId()).getActivation() == 0.5);
		
		link1.setActivation(0.0);
		l = ns1.addDefaultLink(link1);
		//TODO 
//		assertTrue(ns1.getLink(link1.getExtendedId()).getActivation() == 0.0);
	}
	
	public void testAddLinkParams0(){
		Link l = ns1.addDefaultLink(100, 101, category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);
		
		ns1.addDefaultNode(node1);
		l = ns1.addDefaultLink(node1.getId(), 101, category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);
		
		l = ns1.addDefaultLink(100, node1.getId(), category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);
		
		ns1.addDefaultNode(node3);
		l = ns1.addDefaultLink(node2.getId(), node1.getId(), category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);
		
		l = ns1.addDefaultLink(node3.getId(), node1.getId(), category1, 0.0, 0.0);
		assertTrue(l != null);
		assertTrue(ns1.getLinkCount() == 1);
		
		l = ns1.addDefaultLink(node3.getId(), node1.getId(), category2, 0.0, 0.0);
		assertTrue(l != null);
		assertTrue(ns1.getLinkCount() == 2);
		
		l = ns1.addDefaultLink(node1.getId(), node3.getId(), category1, 0.0, 0.0);
		assertTrue(l != null);
		assertTrue(ns1.getLinkCount() == 3);
		
		l = ns1.addDefaultLink(node1.getId(), node3.getId(), category1, 0.0, 0.0);
		assertTrue(l != null);
		assertTrue(ns1.getLinkCount() == 3);
	}
	
	public void testAddLinkParams2(){
		Link l = ns1.addDefaultLink(node1.getId(), node3.getExtendedId(), category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);
		
		ns1.addDefaultNode(node1);
		l = ns1.addDefaultLink(node1.getId(), node3.getExtendedId(), category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);
		
		l = ns1.addDefaultLink(node3.getId(), node1.getExtendedId(), category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);
		
		ns1.addDefaultNode(node3);
		l = ns1.addDefaultLink(node1.getId(), node3.getExtendedId(), category1, 0.0, 0.0);
		assertTrue(l != null);
		assertTrue(ns1.getLinkCount() == 1);
		assertTrue(ns1.containsLink(l));
	}
	
	public void testAddLinkParams3(){
		ns1.addDefaultNode(node1);
		Link sinkLink = new LinkImpl(node2, node3, category1);
		Link stored = ns1.addDefaultLink(node1.getExtendedId(), sinkLink.getExtendedId(), category1, 0.0, 0.0);
		assertTrue(stored == null);
		assertTrue(ns1.getLinkCount() == 0);
		
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		sinkLink = ns1.addDefaultLink(sinkLink);
		assertTrue(ns1.getLinkCount() == 1);
		
		stored = ns1.addDefaultLink(node1.getExtendedId(), sinkLink.getExtendedId(), category1, 0.0, 0.0);
		assertTrue(stored != null);
		assertTrue(ns1.getLinkCount() == 2);
		assertTrue(ns1.containsLink(stored));
	}
	
	public void testClearNodeStructure(){
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultLink(link1);
		ns1.addDefaultLink(link2);
		ns1.addDefaultLink(link3);
		ns1.mergeWith(ns2);
		
		ns1.clearNodeStructure();
		assertTrue(ns1.getLinkableCount() == 0);
		assertTrue(ns1.getNodeCount() == 0);
		assertTrue(ns1.getLinkCount() == 0);
		assertTrue(ns1.getLinkableMap().size() == 0);
	}
	
	public void testClearLinks(){
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultLink(link1);
		ns1.addDefaultLink(link2);
		ns1.addDefaultLink(link3);
		ns1.addDefaultLink(node3.getId(), node1.getId(), category1, 0.0, 0.0);
		
		ns1.clearLinks();
		assertTrue(ns1.getLinkableCount() == 3);
		assertTrue(ns1.getNodeCount() == 3);
		assertTrue(ns1.getLinkCount() == 0);
		assertTrue(ns1.getLinkableMap().size() == 3);
	}

	/**
	 * This method is used to test the NodeStructureImpl.deleteLink() method
	 */
	@Test
	public void testRemoveLink() {
		ns1.addDefaultNode(node1);	
		ns1.addDefaultNode(node2);	
		ns1.addDefaultNode(node3);	
		
		ns1.addDefaultLink(link1);					
		ns1.addDefaultLink(link2);
		
		ns1.removeLink(link1);
		assertTrue(!ns1.containsLink(link1));	
		assertTrue(ns1.getLinkCount() == 1);
	}
	
	public void testRemoveLink2(){
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		Link foo = ns1.addDefaultLink(node1.getId(), node2.getId(), category1, 0.0, 0.0);
		Link bar = ns1.addDefaultLink(node1.getId(), node2.getId(), category2, 0.0, 0.0);
		
		assertTrue(ns1.getLinkCount() == 2);
		ns1.removeLink(foo);
		assertTrue(ns1.getLinkCount() == 1);
		ns1.removeLink(bar);
		assertTrue(ns1.getLinkCount() == 0);
		
		Set<Link> links = ns1.getAttachedLinks(node1);
		assertTrue(links.size() == 0);
		
		links = ns1.getAttachedLinks(node1);
		assertTrue(links.size() == 0);
	}
	
	public void testRemoveNode0(){
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		Link foo = ns1.addDefaultLink(node1.getId(), node2.getId(), category1, 0.0, 0.0);
		
		assertTrue(ns1.getLinkableCount() == 3);
		assertTrue(ns1.getLinkCount() == 1);
		assertTrue(ns1.getNodeCount() == 2);
			
		ns1.removeNode(node1);
		assertTrue(ns1.getLinkCount() == 0);
		assertTrue(ns1.getNodeCount() == 1);
		assertTrue(!ns1.containsNode(node1));
		Link actualLink = ns1.getLink(foo.getExtendedId());
		assertTrue(actualLink == null);
		assertFalse(ns1.containsLink(foo));
		
		//remove other node this time
		
		ns1.addDefaultNode(node1);
		foo = ns1.addDefaultLink(node1.getId(), node2.getId(), category1, 0.0, 0.0);
		assertTrue(ns1.getLinkableCount() == 3);
		assertTrue(ns1.getLinkCount() == 1);
		assertTrue(ns1.getNodeCount() == 2);
		
		ns1.removeNode(node2);
		assertTrue(ns1.getLinkCount() == 0);
		assertTrue(ns1.getNodeCount() == 1);
		assertTrue(!ns1.containsNode(node2));
		actualLink = ns1.getLink(foo.getExtendedId());
		assertTrue(actualLink == null);
		assertFalse(ns1.containsLink(foo));
	}

	/**
	 * This method is used to test the NodeStructureImpl.deleteNode() method
	 */
	@Test
	public void testRemoveNode() {
		ns1.addDefaultNode(node1);	
		ns1.addDefaultNode(node2);	
		ns1.addDefaultNode(node3);
		
		ns1.removeNode(node3);
		assertTrue("Problem with deleteNode", !ns1.containsNode(node3));	
	}
	
	public void testRemoveLinkable(){
		ns1.addDefaultNode(node1);
		Node stored2 = ns1.addDefaultNode(node2);
		Link stored3 = ns1.addDefaultLink(node1.getId(), node2.getId(), category2, 0.0, 0.0);
		assertTrue(ns1.getNodeCount() == 2 && ns1.getLinkCount() == 1);
		
		ns1.removeLinkable(stored2);
		assertTrue(ns1.getNodeCount() == 1 && ns1.getLinkCount() == 0);
		assertFalse(ns1.containsNode(stored2.getId()));
		
		ns1.clearNodeStructure();
		ns1.addDefaultNode(node1);
		stored2 = ns1.addDefaultNode(node2);
		stored3 = ns1.addDefaultLink(node1.getId(), node2.getId(), category2, 0.0, 0.0);
		
		ns1.removeLinkable(stored3);
		
		assertTrue(ns1.getLinkableCount() == 2);
		assertTrue(ns1.getLinkCount() == 0);
		assertTrue(ns1.getNodeCount() == 2);
	}

	/**
	 * This method is used to test the NodeStructureImpl.copy() method
	 */
	@Test
	public void testCopy() {
		ns3.addDefaultNode(node1);
		ns3.addDefaultNode(node2);
		ns3.addDefaultNode(node3);
		ns3.addDefaultLink(link1);
		ns3.addDefaultLink(node2.getId(), node1.getId(), category1, 0.0, 0.0);
		
		NodeStructure copy = ns3.copy();		
		
		assertTrue(copy.getNodeCount() == 3);
		assertTrue(copy.getLinkCount() == 2);
		Collection<Link> links = copy.getAttachedLinks(node1);
		assertTrue(links.size() == 2);
		links = copy.getAttachedLinks(node2);
		assertTrue(links.size() == 2);
	}	
	
	public void testCopy2(){
		double testActivation = 0.69;
		double testRemovable = 0.99;
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultNode(node4);
		
		Link link34 = ns1.addDefaultLink(node3.getId(), node4.getId(), category1, testActivation, testRemovable);
		Link link234 = ns1.addDefaultLink(node2.getId(), link34.getExtendedId(), category1, testActivation, testRemovable);
		Link link1234 = ns1.addDefaultLink(node1.getId(), link234.getExtendedId(), category1, testActivation, testRemovable);
		
		NodeStructure copy = ns1.copy();
		
		assertTrue(copy.getLinkableCount() == 7);
		assertTrue(copy.getLinkCount() == 3);
		assertTrue(copy.getNodeCount() == 4);
		
		assertTrue(copy.containsNode(node1));
		assertTrue(copy.containsNode(node2));
		assertTrue(copy.containsNode(node3));
		assertTrue(copy.containsNode(node4));
		
		assertTrue(copy.containsLink(link34));
		assertTrue(copy.containsLink(link234));
		assertTrue(copy.containsLink(link1234));
		
		assertTrue(copy.getAttachedLinks(node1).size() == 1);
		assertTrue(copy.getAttachedLinks(node2).size() == 1);
		assertTrue(copy.getAttachedLinks(node3).size() == 1);
		assertTrue(copy.getAttachedLinks(node4).size() == 1);
		
		Link testRez = copy.getLink(link34.getExtendedId());
		assertTrue(testRez.getSource().equals(node3) && testRez.getSink().equals(node4));
		assertTrue(testRez.getActivation() == testActivation);
		assertTrue(testRez.getActivatibleRemovalThreshold() == testRemovable);
		
		Link testRez2 = copy.getLink(link234.getExtendedId());
		assertTrue(testRez2.getSource().equals(node2) && testRez2.getSink().equals(link34));
		assertTrue(testRez2.getActivation() == testActivation);
		assertTrue(testRez2.getActivatibleRemovalThreshold() == testRemovable);
		
		Link testRez3 = copy.getLink(link1234.getExtendedId());
		assertTrue(testRez3.getSource().equals(node1) && testRez3.getSink().equals(link234));
		assertTrue(testRez3.getActivation() == testActivation);
		assertTrue(testRez3.getActivatibleRemovalThreshold() == testRemovable);
	}
	
	public void testMerge(){
		//TODO
	}
	
	public void testDecayNodeStructure(){
		NodeStructure ns = new NodeStructureImpl();
		Node n = new NodeImpl();
		n.setActivation(0.1);
		n.setActivatibleRemovalThreshold(0.05);
		
		n.decay(100);
		assertTrue(n.isRemovable());
		
		n.setActivation(0.1);
		n.setActivatibleRemovalThreshold(0.05);
		assertTrue(!n.isRemovable());
		
		Node storedNode = ns.addDefaultNode(n);
		
		assertTrue(storedNode.getActivation() + "", 0.1 == storedNode.getActivation());
		assertTrue(storedNode.getActivatibleRemovalThreshold() + "", 0.05 == storedNode.getActivatibleRemovalThreshold());
		ns.decayNodeStructure(100);
		assertTrue(storedNode.isRemovable());
		
		//TODO
	}

	/**
	 * This method is used to test the NodeStructureImpl.getLinksByType() method
	 */
	@Test
	public void testGetLinksByType() {		
		links = ns1.getLinks(category1);
		for (Link l : links) {
			assertEquals("Problem with GetLinksByType", l.getCategory(),category1);			
			break;				
		}	
	}
	
	public void testGetAttachedLinks(){
		//TODO
	}
	
	public void testGetAttachedLinksByType(){
		//TODO
	}
	
	public void testGetParentLinkMap(){
		//TODO		
	}
	
	/**
	 * This method is used to test the NodeStructureImpl.equals() method
	 */
	@Test
	public void testCompareNodeStructures() {
		
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
					//TODO better example
		assertTrue(NodeStructureImpl.compareNodeStructures(ns1, ns2));			
	}	

}
