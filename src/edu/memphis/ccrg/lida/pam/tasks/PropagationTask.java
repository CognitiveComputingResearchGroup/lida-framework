/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A propagation task excites a node and a link.  
 * The link connects the source of the activation (not accessible) to the node.
 * @author Ryan J McCall
 *
 */
public class PropagationTask extends LidaTaskImpl {
	
	private PamLink link;
	private PamNode source, sink;
	private double excitationAmount;
	
	/**
	 * Used to make another excitation call
	 */
	private PerceptualAssociativeMemory pam;
	
	/**
	 * For threshold task creation
	 */
	private TaskSpawner taskSpawner;

	public PropagationTask(PamNode source, PamLink link, PamNode sink, double amount,
						   PerceptualAssociativeMemory pam, TaskSpawner ts) {
		super();
		this.source = source;
		this.link = link;
		this.sink = sink;
		this.excitationAmount = amount;
		this.pam = pam;
		this.taskSpawner = ts;		
	}

	@Override
	protected void runThisLidaTask() {
		link.excite(excitationAmount);
		sink.excite(excitationAmount);
		pam.sendActivationToParents(sink);
		if(pam.isOverPerceptThreshold(link) && 
		   pam.isOverPerceptThreshold(sink)){
			//If over threshold then spawn a new task to add the node to the percept
			NodeStructure ns = new NodeStructureImpl();
			ns.addNode(source);
			ns.addNode(sink);
			ns.addLink(link);

			AddToPerceptTask task = new AddToPerceptTask(ns, pam);
			taskSpawner.addTask(task);
		}
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}
	
	/**
	 * 
	 */
	public String toString(){
		return "Propagation task " + getTaskId();
	}

}//class
