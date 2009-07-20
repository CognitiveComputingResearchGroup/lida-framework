package edu.memphis.ccrg.lida.proceduralMemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.GuiContentProvider;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.shared.BroadcastLearner;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class ProceduralMemoryImpl implements ProceduralMemory,
		GuiContentProvider, BroadcastLearner {

	private NodeStructure broadcastContent = new NodeStructureImpl();
	private List<ProceduralMemoryListener> listeners = new ArrayList<ProceduralMemoryListener>();
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private List<Object> guiContent = new ArrayList<Object>();

	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		listeners.add(listener);
	}

	public void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}// method

	public void activateSchemes() {
		// TODO Auto-generated method stub

	}

	public void sendSchemes() {
		for (ProceduralMemoryListener p : listeners)
			p.receiveSchemes(new ArrayList<Scheme>());
	}

	public void learn() {
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {
			// TODO:
			n.getId();
		}
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	public void sendEvent() {
		if (!guis.isEmpty()) {
			FrameworkGuiEvent event = new FrameworkGuiEvent(
					FrameworkGuiEvent.PROCEDURAL_MEMORY, "data", guiContent);
			for (FrameworkGuiEventListener gui : guis) {
				gui.receiveGuiEvent(event);
			}
		}
	}

}// class
