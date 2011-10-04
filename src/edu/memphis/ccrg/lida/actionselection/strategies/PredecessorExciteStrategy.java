package edu.memphis.ccrg.lida.actionselection.strategies;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.StrategyImpl;


public class PredecessorExciteStrategy extends StrategyImpl implements ExciteStrategy {

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
			Map<String, ? extends Object> params) {
		// TODO Auto-generated method stub
		return 0;
	}
}
