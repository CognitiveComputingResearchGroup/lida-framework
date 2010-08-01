/*
 * @(#)TEMDriver.java  1.0  June 19, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import java.util.concurrent.Future;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * This is the driver for the transient episodic memory. The driver takes care
 * of running the thread and allowing access to the local associaiton.
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class TemDriver extends ModuleDriverImpl {

    private TransientEpisodicMemory tem;
    private MemoryCue cue;
    private Future<LocalAssociation> association;

    /**
     * 
     * @param tem
     * @param cue
     * @param ticksPerCycle
     * @param tm
     */
    public TemDriver(TransientEpisodicMemory tem, MemoryCue cue, int ticksPerCycle, LidaTaskManager tm) {
    	super(ticksPerCycle, tm,ModuleName.TemDriver);
        this.tem = tem;
        this.cue = cue;
    }
    public TemDriver() {
    	super(DEFAULT_TICKS_PER_CYCLE,ModuleName.TemDriver);
    }

	public void runThisDriver() {
		tem.cue(cue);
	}//method
    
    /**
     * Gets the reference to the local association.
     * @return a future object with a reference to the local association
     */
    public Future<LocalAssociation> getAssociation() {
        return association;
    }//method


	@Override
	public String toString() {
		return ModuleName.TemDriver + "";
	}
	
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof TransientEpisodicMemory
					&& module.getModuleName() == ModuleName.TransientEpisodicMemory) {
				tem = (TransientEpisodicMemory) module;
			}
		}
	}

}//class