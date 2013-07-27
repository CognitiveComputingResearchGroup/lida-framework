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

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Linkable;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.UnmodifiableNodeStructureImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;

public class UnmodifiableNodeStructureImplTest {

	private ElementFactory factory;
	private Node aNode, bNode, cNode;
	private NodeStructure aa = new NodeStructureImpl();
	private NodeStructure xx = new NodeStructureImpl();
	private PamNode category;
	private UnmodifiableNodeStructureImpl uns;
	private UnmodifiableNodeStructureImpl filledUNS;
	private Node n1, n2, n3, n4, n5;

	private Link l23, l123, l43, l53;

	private Link noLink;
	private Node noNode;

	@Before
	public void setUp() throws Exception {
		factory = ElementFactory.getInstance();
		aNode = factory.getNode();
		aNode.setLabel("A");

		bNode = factory.getNode();
		bNode.setLabel("B");

		cNode = factory.getNode();
		cNode.setLabel("C");
		category = (PamNode) factory.getNode("PamNodeImpl");

		uns = new UnmodifiableNodeStructureImpl(new NodeStructureImpl(), true);

		NodeStructure source = new NodeStructureImpl();

		n1 = factory.getNode();
		n1.setLabel("1");
		source.addDefaultNode(n1);

		n2 = factory.getNode();
		n2.setLabel("2");
		source.addDefaultNode(n2);

		n3 = factory.getNode();
		n3.setLabel("3");
		source.addDefaultNode(n3);

		n4 = factory.getNode();
		n4.setLabel("4");
		source.addDefaultNode(n4);

		n5 = factory.getNode();
		n5.setLabel("5");
		source.addDefaultNode(n5);

		l23 = source.addDefaultLink(n2, n3, category, 0.0, 0.0);
		source.addDefaultLink(l23);
		l123 = source.addDefaultLink(n1, l23, category, 0.0, 0.0);
		source.addDefaultLink(l123);
		l43 = source.addDefaultLink(n4, n3, category, 0.0, 0.0);
		source.addDefaultLink(l43);
		l53 = source.addDefaultLink(n5, n3, category, 0.0, 0.0);
		source.addDefaultLink(l53);

		filledUNS = new UnmodifiableNodeStructureImpl(source);

		noNode = factory.getNode();
		noLink = factory.getLink(n3, bNode, category);
	}

	/**
	 * Test empty.
	 */
	@Test
	public void testEmptyEquality() {
		assertEqualsHashCode("Empty NS's: ", aa, xx, true);
	}

	/**
	 * Test add.
	 */
	@Test
	public void testAddEquality() {
		aa.addDefaultNode(aNode);
		xx.addDefaultNode(aNode);
		assertEqualsHashCode("Add 1 node: ", aa, xx, true);
	}

	/**
	 * Test copying.
	 */
	@Test
	public void testCopyingEquality() {
		aa.addDefaultNode(cNode);
		xx.addDefaultNode(cNode);
		NodeStructure cc = new NodeStructureImpl(aa);
		assertEqualsHashCode("NS copying: ", aa, cc, true);
		assertEqualsHashCode("NS copying: ", xx, cc, true);
	}

	/**
	 * Test add remove.
	 */
	@Test
	public void testAddRemoveEquality() {
		aa.addDefaultNode(aNode);
		aa.removeNode(aNode);
		xx.addDefaultNode(aNode);
		xx.removeNode(aNode);
		assertEqualsHashCode("Add/remove: ", aa, xx, true);
	}

	/**
	 * Test add link.
	 */
	@Test
	public void testAddLinkEquality() {
		aa.addDefaultNode(aNode);
		aa.addDefaultNode(bNode);
		aa.addDefaultLink(aNode.getId(), bNode.getExtendedId(), category, 0.0,
				0.0);

		xx.addDefaultNode(aNode);
		xx.addDefaultNode(bNode);
		assertEqualsHashCode("Add link: ", aa, xx, false);
	}

