/*
 * @(#)SimpleAttentionCodelet.java  1.0  May 3, 2007
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */

package edu.memphis.ccrg.lida.attention;

import java.util.List;

import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.csm.CurrentSituationalModelImpl;
import edu.memphis.ccrg.lida.workspace.csm.CSMContentImpl;

public class AttentionCodeletImpl implements AttentionCodelet, Runnable, Stoppable {
	
	private CurrentSituationalModelImpl model;
	private boolean keepRunning = true;
	private CSMContentImpl whatIwant;
	private CSMContentImpl whatIgot;
	private List<AttentionListener> listeners;
	private long threadID;
    
    public AttentionCodeletImpl(CurrentSituationalModelImpl csm){
    	model = csm;
    }
    
    public void addAttentionListener(AttentionListener l){
    	listeners.add(l);
    }

	public void addCoalToGlobalWorkspace(){
		for(int i = 0; i < listeners.size(); i++)
			listeners.get(i).putCoalition(new Coalition(whatIgot));		
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

	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}
}