/*
 * ParamenerSearch.java
 *
 * Created on March 21, 2005, 10:50 AM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.run;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.*;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.util.*;

import java.util.*;
import java.text.*;

public class ParameterSearch 
{     
        private Runner runner;
        private ParameterElaborator elaborator;
        
        private MultiMap state;
        private Hashtable goals;
        
        private Vector summaries; 
        
        public ParameterSearch(Runner runner, ParameterElaborator elaborator)
        {            
            if(runner != null && elaborator != null)
            {
                this.runner = runner;
                this.elaborator = elaborator;
                
                summaries = new Vector();
            }
            else 
                throw new NullPointerException();            
        }
        
        public void setInitializationData(MultiMap state, Hashtable goals)
        {
            this.state = state;
            this.goals = goals;
        }
        
        public Vector run()         
        {           
            summaries.clear();
            
            while(elaborator.hasNext())
            {
                double p[] = elaborator.next();
                
                runner.reset();
                runner.initialize(state, goals);
                runner.getNet().setConstants(p[0], p[1], p[2], p[3], p[4], 0.0);
                runner.run();
                
                System.out.print(p[0] + "," + p[1] + "," + p[2] + "," + p[3] + "," + p[4] + " --> ");
                System.out.println(runner.getRunSummary().successful());
                if(runner.getRunSummary().successful())
                {                    
                    System.out.println(runner.getRunSummary().getFirings());
                    //System.exit(0);
                }
                
                summaries.add(runner.getRunSummary());
            }
         
            /*
            for(Iterator i = summaries.iterator(); i.hasNext();)
                System.out.println((RunSummary)i.next());
             */
            
            return summaries;
        }                
        
        
        public static void main(String[] args)
        {
            String XML_FILE = "/home/sdmello/bnet/test/mars/dat/net.xml";               
            String ENV_FILE = "/home/sdmello/bnet/test/mars/dat/env.dat";
        
            DeterministicRunner runner = new DeterministicRunner();                
            runner.load(XML_FILE);                
                        
            EnvironmentReader reader = new EnvironmentReader(ENV_FILE);
            System.out.println(reader.getGoals());
        
            ParameterElaborator elaborator = new ParameterElaborator(5, 0.5);
                                                                   
            ParameterSearch search = new ParameterSearch(runner, elaborator);
            search.setInitializationData(reader.getState(), reader.getGoals());
            search.run();
        }    
     }
