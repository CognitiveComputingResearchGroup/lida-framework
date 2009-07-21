package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class ActionSelectionImpl implements ActionSelection, ProceduralMemoryListener, GuiContentProvider {

	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
	private List<Object> guiContent = new ArrayList<Object>();
	
	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}
	
	public void receiveSchemes(List<Scheme> schemes) {
		// TODO Auto-generated method stub
		
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	public void sendEvent() {
		for(FrameworkGuiEventListener g: guis)
			g.receiveGuiEvent(new FrameworkGuiEvent(FrameworkGuiEvent.ACTION_SELECTION,"data", guiContent));
		
	}

}//class
