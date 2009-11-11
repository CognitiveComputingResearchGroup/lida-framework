package edu.memphis.ccrg.lida.actionselection;

public interface ActionSelection {
	
	public abstract void addActionSelectionListener(ActionSelectionListener listener);
	
	public abstract void sendAction(LidaAction a);
}
