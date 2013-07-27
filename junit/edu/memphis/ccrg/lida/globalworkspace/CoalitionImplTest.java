/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.globalworkspace;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.attentioncodelets.NeighborhoodAttentionCodelet;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;

/**
 * This is a JUnit class which can be used to test methods of the CoalitionImpl
 * class
 * 
 * @author Siminder Kaur, Nisrine
 */
public class CoalitionImplTest {

	private static final ElementFactory factory = ElementFactory.getInstance();

	private CoalitionImpl coalition;
	private NodeImpl node1, node2, node3;
	private LinkImpl link1, link2;
	private NodeStructure content;
	private AttentionCodelet codelet;

	@Before
	public void setUp() throws Exception {
		node1 = new NodeImpl();
		node2 = new NodeImpl();
		node3 = new NodeImpl();

		content = new NodeStructureImpl();
		codelet = new NeighborhoodAttentionCodelet();

		node1.setId(1);
		node2.setId(2);
		node3.setId(3);

		node1.setActivation(0.3);
		node2.setActivation(0.4);
		node3.setActivation(0.6);

		PamNode cat = new PamNodeImpl();
		link1 = (LinkImpl) factory.getLink(node1, node2, cat);
		link2 = (LinkImpl) factory.getLink(node2, node3, cat);

		link1.setActivation(0.5);
		link1.setActivation(0.7);

		content.addDefaultNode(node1);
		content.addDefaultNode(node2);
		content.addDefaultNode(node3);
		content.addDefaultLink(link1);
		content.addDefaultLink(link2);
	}

	@Test
	public void testCoalitionImpl() {
		codelet.setBaseLevelActivation(0.7);
		coalition = new CoalitionImpl(content, codelet);
		double d = ((0.7 * (node1.getActivation() + node2.getActivation()
				+ node3.getActivation() + link1.getActivation() + link2
				.getActivation())) / (content.getNodeCount() + content
				.getLinkCount()));

		assertEquals(d, coalition.getActivation(), 0.00001);
	}

	@Test
	public void testGetContent() {
		codelet.setBaseLevelActivation(1.0);
		coalition = new CoalitionImpl(content, codelet);
		assertTrue(NodeStructureImpl.compareNodeStructures(content,
				(NodeStructure) coalition.getContent()));
	}
}
