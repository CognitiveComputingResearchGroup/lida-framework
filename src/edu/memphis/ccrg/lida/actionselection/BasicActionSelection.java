/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 *  which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.ConcurrentHashSet;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

/**
 * Rudimentary action selection that selects the behavior with the highest activation.
 * 
 * @author Ryan J McCall
 * 
 */
public class BasicActionSelection extends FrameworkModuleImpl implements
		ActionSelection, ProceduralMemoryListener {

	private static final Logger logger = Logger
			.getLogger(BasicActionSelection.class.getCanonicalName());

	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
	private Set<Behavior> behaviors = new ConcurrentHashSet<Behavior>();
	private double maxActivationThreshold;
	private DecayStrategy behaviorDecayStrategy;
	
	private static final int DEFAULT_REFRACTORY_PERIOD = 80;
	private int refractoryPeriodTicks;
	
	private static final double DEFAULT_CANDIDATE_THRESHOLD = 0.8;
	private double candidateThreshold;
	
	private static final double DEFAULT_REMOVAL_THRESHOLD = 0.1;
	private double defaultRemovalThreshold;
	
	private static final int DEFAULT_TICKS_PER_RUN = 10;
	private int ticksPerRun;
	
	private static final double DEFAULT_THRESHOLD_DECAY_RATE = 0.1;
	private double thresholdDecayRate;
	
	/**
	 * Default constructor
	 */
	public BasicActionSelection() {
	}

	/**
     * Will set parameters with the following names:</br></br>
     * 
     * actionSelection.refractoryperiodTicks</br>
     * actionSelection.candidateThreshold</br>
     * actionSelection.removalThreshold</br>
     * actionSelection.backgroundTaskTicksPerRun</br>
     * actionSelection.behaviorDecayStrategy</br>
     * actionSelection.thresholdDecayRate
     * 
     * @see edu.memphis.ccrg.lida.framework.FrameworkModuleImpl#init()
     */
	@Override
	public void init() {
		refractoryPeriodTicks = (Integer) getParam(
				"actionSelection.refractoryperiodTicks", DEFAULT_REFRACTORY_PERIOD);
		candidateThreshold = (Double) getParam("actionSelection.candidateThreshold", DEFAULT_CANDIDATE_THRESHOLD);
		maxActivationThreshold = candidateThreshold;

		defaultRemovalThreshold = (Double) getParam("actionSelection.removalThreshold", DEFAULT_REMOVAL_THRESHOLD);

		ticksPerRun = (Integer) getParam("actionSelection.backgroundTaskTicksPerRun", DEFAULT_TICKS_PER_RUN);
		taskSpawner.addTask(new BackgroundTask(ticksPerRun));
		
		ElementFactory factory = ElementFactory.getInstance();
		String decayType = (String)getParam("actionSelection.behaviorDecayStrategy", factory.getDefaultDecayType());
		behaviorDecayStrategy = factory.getDecayStrategy(decayType);
		if(behaviorDecayStrategy == null){
			logger.log(Level.WARNING,"factory doesn't have decay strategy");
			behaviorDecayStrategy = factory.getDefaultDecayStrategy();
		}
		
		thresholdDecayRate = (Double) getParam("actionSelection.thresholdDecayRate", DEFAULT_THRESHOLD_DECAY_RATE);
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ActionSelectionListener) {
			addActionSelectionListener((ActionSelectionListener) listener);
		}else{
			logger.log(Level.WARNING, "Cannot add listener {1}", 
					new Object[]{TaskManager.getCurrentTick(), listener});
		}
	}
	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}
	
	private class BackgroundTask extends FrameworkTaskImpl {
		public BackgroundTask(int ticksPerRun) {
			super(ticksPerRun);
		}
		@Override
		protected void runThisFrameworkTask() {
			if(selectAction() != null){
				setNextTicksPerRun(refractoryPeriodTicks);
				candidateThreshold = maxActivationThreshold;
			}else{
				candidateThreshold -= thresholdDecayRate;
				if(candidateThreshold < 0.0){
					candidateThreshold=0.0;
				}
			}
		}
	}

	@Override
	public void receiveBehavior(Behavior b) {
		if(b != null){
			synchronized(this){	
				b.setDecayStrategy(behaviorDecayStrategy);
				b.setActivatibleRemovalThreshold(defaultRemovalThreshold);
				behaviors.add(b);
			}
			logger.log(Level.FINE, "Behavior added {1}",
					   new Object[]{TaskManager.getCurrentTick(), b});
		}else{
			logger.log(Level.WARNING, "null Behavior can not be added",
					   TaskManager.getCurrentTick());
			
		}
	}

	@Override
	public Action selectAction() {
		Behavior behavior = chooseBehavior();
		if (behavior != null) {
			Action action = behavior.getAction();
			logger.log(Level.FINE, "Action Selected: {1}",
					   new Object[]{TaskManager.getCurrentTick(),action});
			for (ActionSelectionListener bl : listeners) {
				bl.receiveAction(action);
			}
			return action;
		}
		return null;
	}

	private Behavior chooseBehavior() {
		Behavior selected = null;
		double highestActivation = -1.0;
		for (Behavior b : behaviors) {
			double behaviorActivation = b.getActivation();
			if (behaviorActivation >= candidateThreshold && behaviorActivation > highestActivation) {
				selected = b;
				highestActivation = behaviorActivation;
			}
		}
		return selected;
	}
	
	@Override
	public Object getModuleContent(Object... params) {
		if("behaviors".equals(params[0])){
			return Collections.unmodifiableCollection(behaviors);
		}
		return null;
	}

	@Override
	public void addPreafferenceListener(PreafferenceListener listener) {
	}

	@Override
	public void learn(BroadcastContent content) {
	}

	@Override
	public void receiveBroadcast(BroadcastContent bc) {
	}

	@Override
	public void decayModule(long ticks) {
		Iterator<Behavior> it = behaviors.iterator();
		while(it.hasNext()){
			Behavior b = it.next();
			b.decay(ticks);
			if (b.isRemovable()){
				it.remove();
			}
		}
	}

}