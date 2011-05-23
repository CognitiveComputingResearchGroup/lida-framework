package edu.memphis.ccrg.lida.attentioncodelets;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class MockAttentionCodeletImpl extends AttentionCodeletImpl {

	@Override
	public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {
		
		return false;
	}

	@Override
	public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
		
		return null;
	}

}
