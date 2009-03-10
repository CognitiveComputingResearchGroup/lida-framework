package edu.memphis.ccrg.lida.workspace.broadcasts;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PBroadsDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private PreviousBroadcasts pbroads;
	private FrameworkTimer timer;
	
	public PBroadsDriver(PreviousBroadcasts p, FrameworkTimer timer){
		pbroads = p;
		this.timer = timer;
	}

	public void run(){
		while(keepRunning){
			timer.checkForClick();
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
}
