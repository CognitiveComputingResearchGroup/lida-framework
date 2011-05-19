package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Map;

import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

public class MockWorkspaceBufferImpl extends WorkspaceBufferImpl {
	
	public Map<String, Object> params;
	
	@Override
	public WorkspaceContent getBufferContent(Map<String, Object> params) {
		this.params = params;
		return super.getBufferContent(params);
	}

}
