package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.Map;

/**
 * A behavior which calculates a new activation amount based on some parameters.
 * @author ryanjmccall
 *
 */
public interface ActivationCalculationBehavior {
	
	
	public void setParams(Map<String, Object> params);
	
	/**
	 * 
	 * @param initialActivation - source activation
	 * @param normalizationFactor - normalizing factor
	 * @return new activation amount based on initialActivation and normalizationFactory
	 */
	public double calculateActivation(double initialActivation, double normalizationFactor);
	
}
