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
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;

/**
 * @author Siminder Kaur, Ryan McCall
 */
public class PamLinkImplTest extends TestCase{
	
	PamLinkImpl link1,link2, linkId1, linkId2;
	LidaElementFactory  Factory;
	Node node1;
	Node node2;
	LinkCategory linkCategory;
 
	@Override
	@Before
	public void setUp() throws Exception {
		Factory = LidaElementFactory.getInstance();
		link1 = new PamLinkImpl();
		link2 = new PamLinkImpl();
		node1 = Factory.getNode();
		node2 = Factory.getNode();
		
		linkCategory= LinkCategoryNode.CHILD;			
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
			
		link1.setSource(node1);
		link2.setSource(node1);
		link1.setSource(node2);
		link2.setSource(node2);
		link1.setCategory(linkCategory);
		link2.setCategory(linkCategory);
		assertEquals(link1.getExtendedId().toString() + " " + link2.getExtendedId().toString(), link1, link2);
	}
	
	/**
	 * {@link edu.memphis.ccrg.lida.pam.PamLinkImpl#hashCode(java.lang.Object)}.
	 */
	public void testHashCode(){
//		int id = (int) (Math.random()*Integer.MAX_VALUE);
//		link1.getl(id);
//		link2.setId(id);
		linkId1 = (PamLinkImpl) Factory.getLink(node1, node2,linkCategory);
		linkId2 = (PamLinkImpl) Factory.getLink(node2, node1,linkCategory); 
		
		assertEquals(linkId1.hashCode() + " " + linkId2.hashCode(), linkId1.hashCode(), linkId2.hashCode());
	}
	


}
