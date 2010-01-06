package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

/**
 * A WorkspaceBuffer listener must receive buffer content.
 * The WorkspaceImpl class is an example of a WorkspaceBufferListener
 * 
 * @author ryanjmccall
 *
 */
public interface WorkspaceBufferListener extends ModuleListener{

	public abstract void receiveBufferContent(ModuleName originatingBuffer, WorkspaceContent ns);

}
