/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * 
 * @author Ryan J. McCall
 *
 */
public class BasicCodeletAction implements CodeletAction {

	/**
	 * Get the content of the buffer and merge it into the CSM's content
	 */
	@Override
	public void performAction(WorkspaceBuffer buffer, WorkspaceBuffer currentSituationalModel) {
		NodeStructure bufferContent = (NodeStructure) buffer.getModuleContent();
		NodeStructure csmContent = (NodeStructure) currentSituationalModel.getModuleContent();
		csmContent.mergeWith(bufferContent);
	} 

}