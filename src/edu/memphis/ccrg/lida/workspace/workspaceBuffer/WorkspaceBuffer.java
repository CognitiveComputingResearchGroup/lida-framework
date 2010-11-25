/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * This interface defines how codelets can access data from Workspace sub modules.
 * Modules that need to be accessible to codelets should implement this interface.
 * 
 * @author Ryan J McCall, Javier Snaider
 *
 */
public interface WorkspaceBuffer extends LidaModule{
	
	/**
	 * Lower bound for a node or link to remain in the buffer
	 */
	public void setLowerActivationBound (double lowerActivationBound);
}
