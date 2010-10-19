/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class MockActionSelectionImpl implements ActionSelection {
	private static Logger logger = Logger.getLogger("lida.actionselection.ActionSelectionImpl");

	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		

	}

	@Override
	public void selectAction() {
		System.out.println("action selected at tick: " + LidaTaskManager.getActualTick());
		logger.log(Level.FINE,"Action Selection Performed at tick: {0}",LidaTaskManager.getActualTick());
	}

	@Override
	public void setTaskSpawner(TaskSpawner taskSpawner) {
		

	}

	@Override
	public void triggerActionSelection() {
		
		selectAction();
	}

	@Override
	public Object getState() {
		
		return null;
	}

	@Override
	public boolean setState(Object content) {
		
		return false;
	}

	@Override
	public void addPreafferenceListener(PreafferenceListener listener) {
		
		
	}

	@Override
	public void addListener(ModuleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void addSubModule(LidaModule lm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void decayModule(long ticks) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModuleName getModuleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LidaModule getSubmodule(ModuleName name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setModuleName(ModuleName moduleName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(Map<String, ?> lidaProperties) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setAssociatedModule(LidaModule module) {
		// TODO Auto-generated method stub
		
	}


}
