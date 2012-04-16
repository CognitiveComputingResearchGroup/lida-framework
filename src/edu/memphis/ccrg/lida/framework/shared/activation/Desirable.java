package edu.memphis.ccrg.lida.framework.shared.activation;


/**
 * An object that has desirability. It also has strategies 
 * to excite and decay this value.
 * @author Ryan J. McCall
 */
public interface Desirable extends Excitable {

	/**
	 * Default desirability for {@link Desirable}.
	 */
	public static final double DEFAULT_DESIRABILITY = 0.0;
	
	/**
	 * Sets node desirability.
	 * 
	 * @param d
	 *            degree to which this node is desired by the agent
	 */
	public void setDesirability(double d);
	
	/**
	 * Returns the current desirability of this node.
	 * 
	 * @return a double signifying the degree to which this node is currently desired by
	 *         the agent
	 */
	public double getDesirability();
    
    /**
     * Returns the total desirability of this {@link Desirable}.
     * @return The total desirability, this is the current desirability if no base-level desirability is used.
     */
	public double getTotalDesirability();
	
	public double getNetDesirability();
}
