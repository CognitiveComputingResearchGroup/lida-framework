package edu.memphis.ccrg.lida.util;

import edu.memphis.ccrg.lida.framework.FrameworkTaskManager;
import edu.memphis.ccrg.lida.framework.LidaTask;


/**
 * 
 * A template to copy and paste.
 * 
 * @author ryanjmccall
 *
 */
public class ThreadClassTemplate implements Runnable, LidaTask{

	//FIELDS
	//
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkTaskManager timer;
		
	public ThreadClassTemplate(FrameworkTaskManager t){
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