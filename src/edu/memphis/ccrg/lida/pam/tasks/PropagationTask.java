/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A propagation task excites a {@link Node} n and a {@link Link} l.  
 * The {@link Link} l is the connection from the source of the activation to the {@link Node} n.
 * @author Ryan J McCall
 *
 */
public class PropagationTask extends FrameworkTaskImpl {
		
	private PamNode source;
	private PamNode sink;
	private PamLink link;
	
	private double excitationAmount;
	
	/*
	 * Used to make another excitation call
	 */
	private PerceptualAssociativeMemory pam;
	
	/*
	 * For threshold task creation
	 */
	private TaskSpawner taskSpawner;

	/**
	 * Propagates specified activation amount from source to sink along link.
	 * 
	 * @param source
	 *            the source of activation
	 * @param link
	 *            the link from the source to the parent
	 * @param sink
	 *            the sink and the parent of the source
	 * @param amount
	 *            the amount to excite
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
	protected void runThisFrameworkTask() {
		//TODO think about a propagation strategy
		link.excite(excitationAmount);
		double linkActivation = link.getActivation();
		sink.excite(linkActivation);
		if(pam.isOverPerceptThreshold(link) && 
		   pam.isOverPerceptThreshold(sink)){
			//If over threshold then spawn a new task to add the node to the percept
			NodeStructure ns = ElementFactory.getInstance().getPamNodeStructure();
			ns.addDefaultNode(source);
			ns.addDefaultNode(sink);
			ns.addDefaultLink(link);

			AddToPerceptTask task = new AddToPerceptTask(ns, pam);
			taskSpawner.addTask(task);
		}
		pam.propagateActivationToParents(sink);
		setTaskStatus(TaskStatus.FINISHED);
	}
	
	@Override
	public String toString(){
		return "Propagation task " + getTaskId();
	}

}
