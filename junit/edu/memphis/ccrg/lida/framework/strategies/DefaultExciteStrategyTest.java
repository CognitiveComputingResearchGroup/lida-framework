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
package edu.memphis.ccrg.lida.framework.strategies;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.NodeImpl;

/**
 * @author Siminder Kaur
 *
 */
public class DefaultExciteStrategyTest extends TestCase{
	
	DefaultExciteStrategy exciteStrategy;
	NodeImpl node1;
	
	@Override
	@Before
	public void setUp() throws Exception {
		exciteStrategy = new DefaultExciteStrategy();
		node1 = new NodeImpl();
		
		node1.setId(1);
		node1.setExciteStrategy(exciteStrategy);
		node1.setActivation(0.2);		
	}

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy#excite(double, double, Object...)}.
	 */
	@Test
	public void testExcite() {
		
		double newActivation = exciteStrategy.excite(node1.getActivation(),0.8);		
		node1.setActivation(newActivation);		
		
		assertEquals("Problem with Excite", 1.0, node1.getTotalActivation());
	}

}
