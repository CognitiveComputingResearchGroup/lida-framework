package edu.memphis.ccrg.lida.workspace.broadcastBuffer;

import edu.memphis.ccrg.lida.framework.FrameworkTimer;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class BroadcastQueueDriver extends GenericModuleDriver {

	private BroadcastQueueImpl bBuffer;

	public BroadcastQueueDriver(BroadcastQueueImpl bb, FrameworkTimer timer) {
		super(timer);
		bBuffer = bb;
	}// method

	@Override
	public void cycleStep() {
		bBuffer.activateCodelets();
		bBuffer.sendEvent();
	}

}// class