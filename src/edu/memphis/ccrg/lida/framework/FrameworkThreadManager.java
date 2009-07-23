package edu.memphis.ccrg.lida.framework;

public class FrameworkThreadManager extends ThreadSpawnerImpl {
	
	/**
	 * true -> Start out paused
	 * false -> Start out running	
	 */
	private boolean threadsArePaused = false;
	/**
	 * When paused, this is how long this thread will sleep before checking 
	 * to see if pause was clicked again to start things back up.
	 */
	private int msUntilICheckForUnpause = 10;
	/**
	 * Threads calling the member function getSleepTime() will sleep for this many ms.
	 */
	private int threadSleepTime = 150; 
	
	public FrameworkThreadManager(boolean startPaused, int threadSleepTime){
		threadsArePaused = startPaused;
		this.threadSleepTime = threadSleepTime; 
	}
	
	public boolean threadsArePaused(){
		return threadsArePaused;
	}
	public synchronized void pauseSpawnedThreads(){
		threadsArePaused = true;
	}
	public synchronized void resumeSpawnedThreads() {
		threadsArePaused = false;		
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
	}//method
	
	/**
	 * Threads call this to get the standard sleep time.  This way the system
	 * operates roughly at the same speed across threads. 
	 * @return how long to sleep
	 */
	public int getSleepTime(){
		return threadSleepTime;
	}
	public synchronized void setSleepTime(int newTime) {
		threadSleepTime = newTime;		
	}//
	
}//class FrameworkTimer