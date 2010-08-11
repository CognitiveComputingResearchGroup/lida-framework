/*
 * Behavior.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 5:35 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.HashMap;
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

    private double alphaActivation = 0.0;
    
    /**
     * Reinforcement activation?
     */
    private double betaActivation = 0.0; 
    
    /**
     * Positive or negative activation from other behaviors?
     */
    private double incomingActivation = 0.0;
 
    //TODO Integer values
    private Map<Node, Boolean> preconditions = new HashMap<Node, Boolean>();

    private List<Node> addList = new ArrayList<Node>();
    private List<Node> deleteList = new ArrayList<Node>();        
    
    private Map<Node, List<BehaviorImpl>> predecessors = new HashMap<Node, List<BehaviorImpl>>();
    private Map<Node, List<BehaviorImpl>> successors = new HashMap<Node, List<BehaviorImpl>>();
    private Map<Node, List<BehaviorImpl>> conflictors = new HashMap<Node, List<BehaviorImpl>>();

    private List<BehaviorCodelet> behaviorCodelets = new ArrayList<BehaviorCodelet>();
    private List<ExpectationCodelet> expectationCodelets = new ArrayList<ExpectationCodelet>();
   
    //TODO why would a behavior have a stream?  To spawn additional behaviors?
//    private Stream stream = null;
    
    public BehaviorImpl(Scheme s){
    	super(s);            
    	deactivate();
    }
        
    public void excite(double excitation){        
        incomingActivation += Math.abs(excitation);
    }
    public void inhibit(double inhibition){       
        incomingActivation -= Math.abs(inhibition);                 
    }
    
    public void reinforce(double reinforcement){
        if(reinforcement >= 0 && reinforcement <= 1)
            betaActivation = reinforcement;
        else
        	logger.log(Level.WARNING, "Reinforcement must be between 0 and 1 " + reinforcement);
    }
    
    public void merge(double omega){        
        alphaActivation = alphaActivation + (omega * betaActivation) + incomingActivation;
        if(alphaActivation < 0)
            alphaActivation = 0;
        incomingActivation = 0;        
    }
    
    public void decay(double alpha){
        this.alphaActivation = alpha;
        if(alpha < 0)
            alpha = 0;
    }
    
    public void resetActivation(){
        alphaActivation = 0;
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
            List<BehaviorImpl> behaviors = successors.get(addProposition);
            for(BehaviorImpl successor: behaviors){
                if(!successor.containsPrecondition(addProposition)){
                	//TODO double check this
                    double granted = ((alphaActivation * phi) / gamma) / (behaviors.size() * successor.getPreconditions().size());
                    successor.excite(granted);

                    logger.info("\t:+" + name + "-->" + granted + " to " +
                                    successor + " for " + addProposition);
                }                
            }
        }        
    }//method
    
    private boolean containsPrecondition(Node prop){
    	return preconditions.get(prop) == true;
    }
    
    public void spreadPredecessorActivation(){             
        for(Node precondition: preconditions.keySet()){
            if(!this.containsPrecondition(precondition)){
            	List<BehaviorImpl> behaviors = predecessors.get(precondition);     
            	for(BehaviorImpl predecessor: behaviors){
            		double granted = (alphaActivation / predecessor.getAddList().size())/behaviors.size();                        
                    predecessor.excite(granted);
                    logger.info("\t:+" + alphaActivation + " " + name + "<--" + granted + " to " +
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
                List<BehaviorImpl> behaviors = conflictors.get(precondition); 
                for(BehaviorImpl conflictor: behaviors){
                	boolean mutualConflict = false;
                    double inhibited = (alphaActivation * fraction) / (behaviors.size() * conflictor.getDeleteList().size());
                    
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
        
    public void deactivate(){                
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
    
    public void addPredecessor(Node precondition, BehaviorImpl predecessor){
        if(precondition != null && predecessor != null){
            List<BehaviorImpl> list = predecessors.get(precondition);            
            if(list == null){
                list = new ArrayList<BehaviorImpl>();
                predecessors.put(precondition, list);
            }
            list.add(predecessor);
        }
        else
            logger.log(Level.WARNING, "", LidaTaskManager.getActualTick());
    } 
    
    public void addSuccessor(Node addProposition, BehaviorImpl successor){
        if(addProposition != null && successor != null){
            List<BehaviorImpl> list = successors.get(addProposition);
            if(list == null){              
                list = new ArrayList<BehaviorImpl>();
                successors.put(addProposition, list);
            }
            list.add(successor);
        }
    }
    
    public void addConflictor(Node precondition, BehaviorImpl conflictor){
        if(precondition != null && conflictor != null){
            List<BehaviorImpl> list = conflictors.get(precondition);
            if(list == null){
                list = new ArrayList<BehaviorImpl>();
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
        return alphaActivation;
    }
    
    public double getBeta(){
        return betaActivation;
    }        
    
//    public Map<Node, Boolean> getPreconditions(){
//        return preconditions;
//    }
    
    public Set<Node> getPreconditions(){
        return preconditions.keySet();
    }
    
    public List<Node> getAddList(){
        return addList;
    }
    
    public List<Node> getDeleteList(){
        return deleteList;
    }
    
    public Map<Node, List<BehaviorImpl>> getPredecessors(){
        return predecessors;
    }
    
    public Map<Node, List<BehaviorImpl>> getSuccessors(){
        return successors;
    } 
    
    public Map<Node, List<BehaviorImpl>> getConflictors(){
        return conflictors;
    }
        

	public List<BehaviorImpl> getConflictors(Node precondition1) {
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
    
    public void setAddList(List<Node> addList){
    	this.addList = addList;
    } 
    
    public void setDeleteList(List<Node> deleteList){
    	this.deleteList = deleteList;
    }
    
    public void setPredecessors(Map<Node, List<BehaviorImpl>> predecessors){
    	this.predecessors = predecessors;
    }    
    
    public void setSuccesors(Map<Node, List<BehaviorImpl>> successors){
    	this.successors = successors;
    }    
    
    public void setConflictors(Map<Node, List<BehaviorImpl>> conflictors){
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
//    
//    public String toString(){
//    	return name;
//    }

	public long getSchemeActionId() {
		//TODO
		return 0;
	}

	public void addConflictors(Node precondition1, List<BehaviorImpl> behaviors) {
		conflictors.put(precondition1, behaviors);
	}

}


