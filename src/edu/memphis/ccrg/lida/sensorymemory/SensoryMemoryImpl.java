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
 * @author unascribed
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

    /**
     * Associates this memory with its respective environment.
     * @param module the environment associated with this memory
     * @param moduleUsage not used
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

    /**
     * The function of this method is still an open question.
     * @param alg not used
     */
    @Override
    public void receiveExecutingAlgorithm(Object alg) {
        //TODO What to do here is a research question.
    }

    @Override
    public void decayModule(long ticks) {
        super.decayModule(ticks);
        //TODO
    }

    //TODO what are these 2 set/get methods? can we delete them?
    //Ask Tamas, he implemented the interface.
    /**
     * 
     * @param state
     * @return
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

    /**
     * 
     * @return 
     */
    @Override
    public Object getState() {
        return sensoryMemoryContent;
    }
}
