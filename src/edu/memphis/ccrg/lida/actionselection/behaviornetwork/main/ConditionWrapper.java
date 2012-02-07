package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Map;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;


/**
 * A wrapper for {@link Condition}. Used with negated context conditions.
 * @author ?
 */
public class ConditionWrapper implements Condition {

	private static final Logger logger = Logger.getLogger(ConditionWrapper.class.getCanonicalName());
	
	private Condition c;
	private double weight;

	/**
	 * @param c the Condition to wrap
	 */
	public ConditionWrapper(Condition c){
		this.c = c;
	}
	
	@Override
	public double getActivation() {
		return c.getActivation();
	}

	@Override
	public void setActivation(double activation) {
		c.setActivation(activation);
	}

	@Override
	public double getTotalActivation() {
		return c.getTotalActivation();
	}

	@Override
	public void excite(double amount) {
		c.excite(amount);
	}

	@Override
	public void setExciteStrategy(ExciteStrategy strategy) {
		c.setExciteStrategy(strategy);
	}

	@Override
	public ExciteStrategy getExciteStrategy() {
		return c.getExciteStrategy();
	}

	@Override
	public void decay(long ticks) {
		c.decay(ticks);
	}

	@Override
	public void setDecayStrategy(DecayStrategy strategy) {
		c.setDecayStrategy(strategy);
	}

	@Override
	public DecayStrategy getDecayStrategy() {
		return c.getDecayStrategy();
	}

	@Override
	public Object getConditionId() {
		return c.getConditionId();
	}

	//TODO review
//	@Override
//	public double getDesirability() {
//		return c.getDesirability();
//	}
//
//	@Override
//	public double getNetDesirability() {
//		return c.getNetDesirability();
//	}
//	@Override
//	public void setDesirability(double d) {
//		c.setDesirability(d);
//	}

	@Override
	public void setWeight(double weight) {
		this.weight = weight;
	}

	@Override
	public double getWeight() {
		return weight;
	}
	
//	public Condition clone(){
//		Object out = null;
//		try {
//			 out=super.clone();
//		} catch (CloneNotSupportedException e) {
//			logger.log(Level.WARNING,null,e);
//		}
//		return (Condition) out;
//	}
	
	@Override
	public String toString(){
		return c.toString();
	}
	
	@Override
	public boolean equals (Object o){
		if (o instanceof Condition){
			return getConditionId().equals(((Condition)o).getConditionId());
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return getConditionId().hashCode();
	}
	
	@Override
	public double getActivatibleRemovalThreshold() {		
		return c.getActivatibleRemovalThreshold();
	}
	@Override
	public boolean isRemovable() {	
		return c.isRemovable();
	}
	@Override
	public void setActivatibleRemovalThreshold(double threshold) {
		c.setActivatibleRemovalThreshold(threshold);
		
	}
	@Override
	public <T> T getParam(String name, T defaultValue) {
		return c.getParam(name, defaultValue);
	}
	@Override
	public Map<String, ?> getParameters() {
		return c.getParameters();
	}
	
	@Override
	public void init(Map<String, ?> parameters) {		
	}
	
	@Override
	public void init() {		
	}

	@Override
	public boolean containsParameter(String key) {
		return c.containsParameter(key);
	}	
}