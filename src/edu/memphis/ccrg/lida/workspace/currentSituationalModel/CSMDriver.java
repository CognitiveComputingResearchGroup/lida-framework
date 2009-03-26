package edu.memphis.ccrg.lida.workspace.currentSituationalModel;

import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;

public class CSMDriver implements Runnable, Stoppable{
	
	private boolean keepRunning = true;
	private CurrentSituationalModelImpl csm;
	private long threadID;
	
	public CSMDriver(CurrentSituationalModelImpl csm){
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
							Misc.rnd((finishTime - startTime)/(double)counter));
		System.out.println("CSM: Num. cycles: " + counter);		
	}//public void run()

	public void stopRunning(){
		try{Thread.sleep(20);}catch(Exception e){}
		keepRunning = false;		
	}//public void stopRunning()
	
	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}
	
}
