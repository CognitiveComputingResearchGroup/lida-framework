package edu.memphis.ccrg.lida.main;

import edu.memphis.ccrg.lida.workspace.episodicBuffer.EpisodicBuffer;

public class EBufferDriver  implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private EpisodicBuffer eb;
	
	public EBufferDriver(EpisodicBuffer eb){
		this.eb = eb;
	}

	public void run(){
		while(keepRunning){
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
}
