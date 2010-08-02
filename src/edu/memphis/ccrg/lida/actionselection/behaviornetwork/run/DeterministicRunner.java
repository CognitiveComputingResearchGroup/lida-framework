/*
 * DeterministicRunner.java
 *
 * Sidney
 * Created on March 19, 2004, 1:58 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.run;

import java.io.IOException;
import java.util.*;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main2.Goal;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.util.EnvironmentReader;

public class DeterministicRunner extends Runner
{           
    public DeterministicRunner() 
    {
        super();
    }                 
    
    public void updateState()
    {
        if(winner != null)
        {
            Iterator li = winner.getAddList().iterator();
            while(li.hasNext())
            {                        
                state.put(li.next(), Boolean.TRUE);
            }

            li = winner.getDeleteList().iterator();
            while(li.hasNext())
            {                           
                state.remove(li.next(), Boolean.TRUE);
            }
        }
    }    
    
    
    public void updateGoals()
    {
        if(winner != null)
        {
            Iterator li = winner.getAddList().iterator();
            while(li.hasNext())
            {
                Object proposition = li.next();   
                for(Enumeration e = goals.keys(); e.hasMoreElements();)
                {
                    Goal goal = net.getGoal((String)e.nextElement());                            
                    if( goal != null)
                    {                        
                        if(goal.getExcitatoryPropositions().containsKey(proposition))
                        {                            
                            goals.remove(goal.getName());
                        }
                    }
                }
            }
        }        
    }
        
    
    public static void main(String[] args)
    {
        //String XML_FILE = "/home/sdmello/bnet/test/mars/dat/net.xml";       
        //String ENV_FILE = "/home/sdmello/bnet/test/mars/dat/env.dat";
        
        String XML_FILE = "C:\\projects\\bnet\\test\\maes\\dat\\net.xml";
        String ENV_FILE = "C:\\projects\\bnet\\test\\maes\\dat\\env.dat";
        
        DeterministicRunner runner = new DeterministicRunner();                
        runner.load(XML_FILE);                
                        
        EnvironmentReader reader = new EnvironmentReader(ENV_FILE);
        System.out.println(reader.getGoals());
                        
        runner.initialize(reader.getState(), reader.getGoals());
        runner.run();                        
        System.out.println(runner.getRunSummary());
        runner.report();               
        
        runner.finalize();
    }
}
