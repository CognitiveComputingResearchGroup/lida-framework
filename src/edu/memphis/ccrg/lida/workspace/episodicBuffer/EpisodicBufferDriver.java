package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import edu.memphis.ccrg.lida.example.genericLIDA.main.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;

public class EpisodicBufferDriver implements FrameworkModuleDriver{

	private boolean keepRunning = true;
	private EpisodicBufferImpl eBuffer;
	private FrameworkTimer timer;
	
	public EpisodicBufferDriver(EpisodicBufferImpl eb, FrameworkTimer timer){
		this.eBuffer = eb;
		this.timer = timer;
	}

	public void run(){
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}				
			timer.checkForStartPause();
			eBuffer.activateCodelets();	
			eBuffer.sendGuiContent();
		}//while		
	}//method

	public void stopRunning(){
		keepRunning = false;		
	}//method

}//class