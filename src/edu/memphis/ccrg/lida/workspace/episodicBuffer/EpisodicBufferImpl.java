package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletObjective;

public class EpisodicBufferImpl implements EpisodicBuffer, CodeletAccessible{

    private List<EpisodicBufferListener> listeners;
	
	public EpisodicBufferImpl(){
		listeners = new ArrayList<EpisodicBufferListener>();
	}

	public void addEBufferListener(EpisodicBufferListener listener) {
		listeners.add(listener);		
	}

	public WorkspaceContent getCodeletsObjective(CodeletObjective objective) {
		// TODO Auto-generated method stub
		return null;
	}

}
