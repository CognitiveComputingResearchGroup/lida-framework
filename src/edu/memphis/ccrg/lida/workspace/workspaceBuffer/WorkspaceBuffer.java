package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * This interface defines how codelets can access data from Workspace sub modules.
 * Modules that need to be accessible to codelets should implement this interface.
 * 
 * @author ryanjmccall
 *
 */
public interface WorkspaceBuffer extends LidaModule{
	
 public void setLowerActivationBound (double lowerActivationBound);
}
