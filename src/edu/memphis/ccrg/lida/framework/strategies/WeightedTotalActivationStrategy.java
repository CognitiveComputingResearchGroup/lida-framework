package edu.memphis.ccrg.lida.framework.strategies;


/**
 * Some proportion of base-level activation and some proportion of current activation 
 * contribute to total activation.
 * @author Ryan J. McCall
 */
public class WeightedTotalActivationStrategy extends StrategyImpl implements TotalActivationStrategy {

	
	private static final double DEFAULT_BASE_LEVEL_WEIGHT = 1.0;
	private double baseLevelWeight;
	private static final double DEFAULT_CURRENT_WEIGHT = 1.0;
	private double currentWeight;
	
	@Override
	public void init(){
		baseLevelWeight = getParam("baseLevelWeight", DEFAULT_BASE_LEVEL_WEIGHT);
		currentWeight = getParam("currentWeight", DEFAULT_CURRENT_WEIGHT);
	}
	
	@Override
	public double calculateTotalActivation(double baseLevelActivation, double currentActivation) {
		double total = (baseLevelWeight*baseLevelActivation + currentWeight*currentActivation)/2;
		return total>1? 1: total;
	}	
}
