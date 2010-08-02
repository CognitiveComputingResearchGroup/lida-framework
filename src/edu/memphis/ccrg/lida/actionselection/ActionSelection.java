package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;

/**
 * Interface for the action selection module
 * @author ryanjmccall
 *
 */
public interface ActionSelection {
	
	/**
	 * Those classes that should be receiving selected actions from Action Selection
	 * @param listener
	 */
	public abstract void addActionSelectionListener(ActionSelectionListener listener);
	
	/**
	 * Action is send to action selection listeners
	 * @param actionId
	 */
	public abstract void sendAction(long actionId);

	//TODO Remove methods below eventually	
	public abstract void runAStep();
	public abstract Behavior getFiredBehavior();
	public abstract void reduceTheta();
	public abstract void restoreTheta();
}
