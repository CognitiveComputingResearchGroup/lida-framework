/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymemory;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;

public abstract class SensoryMemoryImpl extends LidaModuleImpl implements SensoryMemory {

	private List<SensoryMemoryListener> listeners = new ArrayList<SensoryMemoryListener>();
	protected Environment environment;
	protected Object sensoryMemoryContent;

	public SensoryMemoryImpl() {
		super(ModuleName.SensoryMemory);
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof SensoryMemoryListener) {
			addSensoryMemoryListener((SensoryMemoryListener) listener);
		}
	}
	
	@Override
	public void addSensoryMemoryListener(SensoryMemoryListener l) {
		listeners.add(l);
	}
	
	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		if (module != null) {
			if (module instanceof Environment && 
				module.getModuleName() == ModuleName.Environment) {
				environment = (Environment) module;
			}
		}
	}//
	
	@Override
	public void receiveExecutingAlgorithm(Object alg){
		//What to do here is a research question.
	}

	//TODO what is this set state garbage? can we delete it all?
	@Override
	public boolean setState(Object state) {
        try {
            sensoryMemoryContent = state;
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
	public Object getState() {
        return sensoryMemoryContent;
    }
}// class
