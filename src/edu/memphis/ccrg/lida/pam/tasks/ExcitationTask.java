/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A task that allows PAM nodes to be excited asynchronously.
 * Created in PAM method 'receiveActivationBurst' 
 * 
 * @see PerceptualAssociativeMemory#receiveActivationBurst(PamLinkable, double)
 * @author Ryan J McCall
 *
 */
public class ExcitationTask extends FrameworkTaskImpl{
	
	private static final Logger logger = Logger.getLogger(ExcitationTask.class.getCanonicalName());
	
	/*
	 * PamNode to be excited
	 */
	private PamNode pamNode;
	
	/*
	 * Amount to excite
	 */
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
	 * Instantiates a new excitation task to excite supplied {@link PamLinkable} specified amount.
	 * 
	 * @param n
	 *            to be excited
	 * @param excitation
	 *            amount to excite
	 * @param ticksPerRun
	 *            the ticks per run
	 * @param pam
	 *            PerceptualAssociativeMemory module
	 * @param ts
	 *            the ts
	 */
	public ExcitationTask(PamNode n, double excitation, int ticksPerRun,
			              PerceptualAssociativeMemory pam, TaskSpawner ts){
		super();
		this.pamNode = n;
		this.excitationAmount = excitation;
		this.setTicksPerStep(ticksPerRun);
		this.pam = pam;
		this.taskSpawner = ts;
	}

	/*
	 * Excites the pamlinkable.  
	 * If this puts it over threshold then immediately spawn an {@link AddToPerceptTask}.
	 * Propagate activation to parents.
	 */
	@Override
	protected void runThisFrameworkTask() {
		pamNode.excite(excitationAmount); 
		if(pam.isOverPerceptThreshold(pamNode)){
			logger.log(Level.FINER, "PamNode " + pamNode.toString() + " over threshold", TaskManager.getCurrentTick());
			taskSpawner.addTask(new AddToPerceptTask(pamNode, pam));		
		}
		pam.propagateActivationToParents(pamNode);
		setTaskStatus(TaskStatus.FINISHED);
	}

	@Override
	public String toString(){
		return "Excitation " + getTaskId();
	}

}