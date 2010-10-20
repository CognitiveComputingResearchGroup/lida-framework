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

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;


public class SensoryMotorMemoryImpl extends LidaModuleImpl implements SensoryMotorMemory{

	private Map<Long, LidaAction> actionMap = new HashMap<Long, LidaAction>();
	
	public SensoryMotorMemoryImpl() {
		super(ModuleName.SensoryMotorMemory);
	}

	private List<SensoryMotorListener> listeners = new ArrayList<SensoryMotorListener>();

	@Override
	public void addSensoryMotorListener(SensoryMotorListener l) {
		listeners.add(l);		
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof SensoryMotorListener){
			addSensoryMotorListener((SensoryMotorListener)listener);
		}
	}

	@Override
	public void receiveSensoryMemoryContent(Object content) {
		
	}

	@Override
	public void receiveActionId(long id) {
		//System.out.println("Sensory motor memory receiving " + id);
		executeAction(actionMap.get(id));
	}

	@Override
	public void executeAction(LidaAction action) {
		for(SensoryMotorListener l: listeners)
			l.receiveAction(action);		
	}

	@Override
	public void setActionMap(Map<Long, LidaAction> actionMap) {
		this.actionMap = actionMap;		
	}

	@Override
	public void addAction(long actionId, LidaAction action) {
		actionMap.put(actionId, action);
	}

}
