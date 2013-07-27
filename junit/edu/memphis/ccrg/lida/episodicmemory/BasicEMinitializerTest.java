/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.episodicmemory;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.sdm.BasicTranslator;
import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.AgentImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNS;

public class BasicEMinitializerTest {

	private BasicEpisodicMemoryInitializer initializer;

	@Before
	public void setUp() throws Exception {
		initializer = new BasicEpisodicMemoryInitializer();
	}

	@Test
	public void testInitModule() {
		TaskManager tm = new TaskManager(10, 10, -1,null);
		Agent agent = new AgentImpl(tm);
		PerceptualAssociativeMemoryNS pam = new MockPAM();
		pam.setModuleName(ModuleName.PerceptualAssociativeMemory);
		agent.addSubModule(pam);
		EpisodicMemoryImpl module = new EpisodicMemoryImpl();
		initializer.initModule(module, agent, null);
		assertTrue(module.getTranslator() instanceof BasicTranslator);
	}

	@Test
	public void testInitModule1() {
		TaskManager tm = new TaskManager(10, 10, -1,null);
		Agent agent = new AgentImpl(tm);
		EpisodicMemoryImpl module = new EpisodicMemoryImpl();
		initializer.initModule(module, agent, null);
		assertNull(module.getTranslator());
	}

}
