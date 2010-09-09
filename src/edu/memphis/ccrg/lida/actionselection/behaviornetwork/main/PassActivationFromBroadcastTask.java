package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;

public class PassActivationFromBroadcastTask extends LidaTaskImpl {

	private BehaviorNetworkImpl bn;
	
	public PassActivationFromBroadcastTask(BehaviorNetworkImpl behaviorNetworkImpl) {
		bn = behaviorNetworkImpl;
	}

	@Override
	protected void runThisLidaTask() {
		bn.passActivationFromBroadcast();
	}

}