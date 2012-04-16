package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalValueStrategy;

public class LearnedDesirableImpl extends DesirableImpl implements LearnedDesirable {

	private double baseLevelDesirability;
	private ExciteStrategy baseLevelExciteStrategy;
	private DecayStrategy baseLevelDecayStrategy;
	private TotalValueStrategy totalValueStrategy;

	@Override
	public void setBaseLevelDesirability(double d) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public double getBaseLevelDesirability() {
		return baseLevelDesirability;
	}

	@Override
	public ExciteStrategy getBaseLevelExciteStrategy() {
		return baseLevelExciteStrategy;
	}

	@Override
	public TotalValueStrategy getTotalActivationStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setBaseLevelExciteStrategy(ExciteStrategy s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setTotalActivationStrategy(TotalValueStrategy s) {
		// TODO Auto-generated method stub

	}
}