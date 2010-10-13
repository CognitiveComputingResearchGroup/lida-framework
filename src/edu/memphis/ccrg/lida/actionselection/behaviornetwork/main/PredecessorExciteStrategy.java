package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

public class PredecessorExciteStrategy implements ExciteStrategy {

	@Override
	public double excite(double currentActivation, double excitation, Object... params) {
		double initialActivation = (Double) params[0];
		double predecessorFactor = (Double) params[1];
		double normalizationFactor = (Double) params[2];
		
		double newActivation = currentActivation + (initialActivation * predecessorFactor) / normalizationFactor;
		if(newActivation > 1.0)
			return 1.0;
		if(newActivation < 0.0)
			return 0.0;
		return newActivation;
	}

	@Override
	public double excite(double currentActivation, double excitation,
			Map<String, Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setIntercept(int b) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setSlope(int m) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(Map<String, ? extends Object> parameters) {
		// TODO Auto-generated method stub

	}

}
