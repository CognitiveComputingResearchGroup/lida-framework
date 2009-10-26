package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

/**
 * A WorkspaceBuffer listener must receive buffer content.
 * The WorkspaceImpl class is an example of a WorkspaceBufferListener
 * 
 * @author ryanjmccall
 *
 */
public interface WorkspaceBufferListener {

	public abstract void receiveBufferContent(Module originatingBuffer, WorkspaceContent ns);

}
