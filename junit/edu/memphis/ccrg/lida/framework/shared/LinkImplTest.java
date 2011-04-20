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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.pam.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;

/**
 * This is a JUnit class which can be used to test methods of the LinkImpl class
 * @author Siminder Kaur
 *
 */
public class LinkImplTest extends TestCase{
	
	private Node node1,node2,node3,node4;
	private LinkImpl link1,link2,link3;	
	private	PamLinkImpl pamLink1;
	private LinkCategory linktype1;	
	private ElementFactory factory = ElementFactory.getInstance();

	/**
	 * This method is called before running each test case to initialize the objects
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		new PerceptualAssociativeMemoryImpl();
		node1 = factory.getNode();
		node2 = factory.getNode();
		node3 = factory.getNode();
		node4 = factory.getNode();	
		
		linktype1 = PerceptualAssociativeMemoryImpl.NONE;
		
		pamLink1 = (PamLinkImpl) factory.getLink("PamLinkImpl", node3, node4, PerceptualAssociativeMemoryImpl.NONE);
		
		link1 = new LinkImpl(node1,node2,PerceptualAssociativeMemoryImpl.NONE);
		link2 = new LinkImpl(node3,node4,PerceptualAssociativeMemoryImpl.NONE);
		link3 = new LinkImpl(node3,link2,PerceptualAssociativeMemoryImpl.NONE);
	}

	/**
	 * This method is called after running each test case
	 */
	@Override
	@After
	public void tearDown() throws Exception {
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
		assertEquals("Problem with getReferencedLink", pamLink1, link1.getGroundingPamLink());
	}

	/**
	 * This method is used to test the LinkImpl.SetReferencedLink() method
	 */
	@Test
	public void testSetReferencedLink() {
		link1.setGroundingPamLink(pamLink1);
		assertEquals("Problem with setReferencedLink", pamLink1, link1.getGroundingPamLink());
	}
	
	/**
	 * This method is used to test the LinkImpl.LinkImpl(Linkable source, Linkable sink, LinkType type, String ids) method
	 */
	@Test
	public void testLinkImpl() {
		LinkImpl link5= new LinkImpl(node1,node2,linktype1);
		assertEquals(node1, link5.getSource());
		assertEquals(node2, link5.getSink());
		assertEquals(linktype1, link5.getCategory());
		assertTrue( !link5.getExtendedId().isNodeId());
		ExtendedId eid = new ExtendedId(node1.getId(), node2.getExtendedId(), linktype1.getId());
		assertHashCodeEquals(eid, link5.getExtendedId());
	
	}
	
	private void assertHashCodeEquals(Object o1, Object o2){
		assertTrue(o1.equals(o2));
		assertTrue(o2.equals(o1));
		assertTrue(o1.hashCode() == o2.hashCode());
	}
	
	public void testComplexLink(){
		try{
			Link foo = new LinkImpl(node4, link1, linktype1);
			 new LinkImpl(node3, foo, linktype1);
			assertTrue(false);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
	}
	
	public void testComplexLink2(){
		Link foo = new LinkImpl(node4, link1, linktype1);
		
		try{
			new LinkImpl(node1, foo, linktype1);
			assertTrue(false);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		Link bar = new LinkImpl();
		try{
			bar.setSink(foo);
			assertTrue(false);
		}catch(Exception e){
			assertTrue(e instanceof IllegalArgumentException);
		}
		
		bar.setSink(link1);
		assertEquals(link1, bar.getSink());
	}

}
