package edu.memphis.ccrg.lida.proceduralMemory;

import java.util.List;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;

public class ProceduralMemoryImpl implements ProceduralMemory {
	
	private NodeStructure broadcast = new NodeStructureImpl();

	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		// TODO Auto-generated method stub
		
	}

	public List<Object> getGuiContent() {
		// TODO Auto-generated method stub
		return null;
	}

	public void receiveBroadcast(BroadcastContent bc) {
		broadcast = (NodeStructure) bc;
		
	}//method

}//class
