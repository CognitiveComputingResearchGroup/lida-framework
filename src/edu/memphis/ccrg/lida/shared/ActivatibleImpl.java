package edu.memphis.ccrg.lida.shared;

import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
/**
 * generic Activatible Implementation. Useful to inherit from it 
 * Activatible classes like nodes or codelets.
 *  
 * @author Javier Snaider
 * 
 */

public class ActivatibleImpl implements Activatible {
	private double activation;
	private ExciteBehavior eb;
	private DecayBehavior db;
	
	public void decay() {
		if (db != null) {
			activation = db.decay(activation);
		}
	}

	public void excite(double excitation) {
		if (eb != null) {
			activation = eb.excite(activation, excitation);
		}
	}

	public double getActivation() {
		return activation;
	}

	public DecayBehavior getDecayBehavior() {
		return db;
	}

	public ExciteBehavior getExciteBehavior() {
		return eb;
	}

	public void setActivation(double d) {
		this.activation = d;
	}

	public void setDecayBehavior(DecayBehavior db) {
		this.db = db;
	}

	public void setExciteBehavior(ExciteBehavior eb) {
		this.eb = eb;
	}

}
