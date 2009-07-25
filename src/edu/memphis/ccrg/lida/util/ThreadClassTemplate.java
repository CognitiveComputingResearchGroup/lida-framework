package edu.memphis.ccrg.lida.util;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
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
	private LidaTaskManager timer;
		
	public ThreadClassTemplate(LidaTaskManager t){
		timer = t;
	}
	
	public void run() {
		while(keepRunning){
			timer.checkForStartPause();
		}		
	}

	public long getTaskID() {
		return threadID;
	}

	public void setTaskID(long id) {
		threadID = id;		
	}

	public void stopRunning() {
		keepRunning = false;		
	}

}//class SBCodelet