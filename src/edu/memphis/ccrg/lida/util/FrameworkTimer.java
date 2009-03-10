package edu.memphis.ccrg.lida.util;

public class FrameworkTimer {
	
	private boolean shouldWait = false;

	public void checkForClick() {
		
		boolean keepWaiting = false;
		synchronized(this){
			keepWaiting = shouldWait;
		}
		
		while(keepWaiting){
			try{Thread.sleep(100);}catch(Exception e){}
			synchronized(this){
				keepWaiting = shouldWait;
			}
			
		}//while keepWaiting
	
	}
	
	public void startRunningThreads(){
		synchronized(this){
			shouldWait = false;
		}
	}
	
	public void stopRunningThreads(){
		synchronized(this){
			shouldWait = true;
		}
	}
	
	

}
