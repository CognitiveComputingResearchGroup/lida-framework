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
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Activatible;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class WorkspaceBufferImpl extends LidaModuleImpl implements WorkspaceBuffer{
	
	private double activationLowerBound = 0.01;
	
	private static Logger logger = Logger.getLogger("lida.workspace.workspaceBuffer.WorkspaceBufferImpl");
	
	public WorkspaceBufferImpl(ModuleName lidaModule) {
		super(lidaModule);
	}
	public WorkspaceBufferImpl() {
	}

	private NodeStructure buffer = new NodeStructureImpl();

	/**
	 * @return the buffer
	 */

	public NodeStructure getModuleContent() {
		return buffer;
	}
		
	/**
	 * decays all the nodes in the buffer.
	 * If a node's activation results lower than lowerActivationBound, it is removed from the buffer.
	 * 
	 * @param ticks
	 */
	public void decayModule(long ticks){
		Collection<Linkable> linkables = buffer.getLinkables();
		for(Linkable linkable: linkables){
			Activatible activatible = (Activatible) linkable;
			activatible.decay(ticks);
			if (activatible.getActivation() <= activationLowerBound){
//				System.out.println("Deleting linkable: " + linkable.getLabel());
//				if(linkable instanceof Link){
//					Link l = (Link) linkable;
//					System.out.println("deleting link " + l.getSource().getLabel() + " -> " + l.getSink().getLabel());
//				}
				logger.log(Level.FINER, "Deleting linkable: " + linkable.getLabel(), LidaTaskManager.getActualTick());
				buffer.deleteLinkable(linkable);
			}
		}		
	}//method

	public void setLowerActivationBound(double lowerActivationBound) {
		this.activationLowerBound=lowerActivationBound;		
	}
	public void addListener(ModuleListener listener) {
	}
}
