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
package edu.memphis.ccrg.lida.framework;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl;


/**
 * @author Javier Snaider
 *
 */
public class AllModuleDriver extends TaskSpawnerImpl {

	
	public AllModuleDriver(LidaTaskManager tm){
		super(tm);
	}

	@Override
	protected void runThisLidaTask() {
	}

	@Override
	public String toString() {
		return "All ModuleType Driver";
	}


}
