/*
 * ProtectedGoal.java
 *
 * Created on December 15, 2003, 4:48 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

public class ProtectedGoal extends Goal{
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.ProtectedGoal");
    private Map<Node, List<Behavior>> inhibitoryPropositions = new HashMap<Node, List<Behavior>>();    
    
    public ProtectedGoal(String name){
        super(name);
    }
    
    public ProtectedGoal(String name, boolean persistent){
        super(name, persistent);
    }
    
    public void grantActivation(double delta, double gamma, NodeStructure state){        
        super.grantActivation(gamma);
        if(super.isActive()){
        	
            logger.info("GOAL : INHIBITION " + name);
            
            for(Node deleteProposition: inhibitoryPropositions.keySet()){
            	if(state.hasNode((Node) deleteProposition)){ //TODO this is backwards?
            		List<Behavior> behaviors = inhibitoryPropositions.get(deleteProposition);
                    if(behaviors.size() > 0){
                        double inhibited = delta / behaviors.size();

                        for(Behavior behavior: behaviors){
                            behavior.inhibit(inhibited / behavior.getDeleteList().size());
                            logger.info("\t<--" + behavior.toString() + " " + inhibited / behavior.getAddList().size() + " for " + deleteProposition);
                           
                        }
                    }
                }
            }//for
        }//if        
    }
    
    public Map<Node, List<Behavior>> getInhibitoryPropositions(){
        return inhibitoryPropositions;
    }
    public void setInhibitoryPropositions(Map<Node, List<Behavior>> inhibitoryPropositions){
    	this.inhibitoryPropositions = inhibitoryPropositions;
    }

	public boolean containsExcitatoryProposition(Node proposition) {
		return getExcitatoryPropositions().containsKey(proposition);		
	}     
 
}
