package edu.memphis.ccrg.lida.sensoryMemory;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.Stoppable;

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

		while(keepRunning){
			try{Thread.sleep(timer.getSleepTime());
			}catch(Exception e){}
			timer.checkForStartPause();
			
			sm.processSimContent();
			sm.sendSensoryContent();								
		}//while	
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