package edu.memphis.ccrg.lida.framework.strategies;

/**
 * A strategy that calculates total activation.
 * @author Ryan J. McCall
 */
public interface TotalActivationStrategy extends Strategy {

	/**
	 * Calculates and returns total activation.
	 * @param bla Base-level activation
	 * @param ca current activation
	 * @return calculated total activation
	 */
	public double calculateTotalActivation(double bla, double ca);
	
}
