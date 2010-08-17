package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.actionselection.triggers.TriggerListener;

/**
 * Interface for the action selection module
 * @author ryanjmccall
 *
 */
public interface ActionSelection extends TriggerListener{
	
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
	
	public abstract void sendAction();
	
	public abstract void selectAction();

}
