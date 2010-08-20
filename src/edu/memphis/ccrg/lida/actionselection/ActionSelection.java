package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.actionselection.triggers.TriggerListener;

/**
 * Interface for the action selection module
 * @author Ryan J McCall
 *
 */
public interface ActionSelection extends TriggerListener{
	
	/**
	 * Those classes that should be receiving selected actions from Action Selection
	 * @param listener
	 */
	public abstract void addActionSelectionListener(ActionSelectionListener listener);
	
	public abstract void selectAction();

}
