package edu.memphis.ccrg.lida.shared;

import edu.memphis.ccrg.lida.shared.strategies.DefaultExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.LinearDecayBehavior;
/**
 * generic Activatible Implementation. Useful to inherit from it 
 * Activatible classes like nodes or codelets.
 *  
 * @author Javier Snaider
 * 
 */

public class ActivatibleImpl implements Activatible {
	
	private double activation;
	private ExciteBehavior exciteBehavior;
	private DecayBehavior decayBehavior;

	public ActivatibleImpl() {
		activation = 0.0;
		exciteBehavior = new DefaultExciteBehavior();
		decayBehavior = new LinearDecayBehavior();
	}
	
	public ActivatibleImpl(double activation, ExciteBehavior eb, DecayBehavior db) {
		this.activation = activation;
		this.exciteBehavior = eb;
		this.decayBehavior = db;
	}

	public void decay() {
		if (decayBehavior != null) {
			synchronized(this){
				activation = decayBehavior.decay(activation);
			}
		}
	}

	public void excite(double excitation) {
		if (exciteBehavior != null) {
			synchronized(this){
				activation = exciteBehavior.excite(activation, excitation);
			}
		}
	}

	public double getActivation() {
		return activation;
	}

	public DecayBehavior getDecayBehavior() {
		return decayBehavior;
	}

	public ExciteBehavior getExciteBehavior() {
		return exciteBehavior;
	}

	public synchronized void setActivation(double d) {
		this.activation = d;
	}

	public synchronized void setDecayBehavior(DecayBehavior db) {
		this.decayBehavior = db;
	}

	public synchronized void setExciteBehavior(ExciteBehavior eb) {
		this.exciteBehavior = eb;
	}

}//
