/*
 * @(#)SimpleAttentionCodelet.java  1.0  May 3, 2007
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.attention;

import java.util.ArrayList;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionInterface;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.sensoryMemory.Stoppable;
import edu.memphis.ccrg.lida.workspace.csm.CSM;
import edu.memphis.ccrg.lida.workspace.csm.ModelContent;

public class AttentionCodelet implements AttentionCodeletInterface, Runnable, Stoppable {
	
	private CSM model;
	private GlobalWorkspace gwksp;
	private boolean keepRunning = true;
	private ModelContent whatIwant;
	private ModelContent whatIgot;
    
    public AttentionCodelet(CSM csm, GlobalWorkspace g){
    	model = csm;
    	gwksp = g;
    }

	public void addCoalToGlobalWorkspace() {
		gwksp.putCoalition(new Coalition(whatIgot));		
	}

	public void run() {
		while(keepRunning){
			if(lookAtModel()){
				synchronized(this){
					if(lookAtModel()){
						whatIgot = model.getContent(whatIwant);						
						addCoalToGlobalWorkspace();
					}
				}
			}
		}//while
	}//public void run
	
	public boolean lookAtModel(){
		return model.hasContent(whatIwant);
	}

	public void stopRunning() {
		keepRunning = false;		
	}
}