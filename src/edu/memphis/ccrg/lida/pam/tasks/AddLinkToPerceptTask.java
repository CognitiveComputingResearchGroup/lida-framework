/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A task to add a {@link PamLink} and its sink to the percept.
 * @author Ryan J McCall
 * @see ExcitationTask creates this task
 * @see PropagationTask creates this task
 */
public class AddLinkToPerceptTask extends FrameworkTaskImpl {
	
	private PerceptualAssociativeMemory pam;
	private PamLink link;

	/**
	 * Default constructor
	 * @param link {@link PamLink}
	 * @param pam {@link PerceptualAssociativeMemory}
	 */
	public AddLinkToPerceptTask(PamLink link, PerceptualAssociativeMemory pam) {
		this.pam = pam;
		this.link = link;
	}

	/**
	 * Adds link's sink to the percept and tries to add the link as well then finishes.
	 */
	@Override
	protected void runThisFrameworkTask() {		
		pam.addNodeToPercept((Node) link.getSink());
		pam.addLinkToPercept(link);
		setTaskStatus(TaskStatus.FINISHED);
	}
	
}

