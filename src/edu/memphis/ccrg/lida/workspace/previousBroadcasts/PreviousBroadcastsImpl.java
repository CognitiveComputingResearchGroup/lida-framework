package edu.memphis.ccrg.lida.workspace.previousBroadcasts;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletAccessible;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletsDesiredContent;

public class PreviousBroadcastsImpl implements Runnable, Stoppable, 
									PreviousBroadcasts, CodeletAccessible{
	
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

	public WorkspaceContent getCodeletsObjective(CodeletsDesiredContent objective) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getThreadID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setThreadID(long id) {
		// TODO Auto-generated method stub
		
	}

}
