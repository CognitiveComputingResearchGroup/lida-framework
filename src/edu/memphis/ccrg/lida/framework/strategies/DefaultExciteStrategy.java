package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

public class DefaultExciteStrategy implements ExciteStrategy {
	
	@SuppressWarnings("unused")
	private Map<String, ? extends Object> params;

	public double excite(double currentActivation, double excitation) {
		double newActivation = currentActivation + excitation;
		if(newActivation > 1.0)
			return 1.0;
		else 
			return newActivation;
	}

	public void init(Map<String, ? extends Object> params) {
		this.params=params;
	}

}
