package edu.memphis.ccrg.lida.workspace.currentsituationalmodel;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceBufferListener;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.CodeletWritable;

/**
 * A CurrentSituationalModel is both CodeletWritable and Accessible.  It has methods
 * to add, send and receive NodeStructure content. 
 * 
 * @author ryanjmccall
 *
 */
public interface CurrentSituationalModel{
	
	/**
	 * Sends the CSM content to listeners.  For example when a cue to the episodic memories
	 * is made.
	 */
	public abstract void sendCSMContent();
	
	/**
	 * Returns the NodeStructure representing the agent's current take on its situation
	 * @return
	 */
	public abstract NodeStructure getModel();
	
	/**
	 * Add a listener to the CSM.  E.g. Transient Episodic Memory
	 * @param workspace
	 */
	public abstract void addBufferListener(WorkspaceBufferListener workspace);
	
}//interface
