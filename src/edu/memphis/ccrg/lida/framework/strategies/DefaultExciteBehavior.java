package edu.memphis.ccrg.lida.framework.strategies;


public class DefaultExciteBehavior implements ExciteBehavior {

	public double excite(double currentActivation, double excitation) {
		double newActivation = currentActivation + excitation;
		if(newActivation > 1.0)
			return 1.0;
		else 
			return newActivation;
	}

}
