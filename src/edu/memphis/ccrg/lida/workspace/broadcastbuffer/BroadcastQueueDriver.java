package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;

public class BroadcastQueueDriver extends ModuleDriverImpl {

	private BroadcastQueueImpl broadcastQueue;

	public BroadcastQueueDriver(BroadcastQueueImpl bq, int ticksPerCycle) {
		super(ticksPerCycle);
		broadcastQueue = bq;
	}// method


	public void runThisDriver() {
		//bBuffer.activateCodelets();
		broadcastQueue.sendEvent();
	}


	@Override
	protected void processResults(LidaTask task) {
		// TODO Auto-generated method stub
		
	}

}// class