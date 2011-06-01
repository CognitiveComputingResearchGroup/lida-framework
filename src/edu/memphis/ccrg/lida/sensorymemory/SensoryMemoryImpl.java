/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 *  which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.sensorymemory;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.tasks.DetectionAlgorithm;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemory;

/**
 * Default implementation of the {@link SensoryMemory} module. This module should
 * sense the environment, store the sensed data and processing it. It should expect access 
 * to its content from {@link DetectionAlgorithm}s and it should transmit content to
 * {@link SensoryMotorMemory}.
 * @author Ryan J. McCall
 */
public abstract class SensoryMemoryImpl extends FrameworkModuleImpl implements SensoryMemory {

	private static Logger logger = Logger.getLogger(SensoryMemoryImpl.class.getCanonicalName());
	
    /**
     * The listeners associated with this memory.
     */
    protected List<SensoryMemoryListener> listeners
            = new ArrayList<SensoryMemoryListener>();
    
    /**
     * The environment associated with this memory.
     */
    protected Environment environment;

    /**
     * Default Constructor.
     */
    public SensoryMemoryImpl() {
    }

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.framework.FrameworkModule#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)
     */
    @Override
    public void addListener(ModuleListener listener) {
        if (listener instanceof SensoryMemoryListener) {
            addSensoryMemoryListener((SensoryMemoryListener) listener);
        }else{
        	logger.log(Level.WARNING, "Cannot add listener {1}",
					new Object[]{TaskManager.getCurrentTick(),listener});
        }
    }
    @Override
    public void addSensoryMemoryListener(SensoryMemoryListener l) {
        listeners.add(l);
    }

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.framework.FrameworkModuleImpl#setAssociatedModule(edu.memphis.ccrg.lida.framework.FrameworkModule, int)
     */
    @Override
    public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
        if (module instanceof Environment){
             environment = (Environment) module;
        }else{
        	logger.log(Level.WARNING, "Cannot add module {1}",
					new Object[]{TaskManager.getCurrentTick(),module});
        }
    }
    
}