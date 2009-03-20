package edu.memphis.ccrg.lida.templates;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class ThreadClassTemplate implements Runnable, Stoppable{

	//FIELDS
	//
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkTimer timer;
		
	public ThreadClassTemplate(FrameworkTimer t){
		timer = t;
	}
	
	public void run() {
		while(keepRunning){
			timer.checkForStartPause();
		}		
	}

	public long getThreadID() {
		return threadID;
	}

	public void setThreadID(long id) {
		threadID = id;		
	}

	public void stopRunning() {
		keepRunning = false;		
	}

}//class SBCodelet