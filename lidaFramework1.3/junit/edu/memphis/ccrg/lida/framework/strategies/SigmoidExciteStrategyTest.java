/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Daqi
 * 
 */
public class SigmoidExciteStrategyTest {

	private SigmoidExciteStrategy strategy;
	private double epsilon = 1e-5;

	@Before
	public void setUp() {
		strategy = new SigmoidExciteStrategy();
	}

	@Test
	public void testVarargs() {
		double result = strategy.excite(0.6, 3.0);
		assertEquals(0.967874, result, epsilon);

		result = strategy.excite(0.001, .5);
		assertEquals(0.001647, result, epsilon);

		result = strategy.excite(0.0, 1000.0);
		assertEquals(1.0, result, epsilon);

		result = strategy.excite(1.0, 3);
		assertEquals(1.0, result, epsilon);
	}

	@Test
	public void testMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		double result = strategy.excite(0.6, 3, params);
		assertEquals(0.967874, result, epsilon);

		result = strategy.excite(0.001, .5, params);
		assertEquals(0.001647, result, epsilon);

		result = strategy.excite(0.0, 1000, params);
		assertEquals(1.0, result, epsilon);

		result = strategy.excite(1.0, 3, params);
		assertEquals(1.0, result, epsilon);
	}

	@Test
	public void testVarargs1() {
		double result = strategy.excite(0.6, 3, 3.0, 0.4);
		assertEquals(0.999917, result, epsilon);

		result = strategy.excite(0.6, 3, 0.3, 0.4);
		assertEquals(0.78675, result, epsilon);

		result = strategy.excite(1.0, 3, 0.3, 0.4);
		assertEquals(1.0, result, epsilon);

		result = strategy.excite(0.9999999, 65, 0.3, 0.4);
		assertEquals(1.0, result, epsilon);

		result = strategy.excite(.6, 0, 0.3, 0.4);
		assertEquals(.6, result, epsilon);

		result = strategy.excite(0, 70, 0.3, 0.4);
		assertEquals(.116515, result, epsilon);
	}

	@Test
	public void testMap1() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("a", 3.0);
		params.put("c", 0.4);

		double result = strategy.excite(0.6, 3, params);
		assertEquals(0.999917, result, epsilon);

		params.put("a", 0.3);
		params.put("c", 0.4);
		result = strategy.excite(0.6, 3, params);
		assertEquals(0.78675, result, epsilon);

		params.put("a", 0.3);
		params.put("c", 0.4);
		result = strategy.excite(1.0, 3, params);
		assertEquals(1.0, result, epsilon);

		result = strategy.excite(0.999999999, 65, params);
		assertEquals(1.0, result, epsilon);
	}

	@Test
	public void testInit2() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("a", 0.3);
		params.put("c", 0.4);

		strategy.init(params);

		double result = strategy.excite(0.6, 3);
		assertEquals(0.78675, result, epsilon);

		result = strategy.excite(1.0, 3);
		assertEquals(1.0, result, epsilon);

		result = strategy.excite(0.999999999, 65);
		assertEquals(1.0, result, epsilon);
	}

}
