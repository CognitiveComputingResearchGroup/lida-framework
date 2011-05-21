package edu.memphis.ccrg.lida.workspace;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.BroadcastQueueImpl;

public class MockBroadcastQueueImpl extends BroadcastQueueImpl{

	public BroadcastContent broadcastContent;
	
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		this.broadcastContent = bc;
	}
	
}
