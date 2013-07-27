/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl;

public class MockWorkspaceBufferImpl extends WorkspaceBufferImpl {

	public Map<String, Object> params;
	public NodeStructure content = new NodeStructureImpl();

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
