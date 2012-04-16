package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

/**
 * An object that can be excited according to an {@link ExciteStrategy}.
 * @author Ryan J. McCall
 */
public interface Excitable extends Decayable {
	
	/**
	 * Excites this object a specified amount according to its {@link ExciteStrategy}.
	 * 
	 * @param a the amount of excitation
	 */
    public void excite(double a); 
    
    /**
     * Sets the {@link ExciteStrategy}.
     * @param s the {@link ExciteStrategy} used to excite this object
     */
	public void setExciteStrategy(ExciteStrategy s);
	
	/**
	 * Gets the {@link ExciteStrategy}.
	 * @return the {@link ExciteStrategy} used to excite this object
	 */
	public ExciteStrategy getExciteStrategy();
}