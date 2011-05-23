package edu.memphis.ccrg.lida.episodicmemory;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.episodicmemory.sdm.BasicTranslator;
import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.AgentImpl;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.tasks.MockTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;


public class BasicEMinitializerTest {

	private BasicEpisodicMemoryInitializer initializer;
	@Before
	public void setUp() throws Exception {
		initializer = new BasicEpisodicMemoryInitializer();
	}

	@Test
	public void testInitModule() {
		TaskManager tm = new TaskManager(10, 10);
		Agent agent = new AgentImpl(tm);
		PerceptualAssociativeMemory pam = new MockPAM();
		pam.setModuleName(ModuleName.PerceptualAssociativeMemory);
		agent.addSubModule(pam);
		EpisodicMemoryImpl module = new EpisodicMemoryImpl();
		initializer.initModule(module, agent, null);
		assertTrue(module.getTranslator() instanceof BasicTranslator);
	}

	@Test
	public void testInitModule1() {
		TaskManager tm = new TaskManager(10, 10);
		Agent agent = new AgentImpl(tm);
		EpisodicMemoryImpl module = new EpisodicMemoryImpl();
		initializer.initModule(module, agent, null);
		assertNull(module.getTranslator());
	}

}
