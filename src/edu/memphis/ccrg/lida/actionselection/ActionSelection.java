package edu.memphis.ccrg.lida.actionselection;

public interface ActionSelection {
	
	/**
	 * Those classes that should be receiving selected actions from Action Selection
	 * @param listener
	 */
	public abstract void addActionSelectionListener(ActionSelectionListener listener);
	
	/**
	 * Action is send to action selection listeners
	 * @param a
	 */
	public abstract void sendAction(LidaAction a);
}
