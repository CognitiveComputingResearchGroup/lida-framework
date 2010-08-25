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
    private Map<Node, Boolean> preconditions = new ConcurrentHashMap<Node, Boolean>();

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
    private Map<Node, List<Behavior>> predecessors = new ConcurrentHashMap<Node, List<Behavior>>();
    
    /**
     * For each key, the key is in this behavior's add list and indexes all the 
     * behaviors which have the key in their precondition
     */
    private Map<Node, List<Behavior>> successors = new ConcurrentHashMap<Node, List<Behavior>>();
    
    /**
     * Key is an element in this behavior's context.  It indexes all the behaviors which have 
     * the key element in their delete list
     */
    private Map<Node, List<Behavior>> conflictors = new ConcurrentHashMap<Node, List<Behavior>>();

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
	   	deactivateAllPreconditions();
	}
	
    public BehaviorImpl(String label, long id, long actionId, double totalActivation){
    	this(id, actionId);
    	this.label = label;
    	
    	//TODO this is a problem
    	setActivation(totalActivation);
    }

	//Precondition methods
    public void deactivateAllPreconditions(){                
        for(Node s: preconditions.keySet())
        	preconditions.put(s, false);       
    }

    public boolean isPreconditionSatisfied(Node prop){
    	if(preconditions.containsKey(prop))
    		return preconditions.get(prop) == true;
    	return false;
    }
    
    public boolean isAllPreconditionsSatisfied(){
        for(Boolean b: preconditions.values()){
        	if(b == false)
        		return false;
        }
        return true;
    }    
 
	@Override
	public void satisfyPrecondition(Node proposition) {
		preconditions.put(proposition, true);
	}
	
    // start add methods    
    public boolean addPrecondition(Node precondition){
    	return (preconditions.put(precondition, false) != null);
    }
    
    public boolean addAddCondition(Node addCondition){
    	return addList.add(addCondition);
    }
    
    public boolean addDeleteCondition(Node deleteCondition){
    	return deleteList.add(deleteCondition);
    }    
    
    public void addPredecessor(Node precondition, Behavior predecessor){
        if(precondition != null && predecessor != null){
            List<Behavior> list = predecessors.get(precondition);            
            if(list == null){
                list = new ArrayList<Behavior>();
                predecessors.put(precondition, list);
            }
            list.add(predecessor);
        }
        else
            logger.log(Level.WARNING, "", LidaTaskManager.getActualTick());
    } 
    
    public void addSuccessor(Node addProposition, Behavior successor){
        if(addProposition != null && successor != null){
            List<Behavior> list = successors.get(addProposition);
            if(list == null){              
                list = new ArrayList<Behavior>();
                successors.put(addProposition, list);
            }
            list.add(successor);
        }
    }
    
//	@Override
//	public void addSuccessors(Node addProposition, List<Behavior> behaviors) {
//		successors.put(addProposition, behaviors);
//	}
    
    public void addConflictor(Node precondition, Behavior conflictor){
        if(precondition != null && conflictor != null){
            List<Behavior> list = conflictors.get(precondition);
            if(list == null){
                list = new ArrayList<Behavior>();
                conflictors.put(precondition, list);
            }
            list.add(conflictor);            
        }
    }
    
	public void addConflictors(Node precondition1, List<Behavior> behaviors) {
		conflictors.put(precondition1, behaviors);
	}
    
    public void addExpectationCodelet(ExpectationCodelet expectationCodelet){
    	expectationCodelets.add(expectationCodelet);
    }
    
//	@Override
//	public void addPredecessors(Node precondition, List<Behavior> behaviors) {
//		predecessors.put(precondition, behaviors);
//	}
    
    //Get methods
    public Set<Node> getPreconditions(){
        return preconditions.keySet();
    }
    
    public Set<Node> getAddList(){
        return addList;
    }
    
    public Set<Node> getDeleteList(){
        return deleteList;
    }
    
    public Map<Node, List<Behavior>> getPredecessors(){
        return predecessors;
    }
    
    public Map<Node, List<Behavior>> getSuccessors(){
        return successors;
    } 
    
    public Map<Node, List<Behavior>> getConflictors(){
        return conflictors;
    }
        
	public List<Behavior> getConflictors(Node precondition1) {
		return conflictors.get(precondition1);
	}
    
    public List<ExpectationCodelet> getExpectationCodelets(){
        return expectationCodelets;
    }
   
	@Override
	public int getPreconditionCount() {
		return preconditions.size();
	}

	@Override
	public List<Behavior> getPredecessors(Node precondition) {
		return predecessors.get(precondition);
	}
    
	@Override
	public List<Behavior> getSuccessors(Node addProposition) {
		return successors.get(addProposition);
	}

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
    public void setPreconditions(Map<Node, Boolean> preconditions){
    	this.preconditions = preconditions;
    }
    
    public void setAddList(Set<Node> addList){
    	this.addList = addList;
    } 
    
    public void setDeleteList(Set<Node> deleteList){
    	this.deleteList = deleteList;
    }
    
    public void setPredecessors(Map<Node, List<Behavior>> predecessors){
    	this.predecessors = predecessors;
    }    
    
    public void setSuccesors(Map<Node, List<Behavior>> successors){
    	this.successors = successors;
    }    
    
    public void setConflictors(Map<Node, List<Behavior>> conflictors){
    	this.conflictors = conflictors;
    }    
    
    public void setExpectationCodelets(List<ExpectationCodelet> expectationCodelets){
    	this.expectationCodelets = expectationCodelets;
    }

	public void setId(long id) {
		this.id = id;
	}

	public void setSchemeActionId(long schemeActionId) {
		this.schemeActionId = schemeActionId;
	}

}//class