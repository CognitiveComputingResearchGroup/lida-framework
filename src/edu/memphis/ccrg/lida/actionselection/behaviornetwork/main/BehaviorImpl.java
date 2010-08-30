/**
 * Behavior.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 5:35 PM
 * 
 * Modified by Ryan McCall, 2010
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.attention.AttentionCodelet;
import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

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
    private Set<Node> addList = new HashSet<Node>();
    
    /**
     * 
     */
    private Set<Node> deleteList = new HashSet<Node>();        
    
    /**
     * For each key, the key is in this behavior's precondition and indexes
     * all behaviors which contain that key in their add list
     */
    private Map<Node, Set<Behavior>> predecessors = new ConcurrentHashMap<Node, Set<Behavior>>();
    
    /**
     * For each key, the key is in this behavior's add list and indexes all the 
     * behaviors which have the key in their precondition
     */
    private Map<Node, Set<Behavior>> successors = new ConcurrentHashMap<Node, Set<Behavior>>();
    
    /**
     * Key is an element in this behavior's context.  It indexes all the behaviors which have 
     * the key element in their delete list
     */
    private Map<Node, Set<Behavior>> conflictors = new ConcurrentHashMap<Node, Set<Behavior>>();

    private List<AttentionCodelet> attentionCodelets = new ArrayList<AttentionCodelet>();

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
   
    //TODO why would a behavior have a stream?  To spawn additional behaviors?
    private Stream stream = null;
    
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
    	isAllContextSatisfied = false;
    	return (context.put(condition, false) != null);
    }
    
    public boolean addAddCondition(Node addCondition){
    	return addList.add(addCondition);
    }
    
    public boolean addDeleteCondition(Node deleteCondition){
    	return deleteList.add(deleteCondition);
    }    
    
    public void addPredecessor(Node precondition, Behavior predecessor){
        if(precondition != null && predecessor != null){
            Set<Behavior> list = predecessors.get(precondition);            
            if(list == null){
                list = new HashSet<Behavior>();
                predecessors.put(precondition, list);
            }
            list.add(predecessor);
        }
        else
            logger.log(Level.WARNING, "tried to add null Precondition or predecessor in addPredecessor", LidaTaskManager.getActualTick());
    } 
    
    public void addSuccessor(Node addProposition, Behavior successor){
        if(addProposition != null && successor != null){
            Set<Behavior> list = successors.get(addProposition);
            if(list == null){              
                list = new HashSet<Behavior>();
                successors.put(addProposition, list);
            }
            list.add(successor);
        }
        else
            logger.log(Level.WARNING, "tried to add null Precondition or predecessor in addSuccessor", LidaTaskManager.getActualTick());
    }
    
    public void addConflictor(Node precondition, Behavior conflictor){
        if(precondition != null && conflictor != null){
            Set<Behavior> list = conflictors.get(precondition);
            if(list == null){
                list = new HashSet<Behavior>();
                conflictors.put(precondition, list);
            }
            list.add(conflictor);            
        }
        else
            logger.log(Level.WARNING, "tried to add null Precondition or conflictor in addConflictor", LidaTaskManager.getActualTick());
    }
    
    //TODO make sure these methods are threadsafe
    //TODO weak hashset
    
	public void addConflictors(Node precondition1, Set<Behavior> behaviors) {
		if(precondition1 != null && behaviors != null){
	        Set<Behavior> list = conflictors.get(precondition1);
	        if(list == null){
                list = new HashSet<Behavior>();
                conflictors.put(precondition1, list);
            }
            list.addAll(behaviors);               
	    }
	    else
	    	logger.log(Level.WARNING, "tried to add null Precondition or conflictor in addConflictor", LidaTaskManager.getActualTick());
	}
    
    public void addAttentionCodelet(AttentionCodelet attentionCodelet){
    	attentionCodelets.add(attentionCodelet);
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
    
    public Map<Node, Set<Behavior>> getPredecessors(){
        return Collections.unmodifiableMap(predecessors);
    }
    
    public Map<Node, Set<Behavior>> getSuccessors(){
        return Collections.unmodifiableMap(successors);
    } 
    
    public Collection<Set<Behavior>> getConflictors(){
        return Collections.unmodifiableCollection(conflictors.values());
    }
        
	public Set<Behavior> getConflictors(Node precondition1) {
		return Collections.unmodifiableSet(conflictors.get(precondition1));
	}
	
	@Override
	public void removeConflictor(Node deleteItem, Behavior behavior) {
		conflictors.get(deleteItem).remove(behavior);
	}

	@Override
	public void removeSuccessor(Node precondition, Behavior behavior) {
		successors.get(precondition).remove(behavior);
	}

	@Override
	public void removePredecessor(Node addItem, Behavior behavior) {
		predecessors.get(addItem).remove(behavior);
	}
    
    public List<AttentionCodelet> getAttentionCodelets(){
        return Collections.unmodifiableList(attentionCodelets);
    }
   
	@Override
	public int getContextSize() {
		return context.size();
	}

	@Override
	public Set<Behavior> getPredecessors(Node precondition) {
		return Collections.unmodifiableSet(predecessors.get(precondition));
	}
    
	@Override
	public Set<Behavior> getSuccessors(Node addProposition) {
		return Collections.unmodifiableSet(successors.get(addProposition));
	}

	@Override
	public int getSuccessorSize() {
		return successors.size();
	}

	@Override
	public int getPredecessorCount() {
		return predecessors.size();
	}

	@Override
	public int getConflictorCount() {
		return conflictors.size();
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

	public void setStream(Stream stream) {
		this.stream = stream;
	}

	public Stream getStream() {
		return stream;
	}

}//class