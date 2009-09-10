package edu.memphis.ccrg.lida.workspace.broadcastbuffer;

import edu.memphis.ccrg.lida.framework.LidaTask;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.LidaTaskNames;

public class BroadcastQueueDriver extends ModuleDriverImpl {

	private BroadcastQueueImpl broadcastQueue;

	public BroadcastQueueDriver(BroadcastQueueImpl bq, int ticksPerCycle, LidaTaskManager tm) {
		super(ticksPerCycle, tm, LidaTaskNames.broadcastQueueDriver);
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