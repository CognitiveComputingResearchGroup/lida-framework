/*
 * MarsRunner.java
 *
 * Created on June 9, 2005, 7:04 PM
 */

package edu.memphis.ccrg.lida.actionselection2.exp;

import edu.memphis.ccrg.lida.actionselection2.run.DeterministicRunner;
import edu.memphis.ccrg.lida.actionselection2.util.EnvironmentReader;

import java.util.*;

public class MarsRunner extends DeterministicRunner
{    
    public final static int ROCKS = 2;
    
    private int rocks = 0;
    
    public MarsRunner() 
    {
        super();
    }
    
    /*
    public void updateGoals()
    {
        
    }
    
    public boolean satisfied()
    {    
        if(winner == null)
            return false;
        else if(winner.getName().equals("test-rock"))
        {
            rocks ++;
        
            if(rocks == ROCKS)
            {
                System.out.println(rocks);
                return true;
            }
            else
                return false;
        }
        else
            return false;
    }*/
            
        /*
        System.out.println(goals);
        if(super.satisfied())
        {            
            rocks ++;            
            if(rocks == ROCKS)
            {             
                System.out.println(rocks);
                rocks = 0;
                return true;
            }
            else
            {
                goals.put("pick-up-rocks", new Boolean(true));                
                return false;
            }
        }
        else
            return false;
         */
        
    
    public static void main(String[] args)
    {
        //String XML_FILE = "/home/sdmello/bnet/test/mars/dat/net.xml";               
        //String ENV_FILE = "/home/sdmello/bnet/test/mars/dat/env.dat";
        
        String XML_FILE = "C:\\Documents and Settings\\sdmello\\Desktop\\bnet\\test\\maes\\dat\\net.xml";
        String ENV_FILE = "C:\\Documents and Settings\\sdmello\\Desktop\\bnet\\test\\maes\\dat\\env.dat";
        
        //MarsRunner runner = new MarsRunner();                
        DeterministicRunner runner = new DeterministicRunner();
        runner.load(XML_FILE);                
                        
        EnvironmentReader reader = new EnvironmentReader(ENV_FILE);
        runner.initialize(reader.getState(), reader.getGoals());
        
        System.out.println(reader.getState() + "\t" + reader.getGoals());
        
        /*
        ParameterElaborator elaborator = new ParameterElaborator(5, 0.5);                                                                   
        ParameterSearch search = new ParameterSearch(runner, elaborator);
        search.setInitializationData(reader.getState(), reader.getGoals());
        search.run();
        */
        //0.0,0.0,0.5,0.0,0.5
        
        runner.getNet().setConstants(0.0, 0.0, 0.5, 0.0, 0.5, 0.0);
        runner.run();
    }    
    
}
