/**
 * NodeInterface.java
 */
package edu.memphis.ccrg.lida.perception;

import java.util.Map;
import java.util.Set;
import java.util.UUID;

import edu.memphis.ccrg.lida.sensoryMemory.SensoryContent;
import edu.memphis.ccrg.lida.util.DecayCurve;

/**
 * @author Ryan McCall
 * The majority of these methods are called by PAM
 */
public interface NodeInterface{
	
	public int updateLayerDepth(); //there are called when a node is registered in PAM
	public void refreshActivationParameters();
	
	//Activation related methods in the order they are 
	//going to be called by PAM 	
	public void detect(SensoryContent sm); //maybe shouldn't be here since only certain nodes would detect.
	//Related to this, we should discuss how PAM.java will determine which nodes should detect from SensoryMemory.
	public void excite(double amount); //adds to current activation
	public void setExciteBehavior(ExciteBehavior behavior);
	
	public void synchronize();	
	public boolean isRelevant(); //if so add to percept	
	
	public void decay();
	public void setDecayCurve(DecayCurve c);
	
	public void addChild(Node child);//currently throws NullPointerException
	public void addParent(Node parent);	
	public void addLink(Link link);
	public void removeLink(Link link);	
		
	public boolean isAncestor(Node target);//These not used by PAM currently but likely in the future
	public boolean isDescendent(Node target);
	public boolean isPFDNode();
	
	public void setValue(Map<String, Object> values);

	public Set<Node> getChildren(); 
	public Set<Node> getParents();
	public Set<Link> getLinks();	
	public String getLabel();
	public long getIdentifier();
	
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

