package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalValueStrategy;

/**
 * A {@link Desirable} with a learned component.
 * @author Ryan J. McCall
 */
public interface LearnableDesirable extends Desirable {
	
	/**
	 * Default base-level desirability for {@link Desirable}.
	 */
	public static final double DEFAULT_BASE_LEVEL_DESIRABILITY = 0.0;

	/**
	 * Get base-level desirability. 
	 * @return the learned desirability of this object
	 */
	public double getBaseLevelDesirability();
	
	/**
	 * Set base-level desirability. 
	 * @param d new base-level desirability 
	 */
	public void setBaseLevelDesirability(double d);
	
	/**
	 * Reinforces learned desirability specified amount.
	 * @param a desirability to reinforce with 
	 */
	public void reinforceBaseLevelDesirability(double a);
    
    /**
     * Sets base-level {@link ExciteStrategy}.
     * @param s the {@link ExciteStrategy} used to reinforce the learned desirability
     */
	public void setBaseLevelExciteStrategy(ExciteStrategy s);
	
	/**
	 * Gets base-level {@link ExciteStrategy}.
	 * @return the {@link ExciteStrategy} used to reinforce the learned desirability
	 */
	public ExciteStrategy getBaseLevelExciteStrategy();
	
	/**
	 * Returns {@link TotalValueStrategy}
	 * @return {@link TotalValueStrategy} used to calculate total desirability
	 */
	public TotalValueStrategy getTotalValueStrategy();
	
	/**
	 * Sets {@link TotalValueStrategy}
	 * @param s {@link TotalValueStrategy} used to calculate total desirability
	 */
	public void setTotalValueStrategy(TotalValueStrategy s);
}
