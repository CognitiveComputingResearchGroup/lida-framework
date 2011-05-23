package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

public class MockStructureBuildingCodeletImpl extends
		StructureBuildingCodeletImpl {

	@Override
	protected void runThisFrameworkTask() {
	}

	@Override
	public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {
		return false;
	}

	@Override
	public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
		return null;
	}

}
