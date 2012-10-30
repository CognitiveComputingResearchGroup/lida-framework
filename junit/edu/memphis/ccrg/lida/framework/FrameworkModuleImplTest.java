/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl;

public class FrameworkModuleImplTest {

	private TestModule module;
	private TestModule submodule;
	private double epsilon = 10e-9;

	private class TestModule extends FrameworkModuleImpl {

		public boolean initCalled;

		@Override
		public void init() {
			initCalled = true;
		}

		@Override
		public Object getModuleContent(Object... params) {
			return 10;
		}

		public boolean decayed;

		@Override
		public void decayModule(long ticks) {
			decayed = true;
		}
	}

	@Before
	public void setUp() throws Exception {
		module = new TestModule();
		module.setModuleName(ModuleName.Agent);
		submodule = new TestModule();
		submodule.setModuleName(ModuleName.EpisodicBuffer);
		module.addSubModule(submodule);
	}

	@Test
	public void testContainsSubmodule1() {
		assertTrue(module.containsSubmodule(ModuleName.EpisodicBuffer));
		assertTrue(module.containsSubmodule("EpisodicBuffer"));
		assertFalse(module.containsSubmodule(ModuleName.ActionSelection));
		assertFalse(module.containsSubmodule("ActionSelection"));

		TestModule newSub = new TestModule();
		ModuleName newSubName = ModuleName.addModuleName("AI");
		newSub.setModuleName(newSubName);
		module.addSubModule(newSub);

		assertTrue(module.containsSubmodule(newSubName));
		assertTrue(module.containsSubmodule("AI"));
	}

	@Test
	public void testGetModuleContent() {
		assertEquals(10, module.getModuleContent(0));
	}

	@Test
	public void testGetAssistingTaskSpawner() {
		TaskSpawner ts = new TaskSpawnerImpl();
		module.setAssistingTaskSpawner(ts);
		assertEquals(ts, module.getAssistingTaskSpawner());
	}

	@Test
	public void testGetSubmodule1() {
		FrameworkModule sub = module.getSubmodule(ModuleName.EpisodicBuffer);
		assertEquals(submodule, sub);

		sub = module.getSubmodule(ModuleName.DeclarativeMemory);
		assertEquals(null, sub);

		ModuleName foo = null;
		sub = module.getSubmodule(foo);
		assertEquals(null, sub);
	}

	@Test
	public void testGetSubmodule2() {
		FrameworkModule sub = module.getSubmodule("EpisodicBuffer");
		assertEquals(submodule, sub);

		sub = module.getSubmodule("DeclarativeMemory");
		assertEquals(null, sub);

		String foo = null;
		sub = module.getSubmodule(foo);
		assertEquals(null, sub);

		foo = "asdlfkjsdlfkj";
		sub = module.getSubmodule(foo);
		assertEquals(null, sub);
	}

	@Test
	public void testTaskManagerDecayModule() {
		module.taskManagerDecayModule(10);
		assertTrue(module.decayed);
		assertTrue(submodule.decayed);
	}

	@Test
	public void testGetModuleName() {
		assertEquals(ModuleName.Agent, module.getModuleName());
		assertEquals(ModuleName.EpisodicBuffer, submodule.getModuleName());
	}

}
