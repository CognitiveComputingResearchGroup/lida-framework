/*
 * @(#)TEMDriver.java  1.0  June 19, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientepisodicmemory;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskNames;

import java.util.concurrent.Future;

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
     * Constructor of the class.
     * @param tem a transient episodic memory for this driver
     * @param timer the timer used to handle the start/pause of the thread
     * @param cue the cue used to write on this memory
     */
    public TemDriver(TransientEpisodicMemory tem, MemoryCue cue, int ticksPerCycle, LidaTaskManager tm) {
    	super(ticksPerCycle, tm, LidaTaskNames.TEM_DRIVER);
        this.tem = tem;
        this.cue = cue;
    }

	public void runThisDriver() {
		association = tem.cue(cue);
	}//method
    
    /**
     * Gets the reference to the local association.
     * @return a future object with a reference to the local association
     */
    public Future<LocalAssociation> getAssociation() {
        return association;
    }//method

	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}
}//class