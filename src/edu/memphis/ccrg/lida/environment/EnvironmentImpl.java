package edu.memphis.ccrg.lida.environment;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskNames;

public abstract class EnvironmentImpl extends ModuleDriverImpl implements Environment{
	
	public EnvironmentImpl() {
		super(10, null, LidaTaskNames.ENVIRONMENT);
	}
	
	public EnvironmentImpl(int ticksPerCycle){
		super(ticksPerCycle, null, LidaTaskNames.ENVIRONMENT);
	}

	public void setTaskManager(LidaTaskManager taskManager) {
		super.setTaskManager(taskManager);
	}

}//class
