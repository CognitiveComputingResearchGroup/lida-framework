package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class ActionSelectionImpl implements ActionSelection, ProceduralMemoryListener{
	
	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();

	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}
	
	public void receiveScheme(Scheme s) {
		//System.out.println("scheme id " + s.getId());
	}
	
	public void sendAction(LidaAction a){
		for(ActionSelectionListener l: listeners)
			l.receiveAction(a);
	}

}//class
