/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * A task which propagates an amount of activation along a {@link PamLink} to
 * its sink.
 * 
 * @author Ryan J. McCall
 */
public class PropagationTask extends FrameworkTaskImpl {

	/**
	 * Link along which the excitation is being propagated.
	 */
	protected PamLink link;
	/**
	 * The sink of the link.
	 */
	protected PamLinkable sink;
	/**
	 * excitation amount being propagated
	 */
	protected double excitationAmount;
	private PerceptualAssociativeMemory pam;

	/**
	 * Default constructor.
	 * 
	 * @param tpr
	 *            task's ticks per run
	 * 
	 * @param l
	 *            the link from the source to the parent
	 * @param a
	 *            the amount of excitation
	 * @param pam
	 *            the {@link PerceptualAssociativeMemory} module
	 */
	public PropagationTask(int tpr, PamLink l, double a, PerceptualAssociativeMemory pam) {
		super(tpr);
		link = l;
		sink = (PamLinkable)l.getSink();
		excitationAmount = a;
		this.pam = pam;
	}

	/**
	 * Excites the {@link PamLink}'s activation by excitationAmount.
	 * Propagates the excitation along the link and excites the link's sink with the result. 
	 * 
	 */
	@Override
	protected void runThisFrameworkTask() {
		link.exciteActivation(excitationAmount);
		sink.exciteActivation(excitationAmount*link.getBaseLevelActivation());
		runPostExcitation();
	}

	/**
	 * Processes the link and its sink after excitation. Add link and sink to the percept if the sink is over threshold.
	 * Continues the activation propagation beyond the sink by calling {@link PerceptualAssociativeMemory#propagateActivationToParents(PamNode)}
	 * before finishing with a call to {@link #cancel()}.
	 */
	protected void runPostExcitation() {
		if (pam.isOverPerceptThreshold(sink)) {
			AddLinkToPerceptTask task = new AddLinkToPerceptTask(link, pam);
			pam.getAssistingTaskSpawner().addTask(task);
		}
		if (sink instanceof PamNode) {
			pam.propagateActivationToParents((PamNode) sink);
		}
		cancel();
	}
}