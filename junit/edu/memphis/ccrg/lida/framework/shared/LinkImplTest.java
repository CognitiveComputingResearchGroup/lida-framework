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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

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
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.pam.ns.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl;

/**
 * This is a JUnit class which can be used to test methods of the LinkImpl class
 * 
 * @author Siminder Kaur
 * 
 */
public class LinkImplTest {

	private Node node1, node2, node3, node4;
	private LinkImpl link1, link2, link3;
	private PamLinkImpl pamLink1;
	private LinkCategory linktype1;
	private static final ElementFactory factory = ElementFactory.getInstance();

	@BeforeClass
	public static void setUpBeforeClass() {
		Properties prop = ConfigUtils
				.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		FactoriesDataXmlLoader.loadFactoriesData(prop);
	}

	/**
	 * This method is called before running each test case to initialize the
	 * objects
	 * 
	 * @throws Exception
	 *             e
	 */
	@Before
	public void setUp() throws Exception {
		node1 = factory.getNode();
		node2 = factory.getNode();
		node3 = factory.getNode();
		node4 = factory.getNode();

		linktype1 = PerceptualAssociativeMemoryNSImpl.NONE;

		pamLink1 = (PamLinkImpl) factory.getLink("PamLinkImpl", node3, node4,
				PerceptualAssociativeMemoryNSImpl.NONE);

		link1 = (LinkImpl) factory.getLink(node1, node2,
				PerceptualAssociativeMemoryNSImpl.NONE);
		link2 = (LinkImpl) factory.getLink(node3, node4,
				PerceptualAssociativeMemoryNSImpl.NONE);
		link3 = (LinkImpl) factory.getLink(node3, link2,
				PerceptualAssociativeMemoryNSImpl.NONE);
	}

	/**
	 * This method is used to test the LinkImpl.equals() method
	 */
	@Test
	public void testEqualsObject() {
		link1.setSource(node1);
		link1.setSink(node2);
		link1.setCategory(linktype1);

		link3.setSource(node1);
		link3.setSink(node2);
		link3.setCategory(linktype1);

		assertHashCodeEquals(link1, link3);
	}

	/**
	 * This method is used to test the LinkImpl.getSink() method
	 */
	@Test
	public void testGetSink() {
		link1.setSink(node2);
		assertEquals("Problem with getSink", node2, link1.getSink());
	}

	/**
	 * This method is used to test the LinkImpl.getSource() method
	 */
	@Test
	public void testGetSource() {
		link1.setSource(node1);
		assertEquals("Problem with getSource", node1, link1.getSource());
	}

	/**
	 * This method is used to test the LinkType.getSource() method
	 */
	@Test
	public void testGetType() {
		link1.setCategory(linktype1);
		assertEquals("Problem with getType", linktype1, link1.getCategory());
	}

	/**
	 * This method is used to test the LinkImpl.SetSink() method
	 */
	@Test
	public void testSetSink() {
		link1.setSink(node2);
		assertEquals("Problem with setSink", node2, link1.getSink());
	}

	/**
	 * This method is used to test the LinkImpl.SetSource() method
	 */
	@Test
	public void testSetSource() {
		link1.setSource(node1);
		assertEquals("Problem with setSource", node1, link1.getSource());
	}

	/**
	 * This method is used to test the LinkImpl.SetType() method
	 */
	@Test
	public void testSetType() {
		link1.setCategory(linktype1);
		assertEquals("Problem with setType", linktype1, link1.getCategory());
	}

	/**
	 * This method is used to test the LinkImpl.GetReferencedLink() method
	 */
	@Test
	public void testGetReferencedLink() {
		link1.setGroundingPamLink(pamLink1);
		assertEquals("Problem with getReferencedLink", pamLink1, link1
				.getGroundingPamLink());
	}

	/**
	 * This method is used to test the LinkImpl.SetReferencedLink() method
	 */
	@Test
	public void testSetReferencedLink() {
		link1.setGroundingPamLink(pamLink1);
		assertEquals("Problem with setReferencedLink", pamLink1, link1
				.getGroundingPamLink());
	}

	/**
	 * This method is used to test the LinkImpl.LinkImpl(Linkable source,
	 * Linkable sink, LinkType type, String ids) method
	 */
	@Test
	public void testLinkImpl() {
		LinkImpl link5 = (LinkImpl) factory.getLink(node1, node2, linktype1);
		assertEquals(node1, link5.getSource());
		assertEquals(node2, link5.getSink());
		assertEquals(linktype1, link5.getCategory());
		assertTrue(!link5.getExtendedId().isNodeId());
		ExtendedId eid = new ExtendedId(node1.getId(), node2.getExtendedId(),
				linktype1.getId());
		assertHashCodeEquals(eid, link5.getExtendedId());

	}

	private void assertHashCodeEquals(Object o1, Object o2) {
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));
		assertTrue(o1.hashCode() == o2.hashCode());
	}

	@Test
	public void testComplexLink2() {
		Link foo = factory.getLink(node4, link1, linktype1);
		Link bar = new LinkImpl();
		assertNull(bar.getSink());
		bar.setSink(foo);
		assertNull(bar.getSink());

		bar.setSink(link1);
		assertEquals(link1, bar.getSink());
	}
}