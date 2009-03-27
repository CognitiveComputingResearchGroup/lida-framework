package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CSMContentImpl;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModelContent;

public class CoalitionImpl implements Coalition{
	
	private CurrentSituationalModelContent csmContent;
	
	public CoalitionImpl() {
		csmContent = new CSMContentImpl();
	}
	
	//coalition activation: average of all nodes in coalition (0.0 - 1.0) times activation of attention codelet  (0.0 - 1.0)
	public CoalitionImpl(CurrentSituationalModelContent content){
		csmContent = content;
	}

	public void decay() {
		// TODO Auto-generated method stub
		
	}

	public double getActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public CurrentSituationalModelContent getContent() {
		return csmContent;
	}

}
