package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.ExpectationListener;
import edu.memphis.ccrg.lida.actionselection.triggers.TriggerListener;
import edu.memphis.ccrg.lida.framework.dao.Saveable;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * Interface for the action selection module
 * @author Ryan J McCall
 *
 */
public interface ActionSelection extends TriggerListener, Saveable {
	
	/**
	 * Those classes that should be receiving selected actions from Action Selection
	 * @param listener
	 */
	public abstract void addActionSelectionListener(ActionSelectionListener listener);
	
	public abstract void setExpectationListener(ExpectationListener listener);
	
	public abstract void selectAction();

	public abstract void setTaskSpawner(TaskSpawner taskSpawner);

}
