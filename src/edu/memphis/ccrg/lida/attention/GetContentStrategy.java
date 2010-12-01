package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public interface GetContentStrategy {

	public NodeStructure getWorkspaceContent(WorkspaceBuffer buffer, NodeStructure soughtContent); 
}
