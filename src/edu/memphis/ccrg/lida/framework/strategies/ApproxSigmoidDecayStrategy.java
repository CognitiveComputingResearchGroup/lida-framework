package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

/**
 * Approx implementation of sigmoid decay. Uses one parameters in activation calculation.
 * Can pass these parameters when the strategy is initialized. (see factoriesData.xml).
 * Alternatively, parameters can be passed in the decay method call.  
 * Formula used: Divides the curve into 3 parts: quadratic, linear, quadratic. 
 *
 * @author Javier Snaider & Ryan & Daqi
 *
 */
public class ApproxSigmoidDecayStrategy extends StrategyImpl implements DecayStrategy {

	private static final double DEFAULT_A = 1.0;
	private double a = DEFAULT_A;
	
	@Override
	public void init() {
		a = (Double) getParam("a", DEFAULT_A);
	}
	
	@Override
	public double decay(double currentActivation, long ticks, Object... params) {
		double aa = a;
		if(params.length != 0){
			aa = (Double) params[0];
		}
		return calcActivation(currentActivation, ticks, aa);
	}

	//TODO test boundary conditions 1, 0.
	@Override
	public double decay(double currentActivation, long ticks,
			Map<String, ? extends Object> params) {
		double aa = a;

		if(params != null){
			aa = (Double) params.get("a");
		}
		return calcActivation(currentActivation, ticks, aa);
	}
	
	public double calcActivation(double curActiv, long ticks, double aa) {
		double curExcitation = 0;
		double newExcitation = 0;
		double newActiv = 0;
		
		double a1 = 0.0144, b1 = 0.1458, c1 = 0.3759, k = 0.21429, 
		       a2 = -0.0144, b2 = 0.1458, c2 = 0.6241;
		
		//Step 1-3: curActiv -> curExcitation
		if (curActiv <= 0.007)
			curExcitation = -5;
		else if (curActiv < 0.2)
			curExcitation = ((0 - b1) + Math.sqrt(b1*b1 - 4*a1*(c1 - curActiv)))/(2*a1);
		else if (curActiv <= 0.8)
			curExcitation = (curActiv - 0.5)/k;
		else if (curActiv < 0.993)
			curExcitation = ((0 - b2) + Math.sqrt(b2*b2 - 4*a2*(c2 - curActiv)))/(2*a2);
		else 
			curExcitation = 5;
		
		//Step 2-3: curExcitation -> newExcitation
		newExcitation = curExcitation - aa*ticks;
		
		//Step 3-3: newExcitation -> newActiv
		if (newExcitation <= -5)
			newActiv = 0.007;
		else if (newExcitation < -1.4)
			newActiv = a1*newExcitation*newExcitation + b1*newExcitation + c1;
		else if (newExcitation <= 1.4)
			newActiv = k*newExcitation + 0.5;
		else if (newExcitation < 5)
			newActiv = a2*newExcitation*newExcitation + b2*newExcitation + c2;
		else
			newActiv = 0.993;
		
		return newActiv;	
	}

}
