package edu.memphis.ccrg.lida.environment;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public interface Environment extends ActionSelectionListener, LidaModule{

	public abstract void resetEnvironment();
	
	public void setTaskManager(LidaTaskManager taskManager);

}
