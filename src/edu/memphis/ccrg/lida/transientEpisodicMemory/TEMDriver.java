/*
 * @(#)TEMDriver.java  1.0  June 19, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.transientEpisodicMemory;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;
import java.util.concurrent.Future;

/**
 * This is the driver for the transient episodic memory. The driver takes care
 * of running the thread and allowing access to the local associaiton.
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class TEMDriver implements Runnable, Stoppable {

    private TransientEpisodicMemory tem;
    private FrameworkTimer timer;
    private MemoryCue cue;
    private boolean keepRunning = true;
    private Future<LocalAssociation> association;

    /**
     * Constructor of the class.
     * @param tem a transient episodic memory for this driver
     * @param timer the timer used to handle the start/pause of the thread
     * @param cue the cue used to write on this memory
     */
    public TEMDriver(TransientEpisodicMemory tem, FrameworkTimer timer,
            MemoryCue cue) {
        this.tem = tem;
        this.timer = timer;
        this.cue = cue;
    }

    /**
     * Runs the thread for the transient episodic memory.
     */
    public void run() {
        while (keepRunning) {
            try {
                Thread.sleep(timer.getSleepTime());
            } catch (Exception e) {
            }
            timer.checkForStartPause();
            association = tem.cue(cue);
        }
    }

    /**
     * Gets the reference to the local association.
     * @return a future object with a reference to the local association
     */
    public Future<LocalAssociation> getAssociation() {
        return association;
    }

    /**
     * Stops the thread for the TEM.
     */
    public void stopRunning() {
        keepRunning = false;
    }

}