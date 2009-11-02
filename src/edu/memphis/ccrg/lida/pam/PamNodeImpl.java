package edu.memphis.ccrg.lida.pam;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeType;

public class PamNodeImpl extends NodeImpl implements PamNode{
	
	protected final double MIN_ACTIVATION = 0.0;
	protected final double MAX_ACTIVATION = 1.0;
	
	/** Activation required for node to be part of the percept.
	 *  Bounded by minActivation and maxActivation
	 */
	protected double selectionThreshold = 0.9;
	
	/**
	 * Specifies the relative importance of a Node. Only relevant for nodes
	 * that represent feelings. Lies between 00.d and 1.0d inclusive.
	 */
	protected double importance = 0.0;
	protected double baseLevelActivation = 0.0;
	protected NodeType type;
	
	public PamNodeImpl() {
		super();
		refNode = this;
	}

	public PamNodeImpl(PamNodeImpl p) {
		super(p);
		selectionThreshold = p.selectionThreshold;
		importance = p.importance;
		baseLevelActivation = p.baseLevelActivation;
		type = p.type;
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
			importance = (Double)o;
		
		o = values.get("baselevelactivation");
		if ((o != null)&& (o instanceof Double)) 
			baseLevelActivation = (Double)o;		
	}//method

	/**
	 * @param n
	 */
	public boolean equals(Object obj) {
		if(!(obj instanceof PamNodeImpl))
			return false;
		PamNodeImpl other = (PamNodeImpl)obj;
		return getId() == other.getId() && type == other.type;
	}

	/**
	 * 
	 */
	public int hashCode() { 
	    int hash = 1;
	    Integer i = new Integer(type.getValue());
	    Long id =  getId();
	    
	    hash = hash * 31 + id.hashCode();
	    hash = hash * 31 + (i == null ? 0 : i.hashCode());
	    return hash;
	}

	/**
	 * returns selection threshold
	 * @return Selection threshold
	 */
	public double getSelectionThreshold() {
	    return selectionThreshold;
	}

	public double getBaselevelActivation() {
	    return baseLevelActivation;
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

	//TODO: Remove.
	public Node copy() {
		return new PamNodeImpl(this);
	}

	public void printActivationString() {
		System.out.println(getId() + " total activation: " + getTotalActivation());	
	}//method
	
}//class