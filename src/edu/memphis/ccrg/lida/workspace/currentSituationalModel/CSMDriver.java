package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class CSMDriver implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	
	private FrameworkTimer timer;
	private CurrentSituationalModel csm;
	private FrameworkGui flowGui;
	
	public CSMDriver(CurrentSituationalModel csm, FrameworkTimer t, FrameworkGui gui){
		timer = t;
		this.csm = csm;
		flowGui = gui;
	}

	public void run(){
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}
			timer.checkForStartPause();
			csm.sendCSMContent();
			flowGui.receiveGuiContent(FrameworkGui.FROM_CSM, csm.getGuiContent());	        
		}//while
	}//public void run()

	public void stopRunning(){
		try{Thread.sleep(20);}catch(Exception e){}
		keepRunning = false;		
	}//method
	
}//class
