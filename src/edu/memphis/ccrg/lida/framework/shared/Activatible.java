package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

/**
 * 
 * @author ryanjmccall
 *
 */
public interface Activatible {
	
	public double getActivation();
    public void setActivation(double amount);
    
	/**
	 * The current activation of this node is increased by the
	 * excitation value.
	 * 
	 * @param   excitation the value to be added to the current activation of
	 *          this node
	 */
    public void excite(double amount); 
	public void setExciteStrategy(ExciteStrategy behavior);
	public ExciteStrategy getExciteStrategy();
	
	public void decay(long ticks);	
	public void setDecayStrategy(DecayStrategy c);
	public DecayStrategy getDecayStrategy();

}
