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
package edu.memphis.ccrg.lida.framework.strategies;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Siminder Kaur, Javier, Ryan
 * 
 */
public class LinearExciteStrategyTest {

	private LinearExciteStrategy exciteStrategy;
	private double epsilon = 1e-5;

	@Before
	public void setUp() throws Exception {
		exciteStrategy = new LinearExciteStrategy();
	}

	@Test
	public void testDecay1() {
		double newact = exciteStrategy.excite(1.0, 2, (Object[]) null);
		assertEquals(1.0, newact, epsilon);
	}

	@Test
	public void testexcite2() {
		double newact = exciteStrategy.excite(1.0, -2,
				(Map<String, Object>) null);
		assertEquals(0.0, newact, epsilon);
	}

	@Test
	public void testexcite3() {
		double newact = exciteStrategy.excite(0.0, 0.5,
				(Map<String, Object>) null);
		assertEquals(0.5, newact, epsilon);

	}

	@Test
	public void testexcite4() {
		double newact = exciteStrategy.excite(0.6, 0.1,
				(Map<String, Object>) null);
		assertEquals(0.7, newact, epsilon);

	}

	@Test
	public void testexcite5() {
		double newact = exciteStrategy.excite(0.0, 3, (Object[]) null);
		assertEquals(1, newact, epsilon);

	}

	@Test
	public void testexcite6() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("one", 0.2);
		map.put("two", 0.7);
		map.put("three", 1);
		map.put("four", "a");
		double newact = exciteStrategy.excite(0.0, 3, map);
		assertEquals(1.0, newact, epsilon);

	}

	@Test
	public void testexcite8() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		map.put("m", 0.2);

		double newact = exciteStrategy.excite(0.0, 3, map);
		assertEquals(0.6, newact, epsilon);

	}

	@Test
	public void testexcite7() {
		Object[] params = new Object[2];
		params[0] = 0.4;

		double newact = exciteStrategy.excite(0.1, 2, params);
		assertEquals(0.9, newact, epsilon);

		newact = exciteStrategy.excite(1.0, -0.2, params);
		assertEquals(0.92, newact, epsilon);

		newact = exciteStrategy.excite(1.0, -3.0, params);
		assertEquals(0.0, newact, epsilon);
	}

}
