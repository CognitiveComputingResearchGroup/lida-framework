/*
 * @(#)Node.java  2.0  2/10/09
 *
 * This class implements a slipnet node.
 * 
 * @author Sidney D'Mello
 * @author Vivek Datla
 * @author Rodrigo Silva L. <rsilval@acm.org>
 *
 * Copyright 2006-2008 Cognitive Computing Research Group.
 * 365 Innovation Dr, Rm 303, Memphis, TN 38152, USA.
 * All rights reserved.
 */
package edu.memphis.ccrg.lida.perception;

import java.util.Map;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.strategies.BasicExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.LinearDecayCurve;
import edu.memphis.ccrg.lida.util.M;

public class PamNodeImpl implements PamNode{	
    protected final int        TYPE_UNDEF = -1;      
    protected final int        TYPE_OBJECT = 1;
    protected final int        TYPE_CATEGORY = 2;
    protected final double     MIN_ACTIVATION = 0.0;    
    protected final double     MAX_ACTIVATION = 1.0;
    
    /** Activation required for node to be part of the percept.
     *  Bounded by minActivation and maxActivation
     */
    private double selectionThreshold = 0.9;
    /**
     * Specifies the relative importance of a Node. Only relevant for nodes
     * that represent feelings. Lies between 00.d and 1.0d inclusive.
     */
    private double importance = 0.0;
    private double minActivation;
    private double maxActivation;        
    private double totalActivation;
    private double baselevelActivation;    
    private double currentActivation;      
    
    private int layerDepth; 
    private long nodeID;
    private String label;  // Human-readable name used to identify node         
    private int type;
    private ExciteBehavior exciteBehavior;
    private DecayBehavior decayBehav;
    
    /**
     * 
     * @param id
     * @param bla
     * @param ca
     * @param label
     * @param type
     */
    public PamNodeImpl(long id, double bla, double ca, String label, int type){    	
    	minActivation = MIN_ACTIVATION;
    	maxActivation = MAX_ACTIVATION;
    	totalActivation = bla + ca;  
        baselevelActivation = bla;
        currentActivation = ca;

        layerDepth = 0;
        nodeID = id; 
        this.label = label;                
        this.type = type;        
        exciteBehavior = new BasicExciteBehavior();
        decayBehav = new LinearDecayCurve();
    }//public Node(long id,...
    
    /**
     * 
     * @param n
     */
    public PamNodeImpl(PamNodeImpl n){
    	selectionThreshold = n.selectionThreshold;
    	importance = n.importance;
    	
    	minActivation = n.minActivation;
    	maxActivation = n.maxActivation;                   
        totalActivation = n.totalActivation;
        baselevelActivation = n.baselevelActivation;    
        currentActivation = n.currentActivation;    
        
        layerDepth = n.layerDepth; 
        nodeID = n.nodeID;  
        label = n.label;      
        type = n.type; 
        exciteBehavior = n.exciteBehavior;
        decayBehav = n.decayBehav;    	
    }//public Node(Node n)
    
    /**
     * Adds this node's current, baseLevel, and residual activation to total
     * activation. Also updates activation buffers. This method should only
     * be invoked when activation passing for this cycle is complete.
     */
    public void synchronize(){    	 	
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
    public boolean hasBeenSynchronized(){
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
//    	M.p("Node: " + label);
//    	M.p("tot activ " + totalActivation);
//    	M.p("cur activ " + currentActivation);
//    	M.p("excit     " + excitation);
    	
    	
        currentActivation = exciteBehavior.excite(currentActivation, excitation);//+= Math.abs(excitation);
//        
//        
//        M.p("resulting cur activ.: " + currentActivation + "\n");
    }
    
    /**
     * 
     * @param
     */
    public void setExciteBehavior(ExciteBehavior behavior){
    	exciteBehavior = behavior;
    }

    
    /**
     * 
     */    
    public void decay() {
        currentActivation = decayBehav.decay(currentActivation);
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
    public boolean isRelevant(){   
//    	M.p(label + " total " + M.rnd(totalActivation) + " thresh " + selectionThreshold);
//    	
        return (totalActivation >= selectionThreshold);
    }
    
    /**
     * 
     * @param activ
     */
    public void setMinActivation(double activ){
    	minActivation = activ;
    }
    
    /**
     * 
     * @param activ
     */
    public void setMaxActivation(double activ){
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
	public boolean equals(Object obj){
		if(!(obj instanceof PamNodeImpl))
			return false;
		PamNodeImpl other = (PamNodeImpl)obj;
		return nodeID == other.nodeID && type == other.type;
	}	
	
	/**
	 * 
	 */
	public int hashCode(){ 
        int hash = 1;
        Integer i = new Integer(type);
        Long id = new Long(nodeID);
        
        hash = hash * 31 + id.hashCode();
        hash = hash * 31 + (i == null ? 0 : i.hashCode());
        return hash;
    }   
	
	/**
	 * 
	 */   
    public long getIdentifier() {
        return nodeID;
    } 
	
    /** 
     * Returns the label
     * @return label
     */
    public String getLabel() {
        return label;
    }
    
    public void setLayerDepth(int d){
    	layerDepth = d;
    }
   
	public int getLayerDepth(){
		return layerDepth;
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
    
    public double getCurrentActivation() {
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
    
    /**
     *
     */
    public String toString() {
    	String s = "Node name: " + getLabel() + ", Total_activ: ";
    	if(!this.equals(null))
    		s += M.rnd(totalActivation);
    	else
    		s += " NA";
    	return s;
    }
    
    public void printActivationString() {
        System.out.println(toString() + "\t" + totalActivation + " = " + 
        		currentActivation + " + " + baselevelActivation);
    }

	public double getDefaultMaxActivation() {
		return MAX_ACTIVATION;
	}

	public double getDefaultMinActivation() {
		return MIN_ACTIVATION;
	}

	public long getID() {
		return nodeID;
	}

	public void setDecayBehavior(DecayBehavior b) {
		// TODO Auto-generated method stub
		
	}

	public Node copy() {
		// TODO Auto-generated method stub
		return null;
	}

	public DecayBehavior getDecayBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior getExciteBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public Node getReferencedNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setActivation(double d) {
		// TODO Auto-generated method stub
		
	}

	public void setReferencedNode(Node n) {
		// TODO Auto-generated method stub
		
	}

	public void setID(long id) {
		// TODO Auto-generated method stub
		
	}

	public void setLabel(String label) {
		// TODO Auto-generated method stub
		
	}

}//class Node