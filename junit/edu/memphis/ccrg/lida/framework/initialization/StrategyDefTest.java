/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class StrategyDefTest {

	private StrategyDef strategy;
	private Strategy instance;
	private static final Logger logger = Logger.getLogger(StrategyDef.class
			.getCanonicalName());

	@Before
	public void setUp() throws Exception {
		strategy = new StrategyDef();
	}

	@Test
	public void testGetInstance() throws InstantiationException,
			IllegalAccessException, ClassNotFoundException {
		Map<String, Object> p = new HashMap<String, Object>();
		strategy = new StrategyDef("Strategy1", "st", p, "public", true);

		try {
			instance = (Strategy) Class.forName("Strategy1").newInstance();
			instance.init(p);
		} catch (InstantiationException e) {
			logger.log(Level.WARNING, "Error creating Strategy.", TaskManager
					.getCurrentTick());
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Error creating Strategy.", TaskManager
					.getCurrentTick());
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Error creating Strategy.", TaskManager
					.getCurrentTick());
		}
		assertEquals("problem with GetInstance", instance, strategy
				.getInstance());
	}

	@Test
	public void testIsFlyWeight() {
		Map<String, Object> p = new HashMap<String, Object>();
		StrategyDef strategy = new StrategyDef("Strategy1", "st", p, "public",
				true);
		assertEquals("problem wit GetInstance", true, strategy.isFlyWeight());
	}

	@Test
	public void testStrategyDefStringStringMapOfStringObjectStringBoolean() {
		Map<String, Object> p = new HashMap<String, Object>();
		StrategyDef strategy = new StrategyDef("Strategy", "st", p, "public",
				true);
		assertEquals("problem wit GetInstance", "Strategy", strategy
				.getClassName());
		assertEquals("problem wit GetInstance", "st", strategy.getName());
		assertEquals("problem wit GetInstance", p, strategy.getParams());
		assertEquals("problem wit GetInstance", "public", strategy.getType());
		assertEquals("problem wit GetInstance", true, strategy.isFlyWeight());
	}

	@Test
	public void testStrategyDef() {
		Map<String, Object> p = new HashMap<String, Object>();
		strategy = new StrategyDef("Strategy", "st", p, "public", true);
		assertEquals("problem with StrategyDef", p, strategy.getParams());
	}

}
