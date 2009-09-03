package edu.memphis.ccrg.lida.environment;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public abstract class EnvironmentImpl extends ModuleDriverImpl implements Environment{
	
	public EnvironmentImpl() {
		super(10, null);
	}
	
	public EnvironmentImpl(int ticksPerCycle){
		super(ticksPerCycle, null);
	}

	public void setTaskManager(LidaTaskManager taskManager) {
		super.setTaskManager(taskManager);
	}

}//class
