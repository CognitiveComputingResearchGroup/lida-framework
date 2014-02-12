package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.initialization.Initializable;

/**
 * A {@link DecayStrategy} which decays a value (incentive salience) from [-1, 1] down to 0. 
 * 
 * @author Ryan J. McCall
 *
 */
public class LinearIncentiveSalienceDecay extends StrategyImpl implements DecayStrategy {

	/*
	 * The default slope
	 */
	private static final double DEFAULT_M = 0.1;

	/*
	 * The slope of this linear curve.
	 */
	private double m;

	/**
	 * Creates a new instance of a linear curve. Values for slope and intercept are
	 * set to the default ones.
	 */
	public LinearIncentiveSalienceDecay() {
		m = DEFAULT_M;
	}

	/**
	 * If this method is overridden, this init() must be called first! i.e.
	 * super.init(); Will set parameters with the following names:<br/>
	 * <br/>
	 * 
	 * <b>m</b> slope of the excite function<br/>
	 * If any parameter is not specified its default value will be used.
	 * 
	 * @see Initializable
	 */
	@Override
	public void init() {
		m = getParam("m", DEFAULT_M);
	}

	/**
	 * Decays the current activation according to some internal decay function.
	 * 
	 * @param current
	 *            activation of the entity before decay.
	 * @param ticks
	 *            The number of ticks to decay.
	 * @param params
	 *            optionally accepts 1 double parameter specifying the slope of
	 *            decay ticks and activations.
	 * @return new activation
	 */
	@Override
	public double decay(double current, long ticks, Object... params) {
		double mm = m;
		if (params != null && params.length != 0) {
			mm = (Double) params[0];
		}
		return getUpdate(current, ticks, mm);
	}

	/**
	 * Decays the current activation according to some internal decay function.
	 * 
	 * @param current
	 *            activation of the entity before decay.
	 * @param ticks
	 *            how much time has passed since last decay
	 * @param params
	 *            optionally accepts 1 parameter specifying the slope of decay
	 *            ticks and activations.
	 * @return new activation amount
	 */
	@Override
	public double decay(double current, long ticks,
			Map<String, ? extends Object> params) {
		double mm = m;
		if (params != null && params.containsKey("m")) {
			mm = (Double) params.get("m");
		}
		return getUpdate(current, ticks, mm);
	}

	/*
	 * To calculate activation value of decay operation by linear strategy
	 * 
	 * @param currentActivation current activation
	 * 
	 * @param ticks parameter of ticks
	 * 
	 * @param mm parameter of slope (default value is 0.1)
	 * 
	 * @return Calculated activation value
	 */
	private static double getUpdate(double current, long ticks, double slope) {
		if(current > 0){
			current -= slope*ticks;
			if(current < 0){ //If the decrease switches signs, set to 0.
				current = 0;
			}
		}else if(current < 0){
			current += slope*ticks;
			if(current > 0){ //If the increase switches signs, set to 0.
				current = 0;
			}
		}
		return current;
	}
}
