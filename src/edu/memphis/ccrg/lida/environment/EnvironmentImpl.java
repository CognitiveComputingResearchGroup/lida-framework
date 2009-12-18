package edu.memphis.ccrg.lida.environment;

import java.util.Properties;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;

public abstract   class EnvironmentImpl extends ModuleDriverImpl implements Environment{

	public EnvironmentImpl() {
		super(10, null);
	}
	
	public EnvironmentImpl(int ticksPerCycle){
		super(ticksPerCycle, null);
	}

	public void setTaskManager(LidaTaskManager taskManager) {
		super.setTaskManager(taskManager);
	}

	public ModuleName getModuleName() {
		return ModuleName.Environment;
	}

	public LidaModule getSubmodule(ModuleName type) {
		return null;
	}


}//class
