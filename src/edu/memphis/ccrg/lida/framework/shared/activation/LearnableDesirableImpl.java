package edu.memphis.ccrg.lida.framework.shared.activation;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.TotalValueStrategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * The default implementation of {@link LearnableDesirable}.
 * @author Ryan J. McCall
 */
public class LearnableDesirableImpl extends DesirableImpl implements LearnableDesirable {

	private static final Logger logger = Logger.getLogger(LearnableDesirableImpl.class.getCanonicalName());
	private static final ElementFactory factory = ElementFactory.getInstance();
	private double baseLevelDesirability;
	private ExciteStrategy baseLevelExciteStrategy;
	private TotalValueStrategy totalValueStrategy;

	/**
	 * Constructs a new {@link LearnableDesirableImpl}.
	 */
	public LearnableDesirableImpl(){
		baseLevelDesirability = DEFAULT_BASE_LEVEL_DESIRABILITY;
		baseLevelExciteStrategy = factory.getDefaultExciteStrategy();
		totalValueStrategy = factory.getDefaultTotalValueStrategy();
	}
	
	@Override
	public void init(){
		super.init();
		baseLevelDesirability = getParam("learnableDesirable.initialBaseLevelDesirability",DEFAULT_BASE_LEVEL_DESIRABILITY);
		String type = getParam("learnableDesirable.exciteStrategy",factory.getDefaultExciteType());
		baseLevelExciteStrategy = factory.getExciteStrategy(type);
		type = getParam("learnableDesirable.totalValueStrategy",factory.getDefaultTotalValueStrategyType());
		totalValueStrategy = (TotalValueStrategy) factory.getStrategy(type);
		if(totalValueStrategy == null){
			totalValueStrategy = factory.getDefaultTotalValueStrategy();
		}
	}
	
	@Override
	public void setBaseLevelDesirability(double d) {
		if(d < -1.0){
			synchronized (this) {
				baseLevelDesirability = -1.0;
			}
		}else if(d > 1.0){
			synchronized (this) {
				baseLevelDesirability = 1.0;
			}
		}else{
			synchronized (this) {
				baseLevelDesirability = d;
			}
		}
	}
	
	@Override
	public double getBaseLevelDesirability() {
		return baseLevelDesirability;
	}

	@Override
	public synchronized void setBaseLevelExciteStrategy(ExciteStrategy s) {
		baseLevelExciteStrategy = s;
	}

	@Override
	public ExciteStrategy getBaseLevelExciteStrategy() {
		return baseLevelExciteStrategy;
	}

	@Override
	public synchronized void setTotalValueStrategy(TotalValueStrategy s) {
		totalValueStrategy = s;
	}

	@Override
	public TotalValueStrategy getTotalValueStrategy() {
		return totalValueStrategy;
	}

	@Override
	public void reinforceBaseLevelDesirability(double a) {
		if (baseLevelExciteStrategy != null) {
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "Before reinforcement {1} has base-level desirability: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getBaseLevelDesirability()});
			}
			synchronized(this){
				baseLevelDesirability = baseLevelExciteStrategy.excite(getBaseLevelDesirability(), a);
			}
			if(logger.isLoggable(Level.FINEST)){
				logger.log(Level.FINEST, "After reinforcement {1} has base-level desirability: {2}",
							new Object[]{TaskManager.getCurrentTick(),this,getBaseLevelDesirability()});
			}
		}	
	}
	
	@Override
	public double getTotalDesirability() { 
	    return (totalValueStrategy == null)? 0.0:
	    	totalValueStrategy.calculateTotalValue(getBaseLevelDesirability(), getDesirability());
	}
}