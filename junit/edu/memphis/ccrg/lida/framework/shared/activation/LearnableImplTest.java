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
package edu.memphis.ccrg.lida.framework.shared.activation;

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
import edu.memphis.ccrg.lida.framework.strategies.SigmoidDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalActivationStrategy;

/**
 * This is a JUnit class which can be used to test methods of the LearnableImpl
 * class
 * 
 * @author Nisrine Enyinda
 * @author Siminder Kaur
 * @author Ryan J. McCall
 */
public class LearnableImplTest {

	private static final ElementFactory factory = ElementFactory.getInstance();

	private LearnableImpl learnable1;
	private LearnableImpl learnable2;
	private double epsilon = 0.000000001;

	/**
	 * @throws java.lang.Exception
	 *             e
	 */
	@Before
	public void setUp() throws Exception {
		learnable1 = new LearnableImpl();
	}

	@Test
	public void testArgConstructor() {
		double activation = 0.6;
		double activatibleRemovalThreshold = 0.01;
		double baseLevelActivation = 0.5;
		double learnableRemovalThreshold = 0.77;

		ExciteStrategy es = new LinearExciteStrategy();
		DecayStrategy ds = new LinearDecayStrategy();
		ExciteStrategy blEs = new SigmoidExciteStrategy();
		DecayStrategy blDs = new SigmoidDecayStrategy();
		TotalActivationStrategy ts = new DefaultTotalActivationStrategy();

		learnable2 = new LearnableImpl(activation, activatibleRemovalThreshold,
				baseLevelActivation, learnableRemovalThreshold, es, ds, blEs,
				blDs, ts);

		assertEquals(activation, learnable2.getActivation(), epsilon);
		assertEquals(activatibleRemovalThreshold, learnable2
				.getActivatibleRemovalThreshold(), epsilon);
		assertEquals(learnableRemovalThreshold, learnable2
				.getBaseLevelRemovalThreshold(), epsilon);
		assertEquals(baseLevelActivation, learnable2.getBaseLevelActivation(),
				epsilon);

		assertEquals(learnable2.getExciteStrategy(), es);
		assertEquals(learnable2.getDecayStrategy(), ds);
		assertEquals(learnable2.getBaseLevelDecayStrategy(), blDs);
		assertEquals(learnable2.getBaseLevelExciteStrategy(), blEs);
		assertEquals(learnable2.getTotalActivationStrategy(), ts);
	}

