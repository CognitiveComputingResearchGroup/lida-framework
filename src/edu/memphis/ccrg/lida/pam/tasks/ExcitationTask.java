/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;

/**
 * A task that allows PAM nodes to be excited asynchronously.
 * Created in PAM method 'receiveActivationBurst'
 * 
 * @author Ryan J McCall
 *
 */
public class ExcitationTask extends LidaTaskImpl{
	
	private static Logger logger = Logger.getLogger(ExcitationTask.class.getCanonicalName());
	
	/**
	 * PamNode to be excited
	 */
	private PamLinkable pamLinkable;
	
	/**
	 * Amount to excite
	 */
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
	 * 
	 * @param linkable to be excited
	 * @param excitation amount to excite
	 * @param pam PerceptualAssociativeMemory module
	 */
	public ExcitationTask(PamLinkable linkable, double excitation, int ticksPerRun,
			              PerceptualAssociativeMemory pam, TaskSpawner ts){
		super();
		this.pamLinkable = linkable;
		this.excitationAmount = excitation;
		this.setNumberOfTicksPerRun(ticksPerRun);
		this.pam = pam;
		this.taskSpawner = ts;
	}

	/**
	 * 
	 */
	protected void runThisLidaTask() {
		pamLinkable.excite(excitationAmount); 
		if(pam.isOverPerceptThreshold(pamLinkable)){
			//If over threshold then spawn a new task to add the node to the percept
			AddToPerceptTask task;
			
			if(pamLinkable instanceof PamNode){
				PamNode pamNode = (PamNode) pamLinkable;
				task = new AddToPerceptTask(pamNode, pam);
				taskSpawner.addTask(task);
				//Tell PAM to propagate the activation of pamNode to its parents
				pam.sendActivationToParents(pamNode);
			}else if(pamLinkable instanceof PamLink){
				task = new AddToPerceptTask((PamLink) pamLinkable, pam);
				taskSpawner.addTask(task);
			}else{
				logger.log(Level.WARNING, "pam linkable is not a PamNode or PamLink", LidaTaskManager.getActualTick());
			}
		}else if(pamLinkable instanceof PamNode){
			//TODO what if its an instanceof PamLink?
			pam.sendActivationToParents((PamNode) pamLinkable);
		}
		
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}//method

	/**
	 * 
	 */
	public String toString(){
		return "Excitation " + getTaskId();
	}

}//class