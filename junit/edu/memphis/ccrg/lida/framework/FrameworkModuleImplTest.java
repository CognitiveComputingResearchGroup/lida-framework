package edu.memphis.ccrg.lida.framework;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl;

public class FrameworkModuleImplTest {
	
	private TestModule module;
	private TestModule submodule;
	
	private class TestModule extends FrameworkModuleImpl{
		@Override
		public void addListener(ModuleListener listener) {	
		}
		
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
	public void testInitMap() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg0", 10.0);
		params.put("name", "Javier");
		
		module.init(params);
		
		assertEquals(10.0, module.getParam("arg0", null));
		assertEquals("Javier", module.getParam("name", null));
		assertTrue(module.initCalled);
	}

	@Test
	public void testGetParam() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg0", 10.0);
		params.put("name", "Javier");
		
		module.init(params);
		
		assertEquals(5, module.getParam("sdflkj", 5));
		assertNotSame(true, module.getParam("name", true));
		assertTrue(module.initCalled);
	}

	@Test
	public void testGetSubmodule1() {
		FrameworkModule sub = module.getSubmodule(ModuleName.EpisodicBuffer);
		assertEquals(submodule, sub);
		
		sub =  module.getSubmodule(ModuleName.DeclarativeMemory);
		assertEquals(null, sub);
		
		ModuleName foo = null;
		sub =  module.getSubmodule(foo);
		assertEquals(null, sub);
	}

	@Test
	public void testGetSubmodule2() {
		FrameworkModule sub = module.getSubmodule("EpisodicBuffer");
		assertEquals(submodule, sub);
		
		sub =  module.getSubmodule("DeclarativeMemory");
		assertEquals(null, sub);
		
		String foo = null;
		sub =  module.getSubmodule(foo);
		assertEquals(null, sub);
		
		foo = "asdlfkjsdlfkj";
		sub =  module.getSubmodule(foo);
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
