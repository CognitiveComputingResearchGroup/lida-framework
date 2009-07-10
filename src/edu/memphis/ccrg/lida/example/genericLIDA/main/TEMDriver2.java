/*
 * @(#)TEMDriver.java  1.0  June 19, 2009
 *
 * Copyright 2006-2009 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.example.genericLIDA.main;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;
import edu.memphis.ccrg.lida.transientEpisodicMemory.TransientEpisodicMemory;

/**
 * 
 * @author Rodrigo Silva L. <rsilval@acm.org>
 */
public class TEMDriver2 extends GenericModuleDriver {

	private TransientEpisodicMemory tem;

	/**
	 * 
	 * @param tem
	 * @param timer
	 */
	public TEMDriver2(TransientEpisodicMemory tem, FrameworkTimer timer) {
		super(timer);
		this.tem = tem;
	}


	@Override
	public void cycleStep() {
		// TODO Auto-generated method stub
		// CODE to run TEM		
	}

}// class
