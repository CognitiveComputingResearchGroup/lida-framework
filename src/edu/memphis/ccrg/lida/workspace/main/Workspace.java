package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;
import edu.memphis.ccrg.lida.workspace.broadcastbuffer.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * The workspace collection of submodules where episodic memories, recent contents
 * of conscious, and the current situational model is stored.  A workspace should be
 * interfaceable with codelets which operate on the contents of these submodules.  
 * 
 * @author ryanjmccall
 *
 */
public interface Workspace extends LidaModule{

	public abstract WorkspaceBuffer getCSM();	
	public abstract WorkspaceBuffer getEpisodicBuffer();
	public abstract WorkspaceBuffer getPerceptualBuffer();
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
