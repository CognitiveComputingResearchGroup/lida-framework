/*
 * Behavior.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 5:35 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class Behavior{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Behavior");
	
    private String name;

    private double alphaActivation = 0.0;    
    private double betaActivation = 0.0; 
    private double incomingActivation = 0.0;
 
    //Strings represent "node values".  See parser
    private Map<String, Boolean> preconditions = new HashMap<String, Boolean>();
    private List<String> addList = new ArrayList<String>();
    private List<String> deleteList = new ArrayList<String>();        
    
    private Map<String, List<Behavior>> predecessors = new HashMap<String, List<Behavior>>();
    private Map<String, List<Behavior>> successors = new HashMap<String, List<Behavior>>();
    private Map<String, List<Behavior>> conflictors = new HashMap<String, List<Behavior>>();
    
    private List<BehaviorCodelet> behaviorCodelets = new ArrayList<BehaviorCodelet>();
    private List<ExpectationCodelet> expectationCodelets = new ArrayList<ExpectationCodelet>();
   
    //TODO why would a behavior have a stream?  To spawn additional behaviors?
//    private Stream stream = null;
    
    public Behavior(String name){
    	this.name = name;                 
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
        for(String addProposition: successors.keySet()){
            List<Behavior> behaviors = successors.get(addProposition);
            for(Behavior successor: behaviors){
                if(!((Boolean)(successor.getPreconditions().get(addProposition))).booleanValue()){
                	//TODO double check this
                    double granted = ((alphaActivation * phi) / gamma) / (behaviors.size() * successor.getPreconditions().size());
                    successor.excite(granted);

                    logger.info("\t:+" + name + "-->" + granted + " to " +
                                    successor + " for " + addProposition);
                }                
            }
        }        
    }//method
    
    public void spreadPredecessorActivation(){             
        for(String precondition: preconditions.keySet()){
        	boolean b = ((Boolean)(preconditions.get(precondition))).booleanValue();
            if(b == false){
            	List<Behavior> behaviors = predecessors.get(precondition);     
            	for(Behavior predecessor: behaviors){
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
        for(String precondition: preconditions.keySet()){
            //if(((Boolean)(preconditions.get(precondition))).booleanValue())
            if(state.getNode(precondition) != null || state.getLink(precondition) != null){
                List<Behavior> behaviors = conflictors.get(precondition); 
                for(Behavior conflictor: behaviors){
                	boolean mutualConflict = false;
                    double inhibited = (alphaActivation * fraction) / (behaviors.size() * conflictor.getDeleteList().size());
                    
                    Set<String> preconds = conflictor.getPreconditions().keySet();
                    for(String conflictorPreCondition: preconds){
                    	Boolean b2 = conflictor.getPreconditions().get(conflictorPreCondition);
                    	if(b2.booleanValue() == true){
                    		for(String o2: deleteList){
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
        for(String o: preconditions.keySet()){
        	boolean b = ((Boolean)preconditions.get(o)).booleanValue();
        	if(b == false)
        		return false;
        }
        return true;
    }    
        
    public void deactivate(){                
        for(String s: preconditions.keySet())
        	preconditions.put(s, new Boolean(false));       
    }
    
    public void prepareToFire(NodeStructure state){
        logger.info("BEHAVIOR : PREPARE TO FIRE " + name);
        
        for(BehaviorCodelet codelet: behaviorCodelets){
        	Map<String, String> properties = codelet.getProperties();
            for(String name: properties.keySet()){
                String value = state.getNode(name).getLabel();
                if(value != null)
                    codelet.addProperty(name, value );
            }
        }        
    }
    
// start add methods    
    public void addPrecondition(String precondition){
        if(precondition != null && !preconditions.containsKey(precondition))
        	preconditions.put(precondition, new Boolean(false));            
    }
    
    public void addAddCondition(String addCondition){
    	addList.add(addCondition);
    }
    
    public void addDeleteCondition(String deleteCondition){
    	deleteList.add(deleteCondition);
    }    
    
    public void addPredecessor(String precondition, Behavior predecessor){
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
    
    public void addSuccessor(String addProposition, Behavior successor){
        if(addProposition != null && successor != null){
            List<Behavior> list = successors.get(addProposition);
            if(list == null){              
                list = new ArrayList<Behavior>();
                successors.put(addProposition, list);
            }
            list.add(successor);
        }
    }
    
    public void addConflictor(String precondition, Behavior conflictor){
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
    
    public String getName()
    {
        return name;
    }    
    
    public double getAlpha()
    {
        return alphaActivation;
    }
    
    public double getBeta()
    {
        return betaActivation;
    }        
    
    public Map<String, Boolean> getPreconditions()
    {
        return preconditions;
    }
    
    public List<String> getAddList(){
        return addList;
    }
    
    public List<String> getDeleteList(){
        return deleteList;
    }
    
    public Map<String, List<Behavior>> getPredecessors()
    {
        return predecessors;
    }
    
    public Map<String, List<Behavior>> getSuccessors()
    {
        return successors;
    } 
    
    public Map<String, List<Behavior>> getConflictors()
    {
        return conflictors;
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
    
    public void setPreconditions(Map<String, Boolean> preconditions){
    	this.preconditions = preconditions;
    }
    
    public void setAddList(List<String> addList){
    	this.addList = addList;
    } 
    
    public void setDeleteList(List<String> deleteList){
    	this.deleteList = deleteList;
    }
    
    public void setPredecessors(Map<String, List<Behavior>> predecessors){
    	this.predecessors = predecessors;
    }    
    
    public void setSuccesors(Map<String, List<Behavior>> successors){
    	this.successors = successors;
    }    
    
    public void setConflictors(Map<String, List<Behavior>> conflictors){
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
    public SidneyCodelet getCodelet(String name){
        SidneyCodelet codelet = getBehaviorCodelet(name);
        if(codelet != null)
        	return codelet;
        else
            return getExpectationCodelet(name);    
    }
    
    public SidneyCodelet getBehaviorCodelet(String name){
    	for(SidneyCodelet sc: behaviorCodelets)
            if(sc.getName().compareTo(name) == 0)
            	return sc;
        return null;            
    }
    
    public SidneyCodelet getExpectationCodelet(String name){
    	for(SidneyCodelet sc: expectationCodelets)
            if(sc.getName().compareTo(name) == 0)
            	return sc;
        return null; 
    }
    
    public String toString(){
    	return name;
    }

	public long getActionId() {
		//TODO
		return 0;
	}
    
}


