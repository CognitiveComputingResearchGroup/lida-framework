package edu.memphis.ccrg.lida.framework.shared.activation;

/**
 * Default method to calculate total activation.  Sums activations returning sum or 1.0, whichever is lower.
 * @author Ryan J. McCall
 *
 */
public class DefaultTotalActivationStrategy implements TotalActivationStrategy {

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.TotalActivationStrategy#calculateTotalActivation(double, double)
	 */
	@Override
	public double calculateTotalActivation(double baseLevelActivation,
			double currentActivation) {
		double sum = baseLevelActivation + currentActivation;
		return (sum > 1.0) ? 1.0 : sum;
	}

}
