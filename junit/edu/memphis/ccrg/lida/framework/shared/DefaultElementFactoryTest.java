/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.strategies.NoDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.NoExciteStrategy;
import edu.memphis.ccrg.lida.pam.ns.PamNode;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;

public class DefaultElementFactoryTest {

	private static ElementFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = ElementFactory.getInstance();
	}

	@Test
	public void checkHasDefaultElementTypes() {
		assertTrue(factory.containsNodeType("NodeImpl"));
		assertTrue(factory.getNode("NodeImpl") instanceof NodeImpl);
		assertTrue(factory.containsNodeType("PamNodeImpl"));
		assertTrue(factory.getNode("PamNodeImpl") instanceof PamNodeImpl);
		assertTrue(factory.containsNodeType("NoDecayPamNode"));

		PamNode n = (PamNode) factory.getNode("NoDecayPamNode");
		assertTrue(n.getBaseLevelDecayStrategy() instanceof NoDecayStrategy);
		assertTrue(n.getBaseLevelExciteStrategy() instanceof NoExciteStrategy);

		assertTrue(factory.containsLinkType("LinkImpl"));
		assertTrue(factory.containsLinkType("PamLinkImpl"));
		assertTrue(factory.containsLinkType("NoDecayPamLink"));

		assertTrue(factory.containsStrategy("noDecay"));
		assertTrue(factory.containsStrategy("noExcite"));
	}
}
