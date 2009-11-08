package edu.memphis.ccrg.lida.environment;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;

public interface Environment extends ActionSelectionListener{

	public abstract void resetEnvironment();
	
	public void setTaskManager(LidaTaskManager taskManager);

}
