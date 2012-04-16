package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

public class DesirableImpl implements Desirable {

	@Override
	public void excite(double a) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setExciteStrategy(ExciteStrategy s) {
		// TODO Auto-generated method stub

	}

	@Override
	public ExciteStrategy getExciteStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void decay(long ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setDecayStrategy(DecayStrategy strategy) {
		// TODO Auto-generated method stub

	}

	@Override
	public DecayStrategy getDecayStrategy() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getDesirability() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setDesirability(double d) {
		// TODO Auto-generated method stub

	}

	@Override
	public double getTotalDesirability() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getNetDesirability() {
		// TODO Auto-generated method stub
		return 0;
	}

}
