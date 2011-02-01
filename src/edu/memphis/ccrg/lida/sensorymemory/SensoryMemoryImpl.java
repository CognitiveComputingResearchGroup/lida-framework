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

/**
 * This class implements the sensory memory module. This memory's tasks include
 * sensing the environment, storing the sensed data ,and processing it. This
 * module sends information to perceptual associative memory, and sensory motor
 * memory.
 * @author Ryan J. McCall
 */
public abstract class SensoryMemoryImpl extends LidaModuleImpl implements SensoryMemory {

    /**
     * The list of listeners associated with this memory, initially PAM & SMM.
     */
    private List<SensoryMemoryListener> listeners = new ArrayList<SensoryMemoryListener>();
    
    /**
     * The environment associated with this memory.
     */
    protected Environment environment;
    
    /**
     * The content of this memory. 
     */
    protected Object sensoryMemoryContent;

    /**
     * Constructor of the class.
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
        if (module != null) {
            if (module instanceof Environment
                    && module.getModuleName() == ModuleName.Environment) {
                environment = (Environment) module;
            }
        }
    }

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener#receiveExecutingAlgorithm(java.lang.Object)
     */
    @Override
    public void receiveExecutingAlgorithm(Object alg) {
        //TODO What to do here is a research question.
    }

    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.framework.LidaModuleImpl#decayModule(long)
     */
    @Override
    public void decayModule(long ticks) {
        super.decayModule(ticks);
        //TODO
    }

    //TODO what are these 2 set/get methods? can we delete them?
    /* (non-Javadoc)
     * @see edu.memphis.ccrg.lida.framework.dao.Saveable#setState(java.lang.Object)
     */
    @Override
    public boolean setState(Object state) {
        try {
            sensoryMemoryContent = state;
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
        return sensoryMemoryContent;
    }
}
