package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;

/**
 * A workspace listener receives content from the workspace.
 * The prime example is PAM. 
 *
 */
public interface WorkspaceListener extends ModuleListener{
	
	/**
	 * Receive NodeStructure content from ModuleType originatingBuffer 
	 * @param originatingBuffer
	 * @param content
	 */
	public abstract void receiveWorkspaceContent(ModuleName originatingBuffer, WorkspaceContent content);

}