	/**
	 * Test add link2.
	 */
	@Test
	public void testAddLinkEquality2() {
		aa.addDefaultNode(aNode);
		aa.addDefaultNode(bNode);
		aa.addDefaultLink(aNode.getId(), bNode.getExtendedId(), category, 0.0,
				0.0);

		xx.addDefaultNode(aNode);
		xx.addDefaultNode(bNode);
		xx.addDefaultLink(aNode.getId(), bNode.getExtendedId(), category, 0.0,
				0.0);
		assertEqualsHashCode("Add link: ", aa, xx, true);
	}

	/**
	 * Test add link2.
	 */
	@Test
	public void testAddLinkEquality3() {
		aa.addDefaultNode(aNode);
		aa.addDefaultNode(bNode);
		aa.addDefaultNode(cNode);
		Link abLink = aa.addDefaultLink(aNode, bNode, category, 0.0, 0.0);
		aa.addDefaultLink(cNode, abLink, category, 0.0, 0.0);

		xx.addDefaultNode(aNode);
		xx.addDefaultNode(bNode);
		xx.addDefaultNode(cNode);
		abLink = xx.addDefaultLink(aNode.getId(), bNode.getExtendedId(),
				category, 0.0, 0.0);
		xx.addDefaultLink(cNode, abLink, category, 0.0, 0.0);
		assertEqualsHashCode("Add link: ", aa, xx, true);
	}

	/**
	 * Check equals hash code.
	 * 
	 * @param testName
	 *            the test name
	 * @param nsA
	 *            the ns a
	 * @param nsB
	 *            the ns b
	 * @param equalExpected
	 *            the equal expected
	 */
	private void assertEqualsHashCode(String testName, NodeStructure nsA,
			NodeStructure nsB, boolean equalExpected) {
		UnmodifiableNodeStructureImpl a = new UnmodifiableNodeStructureImpl(nsA);
		UnmodifiableNodeStructureImpl b = new UnmodifiableNodeStructureImpl(nsB);
		boolean aEqualsB = a.equals(b);
		boolean bEqualsA = b.equals(a);
		boolean hashCodesEqual = (a.hashCode() == b.hashCode());

		assertEquals(testName + ": A equals B", aEqualsB, equalExpected);
		assertEquals(testName + ": B equals A", bEqualsA, equalExpected);

		if (equalExpected) {
			assertTrue(testName + ": Hashcodes not equal but should be.",
					hashCodesEqual);
		}
	}

	@Test
	public void testUnmodifiableNodeStructureImplNodeStructureConstructor() {
		NodeStructure sourceNodeStructure = new NodeStructureImpl();
		sourceNodeStructure.addDefaultNode(aNode);

		UnmodifiableNodeStructureImpl a = new UnmodifiableNodeStructureImpl(
				sourceNodeStructure);

		assertEquals(1, a.getNodeCount());

		sourceNodeStructure.addDefaultNode(bNode);

		assertEquals(2, sourceNodeStructure.getNodeCount());
		assertEquals(2, a.getNodeCount());
	}

	@Test
	public void testUnmodifiableNodeStructureImplNodeStructureConstructor1() {
		NodeStructure sourceNodeStructure = new NodeStructureImpl();
		sourceNodeStructure.addDefaultNode(aNode);

		UnmodifiableNodeStructureImpl a = new UnmodifiableNodeStructureImpl(
				sourceNodeStructure, false);

		assertEquals(1, a.getNodeCount());

		sourceNodeStructure.addDefaultNode(bNode);

		assertEquals(2, sourceNodeStructure.getNodeCount());
		assertEquals(2, a.getNodeCount());

		// true case
		sourceNodeStructure = new NodeStructureImpl();
		sourceNodeStructure.addDefaultNode(aNode);
		a = new UnmodifiableNodeStructureImpl(sourceNodeStructure, true);

		assertEquals(1, a.getNodeCount());

		sourceNodeStructure.addDefaultNode(bNode);

		assertEquals(2, sourceNodeStructure.getNodeCount());
		assertEquals(1, a.getNodeCount());

		sourceNodeStructure.clearNodeStructure();

		assertEquals(0, sourceNodeStructure.getNodeCount());
		assertEquals(1, a.getNodeCount());
	}

