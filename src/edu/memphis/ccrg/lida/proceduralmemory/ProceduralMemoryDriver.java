/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * 
 * @author Ryan J McCall, Javier Snaider
 */
public class ProceduralMemoryDriver extends ModuleDriverImpl {

	private ProceduralMemory proceduralMemory;
	private static final int DEFAULT_TICKS_PER_CYCLE = 10;

	public ProceduralMemoryDriver(ProceduralMemory pm, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm,ModuleName.ProceduralDriver);
		proceduralMemory = pm;
	}
	
	public ProceduralMemoryDriver() {
		super(DEFAULT_TICKS_PER_CYCLE,ModuleName.ProceduralDriver);
	}

	/**
	 * 
	 */
	@Override
	public void runThisDriver() {
		proceduralMemory.activateSchemes();
	//	proceduralMemory.decayModule(5);
	}// method

	@Override
	public String toString() {
		return ModuleName.ProceduralDriver + "";
	}

	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof ProceduralMemory
					&& module.getModuleName() == ModuleName.ProceduralMemory) {
				proceduralMemory = (ProceduralMemory) module;
			}
		}
	}
	
}// class