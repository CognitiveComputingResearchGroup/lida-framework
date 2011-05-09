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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.shared.NodeImpl;

/**
 * @author Siminder Kaur
 *
 */
public class LinearDecayStrategyTest{

	LinearDecayStrategy decayStrategy;
	NodeImpl node1;
	
	@Before
	public void setUp() throws Exception {
		decayStrategy = new LinearDecayStrategy();		
		node1 = new NodeImpl();
		
		node1.setId(1);
		node1.setDecayStrategy(decayStrategy);
		node1.setActivation(0.8);	
	}	

	/**
	 * Test method for {@link edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy#decay(double, long, Object...)}.
	 */
	@Test
	public void testDecay() {
		double newActivation = decayStrategy.decay(node1.getActivation(), 1000);	
		node1.setActivation(newActivation);		
		
		assertTrue("Problem with Decay", 0.0== node1.getTotalActivation());
	}

}
