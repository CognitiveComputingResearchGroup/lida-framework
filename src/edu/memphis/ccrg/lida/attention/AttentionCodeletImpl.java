/*
 * @(#)SimpleAttentionCodelet.java  1.0  May 3, 2007
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.attention;

import java.util.List;

import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CSMContentImpl;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModelImpl;

public class AttentionCodeletImpl implements AttentionCodelet, Runnable, Stoppable {
	
	private CurrentSituationalModelImpl model;
	private boolean keepRunning = true;
	private NodeStructure whatIwant;
	private NodeStructure whatIgot;
	private GlobalWorkspace global;
	private long threadID;
    
    public AttentionCodeletImpl(CurrentSituationalModelImpl csm, GlobalWorkspace g){
    	model = csm;
    	global = g;
    }

	public void addCoalToGlobalWorkspace(){
		global.putCoalition(new CoalitionImpl(whatIgot));		
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
	
	public boolean lookAtModel(){
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