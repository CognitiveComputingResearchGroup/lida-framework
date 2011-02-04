package edu.memphis.ccrg.lida.framework.shared.activation;

/**
 * A strategy that calculates total activation.
 * @author Ryan J. McCall
 */
public interface TotalActivationStrategy {

	/**
	 * Calculates and returns total activation.
	 * @param baseLevelActivation
	 * @param currentActivation
	 * @return
	 */
	public double calculateTotalActivation(double baseLevelActivation, double currentActivation);
	
}
