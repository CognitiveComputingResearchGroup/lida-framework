/**
 * Node.java
 */
package edu.memphis.ccrg.lida.shared;

import java.util.Map;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

/**
 * @author Javier Snaider
 * 
 */
public interface Node extends Linkable{

	
	public void excite(double amount); 
	public void setExciteBehavior(ExciteBehavior behavior);
	public ExciteBehavior getExciteBehavior();
	
	public void synchronize();	
	public boolean isRelevant(); 
	
	public void decay();	
	public void setDecayBehavior(DecayBehavior c);
	public DecayBehavior getDecayBehavior();
	
	public void setValue(Map<String, Object> values);

	public double getImportance(); 
    public double getCurrentActivation();
    public void setActivation(double d);
    public Node getReferencedNode();
    public void setReferencedNode (Node n);
    public Node copy();
    public long getId();
    public void setID(long id);
    public String getLabel();
    public void setLabel(String label);
	public int getLayerDepth();
	public void printActivationString();
	public void decay(DecayBehavior decayBehavior);
   
}//interface Node

