/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.main;

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
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.pam.PamListener;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener;
import edu.memphis.ccrg.lida.workspace.broadcastqueue.BroadcastQueue;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;

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
									  	LocalAssociationListener,
									  	BroadcastListener, 
									  	SensoryMotorMemoryListener{
	
	private static final Logger logger = Logger.getLogger(WorkspaceImpl.class.getCanonicalName());

	private List<CueListener> cueListeners = new ArrayList<CueListener>();
	private List<WorkspaceListener> wsListeners = new ArrayList<WorkspaceListener>();

	private final double DEFAULT_LOWER_ACTIVATION_BOUND = 0.01;
	private double lowerActivationBound;
	
	public WorkspaceImpl(){
		super (ModuleName.Workspace);
		lowerActivationBound = DEFAULT_LOWER_ACTIVATION_BOUND;
		setActivationLowerBound(lowerActivationBound);
	}
	
	//TODO not used. remove?
	public WorkspaceImpl(WorkspaceBuffer episodicBuffer, WorkspaceBuffer perceptualBuffer,WorkspaceBuffer csm, BroadcastQueue broadcastQueue ){
		this ();
		getSubmodules().put(ModuleName.EpisodicBuffer,episodicBuffer);
		getSubmodules().put(ModuleName.BroadcastQueue,broadcastQueue);
		getSubmodules().put(ModuleName.PerceptualBuffer,perceptualBuffer);
		getSubmodules().put(ModuleName.CurrentSituationalModel,csm);
	}
	
	
	@Override
	public void addSubModule(LidaModule lm){
		super.addSubModule(lm);
	}
	
	/**
	 * @param activationLowerBound the lowerActivationBound to set
	 */
	@Override
	public void setActivationLowerBound(double activationLowerBound) {
		if(activationLowerBound < 0){
			logger.log(Level.WARNING, "Lower bound must be non-negative.  Parameter not set.", 
					LidaTaskManager.getCurrentTick());
		}else{
			this.lowerActivationBound = activationLowerBound;
			for (LidaModule lm: getSubmodules().values()){
				((WorkspaceBuffer)lm).setLowerActivationBound(lowerActivationBound);
			}
		}
		
	}
	
	@Override
	public void init (){		
		lowerActivationBound = (Double) getParam("workspace.activationLowerBound", DEFAULT_LOWER_ACTIVATION_BOUND);
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
		wsListeners.add(listener);
	}
	
	@Override
	public void cueEpisodicMemories(NodeStructure content){
		for(CueListener c: cueListeners)
			c.receiveCue(content);
		logger.log(Level.FINER, "Cue performed.", LidaTaskManager.getCurrentTick());
	}
	private void sendToListeners(NodeStructure content){
		for(WorkspaceListener listener: wsListeners){
			listener.receiveWorkspaceContent(ModuleName.EpisodicBuffer, (WorkspaceContent)content);
		}
	}
	
	/**
	 * Received broadcasts are sent to the broadcast queue.
	 */
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		((BroadcastListener)getSubmodule(ModuleName.BroadcastQueue)).receiveBroadcast(bc);	
	}
		
	/**
	 * Received local associations are merged into the episodic buffer.
	 * Then they are sent to PAM.
	 */
	@Override
	public void receiveLocalAssociation(NodeStructure association) {
		WorkspaceContent ns = (WorkspaceContent) getSubmodule(ModuleName.EpisodicBuffer).getModuleContent();
		//TODO is our merge operation thread-safe?
		ns.mergeWith(association);
		sendToListeners(ns);
	}
	/**
	 * Implementation of the PamListener interface. Send received node to the
	 * the perceptualBuffer.
	 */
	@Override
	public void receiveNode(Node node) {
		((NodeStructure)getSubmodule(ModuleName.PerceptualBuffer).getModuleContent()).addNode(node);
	}
	
	/**
	 * Implementation of the PamListener interface.  Send received link to the
	 * the perceptualBuffer.
	 */
	@Override
	public void receiveLink(Link l) {
		((NodeStructure)getSubmodule(ModuleName.PerceptualBuffer).getModuleContent()).addLink(l);
	}	

	
	/**
	 * Implementation of the PamListener interface.  Send received Node Structure to the
	 * the perceptualBuffer.
	 */
	@Override
	public void receiveNodeStructure(NodeStructure newPercept) {
		NodeStructure perceptualBuffer = (NodeStructure)getSubmodule(ModuleName.PerceptualBuffer).getModuleContent();
		perceptualBuffer.mergeWith(newPercept);
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void receiveExecutingAlgorithm(Object a) {
		// Maybe just pam receives this and not the workspace		
	}

	@Override
	public void learn(BroadcastContent content) {
		// Not applicable for WorkspaceImpl
	}
	
}
