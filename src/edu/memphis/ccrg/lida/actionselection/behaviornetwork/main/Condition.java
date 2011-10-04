package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;


public interface Condition extends Activatible {
	/** 
	 * The return object must be unique and it must be able to use as a map key
	 * equals and hashcode must be overwritten for this Id. 
	*/
	public Object getId();
	
	public double getDesirability();
	public void setDesirability(double d);
	public double getNetDesirability();	
	public void setWeight(double w);
	public double getWeight();
}
