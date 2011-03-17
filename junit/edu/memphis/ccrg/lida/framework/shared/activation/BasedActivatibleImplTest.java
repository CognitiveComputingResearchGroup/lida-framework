/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.framework.shared.activation;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;

/**
 * @author Siminder Kaur
 *
 */
public class BasedActivatibleImplTest extends TestCase{

	LearnableImpl node1;
	/**
	 * @throws java.lang.Exception e
	 */
	@Override
	@Before
	public void setUp() throws Exception {
		node1 = new LearnableImpl();
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getTotalActivation()}.
	 */
	@Test
	public void testGetTotalActivation() {
		node1.setBaseLevelActivation(0.2);
		node1.setActivation(0.3);
				
		assertEquals("Problem with GetTotalActivation", 0.5,node1.getTotalActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decayBaseLevelActivation(long)}.
	 */
	@Test
	public void testDecayBaseLevelActivation() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setBaseLevelDecayStrategy(ds);
		node1.setBaseLevelActivation(0.5);			
		node1.decayBaseLevelActivation(100);
		node1.getBaseLevelActivation();
		
		assertTrue("Problem with DecayBaseLevelActivation", node1.getBaseLevelActivation()<0.5);
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getBaseLevelExciteStrategy()}.
	 */
	@Test
	public void testGetBaseLevelExciteStrategy() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setBaseLevelExciteStrategy(es);
		
		assertEquals("Problem with GetBaseLevelExciteStrategy", es,node1.getBaseLevelExciteStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelExciteStrategy(edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)}.
	 */
	@Test
	public void testSetBaseLevelExciteStrategy() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setBaseLevelExciteStrategy(es);
		
		assertEquals("Problem with SetBaseLevelExciteStrategy", es,node1.getBaseLevelExciteStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getBaseLevelDecayStrategy()}.
	 */
	@Test
	public void testGetBaseLevelDecayStrategy() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setBaseLevelDecayStrategy(ds);
		
		assertEquals("Problem with GetBaseLevelDecayStrategy", ds,node1.getBaseLevelDecayStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelDecayStrategy(edu.memphis.ccrg.lida.framework.strategies.DecayStrategy)}.
	 */
	@Test
	public void testSetBaseLevelDecayStrategy() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setBaseLevelDecayStrategy(ds);
		
		assertEquals("Problem with SetBaseLevelDecayStrategy", ds,node1.getBaseLevelDecayStrategy());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getBaseLevelActivation()}.
	 */
	@Test
	public void testGetBaseLevelActivation() {
		node1.setBaseLevelActivation(0.2);
		
		assertEquals("Problem with GetBaseLevelActivation", 0.2,node1.getBaseLevelActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#reinforceBaseLevelActivation(double)}.
	 */
	@Test
	public void testReinforceBaseLevelActivation() {
		ExciteStrategy es = new DefaultExciteStrategy();
		node1.setBaseLevelExciteStrategy(es);
		node1.setBaseLevelActivation(0.2);
		node1.reinforceBaseLevelActivation(0.3);
		
		assertEquals("Problem with ReinforceBaseLevelActivation", 0.5,node1.getBaseLevelActivation());
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelActivation(double)}.
	 */
	@Test
	public void testSetBaseLevelActivation() {
		node1.setBaseLevelActivation(0.2);
		
		assertEquals("Problem with SetBaseLevelActivation", 0.2,node1.getBaseLevelActivation());
	}

}
