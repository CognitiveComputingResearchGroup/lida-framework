/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.episodicmemory.LocalAssociationListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.PamListener;

/**
 * 
 * The Workspace contains the Perceptual and 
 * Episodic Buffers as well as the Broadcast Queue and Current Situational Model. 
 * This class implements the Facade pattern.  Any outside module that wishes to access and/or 
 * modify these Workspace components must do so through this class. 
 * Thus this class defines the methods to access the data of these submodules.
 * 
 * @author Javier Snaider, Ryan J. McCall
 *
 */

public class WorkspaceImpl extends LidaModuleImpl implements Workspace, PamListener, 
									  	LocalAssociationListener, BroadcastListener{
	
	private static final Logger logger = Logger.getLogger(WorkspaceImpl.class.getCanonicalName());

	private List<CueListener> cueListeners = new ArrayList<CueListener>();
	private List<WorkspaceListener> workspaceListeners = new ArrayList<WorkspaceListener>();
	
	public WorkspaceImpl(){
		super (ModuleName.Workspace);
	}	
	
	@Override
	public void addSubModule(LidaModule lm){
		super.addSubModule(lm);
	}
	
	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof WorkspaceListener){
			addWorkspaceListener((WorkspaceListener)listener);
		}else if (listener instanceof CueListener){
			addCueListener((CueListener)listener);
		}else{
			logger.log(Level.WARNING, "Listener " + listener + " was not added, wrong type.", 
					LidaTaskManager.getCurrentTick());
		}
	}

	@Override
	public void addCueListener(CueListener l){
		cueListeners.add(l);
	}

	@Override
	public void addWorkspaceListener(WorkspaceListener listener){
		workspaceListeners.add(listener);
	}
	
	@Override
	public void cueEpisodicMemories(NodeStructure content){
		for(CueListener c: cueListeners){
			c.receiveCue(content);
		}
		logger.log(Level.FINER, "Cue performed.", LidaTaskManager.getCurrentTick());
	}
	
	/*
	 * Received broadcasts are sent to the BroadcastQueue
	 */
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		((BroadcastListener)getSubmodule(ModuleName.BroadcastQueue)).receiveBroadcast(bc);	
	}
	
	/*
	 * Received local associations are merged into the episodic buffer.
	 * Then they are sent to PAM.
	 */
	@Override
	public void receiveLocalAssociation(NodeStructure association) {
		NodeStructure ns = (NodeStructure) getSubmodule(ModuleName.EpisodicBuffer).getModuleContent();
		ns.mergeWith(association);
		for(WorkspaceListener listener: workspaceListeners){
			listener.receiveWorkspaceContent(ModuleName.EpisodicBuffer, (WorkspaceContent) ns);
		}
	}
	
	/*
	 * Implementation of the PamListener interface.  Send received Node Structure to the
	 * the perceptualBuffer.
	 */
	@Override
	public void receivePercept(NodeStructure newPercept) {
		((NodeStructure)getSubmodule(ModuleName.PerceptualBuffer).getModuleContent()).mergeWith(newPercept);
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void learn(BroadcastContent content) {
		// Not applicable
	}

	@Override
	public void init() {
		// Not applicable		
	}
	
}
