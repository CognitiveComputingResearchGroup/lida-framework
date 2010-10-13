package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;

import java.util.Map;

public class PredecessorActivationCalculator implements ActivationCalculationBehavior {

	private double predecessorFactor = 0.7;
	
	@Override
	public double calculateActivation(double initialActivation, double normalizationFactor) {
		return (initialActivation * predecessorFactor) / normalizationFactor;
	}

	@Override
	public void setParams(Map<String, Object> params) {
		Object o = params.get("predecessorFactor");
		if(o != null)
			predecessorFactor = (Double) o;
	}

}
