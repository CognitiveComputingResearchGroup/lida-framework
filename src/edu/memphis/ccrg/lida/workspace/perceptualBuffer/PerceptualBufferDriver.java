package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class PerceptualBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private PerceptualBuffer pb;
	private FrameworkTimer timer;
	private FrameworkGui flowGui;
	
	public PerceptualBufferDriver(PerceptualBuffer pb, FrameworkTimer timer, FrameworkGui gui){
		this.pb = pb;
		this.timer = timer;
		flowGui = gui;
	}//constructor

	public void run(){	
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());}catch(Exception e){}
			timer.checkForStartPause();
			//
			pb.activateCodelets();
			flowGui.receiveGuiContent(FrameworkGui.FROM_PERCEPTUAL_BUFFER, pb.getGuiContent());
		}//while	
	}//method

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method

}//class