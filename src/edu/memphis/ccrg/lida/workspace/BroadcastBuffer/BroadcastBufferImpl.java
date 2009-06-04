package edu.memphis.ccrg.lida.workspace.BroadcastBuffer;

import edu.memphis.ccrg.lida.framework.Stoppable;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletReadable;
import edu.memphis.ccrg.lida.workspace.structureBuildingCodelets.CodeletsDesiredContent;

public class BroadcastBufferImpl implements Runnable, Stoppable, 
									BroadcastBuffer, CodeletReadable{
	
	private boolean keepRunning = true;
	
	public BroadcastBufferImpl(){
		
	}
	
	public void run() {
		while(keepRunning){
			
		}
		
	}
	
	public void stopRunning(){
		keepRunning = false;
	}

	public void addPBroadsListener(BroadcastBufferListener l) {
		// TODO Auto-generated method stub
		
	}

	public void receiveBroadcast(BroadcastContent bc) {
		// TODO Auto-generated method stub
		
	}

	public WorkspaceContent getCodeletsDesiredContent(CodeletsDesiredContent objective) {
		// TODO Auto-generated method stub
		return null;
	}

	public long getThreadID() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setThreadID(long id) {
		// TODO Auto-generated method stub
		
	}

}
