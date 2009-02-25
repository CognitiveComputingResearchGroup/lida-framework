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

import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.util.DecayCurve;
import edu.memphis.ccrg.lida.util.LinearDecayCurve;


public class Node implements NodeInterface, Linkable{
    private final String      DEF_LABEL = "node"; //Default name for a node
    private final int        TYPE_UNDEF = -1;  //Undefined type for node
    private final int 		   TYPE_PFD = 0;
    private final int 		TYPE_OBJECT = 1;
    private final int 	  TYPE_CATEGORY = 2;
    private final double MIN_ACTIVATION = 0.0;    
    private final double MAX_ACTIVATION = 1.0;
    
    private double selectivity;
    /** Activation required for node to be part of the percept.
     *  Bounded by minActivation and maxActivation
     */
    private double selectionThreshold;
    private double upscale;
    /**
     * Specifies the relative importance of a Node. Only relevant for nodes
     * that represent feelings. Lies between 00.d and 1.0d inclusive.
     */
    private double importance;
    /**
     * "layerDepth" is an integer representing the node's distance from the fringe.
     * This is given by the longest path (in terms of number of links) from this node to some
     * primitive feature detector.
     */
    private int layerDepth;   
    private double minActivation;
    private double maxActivation;        
    private double totalActivation;
    private double baselevelActivation;    
    private double currentActivation;      
      
    //TODO:remove eventually or NOT!!!
    private Set<Node> children;   //Nodes that feed into this node; not necessary do we want?
    private Set<Node> parents;    //Nodes this node feeds into; not necessary, derivable from links
    private Set<Link> links;	//Lateral links
    
    private long nodeID;
    private String label;  // Human-readable name used to identify node         
    private int type;
    private ExciteBehavior exciteBehav;
    private DecayCurve decayCurve; 
    
    public Node(Node n){
    	selectivity = n.selectivity;
    	selectionThreshold = n.selectionThreshold;
    	upscale = n.upscale;
    	importance = n.importance;
    	layerDepth = n.layerDepth;
    	
    	minActivation = n.minActivation;
    	maxActivation = n.maxActivation;                   
        totalActivation = n.totalActivation;
        baselevelActivation = n.baselevelActivation;    
        currentActivation = n.currentActivation;    
        
        children = n.children;
        parents = n.parents;
        links = n.links;
        
        nodeID = n.nodeID;  
        label = n.label;      
        type = n.type; 
        exciteBehav = n.exciteBehav;
        decayCurve = n.decayCurve;    	
    }//public Node(Node n)

    public Node(long id, double bla, double ca, String label, double upscale, double select, int type) {
    	this.selectivity = select;
    	selectionThreshold = 0.0;
    	this.upscale = upscale;
    	importance = 0.0;
    	layerDepth = 0;
    	
    	minActivation = MIN_ACTIVATION;
    	maxActivation = MAX_ACTIVATION;
    	totalActivation = bla + ca;  
        baselevelActivation = bla;
        currentActivation = ca;
        
        parents = new TreeSet<Node>();
        children = new TreeSet<Node>();        
        links = new TreeSet<Link>();
        
        nodeID = id; 
        this.label = label;                
        this.type = type;        
        exciteBehav = new ExciteBehavior();
        decayCurve = new LinearDecayCurve();
    }//public Node(long id,...
    
