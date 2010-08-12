/*
 * Goal.java
 *
 * Sidney D'Mello, Ryan McCall
 * Created on December 10, 2003, 6:29 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Node;

public class Goal{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Goal");
	
	//TODO: change to ID
    protected String name;
    protected boolean persistent = true;    
    
    private Map<Node, List<Behavior>> excitatoryPropositions = new HashMap<Node, List<Behavior>>();           
    
    private boolean active;
    
    public Goal(String name){        
        this.name = name;                             
        activate();
    }    
    
    public Goal(String name, boolean persistent){
    	this.name = name;
        this.persistent = persistent;
        active = persistent;
    }    
    
    public boolean willSatisfy(Object proposition){
        return excitatoryPropositions.containsKey(proposition);
    }
    
    public void activate(){
        logger.info("GOAL : " + name + "ACTIVATED");
        active = true;
    }
    
    public void deactivate(){
        if(!persistent){
            logger.info("GOAL : " + name + "DEACTIVATED");
            active = false;
        }else
           logger.warning("DEACTIVATION FAILED " + name + " PERSISTENT");
    }
    
    public void grantActivation(double gamma)
    {        
        if(active)//TODO check before calling this method
        {
            logger.info("GOAL : EXCITATION " + name);
            
            for(Object addProposition: excitatoryPropositions.keySet()){
                List<Behavior> behaviors = excitatoryPropositions.get(addProposition);

                if(behaviors.size() > 0){
                    double granted = gamma / behaviors.size();

                    for(Behavior behavior: behaviors){
                        behavior.excite(granted / behavior.getAddList().size());
                        logger.info("\t-->" + behavior.toString() + " " + granted / behavior.getAddList().size() + " for " + addProposition);
                    }
                }
            }//for each proposition
        }
    }

    public String getName(){
        return name;
    }
    public boolean isActive(){
        return active;
    }
    public boolean isPersistent(){
        return persistent;
    }
    
    public Map<Node, List<Behavior>> getExcitatoryPropositions(){
        return excitatoryPropositions;
    }
    
    public void setExcitatoryPropositions(Map<Node, List<Behavior>> excitatoryPropositions){
    	this.excitatoryPropositions = excitatoryPropositions;
    }
    
    public String toString(){
        return name;
    }
}
