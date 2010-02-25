package edu.memphis.ccrg.lida.framework.shared;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * generic Activatible Implementation. Useful to inherit from it 
 * Activatible classes like nodes or codelets.
 *  
 * @author Javier Snaider
 * 
 */
public class ActivatibleImpl implements Activatible {
	
	private double activation;
	private ExciteStrategy exciteStrategy;
	private DecayStrategy decayStrategy;
	private static Logger logger = Logger.getLogger("lida.framework.shared.ActivatibleImpl");

	public ActivatibleImpl() {
		activation = 0.0;
		exciteStrategy = new DefaultExciteStrategy();
		decayStrategy = new LinearDecayStrategy();
	}
	
	public ActivatibleImpl(double activation, ExciteStrategy eb, DecayStrategy db) {
		this.activation = activation;
		this.exciteStrategy = eb;
		this.decayStrategy = db;
	}

	public void decay(long ticks) {	
		if (decayStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before decay has " + activation,LidaTaskManager.getActualTick());
			synchronized(this){
				activation = decayStrategy.decay(activation,ticks);
			}
			logger.log(Level.FINEST,this.toString() + " after decay has " + activation,LidaTaskManager.getActualTick());
		}
	}

	public void excite(double excitation) {	
		if (exciteStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before excite has " + activation,LidaTaskManager.getActualTick());
			//System.out.println(this.toString() + " before excite has " + activation);
			synchronized(this){
				activation = exciteStrategy.excite(activation, excitation);
			}
			logger.log(Level.FINEST,this.toString() + " after excite has " + activation,LidaTaskManager.getActualTick());
			//System.out.println(this.toString() + " after excite has " + activation + "\n");
		}
	}

	public double getActivation() {
		return activation;
	}

	public DecayStrategy getDecayStrategy() {
		return decayStrategy;
	}

	public ExciteStrategy getExciteStrategy() {
		return exciteStrategy;
	}

	public synchronized void setActivation(double d) {
		this.activation = d;
	}

	public synchronized void setDecayStrategy(DecayStrategy db) {
		this.decayStrategy = db;
	}

	public synchronized void setExciteStrategy(ExciteStrategy eb) {
		this.exciteStrategy = eb;
	}

}//
