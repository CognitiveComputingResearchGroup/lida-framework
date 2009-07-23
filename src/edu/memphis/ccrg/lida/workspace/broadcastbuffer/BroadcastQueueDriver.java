package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import edu.memphis.ccrg.lida.framework.FrameworkThreadManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class BroadcastQueueDriver extends GenericModuleDriver {

	private BroadcastQueueImpl bBuffer;

	public BroadcastQueueDriver(BroadcastQueueImpl bb, FrameworkThreadManager timer) {
		super(timer);
		bBuffer = bb;
	}// method

	@Override
	public void cycleStep() {
		//bBuffer.activateCodelets();
		bBuffer.sendEvent();
	}

}// class