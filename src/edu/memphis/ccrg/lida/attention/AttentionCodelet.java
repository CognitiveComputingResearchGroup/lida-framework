/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * The interface for LIDA's AttentionCodelet.
 * Specific implementations of AttentionCodlet must implement this interface.
 * AttentionCodelet in LIDA communicates with the workspace returning coalitions
 * @author Ryan J McCall
 */
public interface AttentionCodelet extends Codelet{
	
	/**
  	 * Returns true if the current situational model contains the content which this codelet seeks.
  	 * 
     */
	public abstract boolean csmHasDesiredContent();

	/**
	 * 
	 */
	public abstract NodeStructure getCsmContent();
	
	/**
	 * @param gw the GlobalWorkspace to set
	 */
	public void setGlobalWorkspace(GlobalWorkspace gw);
	
	public void setWorkspaceBuffer (WorkspaceBuffer wb);
	/**
	 * @return the WorkspaceBuffer
	 */
	public WorkspaceBuffer getWorkspaceBuffer();

}
