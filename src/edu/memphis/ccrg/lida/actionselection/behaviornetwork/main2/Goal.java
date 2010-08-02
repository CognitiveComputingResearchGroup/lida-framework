/*
 * Goal.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:29 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main2;

import java.util.*;
import java.util.logging.Logger;

public class Goal 
{   
	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Goal");
	
    protected String name;
    protected boolean persistent;    
    
    private Hashtable excitatoryPropositions;           
    
    private boolean active;
    
    public Goal(String name) throws NullPointerException
    {        
        if(name != null)
        {
            this.name = name;
            excitatoryPropositions = new Hashtable();                        
            
            persistent = true;                        
            activate();
        }
        else
            throw new NullPointerException();        
    }    
    
    public Goal(String name, boolean persistent) throws NullPointerException               
    {
        if(name != null)
        {
            this.name = name;
            this.persistent = persistent;
            if(persistent)
                active = true;
            else
                active = false;
            
            excitatoryPropositions = new Hashtable();            
        }
        else
            throw new NullPointerException();        
    }    
    
    public boolean willSatisfy(Object proposition)
    {
        if(proposition != null)
        {
            return excitatoryPropositions.containsKey(proposition);
        }
        else 
            throw new NullPointerException();
    }
    
    public void activate()
    {
        logger.info("GOAL : " + name + "ACTIVATED");
        active = true;
    }
    
    public void deactivate()
    {
        if(!persistent)
        {
            logger.info("GOAL : " + name + "DEACTIVATED");
            active = false;
        }
        else
        {
            logger.warning("DEACTIVATION FAILED " + name + " PERSISTENT");
        }
    }
    
    public void grantActivation()
    {        
        if(active)
        {
            logger.info("GOAL : EXCITATION " + name);
            
            Iterator iterator = excitatoryPropositions.keySet().iterator();
            while(iterator.hasNext())
            {
                Object addProposition = iterator.next();
                LinkedList behaviors = (LinkedList)excitatoryPropositions.get(addProposition);

                if(behaviors.size() > 0)
                {
                    double granted = BehaviorNetworkImpl.getGamma() / behaviors.size();

                    Iterator li = behaviors.iterator();            
                    while(li.hasNext())
                    {
                        try 
                        {
                            Behavior behavior = (Behavior)li.next();
                            behavior.excite(granted / behavior.getAddList().size());
                            logger.info("\t-->" + behavior.getName() + " " + granted / behavior.getAddList().size() + " for " + addProposition);
                        }
                        catch(ArithmeticException ae)
                        {
                            ae.printStackTrace();
                        }
                    }
                }
            }//while
        }
    }
    

    public String getName()
    {
        return name;
    }
    
    public boolean isActive()
    {
        return active;
    }
    
    public boolean isPersistent()
    {
        return persistent;
    }
    
    public static double getExcitatoryStrength()
    {
        return BehaviorNetworkImpl.getGamma();
    }
    
    public Hashtable getExcitatoryPropositions()
    {
        return excitatoryPropositions;
    }
    
    public void setExcitatoryPropositions(Hashtable excitatoryPropositions) throws NullPointerException                                  
    {
        if(excitatoryPropositions != null)
            this.excitatoryPropositions = excitatoryPropositions;
        else
            throw new NullPointerException();
    }
    
    public String toString()
    {
        return name;
    }
}
