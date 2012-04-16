package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;


/**
 * An object that has desirability. It also has strategies 
 * to excite and decay this value.
 * @author Ryan J. McCall
 */
public interface Desirable {

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
	
	/**
	 * Excites desirable with specified desirability amount.
	 * @param a amount of excitation
	 */
	public void exciteDesirability(double a);
	/**
	 * Decay desirable for a specified time.
	 * @param t time ticks to decay
	 */
	public void decayDesirability(long t);
	
	/**
	 * Gets desirability {@link DecayStrategy}.
	 * @return the {@link DecayStrategy} used to decay desirability
	 */
	public DecayStrategy getDesirabilityDecayStrategy();
	
	/**
	 * Sets desirability {@link DecayStrategy}.
	 * @param d the {@link DecayStrategy} used to decay desirability
	 */
	public void setDesirabilityDecayStrategy(DecayStrategy d);
	
	/**
	 * Sets the desirability {@link ExciteStrategy}.
	 * @param s the {@link ExciteStrategy} used to excite desirability
	 */
	public void setDesirabilityExciteStrategy(ExciteStrategy s);
	
	/**
	 * Gets the desirability {@link ExciteStrategy}.
	 * @return the {@link ExciteStrategy} used to excite desirability
	 */
	public ExciteStrategy getDesirabilityExciteStrategy();
}
