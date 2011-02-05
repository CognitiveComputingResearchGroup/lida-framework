/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;

/**
 * Task which operates a Sensory Memory. This class provides a general way to control various type of
 * sensory memory -- It is the meaning of "background" here.
 * @author Javier Snaider
 */
public class SensoryMemoryBackgroundTask extends LidaTaskImpl {

	private SensoryMemory sm;

	/*
	 * This method overrides runThisLidaTask() from class LidaTaskImpl
	 * It executes method runSensors()of SensoryMemory sm
	 * 
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl#runThisLidaTask()
	 */
	@Override
	protected void runThisLidaTask() {
		//FIXME for NullPointerException
		sm.runSensors();		
	}

	/*
	 * This method overrides setAssociatedModule() from class LidaTaskImpl
	 * It sets a module passing parameter to SensoryMemory sm
	 * 
	 * @param module The module to be associated
	 * @param moduleUsage It is not used here
	 */
	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		if (module instanceof SensoryMemory) {
			sm = (SensoryMemory) module;
		}
	}
	
	@Override
	public String toString(){
		return SensoryMemoryBackgroundTask.class.getSimpleName() + " background task";
	}
}
