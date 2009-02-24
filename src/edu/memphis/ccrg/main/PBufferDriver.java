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
			try{Thread.sleep(25);}catch(Exception e){}
			pb.sendContent();
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
}
