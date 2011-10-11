package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;


/**
 * A requirement for a {@link Behavior} to be selected
 * @author Javier Snaider
 */
public interface Condition extends Activatible {
	
   /** 
    * Gets id. 
    * The return object must be unique and it must be able to use as a map key i.e.
    * {@link Object#equals(Object)} and {@link Object#hashCode()} must be overwritten for this id. 
    * 
    * @return Condition's unique id
	*/
	public int getId();
	
	/**
	 * Gets desirability
	 * @return this condition's desirability
	 */
	public double getDesirability();
	
	/**
	 * Sets desirability
	 * @param d condition's desirability
	 */
	public void setDesirability(double d);
	
	
	/**
	 * Gets net desirability
	 * @return the difference between desirability and activation
	 */
	public double getNetDesirability();	
	
	/**
	 * Sets weight. 
	 * @param w condition's weight. How important the condition is for this {@link Behavior}
	 */
	public void setWeight(double w);
	
	/**
	 * Gets weight
	 * @return this condition's weight. How important the condition is for this {@link Behavior}
	 */
	public double getWeight();
}
