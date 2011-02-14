package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

/**
 * Default implementation of sigmoid decay.  Uses two parameters in activation calculation.
 * Can pass these parameters when the strategy is initialized. (see factoriesData.xml).
 * Alternatively, parameters can be passed in the decay method call.  
 * Formula used: 1 / (1 + exp(-a* x + c))
 * @author Javier Snaider & Ryan
 *
 */
public class SigmoidDecayStrategy extends StrategyImpl implements DecayStrategy {

	private static final double DEFAULT_A = 1.0;
	private double a = DEFAULT_A;
	
	private static final double DEFAULT_C = 0.0;
	private double c = DEFAULT_C;
	
	@Override
	public void init() {
		a = (Double) getParam("a", DEFAULT_A);
		c = (Double) getParam("c", DEFAULT_C);
	}
	
	@Override
	public double decay(double currentActivation, long ticks, Object... params) {
		double aa = a;
		double cc = c;
		if(params.length != 0){
			aa = (Double) params[0];
			cc = (Double) params[1];
		}
		return calcActivation(currentActivation, ticks, aa, cc);
	}

	//TODO test boundary conditions 1, 0.
	@Override
	public double decay(double currentActivation, long ticks,
			Map<String, ? extends Object> params) {
		double aa = a;
		double cc = c;
		if(params != null){
			aa = (Double) params.get("a");
			cc = (Double) params.get("c");
		}
		return calcActivation(currentActivation, ticks, aa, cc);
	}
	
	private double calcActivation(double curActiv, long ticks,
			double aa, double cc) {
		double curExcitation = -(Math.log((1 - curActiv)/curActiv) + cc) / aa - ticks;
		return 1/(1 + Math.exp(-aa * curExcitation + cc));
	}

}
