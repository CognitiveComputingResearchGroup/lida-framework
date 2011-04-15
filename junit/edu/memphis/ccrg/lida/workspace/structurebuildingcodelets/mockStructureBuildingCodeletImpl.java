package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;


public class mockStructureBuildingCodeletImpl extends BasicStructureBuildingCodelet implements StructureBuildingCodelet{

	public Set<WorkspaceBuffer> getReadableBuffers(){
		return readableBuffers;
	}
	
	public WorkspaceBuffer getWritableBuffer() {
		return writableBuffer;
	}
	
	@Override
	public boolean hasSoughtContent(WorkspaceBuffer buffer) {
		// 
		return false;
	}

	@Override
	public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {
		// 
		return null;
	}

	@Override
	protected void runThisLidaTask() {
		// 
		
	}
	
}