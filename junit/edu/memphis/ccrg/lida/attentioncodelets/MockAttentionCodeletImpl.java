/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.attentioncodelets;

import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBuffer;

public class MockAttentionCodeletImpl extends AttentionCodeletImpl {

	@Override
	public boolean bufferContainsSoughtContent(WorkspaceBuffer buffer) {

		return false;
	}

	@Override
	public NodeStructure retrieveWorkspaceContent(WorkspaceBuffer buffer) {

		return null;
	}

	@Override
	public String toString() {
		return null;
	}

}
