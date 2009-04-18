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
package edu.memphis.ccrg.lida.wumpusWorld.d_perception;

import java.util.HashSet;
import java.util.Set;

import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.shared.strategies.BasicExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.LinearDecayCurve;
import edu.memphis.ccrg.lida.util.Printer;

public class RyanPamNode extends PamNodeImpl implements PamNode{	
    private int layerDepth; 
    private Set<SpatialLocation> locationsOfThisNode = new HashSet<SpatialLocation>();
    
    /**
     * 
     * @param id
     * @param bla
     * @param ca
     * @param label
     * @param type
     */
    public RyanPamNode(long id, double bla, double ca, String label, int type){    	
    	minActivation = MIN_ACTIVATION;
    	maxActivation = MAX_ACTIVATION;
    	totalActivation = bla + ca;  
        baselevelActivation = bla;
        currentActivation = ca;

        layerDepth = 0;
        this.id = id; 
        this.label = label;                
        this.type = type;        
        exciteBehavior = new BasicExciteBehavior();
        decayBehav = new LinearDecayCurve();
    }//public Node(long id,...
    
    public RyanPamNode(){    	
    	minActivation = MIN_ACTIVATION;
    	maxActivation = MAX_ACTIVATION;
        layerDepth = 0;
    }//public Node(long id,...
    
    /**
     * 
     * @param n
     */
    public RyanPamNode(RyanPamNode n){
    	selectionThreshold = n.selectionThreshold;
    	importance = n.importance;
    	
    	minActivation = n.minActivation;
    	maxActivation = n.maxActivation;                   
        totalActivation = n.totalActivation;
        baselevelActivation = n.baselevelActivation;    
        currentActivation = n.currentActivation;    
        
        layerDepth = n.layerDepth; 
        this.id = n.id;  
        label = n.label;      
        type = n.type; 
        exciteBehavior = n.exciteBehavior;
        decayBehav = n.decayBehav;    	
        locationsOfThisNode = n.locationsOfThisNode;
    }//public Node(Node n)
    
    /**
     * Wumpus world may have multiple instances of the same node at differnt
     * locations so I will store those locations in the node.  Parameters
     * refer to one location of the this node.
     * 
     * @param i 
     * @param j
     */
	public boolean addNewWWLocation(int i, int j) {
		SpatialLocation sl = new SpatialLocation(i, j);
		boolean res = false;
		synchronized(this){
			res = locationsOfThisNode.add(sl);
		}
		return res;		
	}//method
	
	public synchronized void clearAllWWLocations() {
		locationsOfThisNode.clear();		
	}
	
	public Set<SpatialLocation> getLocations(){
		Set<SpatialLocation> result = new HashSet<SpatialLocation>();
		synchronized(this){
			for(SpatialLocation sl: locationsOfThisNode){
				result.add(new SpatialLocation(sl));
			}
		}
		
		return result;
	}
	
    public void setLayerDepth(int d){
    	layerDepth = d;
    }
   
	public int getLayerDepth(){
		return layerDepth;
	}
   
   /**
     *
     */
    public String toString() {
    	String s = "Node name: " + getLabel() + ", Total_activ: ";
    	if(!this.equals(null))
    		s += Printer.rnd(totalActivation);
    	else
    		s += " NA";
    	return s;
    }
    
    public void printActivationString() {
        System.out.println(toString() + "\t" + totalActivation + " = " + 
        		currentActivation + " + " + baselevelActivation);
    }

	public synchronized void printSpatialLocations() {
		for(SpatialLocation sl: locationsOfThisNode){
			System.out.println(this.label + " at " + sl.getI() + " " + sl.getJ());
		}		
	}//method

	public SpatialLocation getLocation() {
		for(SpatialLocation sl: locationsOfThisNode)
			return sl;
		return new SpatialLocation();
	}

}//class Node