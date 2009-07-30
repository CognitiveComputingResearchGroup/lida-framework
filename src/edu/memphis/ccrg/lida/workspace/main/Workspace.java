package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicbuffer.EpisodicBuffer;
import edu.memphis.ccrg.lida.workspace.perceptualbuffer.PerceptualBuffer;

public interface Workspace{
	 
	public abstract void addContentToCSM(NodeStructure updatedContent);
	public abstract CurrentSituationalModel getCSM();	
	public abstract PerceptualBuffer getPerceptualBuffer();	
	public abstract EpisodicBuffer getEpisodicBuffer();
	public abstract BroadcastQueue getBroadcastBuffer();
	public abstract void addCueListener(CueListener l);
	public abstract void addPamWorkspaceListener(WorkspaceListener pam);
	
}
