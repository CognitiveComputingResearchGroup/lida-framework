package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.ModuleType;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class ActionSelectionImpl implements ActionSelection, ProceduralMemoryListener, GuiEventProvider {

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

	public void sendEvent(FrameworkGuiEvent evt) {
		for(FrameworkGuiEventListener g: guiEventListeners){
			//g.receiveGuiEvent(new FrameworkGuiEvent(ModuleType.actionSelection,"data", guiContent));
		}
	}

}//class
