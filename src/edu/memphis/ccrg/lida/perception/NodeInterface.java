/**
 * NodeInterface.java
 */
package edu.memphis.ccrg.lida.perception;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.util.DecayBehavior;

/**
 * @author Ryan McCall
 * The majority of these methods are called by PAM
 */
public interface NodeInterface{

	
	//Activation related methods in the order they are 
	//going to be called by PAM 	

	public void excite(double amount); //adds to current activation
	public void setExciteBehavior(ExciteBehavior behavior);
	
	public void synchronize();	
	public boolean isRelevant(); //if so add to percept	
	
	public void decay();	
	public void setDecayBehav(DecayBehavior c);
	
	public boolean equals(Object obj);
	public int hashCode();

	public void setValue(Map<String, Object> values);

	public String getLabel();
	public long getIdentifier();
	public void setLayerDepth(int d);   
	public int getLayerDepth();	
	public double getImportance(); 
    public double getSelectionThreshold();  
    public double getBaselevelActivation();      
    public double getCurrentActivation();
    public double getTotalActivation();
    public double getMinActivation();
    public double getMaxActivation();	
    
    public String toString(); //very useful since seeing node activation is very helpful
}//interface NodeInterface

