/*
 * ExpectationCodelet.java
 *
 * Created on December 26, 2003, 3:28 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;


import java.util.*;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Environment;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.SidneyCodelet;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

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
    
    public void execute(NodeStructure e)
    {
        evaluate(e);
    }
    
    /**
     * Iterate throught the add list of this codelets behavior.
     * If a proposition in the add list matches the current state then increment this codelets performance
     * else decrement it.
     * 
     * @param e
     */
    protected void evaluate(NodeStructure e)
    {
        performance = 0;
                
//        Iterator li = behavior.getAddList().iterator();
//        while(li.hasNext())
//        {            
//            Object proposition = li.next();
//            if(proposition.equals(e.get(proposition)))                        
//                performance ++;                        
//            else
//                performance --;
//        }        
    }                         
    
    public double getPerformance()
    {
        return performance;
    }    
}
