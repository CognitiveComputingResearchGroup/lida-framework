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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.Linkable;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.pam.PamLinkImplSubclass;
import edu.memphis.ccrg.lida.pam.PamNodeImplSubclass;
import edu.memphis.ccrg.lida.pam.ns.PamLink;
import edu.memphis.ccrg.lida.pam.ns.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;

/**
 * This is a JUnit class which can be used to test methods of the
 * NodeStructureImpl class.
 * 
 * @author Ryan J. McCall
 * @author Siminder Kaur
 */
public class NodeStructureImplTest {

	private static ElementFactory factory = ElementFactory.getInstance();
	private static final double epsilon = 1e-09;
	private Node node1, node2, node3, node4;
	private Link link1, link2, link3;
	private PamNode category1, category2;
	private NodeStructureImpl ns1, ns2, ns3;

	/**
	 * loads factory data
	 */
	@BeforeClass
	public static void setUpBeforeClass() {
		Properties prop = ConfigUtils
				.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		FactoriesDataXmlLoader.loadFactoriesData(prop);
	}

	/**
	 * @throws Exception
	 *             e
	 */
	@Before
	public void setUp() throws Exception {
		node1 = factory.getNode();
		node1.setLabel("red");
		node1.setActivation(0.1);

		node2 = factory.getNode();
		node2.setLabel("blue");
		node2.setActivation(0.2);

		node3 = factory.getNode();
		node3.setLabel("purple");
		node3.setActivation(0.3);

		node4 = factory.getNode();
		node4.setLabel("green");
		node4.setActivation(0.4);

		category1 = new PamNodeImpl();
		category1.setId(99999);
		category2 = new PamNodeImpl();
		category2.setId(100000);

		link1 = factory.getLink(node1, node2, category1);
		link2 = factory.getLink(node2, node3, category2);
		link3 = factory.getLink(node2, node4, category2);

		ns1 = new NodeStructureImpl();
		ns2 = new NodeStructureImpl();
		ns3 = new NodeStructureImpl();
	}

	/**
	 * {@link NodeStructureImpl#addDefaultNode(Node)}
	 */
	@Test
	public void testAddDefaultNode() {
		ns1.addDefaultNode(node1);
		assertTrue("Problem with addNode", ns1.containsNode(node1));
		assertTrue(ns1.getNodeCount() == 1);
		assertTrue(ns1.getLinkableCount() == 1);
		assertTrue(ns1.containsNode(node1));

		Node stored = ns1.getNode(node1.getId());
		String defaultType = ns1.getDefaultNodeType();
		Node testNode = factory.getNode(defaultType);
		String testNodeType = testNode.getClass().getSimpleName();
		String actualType = stored.getClass().getSimpleName();
		assertTrue(testNodeType.equals(actualType));

		ns1.addDefaultNode(node2);
		assertTrue("Problem with addNode", ns1.containsNode(node2));
		assertTrue(ns1.getNodeCount() == 2);
		assertTrue(ns1.getLinkableCount() == 2);
		assertTrue(ns1.containsNode(node2));

		ns1.addDefaultNode(node3);
		assertTrue("Problem with addNode", ns1.containsNode(node3));
		assertTrue(ns1.getNodeCount() == 3);
		assertTrue(ns1.getLinkableCount() == 3);
		assertTrue(ns1.containsNode(node3));
	}

	/**
	 * Add same node 2x with different activation
	 * {@link NodeStructureImpl#addDefaultNode(Node)}
	 */
	@Test
	public void testAddDefaultNode2() {
		node1.setActivation(0.0);
		node1.setId(9);

		node2.setActivation(0.5);
		node2.setId(9);

		ns1.addDefaultNode(node1);
		assertTrue(ns1.containsNode(node1));
		assertTrue(ns1.getLinkableCount() == 1);
		assertTrue(ns1.getNodeCount() == 1);
		assertTrue(ns1.getNode(9).getActivation() == 0.0);

		ns1.addDefaultNode(node2);
		assertTrue(ns1.containsNode(node2));
		assertTrue(ns1.getLinkableCount() == 1);
		assertTrue(ns1.getNodeCount() == 1);
		assertTrue(ns1.getNode(9).getActivation() == 0.5);

		ns1.addDefaultNode(node1);
		assertTrue(ns1.containsNode(node1));
	}

	/**
	 * {@link NodeStructureImpl#addDefaultNode(Node)}
	 * {@link NodeStructureImpl#addDefaultNodes(Collection)}
	 */
	@Test
	public void testAddDefaultNode3() {
		assertNull(ns1.addDefaultNode(null));
		assertNull(ns1.addDefaultNodes(null));
	}

	/**
	 * {@link NodeStructureImpl#addDefaultNodes(Collection)}
	 */
	@Test
	public void testAddNodesPlural() {
		Collection<Node> nodes = new ArrayList<Node>();
		nodes.add(node1);
		nodes.add(node4);

		ns1.addDefaultNodes(nodes);

		assertTrue(ns1.getNodes().containsAll(nodes));
		assertTrue(nodes.containsAll(ns1.getNodes()));

		assertTrue(ns1.getLinkableCount() == 2);
		assertTrue(ns1.getNodeCount() == 2);
	}

	/**
	 * {@link NodeStructureImpl#addDefaultNodes(Collection)}
	 */
	@Test
	public void addNullInCollection1() {
		Collection<Node> nodes = new ArrayList<Node>();
		nodes.add(null);
		nodes.add(node1);

		ns1.addDefaultNodes(nodes);

		assertEquals(1, ns1.getNodeCount());
		assertTrue(ns1.containsNode(node1));
	}

	/**
	 * {@link NodeStructureImpl#addNode(Node, boolean)}
	 */
	@Test
	public void addNodeCopy() {
		Node res = ns1.addNode(null, true);
		assertNull(res);

		res = ns1.addNode(null, false);
		assertNull(res);

		Node stored = ns1.addNode(node1, false);
		assertSame(stored, node1);
		stored = ns1.getNode(node1.getId());
		assertSame(stored, node1);

		// should return existing node if try to add again without copying
		assertEquals(1, ns1.getNodeCount());
		Node node1Stored = ns1.addNode(node1, false);
		assertEquals(1, ns1.getNodeCount());
		assertSame(node1Stored, node1);

		// add without copying
		stored = ns1.addNode(node2, true);
		assertNotSame(stored, node2);
		assertEquals(stored, node2);
		assertEquals(2, ns1.getNodeCount());

		stored = ns1.addNode(node1, true);
		assertSame(stored, node1Stored);
		assertEquals(2, ns1.getNodeCount());
	}

	/**
	 * {@link NodeStructureImpl#addNode(Node, String)}
	 */
	@Test
	public void testAddNodeType() {
		// basic test
		Node n = ns1.addNode(node1, "NodeImpl");
		assertEquals(1, ns1.getNodeCount());
		assertTrue(n instanceof NodeImpl);

		// error cases
		assertNull(ns1.addNode(null, "NodeImpl"));
		assertNull(ns1.addNode(node2, null));
		assertNull(ns1.addNode(node1, "badType154"));
		assertEquals(1, ns1.getNodeCount());

		// update activation
		assertTrue(n.getActivation() < 0.5);
		node1.setActivation(0.5);
		n = ns1.addNode(node1, "NodeImpl");
		assertEquals(n.getActivation(), 0.5, epsilon);
		assertEquals(1, ns1.getNodeCount());

		// try to add same node specifying different types
		node1.setActivation(1.0);
		Node n2 = ns1.addNode(node1, "PamNodeImpl");
		assertNull(n2);
		assertEquals(1, ns1.getNodeCount());
	}

