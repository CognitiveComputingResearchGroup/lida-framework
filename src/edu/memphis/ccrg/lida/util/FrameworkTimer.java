package edu.memphis.ccrg.lida.util;

import java.util.HashMap;
import java.util.Map;

public class FrameworkTimer {
	
	private boolean shouldWait = true;//Start out paused.
	private boolean shouldRunOneStep = false;
	
	private int sleepTime = 500;
	private int sleepConstant = 800;
	
	private Map<Long, Boolean> nextStepMap = new HashMap<Long, Boolean>();
	
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

	public synchronized void haveThreadRunOnceThenPause() {
		shouldRunOneStep = true;		
	}

//	public boolean checkForNextStep(boolean runOneStep, long threadID){
//		Boolean runCallingThreadOneStep = nextStepMap.get(threadID);
//		if(runCallingThreadOneStep.equals(null))
//			System.out.println("Thread with ID " + threadID + " not registered! This thread continues to run.");
//		
//		if(runOneStep){ // if last time got instruction to run once
//			while(!shouldRunOneStep){ // wait until next step
//				try{
//					Thread.sleep(sleepTime);
//				}catch(Exception e){}
//			}//while
//		}//if
//
//		
//		return shouldRunOneStep;		
//	}//boolean checkForNextStep

	public void registerThread(long threadID) {
		nextStepMap.put(threadID, false);		
	}
	
}//class FrameworkTimer
