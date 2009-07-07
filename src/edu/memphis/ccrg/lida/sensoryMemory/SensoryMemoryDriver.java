package edu.memphis.ccrg.lida.sensoryMemory;

import edu.memphis.ccrg.lida.framework.FrameworkModuleDriver;
import edu.memphis.ccrg.lida.framework.FrameworkTimer;

public class SensoryMemoryDriver implements FrameworkModuleDriver{
	private SensoryMemory sm;
	private boolean keepRunning;	
	private FrameworkTimer timer;
	private long threadID;
	
	public SensoryMemoryDriver(SensoryMemory sm, FrameworkTimer timer){
		this.sm = sm;
		keepRunning = true;		
		this.timer = timer;
	}//constructor
		
	public void run(){
		while(keepRunning){
			try{
				Thread.sleep(timer.getSleepTime());
			}catch(InterruptedException e){
				stopRunning();
			}				
			timer.checkForStartPause();
			
			sm.processSimContent();
			sm.sendSensoryContent();								
		}//while	
	}//method run
	
	public void stopRunning(){
		keepRunning = false;		
	}//method stopRunning

	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}
}//class SMDriver