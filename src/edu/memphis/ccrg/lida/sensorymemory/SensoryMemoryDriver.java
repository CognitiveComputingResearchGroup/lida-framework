/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymemory;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class SensoryMemoryDriver extends ModuleDriverImpl {
	
	private SensoryMemory sm;
	
	public SensoryMemoryDriver(SensoryMemory sm, int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm,ModuleName.SensoryMemoryDriver);
		this.sm = sm;
	}//constructor
		
	public SensoryMemoryDriver(){
		super(DEFAULT_TICKS_PER_CYCLE,ModuleName.SensoryMemoryDriver);
	}//constructor
		
	@Override
	public void runThisDriver(){
		sm.runSensors();		
	}

	@Override
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		
	}

	@Override
	public void sendEventToGui(FrameworkGuiEvent evt) {
		
	}

	@Override
	public String toString() {
		return ModuleName.SensoryMemoryDriver + "";
	}
	
	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof SensoryMemory
					&& module.getModuleName() == ModuleName.SensoryMemory) {
				sm =  (SensoryMemory) module;
			}
		}
	}
		
}//class SMDriver