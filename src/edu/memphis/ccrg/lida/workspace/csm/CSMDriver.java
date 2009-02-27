package edu.memphis.ccrg.lida.workspace.csm;

import edu.memphis.ccrg.lida.sensoryMemory.Stoppable;
import edu.memphis.ccrg.lida.util.M;

public class CSMDriver implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	private CSM csm;
	
	public CSMDriver(CSM csm){
		this.csm = csm;
	}

	public void run(){
		int counter = 0;		
		long startTime = System.currentTimeMillis();		
		while(keepRunning){
			try{Thread.sleep(25);}catch(Exception e){}
			
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();				
		System.out.println("\nCSM: Ave. cycle time: " + 
							M.rnd((finishTime - startTime)/(double)counter));
		System.out.println("CSM: Num. cycles: " + counter);		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
}
