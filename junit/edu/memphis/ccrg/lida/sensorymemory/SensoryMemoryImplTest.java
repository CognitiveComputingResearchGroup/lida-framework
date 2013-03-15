/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.sensorymemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule;
import edu.memphis.ccrg.lida.pam.MockPamListener;

/**
 * This class is the JUnit test for <code>SensoryMemoryImpl</code> class.
 * 
 * @author Rodrigo Silva-Lugo
 * @author Ryan J. McCall
 */
public class SensoryMemoryImplTest {

	private SensoryMemoryImpl sensoryMemory;

	@Before
	public void setUp() {
		sensoryMemory = new MockSensoryMemoryImpl();
	}

	@Test
	public void testAddListener() {
		// test1
		ModuleListener listener = new MockSensoryMemoryListener();
		assertEquals(0, sensoryMemory.sensoryMemoryListeners.size());

		sensoryMemory.addListener(listener);

		assertEquals(1, sensoryMemory.sensoryMemoryListeners.size());
		assertTrue(sensoryMemory.sensoryMemoryListeners.contains(listener));

		// test2
		ModuleListener badListener = new MockPamListener();

		sensoryMemory.addListener(badListener);

		assertEquals(1, sensoryMemory.sensoryMemoryListeners.size());
		assertTrue(sensoryMemory.sensoryMemoryListeners.contains(listener));
		assertFalse(sensoryMemory.sensoryMemoryListeners.contains(badListener));
	}

	/**
	 * Test of addSensoryMemoryListener method, of class SensoryMemoryImpl.
	 */
	@Test
	public void testAddSensoryMemoryListener() {
		MockSensoryMemoryListener listener = new MockSensoryMemoryListener();
		assertEquals(0, sensoryMemory.sensoryMemoryListeners.size());

		sensoryMemory.addSensoryMemoryListener(listener);

		assertEquals(1, sensoryMemory.sensoryMemoryListeners.size());
		assertTrue(sensoryMemory.sensoryMemoryListeners.contains(listener));

		// test adding 2 listeners, one for SMM, and one for PAM
		// Do we need to check addSensoryMemoryListener() with paras of SMM or
		// PAM? --Daqi
		// No because the argument type of the method being tested will not
		// allow those. -- Ryan
	}

	/**
	 * Test of setAssociatedModule method, of class SensoryMemoryImpl.
	 */
	@Test
	public void testSetAssociatedModule() {
		// test1 should not set a non-environment module
		MockFrameworkModule module = new MockFrameworkModule();
		String moduleUsage = ModuleUsage.NOT_SPECIFIED;
		assertNull(sensoryMemory.environment);

		sensoryMemory.setAssociatedModule(module, moduleUsage);

		assertNull(sensoryMemory.environment);

		// test2 should set an environment module
		MockEnvironmentImpl env = new MockEnvironmentImpl();

		sensoryMemory.setAssociatedModule(env, moduleUsage);

		assertNotNull(sensoryMemory.environment);
		assertEquals(env, sensoryMemory.environment);
	}

	class MockSensoryMemoryImpl extends SensoryMemoryImpl {

		@Override
		public void runSensors() {
		}

		@Override
		public Object getSensoryContent(String modality,
				Map<String, Object> params) {

			return null;
		}

		@Override
		public void decayModule(long ticks) {
		}

		@Override
		public Object getModuleContent(Object... params) {

			return null;
		}

		@Override
		public void init() {
		}
	}

	public class MockSensoryMemoryListener implements SensoryMemoryListener {
		@Override
		public void receiveSensoryMemoryContent(Object content) {
		}
	}

}