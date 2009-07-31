package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class BroadcastQueueDriver extends ModuleDriverImpl {

	private BroadcastQueueImpl bBuffer;

	public BroadcastQueueDriver(BroadcastQueueImpl bb, LidaTaskManager timer) {
		super(timer);
		bBuffer = bb;
	}// method


	public void runDriverOneProcessingStep() {
		//bBuffer.activateCodelets();
		bBuffer.sendEvent();
	}

}// class