	@Test
	public void testCopyConstructor() {
		learnable1.setActivatibleRemovalThreshold(0.11);
		learnable1.setActivation(0.22);
		learnable1.setBaseLevelActivation(0.33);
		learnable1.setBaseLevelRemovalThreshold(0.44);

		learnable1.setDecayStrategy(factory.getDefaultDecayStrategy());
		learnable1.setExciteStrategy(factory.getDefaultExciteStrategy());
		learnable1
				.setTotalActivationStrategy(new DefaultTotalActivationStrategy());
		learnable1.setBaseLevelDecayStrategy(new SigmoidDecayStrategy());
		learnable1.setBaseLevelExciteStrategy(new SigmoidExciteStrategy());

		LearnableImpl copy = new LearnableImpl(learnable1);

		assertEquals(learnable1.getActivatibleRemovalThreshold(), copy
				.getActivatibleRemovalThreshold(), epsilon);
		assertEquals(learnable1.getActivation(), copy.getActivation(), epsilon);
		assertEquals(learnable1.getBaseLevelActivation(), copy
				.getBaseLevelActivation(), epsilon);
		assertEquals(learnable1.getBaseLevelRemovalThreshold(), copy
				.getBaseLevelRemovalThreshold(), epsilon);

		assertEquals(learnable1.getDecayStrategy(), copy.getDecayStrategy());
		assertEquals(learnable1.getExciteStrategy(), copy.getExciteStrategy());
		assertEquals(learnable1.getTotalActivationStrategy(), copy
				.getTotalActivationStrategy());
		assertEquals(learnable1.getBaseLevelDecayStrategy(), copy
				.getBaseLevelDecayStrategy());
		assertEquals(learnable1.getBaseLevelExciteStrategy(), copy
				.getBaseLevelExciteStrategy());
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decay(long)}
	 * .
	 */
	@Test
	public void testDecay() {
		DecayStrategy ds = new LinearDecayStrategy();
		learnable1.setDecayStrategy(ds);
		learnable1.setBaseLevelDecayStrategy(ds);
		learnable1.setBaseLevelActivation(0.5);
		learnable1.setActivation(0.7);

		learnable1.decay(1);

		assertTrue(learnable1.getBaseLevelActivation() < 0.5);
		assertTrue(learnable1.getActivation() < 0.7);

		learnable1.setBaseLevelActivation(0.5);
		learnable1.setActivation(0.7);

		learnable1.decay(0);

		assertEquals(learnable1.getBaseLevelActivation(), 0.5, epsilon);
		assertEquals(learnable1.getActivation(), 0.7, epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#isRemovable()}
	 * .
	 */
	@Test
	public void testIsRemovable() {
		// both 0 then removable
		learnable1.setBaseLevelRemovalThreshold(0.0);
		learnable1.setBaseLevelActivation(0.0);
		assertTrue(learnable1.isRemovable());

		learnable1.setBaseLevelActivation(1.0);
		assertFalse(learnable1.isRemovable());

		// changing activatible stuff doesn't matter
		learnable1.setActivation(0.0);
		learnable1.setActivatibleRemovalThreshold(1.0);
		assertFalse(learnable1.isRemovable());

		learnable1.setBaseLevelRemovalThreshold(1.0);
		assertTrue(learnable1.isRemovable());

		learnable1.setBaseLevelRemovalThreshold(-1.0);
		learnable1.setBaseLevelActivation(0.0);
		assertFalse(learnable1.isRemovable());
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getTotalActivation()}
	 * .
	 */
	@Test
	public void testGetTotalActivation() {
		TotalActivationStrategy ts = new DefaultTotalActivationStrategy();
		learnable1.setTotalActivationStrategy(ts);
		learnable1.setActivation(0.11);
		learnable1.setBaseLevelActivation(0.44);
		assertEquals(0.55, learnable1.getTotalActivation(), epsilon);

		learnable1.setActivation(0.9);
		learnable1.setBaseLevelActivation(0.3);
		assertEquals(1.0, learnable1.getTotalActivation(), epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decayBaseLevelActivation(long)}
	 * .
	 */
	@Test
	public void testDecayBaseLevelActivation() {
		DecayStrategy ds = new LinearDecayStrategy();
		learnable1.setBaseLevelDecayStrategy(ds);
		learnable1.setBaseLevelActivation(0.5);
		learnable1.decayBaseLevelActivation(1);
		learnable1.setActivation(1.0);

		assertTrue(learnable1.getBaseLevelActivation() < 0.5);
		assertTrue(learnable1.getActivation() == 1.0);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelExciteStrategy(edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)}
	 * .
	 */
	@Test
	public void testSetBaseLevelExciteStrategy() {
		ExciteStrategy es = new LinearExciteStrategy();
		learnable1.setBaseLevelExciteStrategy(es);

		assertEquals("Problem with SetBaseLevelExciteStrategy", es, learnable1
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
		learnable1.setBaseLevelDecayStrategy(ds);

		assertEquals("Problem with SetBaseLevelDecayStrategy", ds, learnable1
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
		learnable1.setBaseLevelExciteStrategy(es);
		learnable1.setBaseLevelActivation(0.2);
		learnable1.reinforceBaseLevelActivation(0.3);
		learnable1.setActivation(0.0);

		assertEquals(0.5, learnable1.getBaseLevelActivation(), epsilon);
		assertEquals(0.0, learnable1.getActivation(), epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelActivation(double)}
	 * .
	 */
	@Test
	public void testSetBaseLevelActivation() {
		learnable1.setBaseLevelActivation(0.4);
		assertEquals(0.4, learnable1.getBaseLevelActivation(), 0.001);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelRemovalThreshold(double)}
	 * .
	 */
	@Test
	public void testSetLearnableRemovalThreshold() {
		learnable1.setBaseLevelRemovalThreshold(0.4);
		assertEquals(0.4, learnable1.getBaseLevelRemovalThreshold(), 0.0001);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getTotalActivationStrategy()}
	 * .
	 */
	@Test
	public void testGetTotalActivationStrategy() {
		TotalActivationStrategy ts = new DefaultTotalActivationStrategy();
		learnable1.setTotalActivationStrategy(ts);
		assertEquals("problem with GetTotalActivationStrategy() ", ts,
				learnable1.getTotalActivationStrategy());
	}

}