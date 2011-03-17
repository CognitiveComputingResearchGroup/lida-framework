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
 * An encapsulation of the action of a codelet.
 *
 * @author Ryan J. McCall
 *
 */
public interface CodeletAction {

	/**
	 * An action from specified ordered buffer to a destination buffer 
	 * @param soughtContent TODO
	 * @param readable buffer from which codelet reads
	 * @param writable buffer to which the result of action is written.  
	 */	
	public void performAction(NodeStructure soughtContent, WorkspaceBuffer readable, WorkspaceBuffer writable);
	
}
  