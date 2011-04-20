/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A task to add a {@link PamNode}, {@link PamLink}, or {@link NodeStructure} to the percept.
 * @author Ryan J McCall
 * @see ExcitationTask AddToPerceptTask is spawned by ExcitationTask
 */
public class AddToPerceptTask extends FrameworkTaskImpl {
	
	private NodeStructure nodeStructure;
	private PerceptualAssociativeMemory pam;

	/**
	 * Creates a new AddToPerceptTask to add a single {@link PamNode}
	 * 
	 * @param pamNode
	 *            a {@link PamNode}
	 * @param pam
	 *            the {@link PerceptualAssociativeMemory}
	 */
	public AddToPerceptTask(PamNode pamNode, PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		nodeStructure = ElementFactory.getInstance().getPamNodeStructure();
		nodeStructure.addDefaultNode(pamNode);
	}
	
	/**
	 * Creates a new AddToPerceptTask to add a collection of {@link PamNode}
	 * @param nodes to be added
	 * @param pam {@link PerceptualAssociativeMemory}
	 */
	public AddToPerceptTask(Collection<Node> nodes, PerceptualAssociativeMemory pam){
		super();
		this.pam = pam;
		nodeStructure = ElementFactory.getInstance().getPamNodeStructure();
		nodeStructure.addDefaultNodes(nodes);
	}

	/**
	 * Creates a new AddToPerceptTask to add a {@link NodeStructure}
	 * 
	 * @param ns
	 *            a {@link NodeStructure}
	 * @param pam
	 *            the {@link PerceptualAssociativeMemory}
	 */
	public AddToPerceptTask(NodeStructure ns, PerceptualAssociativeMemory pam){
		super();
		this.pam = pam;
		this.nodeStructure = ns.copy();
	}

	/**
	 * While it looks simple, the call to 'addNodeToPercept' takes many step to execute.
	 * Thus it is justifiable to make this a separate thread
	 */
	@Override
	protected void runThisFrameworkTask() {		
		pam.addNodeStructureToPercept(nodeStructure);	
		setTaskStatus(TaskStatus.FINISHED);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return AddToPerceptTask.class.getSimpleName() + " " + getTaskId();
	}

}

