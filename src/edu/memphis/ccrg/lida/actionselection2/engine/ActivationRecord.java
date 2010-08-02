/*
 * FiringHistory.java
 *
 * Sidney D'Mello
 * Created on July 21, 2004, 6:47 PM
 */

package edu.memphis.ccrg.lida.actionselection2.engine;

import java.util.*;

public class ActivationRecord 
{    
    private String behaviorName;
    
    private long timeStamp;
    private long cycle;
    
    private double alpha;
    private double beta;

    private boolean active;
    private boolean fired;    
           
    public ActivationRecord(Behavior behavior)
    {
        if(behavior != null)
        {
            behaviorName = behavior.getName();
            
            cycle = BehaviorNetwork.getCycle();
        
            alpha = behavior.getAlpha();
            beta = behavior.getBeta();
            
            active = behavior.isActive();
            
            if(BehaviorNetwork.getFiredBehavior() != null)
            {
                if(BehaviorNetwork.getFiredBehavior().equals(behavior))
                    fired = true;
                else
                    fired = false;
            }
        }        
        else
            throw new NullPointerException();
    }
    
    public String getBehaviorName()
    {
        return behaviorName;
    }
    
    public long getTimeStamp()
    {
        return timeStamp;
    }
    
    public double getAlpha()
    {
        return alpha;
    }
    
    public double getBeta()
    {
        return beta;
    }
    
    public boolean active()
    {
        return active;
    }
    
    public boolean fired()
    {
        return fired;
    } 
    
    public String toString()
    {
        return cycle + "\t" + behaviorName + "\t" + alpha + "\t" + beta + 
               "\t" + active + "\t" + fired;
    }
}
