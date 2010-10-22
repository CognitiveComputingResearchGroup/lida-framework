/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Basic Lida Implementation.
 * @author Javier Snaider
 *
 */
public class LidaImpl extends LidaModuleImpl implements Lida {
	private static Logger logger = Logger.getLogger("lida.framework.Lida");
	private LidaTaskManager taskManager;
	private Map<ModuleName, ModuleDriver> drivers = new ConcurrentHashMap<ModuleName, ModuleDriver>();

	public LidaImpl(LidaTaskManager tm) {
		super(ModuleName.LIDA);
		taskManager=tm;
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#getTaskManager()
	 */
	public LidaTaskManager getTaskManager() {
		return taskManager;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#addModuleDriver(edu.memphis.ccrg.lida.framework.ModuleDriver)
	 */
	public void addModuleDriver(ModuleDriver driver) {
		System.out.println(driver);
		drivers.put(driver.getModuleName(), driver);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#getModuleDriver(edu.memphis.ccrg.lida.framework.ModuleName)
	 */
	public ModuleDriver getModuleDriver(ModuleName name) {
		return drivers.get(name);
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#start()
	 */
	public void start(){
		taskManager.setDecayingModules(getSubmodules().values());
		taskManager.getMainTaskSpawner().setInitialTasks(drivers.values());
		logger.log(Level.INFO,"Lida modules have been started\n", 0L);		
	}

	public void addListener(ModuleListener listener) {
	}

}
