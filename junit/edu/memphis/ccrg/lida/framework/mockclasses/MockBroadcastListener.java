package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

public class MockBroadcastListener implements BroadcastListener {
	
	public BroadcastContent content;
	@Override
	public void receiveBroadcast(BroadcastContent bc){
		content = bc;
	}

	@Override
	public void learn(BroadcastContent bc) {
		// not implemetned
		
	}

}
