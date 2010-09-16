package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import java.io.Serializable;

/**
 * An object with activation that is excitable and decayable
 * @author Ryan J McCall
 *
 */
public interface Activatible extends Serializable {
	
	/**
	 * @return the current activation.
	 */
	public double getActivation();
	
	/**
	 * Set the current activation.
	 * @param activation
	 */
    public void setActivation(double activation);
    /**
     * 
     * @return The total activation. It should return the 
     * current activation if no based activation is used.
     */
	public double getTotalActivation();
    
	/**
	 * The current activation of this node is increased 
	 * using the excitation value as a parameter for the ExciteStrategy 
	 * 
	 * @param   amount the value to be used to increase the current activation of
	 *          this node
	 */
    public void excite(double amount); 
    /**
     * 
     * @param strategy the Excite strategy for the current activation.
     */
	public void setExciteStrategy(ExciteStrategy strategy);
	/**
	 * 
	 * @return the excite strategy
	 */
	public ExciteStrategy getExciteStrategy();
	
	/**
	 * decay the current activation using the decay strategy. The decay depends on 
	 * the time since the last decaying. It is indicated by the parameter ticks.
	 * 
	 * @param ticks the number of ticks to decay
	 */
	public void decay(long ticks);	

    /**
     * 
     * @param strategy the decay strategy for the current activation.
     */
	public void setDecayStrategy(DecayStrategy strategy);
	/**
	 * 
	 * @return the decay strategy.
	 */
	public DecayStrategy getDecayStrategy();

}
