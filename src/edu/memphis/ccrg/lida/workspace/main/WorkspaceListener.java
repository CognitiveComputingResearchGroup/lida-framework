package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface WorkspaceListener {
	
	public abstract void receiveWorkspaceContent(NodeStructure content);

}
