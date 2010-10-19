package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;


/**
 * 
 * @author Ryan J McCall
 *
 */
public class SigmoidExciteStrategy implements ExciteStrategy{

	private final int DEFAULT_M = 10;
	private final int DEFAULT_B = 0;
	
	private int m = DEFAULT_M;
	private int b = DEFAULT_B;
	
	@Override
	public void init(Map<String, ? extends Object> parameters) {
		m = (Integer) parameters.get("m");
		b = (Integer) parameters.get("b");
	}

	@Override
	public double excite(double currentActivation, double excitation, Object... params) {
		double newActivation = currentActivation + 1 / (1 + Math.exp(-excitation * m + b));
		return newActivation;
	}

	@Override
	public double excite(double currentActivation, double excitation,
			Map<String, ? extends Object> params) {
		// TODO Auto-generated method stub
		return 0;
	} 
	
}
