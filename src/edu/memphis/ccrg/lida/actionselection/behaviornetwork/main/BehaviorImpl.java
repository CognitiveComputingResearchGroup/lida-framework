/*
 * Behavior.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 5:35 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

public class BehaviorImpl extends SchemeImpl implements Behavior{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.main.Behavior");
	
	/**
	 * Label for description
	 */
    private String name = "blank behavior";

    /**
     * total activation
     */
    private double totalActivation = 0.0;
    
    /**
     * base level
     */
    private double baseLevelActivation = 0.0; 
    
    /**
     * Positive or negative activation from other behaviors
     */
    private double incomingActivation = 0.0;
 
    /**
     * 
     */
    private Map<Node, Boolean> preconditions = new HashMap<Node, Boolean>();

    private Set<Node> addList = new HashSet<Node>();
    private Set<Node> deleteList = new HashSet<Node>();        
    
    /**
     * For each key, the key is in this behavior's precondition and indexes
     * all behaviors which contain that key in their add list
     */
    private Map<Node, List<Behavior>> predecessors = new HashMap<Node, List<Behavior>>();
    
    /**
     * For each key, the key is in this behavior's add list and indexes all the 
     * behaviors which have the key in their precondition
     */
    private Map<Node, List<Behavior>> successors = new HashMap<Node, List<Behavior>>();
    
    /**
     * Key is an element in this behavior's context.  It indexes all the behaviors which have 
     * the key element in their delete list
     */
    private Map<Node, List<Behavior>> conflictors = new HashMap<Node, List<Behavior>>();

    private List<BehaviorCodelet> behaviorCodelets = new ArrayList<BehaviorCodelet>();
    private List<ExpectationCodelet> expectationCodelets = new ArrayList<ExpectationCodelet>();
   
    //TODO why would a behavior have a stream?  To spawn additional behaviors?
//   private Stream stream = null;
    
    public BehaviorImpl(Scheme s){
    	super(s);            
    	deactivatePreconditions();
    }
        
    public void excite(double excitation){        
        incomingActivation += Math.abs(excitation);
    }
    public void inhibit(double inhibition){       
        incomingActivation -= Math.abs(inhibition);                 
    }
    
    public void reinforce(double reinforcement){
        if(reinforcement >= 0.0 && reinforcement <= 1.0)
            baseLevelActivation = reinforcement;
        else
        	logger.log(Level.WARNING, "Reinforcement must be between 0 and 1 " + reinforcement);
    }
    
    //TODO this is like the synchronize method in the old pam
    public void merge(double amplificationFactor){        
        totalActivation += (amplificationFactor * baseLevelActivation) + incomingActivation;
        if(totalActivation < 0)
            totalActivation = 0;
        incomingActivation = 0;        
    }
    
    //TODO fix
    public void decay(double alpha){
        this.totalActivation = alpha;
        if(alpha < 0)
            alpha = 0;
    }
    
    public void resetActivation(){
        totalActivation = 0;
    }
     
    public void spreadExcitation(double phi, double gamma){           
        //logger.info("BEHAVIOR : EXCITATION " + name);
        if(isActive())
            spreadSuccessorActivation(phi, gamma);        
        else
            spreadPredecessorActivation();                
    }
    
    public void spreadInhibition(NodeStructure state, double gamma, double delta){
        //logger.info("BEHAVIOR : INHIBITION " + name);
        spreadConflictorActivation(state, gamma, delta);        
    }
  
    public void spreadSuccessorActivation(double phi, double gamma){           
        for(Node addProposition: successors.keySet()){
            List<Behavior> behaviors = successors.get(addProposition);
            for(Behavior successor: behaviors){
                if(!successor.containsPrecondition(addProposition)){
                	//TODO double check this
                    double granted = ((totalActivation * phi) / gamma) / (behaviors.size() * successor.getPreconditions().size());
                    successor.excite(granted);

                    logger.info("\t:+" + name + "-->" + granted + " to " +
                                    successor + " for " + addProposition);
                }                
            }
        }        
    }//method
    
    public boolean containsPrecondition(Node prop){
    	return preconditions.get(prop) == true;
    }
    
    public void spreadPredecessorActivation(){             
        for(Node precondition: preconditions.keySet()){
            if(!this.containsPrecondition(precondition)){
            	List<Behavior> behaviors = predecessors.get(precondition);     
            	for(Behavior predecessor: behaviors){
            		double granted = (totalActivation / predecessor.getAddList().size())/behaviors.size();                        
                    predecessor.excite(granted);
                    logger.info("\t:+" + totalActivation + " " + name + "<--" + granted + " to " +
                                        predecessor + " for " + precondition);
                    
                }
            }
        }        
    }
    
    //TODO Double check I converted this monster correctly
    public void spreadConflictorActivation(NodeStructure state, double gamma, double delta){
        double fraction = delta / gamma;
        for(Node precondition: preconditions.keySet()){
            //if(((Boolean)(preconditions.get(precondition))).booleanValue())
            if(state.hasNode(precondition)){
                List<Behavior> behaviors = conflictors.get(precondition); 
                for(Behavior conflictor: behaviors){
                	boolean mutualConflict = false;
                    double inhibited = (totalActivation * fraction) / (behaviors.size() * conflictor.getDeleteList().size());
                    
                    Set<Node> preconds = conflictor.getPreconditions();
                    for(Node conflictorPreCondition: preconds){
                    	if(!conflictor.containsPrecondition(conflictorPreCondition)){
                    		for(Node o2: deleteList){
                    			if(conflictorPreCondition.equals(o2)){
                    				mutualConflict = true;
                    				break;
                    			}
                    		}
                    	}
                        if(mutualConflict)
                        	break;
                    }   
                    if(mutualConflict){
                        if(conflictor.getAlpha() < this.getAlpha()){
                                conflictor.inhibit(inhibited);
                                logger.info("\t:-" + name + "---" + 
                                                inhibited + " to " + conflictor 
                                                + " for " + precondition);                                
                        }
                    }else{
                            conflictor.inhibit(inhibited);
                            logger.info("\t:-" + name + "---" + inhibited + 
                                            " to " + conflictor + " for " + precondition);                                
                    }
                    
                }//for behaviors
            }                
        }//for preconditions        
    }//method    
    
    public boolean isActive(){
        for(Boolean b: preconditions.values()){
        	if(b == false)
        		return false;
        }
        return true;
    }    
        
    public void deactivatePreconditions(){                
        for(Node s: preconditions.keySet())
        	preconditions.put(s, false);       
    }
    
    public void prepareToFire(NodeStructure state){
        logger.info("BEHAVIOR : PREPARE TO FIRE " + name);
        
        
        //TODO find out what the properties are for
//        for(BehaviorCodelet codelet: behaviorCodelets){
//        	Map<String, String> properties = codelet.getProperties();
//            for(String name: properties.keySet()){
//                String value = state.getNode(name).getLabel();
//                if(value != null)
//                    codelet.addProperty(name, value );
//            }
//        }        
    }
    
