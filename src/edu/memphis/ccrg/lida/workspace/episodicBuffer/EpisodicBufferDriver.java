package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import edu.memphis.ccrg.lida.framework.FrameworkGui;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

public class EpisodicBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private EpisodicBufferImpl eb;
	private FrameworkTimer timer;
	
	public EpisodicBufferDriver(EpisodicBufferImpl eb, FrameworkTimer timer){
		this.eb = eb;
		this.timer = timer;
	}

	public void run(){
		while(keepRunning){
			timer.checkForStartPause();
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//method

	public void addFlowGui(FrameworkGui gui) {
		// TODO Auto-generated method stub
		
	}
	
}
