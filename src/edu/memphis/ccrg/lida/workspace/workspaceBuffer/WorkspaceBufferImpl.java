package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class WorkspaceBufferImpl implements WorkspaceBuffer{
	
	private NodeStructure buffer = new NodeStructureImpl();

	/**
	 * @return the buffer
	 */

	public NodeStructure getModuleContent() {
		return buffer;
	}
}