// start add methods    
    public void addPrecondition(Node precondition){
        if(!preconditions.containsKey(precondition))
        	preconditions.put(precondition, false);            
    }
    
    public void addAddCondition(Node addCondition){
    	addList.add(addCondition);
    }
    
    public void addDeleteCondition(Node deleteCondition){
    	deleteList.add(deleteCondition);
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
    
    public void addBehaviorCodelet(BehaviorCodelet behaviorCodelet){
    	behaviorCodelets.add(behaviorCodelet);
    }    
    
    public void addExpectationCodelet(ExpectationCodelet expectationCodelet){
    	expectationCodelets.add(expectationCodelet);
    }     
// end add methods    
    
// start get methods.          
    
    public String toString(){
        return name + " schm " + super.toString();
    }    
    
    public double getAlpha(){
        return totalActivation;
    }
    
    public double getBaseLevelActivation(){
        return baseLevelActivation;
    }        
    
//    public Map<Node, Boolean> getPreconditions(){
//        return preconditions;
//    }
    
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
    
    
    public List<BehaviorCodelet> getBehaviorCodelets(){
        return behaviorCodelets;
    } 
    public List<ExpectationCodelet> getExpectationCodelets(){
        return expectationCodelets;
    }
    
//    private Stream getStream(){
//        return stream;
//    }
//end get methods    
    
//start set methods    
    
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
    
    public void setBehaviorCodelets(List<BehaviorCodelet> behaviorCodelets){
    	this.behaviorCodelets = behaviorCodelets;
    }
    
    public void setExpectationCodelets(List<ExpectationCodelet> expectationCodelets){
    	this.expectationCodelets = expectationCodelets;
    }
    
//    public void setStream(Stream stream){
//    	this.stream = stream;
//    }    
//    
//    public SidneyCodelet getCodelet(String name){
//        SidneyCodelet codelet = getBehaviorCodelet(name);
//        if(codelet != null)
//        	return codelet;
//        else
//            return getExpectationCodelet(name);    
//    }
    
//    public SidneyCodelet getBehaviorCodelet(String name){
//    	for(SidneyCodelet sc: behaviorCodelets)
//            if(sc.getName().compareTo(name) == 0)
//            	return sc;
//        return null;            
//    }
    
//    public SidneyCodelet getExpectationCodelet(String name){
//    	for(SidneyCodelet sc: expectationCodelets)
//            if(sc.getName().compareTo(name) == 0)
//            	return sc;
//        return null; 
//    }

	public void addConflictors(Node precondition1, List<Behavior> behaviors) {
		conflictors.put(precondition1, behaviors);
	}

	@Override
	public double getPreconditionCount() {
		return preconditions.size();
	}

	@Override
	public List<Behavior> getPredecessors(Node precondition) {
		return predecessors.get(precondition);
	}

	@Override
	public void addPredecessors(Node precondition, List<Behavior> behaviors) {
		predecessors.put(precondition, behaviors);
	}

	@Override
	public List<Behavior> getSuccessors(Node addProposition) {
		return successors.get(addProposition);
	}

	@Override
	public void addSuccessors(Node addProposition, List<Behavior> behaviors) {
		successors.put(addProposition, behaviors);
	}

	@Override
	public void satisfyPrecondition(Node proposition) {
		preconditions.put(proposition, true);
	}

}


