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

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * Basic {@link Agent} Implementation.
 * @author Javier Snaider
 *
 */
public class AgentImpl extends FrameworkModuleImpl implements Agent {
	
	private static final Logger logger = Logger.getLogger(AgentImpl.class.getCanonicalName());
	
	private TaskManager taskManager;

	public AgentImpl(TaskManager tm) {
		super(ModuleName.Agent);
		taskManager=tm;
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#start()
	 */
	@Override
	public void init(){
		taskManager.setDecayingModules(getSubmodules().values());
		logger.log(Level.INFO,"Lida modules have been started\n", 0L);		
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.Lida#getTaskManager()
	 */
	@Override
	public TaskManager getTaskManager() {
		return taskManager;
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}
	@Override
	public void addListener(ModuleListener listener) {
	}
	
	/** 
	 * Should do nothing, submodules' decayModule method is called 
     * in FrameworkModuleImpl#taskManagerDecayModule.
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModule#decayModule(long)
	 */
	@Override
	public void decayModule(long ticks) {
	}

}
