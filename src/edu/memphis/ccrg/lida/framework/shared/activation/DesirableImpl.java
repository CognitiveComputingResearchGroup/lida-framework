package edu.memphis.ccrg.lida.framework.shared.activation;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.initialization.InitializableImpl;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;


/**
 * The default implementation of {@link Desirable}.
 * @author Ryan J. McCall
 *
 */
public class DesirableImpl extends InitializableImpl implements Desirable {

	private static final Logger logger = Logger.getLogger(DesirableImpl.class.getCanonicalName());
	private static final ElementFactory factory = ElementFactory.getInstance();
	
	private double desirability;
	private ExciteStrategy exciteStrategy;
	private DecayStrategy decayStrategy;
	
	/**
	 * Constructs a new {@link DesirableImpl}.
	 */
	public DesirableImpl(){
		desirability = DEFAULT_DESIRABILITY;
		decayStrategy = factory.getDefaultDecayStrategy();
		exciteStrategy = factory.getDefaultExciteStrategy();
	}
	
	@Override
	public void init(){
		desirability = getParam("desirable.initialDesirability",DEFAULT_DESIRABILITY);
		String type = getParam("desirable.decayStrategy",factory.getDefaultDecayType());
		decayStrategy = factory.getDecayStrategy(type);
		type = getParam("desirable.exciteStrategy",factory.getDefaultExciteType());
		exciteStrategy = factory.getExciteStrategy(type);
	}
	
	@Override
	public DecayStrategy getDesirabilityDecayStrategy() {
		return decayStrategy;
	}
	
	@Override
	public void setDesirabilityDecayStrategy(DecayStrategy d) {
		decayStrategy = d;
	}
	
	@Override
	public void setDesirabilityExciteStrategy(ExciteStrategy s){
		exciteStrategy = s;
	}
	
	@Override
	public ExciteStrategy getDesirabilityExciteStrategy(){
		return exciteStrategy;
	}

	@Override
	public void setDesirability(double d) {
		if(d < -1.0){
			synchronized (this) {
				desirability = -1.0;
			}
		}else if(d > 1.0){
			synchronized (this) {
				desirability = 1.0;
			}
		}else{
			synchronized (this) {
				desirability = d;
			}
		}
	}
	
	@Override
	public double getDesirability() {
		return desirability;
	}

	@Override
	public double getTotalDesirability() {
		return getDesirability();
	}

	@Override
	public void exciteDesirability(double a) {
		if (exciteStrategy != null) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "Before excitation {1} has current desirability: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getDesirability()});
			}
			synchronized(this){
				desirability = exciteStrategy.excite(getDesirability(), a);
			}
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "After excitation {1} has current desirability: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getDesirability()});
			}
		}
	}

	@Override
	public void decayDesirability(long t) {
		if (decayStrategy != null) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "Before decaying {1} has current activation: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getDesirability()});
			}
			synchronized(this){
				desirability = decayStrategy.decay(getDesirability(),t);
			}
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "After decaying {1} has current activation: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getDesirability()});
			}
		}
	}
}