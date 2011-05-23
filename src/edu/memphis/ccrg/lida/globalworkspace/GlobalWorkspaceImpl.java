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

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

/**
 * This class implements GlobalWorkspace and maintains the collection of
 * {@link Coalition}s. It supports {@link BroadcastTrigger}s that are in charge
 * of triggering the new broadcast. Triggers should implement
 * {@link BroadcastTrigger} interface. This class maintains a list of
 * {@link BroadcastListener}s. These are the modules that need to receive
 * broadcast content.
 * 
 * @author Javier Snaider
 * 
 */
public class GlobalWorkspaceImpl extends FrameworkModuleImpl implements
		GlobalWorkspace, GuiEventProvider {

	private static final Logger logger = Logger
			.getLogger(GlobalWorkspaceImpl.class.getCanonicalName());
	private static final double LOWER_ACTIVATION_BOUND = 0.0;

	private double winnerCoalActivation;
	private BroadcastTrigger lastBroadcastTrigger;

	public GlobalWorkspaceImpl() {
	}

	private Queue<Coalition> coalitions = new ConcurrentLinkedQueue<Coalition>();
	private List<BroadcastTrigger> broadcastTriggers = new ArrayList<BroadcastTrigger>();
	private List<BroadcastListener> broadcastListeners = new ArrayList<BroadcastListener>();
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private AtomicBoolean broadcastStarted = new AtomicBoolean(false);

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * lida.globalworkspace.GlobalWorkspace#addBroadcastListener(edu.memphis.
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
			logger.log(Level.FINE, "New Coalition added", TaskManager
					.getCurrentTick());
			newCoalitionEvent();
			return true;
		} else {
			return false;
		}
	}

	private void newCoalitionEvent() {
		for (BroadcastTrigger trigger : broadcastTriggers) {
			trigger.checkForTriggerCondition(coalitions);
		}
	}

	@Override
	public void triggerBroadcast(BroadcastTrigger trigger) {
		if (broadcastStarted.compareAndSet(false, true)) {

			boolean broadcastSent;
			broadcastSent = sendBroadcast();

			if (broadcastSent == true) {
				lastBroadcastTrigger = trigger;
			}
		}
	}

	/*
	 * This method realizes the broadcast. First it chooses the winner
	 * coalition. Then, all registered {@link BroadcastListener}s receive a
	 * reference to the coalition content. The winning Coalition is removed from
	 * the pool. Broadcast recipients must return as soon as possible in order
	 * to not delay the rest of the broadcasting. A good implementation should
	 * copy the broadcast content and create a task to process it. This method
	 * is supposed to be called from {@link BroadcastTrigger}s. The reset()
	 * method is invoked on each trigger at the end of this method.
	 */
	private boolean sendBroadcast() {
		logger.log(Level.FINE, "Triggering broadcast", TaskManager
				.getCurrentTick());
		boolean broadcastWasSent = false;
		Coalition coal = chooseCoalition();
		if (coal != null) {
			winnerCoalActivation = coal.getActivation();
			coalitions.remove(coal);
			NodeStructure copy = ((NodeStructure) coal.getContent()).copy();
			// TODO Create FrameworkTask for parallel processing
			for (BroadcastListener bl : broadcastListeners) {
				bl.receiveBroadcast((BroadcastContent) copy);
			}

			logger.log(Level.FINE, "Broadcast Performed at tick: {0}",
					TaskManager.getCurrentTick());
			broadcastWasSent = true;
		}
		resetTriggers();
		broadcastStarted.set(false);
		return broadcastWasSent;
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
		for (FrameworkGuiEventListener fg : guis) {
			fg.receiveFrameworkGuiEvent(evt);
		}
	}

	@Override
	public Object getModuleContent(Object... params) {
		if (params.length > 0) {
			if (params[0].equals("winnerCoalActivation")) {
				return winnerCoalActivation;
			} else if (params[0].equals("lastBroadcastTrigger")) {
				return lastBroadcastTrigger;
			} else if (params[0].equals("coalitions")) {
				return Collections.unmodifiableCollection(coalitions);
			}
		}
		return Collections.unmodifiableCollection(coalitions);
	}

	@Override
	public void decayModule(long ticks) {
		decay(ticks);
		logger.log(Level.FINEST, "Coallitions Decayed", TaskManager
				.getCurrentTick());
	}
	private void decay(long ticks) {
		for (Coalition c : coalitions) {
			c.decay(ticks);
			if (c.getActivation() <= LOWER_ACTIVATION_BOUND) {
				coalitions.remove(c);
				logger.log(Level.FINE, "Coallition removed", TaskManager
						.getCurrentTick());
			}
		}
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof BroadcastListener) {
			addBroadcastListener((BroadcastListener) listener);
		} else {
			logger.log(Level.WARNING,
					"Can only add listeners of type BroadcastListener. "
							+ "Tried to add " + listener);
		}
	}

	@Override
	public void init() {
		getAssistingTaskSpawner().addTask(new BackgroundTask());
	}

	private class BackgroundTask extends FrameworkTaskImpl {
		public BackgroundTask() {
			super(1);
		}

		@Override
		protected void runThisFrameworkTask() {
			for (BroadcastTrigger t : broadcastTriggers) {
				t.start();
			}
			setTaskStatus(TaskStatus.FINISHED); // Runs only once
		}

		@Override
		public String toString() {
			return GlobalWorkspaceImpl.class.getSimpleName()
					+ " background task";
		}
	}

}
