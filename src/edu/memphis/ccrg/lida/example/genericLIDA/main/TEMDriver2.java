/*
 * @(#)TEMDriver.java  1.0  June 19, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.example.genericLIDA.main;

import edu.memphis.ccrg.lida.framework.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.transientEpisodicMemory.TransientEpisodicMemory;

/**
 *
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class TEMDriver2 implements FrameworkModuleDriver {

    private TransientEpisodicMemory tem;
    private FrameworkTimer timer;
    private boolean keepRunning = true;

    /**
     * 
     * @param tem
     * @param timer
     */
    public TEMDriver2(TransientEpisodicMemory tem, FrameworkTimer timer) {
        this.tem = tem;
        this.timer = timer;
    }

    /**
     *
     */
    public void run() {
        while (keepRunning) {
        	try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}				
			timer.checkForStartPause();//won't return if paused until started again					
			
			//CODE to run TEM
        }
    }//method

    /**
     *
     */
    public void stopRunning() {
        keepRunning = false;
    }

}//class
