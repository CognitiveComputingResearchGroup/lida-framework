package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.Module;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public interface WorkspaceListener {
	
	public abstract void receiveWorkspaceContent(Module originatingBuffer, NodeStructure content);

}
