/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

//TODO consider having multiple NodeStructures as content
public class WorkspaceBufferImpl extends LidaModuleImpl implements WorkspaceBuffer{
	
	private final double DEFAULT_ACTIVATION_LOWER_BOUND = 0.01;

	private double activationLowerBound;
	
	private static final Logger logger = Logger.getLogger(WorkspaceBufferImpl.class.getCanonicalName());
	
	private NodeStructure bufferContent;
	
	public WorkspaceBufferImpl() {
		bufferContent = new NodeStructureImpl();
		activationLowerBound = DEFAULT_ACTIVATION_LOWER_BOUND;
	}

	@Override
	public Object getModuleContent(Object... params) {
		return bufferContent;
	}
		
	/**
	 * decays all the nodes in the buffer.
	 * If a node's activation results lower than lowerActivationBound, it is removed from the buffer.
	 * @param ticks how long since last decay
	 */
	@Override
	public void decayModule(long ticks){
		super.decayModule(ticks);
		Collection<Linkable> linkables = bufferContent.getLinkables();
		for(Linkable linkable: linkables){
			Activatible activatible = (Activatible) linkable;
			activatible.decay(ticks);
			if (activatible.getActivation() <= activationLowerBound){
				logger.log(Level.FINER, "Deleting linkable: " + linkable.getLabel(), LidaTaskManager.getCurrentTick());
				bufferContent.removeLinkable(linkable);
			}
		}		
	}

	@Override
	public void setLowerActivationBound(double activationLowerBound) {
		if(activationLowerBound < 0){
			logger.log(Level.WARNING, "Lower bound must be non-negative.  Parameter not set.", 
					LidaTaskManager.getCurrentTick());
		}else{
			this.activationLowerBound = activationLowerBound;
		}
	}
	
	@Override
	public void addListener(ModuleListener listener) {
		//N/A
	}

	@Override
	public void init() {
	}
}
