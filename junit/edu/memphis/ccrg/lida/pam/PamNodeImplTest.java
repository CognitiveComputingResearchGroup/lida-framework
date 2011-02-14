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

/**
 * @author Siminder Kaur, Ryan McCall
 */
public class PamNodeImplTest extends TestCase{
	
	PamNodeImpl node1,node2;
	PerceptualAssociativeMemoryImpl pam;
	
	@Override
	@Before
	public void setUp() throws Exception {
		node1 = new PamNodeImpl();
		node2 = new PamNodeImpl();
		pam = new PerceptualAssociativeMemoryImpl();				
	}
	
	@Override
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * {@link edu.memphis.ccrg.lida.pam.PamNodeImpl#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		int id = (int) (Math.random()*Integer.MAX_VALUE);
		node1.setId(id);
		node2.setId(id);
		assertEquals(node1.getId() + " " + node2.getId(), node1, node2);
	}
	
	/**
	 * {@link PamNodeImpl#hashCode()}
	 */
	public void testHashCode(){
		int id = (int) (Math.random()*Integer.MAX_VALUE);
		node1.setId(id);
		node2.setId(id);
		assertEquals(node1.hashCode() + " " + node2.hashCode(), node1.hashCode(), node2.hashCode());
	}
	
	/**
	 * {@link PamNodeImpl#getFactoryNodeType()} 
	 */
	public void testGetFactoryNodeType(){
		assertEquals(PamNodeImpl.class.getSimpleName(), node1.getFactoryNodeType());
	}

}
