package edu.memphis.ccrg.lida.globalworkspace;

import java.util.Collection;
import java.util.Map;

import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

public class MockBroadcastTrigger implements BroadcastTrigger {

	public Collection<Coalition> coalitions;
	@Override
	public void checkForTriggerCondition(Collection<Coalition> coalitions) {
		this.coalitions = coalitions;
	}

	@Override
	public void init(Map<String, Object> parameters, GlobalWorkspace gw) {
		// TODO Auto-generated method stub

	}

	public boolean wasReset;
	@Override
	public void reset() {
		wasReset = true;

	}

	@Override
	public void start() {
		// TODO Auto-generated method stub

	}

}
