/*
 * BehaviorCodelet.java
 *
 * Created on December 26, 2003, 3:25 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.util;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Environment;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.SidneyCodelet;

import java.util.*;

public class BehaviorCodelet extends SidneyCodelet
{    
    public BehaviorCodelet() 
    {
        super();
        setType(SidneyCodelet.BEHAVIOR);
    }
    
    public BehaviorCodelet(String name)
    {
        super(name, SidneyCodelet.BEHAVIOR);
    }
    
    public void execute(Environment e) 
    {
    }
    
    protected void initialize(Hashtable initializationData)
    {
    }

}
