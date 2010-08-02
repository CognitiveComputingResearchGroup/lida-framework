/*
 * ExpectationCodelet.java
 *
 * Created on December 26, 2003, 3:28 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;


import java.util.*;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main2.Environment;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main2.SidneyCodelet;

public class ExpectationCodelet extends SidneyCodelet
{        
    protected double performance;
    
    public ExpectationCodelet() 
    {
        super();
        setType(SidneyCodelet.EXPECTATION);
    }
    
    public ExpectationCodelet(String name)
    {
        super(name, SidneyCodelet.EXPECTATION);
    }
    
    public void execute()
    {
        evaluate();
    }
    
    protected void evaluate()
    {
        performance = 0;
                
        Iterator li = behavior.getAddList().iterator();
        while(li.hasNext())
        {            
            Object proposition = li.next();
            if(proposition.equals(Environment.getCurrentState().get(proposition)))                        
                performance ++;                        
            else
                performance --;
        }        
    }                         
    
    protected void initialize(Hashtable initializationData) 
    {
    }
    
    public double getPerformance()
    {
        return performance;
    }    
}
