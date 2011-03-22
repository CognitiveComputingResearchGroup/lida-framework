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

/**
 * @author Siminder Kaur, Ryan McCall
 */
public class PamNodeImplTest extends TestCase{
	
	private PamNodeImpl node1;
	private PamNodeImpl node2;
	private LidaElementFactory factory = LidaElementFactory.getInstance();
	
	@Override
	@Before
	public void setUp() throws Exception {
		node1 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName);
		node2 = (PamNodeImpl) factory.getNode(PamNodeImpl.factoryName);	
		
		node1.getActivation();
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
		assertEquals(node1, node2);
		assertEquals(node2, node1);
	}
	
	public void testNotEqual(){
		node1.setId(0);
		node2.setId(Integer.MIN_VALUE);
		assertFalse(node1.equals(node2));
		assertFalse(node2.equals(node1));
	}
	
	/**
	 * {@link PamNodeImpl#hashCode()}
	 */
	public void testHashCode(){
		int id = (int) (Math.random()*Integer.MAX_VALUE);
		node1.setId(id);
		node2.setId(id);
		assertEquals(node1.hashCode(), node2.hashCode());
	}
	
	/**
	 * 
	 */
	public void testSetActivationThreshold(){
		node1.setActivatibleRemovalThreshold(1.0);
		node1.setActivation(0.0);
		
		double t = 0.7;
		node1.setLearnableRemovalThreshold(t);
		assertEquals(t, node1.getLearnableRemovalThreshold());
		
		node1.setBaseLevelActivation(1.0);
		assertFalse(node1.isRemovable());
		node1.decay(1000);
		assertTrue(node1.isRemovable());
	}
	

}
