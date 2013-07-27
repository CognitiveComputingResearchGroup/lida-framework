/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared.activation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.SigmoidExciteStrategy;

public class ActivatibleImplTest {

	private ActivatibleImpl act1;
	private ActivatibleImpl multArg;
	private static ElementFactory factory;

	@BeforeClass
	public static void setUpFirst() {
		factory = ElementFactory.getInstance();
		StrategyDef decayDef = new StrategyDef(SigmoidDecayStrategy.class
				.getCanonicalName(), "specialDecay", null, "decay", true);
		factory.addDecayStrategy("specialDecay", decayDef);

		StrategyDef exciteDef = new StrategyDef(SigmoidExciteStrategy.class
				.getCanonicalName(), "specialExcite", null, "excite", true);
		factory.addExciteStrategy("specialExcite", exciteDef);
	}

	@Before
	public void setUp() throws Exception {
		act1 = new ActivatibleImpl();
		multArg = new ActivatibleImpl(0.11, 0.22, factory
				.getExciteStrategy("specialExcite"), factory
				.getDecayStrategy("specialDecay"));
	}

	@Test
	public void testGetActivation() {
		assertTrue(act1.getActivation() == Activatible.DEFAULT_ACTIVATION);
		assertTrue(multArg.getActivation() == 0.11);
	}

	@Test
	public void testGetActivatibleRemovalThreshold() {
		assertTrue(act1.getActivatibleRemovalThreshold() == Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
		assertTrue(multArg.getActivatibleRemovalThreshold() == 0.22);
	}

	@Test
	public void testGetDecayStrategy() {
		assertEquals(act1.getDecayStrategy(), factory.getDefaultDecayStrategy());
		assertEquals(SigmoidDecayStrategy.class, multArg.getDecayStrategy()
				.getClass());
	}

	@Test
	public void testGetExciteStrategy() {
		assertEquals(act1.getExciteStrategy(), factory
				.getDefaultExciteStrategy());
		assertEquals(SigmoidExciteStrategy.class, multArg.getExciteStrategy()
				.getClass());
	}

	private double epsilon = 0.000000001;

	@Test
	public void testSetActivation() {
		act1.setActivation(345.45);
		assertEquals(1.0, act1.getActivation(), epsilon);

		act1.setActivation(-35395);
		assertEquals(0.0, act1.getActivation(), epsilon);

		act1.setActivation(0.0);
		assertEquals(0.0, act1.getActivation(), epsilon);

		act1.setActivation(1.0);
		assertEquals(1.0, act1.getActivation(), epsilon);

		act1.setActivation(0.345);
		assertEquals(0.345, act1.getActivation(), epsilon);
	}

	@Test
	public void testSetActivatibleRemovalThreshold() {
		act1.setActivatibleRemovalThreshold(-345);
		assertEquals(-345, act1.getActivatibleRemovalThreshold(), epsilon);

		act1.setActivatibleRemovalThreshold(1.0);
		assertEquals(1.0, act1.getActivatibleRemovalThreshold(), epsilon);

		act1.setActivatibleRemovalThreshold(1.01);
		assertEquals(1.0, act1.getActivatibleRemovalThreshold(), epsilon);
	}

	@Test
	public void testGetTotalActivation() {
		act1.setActivation(0.0);
		assertEquals(0.0, act1.getTotalActivation(), epsilon);

		act1.setActivation(5.0);
		assertEquals(1.0, act1.getTotalActivation(), epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#decay(long)}
	 * .
	 */
	@Test
	public void testDecay() {
		DecayStrategy ds = new LinearDecayStrategy();
		act1.setActivation(0.5);
		act1.setDecayStrategy(ds);
		act1.decay(10);

		assertTrue("Problem with Decay", act1.getActivation() < 0.5);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl#excite(double)}
	 * .
	 */
	@Test
	public void testExcite() {
		ExciteStrategy es = new LinearExciteStrategy();
		act1.setActivation(0.5);
		act1.setExciteStrategy(es);
		act1.excite(0.1);

		assertTrue("Problem with Excite", act1.getActivation() > 0.5);
	}

	@Test
	public void testIsRemovable() {
		act1.setActivation(5.0);
		act1.setActivatibleRemovalThreshold(7.0);

		assertTrue(act1.isRemovable());

		act1.setActivation(0.0);
		act1.setActivatibleRemovalThreshold(7.0);

		assertTrue(act1.isRemovable());

		act1.setActivation(0.0);
		act1.setActivatibleRemovalThreshold(0.0);

		assertTrue(act1.isRemovable());

		act1.setActivation(-1.0);
		act1.setActivatibleRemovalThreshold(-1.0);

		assertFalse(act1.isRemovable());
	}

}
