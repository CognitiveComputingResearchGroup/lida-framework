package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.workspace.csm.CSMContentImpl;

public class Coalition implements CoalitionInterface{
	
	//coalition activation: average of all nodes in coalition (0.0 - 1.0) times activation of attention codelet  (0.0 - 1.0)
	public Coalition(CSMContentImpl c){
		
	}

	public void decay() {
		// TODO Auto-generated method stub
		
	}

	public double getActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public BroadcastContent getContent() {
		// TODO Auto-generated method stub
		return null;
	}

}
