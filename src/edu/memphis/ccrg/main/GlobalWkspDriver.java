package edu.memphis.ccrg.main;

import edu.memphis.ccrg.globalworkspace.GlobalWorkspaceImpl;

public class GlobalWkspDriver implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	private GlobalWorkspaceImpl g;
	
	public GlobalWkspDriver(GlobalWorkspaceImpl g){
		this.g = g;
	}

	public void run(){
		while(keepRunning){
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()

}
