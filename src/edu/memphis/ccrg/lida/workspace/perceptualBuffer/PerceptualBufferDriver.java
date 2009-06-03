package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class PerceptualBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private PerceptualBuffer pb;
	private FrameworkTimer timer;
	private long threadID;
	
	public PerceptualBufferDriver(PerceptualBuffer pb, FrameworkTimer timer){
		this.pb = pb;
		this.timer = timer;
	}//

	public void run(){	
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());
			}catch(Exception e){}
			timer.checkForStartPause();
			pb.sendContentToCodelets();
			
		}//while	
	}//method

	public void stopRunning(){
		try{Thread.sleep(20);}catch(InterruptedException e){}
		keepRunning = false;		
	}//method

}//class