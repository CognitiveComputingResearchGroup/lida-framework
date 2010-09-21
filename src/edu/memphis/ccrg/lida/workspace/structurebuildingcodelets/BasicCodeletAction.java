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

public class BasicCodeletAction implements CodeletAction {

	/**
	 * Simply get the content of the buffer and merge it into the CSM's content
	 */
	public void performAction(WorkspaceBuffer buffer, WorkspaceBuffer csm) {
		
		NodeStructure bufferContent = (NodeStructure) buffer.getModuleContent();
		
		//System.out.println("Codelet performing action, perceptual buff has " + bufferContent.getNodeCount());
		
		NodeStructure csmContent = (NodeStructure) csm.getModuleContent();
		csmContent.mergeWith(bufferContent);
	} 

}//class