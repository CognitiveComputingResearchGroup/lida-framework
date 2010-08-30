package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;

public class ActivateBehaviorsTask extends LidaTaskImpl {

	private BehaviorNetworkImpl bn;
	
	public ActivateBehaviorsTask(BehaviorNetworkImpl behaviorNetworkImpl) {
		bn = behaviorNetworkImpl;
	}

	@Override
	protected void runThisLidaTask() {
		bn.grantActivationFromBroadcast();
		
		//spread activation and inhibition among behaviors
    	//TODO have this method be called by the driver?
        bn.passActivationAmongBehaviors();
	}

}