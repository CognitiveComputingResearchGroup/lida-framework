package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;

public class PassActivationAmongBehaviorsTask extends LidaTaskImpl{

	private BehaviorNetworkImpl bn;
	
	public PassActivationAmongBehaviorsTask(BehaviorNetworkImpl behaviorNetworkImpl) {
		bn = behaviorNetworkImpl;
	}
	
	@Override
	protected void runThisLidaTask() {
        bn.passActivationAmongBehaviors();
	}

}