	/**
	 * {@link NodeStructureImpl#addNode(Node, String)}
	 */
	@Test
	public void testAddNodeType2() {
		// same as above but order of types is reversed
		node2.setActivation(0.5);
		ns1.addNode(node2, "PamNodeImpl");
		node2.setActivation(1.0);
		Node n3 = ns1.addNode(node2, "NodeImpl");
		assertNull(n3);
		assertEquals(1, ns1.getNodeCount());
	}

	/**
	 * {@link NodeStructureImpl#addDefaultNode(String, double, double)}
	 */
	@Test
	public void testAddNewDefaultNode() {
		Node n = ns1.addDefaultNode("a", 0.123, 0.456);
		assertEquals(ns1.getDefaultNodeType(), n.getFactoryType());
		assertEquals(1, ns1.getNodeCount());
		assertEquals("a", n.getLabel());
		assertEquals(0.123, n.getActivation(), epsilon);
		assertEquals(0.456, n.getActivatibleRemovalThreshold(), epsilon);

		Node n2 = ns1.addDefaultNode("a", 0.123, 0.456);
		assertNotSame(n, n2);
		assertFalse(n.equals(n2));
		assertEquals(2, ns1.getNodeCount());

		n = ns1.addDefaultNode(null, 0.123, 0.456);
		assertNull(n.getLabel());

		n = ns1.addDefaultNode("a", -0.123, -0.456);
		assertEquals(0, n.getActivation(), epsilon);
		assertEquals(-0.456, n.getActivatibleRemovalThreshold(), epsilon);
	}

