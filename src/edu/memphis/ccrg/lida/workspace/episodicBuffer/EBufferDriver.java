package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class EBufferDriver  implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private EpisodicBuffer eb;
	private FrameworkTimer timer;
	
	public EBufferDriver(EpisodicBuffer eb, FrameworkTimer timer){
		this.eb = eb;
		this.timer = timer;
	}

	public void run(){
		while(keepRunning){
			timer.checkForClick();
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
}
