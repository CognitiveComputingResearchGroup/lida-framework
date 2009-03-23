package edu.memphis.ccrg.lida.workspace.broadcasts;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PreviousBroadcastsDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private PreviousBroadcastsImpl pbroads;
	private FrameworkTimer timer;
	private long threadID;
	
	public PreviousBroadcastsDriver(PreviousBroadcastsImpl p, FrameworkTimer timer){
		pbroads = p;
		this.timer = timer;
	}

	public void run(){
		while(keepRunning){
			timer.checkForStartPause();
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
	
	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}
	
}