	/**
	 * {@link NodeStructureImpl#addNode(String, String, double, double)}
	 */
	@Test
	public void testAddNewTypeNode() {
		assertNull(ns1.addNode(null, "a", 0.0, 0.0));
		assertNull(ns1.addNode("badatre43", "a", 0, 0));
		Node n = ns1.addNode("NodeImpl", "", 0, 0);
		assertTrue(ns1.containsNode(n));
		assertEquals(1, ns1.getNodeCount());
		assertTrue(n instanceof NodeImpl);

		n = ns1.addNode("PamNodeImpl", "", 0, 0);
		assertEquals(2, ns1.getNodeCount());
		assertTrue(n instanceof PamNodeImpl);
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 */
	@Test
	public void testAddDefaultLink() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		Link stored = ns1.addDefaultLink(link1);
		assertTrue("Problem with addLink", ns1.containsLink(link1));
		assertTrue(ns1.getLinkCount() == 1);
		assertTrue(ns1.getLinkableCount() == 3);

		String defaultType = ns1.getDefaultLinkType();
		Link testLink = factory.getLink(defaultType, node1, node2, category1);
		String testLinkType = testLink.getClass().getSimpleName();
		String actualType = stored.getClass().getSimpleName();
		assertTrue(testLinkType.equals(actualType));

		ns1.addDefaultNode(node3);
		ns1.addDefaultLink(link2);
		assertTrue("Problem with addLink", ns1.containsLink(link2));
		assertTrue(ns1.getLinkCount() == 2);
		assertTrue(ns1.getLinkableCount() == 5);
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 * 
	 * {@link NodeStructureImpl#getAttachedLinks(Linkable)}
	 */
	@Test
	public void testAddLinkAttachments() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultLink(link1);

		assertEquals(3, ns1.getLinkableCount());
		assertEquals(0, ns1.getAttachedLinks(link1).size());
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 */
	@Test
	public void testAddLinkSelf() {
		ns1.addDefaultNode(node1);
		assertNull(ns1.addDefaultLink(node1, node1, category1, 0.0, 0.0));
		assertEquals(1, ns1.getLinkableCount());
		assertEquals(0, ns1.getLinkCount());
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLinks(Collection)}
	 */
	@Test
	public void testAddDefaultLinks() {
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

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 */
	@Test
	public void addNullLink() {
		assertNull(ns1.addDefaultLink(null));
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLinks(Collection)}
	 */
	@Test
	public void addNullInCollection() {
		Collection<Link> links = new ArrayList<Link>();
		links.add(null);
		links.add(link1);
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);

		links = ns1.addDefaultLinks(links);

		assertEquals(1, ns1.getLinkCount());
		assertTrue(ns1.containsLink(link1));
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 */
	@Test
	public void testAddLinkFail() {
		link1.setActivation(0.0);

		Link l = ns1.addDefaultLink(link1);
		assertNull(l);

		ns1.addDefaultNode(node1);
		l = ns1.addDefaultLink(link1);
		assertNull(l);

		ns1.addDefaultNode(node3);
		l = ns1.addDefaultLink(link1);
		assertNull(l);

		ns1.addDefaultNode(node2);
		l = ns1.addDefaultLink(link1);
		assertNotNull(l);
		assertTrue(l.getCategory() == category1);
		assertTrue(l.getSource().equals(node1) && l.getSink().equals(node2));

		link1.setActivation(0.5);
		l = ns1.addDefaultLink(link1);
		assertTrue(ns1.getLink(link1.getExtendedId()).getActivation() == 0.5);

		link1.setActivation(0.0);
		l = ns1.addDefaultLink(link1);

		assertTrue(ns1.getLink(link1.getExtendedId()).getActivation() == 0.5);
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 */
	@Test
	public void testAddLinkParams0() {
		Link l = ns1.addDefaultLink(Integer.MAX_VALUE - 100,
				Integer.MAX_VALUE - 101, category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);

		ns1.addDefaultNode(node1);
		l = ns1.addDefaultLink(node1.getId(), Integer.MAX_VALUE - 101,
				category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);

		l = ns1.addDefaultLink(Integer.MAX_VALUE - 100, node1.getId(),
				category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);

		ns1.addDefaultNode(node3);
		l = ns1.addDefaultLink(node2.getId(), node1.getId(), category1, 0.0,
				0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);

		l = ns1.addDefaultLink(node3.getId(), node1.getId(), category1, 0.0,
				0.0);
		assertTrue(l != null);
		assertTrue(ns1.getLinkCount() == 1);

		l = ns1.addDefaultLink(node3.getId(), node1.getId(), category2, 0.0,
				0.0);
		assertTrue(l != null);
		assertTrue(ns1.getLinkCount() == 2);

		l = ns1.addDefaultLink(node1.getId(), node3.getId(), category1, 0.0,
				0.0);
		assertTrue(l != null);
		assertTrue(ns1.getLinkCount() == 3);

		l = ns1.addDefaultLink(node1.getId(), node3.getId(), category1, 0.9,
				0.0);
		assertNotNull(l);
		assertEquals(3, ns1.getLinkCount());
		assertEquals(0.9, l.getActivation(), epsilon);
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 */
	@Test
	public void testAddLinkParams2() {
		Link l = ns1.addDefaultLink(node1.getId(), node3.getExtendedId(),
				category1, 0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);

		ns1.addDefaultNode(node1);
		l = ns1.addDefaultLink(node1.getId(), node3.getExtendedId(), category1,
				0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);

		l = ns1.addDefaultLink(node3.getId(), node1.getExtendedId(), category1,
				0.0, 0.0);
		assertTrue(l == null);
		assertTrue(ns1.getLinkCount() == 0);

		ns1.addDefaultNode(node3);
		l = ns1.addDefaultLink(node1.getId(), node3.getExtendedId(), category1,
				0.0, 0.0);
		assertTrue(l != null);
		assertTrue(ns1.getLinkCount() == 1);
		assertTrue(ns1.containsLink(l));
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 */
	@Test
	public void testAddLinkParams3() {
		ns1.addDefaultNode(node1);
		Link sinkLink = factory.getLink(node2, node3, category1);
		Link stored = ns1.addDefaultLink(node1.getId(), sinkLink
				.getExtendedId(), category1, 0.0, 0.0);
		assertTrue(stored == null);
		assertTrue(ns1.getLinkCount() == 0);

		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		sinkLink = ns1.addDefaultLink(sinkLink);
		assertTrue(ns1.getLinkCount() == 1);

		stored = ns1.addDefaultLink(node1.getId(), sinkLink.getExtendedId(),
				category1, 0.0, 0.0);
		assertTrue(stored != null);
		assertTrue(ns1.getLinkCount() == 2);
		assertTrue(ns1.containsLink(stored));
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 */
	@Test
	public void addDuplicateLink() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultLink(link1);
		assertTrue(ns1.containsLink(link1));
		assertTrue(ns1.getLinkCount() == 1);
		assertTrue(ns1.getLinkableCount() == 3);
		Link anotherLink = ns1.addDefaultLink(node1, node2,
				link1.getCategory(), 1.0, 0.0);
		assertNotNull(anotherLink);
		assertTrue(ns1.getLinkCount() == 1);
		assertTrue(ns1.getLinkableCount() == 3);
		assertEquals(1.0, anotherLink.getActivation(), epsilon);
	}

	/**
	 * {@link NodeStructureImpl#addDefaultLink(Link)}
	 */
	@Test
	public void testAddGroudingPamLink() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);

		PamLink pl = (PamLink) factory.getLink("PamLinkImpl",
				link1.getSource(), link1.getSink(), link1.getCategory());
		link1.setGroundingPamLink(pl);
		Link stored = ns1.addDefaultLink(link1);

		assertEquals(pl, stored.getGroundingPamLink());
	}

	/**
	 * {@link NodeStructureImpl#addLink(Link, String)}
	 */
	@Test
	public void testAddLinkType() {
		assertNull(ns1.addLink(null, "LinkImpl"));
		assertNull(ns1.addLink(link1, null));
		assertNull(ns1.addLink(link1, "badtype"));
		assertNull(ns1.addLink(link1, "LinkImpl"));

		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		link1.setActivation(0.1);
		Link l = ns1.addLink(link1, "LinkImpl");
		assertTrue(ns1.containsLink(l));
		assertEquals(1, ns1.getLinkCount());
		assertNotNull(l);
		assertEquals(0.1, l.getActivation(), epsilon);

		link1.setActivation(0.2);
		l = ns1.addLink(link1, "LinkImpl");
		assertEquals(1, ns1.getLinkCount());
		assertEquals(0.2, l.getActivation(), epsilon);
		assertNull(ns1.addLink(link1, "PamLinkImpl"));

		ns1.addDefaultNode(node3);
		PamLink gpl = (PamLink) factory.getLink("PamLinkImpl", link2
				.getSource(), link2.getSink(), link2.getCategory());
		link2.setGroundingPamLink(gpl);
		PamLink pl = (PamLink) ns1.addLink(link2, "PamLinkImpl");
		assertTrue(ns1.containsLink(pl));
		assertEquals(2, ns1.getLinkCount());
		assertSame(gpl, pl.getGroundingPamLink());
		assertNull(ns1.addLink(link2, "LinkImpl"));
	}

	/**
	 * {@link NodeStructureImpl#addLink(String, int, ExtendedId, LinkCategory, double, double)}
	 */
	@Test
	public void testAddLinkNewType() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		Link l = ns1.addLink("LinkImpl", node1.getId(), node2.getExtendedId(),
				category1, 0.1, 0.2);
		assertTrue(l instanceof LinkImpl);
		assertTrue(ns1.containsLink(l));
		assertEquals(1, ns1.getLinkCount());
		assertEquals(node1, l.getSource());
		assertEquals(node2, l.getSink());
		assertEquals(category1, l.getCategory());
		assertEquals(0.1, l.getActivation(), epsilon);
		assertEquals(0.2, l.getActivatibleRemovalThreshold(), epsilon);
		assertNull(l.getGroundingPamLink());
	}

	/**
	 * {@link NodeStructureImpl#addLink(String, int, ExtendedId, LinkCategory, double, double)}
	 */
	@Test
	public void testAddLinkNewType1() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		PamLink l = (PamLink) ns1.addLink("PamLinkImpl", node1.getId(), node2
				.getExtendedId(), category1, 0.1, 0.2);
		assertTrue(l instanceof PamLinkImpl);
		assertTrue(ns1.containsLink(l));
		assertEquals(1, ns1.getLinkCount());
		assertEquals(node1, l.getSource());
		assertEquals(node2, l.getSink());
		assertEquals(category1, l.getCategory());
		assertEquals(0.1, l.getActivation(), epsilon);
		assertEquals(0.2, l.getActivatibleRemovalThreshold(), epsilon);
		assertEquals(l, l.getGroundingPamLink());
	}

	/**
	 * {@link NodeStructureImpl#addLink(String, Node, Linkable, LinkCategory, double, double)}
	 */
	@Test
	public void testAddLinkNewType2() {
		Node src = null;
		Linkable sink = null;
		assertNull(ns1.addLink("LinkImpl", src, sink, category1, 0, 0));
	}

	/**
	 * Tests the following for null and bad input:
	 * {@link NodeStructureImpl#addDefaultLink(int, ExtendedId, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addDefaultLink(int, int, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addDefaultLink(Node, Linkable, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addLink(String, int, ExtendedId, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addLink(String, Node, Linkable, LinkCategory, double, double)}
	 */
	@Test
	public void testAddNewLinkMethods() {
		double a = 0.1;
		double rt = 0.2;
		LinkCategory cat = category1;
		String type = "LinkImpl";
		Node src = node1;
		Linkable sink = node2;
		ExtendedId sinkId = node2.getExtendedId();
		int sinkId2 = node2.getId();
		int srcInt = node1.getId();

		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		Link l = null;

		l = ns1.addDefaultLink(srcInt, null, cat, a, rt);
		assertNull(l);
		l = ns1.addDefaultLink(srcInt, sinkId, null, a, rt);
		assertNull(l);
		//
		l = ns1.addDefaultLink(srcInt, sinkId2, null, a, rt);
		assertNull(l);
		//
		l = ns1.addDefaultLink(src, sink, null, a, rt);
		assertNull(l);
		l = ns1.addDefaultLink(null, sink, cat, a, rt);
		assertNull(l);
		l = ns1.addDefaultLink(src, null, cat, a, rt);
		assertNull(l);
		//
		l = ns1.addLink(type, srcInt, sinkId, null, a, rt);
		assertNull(l);
		l = ns1.addLink(type, srcInt, null, cat, a, rt);
		assertNull(l);
		l = ns1.addLink("BADTYPE", srcInt, sinkId, cat, a, rt);
		assertNull(l);
		//
		l = ns1.addLink(type, null, sink, cat, a, rt);
		assertNull(l);
		l = ns1.addLink(type, src, null, cat, a, rt);
		assertNull(l);
		l = ns1.addLink(type, src, sink, null, a, rt);
		assertNull(l);
		l = ns1.addLink("BADTYPE", src, sink, cat, a, rt);
		assertNull(l);
	}

	/**
	 * Tests the following method requesting links whose src and sink are not
	 * present:
	 * {@link NodeStructureImpl#addDefaultLink(int, ExtendedId, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addDefaultLink(int, int, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addDefaultLink(Node, Linkable, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addLink(String, int, ExtendedId, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addLink(String, Node, Linkable, LinkCategory, double, double)}
	 */
	@Test
	public void testAddNewLinkMethods1() {
		double a = 0.1;
		double rt = 0.2;
		LinkCategory cat = category1;
		String type = "LinkImpl";
		Node src = node1;
		Linkable sink = node2;
		ExtendedId sinkId = node2.getExtendedId();
		int sinkId2 = node2.getId();
		int srcInt = node1.getId();

		Link l = null;
		l = ns1.addDefaultLink(srcInt, sinkId, cat, a, rt);
		assertNull(l);
		l = ns1.addDefaultLink(srcInt, sinkId2, cat, a, rt);
		assertNull(l);
		l = ns1.addDefaultLink(src, sink, cat, a, rt);
		assertNull(l);
		l = ns1.addLink(type, src, sink, cat, a, rt);
		assertNull(l);
		l = ns1.addLink(type, srcInt, sinkId, cat, a, rt);
		assertNull(l);
	}

	/**
	 * Tests the following method requesting links whose sink is a complex link:
	 * {@link NodeStructureImpl#addDefaultLink(int, ExtendedId, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addDefaultLink(int, int, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addDefaultLink(Node, Linkable, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addLink(String, int, ExtendedId, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addLink(String, Node, Linkable, LinkCategory, double, double)}
	 */
	@Test
	public void testAddNewLinkMethods2() {
		double a = 0.1;
		double rt = 0.2;
		LinkCategory cat = category1;
		String type = "LinkImpl";

		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultNode(node4);
		Link simple = ns1.addDefaultLink(link1);
		Link complex = ns1.addDefaultLink(node3, simple, cat, a, rt);

		Node src = node4;
		Linkable sink = complex;
		int srcInt = node4.getId();
		ExtendedId sinkId = sink.getExtendedId();

		Link l = null;
		l = ns1.addDefaultLink(srcInt, sinkId, cat, a, rt);
		assertNull(l);
		l = ns1.addDefaultLink(src, sink, cat, a, rt);
		assertNull(l);
		l = ns1.addLink(type, src, sink, cat, a, rt);
		assertNull(l);
		l = ns1.addLink(type, srcInt, sinkId, cat, a, rt);
		assertNull(l);
	}

	/**
	 * Tests the following method requesting links whose src and sink are the
	 * same:
	 * {@link NodeStructureImpl#addDefaultLink(int, ExtendedId, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addDefaultLink(int, int, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addDefaultLink(Node, Linkable, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addLink(String, int, ExtendedId, LinkCategory, double, double)}
	 * {@link NodeStructureImpl#addLink(String, Node, Linkable, LinkCategory, double, double)}
	 */
	@Test
	public void testAddNewLinkMethods3() {
		double a = 0.1;
		double rt = 0.2;
		LinkCategory cat = category1;
		String type = "LinkImpl";
		Node src = node1;
		Linkable sink = node1;
		int srcInt = src.getId();
		ExtendedId sinkId = sink.getExtendedId();
		int sinkId2 = node1.getId();

		ns1.addDefaultNode(node1);
		Link l = null;

		l = ns1.addDefaultLink(srcInt, sinkId, cat, a, rt);
		assertNull(l);
		l = ns1.addDefaultLink(srcInt, sinkId2, cat, a, rt);
		assertNull(l);
		l = ns1.addDefaultLink(src, sink, cat, a, rt);
		assertNull(l);
		l = ns1.addLink(type, src, sink, cat, a, rt);
		assertNull(l);
		l = ns1.addLink(type, srcInt, sinkId, cat, a, rt);
		assertNull(l);
	}

	/**
	 * {@link NodeStructureImpl#clearNodeStructure()}
	 */
	@Test
	public void testClearNodeStructure() {
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

	/**
	 * {@link NodeStructureImpl#clearLinks()}
	 */
	@Test
	public void testClearLinks() {
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
	 * {@link NodeStructureImpl#removeLink(Link)}
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

	/**
	 * {@link NodeStructureImpl#removeLink(Link)}
	 */
	@Test
	public void testRemoveLink2() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		Link foo = ns1.addDefaultLink(node1.getId(), node2.getId(), category1,
				0.0, 0.0);
		Link bar = ns1.addDefaultLink(node1.getId(), node2.getId(), category2,
				0.0, 0.0);

		assertTrue(ns1.getLinkCount() == 2);
		ns1.removeLink(foo);
		assertTrue(ns1.getLinkCount() == 1);
		ns1.removeLink(bar);
		assertTrue(ns1.getLinkCount() == 0);

		Set<Link> links = ns1.getAttachedLinks(node1);
		assertTrue(links.size() == 0);

		links = ns1.getAttachedLinks(node2);
		assertTrue(links.size() == 0);
	}

	/**
	 * {@link NodeStructureImpl#removeNode(Node)}
	 */
	@Test
	public void testRemoveNode0() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		Link foo = ns1.addDefaultLink(node1.getId(), node2.getId(), category1,
				0.0, 0.0);

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

		// remove other node this time

		ns1.addDefaultNode(node1);
		foo = ns1.addDefaultLink(node1.getId(), node2.getId(), category1, 0.0,
				0.0);
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
	 * {@link NodeStructureImpl#removeNode(Node)}
	 */
	@Test
	public void testRemoveNode() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);

		ns1.removeNode(node3);
		assertTrue("Problem with deleteNode", !ns1.containsNode(node3));
	}

	/**
	 * {@link NodeStructureImpl#removeLinkable(Linkable)}
	 */
	@Test
	public void testRemoveLinkable() {
		ns1.addDefaultNode(node1);
		Node stored2 = ns1.addDefaultNode(node2);
		Link stored3 = ns1.addDefaultLink(node1.getId(), node2.getId(),
				category2, 0.0, 0.0);
		assertTrue(ns1.getNodeCount() == 2 && ns1.getLinkCount() == 1);

		ns1.removeLinkable(stored2);
		assertTrue(ns1.getNodeCount() == 1 && ns1.getLinkCount() == 0);
		assertFalse(ns1.containsNode(stored2.getId()));

		ns1.clearNodeStructure();
		ns1.addDefaultNode(node1);
		stored2 = ns1.addDefaultNode(node2);
		stored3 = ns1.addDefaultLink(node1.getId(), node2.getId(), category2,
				0.0, 0.0);

		ns1.removeLinkable(stored3);

		assertTrue(ns1.getLinkableCount() == 2);
		assertTrue(ns1.getLinkCount() == 0);
		assertTrue(ns1.getNodeCount() == 2);
	}

	/**
	 * {@link NodeStructureImpl#removeLinkable(ExtendedId)}
	 */
	@Test
	public void testRemoveLinkable2() {
		ns1.addDefaultNode(node1);

		ns1.removeLinkable(node1.getExtendedId());

		assertTrue(ns1.getNodeCount() == 0);
		assertTrue(ns1.getLinkableCount() == 0);
		assertFalse(ns1.containsNode(node1));

		ns1.removeLinkable(node1.getExtendedId());

		assertTrue(ns1.getNodeCount() == 0);
		assertTrue(ns1.getLinkableCount() == 0);
		assertFalse(ns1.containsNode(node1));

		// test 2

		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		Link tempLink = ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);

		ns1.removeLinkable(tempLink);

		assertTrue(ns1.getNodeCount() == 2);
		assertTrue(ns1.getLinkCount() == 0);
		assertTrue(ns1.getLinkableCount() == 2);

		ns1.removeLinkable(tempLink);

		assertTrue(ns1.getNodeCount() == 2);
		assertTrue(ns1.getLinkCount() == 0);
		assertTrue(ns1.getLinkableCount() == 2);
	}

	/**
	 * {@link NodeStructureImpl#removeLinkable(Linkable)}
	 */
	@Test
	public void testRemoveLinkLink() {
		// setup
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		Link l12 = ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		ns1.addDefaultLink(node3, l12, category1, 0.0, 0.0);

		// code tested
		ns1.removeLinkable(l12);

		// check results
		assertTrue(ns1.getLinkableCount() == 3);
		assertTrue(ns1.getLinkCount() == 0);
		assertTrue(ns1.getNodeCount() == 3);
	}

	/**
	 * {@link NodeStructureImpl#removeLinkable(Linkable)}
	 */
	@Test
	public void testRemoveLinkLink2() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		Link l12 = ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		Link l312 = ns1.addDefaultLink(node3, l12, category1, 0.0, 0.0);

		ns1.removeLinkable(l312);

		assertTrue(ns1.getLinkableCount() == 4);
		assertTrue(ns1.getLinkCount() == 1);
		assertTrue(ns1.getNodeCount() == 3);
	}

	/**
	 * {@link NodeStructureImpl#copy()}
	 */
	@Test
	public void testCopy() {
		node2.setActivatibleRemovalThreshold(0.666);
		node2.setActivation(0.777);
		PamNode pn = (PamNode) factory.getNode("PamNodeImpl");
		node2.setGroundingPamNode(pn);
		node2.setLabel("FOO");

		ns3.addDefaultNode(node1);
		ns3.addDefaultNode(node2);
		ns3.addDefaultNode(node3);

		PamLink pl = (PamLink) factory.getLink("PamLinkImpl",
				link1.getSource(), link1.getSink(), link1.getCategory());
		link1.setGroundingPamLink(pl);
		link1.setActivation(0.111);
		link1.setActivatibleRemovalThreshold(0.00001);
		ns3.addDefaultLink(link1);
		ns3.addDefaultLink(node2.getId(), node1.getId(), category1, 0.0, 0.0);

		NodeStructure copy = ns3.copy();

		assertTrue(copy.getNodeCount() == 3);
		assertTrue(copy.getLinkCount() == 2);
		Collection<Link> links = copy.getAttachedLinks(node1);
		assertTrue(links.size() == 2);
		links = copy.getAttachedLinks(node2);
		assertTrue(links.size() == 2);
		assertTrue(copy.getAttachedLinks(node3).size() == 0);

		Node copiedNode2 = copy.getNode(node2.getId());
		assertTrue(copiedNode2.getActivatibleRemovalThreshold() == node2
				.getActivatibleRemovalThreshold());
		assertTrue(copiedNode2.getActivation() == node2.getActivation());
		assertEquals(copiedNode2.getGroundingPamNode(), node2
				.getGroundingPamNode());
		assertEquals(copiedNode2.getLabel(), node2.getLabel());

		Link copiedLink1 = copy.getLink(link1.getExtendedId());
		assertTrue(copiedLink1.getActivatibleRemovalThreshold() == link1
				.getActivatibleRemovalThreshold());
		assertTrue(copiedLink1.getActivation() == link1.getActivation());
		assertTrue(copiedLink1.getTotalActivation() == link1
				.getTotalActivation());
		assertEquals(copiedLink1.getCategory(), link1.getCategory());
		assertEquals(copiedLink1.getGroundingPamLink(), link1
				.getGroundingPamLink());
		assertEquals(copiedLink1.getSink(), link1.getSink());
		assertEquals(copiedLink1.getSource(), link1.getSource());
	}

	/**
	 * {@link NodeStructureImpl#copy()}
	 */
	@Test
	public void testCopy2() {
		double testActivation = 0.69;
		double testRemovable = 0.99;
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultNode(node4);

		Link link34 = ns1.addDefaultLink(node3, node4, category1,
				testActivation, testRemovable);
		Link link234 = ns1.addDefaultLink(node2, link34, category1,
				testActivation, testRemovable);
		Link link12 = ns1.addDefaultLink(node1, node2, category1,
				testActivation, testRemovable);

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
		assertTrue(copy.containsLink(link12));

		assertTrue(copy.getAttachedLinks(node1).size() == 1);
		assertTrue(copy.getAttachedLinks(node2).size() == 2);
		assertTrue(copy.getAttachedLinks(node3).size() == 1);
		assertTrue(copy.getAttachedLinks(node4).size() == 1);

		Link testRez = copy.getLink(link34.getExtendedId());
		assertTrue(testRez.getSource().equals(node3)
				&& testRez.getSink().equals(node4));
		assertTrue(testRez.getActivation() == testActivation);
		assertTrue(testRez.getActivatibleRemovalThreshold() == testRemovable);

		Link testRez2 = copy.getLink(link234.getExtendedId());
		assertTrue(testRez2.getSource().equals(node2)
				&& testRez2.getSink().equals(link34));
		assertTrue(testRez2.getActivation() == testActivation);
		assertTrue(testRez2.getActivatibleRemovalThreshold() == testRemovable);

		Link testRez3 = copy.getLink(link12.getExtendedId());
		assertTrue(testRez3.getSource().equals(node1)
				&& testRez3.getSink().equals(node2));
		assertTrue(testRez3.getActivation() == testActivation);
		assertTrue(testRez3.getActivatibleRemovalThreshold() == testRemovable);
	}

	/**
	 * {@link NodeStructureImpl#mergeWith(NodeStructure)}
	 */
	@Test
	public void testMerge() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultLink(node1.getId(), node2.getId(), category1, 0.0, 0.0);

		ns2.addDefaultNode(node1);
		ns2.addDefaultNode(node2);
		ns2.addDefaultLink(node1.getId(), node2.getId(), category1, 0.0, 0.0);

		ns1.mergeWith(ns2);
		assertTrue(ns1.getNodeCount() == 2);
		assertTrue(ns1.getLinkCount() == 1);

		ns2.mergeWith(ns1);

		assertTrue(ns2.getNodeCount() == 2);
		assertTrue(ns2.getLinkCount() == 1);
	}

