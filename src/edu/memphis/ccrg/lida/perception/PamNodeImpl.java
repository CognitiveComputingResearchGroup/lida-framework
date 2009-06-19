package edu.memphis.ccrg.lida.perception;

import java.util.Map;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

public class PamNodeImpl implements PamNode{

	public static final int DEFAULT_DEPTH = -10;
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
	protected double totalActivation = 0.0;
	protected double baselevelActivation = 0.0;
	protected double currentActivation = 0.0;
	protected long id;
	protected String label;
	protected int type = 0;
	protected ExciteBehavior exciteBehavior;
	protected DecayBehavior decayBehav;
	
	private PamNode groundingPamNode = this;
	private int layerDepth = DEFAULT_DEPTH;

	public PamNodeImpl() {
		super();
	}

	public PamNodeImpl(PamNodeImpl pamNodeImpl) {
		//super(pamNodeImpl);
	}

	/**
	 * Adds this node's current, baseLevel, and residual activation to total
	 * activation. Also updates activation buffers. This method should only
	 * be invoked when activation passing for this cycle is complete.
	 */
	public void synchronize() {    	 	
		if((currentActivation + baselevelActivation) > maxActivation)
			totalActivation = maxActivation;
		else
			totalActivation = currentActivation + baselevelActivation;   
	}

	/**
	 * Determines if this node has been synchronized. This is done by comparing
	 * the total activation of this node, to the activation lowe bound.
	 * 
	 * @return  <code>true</code> if this node has been synchronized.
	 * @see     Parameters#ACTIVATION_LOWERBOUND
	 */
	public boolean hasBeenSynchronized() {
	    return totalActivation != MIN_ACTIVATION;
	}

	/**
	 * The current activation of this node is increased by the
	 * excitation value.
	 * 
	 * @param   excitation the value to be added to the current activation of
	 *          this node
	 */
	public void excite(double excitation){
	    currentActivation = exciteBehavior.excite(currentActivation, excitation);
	}

	/**
	 * 
	 * @param
	 */
	public void setExciteBehavior(ExciteBehavior behavior) {
		exciteBehavior = behavior;
	}

	/**
	 * 
	 */
	public void decay() {
	    currentActivation = decayBehav.decay(currentActivation);
	}

	public void decay(DecayBehavior db) {
		currentActivation = db.decay(currentActivation);   	
	}

	/**
	 * 
	 * @param b
	 */
	public void setDecayBehav(DecayBehavior b) {
		decayBehav = b;		
	}

	/**
	  * Determines if this node is relevant. A node is relevant if its total
	  * activation is greater or equal to the selection threshold.
	  * 
	  * @return     <code>true</code> if this node is relevant
	  * @see        #selectionThreshold
	  */
	public boolean isRelevant() {    	
	    return (totalActivation >= selectionThreshold);
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
	}//private void updateMaxActivation()

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
		return id == other.id && type == other.type;
	}

	/**
	 * 
	 */
	public int hashCode() { 
	    int hash = 1;
	    Integer i = new Integer(type);
	    Long id = new Long(this.id);
	    
	    hash = hash * 31 + id.hashCode();
	    hash = hash * 31 + (i == null ? 0 : i.hashCode());
	    return hash;
	}

	/** 
	 * Returns the label
	 * @return label
	 */
	public String getLabel() {
	    return label;
	}

	/**
	    * Standard getter for importance.
	    * @return a double value for importance between 0 and 1.
	    */
	public double getImportance() {
	       return importance;
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

	public double getActivation() {
	    return currentActivation;
	}

	public double getTotalActivation() {
	    return totalActivation;
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

	public long getId() {
		return id;
	}

	public void setDecayBehavior(DecayBehavior b) {
		decayBehav = b;		
	}

	public Node copy() {
		return new PamNodeImpl(this);
	}

	public DecayBehavior getDecayBehavior() {
		return decayBehav;
	}

	public ExciteBehavior getExciteBehavior() {
		return exciteBehavior;
	}

	public PamNode getReferencedNode() {
		return this;
	}

	public void setActivation(double d) {
		currentActivation = d;
	}

	/**
	 * TODO: Is this applicable for pam nodes?
	 */
	public void setReferencedNode(PamNode n) {
		//do nothing
	}

	public void setId(long id) {
		this.id = id;		
	}

	public void setLabel(String label) {
		this.label = label;		
	}

	public String getIds() {
		return ""+id;
	}

	public int getLayerDepth() {
		return layerDepth ;
	}

	public void setLayerDepth(int d) {
		layerDepth = d;
	}

	public void printActivationString() {
		System.out.println(id + " total activation: " + totalActivation);	
	}//method

}//class