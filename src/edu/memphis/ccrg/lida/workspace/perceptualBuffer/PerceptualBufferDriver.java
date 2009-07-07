package edu.memphis.ccrg.lida.workspace.perceptualBuffer;

import edu.memphis.ccrg.lida.framework.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;

public class PerceptualBufferDriver implements FrameworkModuleDriver{

	private boolean keepRunning = true;
	private PerceptualBufferImpl pb;
	private FrameworkTimer timer;
	
	public PerceptualBufferDriver(PerceptualBufferImpl pb, FrameworkTimer timer){
		this.pb = pb;
		this.timer = timer;
	}//constructor

	public void run(){	
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e) {
				stopRunning();
			}
			timer.checkForStartPause();
			//
			pb.activateCodelets();
			pb.sendGuiContent();
		}//while	
	}//method

	public void stopRunning(){
		keepRunning = false;		
	}//method

}//class