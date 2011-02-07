/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.declarativememory;

import edu.memphis.ccrg.lida.episodicmemory.CueListener;
import edu.memphis.ccrg.lida.episodicmemory.EpisodicMemoryImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class DeclarativeMemoryImpl extends EpisodicMemoryImpl implements DeclarativeMemory, CueListener{

    public DeclarativeMemoryImpl() {
    	super();
		setModuleName(ModuleName.DeclarativeMemory);		
	}

	// This method is called continually.  The rate at which is called 
	// can be modified by changing the 'ticksPerCycle' parameter of PerceptualBufferDriver.
	// This is set in the Lida Class.  The higher the value for 'tickPerCycle' the slower
	// the rate of cueing will be.
	@Override
	public synchronized void receiveCue(NodeStructure cue) {
	
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}
	@Override
	public void addListener(ModuleListener listener) {
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}
	
}
