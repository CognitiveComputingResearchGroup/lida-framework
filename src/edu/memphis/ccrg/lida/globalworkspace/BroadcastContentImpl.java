package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModelContent;

public class BroadcastContentImpl implements BroadcastContent{
	
	private Coalition c;
	
	public BroadcastContentImpl(Coalition c){
		this.c = c;
	}
	
	public BroadcastContentImpl() {
		c = new CoalitionImpl();
	}

	public void setContent(Coalition c){
		this.c = c;
	}

	public Object getContent() {
		return c;
	}

}
