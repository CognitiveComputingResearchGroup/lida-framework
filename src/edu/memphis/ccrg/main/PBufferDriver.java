package edu.memphis.ccrg.main;

import edu.memphis.ccrg.workspace.perceptualBuffer.PerceptualBuffer;

public class PBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private PerceptualBuffer pb;
	
	public PBufferDriver(PerceptualBuffer pb){
		this.pb = pb;
	}//

	public void run(){
		while(keepRunning){
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
}
