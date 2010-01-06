package edu.memphis.ccrg.lida.framework.shared;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteBehavior;
import edu.memphis.ccrg.lida.framework.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayBehavior;

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
	private static Logger logger = Logger.getLogger("lida.framework.shared.ActivatibleImpl");

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

	public void decay(long ticks) {	
		if (decayBehavior != null) {
			logger.log(Level.FINEST,this.toString() + " before decay has " + activation,LidaTaskManager.getActualTick());
			synchronized(this){
				activation = decayBehavior.decay(activation,ticks);
			}
			logger.log(Level.FINEST,this.toString() + " after decay has " + activation,LidaTaskManager.getActualTick());
		}
	}

	public void excite(double excitation) {	
		if (exciteBehavior != null) {
			logger.log(Level.FINEST,this.toString() + " before excite has " + activation,LidaTaskManager.getActualTick());
			//System.out.println(this.toString() + " before excite has " + activation);
			synchronized(this){
				activation = exciteBehavior.excite(activation, excitation);
			}
			logger.log(Level.FINEST,this.toString() + " after excite has " + activation,LidaTaskManager.getActualTick());
			//System.out.println(this.toString() + " after excite has " + activation + "\n");
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
