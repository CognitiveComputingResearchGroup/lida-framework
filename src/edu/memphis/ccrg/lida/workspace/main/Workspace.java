package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.currentsituationalmodel.CurrentSituationalModel;
import edu.memphis.ccrg.lida.workspace.episodicbuffer.EpisodicBuffer;

public interface Workspace{
	 
	/**
	 * To be called by codelets
	 * @param updatedContent
	 */
	public abstract void addContentToCSM(NodeStructure updatedContent);
	public abstract CurrentSituationalModel getCSM();	
	public abstract EpisodicBuffer getEpisodicBuffer();
	public abstract BroadcastQueue getBroadcastQueue();
	
	/**
	 * Add episodic memory that will listen for cues from the Workspace
	 * @param l
	 */
	public abstract void addCueListener(CueListener l);
	
	/**
	 * Add pam that will listen for episodic memories to ground in PAM.
	 * @param pam
	 */
	public abstract void addPamWorkspaceListener(WorkspaceListener pam);
	
}
