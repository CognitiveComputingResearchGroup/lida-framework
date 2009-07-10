package edu.memphis.ccrg.lida.actionSelection;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.FrameworkGui;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.proceduralMemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralMemory.Scheme;

public class ActionSelectionImpl implements ActionSelection, ProceduralMemoryListener, GuiContentProvider {

	private List<FrameworkGui> guis = new ArrayList<FrameworkGui>();
	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
	private List<Object> guiContent = new ArrayList<Object>();
	
	public void addBehaviorListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}
	
	public void addFrameworkGui(FrameworkGui listener) {
		guis.add(listener);
	}

	public void receiveSchemes(List<Scheme> schemes) {
		// TODO Auto-generated method stub
		
	}

	public void sendGuiContent() {
		for(FrameworkGui g: guis)
			g.receiveGuiContent(FrameworkGui.FROM_ACTION_SELECTION, guiContent);
	}

}//class
