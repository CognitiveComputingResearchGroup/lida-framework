package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;

/**
 * An object that can be decayed according to a {@link DecayStrategy}.
 * @author Ryan J. McCall
 */
public interface Decayable {
	
	/**
	 * Decays this object using its {@link DecayStrategy}. The decay depends on 
	 * the time that has passed since the last decay, which is indicated by the ticks parameter.
	 * 
	 * @param t the number of time ticks to decay for
	 */
	public void decay(long t);	

    /**
     * Sets the {@link DecayStrategy}.
     * @param s the {@link DecayStrategy} used to decay this object
     */
	public void setDecayStrategy(DecayStrategy s);
	
	/**
	 * Gets the {@link DecayStrategy}.
	 * @return the {@link DecayStrategy} used to decay this object
	 */
	public DecayStrategy getDecayStrategy();
}
