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

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.gui.events.GuiEventProvider;
import edu.memphis.ccrg.lida.framework.gui.events.TaskCountEvent;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

/**
 * This class implements GlobalWorkspace and maintains the collection of
 * Coalitions. It supports triggers that are in charge to trigger the new
 * broadcast. Triggers should implement Trigger interface. This class maintains
 * a list of broadcastListeners. These are the modules (classes) that need to
 * receive broadcast content.
 * 
 * @author Javier Snaider
 * 
 */
public class GlobalWorkspaceImpl extends LidaModuleImpl implements GlobalWorkspace,
											GuiEventProvider {
	
	private static Logger logger = Logger.getLogger("lida.globalworkspace.GlobalWorkspaceImpl");

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
	public void addBroadcastTrigger(BroadcastTrigger t) {
		broadcastTriggers.add(t);
	}

	public void start() {
		for (BroadcastTrigger t : broadcastTriggers) {
			t.start();
		}

	}// method

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.globalworkspace.GlobalWorkspace#putCoalition(edu.memphis
	 * .ccrg.globalworkspace .Coalition)
	 */
	public boolean addCoalition(Coalition coalition) {
		if (coalitions.add(coalition)) {
			logger.log(Level.FINE,"New Coalition added",LidaTaskManager.getActualTick());
			newCoalitionEvent();
			return true;
		} else {
			return false;
		}
	}// method

	private void newCoalitionEvent() {
		for (BroadcastTrigger trigger : broadcastTriggers)
			trigger.checkForTrigger(coalitions);
	}// method

	/**
	 * This method realizes the broadcast. First it chooses the winner
	 * coalition. Then, all registered BroadcastListeners receive a reference
	 * SEEEEE to the coalition content.
	 * 
	 * this method is supposed to be called from Triggers. All triggers are
	 * reseted, reset() method is invoked on each of them. The coalition pool is
	 * cleared.
	 * 
	 */
	public void triggerBroadcast() {
		if (broadcastStarted.compareAndSet(false, true)) {
			sendBroadcast();
		}
	}// method

	private void sendBroadcast() {
		logger.log(Level.FINE, "Triggering broadcast", LidaTaskManager.getActualTick());
		Coalition coal = chooseCoalition();
		if (coal != null) {
			coalitions.remove(coal);
		}
		
		if (coal != null) {
			NodeStructure copy = new NodeStructureImpl((NodeStructure) coal
					.getContent());
			for (BroadcastListener bl : broadcastListeners) {
				bl.receiveBroadcast((BroadcastContent) copy);
			}
			FrameworkGuiEvent ge = new TaskCountEvent(ModuleName.GlobalWorkspace, coalitions.size()+"");
			sendEventToGui(ge);
		} 
		logger.log(Level.FINE,"Broadcast Performed at tick: {0}",LidaTaskManager.getActualTick());

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
	}// method

	private void resetTriggers() {
		for (BroadcastTrigger t : broadcastTriggers) {
			t.reset();
		}
	}

	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}

	public void sendEventToGui(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener fg : guis)
			fg.receiveFrameworkGuiEvent(evt);
	}


	public void decay(long ticks){
		for (Coalition c : coalitions) {
			c.decay(ticks);
			if (c.getActivation()<=0.0){
				coalitions.remove(c);
				logger.log(Level.FINE,"Coallition removed",LidaTaskManager.getActualTick());
			}
	}
	}

	@Override
	public Object getModuleContent(Object... params) {
		return Collections.unmodifiableCollection(coalitions);
	}

	public LidaModule getSubmodule(ModuleName type) {
		return null;
	}

	public void decayModule(long ticks){
		decay(ticks);
		logger.log(Level.FINEST,"Coallitions Decayed",LidaTaskManager.getActualTick());
	}

	public void addSubModule(LidaModule lm) {
	}

	public void addListener(ModuleListener listener) {
		if (listener instanceof BroadcastListener){
			addBroadcastListener((BroadcastListener)listener);
		}
	}
	public void init(){
		getAssistingTaskSpawner().addTask(new BackgroundTask());
	}
	
	private class BackgroundTask extends LidaTaskImpl {

		public BackgroundTask() {
			super(1);
		}

		@Override
		protected void runThisLidaTask() {
			start();
			setTaskStatus(LidaTaskStatus.FINISHED); //Runs only once
		}
	}

}// class
