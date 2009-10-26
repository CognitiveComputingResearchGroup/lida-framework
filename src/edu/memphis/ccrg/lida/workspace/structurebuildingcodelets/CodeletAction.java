package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * An encapsulation of the action of a codelet.
 *
 * @author ryanjmccall
 *
 */
public interface CodeletAction {

	/**
	 * An action from specified ordered buffer to a destination buffer 
	 * @param buffer
	 * @param destination
	 */	
	public void performAction(WorkspaceBuffer buffer);
	
}
  