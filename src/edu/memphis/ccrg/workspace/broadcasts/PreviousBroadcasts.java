package edu.memphis.ccrg.workspace.broadcasts;

public class PreviousBroadcasts implements Runnable {
	
	private boolean keepRunning = true;
	
	public PreviousBroadcasts(){
		
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void stopRunning(){
		keepRunning = false;
	}

}
