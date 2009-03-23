package edu.memphis.ccrg.lida.workspace.broadcasts;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.workspace.sbCodelets.CodeletAccessible;

public class PreviousBroadcastsImpl implements Runnable, BroadcastListener, CodeletAccessible{
	
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

}
