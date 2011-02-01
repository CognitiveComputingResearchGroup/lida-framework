/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeStructure;
import edu.memphis.ccrg.lida.pam.PamNodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A propagation task excites a node and a link.  
 * The link connects the source of the activation to the node.
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

	/**
	 * Instantiates a new propagation task.
	 * 
	 * @param source
	 *            the source
	 * @param link
	 *            the link
	 * @param sink
	 *            the sink
	 * @param amount
	 *            the amount
	 * @param pam
	 *            the pam
	 * @param ts
	 *            the ts
	 */
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
		pam.propagateActivationToParents(sink);
		if(pam.isOverPerceptThreshold(link) && 
		   pam.isOverPerceptThreshold(sink)){
			//If over threshold then spawn a new task to add the node to the percept
			PamNodeStructure ns = new PamNodeStructureImpl();
			ns.addNode(source);
			ns.addNode(sink);
			ns.addLink(link);

			AddToPerceptTask task = new AddToPerceptTask(ns, pam);
			taskSpawner.addTask(task);
		}
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return "Propagation task " + getTaskId();
	}

}