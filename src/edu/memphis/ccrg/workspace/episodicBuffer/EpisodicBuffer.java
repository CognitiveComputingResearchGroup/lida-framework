package edu.memphis.ccrg.workspace.episodicBuffer;


public class EpisodicBuffer implements Runnable, EpisodicBufferInt{
	
    private boolean keepRunning = true;
	
	public EpisodicBuffer(){
		
	}
	
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
	public void stopRunning(){
		keepRunning = false;
	}

}
