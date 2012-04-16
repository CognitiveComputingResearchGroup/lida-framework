package edu.memphis.ccrg.lida.framework.shared.activation;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.TaskManager;


/**
 * The default implementation of {@link Desirable}.
 * @author Ryan J. McCall
 *
 */
public class DesirableImpl extends ExcitableImpl implements Desirable {

	private static final Logger logger = Logger.getLogger(DesirableImpl.class.getCanonicalName());
	private double desirability;

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
	public void excite(double a) {
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
	public void decay(long t) {
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

	@Override
	public double getNetDesirability() {
		// TODO Auto-generated method stub
		return 0;
	}

}
