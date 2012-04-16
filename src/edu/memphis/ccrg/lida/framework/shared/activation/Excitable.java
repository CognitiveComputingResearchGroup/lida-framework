package edu.memphis.ccrg.lida.framework.shared.activation;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

public interface Excitable {
	
    
	//TODO A common interface containing the next 6 methods for both activatible and desirable
	/**
	 * The current desirability of this node is increased 
	 * using the excitation value as a parameter for the {@link ExciteStrategy}. 
	 * 
	 * @param   a the value to be used to increase the current desirability of
	 *          this node
	 */
    public void excite(double a); 
    
    /**
     * Sets the excite strategy
     * @param s the Excite strategy for the current activation.
     */
	public void setExciteStrategy(ExciteStrategy s);
	/**
	 * Gets the excite strategy
	 * @return the excite strategy
	 */
	public ExciteStrategy getExciteStrategy();

}
