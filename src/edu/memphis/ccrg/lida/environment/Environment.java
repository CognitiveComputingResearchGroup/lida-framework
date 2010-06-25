package edu.memphis.ccrg.lida.environment;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorListener;

public interface Environment extends SensoryMotorListener, LidaModule{

	public abstract void resetEnvironment();
	
	public void setTaskManager(LidaTaskManager taskManager);

}
