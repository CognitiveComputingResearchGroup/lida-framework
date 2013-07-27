/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.framework.shared;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;

/**
 * This is a JUnit class which can be used to test methods of the NodeImpl class
 * 
 * @author Siminder Kaur
 */
public class NodeImplTest {

	private NodeImpl node1, node2;
	private PamNodeImpl pamNode1, pamNode2;

	private ElementFactory factory = ElementFactory.getInstance();

	/**
	 * This method is called before running each test case to initialize the
	 * objects
	 * 
	 * @throws Exception
	 *             e
	 * 
	 */
	@Before
	public void setUp() throws Exception {
		node1 = (NodeImpl) factory.getNode();
		node2 = new NodeImpl();
		pamNode1 = (PamNodeImpl) factory.getNode("PamNodeImpl");
		pamNode2 = (PamNodeImpl) factory.getNode("PamNodeImpl");

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

	@Test
	public void testSetExtendedId() {
		ExtendedId id = new ExtendedId(345);
		node1.setExtendedId(id);

		assertEquals(345, node1.getId());
		assertEquals(id, node1.getExtendedId());

		ExtendedId id2 = new ExtendedId(2, id, 3);
		node1.setExtendedId(id2);

		assertEquals(345, node1.getId());
		assertEquals(id, node1.getExtendedId());

		node1.setExtendedId(null);
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
		assertEquals("Problem with getReferencedNode", pamNode1, node1
				.getGroundingPamNode());
	}

	/**
	 * This method is used to test the NodeImpl.setReferencedNode() method
	 */
	@Test
	public void testSetReferencedNode() {
		node2.setGroundingPamNode(pamNode2);
		assertEquals("Problem with setReferencedNode", pamNode2, node2
				.getGroundingPamNode());
	}

	/**
	 * This method is used to test the NodeImpl.equals() method
	 */
	@Test
	public void testEquals() {
		node1.setId(1);
		node2.setId(1);
		assertEquals("Problem with equals", node1, node2);
	}

	/**
	 * This method is used to test the NodeImpl.hashCode() method
	 */
	@Test
	public void testHashCode() {
		node1.setId(1);
		node2.setId(1);
		assertEquals("Problem with setNodeClass", node1.hashCode(), node2
				.hashCode());
	}
}