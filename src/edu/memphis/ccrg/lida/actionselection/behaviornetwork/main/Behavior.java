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

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

public class Behavior{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Behavior");
	
    private String name;

    private double alpha = 0.0;    
    private double beta = 0.0; 
    private double incoming = 0.0;
 
    private Map<Object, Boolean> preconditions = new HashMap<Object, Boolean>();
    private List<Object> addList = new ArrayList<Object>();
    private List<Object> deleteList = new ArrayList<Object>();        
    
    private Map<Object, List<Behavior>> predecessors = new HashMap<Object, List<Behavior>>();
    private Map<Object, List<Behavior>> successors = new HashMap<Object, List<Behavior>>();
    private Map<Object, List<Behavior>> conflictors = new HashMap<Object, List<Behavior>>();
    
    private List<BehaviorCodelet> behaviorCodelets = new ArrayList<BehaviorCodelet>();
    private List<ExpectationCodelet> expectationCodelets = new ArrayList<ExpectationCodelet>();
   
    private Stream stream = null;
    
    public Behavior(String name){
    	this.name = name;                 
        deactivate();
    }
        
    public void excite(double excitation){        
        incoming += Math.abs(excitation);
    }
    public void inhibit(double inhibition){       
        incoming -= Math.abs(inhibition);                 
    }
    
    public void reinforce(double reinforcement){
        if(reinforcement >= 0 && reinforcement <= 1)
            beta = reinforcement;
        else
        	logger.log(Level.WARNING, "Reinforcement must be between 0 and 1 " + reinforcement);
    }
    
    public void merge(double omega){        
        alpha = alpha + (omega * beta) + incoming;
        if(alpha < 0)
            alpha = 0;
        incoming = 0;        
    }
    
    public void decay(double alpha){
        this.alpha = alpha;
        if(alpha < 0)
            alpha = 0;
    }
    
    public void resetActivation(){
        alpha = 0;
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
        for(Object addProposition: successors.keySet()){
            List<Behavior> behaviors = successors.get(addProposition);
            for(Behavior successor: behaviors){
                if(!((Boolean)(successor.getPreconditions().get(addProposition))).booleanValue()){
                	//TODO double check this
                    double granted = ((alpha * phi) / gamma) / (behaviors.size() * successor.getPreconditions().size());
                    successor.excite(granted);

                    logger.info("\t:+" + name + "-->" + granted + " to " +
                                    successor + " for " + addProposition);
                }                
            }
        }        
    }//method
    
    public void spreadPredecessorActivation(){             
        for(Object precondition: preconditions.keySet()){
        	boolean b = ((Boolean)(preconditions.get(precondition))).booleanValue();
            if(b == false){
            	List<Behavior> behaviors = predecessors.get(precondition);     
            	for(Behavior predecessor: behaviors){
            		double granted = (alpha / predecessor.getAddList().size())/behaviors.size();                        
                    predecessor.excite(granted);
                    logger.info("\t:+" + alpha + " " + name + "<--" + granted + " to " +
                                        predecessor + " for " + precondition);
                    
                }
            }
        }        
    }
    
