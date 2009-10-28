package edu.memphis.ccrg.lida.pam;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;

public class PamNodeImpl extends NodeImpl implements PamNode{
	
	protected final int TYPE_UNDEF = -1;
	protected final int TYPE_OBJECT = 1;
	protected final int TYPE_CATEGORY = 2;
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
	protected double minActivation = 0.0;
	protected double maxActivation = 1.0;
	protected double baselevelActivation = 0.0;
	protected double currentActivation = 0.0;
	protected int type = 0;
	private PropagationBehavior propogationBehavior; //TODO: set get methods copy constructor
	
	public PamNodeImpl() {
		super();
		refNode=this;
	}

	public PamNodeImpl(PamNodeImpl p) {
		super(p);
		selectionThreshold = p.selectionThreshold;
		importance = p.importance;
		minActivation = p.minActivation;
		maxActivation = p.maxActivation;
		baselevelActivation = p.baselevelActivation;
		currentActivation = p.currentActivation;
		type = p.type;
	}

	/**
	 * Adds this node's current, baseLevel, and residual activation to total
	 * activation. Also updates activation buffers. This method should only
	 * be invoked when activation passing for this cycle is complete.
	 */
	public void synchronize() {		
		if((currentActivation + baselevelActivation) > maxActivation)
			setActivation(maxActivation);
		else
			setActivation(currentActivation + baselevelActivation);   
	}
	
	/**
	  * Determines if this node is relevant. A node is relevant if its total
	  * activation is greater or equal to the selection threshold.
	  * 
	  * @return     <code>true</code> if this node is relevant
	  * @see        #selectionThreshold
	  */
	public boolean isOverThreshold() {
		//System.out.println(getLabel() + " " + getActivation() + " " + selectionThreshold);
	    return getActivation() >= selectionThreshold;
	}

	/**
	 * 
	 * @param activ
	 */
	public void setMinActivation(double activ) {
		minActivation = activ;
	}

	/**
	 * 
	 * @param activ
	 */
	public void setMaxActivation(double activ) {
		maxActivation = activ; 
	}//method

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
			baselevelActivation = (Double)o;		
	}//public void setValue(Map<String, Object> values)

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
	    Integer i = new Integer(type);
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
	    return baselevelActivation;
	}

	public double getTotalActivation() {
	    return getActivation();
	}

	public double getMinActivation() {
	    return minActivation;
	    
	}

	public double getMaxActivation() {
	    return maxActivation;
	}

	public double getDefaultMaxActivation() {
		return MAX_ACTIVATION;
	}

	public double getDefaultMinActivation() {
		return MIN_ACTIVATION;
	}

	public Node copy() {
		return new PamNodeImpl(this);
	}

	public void printActivationString() {
		System.out.println(getId() + " total activation: " + getActivation());	
	}//method

	public double getActivationToPropagate(Map<String, Object> propagateParams) {
		return propogationBehavior.getActivationToPropagate(propagateParams);
	}

}//class