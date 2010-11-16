/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;

/**
 * A workspace listener receives content from the workspace.
 * The prime example is PAM. 
 *
 */
public interface WorkspaceListener extends ModuleListener{
	
	/**
	 * Receive NodeStructure content from ModuleType originatingBuffer 
	 * @param originatingBuffer source of content
	 * @param content sent content
	 */
	public void receiveWorkspaceContent(ModuleName originatingBuffer, WorkspaceContent content);

}
