package edu.memphis.ccrg.lida.actionselection;

/**
 * Something that listens to the Action Selection Module
 * @author Ryan J. McCall
 *
 */
public interface ActionSelectionListener {
	
	/**
	 * Listener must receive actions.  Will be called for each action selected by the behavior network.
	 * @param a
	 */
	public abstract void receiveAction(LidaAction a);

}
