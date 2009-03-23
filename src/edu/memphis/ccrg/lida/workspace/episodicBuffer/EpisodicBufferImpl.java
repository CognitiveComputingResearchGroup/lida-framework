package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletObjective;
import edu.memphis.ccrg.lida.workspace.sbCodelets.WorkspaceContent;

public class EpisodicBufferImpl implements EpisodicBuffer, CodeletAccessible{

    private List<EBufferListener> listeners;
	
	public EpisodicBufferImpl(){
		listeners = new ArrayList<EBufferListener>();
	}

	public void addEBufferListener(EBufferListener listener) {
		listeners.add(listener);		
	}

	public WorkspaceContent getCodeletsObjective(CodeletObjective objective) {
		// TODO Auto-generated method stub
		return null;
	}

}
