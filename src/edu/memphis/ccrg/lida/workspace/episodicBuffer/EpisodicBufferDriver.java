package edu.memphis.ccrg.lida.workspace.episodicBuffer;

import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;

public class EpisodicBufferDriver implements Runnable, Stoppable{

	private boolean keepRunning = true;
	private EpisodicBufferImpl eb;
	private FrameworkTimer timer;
	private long threadID;
	
	public EpisodicBufferDriver(EpisodicBufferImpl eb, FrameworkTimer timer){
		this.eb = eb;
		this.timer = timer;
	}

	public void run(){
		while(keepRunning){
			timer.checkForStartPause();
			
		}//while keepRunning		
	}//public void run()

	public void stopRunning(){
		keepRunning = false;		
	}//public void stopRunning()
	
	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}
}
