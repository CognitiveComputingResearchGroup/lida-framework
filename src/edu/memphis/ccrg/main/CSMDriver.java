package edu.memphis.ccrg.main;

import edu.memphis.ccrg.workspace.CSM.CSM;

public class CSMDriver implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	private CSM csm;
	
	public CSMDriver(CSM csm){
		this.csm = csm;
	}

	public void run(){
		while(keepRunning){
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
}
