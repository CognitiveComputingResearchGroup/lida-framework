/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared;


import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.pam.PamNodeImpl;

/**
 * This is a JUnit class which can be used to test methods of the NodeImpl class
 * @author Siminder Kaur
 */
public class NodeImplTest extends TestCase{

	private NodeImpl node1,node2;
	private PamNodeImpl pamNode1,pamNode2;
	
	private LidaElementFactory factory = LidaElementFactory.getInstance();
	
	/**
	 * This method is called before running each test case to initialize the objects
	 * 
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		node1 = (NodeImpl) factory.getNode(NodeImpl.factoryName);
		node2 = new NodeImpl();
		pamNode1 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName);	
		pamNode2 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName);
					
		node1.setLabel("red");		
		node1.setGroundingPamNode(pamNode1);		
	}
	
	/**
	 * This method is used to test the NodeImpl.getId() method
	 */
	@Test
	public void testGetId() {		
		node1.setId(1);
		assertEquals("Problem with getId", 1, node1.getId());
	}	
	
	/**
	 * This method is used to test the NodeImpl.setId() method
	 */
	@Test
	public void testSetId() {
		node2.setId(22);					
		assertEquals("Problem with setId", 22, node2.getId());
	}	
	
	/**
	 * This method is used to test the NodeImpl.getLabel() method
	 */
	@Test
	public void testGetLabel() {	
		assertEquals("Problem with getLabel", "red", node1.getLabel());
	}
	
	/**
	 * This method is used to test the NodeImpl.setLabel() method
	 */
	@Test
	public void testSetLabel() {
		node2.setLabel("purple");					
		assertEquals("Problem with setLabel", "purple", node2.getLabel());
	}	
	
	/**
	 * This method is used to test the NodeImpl.getReferencedNode() method
	 */
	@Test
	public void testGetReferencedNode() {		
		assertEquals("Problem with getReferencedNode", pamNode1, node1.getGroundingPamNode());
	}
	
	/**
	 * This method is used to test the NodeImpl.setReferencedNode() method
	 */
	@Test
	public void testSetReferencedNode() {
		node2.setGroundingPamNode(pamNode2);					
		assertEquals("Problem with setReferencedNode", pamNode2, node2.getGroundingPamNode());
	}	
	
	/**
	 * This method is used to test the NodeImpl.equals() method
	 */
	@Test
	public void testEquals() {
		node1.setId(1);
		node2.setId(1);
		assertEquals("Problem with equals", node1,node2);
	}	
	
	/**
	 * This method is used to test the NodeImpl.hashCode() method
	 */
	@Test
	public void testHashCode() {
		node1.setId(1);
		node2.setId(1);	
		assertEquals("Problem with setNodeClass",node1.hashCode(), node2.hashCode());
	}	
	
	public void testDesirability(){
		node1.setDesirability(5.0);
		assertEquals(1.0, node1.getDesirability());
		
		node1.setDesirability(-14534545);
		assertEquals(0.0, node1.getDesirability());
		
		node1.setDesirability(0.4);
		assertEquals(0.4, node1.getDesirability());
		
		node1.setDesirability(0.0);
		assertEquals(0.0, node1.getDesirability());
		
		node1.setDesirability(1.0);
		assertEquals(1.0, node1.getDesirability());
	}
	
}
