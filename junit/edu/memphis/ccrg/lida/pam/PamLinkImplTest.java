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
package edu.memphis.ccrg.lida.pam;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;

/**
 * @author Usef Faghihi
 */
public class PamLinkImplTest extends TestCase{
	
	private LidaElementFactory  factory = LidaElementFactory.getInstance();
	private Node node1;
	private Node node2;
	private LinkCategory linkCategory;
	private PamLinkImpl link1,link2, link3;
 
	@Override
	@Before
	public void setUp() throws Exception {
		new PerceptualAssociativeMemoryImpl();
		
		node1 = factory.getNode();
		node2 = factory.getNode();
		linkCategory= PerceptualAssociativeMemoryImpl.NONE;	
		link1 = (PamLinkImpl) factory.getLink(PamLinkImpl.factoryName, node1, node2, linkCategory);
		link2 = (PamLinkImpl) factory.getLink(PamLinkImpl.factoryName, node1, node2, linkCategory);
		link3 = (PamLinkImpl) factory.getLink(PamLinkImpl.factoryName, node2, node1, linkCategory);	
	}
	
	@Override
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * {@link edu.memphis.ccrg.lida.pam.PamLinkImpl#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		assertTrue(link1.equals(link2));
		assertTrue(link2.equals(link1));
		assertFalse(link1.equals(link3));
		assertFalse(link3.equals(link2));
	}
	
	/**
	 * {@link edu.memphis.ccrg.lida.pam.PamLinkImpl#equals(Object)}
	 */
	public void testHashCode(){		
		assertEquals(link1.hashCode(), link2.hashCode());
	}
	


}
