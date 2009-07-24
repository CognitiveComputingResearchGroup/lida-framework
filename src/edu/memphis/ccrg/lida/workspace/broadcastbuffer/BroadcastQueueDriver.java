package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import edu.memphis.ccrg.lida.framework.FrameworkTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class BroadcastQueueDriver extends GenericModuleDriver {

	private BroadcastQueueImpl bBuffer;

	public BroadcastQueueDriver(BroadcastQueueImpl bb, FrameworkTaskManager timer) {
		super(timer);
		bBuffer = bb;
	}// method

	@Override
	public void cycleStep() {
		//bBuffer.activateCodelets();
		bBuffer.sendEvent();
	}

}// class