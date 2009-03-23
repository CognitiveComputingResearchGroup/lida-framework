package edu.memphis.ccrg.lida.sensoryMemory;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Misc;
import edu.memphis.ccrg.lida.util.Stoppable;

public class SensoryMemoryDriver implements Runnable, Stoppable{
	private SensoryMemoryImpl sm;
	private boolean keepRunning;	
	private FrameworkTimer timer;
	private long threadID;
	
	public SensoryMemoryDriver(SensoryMemoryImpl sm, FrameworkTimer timer){
		this.sm = sm;
		keepRunning = true;		
		this.timer = timer;
	}//PAMDrive constructor
		
	public void run(){
		//boolean printSentContent = true;
		boolean printSentContent = false;
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(21 + timer.getSleepTime());
			}catch(Exception e){}
			timer.checkForStartPause();
			
			sm.processSimContent();
			sm.sendSensoryContent(printSentContent);					
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("\nSM: Ave. cycle time: " + 
							Misc.rnd((finishTime - startTime)/(double)counter));
		System.out.println("SM: Num. cycles: " + counter + "\n");			
	}//method run
	
	public void stopRunning(){
		try{Thread.sleep(20);} catch(InterruptedException e){}
		keepRunning = false;		
	}//method stopRunning

	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}
}//class SMDriver