/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
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

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.PamLinkable;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

/**
 * This class implements the FeatureDetector interface and provides default
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
public abstract class BasicDetectionAlgorithm extends FrameworkTaskImpl implements
		DetectionAlgorithm {

	private static final Logger logger = Logger
			.getLogger(BasicDetectionAlgorithm.class.getCanonicalName());
	
	/**
	 * the {@link SensoryMemory}
	 */
	protected SensoryMemory sensoryMemory;
	/**
	 * the {@link PerceptualAssociativeMemory}
	 */
	protected PerceptualAssociativeMemory pam;
	/**
	 * {@link PamLinkable} this algorithm detects
	 */
	protected List<PamLinkable> linkables = new ArrayList<PamLinkable>();	

	/**
	 * Default constructor
	 * 
	 * @param linkable
	 *            {@link PamLinkable} this detector will detect
	 * @param sm
	 *            {@link SensoryMemory}
	 * @param pam
	 *            {@link PerceptualAssociativeMemory}
	 */
	public BasicDetectionAlgorithm(PamLinkable linkable, SensoryMemory sm,
			PerceptualAssociativeMemory pam) {
		super();
		this.pam = pam;
		this.sensoryMemory = sm;
		this.linkables.add(linkable);
	}

	/**
	 * Default constructor.
	 * Associated {@link Linkable}, {@link PerceptualAssociativeMemory} and 
	 * {@link SensoryMemory} must be set using setters.
	 */
	public BasicDetectionAlgorithm(){		
	}
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl#init()
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void init() {
		List<PamLinkable> ids = (List<PamLinkable>) getParam("linkables", null);
		if (ids != null) {
			linkables.addAll(ids);
		}
		PamLinkable linkable =  (PamLinkable) getParam("linkable", null);
		if (linkable != null) {
			linkables.add(linkable);
		}
	}
	
	@Override
	public void setAssociatedModule(FrameworkModule module, String moduleUsage){
		if(module instanceof PerceptualAssociativeMemory){
			pam = (PerceptualAssociativeMemory) module;
		}else if(module instanceof SensoryMemory){
			sensoryMemory = (SensoryMemory) module;
		}else{
			logger.log(Level.WARNING, "Cannot set associated module {1}",
					new Object[]{TaskManager.getCurrentTick(),module});
		}
	}

	@Override
	public void addPamLinkable(PamLinkable linkable) {
		if (linkable != null){
			linkables.add(linkable);
		}
	}

	@Override
	public Collection<PamLinkable> getPamLinkables() {
		return Collections.unmodifiableCollection(linkables);
	}

	@Override
	protected void runThisFrameworkTask() {
		double amount = detect();
		if(logger.isLoggable(Level.FINEST)){
			logger.log(Level.FINEST,"detection performed {1}: {2}",
					new Object[]{TaskManager.getCurrentTick(),this,amount});
		}
		if (amount > 0.0) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST,"Pam excited: {1}"
						,new Object[]{TaskManager.getCurrentTick(),amount});
			}
			excitePam(amount);
		}
	}
	private void excitePam(double amount) {
		for (PamLinkable pn : linkables) {
			pam.receiveActivationBurst(pn, amount);
		}
	}

	/*
	 * Override this method for domain-specific feature detection.
	 * 
	 * @return the double
	 */
	@Override
	public abstract double detect();

	@Override
	public String toString() {
		return "Feature Detector [" + getTaskId() + "] ";
	}

}