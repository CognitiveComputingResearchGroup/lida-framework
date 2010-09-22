/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;

public class PassActivationFromBroadcastTask extends LidaTaskImpl {

	private BehaviorNetworkImpl bn;
	
	public PassActivationFromBroadcastTask(BehaviorNetworkImpl behaviorNetworkImpl) {
		bn = behaviorNetworkImpl;
	}

	@Override
	protected void runThisLidaTask() {
		bn.passActivationFromBroadcast();
		this.setTaskStatus(LidaTaskStatus.FINISHED);
	}

}