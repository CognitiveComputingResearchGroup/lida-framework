/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.structurebuildingcodelets;

import java.util.Set;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

/**
 * Demon-like process operating on the workspace searching for particular content
 * which, when found, triggers its action producing its result.  
 * Has workspace buffers it can access.
 * 
 * @author ryanjmccall
 *
 */
public interface StructureBuildingCodelet extends Codelet{

	 public void setSoughtContent(NodeStructure content);
	 public NodeStructure getSoughtContent();
	
	 public void setCodeletAction(CodeletAction a);
	 public CodeletAction getCodeletAction();
	 
	 public void setCodeletResult(CodeletResult cr);
	 public CodeletResult getCodeletResult();

	 public void addAccessibleBuffer(WorkspaceBuffer buffer);
	
	 /**
	  * Type is determined by what buffers are accessible to this codelet
	  * @return type
	  */
	 public int getType();

}//interface