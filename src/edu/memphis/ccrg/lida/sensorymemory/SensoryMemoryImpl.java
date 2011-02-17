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
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetector;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener;

/**
 * Default implementation of the {@link SensoryMemory} module. This module should
 * sense the environment, store the sensed data and processing it. It should expect access 
 * to its content from {@link FeatureDetector}s and it should transmit content to
 * {@link SensoryMotorMemory}.
 * @author Ryan J. McCall
 */
public abstract class SensoryMemoryImpl extends LidaModuleImpl implements SensoryMemory, SensoryMotorMemoryListener {

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
     * The content of this memory. 
     */
    protected Object content;

    /**
     * Default Constructor.
     */
    public SensoryMemoryImpl() {
        super(ModuleName.SensoryMemory);
    }

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.framework.LidaModule#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)
     */
    @Override
    public void addListener(ModuleListener listener) {
        if (listener instanceof SensoryMemoryListener) {
            addSensoryMemoryListener((SensoryMemoryListener) listener);
        }else{
        	logger.log(Level.WARNING, "Cannot add listener " + listener.toString(), LidaTaskManager.getCurrentTick());
        }
    }
    @Override
    public void addSensoryMemoryListener(SensoryMemoryListener l) {
        listeners.add(l);
    }

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.framework.LidaModuleImpl#setAssociatedModule(edu.memphis.ccrg.lida.framework.LidaModule, int)
     */
    @Override
    public void setAssociatedModule(LidaModule module, int moduleUsage) {
        if (module instanceof Environment){
             environment = (Environment) module;
        }else{
        	logger.log(Level.WARNING, "Cannot add module " + module.getModuleName(), LidaTaskManager.getCurrentTick());
        }
    }
    
    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.sensorymemory.SensoryMemory#runSensors()
     */
    @Override
	public abstract void runSensors();

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener#receiveExecutingAlgorithm(java.lang.Object)
     */
    @Override
    public void receiveExecutingAlgorithm(Object alg) {
        //What to do here is a research question.
    }

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.framework.LidaModuleImpl#decayModule(long)
     */
    @Override
    public void decayModule(long ticks) {
        super.decayModule(ticks);
        //your implementation specific decay method should call super first. 
    }

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.framework.dao.Saveable#setState(java.lang.Object)
     */
    @Override
    public boolean setState(Object state) {
        try {
            content = state;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.framework.dao.Saveable#getState()
     */
    @Override
    public Object getState() {
        return content;
    }
}
