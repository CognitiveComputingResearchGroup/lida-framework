/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

/**
 * This class implements GlobalWorkspace and maintains the collection of
 * {@link Coalition}s. It supports {@link BroadcastTrigger}s that are in charge of triggering
 *  the new broadcast. Triggers should implement {@link BroadcastTrigger} interface. This class maintains
 * a list of {@link BroadcastListener}s. These are the modules that need to
 * receive broadcast content.
 * 
 * @author Javier Snaider
 * 
 */
public class GlobalWorkspaceImpl extends LidaModuleImpl implements
		GlobalWorkspace, GuiEventProvider {

	private static final Logger logger = Logger
			.getLogger(GlobalWorkspaceImpl.class.getCanonicalName());
	private static final double LOWER_ACTIVATION_BOUND = 0.0;

	public GlobalWorkspaceImpl() {
		super(ModuleName.GlobalWorkspace);
	}

	private Queue<Coalition> coalitions = new ConcurrentLinkedQueue<Coalition>();
	private List<BroadcastTrigger> broadcastTriggers = new ArrayList<BroadcastTrigger>();
	private List<BroadcastListener> broadcastListeners = new ArrayList<BroadcastListener>();
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private AtomicBoolean broadcastStarted = new AtomicBoolean(false);

	/*
	 * (non-Javadoc)
	 * 
	 * @seelida.globalworkspace.GlobalWorkspace#addBroadcastListener(edu.memphis.
	 * ccrg. globalworkspace.BroadcastListener)
	 */
	@Override
	public void addBroadcastListener(BroadcastListener bl) {
		broadcastListeners.add(bl);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.globalworkspace.GlobalWorkspace#addTrigger(edu.memphis
	 * .ccrg.globalworkspace. Trigger)
	 */
	@Override
	public void addBroadcastTrigger(BroadcastTrigger t) {
		broadcastTriggers.add(t);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.globalworkspace.GlobalWorkspace#putCoalition(edu.memphis
	 * .ccrg.globalworkspace .Coalition)
	 */
	@Override
	public boolean addCoalition(Coalition coalition) {
		if (coalitions.add(coalition)) {
			logger.log(Level.FINE, "New Coalition added",
					LidaTaskManager.getCurrentTick());
			newCoalitionEvent();
			return true;
		} else {
			return false;
		}
	}

	private void newCoalitionEvent() {
		for (BroadcastTrigger trigger : broadcastTriggers)
			trigger.checkForTriggerCondition(coalitions);
	}

	/**
	 * This method realizes the broadcast. First it chooses the winner
	 * coalition. Then, all registered {@link BroadcastListener}s receive a reference
	 * to the coalition content.
	 * The winning Coalition is removed from the pool.
	 * Broadcast recipients must return as soon as possible in order to not 
	 * delay the rest of the broadcasting. A good implementation should copy the broadcast 
	 * content and create a task to process it.
	 * This method is supposed to be called from {@link BroadcastTrigger}s. 
	 * The reset() method is invoked on each trigger at the end of this method.
	 * 
	 */
	@Override
	public void triggerBroadcast() {
		if (broadcastStarted.compareAndSet(false, true)) {
			sendBroadcast();
		}
	}

	private void sendBroadcast() {
		logger.log(Level.FINE, "Triggering broadcast",
				LidaTaskManager.getCurrentTick());
		Coalition coal = chooseCoalition();
		if (coal != null) {
			coalitions.remove(coal);
			NodeStructure copy = ((NodeStructure) coal.getContent()).copy();
			//TODO Create LidaTask for parallel processing 
			for (BroadcastListener bl : broadcastListeners) {
				bl.receiveBroadcast((BroadcastContent) copy);
			}
//			FrameworkGuiEvent ge = new TaskCountEvent(
//					ModuleName.GlobalWorkspace, coalitions.size() + "");
//			sendEventToGui(ge);
		}
		logger.log(Level.FINE, "Broadcast Performed at tick: {0}",
				LidaTaskManager.getCurrentTick());

		resetTriggers();
		broadcastStarted.set(false);
	}

	private Coalition chooseCoalition() {
		Coalition chosenCoal = null;
		for (Coalition c : coalitions) {
			if (chosenCoal == null
					|| c.getActivation() > chosenCoal.getActivation()) {
				chosenCoal = c;
			}
		}// for
		return chosenCoal;
	}

	private void resetTriggers() {
		for (BroadcastTrigger t : broadcastTriggers) {
			t.reset();
		}
	}

	@Override
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	@Override
	public void sendEventToGui(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener fg : guis)
			fg.receiveFrameworkGuiEvent(evt);
	}

	private void decay(long ticks) {
		for (Coalition c : coalitions) {
			c.decay(ticks);
			if (c.getActivation() <= LOWER_ACTIVATION_BOUND) {
				coalitions.remove(c);
				logger.log(Level.FINE, "Coallition removed",
						LidaTaskManager.getCurrentTick());
			}
		}
	}

	@Override
	public Object getModuleContent(Object... params) {
		return Collections.unmodifiableCollection(coalitions);
	}

	@Override
	public void decayModule(long ticks) {
		super.decayModule(ticks);
		decay(ticks);
		logger.log(Level.FINEST, "Coallitions Decayed", LidaTaskManager.getCurrentTick());
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof BroadcastListener) {
			addBroadcastListener((BroadcastListener) listener);
		}
	}

	@Override
	public void init() {
		getAssistingTaskSpawner().addTask(new BackgroundTask());
	}

	private class BackgroundTask extends LidaTaskImpl {
		public BackgroundTask() {
			super(1);
		}

		@Override
		protected void runThisLidaTask() {
			for (BroadcastTrigger t : broadcastTriggers) {
				t.start();
			}
			setTaskStatus(LidaTaskStatus.FINISHED); // Runs only once
		}

		@Override
		public String toString() {
			return GlobalWorkspaceImpl.class.getSimpleName()
					+ " background task";
		}
	}

}// class
