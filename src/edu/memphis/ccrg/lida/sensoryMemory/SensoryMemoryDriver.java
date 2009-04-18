package edu.memphis.ccrg.lida.sensoryMemory;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Printer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.wumpusWorld.b_sensoryMemory.SensoryMemoryImpl;

public class SensoryMemoryDriver implements Runnable, Stoppable{
	private SensoryMemory sm;
	private boolean keepRunning;	
	private FrameworkTimer timer;
	private long threadID;
	
	public SensoryMemoryDriver(SensoryMemory sm, FrameworkTimer timer){
		this.sm = sm;
		keepRunning = true;		
		this.timer = timer;
	}//PAMDrive constructor
		
	public void run(){
		int counter = 0;		
		long startTime = System.currentTimeMillis();
		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());
			}catch(Exception e){}
			timer.checkForStartPause();
			
			sm.processSimContent();
			sm.sendSensoryContent();					
			counter++;			
		}//while keepRunning
		long finishTime = System.currentTimeMillis();		
			
		System.out.println("SM : Ave. cycle time: " + 
							Printer.rnd((finishTime - startTime)/(double)counter));
		//System.out.println("SM: Num. cycles: " + counter + "\n");			
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