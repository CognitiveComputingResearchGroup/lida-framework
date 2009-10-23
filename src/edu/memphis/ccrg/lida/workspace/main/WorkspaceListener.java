package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * A workspace listener receives content from the workspace.
 * The prime example is PAM. 
 *
 */
public interface WorkspaceListener {
	
	/**
	 * Receive NodeStructure content from Moduel originatingBuffer 
	 * @param originatingBuffer
	 * @param content
	 */
	public abstract void receiveWorkspaceContent(Module originatingBuffer, NodeStructure content);

}
