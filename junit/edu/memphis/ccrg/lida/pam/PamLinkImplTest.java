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

import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultTotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalActivationStrategy;
import edu.memphis.ccrg.lida.pam.ns.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl;

/**
 * @author Ryan J. McCall
 */
public class PamLinkImplTest {

	private Node node1;
	private Node node2;
	private LinkCategory linkCategory;
	private PamLinkImpl link1, link2, link3;
	private double epsilon = 0.000000001;

	private static ElementFactory factory = ElementFactory.getInstance();

	@BeforeClass
	public static void setUpBeforeClass() {
		factory = ElementFactory.getInstance();
		FactoriesDataXmlLoader factoryLoader = new FactoriesDataXmlLoader();
		Properties prop = ConfigUtils
				.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		factoryLoader.loadFactoriesData(prop);
	}

	@Before
	public void setUp() throws Exception {
		node1 = factory.getNode();
		node2 = factory.getNode();
		linkCategory = PerceptualAssociativeMemoryNSImpl.NONE;
		link1 = (PamLinkImpl) factory.getLink("PamLinkImpl", node1, node2,
				linkCategory);
		link2 = (PamLinkImpl) factory.getLink("PamLinkImpl", node1, node2,
				linkCategory);
		link3 = (PamLinkImpl) factory.getLink("PamLinkImpl", node2, node1,
				linkCategory);
	}

	/**
	 * {@link edu.memphis.ccrg.lida.pam.ns.PamLinkImpl#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		assertTrue(link1.equals(link2));
		assertTrue(link2.equals(link1));
		assertFalse(link1.equals(link3));
		assertFalse(link3.equals(link2));
	}

	/**
	 * {@link edu.memphis.ccrg.lida.pam.ns.PamLinkImpl#equals(Object)}
	 */
	@Test
	public void testHashCode() {
		assertEquals(link1.hashCode(), link2.hashCode());
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decay(long)}
	 * .
	 */
	@Test
	public void testDecay() {
		DecayStrategy ds = new LinearDecayStrategy();
		link1.setDecayStrategy(ds);
		link1.setBaseLevelDecayStrategy(ds);
		link1.setBaseLevelActivation(0.5);
		link1.setActivation(0.7);

		link1.decay(1);

		assertTrue(link1.getBaseLevelActivation() < 0.5);
		assertTrue(link1.getActivation() < 0.7);

		link1.setBaseLevelActivation(0.5);
		link1.setActivation(0.7);

		link1.decay(0);

		assertEquals(link1.getBaseLevelActivation(), 0.5, epsilon);
		assertEquals(link1.getActivation(), 0.7, epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#isRemovable()}
	 * .
	 */
	@Test
	public void testIsRemovable() {
		// both 0 then removable
		link1.setBaseLevelRemovalThreshold(0.0);
		link1.setBaseLevelActivation(0.0);
		assertTrue(link1.isRemovable());

		link1.setBaseLevelActivation(1.0);
		assertFalse(link1.isRemovable());

		// changing activatible stuff doesn't matter
		link1.setActivation(0.0);
		link1.setActivatibleRemovalThreshold(1.0);
		assertFalse(link1.isRemovable());

		link1.setBaseLevelRemovalThreshold(1.0);
		assertTrue(link1.isRemovable());

		link1.setBaseLevelRemovalThreshold(-1.0);
		link1.setBaseLevelActivation(0.0);
		assertFalse(link1.isRemovable());
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getTotalActivation()}
	 * .
	 */
	@Test
	public void testGetTotalActivation() {
		TotalActivationStrategy ts = new DefaultTotalActivationStrategy();
		link1.setTotalActivationStrategy(ts);
		link1.setActivation(0.11);
		link1.setBaseLevelActivation(0.44);
		assertEquals(0.55, link1.getTotalActivation(), epsilon);

		link1.setActivation(0.9);
		link1.setBaseLevelActivation(0.3);
		assertEquals(1.0, link1.getTotalActivation(), epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decayBaseLevelActivation(long)}
	 * .
	 */
	@Test
	public void testDecayBaseLevelActivation() {
		DecayStrategy ds = new LinearDecayStrategy();
		link1.setBaseLevelDecayStrategy(ds);
		link1.setBaseLevelActivation(0.5);
		link1.decayBaseLevelActivation(1);
		link1.setActivation(1.0);

		assertTrue(link1.getBaseLevelActivation() < 0.5);
		assertTrue(link1.getActivation() == 1.0);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelExciteStrategy(edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)}
	 * .
	 */
	@Test
	public void testSetBaseLevelExciteStrategy() {
		ExciteStrategy es = new LinearExciteStrategy();
		link1.setBaseLevelExciteStrategy(es);

		assertEquals("Problem with SetBaseLevelExciteStrategy", es, link1
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
		link1.setBaseLevelDecayStrategy(ds);

		assertEquals("Problem with SetBaseLevelDecayStrategy", ds, link1
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
		link1.setBaseLevelExciteStrategy(es);
		link1.setBaseLevelActivation(0.2);
		link1.reinforceBaseLevelActivation(0.3);
		link1.setActivation(0.0);

		assertEquals(0.5, link1.getBaseLevelActivation(), epsilon);
		assertEquals(0.0, link1.getActivation(), epsilon);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelActivation(double)}
	 * .
	 */
	@Test
	public void testSetBaseLevelActivation() {
		link1.setBaseLevelActivation(0.4);
		assertEquals(0.4, link1.getBaseLevelActivation(), 0.001);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#setBaseLevelRemovalThreshold(double)}
	 * .
	 */
	@Test
	public void testSetLearnableRemovalThreshold() {
		link1.setBaseLevelRemovalThreshold(0.4);
		assertEquals(0.4, link1.getBaseLevelRemovalThreshold(), 0.0001);
	}

	/**
	 * Test method for
	 * {@link edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#getTotalActivationStrategy()}
	 * .
	 */
	@Test
	public void testGetTotalActivationStrategy() {
		TotalActivationStrategy ts = new DefaultTotalActivationStrategy();
		link1.setTotalActivationStrategy(ts);
		assertEquals("problem with GetTotalActivationStrategy() ", ts, link1
				.getTotalActivationStrategy());
	}

}
