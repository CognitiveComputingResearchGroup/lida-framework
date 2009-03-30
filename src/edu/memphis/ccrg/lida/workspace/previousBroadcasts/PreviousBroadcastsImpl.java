package edu.memphis.ccrg.lida.workspace.previousBroadcasts;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletObjective;

public class PreviousBroadcastsImpl implements Runnable, PreviousBroadcasts, BroadcastListener, CodeletAccessible{
	
	private boolean keepRunning = true;
	
	public PreviousBroadcastsImpl(){
		
	}
	
	public void run() {
		while(keepRunning){
			
		}
		
	}
	
	public void stopRunning(){
		keepRunning = false;
	}

	public void addPBroadsListener(PreviousBroadcastsListener l) {
		// TODO Auto-generated method stub
		
	}

	public void receiveBroadcast(BroadcastContent bc) {
		// TODO Auto-generated method stub
		
	}

	public WorkspaceContent getCodeletsObjective(CodeletObjective objective) {
		// TODO Auto-generated method stub
		return null;
	}

}
