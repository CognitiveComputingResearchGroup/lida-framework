/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.ns.ExtendedId;

/**
 * 
 * If you are really bored then you can test the case where the sinks link's
 * sink is a link between 2 nodes I've just tested up to the case where the
 * sink's link is just between 2 nodes and that should be sufficient because it
 * tests the same code (the link constructor) just with different values in the
 * sink ExtendedId
 * 
 * @author Ryan J. McCall
 * 
 */
public class ExtendedIdTest {

	private int category1 = (int) (Math.random() * Integer.MAX_VALUE);

	@Test
	public void testNodeId() {
		int nodeId = (int) (Math.random() * Integer.MAX_VALUE);
		ExtendedId id1 = new ExtendedId(nodeId);
		ExtendedId id2 = new ExtendedId(nodeId);

		assertTrue(id1.isNodeId());
		assertTrue(id2.isNodeId());
		assertFalse(id1.isComplexLink());
		assertFalse(id2.isComplexLink());
		assertFalse(id1.isSimpleLink());
		assertFalse(id2.isSimpleLink());

		assertEquals(nodeId, id1.getSourceNodeId());
		assertEquals(nodeId, id2.getSourceNodeId());

		assertHashCodeEquals(id1, id2);
	}

	@Test
	public void testNodeId0() {
		int nodeId = (int) (Math.random() * Integer.MAX_VALUE);
		int nodeId2 = nodeId % 31;
		ExtendedId id1 = new ExtendedId(nodeId);
		ExtendedId id2 = new ExtendedId(nodeId2);

		assertTrue(id1.isNodeId());
		assertTrue(id2.isNodeId());
		assertFalse(id1.isComplexLink());
		assertFalse(id2.isComplexLink());
		assertFalse(id1.isSimpleLink());
		assertFalse(id2.isSimpleLink());

		assertEquals(nodeId, id1.getSourceNodeId());
		assertEquals(nodeId2, id2.getSourceNodeId());

		assertDifferentHashCodeEquals(id1, id2);
	}

	// Two nodes case
	@Test
	public void testLinkId() {
		int nodeId = (int) (Math.random() * Integer.MAX_VALUE);
		ExtendedId sinkNodeId = new ExtendedId(nodeId);
		int sourceNodeId = nodeId % 31;
		int category = (int) (Math.random() * Integer.MAX_VALUE);

		ExtendedId id = new ExtendedId(sourceNodeId, sinkNodeId, category);
		assertTrue(!id.isNodeId());
		assertEquals(sourceNodeId, id.getSourceNodeId());

		// equivalent id
		ExtendedId id2 = new ExtendedId(sourceNodeId, sinkNodeId, category);
		assertHashCodeEquals(id, id2);

		// non equivalent id
		ExtendedId id3 = new ExtendedId(sourceNodeId, sinkNodeId, category + 1);
		assertDifferentHashCodeEquals(id, id3);

		// non equivalent id
		ExtendedId id4 = new ExtendedId(sourceNodeId + 1, sinkNodeId, category);
		assertDifferentHashCodeEquals(id, id4);

		// non equivalent id
		ExtendedId sinkNodeId2 = new ExtendedId(nodeId + 1);
		ExtendedId id5 = new ExtendedId(sourceNodeId, sinkNodeId2, category);
		assertDifferentHashCodeEquals(id, id5);
	}

	// One node, one link, link between 2 nodes case
	@Test
	public void testLinkId2() {
		// sink will be a link between 2 nodes
		int slCat = 10;
		int slSource = 11;
		ExtendedId slSink = new ExtendedId(12);
		ExtendedId sinkLink = new ExtendedId(slSource, slSink, slCat);

		// Node to link link
		int category = 2;
		int sourceNodeId = 5;
		ExtendedId nodeLinkId = new ExtendedId(sourceNodeId, sinkLink, category);
		assertTrue(!nodeLinkId.isNodeId());

		// clone
		sinkLink = new ExtendedId(slSource, slSink, slCat);
		ExtendedId sameNodeLinkId = new ExtendedId(sourceNodeId, sinkLink,
				category);
		assertHashCodeEquals(nodeLinkId, sameNodeLinkId);

		// same structure, different sink link category
		sinkLink = new ExtendedId(slSource, slSink, slCat + 1);
		ExtendedId diffNodeLinkId = new ExtendedId(sourceNodeId, sinkLink,
				category);
		assertDifferentHashCodeEquals(nodeLinkId, diffNodeLinkId);

		// same structure, different sink link source node
		sinkLink = new ExtendedId(slSource + 1, slSink, slCat);
		diffNodeLinkId = new ExtendedId(sourceNodeId, sinkLink, category);
		assertDifferentHashCodeEquals(nodeLinkId, diffNodeLinkId);

		// same structure, different sink link sink node
		slSink = new ExtendedId(slSink.getSourceNodeId() + 1);
		sinkLink = new ExtendedId(slSource, slSink, slCat);
		diffNodeLinkId = new ExtendedId(sourceNodeId, sinkLink, category);
		assertDifferentHashCodeEquals(nodeLinkId, diffNodeLinkId);
	}

	private void assertDifferentHashCodeEquals(Object o1, Object o2) {
		assertTrue(!o1.equals(o2));
		assertTrue(!o2.equals(o1));
		assertTrue(o1.hashCode() != o2.hashCode());
	}

	private void assertHashCodeEquals(Object o1, Object o2) {
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));
		assertTrue(o1.hashCode() == o2.hashCode());
	}

	@Test
	public void testIsSimpleLink() {
		int nodeId = (int) (Math.random() * Integer.MAX_VALUE);
		ExtendedId id1 = new ExtendedId(nodeId);
		ExtendedId id2 = new ExtendedId(nodeId + 1);
		ExtendedId lid = new ExtendedId(id1.getSourceNodeId(), id2, category1);

		assertTrue(lid.isSimpleLink());
		assertFalse(lid.isNodeId());
		assertFalse(lid.isComplexLink());
	}

	@Test
	public void testIsComplexLink() {
		int nodeId = 45;
		ExtendedId id1 = new ExtendedId(nodeId);
		ExtendedId id2 = new ExtendedId(99);
		ExtendedId simple = new ExtendedId(id1.getSourceNodeId(), id2,
				category1);
		ExtendedId complex = new ExtendedId(2348, simple, category1);

		assertTrue(complex.isComplexLink());
		assertFalse(complex.isSimpleLink());
		assertFalse(complex.isNodeId());
	}

}
