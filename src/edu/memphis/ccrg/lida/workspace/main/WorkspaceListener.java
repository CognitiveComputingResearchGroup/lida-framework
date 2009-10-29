package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.ModuleType;

/**
 * A workspace listener receives content from the workspace.
 * The prime example is PAM. 
 *
 */
public interface WorkspaceListener {
	
	/**
	 * Receive NodeStructure content from ModuleType originatingBuffer 
	 * @param originatingBuffer
	 * @param content
	 */
	public abstract void receiveWorkspaceContent(ModuleType originatingBuffer, WorkspaceContent content);

}
