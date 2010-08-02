/*
 * Environment.java
 *
 * Created on December 10, 2003, 6:41 PM
 */

package edu.memphis.ccrg.lida.actionselection2.engine;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Environment 
{        
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Environment");
	
    private Hashtable propositions;
    
    private static Hashtable state;
    private static Hashtable goals;
    
    public Environment()
    {
        propositions = new Hashtable();
        
        state = new Hashtable();        
        goals = new Hashtable();        
    }
    
/*
 *  Spreads activation to Behaviors in the propositions Hashtable for
 *  true propositions as specified by the state.
 *
 *  The state Hashtable consists of proposition as a key and value, as
 *  a value that may be reuired by the winning Behavior as an Object.
 *  
 *  For true preconditions with no value: "true" is the value.
 *  False preconditions do not appear in the state Hashtable.
 */    
    public void grantActivation()
    {
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
                    double granted = BehaviorNetwork.getPhi() / behaviors.size();
            
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
    
    public void updateGoals(Hashtable currentGoals) throws NullPointerException
    {
        if(currentGoals != null)
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
        else
            throw new NullPointerException();
    }
        
    public static Hashtable getCurrentState()
    {
        return state;
    }
    
    public static Hashtable getCurrentGoals()
    {
        return goals;
    }
    
    public static double getEnergy()
    {
        return BehaviorNetwork.getPhi();
    }
    
    public Hashtable getPropositions()
    {
        return propositions;
    }    
        
    public void setPropositions(Hashtable propositions)
                                throws NullPointerException
    {
        if(propositions != null)
            this.propositions = propositions;
        else
            throw new NullPointerException();
    }
    
    public boolean isPropositionTrue(Object proposition)
    {
        return state.containsKey(proposition);
    }
}
