package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class ActionSelectionImpl extends LidaModuleImpl implements ActionSelection, ProceduralMemoryListener{
	
	public ActionSelectionImpl( ) {
		super(ModuleName.ActionSelection);
		// TODO Auto-generated constructor stub
	}

	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();

	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}
	
	public void receiveScheme(Scheme s) {
		
	}
	
	public void sendAction(LidaAction a){
		for(ActionSelectionListener l: listeners)
			l.receiveAction(a);
	}

	public Object getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}
	public void addListener(ModuleListener listener) {
		if (listener instanceof ActionSelectionListener){
			addActionSelectionListener((ActionSelectionListener)listener);
		}
	}

}//class
