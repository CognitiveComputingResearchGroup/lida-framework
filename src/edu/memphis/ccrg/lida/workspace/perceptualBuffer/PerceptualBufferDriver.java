package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class PerceptualBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private PerceptualBuffer pb;
	private FrameworkTimer timer;
	
	public PerceptualBufferDriver(PerceptualBuffer pb, FrameworkTimer timer){
		this.pb = pb;
		this.timer = timer;
	}//constructor

	public void run(){	
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());
			}catch(Exception e){}
			timer.checkForStartPause();
			pb.activateCodelets();
			
		}//while	
	}//method

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method

}//class