/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymotormemory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemoryListener;

/**
 * Default implementation of a Map-based SensoryMotorMemory
 * 
 * @author Ryan J. McCall
 * 
 */
public class BasicSensoryMotorMemory extends LidaModuleImpl implements
		SensoryMotorMemory, SensoryMemoryListener, ActionSelectionListener {

	private static final Logger logger = Logger
			.getLogger(BasicSensoryMotorMemory.class.getCanonicalName());

	private List<SensoryMotorMemoryListener> listeners = new ArrayList<SensoryMotorMemoryListener>();
	private Map<Long, Object> actionAlgorithmMap = new HashMap<Long, Object>();
	private Environment environment;

	public BasicSensoryMotorMemory() {
		super(ModuleName.SensoryMotorMemory);
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof SensoryMotorMemoryListener) {
			addSensoryMotorMemoryListener((SensoryMotorMemoryListener) listener);
		} else {
			logger.log(Level.WARNING, "Cannot add listener "
					+ listener.toString(), LidaTaskManager.getCurrentTick());
		}
	}

	@Override
	public void addSensoryMotorMemoryListener(SensoryMotorMemoryListener l) {
		if (l instanceof Environment) {
			logger
					.log(
							Level.SEVERE,
							"Cannot add Environment as SensoryMotorMemoryListener.  Add it as an associated module.",
							LidaTaskManager.getCurrentTick());
		} else {
			listeners.add(l);
		}
	}

	@Override
	public void setAssociatedModule(LidaModule module, String moduleUsage) {
		if (module instanceof Environment) {
			environment = (Environment) module;
		} else {
			logger.log(Level.WARNING, "Cannot add module "
					+ module.getModuleName(), LidaTaskManager.getCurrentTick());
		}
	}
	
	public void setLidaActionAlgorithmMap(Map<Long, Object> actionMap) {
		this.actionAlgorithmMap = actionMap;
	}

	public void addActionAlgorithm(long actionId, Object action) {
		actionAlgorithmMap.put(actionId, action);
	}

	@Override
	public synchronized void receiveAction(LidaAction action) {
		ProcessActionTask t = new ProcessActionTask(action);
		taskSpawner.addTask(t);
	}
	private class ProcessActionTask extends LidaTaskImpl {
		private LidaAction action;
		public ProcessActionTask(LidaAction a) {
			action = a;
		}
		@Override
		protected void runThisLidaTask() {
			Long id = (Long) action.getContent();
			Object alg = actionAlgorithmMap.get(id);
			sendActuatorCommand(alg);
		}
	}

	@Override
	public void sendActuatorCommand(Object command) {
		environment.processAction(command);
		for (SensoryMotorMemoryListener l : listeners) {
			l.receiveActuatorCommand(command);
		}
	}

	@Override
	public void decayModule(long ticks) {
		super.decayModule(ticks);
		// Your module specific decay code. should call super first.
	}
	

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void receiveSensoryMemoryContent(Object content) {
		// Research problem
	}

	@Override
	public void init() {
	}

	@Override
	public Object getState() {
		// TODO DAO
		return null;
	}

	@Override
	public boolean setState(Object content) {
		// TODO DAO
		return false;
	}

}
