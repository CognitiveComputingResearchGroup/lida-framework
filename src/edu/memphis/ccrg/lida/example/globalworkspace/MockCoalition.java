package edu.memphis.ccrg.lida.example.globalworkspace;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.workspace.currentSituationalModel.CurrentSituationalModelContent;

public class MockCoalition implements Coalition {

	private MockContent content;
	private double activation;
	
	public MockCoalition(int content,double activation){
		this.content=new MockContent(content);
		this.activation=activation;
	}
	/**
	 * @return the content
	 */
	public MockContent getContent() {
		return content;
	}

	/**
	 * @return the activation
	 */
	public double getActivation() {
		return activation;
	}
	public void decay() {
		// TODO Auto-generated method stub
		
	}


}
