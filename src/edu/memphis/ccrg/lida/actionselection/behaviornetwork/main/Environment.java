/*
 * Environment.java
 *
 * Created on December 10, 2003, 6:41 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Environment{ 
	
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Environment");
	
    private Map<Object, List<Behavior>> propositions = new HashMap<Object, List<Behavior>>();
    private Map<String, Object> state = new HashMap<String, Object>();
    private Map<String, Goal> goals = new HashMap<String, Goal>();
    
    public Environment(){      
    }
    
	/*
	 *  Spreads activation to Behaviors in the propositions Hashtable for
	 *  true propositions as specified by the state.
	 *
	 *  The state Hashtable consists of proposition as a key and value, as
	 *  a value that may be required by the winning Behavior as an Object.
	 *  
	 *  For true preconditions with no value: "true" is the value.
	 *  False preconditions do not appear in the state Hashtable.
	 */ 
    //Iterate through the propositions in the current state.
    //For each proposition get the behaviors indexed by that proposition
    //For each behavior, excite it an amount equal to (phi)/(num behaviors indexed at current proposition * # of preconditions in behavior)
    public void grantActivation(double phi){
        logger.info("ENVIRONMENT : EXCITATION");
        
        Iterator li = state.keySet().iterator();
        while(li.hasNext())
        {
            Object precondition = li.next();
            LinkedList behaviors = (LinkedList)propositions.get(precondition);
            
            if(behaviors != null)
            {
                if(behaviors.size() > 0)
                {
                    double granted = phi / behaviors.size();
            
                    Iterator lj = behaviors.iterator();            
                    while(lj.hasNext())
                    {
                        try 
                        {
                            Behavior behavior = (Behavior)lj.next();
                            behavior.getPreconditions().put(precondition, new Boolean(true));
                            behavior.excite(granted / behavior.getPreconditions().size());
                        
                            logger.info("\t-->" + behavior.getName() + " " + granted / behavior.getPreconditions().size() + " for " + precondition);
                        }
                        catch(ArithmeticException ae)
                        {
                            logger.log(Level.WARNING, ae.getMessage());
                            ae.printStackTrace();
                        }
                    }
                }
                else
                {
                    logger.warning("Proposition " + precondition + " has no behaviors linked");
                }
            }
            /*
            else
            {
                logger.warning("UNRECOGNIZED PROPOSITION : "  + precondition);
            }
            */
        }
    }
    
    public void updateState(Hashtable currentState) throws NullPointerException                               
    {
        if(currentState != null)
        {    
            state = currentState;
            
            logger.info("ENVIRONMENT: STATE UPDATE");
            Iterator i = state.keySet().iterator();
            while(i.hasNext())
            {
                String proposition = (String)i.next();
                logger.info("\t" + proposition + "-->" + state.get(proposition));
            }                        
        }
        else
            throw new NullPointerException();
    }        
    
    public void updateGoals(Hashtable<String, Goal> currentGoals) throws NullPointerException
    {
            goals = currentGoals;
            
            logger.info("ENVIRONMENT: GOAL UPDATE");
            Iterator i = goals.keySet().iterator();
            while(i.hasNext())
            {
                String goal = (String)i.next();
                logger.info(goal + "-->" + goals.get(goal));
            }                        

    }
        
    public Map<String, Object> getCurrentState(){
        return state;
    }
    
    public Map<String, Goal> getCurrentGoals(){
        return goals;
    }
    
    public double getEnergy(){
    	//TODO
    	return 0.4;
        //return BehaviorNetworkImpl.getPhi();
    }
    
    public Map<Object, List<Behavior>> getPropositions(){
        return propositions;
    }    
        
    public void setPropositions(Map<Object, List<Behavior>> propositions){
        this.propositions = propositions;
    }
    
    public boolean isPropositionTrue(Object proposition){
        return state.containsKey(proposition);
    }
}
