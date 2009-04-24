package edu.memphis.ccrg.lida.attention;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.util.FrameworkTimer;
import edu.memphis.ccrg.lida.util.Stoppable;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModel;

public class AttentionDriver implements Runnable, Stoppable, BroadcastListener{

	private FrameworkTimer timer;
	private boolean keepRunning = true;
	private BroadcastContent broadcastContent;
	private CurrentSituationalModel csm;
	private long threadID;
	private GlobalWorkspace global;
	
	public AttentionDriver(FrameworkTimer timer, CurrentSituationalModel csm, GlobalWorkspaceImpl gwksp) {
		this.timer = timer;
		this.csm = csm;
		global = gwksp;
	}

	public void addCSM(CurrentSituationalModel csm) {
		this.csm = csm;		
	}

	public void run() {
		while(keepRunning){
			timer.checkForStartPause();
			
		}
		
	}

	public void stopRunning() {
		keepRunning = false;		
	}

	public synchronized void receiveBroadcast(BroadcastContent bc) {
			broadcastContent = bc;
	}
	
	public void setThreadID(long id){
		threadID = id;
	}
	
	public long getThreadID() {
		return threadID;
	}

}
