package edu.memphis.ccrg.lida.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class FrameworkTimer {
	
	private boolean shouldWait = false;//true -> Start out paused
										//false -> Start out running	
	private int sleepTime = 500;
	private int sleepConstant = 800;
	
	private Map<Long, Boolean> shouldWaitForNextStepMap = new HashMap<Long, Boolean>();
	
	public int getSleepTime(){
		return sleepConstant;
	}
	
	public void checkForStartPause(){		
		while(shouldWait){
			try{
				Thread.sleep(sleepTime);
			}catch(Exception e){}
		}//while	
	}//checkForClick
	
	public synchronized void toggleRunningThreads(){
		shouldWait = !shouldWait;
	}

	public synchronized void resumeRunningThreads() {
		shouldWait = false;		
	}

	public synchronized void haveThreadRunOnceThenPause(){
		shouldWait = false;		
		
		Set<Long> keys = shouldWaitForNextStepMap.keySet();
		for(Long l: keys){
			shouldWaitForNextStepMap.put(l, true);
		}
	}

	public boolean checkForNextStep(boolean justRanOneStep, long threadID){
		Boolean callerShouldRunOnceAndWait = shouldWaitForNextStepMap.get(threadID);
		if(callerShouldRunOnceAndWait == null){
			System.out.println("Thread with ID " + threadID + " not registered! This thread continues to run.");
			return false;
		}
		
		if(justRanOneStep == false){
			if(callerShouldRunOnceAndWait == true){
				shouldWaitForNextStepMap.put(threadID, false);
				return callerShouldRunOnceAndWait;
			}else{
				return callerShouldRunOnceAndWait;
			}				
		}else /*if(justRanOneStep == true)*/{ // if last time got instruction to run once
			if(callerShouldRunOnceAndWait == true){//next step has already been pressed again!!			
				shouldWaitForNextStepMap.put(threadID, false);
				return callerShouldRunOnceAndWait;
			}else{
				while(shouldWaitForNextStepMap.get(threadID) == false){ // wait until next step is clicked
					try{
						Thread.sleep(sleepTime);
					}catch(Exception e){}
				}//while
				return true;
			}
		}
					
	}//boolean checkForNextStep

	public void registerThread(long threadID) {
		shouldWaitForNextStepMap.put(threadID, false);		
	}
	
}//class FrameworkTimer
