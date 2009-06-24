package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.declarativeMemory.DeclarativeMemoryContent;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.transientEpisodicMemory.TransientEpisodicMemoryContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;

public class EpisodicBufferImpl implements EpisodicBuffer, CodeletReadable{

    private List<EpisodicBufferListener> listeners;
	private WorkspaceContent association;
    
	public EpisodicBufferImpl(int capacity){
		listeners = new ArrayList<EpisodicBufferListener>();
		association = new NodeStructureImpl();
	}

	public void addEBufferListener(EpisodicBufferListener listener) {
		listeners.add(listener);		
	}

	public WorkspaceContent lookForContent(NodeStructure objective) {
		//TODO:
		return (WorkspaceContent) objective;
	}

	public synchronized void receiveLocalAssociation(WorkspaceContent association){
		this.association = association;
	}
}
