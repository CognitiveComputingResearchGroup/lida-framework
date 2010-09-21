/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

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
						   PerceptualAssociativeMemory pam, TaskSpawner taskSpawner) {
		super();
		this.source = source;
		this.link = link;
		this.sink = sink;
		this.excitationAmount = amount;
		this.pam = pam;
		this.taskSpawner = taskSpawner;		
	}

	@Override
	protected void runThisLidaTask() {
//		System.out.println("\npropag task " + link.getTotalActivation() + " current " + link.getActivation());
		link.excite(excitationAmount);
//		System.out.println("propag task " + link.getTotalActivation() + " current " + link.getActivation());
		
		sink.excite(excitationAmount);
		//
		pam.sendActivationToParents(sink);
		if(link.isOverThreshold() && sink.isOverThreshold()){
			//If over threshold then spawn a new task to add the node to the percept
			NodeStructure ns = new NodeStructureImpl();
			ns.addNode(source);
			ns.addNode(sink);
			ns.addLink(link);
//			for(Link l: ns.getLinks())
//				System.out.println("propag task " + l.getTotalActivation() + " current " + link.getActivation());
			
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
