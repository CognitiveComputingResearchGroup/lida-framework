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
 *
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class TEMDriver implements Runnable, Stoppable {

    private TransientEpisodicMemory tem;
    private FrameworkTimer timer;
    private MemoryCue cue;
    private boolean keepRunning = true;
    private Future association;

    /**
     * 
     * @param tem
     * @param timer
     * @param cue
     */
    public TEMDriver(TransientEpisodicMemory tem, FrameworkTimer timer, MemoryCue cue) {
        this.tem = tem;
        this.timer = timer;
        this.cue = cue;
    }

    /**
     *
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
     * 
     * @return
     */
    public Future getAssociation() {
        return association;
    }

    /**
     *
     */
    public void stopRunning() {
        keepRunning = false;
    }

}