	@Test
	public void testCopy() {
		NodeStructure sourceNodeStructure = new NodeStructureImpl();
		sourceNodeStructure.addDefaultNode(aNode);
		UnmodifiableNodeStructureImpl a = new UnmodifiableNodeStructureImpl(
				sourceNodeStructure, false);
		assertEquals(1, a.getNodeCount());

		NodeStructure copy = a.copy();

		assertEquals(1, copy.getNodeCount());
		assertEquals(1, a.getNodeCount());

		sourceNodeStructure.clearNodeStructure();

		assertEquals(1, copy.getNodeCount());
		assertEquals(0, a.getNodeCount());
	}

	@Test
	public void testAddLink() {
		try {
			uns.addLink(null, null);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testAddDefaultLinkLink() {
		try {
			uns.addDefaultLink(null);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testAddDefaultLinkIntExtendedIdLinkCategoryDoubleDouble() {
		try {
			uns.addDefaultLink(0, new ExtendedId(0), new PamNodeImpl(), 0.0,
					0.0);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testAddDefaultLinkIntIntLinkCategoryDoubleDouble() {
		try {
			uns.addDefaultLink(0, 0, new PamNodeImpl(), 0.0, 0.0);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testAddDefaultLinkConven() {
		try {
			uns.addDefaultLink(aNode, bNode, new PamNodeImpl(), 0.0, 0.0);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testAddDefaultLinks() {
		try {
			uns.addDefaultLinks(null);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testAddDefaultNode() {
		try {
			uns.addDefaultNode(aNode);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testAddNode() {
		try {
			uns.addNode(null, null);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testAddDefaultNodes() {
		try {
			uns.addDefaultNodes(null);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testRemoveLink() {
		try {
			uns.removeLink(null);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testRemoveLinkableLinkable() {
		try {
			uns.removeLinkable(new ExtendedId(0));
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testRemoveLinkableExtendedId() {
		try {
			uns.removeLinkable(new NodeImpl());
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testRemoveNode() {
		try {
			uns.removeNode(aNode);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testClearLinks() {
		try {
			uns.clearLinks();
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testClearNodeStructure() {
		try {
			uns.clearNodeStructure();
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testMergeWith() {
		try {
			uns.mergeWith(null);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testDecayNodeStructure() {
		try {
			uns.decayNodeStructure(0);
			assertTrue(false);
		} catch (UnsupportedOperationException e) {

		}
	}

	@Test
	public void testContainsLinkLink() {
		assertTrue(filledUNS.containsLink(l23));
		assertTrue(filledUNS.containsLink(l123));
		assertTrue(filledUNS.containsLink(l43));
		assertTrue(filledUNS.containsLink(l53));

		assertFalse(filledUNS.containsLink(noLink));
	}

	@Test
	public void testContainsLinkExtendedId() {
		assertTrue(filledUNS.containsLink(l23.getExtendedId()));
		assertTrue(filledUNS.containsLink(l123.getExtendedId()));
		assertTrue(filledUNS.containsLink(l43.getExtendedId()));
		assertTrue(filledUNS.containsLink(l53.getExtendedId()));

		assertFalse(filledUNS.containsLink(noLink.getExtendedId()));
	}

	@Test
	public void testContainsLinkableLinkable() {
		assertTrue(filledUNS.containsLinkable(l23));
		assertTrue(filledUNS.containsLinkable(l123));
		assertTrue(filledUNS.containsLinkable(l43));
		assertTrue(filledUNS.containsLinkable(l53));

		assertTrue(filledUNS.containsLinkable(n1));
		assertTrue(filledUNS.containsLinkable(n2));
		assertTrue(filledUNS.containsLinkable(n3));
		assertTrue(filledUNS.containsLinkable(n4));
		assertTrue(filledUNS.containsLinkable(n5));

		assertFalse(filledUNS.containsLinkable(noLink));
		assertFalse(filledUNS.containsLinkable(noNode));
	}

	@Test
	public void testContainsLinkableExtendedId() {
		assertTrue(filledUNS.containsLinkable(l23.getExtendedId()));
		assertTrue(filledUNS.containsLinkable(l123.getExtendedId()));
		assertTrue(filledUNS.containsLinkable(l43.getExtendedId()));
		assertTrue(filledUNS.containsLinkable(l53.getExtendedId()));

		assertTrue(filledUNS.containsLinkable(n1.getExtendedId()));
		assertTrue(filledUNS.containsLinkable(n2.getExtendedId()));
		assertTrue(filledUNS.containsLinkable(n3.getExtendedId()));
		assertTrue(filledUNS.containsLinkable(n4.getExtendedId()));
		assertTrue(filledUNS.containsLinkable(n5.getExtendedId()));

		assertFalse(filledUNS.containsLinkable(noLink.getExtendedId()));
		assertFalse(filledUNS.containsLinkable(noNode.getExtendedId()));
	}

	@Test
	public void testContainsNodeNode() {
		assertTrue(filledUNS.containsNode(n1));
		assertTrue(filledUNS.containsNode(n2));
		assertTrue(filledUNS.containsNode(n3));
		assertTrue(filledUNS.containsNode(n4));
		assertTrue(filledUNS.containsNode(n5));

		assertFalse(filledUNS.containsNode(noNode));
	}

	@Test
	public void testContainsNodeInt() {
		assertTrue(filledUNS.containsNode(n1.getId()));
		assertTrue(filledUNS.containsNode(n2.getId()));
		assertTrue(filledUNS.containsNode(n3.getId()));
		assertTrue(filledUNS.containsNode(n4.getId()));
		assertTrue(filledUNS.containsNode(n5.getId()));

		assertFalse(filledUNS.containsNode(noNode.getId()));
	}

	@Test
	public void testContainsNodeExtendedId() {
		assertTrue(filledUNS.containsNode(n1.getExtendedId()));
		assertTrue(filledUNS.containsNode(n2.getExtendedId()));
		assertTrue(filledUNS.containsNode(n3.getExtendedId()));
		assertTrue(filledUNS.containsNode(n4.getExtendedId()));
		assertTrue(filledUNS.containsNode(n5.getExtendedId()));

		assertFalse(filledUNS.containsNode(noNode.getExtendedId()));
	}

	@Test
	public void testGetAttachedLinksLinkable() {
		assertEquals(1, filledUNS.getAttachedLinks(n1).size());
		assertEquals(1, filledUNS.getAttachedLinks(n2).size());
		assertEquals(3, filledUNS.getAttachedLinks(n3).size());
		assertEquals(1, filledUNS.getAttachedLinks(n4).size());
		assertEquals(1, filledUNS.getAttachedLinks(n5).size());

		assertEquals(1, filledUNS.getAttachedLinks(l23).size());
		assertEquals(0, filledUNS.getAttachedLinks(l123).size());
		assertEquals(0, filledUNS.getAttachedLinks(l43).size());
		assertEquals(0, filledUNS.getAttachedLinks(l53).size());
	}

	@Test
	public void testGetAttachedLinksLinkableLinkCategory() {
		assertEquals(1, filledUNS.getAttachedLinks(n1, category).size());
		assertEquals(1, filledUNS.getAttachedLinks(n2, category).size());
		assertEquals(3, filledUNS.getAttachedLinks(n3, category).size());
		assertEquals(1, filledUNS.getAttachedLinks(n4, category).size());
		assertEquals(1, filledUNS.getAttachedLinks(n5, category).size());

		assertEquals(1, filledUNS.getAttachedLinks(l23, category).size());
		assertEquals(0, filledUNS.getAttachedLinks(l123, category).size());
		assertEquals(0, filledUNS.getAttachedLinks(l43, category).size());
		assertEquals(0, filledUNS.getAttachedLinks(l53, category).size());

		PamNodeImpl dumbCat = new PamNodeImpl();
		dumbCat.setId(Integer.MAX_VALUE);

		assertEquals(0, filledUNS.getAttachedLinks(n1, dumbCat).size());
		assertEquals(0, filledUNS.getAttachedLinks(n2, dumbCat).size());
		assertEquals(0, filledUNS.getAttachedLinks(n3, dumbCat).size());
		assertEquals(0, filledUNS.getAttachedLinks(n4, dumbCat).size());
		assertEquals(0, filledUNS.getAttachedLinks(n5, dumbCat).size());

		assertEquals(0, filledUNS.getAttachedLinks(l23, dumbCat).size());
		assertEquals(0, filledUNS.getAttachedLinks(l123, dumbCat).size());
		assertEquals(0, filledUNS.getAttachedLinks(l43, dumbCat).size());
		assertEquals(0, filledUNS.getAttachedLinks(l53, dumbCat).size());
	}

	@Test
	public void testGetConnectedSinks() {
		Map<Linkable, Link> sinks = filledUNS.getConnectedSinks(n3);
		assertEquals(0, sinks.size());

		sinks = filledUNS.getConnectedSinks(n5);
		assertEquals(1, sinks.size());
		assertTrue(sinks.containsKey(n3));
		assertTrue(sinks.containsValue(l53));
	}

	@Test
	public void testGetConnectedSources() {
		Map<Node, Link> sources = filledUNS.getConnectedSources(n3);
		assertEquals(3, sources.size());
		assertTrue(sources.containsKey(n2));
		assertTrue(sources.containsKey(n4));
		assertTrue(sources.containsKey(n5));

		sources = filledUNS.getConnectedSources(n5);
		assertEquals(0, sources.size());
	}

	@Test
	public void testGetDefaultLinkType() {
		assertEquals("LinkImpl", uns.getDefaultLinkType());
		assertEquals("LinkImpl", filledUNS.getDefaultLinkType());
	}

	@Test
	public void testGetDefaultNodeType() {
		assertEquals("NodeImpl", uns.getDefaultNodeType());
		assertEquals("NodeImpl", filledUNS.getDefaultNodeType());
	}

	@Test
	public void testGetLink() {
		assertEquals(l23, filledUNS.getLink(l23.getExtendedId()));
	}

	@Test
	public void testGetLinkCount() {
		assertEquals(4, filledUNS.getLinkCount());
	}

	@Test
	public void testGetLinkable() {
		assertEquals(n5, filledUNS.getLinkable(n5.getExtendedId()));
		assertEquals(l123, filledUNS.getLinkable(l123.getExtendedId()));
	}

	@Test
	public void testGetLinkableCount() {
		assertEquals(9, filledUNS.getLinkableCount());
	}

	@Test
	public void testGetLinkableMap() {
		Map<Linkable, Set<Link>> map = filledUNS.getLinkableMap();
		assertEquals(9, map.size());
	}

	@Test
	public void testGetLinkables() {
		Collection<Linkable> linkables = filledUNS.getLinkables();
		assertTrue(linkables.contains(n1));
		assertTrue(linkables.contains(n2));
		assertTrue(linkables.contains(n3));
		assertTrue(linkables.contains(n4));
		assertTrue(linkables.contains(n5));

		assertTrue(linkables.contains(l23));
		assertTrue(linkables.contains(l123));
		assertTrue(linkables.contains(l53));
		assertTrue(linkables.contains(l43));
	}

	@Test
	public void testGetLinks() {
		Collection<Link> links = filledUNS.getLinks();
		assertTrue(links.contains(l123));
		assertTrue(links.contains(l23));
		assertTrue(links.contains(l43));
		assertTrue(links.contains(l53));
	}

	@Test
	public void testGetLinksLinkCategory() {
		Collection<Link> links = filledUNS.getLinks(category);
		assertTrue(links.contains(l123));
		assertTrue(links.contains(l23));
		assertTrue(links.contains(l43));
		assertTrue(links.contains(l53));

		PamNodeImpl cat2 = new PamNodeImpl();
		cat2.setId(453458934);
		links = filledUNS.getLinks(cat2);
		assertTrue(!links.contains(l123));
		assertTrue(!links.contains(l23));
		assertTrue(!links.contains(l43));
		assertTrue(!links.contains(l53));
	}

	@Test
	public void testGetNodeInt() {
		assertEquals(n4, filledUNS.getNode(n4.getId()));
	}

	@Test
	public void testGetNodeExtendedId() {
		assertEquals(n3, filledUNS.getNode(n3.getExtendedId()));
	}

	@Test
	public void testGetNodeCount() {
		assertEquals(5, filledUNS.getNodeCount());
	}

	@Test
	public void testGetNodes() {
		Collection<Node> nodes = filledUNS.getNodes();
		assertTrue(nodes.contains(n1));
		assertTrue(nodes.contains(n2));
		assertTrue(nodes.contains(n3));
		assertTrue(nodes.contains(n4));
		assertTrue(nodes.contains(n5));
	}

}
