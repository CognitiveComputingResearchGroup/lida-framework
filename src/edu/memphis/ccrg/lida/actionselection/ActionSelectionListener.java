package edu.memphis.ccrg.lida.actionselection;

import edu.memphis.ccrg.lida.framework.ModuleListener;

/**
 * Something that listens to the Action Selection Module
 * @author Ryan J. McCall
 *
 */
public interface ActionSelectionListener extends ModuleListener{
	
	/**
	 * Listener must receive actions.  Will be called for each action selected by the behavior network.
	 * @param a
	 */
	public abstract void receiveAction(LidaAction a);

}