    //TODO Double check I converted this monster correctly
    public void spreadConflictorActivation(NodeStructure state, double gamma, double delta){
        double fraction = delta / gamma;
        for(Object precondition: preconditions.keySet()){
            //if(((Boolean)(preconditions.get(precondition))).booleanValue())
            if(state.hasNode((Node) precondition) || state.hasLink((Link) precondition)){
                List<Behavior> behaviors = conflictors.get(precondition); 
                for(Behavior conflictor: behaviors){
                	boolean mutualConflict = false;
                    double inhibited = (alpha * fraction) / (behaviors.size() * conflictor.getDeleteList().size());
                    
                    Set<Object> preconds = conflictor.getPreconditions().keySet();
                    for(Object conflictorPreCondition: preconds){
                    	Boolean b2 = conflictor.getPreconditions().get(conflictorPreCondition);
                    	if(b2.booleanValue() == true){
                    		for(Object o2: deleteList){
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
        for(Object o: preconditions.keySet()){
        	boolean b = ((Boolean)preconditions.get(o)).booleanValue();
        	if(b == false)
        		return false;
        }
        return true;
    }    
        
    public void deactivate(){                
        for(Object o: preconditions.keySet())
        	preconditions.put(o, new Boolean(false));       
    }
    
    public void prepareToFire(NodeStructure state){
        logger.info("BEHAVIOR : PREPARE TO FIRE " + name);
        
        for(BehaviorCodelet codelet: behaviorCodelets){
        	Map<Object, Object> properties = codelet.getProperties();
            for(Object name: properties.keySet()){
                Object value = ((Hashtable) state).get(name);
                //TODO ???? Check original code.
                if(value != null)
                    codelet.addProperty(name, value );
            }
        }        
    }
    
// start add methods    
    public void addPrecondition(Object precondition){
        if(precondition != null && !preconditions.containsKey(precondition))
                preconditions.put(precondition, new Boolean(false));            
    }
    
    public void addAddCondition(Object addCondition){
    	addList.add(addCondition);
    }
    
    public void addDeleteCondition(Object deleteCondition){
    	deleteList.add(deleteCondition);
    }    
    
    public void addPredecessor(Object precondition, Behavior predecessor){
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
    
    public void addSuccessor(Object addProposition, Behavior successor){
        if(addProposition != null && successor != null){
            List<Behavior> list = successors.get(addProposition);
            if(list == null){              
                list = new ArrayList<Behavior>();
                successors.put(addProposition, list);
            }
            list.add(successor);
        }
    }
    
    public void addConflictor(Object precondition, Behavior conflictor){
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
        return alpha;
    }
    
    public double getBeta()
    {
        return beta;
    }        
    
    public Map<Object, Boolean> getPreconditions()
    {
        return preconditions;
    }
    
    public List<Object> getAddList()
    {
        return addList;
    }
    
    public List<Object> getDeleteList()
    {
        return deleteList;
    }
    
    public Map<Object, List<Behavior>> getPredecessors()
    {
        return predecessors;
    }
    
    public Map<Object, List<Behavior>> getSuccessors()
    {
        return successors;
    } 
    
    public Map<Object, List<Behavior>> getConflictors()
    {
        return conflictors;
    }
        
    public List<BehaviorCodelet> getBehaviorCodelets(){
        return behaviorCodelets;
    } 
    public List<ExpectationCodelet> getExpectationCodelets(){
        return expectationCodelets;
    }
    
    private Stream getStream(){
        return stream;
    }
//end get methods    
    
//start set methods    
    
    public void setPreconditions(Map<Object, Boolean> preconditions){
    	this.preconditions = preconditions;
    }
    
    public void setAddList(List<Object> addList){
    	this.addList = addList;
    } 
    
    public void setDeleteList(List<Object> deleteList){
    	this.deleteList = deleteList;
    }
    
    public void setPredecessors(Map<Object, List<Behavior>> predecessors){
    	this.predecessors = predecessors;
    }    
    
    public void setSuccesors(Map<Object, List<Behavior>> successors){
    	this.successors = successors;
    }    
    
    public void setConflictors(Map<Object, List<Behavior>> conflictors){
    	this.conflictors = conflictors;
    }    
    
    public void setBehaviorCodelets(List<BehaviorCodelet> behaviorCodelets){
    	this.behaviorCodelets = behaviorCodelets;
    }
    
    public void setExpectationCodelets(List<ExpectationCodelet> expectationCodelets){
    	this.expectationCodelets = expectationCodelets;
    }
    
    public void setStream(Stream stream){
    	this.stream = stream;
    }    
    
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
    
    public void reset(){
        alpha = 0;
        beta = 0;
        incoming = 0;        
        deactivate();
    }
    
    public String toString(){
    	return name;
    }

	public long getActionId() {
		// TODO Auto-generated method stub
		return 0;
	}
    
}


