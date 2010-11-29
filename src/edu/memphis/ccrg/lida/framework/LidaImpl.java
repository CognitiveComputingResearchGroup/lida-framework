/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Basic Lida Implementation.
 * @author Javier Snaider
 *
 */
public class LidaImpl extends LidaModuleImpl implements Lida {
	
	private static Logger logger = Logger.getLogger(LidaImpl.class.getCanonicalName());
	
	private LidaTaskManager taskManager;

	public LidaImpl(LidaTaskManager tm) {
		super(ModuleName.LIDA);
		taskManager=tm;
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#start()
	 */
	@Override
	public void start(){
		taskManager.setDecayingModules(getSubmodules().values());
		logger.log(Level.INFO,"Lida modules have been started\n", 0L);		
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#getTaskManager()
	 */
	@Override
	public LidaTaskManager getTaskManager() {
		return taskManager;
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}
	@Override
	public void addListener(ModuleListener listener) {
	}

}
