package edu.memphis.ccrg.lida.workspace.BroadcastBuffer;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class BroadcastBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private BroadcastBufferImpl pbroads;
	private FrameworkTimer timer;
	
	public BroadcastBufferDriver(BroadcastBufferImpl p, FrameworkTimer timer){
		pbroads = p;
		this.timer = timer;
	}//

	public void run(){
		while(keepRunning){
			timer.checkForStartPause();
		}//while keepRunning		
	}//method

	public void stopRunning(){
		keepRunning = false;		
	}//method

	public void addFlowGui(FrameworkGui gui) {
		// TODO Auto-generated method stub
		
	}
	
}//class
