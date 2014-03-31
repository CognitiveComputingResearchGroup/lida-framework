package edu.memphis.ccrg.lida.framework.strategies;


/**
 * Computes total activation as a weighted average of base-level and current.
 * @author Ryan J. McCall
 */
public class WeightedTotalActivationStrategy extends StrategyImpl implements TotalActivationStrategy {

	
	private static final double DEFAULT_BLA_WEIGHT = 0.1;
	private double blaWeight;
	
	@Override
	public void init(){
		blaWeight = getParam("blaWeight",DEFAULT_BLA_WEIGHT);
	}
	
	@Override
	public double calculateTotalActivation(double baseLevelActivation, double currentActivation) {
		double sum = (blaWeight*baseLevelActivation + (1-blaWeight)*currentActivation)/2;
		return (sum > 1.0)? 1.0 : sum;
	}

}
