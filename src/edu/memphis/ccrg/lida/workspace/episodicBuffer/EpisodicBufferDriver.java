package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class EpisodicBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private EpisodicBufferImpl eb;
	private FrameworkTimer timer;
	private FrameworkGui flowGui;
	
	public EpisodicBufferDriver(EpisodicBufferImpl eb, FrameworkTimer timer, FrameworkGui gui){
		this.eb = eb;
		this.timer = timer;
		flowGui = gui;
	}

	public void run(){
		while(keepRunning){
			timer.checkForStartPause();
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//method

}