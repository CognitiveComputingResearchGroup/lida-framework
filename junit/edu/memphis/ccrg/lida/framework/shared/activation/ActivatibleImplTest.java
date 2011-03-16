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
package edu.memphis.ccrg.lida.framework.shared.activation;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;

/**
 * This is a JUnit class which can be used to test methods of the ActivatibleImpl class
 * @author Siminder Kaur
 */

public class ActivatibleImplTest extends TestCase{

	ActivatibleImpl node1,node2;
	
	/**
	 * @throws java.lang.Exception e
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		node1 = new ActivatibleImpl();		
	}	
	
	public void testIsRemovable(){
		Node n = new NodeImpl();
		n.setActivation(0.1);
		n.setActivatibleRemovalThreshold(0.05);
		n.decay(100);
		assertTrue("Problem with IsRemovable",n.isRemovable());
		
		n.setActivation(0.1);
		n.setActivatibleRemovalThreshold(0.05);
		assertTrue("Problem with IsRemovable",!n.isRemovable());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#ActivatibleImpl(double, edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy, edu.memphis.ccrg.lida.framework.strategies.DecayStrategy, double)}.
	 */
	@Test
	public void testActivatibleImplDoubleExciteStrategyDecayStrategy() {
		ExciteStrategy es = new DefaultExciteStrategy();
		DecayStrategy ds = new LinearDecayStrategy();
		
		node2 = new ActivatibleImpl(0.2,es,ds, 0.0);
		
		assertEquals("Problem with ActivatibleImpl(Double, ExciteStrategy, DecayStrategy)", 0.2, node2.getActivation());
		assertEquals("Problem with ActivatibleImpl(Double, ExciteStrategy, DecayStrategy)", es, node2.getExciteStrategy());
		assertEquals("Problem with ActivatibleImpl(Double, ExciteStrategy, DecayStrategy)", ds, node2.getDecayStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#decay(long)}.
	 */
	@Test
	public void testDecay() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setActivation(0.5);
		node1.setDecayStrategy(ds);
		node1.decay(10);
		
		assertTrue("Problem with Decay", node1.getActivation()<0.5);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#excite(double)}.
	 */
	@Test
	public void testExcite() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setActivation(0.5);
		node1.setExciteStrategy(es);
		node1.excite(0.1);
				
		assertTrue("Problem with Excite", node1.getActivation()>0.5);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#getActivation()}.
	 */
	@Test
	public void testGetActivation() {
		node1.setActivation(0.2);
		assertEquals("Problem with GetActivation", 0.2,node1.getActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#getDecayStrategy()}.
	 */
	@Test
	public void testGetDecayStrategy() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setActivation(0.5);
		node1.setDecayStrategy(ds);
		
		assertEquals("Problem with GetDecayStrategy", ds,node1.getDecayStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#getExciteStrategy()}.
	 */
	@Test
	public void testGetExciteStrategy() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setActivation(0.5);
		node1.setExciteStrategy(es);
		
		assertEquals("Problem with GetExciteStrategy", es,node1.getExciteStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#setActivation(double)}.
	 */
	@Test
	public void testSetActivation() {
		node1.setActivation(0.2);
		assertEquals("Problem with GetActivation", 0.2,node1.getActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#setDecayStrategy(edu.memphis.ccrg.lida.framework.strategies.DecayStrategy)}.
	 */
	@Test
	public void testSetDecayStrategy() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setActivation(0.5);
		node1.setDecayStrategy(ds);
		
		assertEquals("Problem with GetDecayStrategy", ds,node1.getDecayStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#setExciteStrategy(edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)}.
	 */
	@Test
	public void testSetExciteStrategy() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setActivation(0.5);
		node1.setExciteStrategy(es);
		
		assertEquals("Problem with GetExciteStrategy", es,node1.getExciteStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#getTotalActivation()}.
	 */
	@Test
	public void testGetTotalActivation() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setActivation(0.5);
		node1.setExciteStrategy(es);
		node1.excite(0.1);
		
		assertEquals("Problem with GetTotalActivation", 0.6,node1.getActivation());
	}
	
	@Test
	public void testGetActivatibleRemovalThreshold() {		
		node1.setActivatibleRemovalThreshold(0.5);		
		assertEquals("Problem with GetActivatibleRemovalThreshold", 0.5,node1.getActivatibleRemovalThreshold());
	}
	
	@Test
	public void testSetActivatibleRemovalThreshold() {		
		node1.setActivatibleRemovalThreshold(0.5);		
		assertEquals("Problem with SetActivatibleRemovalThreshold", 0.5,node1.getActivatibleRemovalThreshold());
	}

}
