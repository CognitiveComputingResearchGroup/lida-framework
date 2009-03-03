package edu.memphis.ccrg.lida.example.globalworkspace;

import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.util.Stoppable;

public class GlobalWkspDriver implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	private GlobalWorkspace g;
	
	public GlobalWkspDriver(GlobalWorkspace g){
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
