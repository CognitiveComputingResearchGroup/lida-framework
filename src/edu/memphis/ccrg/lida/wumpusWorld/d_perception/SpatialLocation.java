package edu.memphis.ccrg.lida.wumpusWorld.d_perception;

import java.util.Map;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

public class SpatialLocation implements Node{
	private int iLocation = (int)Math.random()*Integer.MAX_VALUE;
	private int jLocation = (int)Math.random()*Integer.MAX_VALUE;
	private String label = "spatial_location";
	
	public SpatialLocation(){
		
	}

	public SpatialLocation(int i, int j) {
		iLocation = i;
		jLocation = j;
	}
	
	public SpatialLocation(SpatialLocation oldSL) {
		this.iLocation = oldSL.iLocation;
		this.jLocation = oldSL.jLocation;
		label = oldSL.label;
	}

	public boolean equals(Object obj){
		if(!(obj instanceof SpatialLocation))
			return false;
		SpatialLocation other = (SpatialLocation)obj;
		return iLocation == other.iLocation && jLocation == other.jLocation;
	}	
	
	public char getDirection(){
		char c = 'x';
		if(iLocation == 1){
			if(jLocation == 0){
				c = '>';
			}else if(jLocation == 2){
				c = '<';
			}
			
		}
		if(jLocation == 1){
			if(iLocation == 0){
				c = 'V';
			}else if(iLocation == 2){
				c = 'A';
			}
		}
				
		return c;
	}
	
	/**
	 * 
	 */
	public int hashCode(){ 
        int hash = 1;
        Integer i = new Integer(iLocation);
        Integer j = new Integer(jLocation);
        
        hash = hash * 31 + i.hashCode();
        hash = hash * 31 + (j == null ? 0 : i.hashCode());
        return hash;
    }   
	
	public int getI(){return iLocation;}
	public void setI(int i){iLocation = i;}
	public int getJ(){return jLocation;}
	public void setJ(int j){jLocation = j;}

	public void print() {
		System.out.println("x: " + jLocation + " y: " + iLocation);		
	}

	public Node copy() {
		// TODO Auto-generated method stub
		return null;
	}

	public void decay() {
		// TODO Auto-generated method stub
		
	}

	public void excite(double amount) {
		// TODO Auto-generated method stub
		
	}

	public double getCurrentActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public DecayBehavior getDecayBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExciteBehavior getExciteBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getImportance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLabel() {
		return label;
	}

	public Node getReferencedNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRelevant() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setActivation(double d) {
		// TODO Auto-generated method stub
		
	}

	public void setDecayBehavior(DecayBehavior c) {
		// TODO Auto-generated method stub
		
	}

	public void setExciteBehavior(ExciteBehavior behavior) {
		// TODO Auto-generated method stub
		
	}

	public void setID(long id) {
		// TODO Auto-generated method stub
		
	}

	public void setLabel(String label) {
		// TODO Auto-generated method stub
		
	}

	public void setReferencedNode(Node n) {
		// TODO Auto-generated method stub
		
	}

	public void setValue(Map<String, Object> values) {
		// TODO Auto-generated method stub
		
	}

	public void synchronize() {
		// TODO Auto-generated method stub
		
	}

	public int getLayerDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void printActivationString() {
		// TODO Auto-generated method stub
		
	}

	public void decay(DecayBehavior decayBehavior) {
		// TODO Auto-generated method stub
		
	}

	public boolean isSameAs(SpatialLocation sl) {
		return (iLocation == sl.iLocation) && (jLocation == sl.jLocation);
	}

	public boolean isSameAs(int i, int j) {
		return (iLocation == i) && (jLocation == j);
	}

	public void setId(long id) {
		// TODO Auto-generated method stub
	}

	public String getIds() {
		return "loc(" + iLocation + "," + jLocation + ")";
	}	
	
}
