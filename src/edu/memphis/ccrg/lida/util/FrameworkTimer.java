package edu.memphis.ccrg.lida.util;

import java.util.Map;
import java.util.Set;
import java.util.HashMap;

public class FrameworkTimer {
	
	/**
	 * true -> Start out paused
	 * false -> Start out running	
	 */
	private boolean threadsArePaused = false;
	/**
	 * When paused, this is how long this thread will sleep before checking 
	 * to see if pause was clicked again to start things back up.
	 */
	private int msUntilICheckForUnpause = 100;
	/**
	 * Threads calling the member function 'getSleepTime' will sleep for this many ms.
	 */
	private int threadSleepTime = 150; 
	/**
	 * Map w/ key as the thread ID and the value for whether or not that thread
	 * should wait for the next time step.  TODO: Waiting for the next step has not yet
	 * been implemented.
	 */
	private Map<Long, Boolean> shouldWaitForNextStepMap = new HashMap<Long, Boolean>();
	
	public FrameworkTimer(boolean startPaused, int threadSleepTime){
		threadsArePaused = startPaused;
		this.threadSleepTime = threadSleepTime; 
	}
	
	/**
	 * Threads call this to get the standard sleep time.  This way the system
	 * operates roughly at the same speed across threads. 
	 * @return how long to sleep
	 */
	public int getSleepTime(){
		return threadSleepTime;
	}
	
	/**
	 * Threads should call this in every iteration of their cycle so that
	 * the system is pausable.
	 */
	public void checkForStartPause(){		
		while(threadsArePaused){
			try{
				Thread.sleep(msUntilICheckForUnpause);
			}catch(Exception e){}
		}//while	
	}//checkForClick
	
	public synchronized void toggleRunningThreads(){
		threadsArePaused = !threadsArePaused;
	}//

	public synchronized void resumeRunningThreads() {
		threadsArePaused = false;		
	}//

	/**
	 * Called from the GUI.  This sets all the values in the map
	 * to true, signifying that all these threads should run once 
	 * cycle and pause.
	 */
	public synchronized void haveThreadRunOnceThenPause(){
		threadsArePaused = false;		
		
		Set<Long> keys = shouldWaitForNextStepMap.keySet();
		for(Long l: keys){
			shouldWaitForNextStepMap.put(l, true);
		}
	}//

	/**
	 * Threads call this functions with their ID.
	 * If justRanOneStep = false  then return the current value for their entry 
	 * in the map.  
	 * Else if did just run once and the MAP says to run once and wait then return true
	 * and update map to false.
	 * If did just run once and MAP says not to 
	 * 
	 * @param justRanOneStep
	 * @param threadID
	 * @return
	 */
	public boolean checkForNextStep(boolean justRanOneStep, long threadID){
		Boolean callerShouldRunOnceAndWait = shouldWaitForNextStepMap.get(threadID);
		if(callerShouldRunOnceAndWait == null){
			System.out.println("Thread with ID " + threadID + " not registered! This thread continues to run.");
			return false;
		}
		
		if(justRanOneStep == false){
			if(callerShouldRunOnceAndWait){
				shouldWaitForNextStepMap.put(threadID, false);
				return true;
			}else{
				return false;
			}				
		}else{ // if last time got instruction to run once
			if(callerShouldRunOnceAndWait){//next step has already been pressed again!!			
				shouldWaitForNextStepMap.put(threadID, false);
				return true;
			}else
				return false;
		}
					
	}//boolean checkForNextStep

	/**
	 * Add a thread for the 'run one step' functionality
	 * @param threadID
	 */
	public void registerThread(long threadID) {
		shouldWaitForNextStepMap.put(threadID, false);		
	}//
	
	public boolean getStartStatus(){
		return threadsArePaused;
	}//
	
	public boolean threadsArePaused(){
		return threadsArePaused;
	}//

	public synchronized void setSleepTime(int newTime) {
		threadSleepTime = newTime;		
	}//
	
}//class FrameworkTimer