package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.GenericModuleDriver;

public class BroadcastQueueDriver extends GenericModuleDriver {

	private BroadcastQueueImpl bBuffer;

	public BroadcastQueueDriver(BroadcastQueueImpl bb, LidaTaskManager timer) {
		super(timer);
		bBuffer = bb;
	}// method

	@Override
	public void cycleStep() {
		//bBuffer.activateCodelets();
		bBuffer.sendEvent();
	}

}// class