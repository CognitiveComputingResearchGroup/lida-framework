package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;

/**
 * The default (abstract) implementation of {@link Decayable}.
 * @author Ryan J. McCall
 */
public abstract class DecayableImpl implements Decayable {
	
	/**
	 * The {@link DecayStrategy} used by this {@link Decayable}.
	 */
	protected DecayStrategy decayStrategy;

	@Override
	public DecayStrategy getDecayStrategy() {
		return decayStrategy;
	}
	
	@Override
	public void setDecayStrategy(DecayStrategy d) {
		decayStrategy = d;
	}

}
