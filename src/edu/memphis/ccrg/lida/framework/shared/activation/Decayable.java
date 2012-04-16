package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;

public interface Decayable {
	
	/**
	 * decay the current activation using the decay strategy. The decay depends on 
	 * the time since the last decaying. It is indicated by the parameter ticks.
	 * 
	 * @param ticks the number of ticks to decay
	 */
	public void decay(long ticks);	

    /**
     * Sets the decay strategy
     * @param strategy the decay strategy for the current activation.
     */
	public void setDecayStrategy(DecayStrategy strategy);
	
	/**
	 * Gets the decay strategy
	 * @return the decay strategy.
	 */
	public DecayStrategy getDecayStrategy();
}
