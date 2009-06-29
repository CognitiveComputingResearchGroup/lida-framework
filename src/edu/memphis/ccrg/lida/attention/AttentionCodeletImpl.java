/*
 * @(#)SimpleAttentionCodelet.java  1.0  May 3, 2007
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public class AttentionCodeletImpl implements AttentionCodelet, Runnable, Stoppable {
	
	private boolean keepRunning = true;
	private NodeStructure csmContent = new NodeStructureImpl();
	
	private CurrentSituationalModel csm;
	private GlobalWorkspace global;
	private double activation;
	private NodeStructure soughtContent;	
    
    public AttentionCodeletImpl(CurrentSituationalModel csm, GlobalWorkspace g, 
    							double activation, NodeStructure content){
    	this.csm = csm;
    	global = g;
    	this.activation = activation;
    	soughtContent = content;    	
    }

	public void run() {
		boolean shouldAddCoalition = false;
		while(keepRunning){
			if(csm.hasContent(soughtContent)){
				synchronized(this){				
					if(csm.hasContent(soughtContent)){
						shouldAddCoalition = true; //Use this to free up CSM quickly
						csmContent = csm.getCSMContent();	//TODO: more sophisticated	
					}
				}//synchronized
				if(shouldAddCoalition){ //Based on boolean, add a new coalition
					global.addCoalition(new CoalitionImpl(csmContent, activation));
					shouldAddCoalition = false;
				}//if
			}//if
		}//while
	}//method	

	public void stopRunning() {
		keepRunning = false;		
	}
	
}//class