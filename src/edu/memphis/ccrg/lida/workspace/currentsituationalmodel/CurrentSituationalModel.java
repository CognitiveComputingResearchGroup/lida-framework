package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferListener;

/**
 * A CurrentSituationalModel is both CodeletWritable and Accessible.  It has methods
 * to add, send and receive NodeStructure content. 
 * 
 * @author ryanjmccall
 *
 */
public interface CurrentSituationalModel extends WorkspaceBuffer{
	
	/**
	 * Sends the CSM content to listeners.  For example when a cue to the episodic memories
	 * is made.
	 */
	public abstract void sendCsmContent();
	
	/**
	 * Add a listener to the CSM.  E.g. Transient Episodic Memory
	 * @param workspace
	 */
	public abstract void addBufferListener(WorkspaceBufferListener workspace);
	
}//interface
