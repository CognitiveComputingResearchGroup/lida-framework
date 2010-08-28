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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.LearnableActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class BehaviorImpl extends LearnableActivatibleImpl implements Behavior{
	
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

    //TODO attention codelets
    private List<ExpectationCodelet> expectationCodelets = new ArrayList<ExpectationCodelet>();

    /**
     * Id of the action(s) in sensory-motor to be taken if this behavior executes
     */
	private long schemeActionId;
	
	/**
	 * unique identifier
	 */
	private long id;
   
    //TODO why would a behavior have a stream?  To spawn additional behaviors?
//   private Stream stream = null;
    
	public BehaviorImpl(long id, long actionId) {
	   	this.id = id;
	   	this.schemeActionId = actionId;
	   	deactivateContext();
	}
	
    public BehaviorImpl(String label, long id, long actionId, double totalActivation){
    	this(id, actionId);
    	this.label = label;
    	
    	//TODO Behaviors have no bla
    	setActivation(totalActivation);
    }

	//Precondition methods
    public void deactivateContext(){                
        for(Node s: context.keySet())
        	context.put(s, false);       
    }

    public boolean isContextConditionSatisfied(Node prop){
    	if(context.containsKey(prop))
    		return context.get(prop) == true;
    	return false;
    }
    
    //TODO optimization 
    public boolean isAllContextConditionsSatisfied(){
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
	
    // start add methods    
    public boolean addContextCondition(Node condition){
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
            logger.log(Level.WARNING, "tried to add null Precondition or predecessor as predecessor", LidaTaskManager.getActualTick());
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
            logger.log(Level.WARNING, "tried to add null Precondition or predecessor as successor", LidaTaskManager.getActualTick());
    }
    
//	@Override
//	public void addSuccessors(Node addProposition, List<Behavior> behaviors) {
//		successors.put(addProposition, behaviors);
//	}
    
    public void addConflictor(Node precondition, Behavior conflictor){
        if(precondition != null && conflictor != null){
            Set<Behavior> list = conflictors.get(precondition);
            if(list == null){
                list = new HashSet<Behavior>();
                conflictors.put(precondition, list);
            }
            list.add(conflictor);            
        }
        //TODO
    }
    
    //TODO make sure these methods are threadsafe
    
	public void addConflictors(Node precondition1, Set<Behavior> behaviors) {
		conflictors.put(precondition1, behaviors);
		//TODO follow previous method
	}
    
    public void addExpectationCodelet(ExpectationCodelet expectationCodelet){
    	expectationCodelets.add(expectationCodelet);
    }
    
//	@Override
//	public void addPredecessors(Node precondition, List<Behavior> behaviors) {
//		predecessors.put(precondition, behaviors);
//	}
    
    //Get methods
    public Set<Node> getContextConditions(){
    	//TODO use unmodifiable set
        return context.keySet();
    }
    
    public Set<Node> getAddList(){
        return addList;
    }
    
    public Set<Node> getDeleteList(){
        return deleteList;
    }
    
    public Map<Node, Set<Behavior>> getPredecessors(){
        return predecessors;
    }
    
    public Map<Node, Set<Behavior>> getSuccessors(){
        return successors;
    } 
    
    public Collection<Set<Behavior>> getConflictors(){
        return conflictors.values();
    }
        
	public Set<Behavior> getConflictors(Node precondition1) {
		return conflictors.get(precondition1);
	}
    
    public List<ExpectationCodelet> getExpectationCodelets(){
        return expectationCodelets;
    }
   
	@Override
	public int getContextSize() {
		return context.size();
	}

	@Override
	public Set<Behavior> getPredecessors(Node precondition) {
		return predecessors.get(precondition);
	}
    
	@Override
	public Set<Behavior> getSuccessors(Node addProposition) {
		return successors.get(addProposition);
	}

	//TODO change to SIZE
	@Override
	public int getSuccessorCount() {
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
	public long getSchemeActionId() {
		return schemeActionId;
	}

	public long getId() {
		return id;
	}

	public String getLabel(){
		return label;
	}
	
    //Set methods       
//    public void setContextConditions(Map<Node, Boolean> conditions){
//    	this.context = conditions;
//    }
//    
//    public void setAddList(Set<Node> addList){
//    	this.addList = addList;
//    } 
//    
//    public void setDeleteList(Set<Node> deleteList){
//    	this.deleteList = deleteList;
//    }
//    
//    public void setPredecessors(Map<Node, Set<Behavior>> predecessors){
//    	this.predecessors = predecessors;
//    }    
//    
//    public void setSuccesors(Map<Node, Set<Behavior>> successors){
//    	this.successors = successors;
//    }    
//    
//    public void setConflictors(Map<Node, Set<Behavior>> conflictors){
//    	this.conflictors = conflictors;
//    }    
//    
//    public void setExpectationCodelets(List<ExpectationCodelet> expectationCodelets){
//    	this.expectationCodelets = expectationCodelets;
//    }

	public void setId(long id) {
		this.id = id;
	}

	public void setSchemeActionId(long schemeActionId) {
		this.schemeActionId = schemeActionId;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Behavior))
			return false;
		
		Behavior behavior = (Behavior) o;
		return behavior.getId() == id && behavior.getSchemeActionId() == schemeActionId;
	}

	public int hashCode() {
		int hash = 1;
		Long v1 = new Long(id);
		Long v2 = new Long(schemeActionId);
		hash = hash * 31 + v2.hashCode();
		hash = hash * 31 + (v1 == null ? 0 : v1.hashCode());
		return hash;
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

}//class