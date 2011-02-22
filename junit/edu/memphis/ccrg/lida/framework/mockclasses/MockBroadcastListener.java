package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

public class MockBroadcastListener implements BroadcastListener {
	
	@Override
	public void receiveBroadcast(BroadcastContent bc){
		System.out.println ("Listener received broadcast: "+ bc);
		
	}

	@Override
	public void learn(BroadcastContent bc) {
		// TODO Auto-generated method stub
		
	}

}
