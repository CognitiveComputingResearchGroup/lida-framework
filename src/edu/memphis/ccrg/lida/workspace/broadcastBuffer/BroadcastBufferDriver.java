package edu.memphis.ccrg.lida.workspace.broadcastBuffer;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class BroadcastBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private BroadcastBuffer bBuffer;
	private FrameworkTimer timer;
	private FrameworkGui flowGui;
	
	public BroadcastBufferDriver(BroadcastBuffer bb, FrameworkTimer timer, FrameworkGui gui){
		bBuffer = bb;
		this.timer = timer;
		flowGui = gui;
	}//method

	public void run(){
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}				
			timer.checkForStartPause();
			bBuffer.activateCodelets();
		
			flowGui.receiveGuiContent(FrameworkGui.FROM_BROADCAST_BUFFER, bBuffer.getGuiContent());
		}//while keepRunning		
	}//method

	public void stopRunning(){
		keepRunning = false;		
	}//method
	
}//class