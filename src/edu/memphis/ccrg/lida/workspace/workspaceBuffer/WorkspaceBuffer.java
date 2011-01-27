/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.StructureBuildingCodelet;

/**
 * A submodule of the Workspace.  Managed by {@link WorkspaceImpl}.  
 * {@link StructureBuildingCodelet} read and write from them.
 * 
 * @author Ryan J McCall, Javier Snaider
 */
public interface WorkspaceBuffer extends LidaModule{
	
	/**
	 * Sets lowerActivationBound
	 * @param lowerActivationBound lower bound for the amount of activation
	 * a node or link must have to remain in the buffer.
	 */
	public void setLowerActivationBound(double lowerActivationBound);
}
