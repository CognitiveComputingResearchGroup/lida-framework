package edu.memphis.ccrg.lida.util;

public class FrameworkTimer {
	
	private boolean shouldWait = false;
	private int sleepTime = 500;
	private int sleepConstant = 800;

	public void checkForClick(){		
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
	
	public int getSleepTime(){
		return sleepConstant;
	}

}//class FrameworkTimer
