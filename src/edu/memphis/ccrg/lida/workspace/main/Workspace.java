/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.main;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.transientepisodicmemory.CueListener;

/**
 * The workspace collection of submodules where episodic memories, recent contents
 * of conscious, and the current situational model is stored.  A workspace should be
 * interfaceable with codelets which operate on the contents of these submodules.  
 * 
 * @author ryanjmccall
 *
 */
public interface Workspace extends LidaModule{

	/**
	 * Add episodic memory that will listen for cues from the Workspace
	 * @param l listener
	 */
	public void addCueListener(CueListener l);
	
	/**
	 * Add pam that will listen for episodic memories to ground in PAM.
	 * @param listener listener
	 */
	public void addWorkspaceListener(WorkspaceListener listener);

	public void cueEpisodicMemories(NodeStructure content);
	
	public void setActivationLowerBound(double lowerBound);
}
