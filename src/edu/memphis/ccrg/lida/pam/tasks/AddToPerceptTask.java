/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeStructure;
import edu.memphis.ccrg.lida.pam.PamNodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A task to add a {@link PamNode}, {@link PamLink}, or {@link NodeStructure} to the percept.
 * @author Ryan J McCall
 * @see ExcitationTask AddToPerceptTask is spawned by ExcitationTask
 */
public class AddToPerceptTask extends LidaTaskImpl {
	
	private NodeStructure nodeStructure;
	private PerceptualAssociativeMemory pam;

	/**
	 * Instantiates a new adds to percept task.
	 * 
	 * @param pamNode
	 *            the pam node
	 * @param pam
	 *            the pam
	 */
	public AddToPerceptTask(PamNode pamNode, PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		nodeStructure = new PamNodeStructureImpl();
		nodeStructure.addNode(pamNode);
	}
	
	/**
	 * Instantiates a new adds to percept task.
	 * 
	 * @param pamLink
	 *            the pam link
	 * @param pam
	 *            the pam
	 */
	public AddToPerceptTask(PamLink pamLink, PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		nodeStructure = new PamNodeStructureImpl();
		nodeStructure.addLink(pamLink);
	}

	/**
	 * Instantiates a new adds to percept task.
	 * 
	 * @param pamNodeStructure
	 *            the pam node structure
	 * @param pam
	 *            the pam
	 */
	public AddToPerceptTask(PamNodeStructure pamNodeStructure, PerceptualAssociativeMemory pam) {

		super();
		this.pam = pam;
		nodeStructure = new PamNodeStructureImpl(pamNodeStructure);
	}

	/**
	 * While it looks simple, the call to 'addNodeToPercept' takes many step to execute.
	 * Thus it is justifiable to make this a separate thread
	 */
	@Override
	public void runThisLidaTask() {				
		pam.addNodeStructureToPercept(nodeStructure);	
		setTaskStatus(LidaTaskStatus.FINISHED);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString(){
		return "AddToPerceptTask " + getTaskId();
	}

}

