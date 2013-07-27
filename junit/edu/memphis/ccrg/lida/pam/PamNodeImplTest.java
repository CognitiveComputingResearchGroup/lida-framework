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
package edu.memphis.ccrg.lida.pam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultTotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalActivationStrategy;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;

/**
 * @author Siminder Kaur
 * @author Ryan J. McCall
 */
public class PamNodeImplTest {

	private ElementFactory factory = ElementFactory.getInstance();
	private PamNodeImpl node1;
	private PamNodeImpl node2;
	private double epsilon = 0.000000001;

	@Before
	public void setUp() throws Exception {
		node1 = (PamNodeImpl) factory.getNode("PamNodeImpl");
		node2 = (PamNodeImpl) factory.getNode("PamNodeImpl");
	}

	/**
	 * {@link edu.memphis.ccrg.lida.pam.ns.PamNodeImpl#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		int id = (int) (Math.random() * Integer.MAX_VALUE);
		node1.setId(id);
		node2.setId(id);
		assertEquals(node1, node2);
		assertEquals(node2, node1);
	}

	@Test
	public void testNotEqual() {
		node1.setId(0);
		node2.setId(Integer.MIN_VALUE);
		assertFalse(node1.equals(node2));
		assertFalse(node2.equals(node1));
	}

	/**
	 * {@link PamNodeImpl#hashCode()}
	 */
	@Test
	public void testHashCode() {
		int id = (int) (Math.random() * Integer.MAX_VALUE);
		node1.setId(id);
		node2.setId(id);
		assertEquals(node1.hashCode(), node2.hashCode());
	}

	/**
	 * 
	 */
	@Test
	public void testSetActivationThreshold() {
		node1.setBaseLevelDecayStrategy(new LinearDecayStrategy());
		node1.setActivatibleRemovalThreshold(1.0);
		node1.setActivation(0.0);

		double t = 0.7;
		node1.setBaseLevelRemovalThreshold(t);
		assertTrue(t == node1.getBaseLevelRemovalThreshold());

		node1.setBaseLevelActivation(1.0);
		assertFalse(node1.isRemovable());
		node1.decay(1000);
		assertTrue(node1.isRemovable());
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decay(long)}
	 * .
	 */
	@Test
	public void testDecay() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setDecayStrategy(ds);
		node1.setBaseLevelDecayStrategy(ds);
		node1.setBaseLevelActivation(0.5);
		node1.setActivation(0.7);

		node1.decay(1);

		assertTrue(node1.getBaseLevelActivation() < 0.5);
		assertTrue(node1.getActivation() < 0.7);

		node1.setBaseLevelActivation(0.5);
		node1.setActivation(0.7);

		node1.decay(0);

		assertEquals(node1.getBaseLevelActivation(), 0.5, epsilon);
		assertEquals(node1.getActivation(), 0.7, epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#isRemovable()}
	 * .
	 */
	@Test
	public void testIsRemovable() {
		// both 0 then removable
		node1.setBaseLevelRemovalThreshold(0.0);
		node1.setBaseLevelActivation(0.0);
		assertTrue(node1.isRemovable());

		node1.setBaseLevelActivation(1.0);
		assertFalse(node1.isRemovable());

		// changing activatible stuff doesn't matter
		node1.setActivation(0.0);
		node1.setActivatibleRemovalThreshold(1.0);
		assertFalse(node1.isRemovable());

		node1.setBaseLevelRemovalThreshold(1.0);
		assertTrue(node1.isRemovable());

		node1.setBaseLevelRemovalThreshold(-1.0);
		node1.setBaseLevelActivation(0.0);
		assertFalse(node1.isRemovable());
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getTotalActivation()}
	 * .
	 */
	@Test
	public void testGetTotalActivation() {
		TotalActivationStrategy ts = new DefaultTotalActivationStrategy();
		node1.setTotalActivationStrategy(ts);
		node1.setActivation(0.11);
		node1.setBaseLevelActivation(0.44);
		assertEquals(0.55, node1.getTotalActivation(), epsilon);

		node1.setActivation(0.9);
		node1.setBaseLevelActivation(0.3);
		assertEquals(1.0, node1.getTotalActivation(), epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decayBaseLevelActivation(long)}
	 * .
	 */
	@Test
	public void testDecayBaseLevelActivation() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setBaseLevelDecayStrategy(ds);
		node1.setBaseLevelActivation(0.5);
		node1.decayBaseLevelActivation(1);
		node1.setActivation(1.0);

		assertTrue(node1.getBaseLevelActivation() < 0.5);
		assertTrue(node1.getActivation() == 1.0);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelExciteStrategy(edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)}
	 * .
	 */
	@Test
	public void testSetBaseLevelExciteStrategy() {
		ExciteStrategy es = new LinearExciteStrategy();
		node1.setBaseLevelExciteStrategy(es);

		assertEquals("Problem with SetBaseLevelExciteStrategy", es, node1
				.getBaseLevelExciteStrategy());

	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelDecayStrategy(edu.memphis.ccrg.lida.framework.strategies.DecayStrategy)}
	 * .
	 */
	@Test
	public void testSetBaseLevelDecayStrategy() {
		DecayStrategy ds = new LinearDecayStrategy();
		node1.setBaseLevelDecayStrategy(ds);

		assertEquals("Problem with SetBaseLevelDecayStrategy", ds, node1
				.getBaseLevelDecayStrategy());
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#reinforceBaseLevelActivation(double)}
	 * .
	 */
	@Test
	public void testReinforceBaseLevelActivation() {
		ExciteStrategy es = new LinearExciteStrategy();
		node1.setBaseLevelExciteStrategy(es);
		node1.setBaseLevelActivation(0.2);
		node1.reinforceBaseLevelActivation(0.3);
		node1.setActivation(0.0);

		assertEquals(0.5, node1.getBaseLevelActivation(), epsilon);
		assertEquals(0.0, node1.getActivation(), epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelActivation(double)}
	 * .
	 */
	@Test
	public void testSetBaseLevelActivation() {
		node1.setBaseLevelActivation(0.4);
		assertEquals(0.4, node1.getBaseLevelActivation(), 0.001);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelRemovalThreshold(double)}
	 * .
	 */
	@Test
	public void testSetLearnableRemovalThreshold() {
		node1.setBaseLevelRemovalThreshold(0.4);
		assertEquals(0.4, node1.getBaseLevelRemovalThreshold(), 0.0001);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getTotalActivationStrategy()}
	 * .
	 */
	@Test
	public void testGetTotalActivationStrategy() {
		TotalActivationStrategy ts = new DefaultTotalActivationStrategy();
		node1.setTotalActivationStrategy(ts);
		assertEquals("problem with GetTotalActivationStrategy() ", ts, node1
				.getTotalActivationStrategy());
	}

}
