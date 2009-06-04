/*
 * @(#)SimpleAttentionCodelet.java  1.0  May 3, 2007
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public class AttentionCodeletImpl implements AttentionCodelet, Runnable, Stoppable {
	
	private CurrentSituationalModel model;
	private boolean keepRunning = true;
	private NodeStructure whatIwant;
	private NodeStructure whatIgot;
	private GlobalWorkspace global;
	private long threadID;
    
    public AttentionCodeletImpl(CurrentSituationalModel csm, GlobalWorkspace g){
    	model = csm;
    	global = g;
    }

	public void addCoalToGlobalWorkspace(){
		global.putCoalition(new CoalitionImpl((BroadcastContent)whatIgot));		
	}

	public void run() {
		while(keepRunning){
			if(lookAtModel()){
				synchronized(this){
					if(lookAtModel()){
						whatIgot = model.getContent();						
						addCoalToGlobalWorkspace();
					}
				}
			}
		}//while
	}//public void run
	
	private boolean lookAtModel(){
		return model.hasContent(whatIwant);
	}

	public void stopRunning() {
		keepRunning = false;		
	}

	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}
}