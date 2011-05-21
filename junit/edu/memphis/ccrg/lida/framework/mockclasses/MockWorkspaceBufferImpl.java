package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

public class MockWorkspaceBufferImpl extends WorkspaceBufferImpl {
	
	public Map<String, Object> params;
	public NodeStructure content;
	
	@Override
	public WorkspaceContent getBufferContent(Map<String, Object> params) {
		this.params = params;
		return super.getBufferContent(params);
	}
	
	@Override
	public void addBufferContent(WorkspaceContent content) {
		super.addBufferContent(content);
		this.content = content;
	}

}
