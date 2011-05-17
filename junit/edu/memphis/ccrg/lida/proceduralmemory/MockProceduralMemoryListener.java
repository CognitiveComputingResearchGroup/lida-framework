package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;

public class MockProceduralMemoryListener implements ProceduralMemoryListener {

	public Behavior behavior;
	@Override
	public void receiveBehavior(Behavior behavior) {
		this.behavior = behavior;
	}

}
