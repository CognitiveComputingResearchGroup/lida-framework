/*
 * SensorialAnticipationRunner.java
 *
 * Created on November 10, 2006, 12:28 PM
 *
 * Sidney D'Mello
 * The University of Memphis
 * sdmello@memphis.edu
 */

package edu.memphis.ccrg.lida.actionselection2.exp.sensorial;

import java.util.Enumeration;
import java.util.Iterator;

import edu.memphis.ccrg.lida.actionselection2.engine.Goal;
import edu.memphis.ccrg.lida.actionselection2.run.DeterministicRunner;
import edu.memphis.ccrg.lida.actionselection2.run.Runner;
import edu.memphis.ccrg.lida.actionselection2.util.EnvironmentReader;

public class SensorialAnticipationRunner extends Runner
{
    public SensorialAnticipationRunner()
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
        String XML_FILE = "/home/sdmello/bnet/test/mars/dat/net.xml";       
        String ENV_FILE = "/home/sdmello/bnet/test/mars/dat/env.dat";
        
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
