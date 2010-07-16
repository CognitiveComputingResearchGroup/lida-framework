package edu.memphis.ccrg.lida.pam;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class PamNodeImpl extends NodeImpl implements PamNode{
	
	protected static final double MIN_ACTIVATION = 0.0;
	protected static final double MAX_ACTIVATION = 1.0;
	
	/** Activation required for node to be part of the percept.
	 *  Bounded by minActivation and maxActivation
	 */
	protected double selectionThreshold = 0.9;
	
	/**
	 * Specifies the relative importance of a Node. Only relevant for nodes
	 * that represent feelings. Lies between 00.d and 1.0d inclusive.
	 */
	//protected double importance = 0.0;
	protected double baseLevelActivation = 0.0;
	
	private DecayStrategy baseLevelDecayStrategy = new LinearDecayStrategy();
	private ExciteStrategy baseLevelExciteStrategy = new DefaultExciteStrategy();
	private static Logger logger = Logger.getLogger("lida.framework.pam.PamNodeImpl");

	
	public PamNodeImpl() {
		super();
		refNode = this;
	}

	public PamNodeImpl(PamNodeImpl p) {
		super(p);
		selectionThreshold = p.selectionThreshold;
		setImportance(p.getImportance());
		//importance = p.importance;
		baseLevelActivation = p.baseLevelActivation;
	}

	/**
	 * Adds this node's current, baseLevel, and residual activation to total
	 * activation. Also updates activation buffers. This method should only
	 * be invoked when activation passing for this cycle is complete.
	 */
	public void synchronize() {		
		double currentActivation = getActivation();
		if((currentActivation + baseLevelActivation) < MAX_ACTIVATION)
			setActivation(currentActivation + baseLevelActivation); 
		else
			setActivation(MAX_ACTIVATION);
	}
	
	/**
	  * Determines if this node is relevant. A node is relevant if its total
	  * activation is greater or equal to the selection threshold.
	  * 
	  * @return     <code>true</code> if this node is relevant
	  * @see        #selectionThreshold
	  */
	public boolean isOverThreshold() {
	    return getTotalActivation() >= selectionThreshold;
	}

	/**
	 * 
	 * @param threshold
	 */
	public void setSelectionThreshold(double threshold) {
		selectionThreshold = threshold;
	}

	/**
	 * 
	 * @param values
	 */
	public void setValue(Map<String, Object> values) {
		Object o = values.get("importance");
		if ((o != null)&& (o instanceof Double)) 
			setImportance((Double)o);
			//importance = (Double)o;
		
		o = values.get("baselevelactivation");
		if ((o != null)&& (o instanceof Double)) 
			baseLevelActivation = (Double)o;		
	}//method

	/**
	 * returns selection threshold
	 * @return Selection threshold
	 */
	public double getSelectionThreshold() {
	    return selectionThreshold;
	}

	public double getTotalActivation() {
	    return getActivation() + baseLevelActivation;
	}

	public double getMaxActivation() {
		return MAX_ACTIVATION;
	}

	public double getMinActivation() {
		return MIN_ACTIVATION;
	}
	
	/**
	 * @param n
	 */
	public boolean equals(Object obj) {
		if(!(obj instanceof PamNodeImpl))
			return false;
		PamNodeImpl other = (PamNodeImpl)obj;
		return getId() == other.getId();
	}

	/**
	 * 
	 */
	public int hashCode() { 
	    int hash = 1;
	    Long id =  getId();
	    hash = hash * 31 + id.hashCode();
	    return hash;
	}

	public void printActivationString() {
		System.out.println(getId() + " total activation: " + getTotalActivation());	
	}//method
	
	public void decayBaseLevelActivation(long ticks) {
		if (baseLevelDecayStrategy != null) {
			logger.log(Level.FINEST,this.toString() + " before decay has a BaseLevelAct. of " + baseLevelActivation,LidaTaskManager.getActualTick());
			synchronized(this){
				baseLevelActivation = baseLevelDecayStrategy.decay(baseLevelActivation,ticks);
			}
			logger.log(Level.FINEST,this.toString() + " after decay has a BaseLevelAct. of " + baseLevelActivation,LidaTaskManager.getActualTick());
		}		
	}

	@Override
	public double getBaseLevelActivation() {
		return this.baseLevelActivation;
	}

	@Override
	public DecayStrategy getBaseLevelDecayStrategy() {
		return this.baseLevelDecayStrategy;
	}

	@Override
	public ExciteStrategy getBaseLevelExciteStrategy() {
		return this.baseLevelExciteStrategy;
	}

	@Override
	public void reinforceBaseLevelActivation(double amount) {
		baseLevelActivation = baseLevelExciteStrategy.excite(baseLevelActivation, amount);
	}

	@Override
	public void setBaseLevelActivation(double amount) {
		baseLevelActivation = amount;
	}

	@Override
	public void setBaseLevelDecayStrategy(DecayStrategy strategy) {
		baseLevelDecayStrategy = strategy;
	}

	@Override
	public void setBaseLevelExciteStrategy(ExciteStrategy strategy) {
		baseLevelExciteStrategy = strategy;		
	}

	
}//class