    /**
     * Adds this node's current, baseLevel, and residual activation to total
     * activation. Also updates activation buffers. This method should only
     * be invoked when activation passing for this cycle is complete.
     */
    public void synchronize() {    	 	
    	if((currentActivation + baselevelActivation) > MAX_ACTIVATION)
    		totalActivation = MAX_ACTIVATION;
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
     * Increase current activation and propogate newly received energy to
     * parents. The current activation of this node is increased by the
     * excitation value.
     * 
     * @param   excitation the value to be added to the current activation of
     *          this node
     * @see     #activateParents(double)
     */
    public void excite(double excitation) {
        currentActivation += Math.abs(excitation);
        activateParents(Math.abs(excitation));
    }
    
    public void setExciteBehavior(ExciteBehavior behavior){
    	exciteBehav = behavior;
    }
    
    /**
     * Distribute the activation of this node to each of its parents. A fraction
     * of this activation is used to excite each of this node's parents.
     * 
     * @param   activation the activation to be sent to the parents
     * @see     #excite(double)
     */
    private void activateParents(double activation) {
        if(!this.isRootNode()) {
            double energy = activation * upscale;
            for(Node n: parents)
                n.excite(energy);
        }//if not a root node
    }//activateParents
    
     /*
    public void inhibit(double inbibition)
    {
        this.currentActivation -= Math.abs(inhibition);
        if(this.currentActivation < 0)
            this.currentActivation = 0;
    }
      **/
    
    /**
     *  Reset current and total activation to the activation lowerbound
     *  Set residual activation
     *
     *  TO DO: Invoke base level activation decay cycle
     */
    /**Original Implimentation of decay(Sidney's Code)
     * public void decay(double activation) {
     * this.activation.setCurrentActivation(Parameters.ACTIVATION_LOWERBOUND);
     * this.activation.setBaselevelActivation(Parameters.ACTIVATION_LOWERBOUND);
     * this.activation.setResidualActivation(activation);
     *
     * //decay base level activation
     * }
     */
    /*My Implimentation of decay*/
  /*  public void decay(double activation) {
        this.activation.setCurrentActivation(this.linearDecayCurve.getNewActivation(this.activation.getCurrentActivation()));
        this.activation.setBaselevelActivation(this.linearDecayCurve.getNewActivation(this.activation.getBaselevelActivation()));
        this.activation.setResidualActivation(this.linearDecayCurve.getNewActivation(this.activation.getResidualActivation()));
   
        //decay base level activation
    }
   */
    
    public void decay() {
        currentActivation = decayCurve.decay(currentActivation);
    }
    
	public void setDecayCurve(DecayCurve c) {
		decayCurve = c;		
	}
    
    public void detect(SensoryContent sm){
    	//specific implement would have this:
    	//int[] senseData = sm.getSenseData();
    }    
    
     /**
      * Determines if this node is relevant. A node is relevant if its total
      * activation is greater or equal to the selection threshold.
      * 
      * @return     <code>true</code> if this node is relevant
      * @see        #selectionThreshold
      */
    public boolean isRelevant(){
        return (totalActivation >= selectionThreshold);
    }//
    
    /**Update Methods**/
    
    public int updateLayerDepth() {
        this.layerDepth = 0;
        
        if(this.isFringeNode())
            this.layerDepth = 0;
        else {
            int layerDepth[] = new int[children.size()];
            int ild = 0;
            for(Node node: children) {
                layerDepth[ild] = node.updateLayerDepth();
                ild++;
            }
            Arrays.sort(layerDepth);
            this.layerDepth = layerDepth[layerDepth.length - 1] + 1;
        }
        return layerDepth;
    }
    
    public void refreshActivationParameters() {
        updateMinActivation();
        updateMaxActivation();
        updateSelectionThreshold();
    }
    
    /**
     * Updates the minimum activation possible for this node.
     * <p>
     * Since this method recursively invokes getMinActivation from its children,
     * it assumes that the children have already been updated.
     */
    protected void updateMinActivation() {
        if(isFringeNode())
        	minActivation = MIN_ACTIVATION;
        else{
            for(Node n: children)
                minActivation += n.getMinActivation();
            
            minActivation *= upscale;            
        }  
    }
    
    /**
     * Updates the maximum activation possible for this node.
     * <p>
     * Since this method recursively invokes getMaxActivation from its children,
     * it assumes that the children have already been updated.
     */
    private void updateMaxActivation(){        
        if(this.isFringeNode())
            maxActivation = MAX_ACTIVATION;
        else {
        	for(Node n: children)
            	maxActivation += n.getMaxActivation();
            
            maxActivation *= upscale;           
        }     
    }//private void updateMaxActivation()
    
    private void updateSelectionThreshold() {
        selectionThreshold = selectivity*(maxActivation - minActivation) + minActivation;
    }
    
    /**
     * Checks whether given node is root node.
     * 
     * @return <code>true</code> if it is a root node else false
     */
    public boolean isRootNode() {
        return parents.isEmpty();
    }
    
    /**
     * Checks whether given node is fringe node.
     * 
     * @return <code>true</code> if it is a fringe node else false
     */
    public boolean isFringeNode() {
        return children.isEmpty();
    }
        
    /**
     * Add parameterized node as my child As a side effect this node
     * automatically makes itself the parent of parametrized node
     * @param child The node to be added as the child
     * @throws Null pointer exception
     */
    public void addChild(Node child){
        if(child != null) {
            this.children.add(child);
            child.addParent(this);
        }else
        	System.out.println("tried to add a null child !");
    }
    
    /**
     * Add myself as parameterized node's parent.
     * @param parent the node to be added as the parent
     */
    public void addParent(Node parent) {
        this.parents.add(parent);
    }

    public void addLink(Link link) {
        if(link!= null)
            this.links.add(link);
        else
            System.out.println("Tried to add a null Link");        
    }
    
    public void removeLink(Link link ) {
        if(link!= null)
            this.links.remove(link);        
    }
    
    /**
     * Returns all the ancestors of the given node
     * @return Linked list of type node of  all ancestors of the given node
     */
    public Set<Node> getAncestors() {
    	Set<Node> ancestors = new TreeSet<Node>();
        
        if(!this.isRootNode()) {
            for(Node parent:getParents()) {
                ancestors.add(parent);
                ancestors.addAll(parent.getAncestors());
            }
        }
        return ancestors;
    }
    
    /**
     * Returns descendents  of the given node
     * @return All descendents of the given node
     */
    public Set<Node> getDescendents(){
        Set<Node> descendents = new TreeSet<Node>();
        
        if(!this.isFringeNode()) {
            for(Node child:getChildren()) {
                descendents.add(child);
                descendents.addAll(child.getDescendents());
            }
        }
        return descendents;
    }
    
    /**
     * Checks whether the target node is an ancestor or not.
     * 
     * @return  <code>true</code> if an ancestor else false
     * @param   target The node which we want to test whether an ancestor or not
     */
    public boolean isAncestor(Node target) {
        return this.getAncestors().contains(target);
    }
    
    /**
     * Checks whether the target node is a descendent or not.
     * 
     * @param   target The node which we want to test a descendent or not
     * @return  <code>true</code> if it ia a descendent else false
     */
    public boolean isDescendent(Node target) {
        return this.getDescendents().contains(target);
    }
    
    public boolean isPFDNode(){
    	return (type == TYPE_PFD);
    }
    
    /**
     *
     */
    public String toString() {
    	String s = "node: " + getLabel() + " tot. activ.: ";//+ activation.getCurrentActivation();
    	if(!this.equals(null))
    		s += rnd(totalActivation);
    	else
    		s += " NA";
    	return s;
    }
    
    public String toActivationString() {
        return toString() + "\t" + totalActivation + " --> " + currentActivation + "," + baselevelActivation;
    }
    
    /**
     * Returns the children
     * @return child nodes
     */    
    public Set<Node> getChildren() {
        return children;
    }
    
    /**
     * returns the parents
     * @return parent nodes
     */
    public Set<Node> getParents() {
        return parents;
    }
    
    //my code
    public Set<Link> getLinks() {
        return links;
    }
    //
    
    /**
     * returns the label
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
    
	public void setValue(Map<String, Object> values) {
		Object o = values.get("importance");
		if ((o != null)&& (o instanceof Double)) 
			importance = (Double)o;
		
		o = values.get("baselevelactivation");
		if ((o != null)&& (o instanceof Double)) 
			baselevelActivation = (Double)o;
		
		o = values.get("selectivity");
		if ((o != null)&& (o instanceof Double)) 
			selectivity = (Double)o;  
		
		o = values.get("upscale");
		if ((o != null)&& (o instanceof Double)) 
			upscale = (Double)o;		
	}//public void setValue(Map<String, Object> values)
	
	public boolean equals(Node n){
		if(!(n instanceof Node))
			return false;
		return nodeID == n.nodeID;
	}
	
	public int hashCode() { 
        int hash = 1;
        Integer i = new Integer(type);
        Long id = new Long(nodeID);
        
        hash = hash * 31 + id.hashCode();
        hash = hash * 31 + (i == null ? 0 : i.hashCode());
        return hash;
    }     
     
    public int getLayerDepth() {
        return layerDepth;
    }
        
    /**
     * returns selection threshold
     * @return Selection threshold
     */
    public double getSelectionThreshold() {
        return selectionThreshold;
    }

    /**
     * This is done for Encapsulation purpose of the code
     *
     */
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
    
    public long getIdentifier() {
        return nodeID;
    }
    
    public Set<Node> getOutgoingNodes() {
    	return new TreeSet<Node>(parents);
    }//getOutgoingNodes    
    
    public void printActivation(){
    	System.out.println(label + " tot: " + rnd(totalActivation) + " sel.: " + selectivity);	    	
    }
    
    public double rnd(double d){    //rounds a double to the nearest 100th
    	return Math.round(d*100.0)/100.0;
    }

    
}//class Node