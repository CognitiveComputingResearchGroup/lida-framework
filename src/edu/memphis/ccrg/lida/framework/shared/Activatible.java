package edu.memphis.ccrg.lida.framework.shared;

import edu.memphis.ccrg.lida.framework.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.framework.strategies.ExciteBehavior;

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
	public void setExciteBehavior(ExciteBehavior behavior);
	public ExciteBehavior getExciteBehavior();
	
	public void decay(long ticks);	
	public void setDecayBehavior(DecayBehavior c);
	public DecayBehavior getDecayBehavior();

}
