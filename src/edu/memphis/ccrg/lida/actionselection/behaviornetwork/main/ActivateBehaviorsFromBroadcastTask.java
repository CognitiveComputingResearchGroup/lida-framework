package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;

public class ActivateBehaviorsFromBroadcastTask extends LidaTaskImpl {

	private BehaviorNetworkImpl bn;
	
	public ActivateBehaviorsFromBroadcastTask(BehaviorNetworkImpl behaviorNetworkImpl) {
		bn = behaviorNetworkImpl;
	}

	@Override
	protected void runThisLidaTask() {
		bn.grantActivationFromBroadcast();
	}

}