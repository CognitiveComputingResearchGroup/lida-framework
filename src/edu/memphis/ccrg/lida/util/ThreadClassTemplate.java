package edu.memphis.ccrg.lida.util;

import edu.memphis.ccrg.lida.framework.FrameworkThreadManager;
import edu.memphis.ccrg.lida.framework.Stoppable;


/**
 * 
 * A template to copy and paste.
 * 
 * @author ryanjmccall
 *
 */
public class ThreadClassTemplate implements Runnable, Stoppable{

	//FIELDS
	//
	private boolean keepRunning = true;
	private long threadID;
	private FrameworkThreadManager timer;
		
	public ThreadClassTemplate(FrameworkThreadManager t){
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