package edu.memphis.ccrg.lida.attention;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockAttentionCodeletImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;

public class MockAttentionCodeletImplTest {
	
	MockAttentionCodeletImpl codelet;
	LidaModule module;

	@Before
	public void setUp() throws Exception {
		module = new GlobalWorkspaceImpl();
		codelet = new MockAttentionCodeletImpl();
		
		module.setModuleName(ModuleName.GlobalWorkspace);
	}

	@Test
	public void testRunThisLidaTask() {
		
	}

	@Test
	public void testSetAssociatedModule() {
		codelet.setAssociatedModule(module,ModuleUsage.TO_WRITE_TO);
		
		assertEquals("Problem with setAssociatedModule",ModuleName.GlobalWorkspace,codelet.globalWorkspace.getModuleName());
	}

}
