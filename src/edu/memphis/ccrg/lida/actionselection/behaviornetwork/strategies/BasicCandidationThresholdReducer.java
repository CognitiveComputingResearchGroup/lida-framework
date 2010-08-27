package edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies;


public class BasicCandidationThresholdReducer implements CandidateThresholdReducer {
	
    /**
	 * Percent to reduce the behavior activation threshold by if no behavior is selected
	 */
    private final double activationThresholdReduction  = 0.10;  

	@Override
	public double reduce(double behaviorActivationThreshold) {
		return behaviorActivationThreshold * (1.00 - activationThresholdReduction);
	}

}