	/**
	 * {@link NodeStructureImpl#mergeWith(NodeStructure)}
	 */
	@Test
	public void testMerge2() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultLink(node1.getId(), node2.getId(), category1, 0.0, 0.0);

		ns2.addDefaultNode(node2);
		ns2.addDefaultNode(node3);
		ns2.addDefaultNode(node4);
		Link l23 = ns2.addDefaultLink(node2.getId(), node3.getId(), category1,
				0.0, 0.0);
		ns2.addDefaultLink(node4.getId(), l23.getExtendedId(), category1, 0.0,
				0.0);

		ns1.mergeWith(ns2);
		assertTrue(ns1.getNodeCount() == 4);
		assertTrue(ns1.getLinkCount() == 3);

		assertTrue(ns1.getAttachedLinks(node2).size() == 2);

		ns2.mergeWith(ns1);
		assertTrue(ns2.getNodeCount() == 4);
		assertTrue(ns2.getLinkCount() == 3);

		assertTrue(ns2.getAttachedLinks(node2).size() == 2);
	}

	/**
	 * {@link NodeStructureImpl#mergeWith(NodeStructure)}
	 */
	@Test
	public void testMerge3() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultLink(node1.getId(), node2.getId(), category1, 0.0, 0.0);

		ns2.addDefaultNode(node2);
		ns2.addDefaultNode(node3);
		ns2.addDefaultNode(node4);
		Node node5 = factory.getNode();
		ns2.addDefaultNode(node5);

		Link l45 = ns2.addDefaultLink(node4, node5, category1, 0.0, 0.0);
		ns2.addDefaultLink(node3, l45, category1, 0.0, 0.0);
		ns2.addDefaultLink(node2, node3, category1, 0.0, 0.0);

		ns1.mergeWith(ns2);
		assertTrue(ns1.getLinkCount() == 4);
		assertTrue(ns1.getNodeCount() == 5);
		assertTrue(ns1.getAttachedLinks(node1).size() == 1);
		assertTrue(ns1.getAttachedLinks(node2).size() == 2);
		assertTrue(ns1.getAttachedLinks(node3).size() == 2);
		assertTrue(ns1.getAttachedLinks(node4).size() == 1);
		assertTrue(ns1.getAttachedLinks(node5).size() == 1);

		ns2.mergeWith(ns1);
		assertTrue(ns2.getLinkCount() == 4);
		assertTrue(ns2.getNodeCount() == 5);
		assertTrue(ns2.getAttachedLinks(node1).size() == 1);
		assertTrue(ns2.getAttachedLinks(node2).size() == 2);
		assertTrue(ns2.getAttachedLinks(node3).size() == 2);
		assertTrue(ns2.getAttachedLinks(node4).size() == 1);
		assertTrue(ns2.getAttachedLinks(node5).size() == 1);
	}

	/**
	 * {@link NodeStructureImpl#mergeWith(NodeStructure)}
	 */
	@Test
	public void testMergeDifferentTypes() {
		Node pn1 = ns1.addNode("PamNodeImpl", "pn1", 0, 0);
		assertTrue(pn1 instanceof PamNodeImpl);
		ns2.addDefaultNode(node1);

		assertEquals(1, ns1.getNodeCount());
		ns1.mergeWith(ns2);
		assertEquals(2, ns1.getNodeCount());
		assertTrue(ns1.getNode(node1.getId()) instanceof NodeImpl);
	}

	/**
	 * {@link NodeStructureImpl#mergeWith(NodeStructure)}
	 */
	@Test
	public void testMergeDifferentTypes1() {
		Node pn1 = ns1.addNode("PamNodeImpl", "pn1", 0, 0);
		ns2.addDefaultNode(node1);

		assertEquals(1, ns2.getNodeCount());
		ns2.mergeWith(ns1);
		assertEquals(2, ns2.getNodeCount());
		assertTrue(ns2.getNode(pn1.getId()) instanceof PamNodeImpl);
	}

	/**
	 * {@link NodeStructureImpl#mergeWith(NodeStructure)}
	 */
	@Test
	public void testMergeDifferentTypes2() {
		Node pn1 = ns1.addNode("PamNodeImpl", "pn1", 0, 0);
		ns1.addDefaultNode(node1);
		Link l1 = ns1.addDefaultLink(node1, pn1, category1, 0, 0);
		//
		ns2.addNode(pn1, "PamNodeImpl");
		ns2.addDefaultNode(node1);
		ns1.addLink("PamLinkImpl", node1, pn1, category1, 0, 0);

		ns1.mergeWith(ns2);

		assertEquals(2, ns1.getNodeCount());
		assertEquals(1, ns1.getLinkCount());
		assertTrue(ns1.containsLink(l1));
	}

	/**
	 * {@link NodeStructureImpl#mergeWith(NodeStructure)}
	 */
	@Test
	public void testMergeDifferentTypes3() {
		Node pn1 = ns1.addNode("PamNodeImpl", "pn1", 0, 0);
		ns1.addDefaultNode(node1);
		Link l1 = ns1.addDefaultLink(node1, pn1, category1, 0, 0);
		//
		ns2.addNode(pn1, "PamNodeImpl");
		ns2.addDefaultNode(node1);
		Link l2 = ns2.addLink("PamLinkImpl", node1, pn1, category2, 0, 0);

		ns1.mergeWith(ns2);

		assertEquals(2, ns1.getNodeCount());
		assertEquals(2, ns1.getLinkCount());
		assertTrue(ns1.containsLink(l1));
		assertTrue(ns1.containsLink(l2));
		assertTrue(ns1.getLink(l1.getExtendedId()) instanceof LinkImpl);
		assertTrue(ns1.getLink(l2.getExtendedId()) instanceof PamLinkImpl);
	}

	/**
	 * {@link NodeStructureImpl#copy()}
	 */
	@Test
	public void testCopyDifferentTypes() {
		factory.addNodeType("specialNode",
				"edu.memphis.ccrg.lida.pam.PamNodeImplSubclass");
		factory.addLinkType("specialLink",
				"edu.memphis.ccrg.lida.pam.PamLinkImplSubclass");
		Node n1 = ns1.addNode("PamNodeImpl", "", 0, 0);
		Node n2 = ns1.addDefaultNode("", 0, 0);
		Node n3 = ns1.addNode("specialNode", "", 0, 0);
		Link l1 = ns1.addDefaultLink(n1, n2, category1, 0, 0);
		Link l2 = ns1.addLink("PamLinkImpl", n2, n3, category1, 0, 0);
		Link l3 = ns1.addDefaultLink(n3, n1, category1, 0, 0);
		Link complexLink = ns1.addLink("specialLink", n1, l2, category1, 0, 0);

		NodeStructure copy = ns1.copy();
		assertEquals(7, copy.getLinkableCount());
		assertEquals(3, copy.getNodeCount());
		assertEquals(4, copy.getLinkCount());
		Node n = copy.getNode(n1.getId());
		assertTrue(n instanceof PamNodeImpl);
		n = copy.getNode(n2.getId());
		assertTrue(n instanceof NodeImpl);
		n = copy.getNode(n3.getId());
		assertTrue(n instanceof PamNodeImplSubclass);

		Link l = copy.getLink(l2.getExtendedId());
		assertTrue(l instanceof PamLink);
		assertTrue(copy.containsLink(l1));
		assertTrue(copy.containsLink(l2));
		assertTrue(copy.containsLink(l3));
		assertTrue(copy.containsLink(complexLink));
		l = copy.getLink(complexLink.getExtendedId());
		assertTrue(l instanceof PamLinkImplSubclass);
	}

	/**
	 * {@link NodeStructureImpl#decayNodeStructure(long)}
	 */
	@Test
	public void testDecayNodeStructure() {
		double activationAmount = 0.1;
		double removableThresh = 0.05;
		NodeStructure ns = new NodeStructureImpl();

		node1.setDecayStrategy(new LinearDecayStrategy());
		node1.setActivatibleRemovalThreshold(removableThresh);
		node1.setActivation(activationAmount);
		Node storedNode1 = ns.addDefaultNode(node1);

		node2.setDecayStrategy(new LinearDecayStrategy());
		node2.setActivatibleRemovalThreshold(removableThresh);
		node2.setActivation(1.0);
		Node storedNode2 = ns.addDefaultNode(node2);

		node3.setDecayStrategy(new LinearDecayStrategy());
		node3.setActivatibleRemovalThreshold(removableThresh);
		node3.setActivation(1.0);
		ns.addDefaultNode(node3);

		Link storedLink = ns.addDefaultLink(node3, node2, category1, 0.1, 0.0);

		assertTrue(activationAmount == storedNode1.getActivation());
		assertTrue(removableThresh == storedNode1
				.getActivatibleRemovalThreshold());
		assertFalse(storedNode1.isRemovable());
		assertFalse(storedNode2.isRemovable());

		ns.decayNodeStructure(3);

		assertFalse(ns.containsNode(storedNode1));
		assertTrue(ns.containsNode(storedNode2));
		assertFalse(ns.containsLink(storedLink));
	}

	/**
	 * {@link NodeStructureImpl#decayNodeStructure(long)}
	 */
	@Test
	public void testDecayNodeStructure1() {
		double activationAmount = 0.1;
		double removableThresh = 0.05;
		NodeStructure ns = new NodeStructureImpl();

		node1.setDecayStrategy(new LinearDecayStrategy());
		node1.setActivatibleRemovalThreshold(removableThresh);
		node1.setActivation(activationAmount);
		Node storedNode1 = ns.addDefaultNode(node1);

		node2.setDecayStrategy(new LinearDecayStrategy());
		node2.setActivatibleRemovalThreshold(removableThresh);
		node2.setActivation(1.0);
		Node storedNode2 = ns.addDefaultNode(node2);

		Link storedLink = ns.addDefaultLink(node1, node2, category1, 0.1, 0.0);

		assertTrue(activationAmount == storedNode1.getActivation());
		assertTrue(removableThresh == storedNode1
				.getActivatibleRemovalThreshold());
		assertFalse(storedNode1.isRemovable());
		assertFalse(storedNode2.isRemovable());

		ns.decayNodeStructure(3);

		assertFalse(ns.containsNode(storedNode1));
		assertTrue(ns.containsNode(storedNode2));
		assertFalse(ns.containsLink(storedLink));
	}

	/**
	 * {@link NodeStructureImpl#decayNodeStructure(long)}
	 */
	@Test
	public void testDecayNodeStructure2() {
		node1.setActivation(1.0);
		node2.setActivation(1.0);
		// weak node
		node3.setActivation(0.2);
		node4.setActivation(1.0);

		node1.setActivatibleRemovalThreshold(0.0);
		node2.setActivatibleRemovalThreshold(0.0);
		node3.setActivatibleRemovalThreshold(0.0);
		node4.setActivatibleRemovalThreshold(0.0);
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultNode(node4);

		// weak link
		ns1.addDefaultLink(node1, node2, category1, 0.1, 0.0);
		// strong links
		ns1.addDefaultLink(node2, node3, category1, 1.0, 0.0);
		ns1.addDefaultLink(node3, node4, category1, 1.0, 0.0);

		ns1.decayNodeStructure(2);

		assertEquals(3, ns1.getLinkableCount());
		assertEquals(3, ns1.getNodeCount());
		assertTrue(ns1.getLinkCount() == 0);
	}

	/**
	 * This method is used to test the NodeStructureImpl.getLinksByType() method
	 */
	@Test
	public void testGetLinksByType() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);

		ns1.addDefaultLink(node1, node2, category2, 0.0, 0.0);
		ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		ns1.addDefaultLink(node2, node1, category1, 0.0, 0.0);

		ns1.addDefaultLink(node2, node3, category2, 0.0, 0.0);
		ns1.addDefaultLink(node3, node1, category1, 0.0, 0.0);

		Set<Link> links = ns1.getLinks(category1);
		assertTrue(links.size() == 3);

		links = ns1.getLinks(category2);
		assertTrue(links.size() == 2);

	}

	/**
	 * {@link NodeStructureImpl#getAttachedLinks(Linkable)}
	 */
	@Test
	public void testGetAttachedLinks() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);

		ns1.addDefaultLink(node1, node2, category2, 0.0, 0.0);
		ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		ns1.addDefaultLink(node2, node1, category1, 0.0, 0.0);

		assertTrue(ns1.getAttachedLinks(node1).size() == 3);
		assertTrue(ns1.getAttachedLinks(node2).size() == 3);
	}

	/**
	 * {@link NodeStructureImpl#getAttachedLinks(Linkable)}
	 */
	@Test
	public void testGetAttachedLinks2() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		Link foo = ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		Link bar = ns1.addDefaultLink(node3, foo, category1, 0.0, 0.0);
		assertTrue(ns1.getAttachedLinks(foo).size() == 1);
		assertTrue(ns1.getAttachedLinks(bar).size() == 0);
	}

	/**
	 * {@link NodeStructureImpl#getAttachedLinks(Linkable, LinkCategory)}
	 */
	@Test
	public void testGetAttachedLinksByType() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);

		ns1.addDefaultLink(node3, node2, category2, 0.0, 0.0);
		ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		ns1.addDefaultLink(node2, node1, category1, 0.0, 0.0);

		assertTrue(ns1.getAttachedLinks(node3, category1).size() == 0);
		assertTrue(ns1.getAttachedLinks(node3, category2).size() == 1);

		assertTrue(ns1.getAttachedLinks(node2, category1).size() == 2);
		assertTrue(ns1.getAttachedLinks(node2, category2).size() == 1);

		assertTrue(ns1.getAttachedLinks(node1, category1).size() == 2);
		assertTrue(ns1.getAttachedLinks(node1, category2).size() == 0);

	}

	/**
	 * {@link NodeStructureImpl#getConnectedSinks(Node)}
	 */
	@Test
	public void testGetConnectedSinks() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		ns1.addDefaultNode(node4);

		Link l12 = ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		Link l23 = ns1.addDefaultLink(node2, node3, category2, 0.0, 0.0);
		Link l24 = ns1.addDefaultLink(node2, node4, category1, 0.0, 0.0);

		Map<Linkable, Link> map = ns1.getConnectedSinks(node1);
		assertTrue(map.size() == 1);
		assertTrue(map.containsKey(node2));
		assertTrue(map.containsValue(l12));

		map = ns1.getConnectedSinks(node2);
		assertTrue(map.size() == 2);
		assertTrue(map.containsKey(node3));
		assertTrue(map.containsValue(l23));
		assertTrue(map.containsKey(node4));
		assertTrue(map.containsValue(l24));

		map = ns1.getConnectedSinks(node3);
		assertTrue(map.size() == 0);

		map = ns1.getConnectedSinks(node4);
		assertTrue(map.size() == 0);
	}

	/**
	 * {@link NodeStructureImpl#NodeStructureImpl(String, String)}
	 */
	@Test
	public void testDefaultTypes() {
		NodeStructure ns = new NodeStructureImpl("PamNodeImpl", "LinkImpl");
		assertEquals(ns.getDefaultNodeType(), "PamNodeImpl");
		assertEquals(ns.getDefaultLinkType(), "LinkImpl");

		ns = new NodeStructureImpl(ns);
		assertEquals(ns.getDefaultNodeType(), "PamNodeImpl");
		assertEquals(ns.getDefaultLinkType(), "LinkImpl");

		try {
			ns = new NodeStructureImpl("LinkImpl", "PamNodeImpl");
			assertFalse(true);
		} catch (IllegalArgumentException e) {

		}
	}

	/**
	 * {@link NodeStructureImpl#compareNodeStructures(NodeStructure, NodeStructure)}
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

		assertTrue(NodeStructureImpl.compareNodeStructures(ns1, ns2));
	}

	/**
	 * {@link NodeStructureImpl#compareNodeStructures(NodeStructure, NodeStructure)}
	 */
	@Test
	public void testCompareNodeStructures2() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		ns1.addDefaultLink(node2, node1, category1, 0.0, 0.0);

		ns2.addDefaultNode(node2);
		ns2.addDefaultNode(node1);
		ns2.addDefaultLink(node1, node2, category1, 0.0, 0.0);

		assertFalse(NodeStructureImpl.compareNodeStructures(ns1, ns2));
	}

	/**
	 * {@link NodeStructureImpl#compareNodeStructures(NodeStructure, NodeStructure)}
	 */
	@Test
	public void testCompareNodeStructures3() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);

		ns2.addDefaultNode(node1);
		ns2.addDefaultNode(node2);
		ns1.addDefaultLink(node1, node2, category2, 0.0, 0.0);

		assertFalse(NodeStructureImpl.compareNodeStructures(ns1, ns2));
	}

	/**
	 * {@link NodeStructureImpl#compareNodeStructures(NodeStructure, NodeStructure)}
	 */
	@Test
	public void testCompareNodeStructures4() {
		ns1 = new NodeStructureImpl("NodeImpl", "LinkImpl");
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);

		ns2 = new NodeStructureImpl("PamNodeImpl", "PamLinkImpl");
		ns2.addDefaultNode(node1);
		ns2.addDefaultNode(node2);
		ns2.addDefaultLink(node1, node2, category1, 0.0, 0.0);

		assertTrue(NodeStructureImpl.compareNodeStructures(ns1, ns2));
	}

	/**
	 * 
	 */
	@Test
	public void testWeirdStructure() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		link1 = (LinkImpl) ns1
				.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		Link w = ns1.addDefaultLink(node1, link1, category1, 0.0, 0.0);
		Link w2 = ns1.addDefaultLink(node2, link1, category1, 0.0, 0.0);
		assertFalse(w.isSimpleLink());
		assertFalse(w2.isSimpleLink());

		NodeStructure copy = ns1.copy();
		copy.removeNode(node1);
		assertEquals(0, copy.getLinkCount());

		assertEquals(3, ns1.getLinkCount());
		assertEquals(2, ns1.getNodeCount());
		ns1.removeLink(link1);
		assertEquals(0, ns1.getLinkCount());
		assertEquals(2, ns1.getNodeCount());
	}

	/**
	 * 
	 */
	@Test
	public void testWeirdStructure2() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		link1 = ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		Link foo = ns1.addDefaultLink(node2, link1, category1, 0.0, 0.0);
		Link bar = ns1.addDefaultLink(node1, foo, category1, 0.0, 0.0);
		assertNull(bar);
		assertEquals(2, ns1.getLinkCount());
	}

	/**
	 * 
	 */
	@Test
	public void testWeirdStructure3() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);
		Link l23 = ns1.addDefaultLink(node2, node3, category1, 0.0, 0.0);
		ns1.addDefaultLink(node1, node2, category1, 0.0, 0.0);
		ns1.addDefaultLink(node1, l23, category1, 0.0, 0.0);

		assertTrue(ns1.getLinkCount() == 3);
		assertTrue(ns1.getNodeCount() == 3);
		assertTrue(ns1.getAttachedLinks(node1).size() == 2);
		assertTrue(ns1.getAttachedLinks(node2).size() == 2);
		assertTrue(ns1.getAttachedLinks(node3).size() == 1);

		NodeStructure copy = ns1.copy();

		assertTrue(copy.getLinkCount() == 3);
		assertTrue(copy.getNodeCount() == 3);
		assertTrue(copy.getAttachedLinks(node1).size() == 2);
		assertTrue(copy.getAttachedLinks(node2).size() == 2);
		assertTrue(copy.getAttachedLinks(node3).size() == 1);

		copy.removeNode(node3);
		assertTrue(copy.getNodeCount() == 2);
		assertTrue(copy.getLinkCount() == 1);

		ns1.removeNode(node2);
		assertTrue(ns1.getNodeCount() == 2);
		assertTrue(ns1.getLinkCount() == 0);
	}

	/**
	 * {@link NodeStructureImpl#getConnectedSinks(Node)}
	 */
	@Test
	public void testGetConnectedSinks2() {
		ns1.addDefaultNode(node1);
		assertTrue(ns1.getConnectedSinks(node1).size() == 0);

		ns1.addDefaultNode(node2);
		ns1.addDefaultLink(node2, node1, category2, 0.0, 0.0);

		assertTrue(ns1.getConnectedSinks(node1).size() == 0);
		assertTrue(ns1.getConnectedSinks(node2).size() == 1);

		ns1.addDefaultNode(node3);
		Link l23 = ns1.addDefaultLink(node2, node3, category2, 0.0, 0.0);
		ns1.addDefaultLink(node1, l23, category2, 0.0, 0.0);
		ns1.addDefaultLink(node1, node2, category2, 0.0, 0.0);

		assertTrue(ns1.getConnectedSinks(node1).size() == 2);
		assertTrue(ns1.getConnectedSinks(node2).size() == 2);
		assertTrue(ns1.getConnectedSinks(node3).size() == 0);
	}

	/**
	 * {@link NodeStructureImpl#getConnectedSources(Linkable)}
	 */
	@Test
	public void testGetConnectedSources() {
		ns1.addDefaultNode(node1);
		ns1.addDefaultNode(node2);
		ns1.addDefaultNode(node3);

		Link l12 = ns1.addDefaultLink(node1, node2, category2, 0.0, 0.0);
		Link l23 = ns1.addDefaultLink(node2, node3, category2, 0.0, 0.0);
		Link l232 = ns1.addDefaultLink(node3, node2, category2, 0.0, 0.0);
		Link l312 = ns1.addDefaultLink(node3, l12, category2, 0.0, 0.0);

		assertTrue(ns1.getConnectedSources(node1).size() == 0);
		assertTrue(ns1.getConnectedSources(node2).size() == 2);
		assertTrue(ns1.getConnectedSources(node3).size() == 1);
		assertTrue(ns1.getConnectedSources(l12).size() == 1);
		assertTrue(ns1.getConnectedSources(l23).size() == 0);
		assertTrue(ns1.getConnectedSources(l232).size() == 0);
		assertTrue(ns1.getConnectedSources(l312).size() == 0);
	}

	/**
	 * get methods passing null
	 */
	@Test
	public void testGetWithNull() {
		assertNull(ns1.getNode(null));
		assertNull(ns1.getLink(null));
		assertNull(ns1.getLinkable(null));
		assertNull(ns1.getAttachedLinks(null));
		assertNull(ns1.getAttachedLinks(null, category1));
		assertNull(ns1.getAttachedLinks(node1, null));
		assertNull(ns1.getConnectedSinks(null));
		assertNull(ns1.getConnectedSources(null));
		assertNull(ns1.getLinks(null));
	}

	/**
	 * contains methods passing null
	 */
	@Test
	public void testContainsNull() {
		Link l = null;
		Linkable lnk = null;
		ExtendedId id = null;
		Node n = null;
		assertFalse(ns1.containsLink(l));
		assertFalse(ns1.containsLink(id));
		assertFalse(ns1.containsLinkable(id));
		assertFalse(ns1.containsLinkable(lnk));
		assertFalse(ns1.containsNode(id));
		assertFalse(ns1.containsNode(n));
	}
}