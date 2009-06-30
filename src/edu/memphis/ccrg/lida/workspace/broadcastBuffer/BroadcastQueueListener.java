package edu.memphis.ccrg.lida.workspace.broadcastBuffer;

import edu.memphis.ccrg.lida.shared.NodeStructure;

public interface BroadcastQueueListener {
	
	public void receiveBroadcastQueueContent(NodeStructure c);

}
