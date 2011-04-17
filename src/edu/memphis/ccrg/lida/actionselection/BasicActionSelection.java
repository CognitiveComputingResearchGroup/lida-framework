/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

/**
 * Rudimentary action selection that selects the behavior with the highest activation.
 * 
 * @author Ryan J McCall
 * 
 */
public class BasicActionSelection extends LidaModuleImpl implements
		ActionSelection, ProceduralMemoryListener {

	private static final Logger logger = Logger
			.getLogger(BasicActionSelection.class.getCanonicalName());

//	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
	private Queue<Behavior> behaviors = new LinkedList<Behavior>();
	
	/**
	 * Default constructor
	 */
	public BasicActionSelection() {
	}

	@Override
	public void init() {
		int ticksPerRun = (Integer) getParam(
				"actionSelection.backgroundTaskTicksPerRun", 10);
		taskSpawner.addTask(new BackgroundTask(ticksPerRun));
	}

	// TODO move to xml as initial task
	private class BackgroundTask extends LidaTaskImpl {
		public BackgroundTask(int ticksPerRun) {
			super(ticksPerRun);
		}
		@Override
		protected void runThisLidaTask() {
			selectAction();
		}
		@Override
		public String toString() {
			return BasicActionSelection.class.getSimpleName()
					+ " background task";
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
		synchronized(this){
			behaviors.add(b);
		}
		logger.log(Level.FINE, "Behavior added " + b,
				   LidaTaskManager.getCurrentTick());
	}

	@Override
	public void selectAction() {
		Behavior behavior = chooseBehavior();
		if (behavior != null) {
			LidaAction action = behavior.getAction();
			logger.log(Level.FINE, "Action Selected at tick: " + LidaTaskManager.getCurrentTick() + " act: " + action,
					LidaTaskManager.getCurrentTick());
			for (ActionSelectionListener bl : listeners) {
				bl.receiveAction(action);
			}
		}
	}

	private Behavior chooseBehavior() {
		Behavior selected = behaviors.peek();
		for (Behavior b : behaviors) {
			if (b.getActivation() > selected.getActivation()) {
				selected = b;
			}
		}
		synchronized(this){
			behaviors.clear();
		}
		return selected;
	}
	
	@Override
	public Object getModuleContent(Object... params) {
		if(params[0].equals("behaviors")){
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
					this.behaviors = (Queue<Behavior>) state[0];
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
		// TODO Auto-generated method stub
	}

	@Override
	public void learn(BroadcastContent content) {
	}

	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		// TODO
	}

	@Override
	public void decayModule(long ticks) {
		super.decayModule(ticks);
		// TODO decay behaviors
	}

}