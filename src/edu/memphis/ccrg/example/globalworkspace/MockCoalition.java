package edu.memphis.ccrg.example.globalworkspace;

import edu.memphis.ccrg.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.globalworkspace.Coalition;

public class MockCoalition implements Coalition {

	private BroadcastContent content;
	private double activation;
	
	public MockCoalition(int content,double activation){
		this.content=new MockContent(content);
		this.activation=activation;
	}
	/**
	 * @return the content
	 */
	public BroadcastContent getContent() {
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
