/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
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

	private int ticksPerRun;
	private int refractoryPeriodTicks;
	
	private double activationThreshold;
	private double maxActivationThreshold;

	private double defaultRemovableThreshold;
	
	/**
	 * Default constructor
	 */
	public BasicActionSelection() {
	}

	@Override
	public void init() {
		ticksPerRun = (Integer) getParam(
				"actionSelection.backgroundTaskTicksPerRun", 10);
		refractoryPeriodTicks = (Integer) getParam(
				"actionSelection.refractoryperiodTicks", 80);
		activationThreshold = (Double) getParam(
				"actionSelection.activationThreshold", 0.0);
		maxActivationThreshold = activationThreshold;

		defaultRemovableThreshold = (Double) getParam(
				"actionSelection.activationThreshold", 0.1);

		taskSpawner.addTask(new BackgroundTask(ticksPerRun));
	}

	// TODO move to xml as initial task
	private class BackgroundTask extends FrameworkTaskImpl {
		public BackgroundTask(int ticksPerRun) {
			super(ticksPerRun);
		}
		@Override
		protected void runThisFrameworkTask() {
			if(selectAction()!=null){
				setNextTicksPerRun(refractoryPeriodTicks);
				activationThreshold = maxActivationThreshold;
			}else{
				activationThreshold -= .1;
				if(activationThreshold<0.0){
					activationThreshold=0.0;
				}
			}
		}
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ActionSelectionListener) {
			addActionSelectionListener((ActionSelectionListener) listener);
		}
	}
	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void receiveBehavior(Behavior b) {
		if(b!=null){
		synchronized(this){	
			b.setActivatibleRemovalThreshold(defaultRemovableThreshold);
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
	public AgentAction selectAction() {
		Behavior behavior = chooseBehavior();
		if (behavior != null) {
			AgentAction action = behavior.getAction();
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
		double activation = -1.0;
		for (Behavior b : behaviors) {
			double behaviorActivation = b.getActivation();
			if (behaviorActivation >= activationThreshold && behaviorActivation > activation) {
				selected = b;
				activation = behaviorActivation;
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
	public Object getState() {
		Object[] state = new Object[4];
		state[0] = this.behaviors;
		state[1] = null;
		state[2] = null;
		state[3] = null;
		return state;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean setState(Object content) {
		if (content instanceof Object[]) {
			Object[] state = (Object[]) content;
			if (state.length == 4) {
				try {
					this.behaviors = (Set<Behavior>) state[0];
					return true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
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