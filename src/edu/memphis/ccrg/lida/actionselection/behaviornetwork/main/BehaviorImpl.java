/**
 * Behavior.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 5:35 PM
 * 
 * Modified by Ryan McCall, 2010
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;

public class BehaviorImpl extends ActivatibleImpl implements Behavior{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.main.Behavior");
	
	/**
	 * Label for description
	 */
    private String label = "blank behavior";
 
    /**
     * 
     */
    private Map<Node, Boolean> context = new ConcurrentHashMap<Node, Boolean>();

    /**
     * Set of nodes that this scheme adds
     */
    private Set<Node> addList = new ConcurrentHashSet<Node>();
    
    /**
     * 
     */
    private Set<Node> deleteList = new HashSet<Node>();        
    
    /**
     * Id of the action(s) in sensory-motor to be taken if this behavior executes
     */
	private long actionId;
	
	/**
	 * unique identifier
	 */
	private long id;
	
	/**
	 * optimization for checking if context is all satisfied
	 */
    private boolean isAllContextSatisfied = false;
   
    /**
     * The streams that contains this behavior
     */
    private List<Stream> containingStreams = null;
    
	public BehaviorImpl(long id, long actionId) {
	   	this.id = id;
	   	this.actionId = actionId;
	   	deactivateAllContextConditions();
	}
	
    public BehaviorImpl(String label, long id, long actionId, double totalActivation){
    	this(id, actionId);
    	this.label = label;
    	setActivation(totalActivation);
    }

	//Precondition methods
    public void deactivateAllContextConditions(){      
        isAllContextSatisfied = false;
        for(Node s: context.keySet())
        	context.put(s, false);    
    }
    
	public void setId(long id) {
		this.id = id;
	}

	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

    public boolean isContextConditionSatisfied(Node prop){
    	if(context.containsKey(prop))
    		return context.get(prop) == true;
    	return false;
    }
    
    public boolean isAllContextConditionsSatisfied(){
    	if(isAllContextSatisfied)
    		return true;
    	
        for(Boolean b: context.values()){
        	if(b == false)
        		return false;
        }
        return true;
    }    
 
	@Override
	public void satisfyContextCondition(Node proposition) {
		context.put(proposition, true);
	}
	
	@Override
	public void deactiveContextCondition(Node condition) {
		if(context.containsKey(condition)){
			isAllContextSatisfied = false;
			context.put(condition, false);
		}	
	}
	
    // start add methods    
    public boolean addContextCondition(Node condition){
    	logger.log(Level.FINEST, "Adding context condition " + condition.getLabel() + " to " + label);
    	isAllContextSatisfied = false;
    	return (context.put(condition, false) != null);
    }
    
    public boolean addAddCondition(Node addCondition){
    	return addList.add(addCondition);
    }
    
    public boolean addDeleteCondition(Node deleteCondition){
    	return deleteList.add(deleteCondition);
    }    
    
    //Get methods
    public Set<Node> getContextConditions(){
        return Collections.unmodifiableSet(context.keySet());
    }
    
    public Set<Node> getAddList(){
        return Collections.unmodifiableSet(addList);
    }
    
    public Set<Node> getDeleteList(){
        return Collections.unmodifiableSet(deleteList);
    }
   
	@Override
	public int getContextSize() {
		return context.size();
	}
	
	@Override
	public double getAddListCount() {
		return addList.size();
	}
	
	@Override
	public double getDeleteListCount() {
		return deleteList.size();
	}

	@Override
	public long getActionId() {
		return actionId;
	}

	public long getId() {
		return id;
	}

	public String getLabel(){
		return label;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Behavior))
			return false;
		
		Behavior behavior = (Behavior) o;
		return behavior.getId() == id && behavior.getActionId() == actionId;
	}

	public int hashCode() {
		int hash = 1;
		Long v1 = new Long(id);
		Long v2 = new Long(actionId);
		hash = hash * 31 + v2.hashCode();
		hash = hash * 31 + (v1 == null ? 0 : v1.hashCode());
		return hash;
	}

	public void addContainingStream(Stream stream) {
		containingStreams.add(stream);
	}

	public List<Stream> getContainingStreams() {
		return containingStreams;
	}

}//class