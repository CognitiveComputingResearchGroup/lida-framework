package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class BasicGetContentStrategy implements GetContentStrategy {

	@Override
	public NodeStructure getCsmContent(WorkspaceBuffer buffer, NodeStructure soughtContent) {
		return (NodeStructure) buffer.getModuleContent();
	}

}
