package edu.memphis.ccrg.lida.perception;

public class BasicExciteBehavior implements ExciteBehavior {

	public double excite(double currentActivation, double excitation) {
		return currentActivation + excitation;
	}

}
