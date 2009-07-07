package edu.memphis.ccrg.lida.workspace.broadcastBuffer;

import edu.memphis.ccrg.lida.example.genericLIDA.main.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;

public class BroadcastQueueDriver implements FrameworkModuleDriver{

	private boolean keepRunning = true;
	private BroadcastQueueImpl bBuffer;
	private FrameworkTimer timer;
	
	public BroadcastQueueDriver(BroadcastQueueImpl bb, FrameworkTimer timer){
		bBuffer = bb;
		this.timer = timer;
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
			bBuffer.sendGuiContent();
		}//while keepRunning		
	}//method

	public void stopRunning(){
		keepRunning = false;		
	}//method
	
}//class