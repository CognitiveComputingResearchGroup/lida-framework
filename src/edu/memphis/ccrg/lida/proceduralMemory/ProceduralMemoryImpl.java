package edu.memphis.ccrg.lida.proceduralMemory;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.GuiContentProvider;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class ProceduralMemoryImpl implements ProceduralMemory, GuiContentProvider {
	
	private NodeStructure broadcast = new NodeStructureImpl();
	private List<ProceduralMemoryListener> listeners = new ArrayList<ProceduralMemoryListener>();
	private List<Object> guiContent = new ArrayList<Object>();

	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		listeners.add(listener);		
	}

	public void receiveBroadcast(BroadcastContent bc) {
		broadcast = (NodeStructure) bc;
	}//method

	public void activateSchemes() {
		// TODO Auto-generated method stub
		
	}

	public void sendSchemes() {
		// TODO Auto-generated method stub
		
	}
	
	public List<Object> getGuiContent() {
		return guiContent ;
	}

}//class
