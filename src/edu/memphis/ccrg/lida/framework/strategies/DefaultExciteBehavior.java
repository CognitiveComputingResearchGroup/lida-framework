package edu.memphis.ccrg.lida.framework.strategies;


public class DefaultExciteBehavior implements ExciteBehavior {

	public double excite(double currentActivation, double excitation) {
		return currentActivation + excitation;
	}

}
