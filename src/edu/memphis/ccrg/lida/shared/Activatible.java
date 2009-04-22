package edu.memphis.ccrg.lida.shared;

import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

/**
 * 
 * @author ryanjmccall
 *
 */
public interface Activatible {
	
	public double getActivation();
    public void setActivation(double amount);
    
    public void excite(double amount); 
	public void setExciteBehavior(ExciteBehavior behavior);
	public ExciteBehavior getExciteBehavior();
	
	public void decay();	
	public void setDecayBehavior(DecayBehavior c);
	public DecayBehavior getDecayBehavior();

}
