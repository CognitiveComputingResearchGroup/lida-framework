/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.featuredetector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * This class implements the FeatureDetector interface and provides hook default
 * methods. Users should extend this class and overwrite the detect() and
 * excitePam() methods. A convenience init() method is added to initialize the
 * class. This method can be overwritten as well. This implementation is
 * oriented to detect features from sensoryMemory, but the implementation can be
 * used to detect and burstActivation from other modules, like Workspace,
 * emotions or internal states.
 * 
 * @author Ryan McCall - Javier Snaider
 * 
 */
public abstract class FeatureDetectorImpl extends LidaTaskImpl implements
		FeatureDetector {

	private static Logger logger = Logger
			.getLogger("lida.pam.featuredetector.FeatureDetectorImpl");
	protected List<PamNode> pamNodes = new ArrayList<PamNode>();
	protected PerceptualAssociativeMemory pam;
	protected SensoryMemory sm;

	public FeatureDetectorImpl(PamNode n, SensoryMemory sm,
			PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		this.sm = sm;
		this.pamNodes.add(n);
	}

	@SuppressWarnings("unchecked")
	public void init() {
		pam = (PerceptualAssociativeMemory) getParam("PAM", null);
		sm = (SensoryMemory) getParam("SensoryMemory", null);
		List<PamNode> nodes = (List<PamNode>) getParam("PamNodes", null);
		if (nodes != null) {
			pamNodes.addAll(nodes);
		}
		PamNode pn = (PamNode) getParam("PamNode", null);
		if (pn != null) {
			pamNodes.add(pn);
		}
	}

	public void addPamNode(PamNode node) {
		pamNodes.add(node);
	}

	public Collection<PamNode> getPamNodes() {
		return Collections.unmodifiableCollection(pamNodes);
	}

	protected void runThisLidaTask() {
		double amount = detect();
		logger.log(Level.FINE, "detection performed " + toString() + ": "
				+ amount, LidaTaskManager.getActualTick());
		if (amount > 0.0) {
			logger.log(Level.FINE, "Pam excited: " + amount, LidaTaskManager
					.getActualTick());
			excitePam(amount);
		}
	}// method

	/**
	 * Override this method for domain-specific feature detection
	 */
	public abstract double detect();

	public void excitePam(double amount) {
		for (PamNode pn : pamNodes) {
			pam.receiveActivationBurst(pn, amount);
		}
	}

	public String toString() {
		return "Feature Detector [" + getTaskId() + "] ";
	}

}// class