package edu.memphis.ccrg.lida.workspace.episodicbuffer;

import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletAccessible;

/**
 * Data structure to hold recent local associations. 
 * @author ryanjmccall
 *
 */
public interface EpisodicBuffer extends CodeletAccessible{
	
	/**
	 * Add a listener to the Episodic Buffer.  Currently the WorkspaceImpl listens
	 * to the Episodic Buffer.
	 * @param workspace
	 */
	public abstract void addBufferListener(WorkspaceBufferListener workspace);

}
