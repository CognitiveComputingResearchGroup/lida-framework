/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam.tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
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
 * @author Ryan J. McCall - Javier Snaider
 * 
 */
public abstract class FeatureDetectorImpl extends LidaTaskImpl implements
		FeatureDetector {

	private static final Logger logger = Logger
			.getLogger(FeatureDetectorImpl.class.getCanonicalName());
	protected List<ExtendedId> linkableIds = new ArrayList<ExtendedId>();
	protected PerceptualAssociativeMemory pam;
	protected SensoryMemory sm;

	/**
	 * Instantiates a new feature detector impl.
	 * 
	 * @param eid
	 *            the n
	 * @param sm
	 *            the sm
	 * @param pam
	 *            the pam
	 */
	public FeatureDetectorImpl(ExtendedId eid, SensoryMemory sm,
			PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		this.sm = sm;
		this.linkableIds.add(eid);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl#init()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void init() {
		List<ExtendedId> ids = (List<ExtendedId>) getParam("linkables", null);
		if (ids != null) {
			linkableIds.addAll(ids);
		}
		ExtendedId id =  (ExtendedId) getParam("id", null);
		if (id != null) {
			linkableIds.add(id);
		}
	}
	
	@Override
	public void setAssociatedModule(LidaModule module, String moduleUsage){
		if(module instanceof PerceptualAssociativeMemory){
			pam = (PerceptualAssociativeMemory) module;
		}else if(module instanceof SensoryMemory){
			sm = (SensoryMemory) module;
		}else{
			logger.log(Level.WARNING, "Cannot set associated module " + module, LidaTaskManager.getCurrentTick());
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.tasks.FeatureDetector#addPamNode(edu.memphis.ccrg.lida.pam.PamNode)
	 */
	@Override
	public void addPamLinkable(ExtendedId eid) {
		linkableIds.add(eid);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.tasks.FeatureDetector#getPamNodes()
	 */
	@Override
	public Collection<ExtendedId> getPamLinkables() {
		return Collections.unmodifiableCollection(linkableIds);
	}

	@Override
	protected void runThisLidaTask() {
		double amount = detect();
		logger.log(Level.FINE, "detection performed " + toString() + ": "
				+ amount, LidaTaskManager.getCurrentTick());
		if (amount > 0.0) {
			logger.log(Level.FINE, "Pam excited: " + amount, LidaTaskManager
					.getCurrentTick());
			excitePam(amount);
		}
	}

	/*
	 * Override this method for domain-specific feature detection.
	 * 
	 * @return the double
	 */
	@Override
	public abstract double detect();

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.pam.tasks.FeatureDetector#excitePam(double)
	 */
	@Override
	public void excitePam(double amount) {
		for (ExtendedId pn : linkableIds) {
			pam.receiveActivationBurst(pn, amount);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Feature Detector [" + getTaskId() + "] ";
	}

}