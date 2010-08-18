/*
 * ProtectedGoal.java
 *
 * Created on December 15, 2003, 4:48 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Node;

public class ProtectedGoal extends Goal{
	
	@SuppressWarnings("unused")
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.main.ProtectedGoal");
    private Map<Node, List<Behavior>> inhibitoryPropositionMap = new HashMap<Node, List<Behavior>>();    
    
    public ProtectedGoal(String name){
        super(name);
    }
    
    public ProtectedGoal(String name, boolean persistent){
        super(name, persistent);
    }
    
    public Map<Node, List<Behavior>> getInhibitoryPropositionMap(){
    	return inhibitoryPropositionMap;
    }
    
    public Set<Node> getInhibitoryPropositions(){
        return inhibitoryPropositionMap.keySet();
    }
    public void setInhibitoryPropositions(Map<Node, List<Behavior>> inhibitoryPropositions){
    	this.inhibitoryPropositionMap = inhibitoryPropositions;
    }

	public boolean containsExcitatoryProposition(Node proposition) {
		return getExcitatoryPropositions().contains(proposition);		
	}

	public List<Behavior> getInhibitoryBehaviors(Node deleteProposition) {
		return inhibitoryPropositionMap.get(deleteProposition);
	}     
 
}
