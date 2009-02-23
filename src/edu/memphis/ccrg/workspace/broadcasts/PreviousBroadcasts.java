package edu.memphis.ccrg.workspace.broadcasts;

import edu.memphis.ccrg.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.globalworkspace.BroadcastListener;

public class PreviousBroadcasts implements Runnable, BroadcastListener{
	
	private boolean keepRunning = true;
	
	public PreviousBroadcasts(){
		
	}
	
	public void run() {
		while(keepRunning){
			
		}
		
	}
	
	public void stopRunning(){
		keepRunning = false;
	}

	public void addPBroadsListener(PBroadsListener l) {
		// TODO Auto-generated method stub
		
	}

	public void receiveBroadcast(BroadcastContent bc) {
		// TODO Auto-generated method stub
		
	}

}
