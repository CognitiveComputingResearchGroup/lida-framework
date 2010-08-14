package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;


public class BasicThetaReductionStrategy implements ThetaReductionStrategy {

	@Override
	public double reduce(double behaviorActivationThreshold, double activationThresholdReduction) {

		return behaviorActivationThreshold - (behaviorActivationThreshold * (activationThresholdReduction / 100));
	}

}
