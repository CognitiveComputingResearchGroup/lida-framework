package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class ActionSelectionImpl implements ActionSelection, ProceduralMemoryListener, GuiContentProvider {

	private List<FrameworkGuiEventListener> guiEventListeners = new ArrayList<FrameworkGuiEventListener>();
	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
	private List<Object> guiContent = new ArrayList<Object>();
	
	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}
	
	public void receiveSchemes(List<Scheme> schemes) {
		// TODO Auto-generated method stub
		
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guiEventListeners.add(listener);
	}

	public void sendEvent() {
		for(FrameworkGuiEventListener g: guiEventListeners)
			g.receiveGuiEvent(new FrameworkGuiEvent(Module.actionSelection,"data", guiContent));
	}

}//class
