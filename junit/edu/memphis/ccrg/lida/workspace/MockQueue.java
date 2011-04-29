package edu.memphis.ccrg.lida.workspace;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.BroadcastQueueImpl;

public class MockQueue extends BroadcastQueueImpl{

	private BroadcastContent bc;
	
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		this.bc = bc;
	}
	
	public BroadcastContent checkContent(){
		return bc;
	}
}
