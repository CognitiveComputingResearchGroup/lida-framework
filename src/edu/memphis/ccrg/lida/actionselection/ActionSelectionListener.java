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
	 * @param id - Id of the action stored in sensory motor memory
	 */
	public abstract void receiveActionId(long id);

}
