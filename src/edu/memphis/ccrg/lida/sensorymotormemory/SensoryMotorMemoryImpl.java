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

import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;


public class SensoryMotorMemoryImpl extends LidaModuleImpl implements SensoryMotorMemory{

	private static final Logger logger = Logger.getLogger(SensoryMotorMemoryImpl.class.getCanonicalName());
	private Map<Long, Object> actionMap = new HashMap<Long, Object>();
	private Environment environment;
	
	public SensoryMotorMemoryImpl() {
		super(ModuleName.SensoryMotorMemory);
	}

	private List<SensoryMotorListener> listeners = new ArrayList<SensoryMotorListener>();

	@Override
	public void addSensoryMotorListener(SensoryMotorListener l) {
		if(l instanceof Environment){
			logger.log(Level.SEVERE, "Cannot Add Environment as SensoryMotorListener.  Add it as an associated module.", LidaTaskManager.getActualTick());			
		}else{
			listeners.add(l);
		}
	}
	
	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		if(module instanceof Environment){
			environment = (Environment) module;
		}
			
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
		executeAction(actionMap.get(id));
	}

	@Override
	public void executeAction(Object action) {
		environment.processAction(action);
		for(SensoryMotorListener l: listeners)
			l.receiveExecutingAlgorithm(action);		
	}

	@Override
	public void setActionMap(Map<Long, Object> actionMap) {
		this.actionMap = actionMap;		
	}

	@Override
	public void addAction(long actionId, Object action) {
		actionMap.put(actionId, action);
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
