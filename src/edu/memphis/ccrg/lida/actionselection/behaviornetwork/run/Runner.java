/*
 * Runner.java
 *
 * Sidney D'Mello
 * Created on March 18, 2004, 7:15 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.run;

import java.io.*;
import java.util.*;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorNetworkImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.util.CycleInformation;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.util.MultiMap;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.util.Parser;

public abstract class Runner 
{
    public final static int MAX_CYCLES  = 100;
    
    public final static String SYSTEM   = "system.log";
    public final static String ERROR    = "error.log";
    public final static String WARNING  = "warning.log";
    public final static String INFO     = "info.log";
    
    private int run;
    
    protected BehaviorNetworkImpl net;
    protected Behavior winner;
    
    protected MultiMap state;
    protected Hashtable goals;  
    
    protected Vector trace;    
    protected RunSummary summary;
    
    public Runner() 
    {
        run = 0;
        
        net = new BehaviorNetworkImpl();
        
        state = new MultiMap();
        goals = new Hashtable();
        
        reset();
    }
    
    public abstract void updateState();
    public abstract void updateGoals();      
    
/*  Updates the internal data structures as well as propogates
 *  the updated state and environment to the net.
 *
 *  Should be envoked exactly once per cycle. 
 */
    public void update()
    {
        updateState();
        updateGoals();
        
        net.updateState(state);            
        net.updateGoals(goals);
    }        
    
    public void load(String netXMLFileName)
    {
        Parser parser = new Parser();
  //      net = parser.parse(netXMLFileName);
        
        net.link();
        
        if(net == null)
        {
            System.err.println("ERROR: LOADING NET FROM " + netXMLFileName);
        }
    }
    
    public void enableLogging()
    {                
        try
        {
//            LogManager.addLogger(new Logger(new FileWriter(SYSTEM), Logger.SYSTEM));
//            LogManager.addLogger(new Logger(new FileWriter(ERROR), Logger.ERROR));
//            LogManager.addLogger(new Logger(new FileWriter(WARNING), Logger.WARNING));
//            LogManager.addLogger(new Logger(new FileWriter(INFO), Logger.INFO));
//            
//            LogManager.openConnections();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }         
    }
    
/*  This method should be used ONLY ONCE PER RUN.
 *  It basically a way to specify initial states and
 *  goals at the begining of a simulation.
 */    
        
    public void initialize(MultiMap state, Hashtable goals)
    {
        if(state != null && goals != null)
        {            
            this.goals = (Hashtable)goals.clone();
            this.state = (MultiMap)state.clone();                                    
            
        }
        else
            throw new NullPointerException();                    
    }        
    
/*  Runs the net until all the goals are completed, i.e. satisfied()
 *  return true or the MAX_CYCLES is not exceeded.
 * 
 */    
    
    public void run()
    {                
        net.reset();          
        summary.start();
        
        int cycle = 0;
        boolean success = false;
        
        //net.updateState(state);
        //net.updateGoals(goals);
        //System.out.println(net.getEnvironment().getCurrentState() + "\t" + net.getEnvironment().getCurrentGoals());
        while(!success && cycle < MAX_CYCLES)
        {   
            update(); 
            runOneCycle();                                    
                                   
            if (!satisfied())            
                cycle ++;            
            else            
                success = true;                            
        }        
        summary.stop();
        
        summary.setCycles(cycle);        
        if(success)
            summary.markSuccess();
        else
            summary.markFailure();            
        
        System.out.println(getRunSummary());
        
        run ++;
    }
  
/*
 *  Runs the net for a specified number of cycles.
 *  The execution continues, enev if satisfied() returns true.
 *  However, he first time the satisfied() returns true, the 
 *  RunSummary object is updated.
 */    
    
    public void run(int cycles)
    {                
        net.reset();        
        summary.start();
        
        for(int i = 0; i < cycles; i++)
        {                        
            update();
            runOneCycle();
            
            if(satisfied())                            
                summary.markSuccess();           
        }
        summary.stop();        
        summary.setCycles(cycles);
        
        run ++;
    }
         
/*  Run the net for one cycle and collect a host of statistics. 
 *
 */    
    public void runOneCycle()
    {                
        net.selectAction();
        winner = net.getFiredBehavior();                        
        
        CycleInformation cycle = new CycleInformation((int)BehaviorNetworkImpl.getCycle() - 1);
        
        if(winner != null)
        {
            cycle.setWinner(winner.getName());
            cycle.setFiringEnergy(winner.getAlpha());
        }           
        trace.add(cycle);        
        
        if(winner == null)
        {            
            net.reduceTheta();                                        
        }
        else
        {           
            //System.out.print(winner.getName() + "-->");
            net.restoreTheta();
         //   summary.add(cycleInfo);
        }        
        //System.out.println(net.getCycle() + "\t" + winner);                       
    }
    
    public RunSummary getRunSummary()
    {
        return summary;
    }
    
    public boolean satisfied()
    {
        return goals.isEmpty();
    }
    
    protected CycleInformation extractCycleInformation()
    {
        CycleInformation cycle = new CycleInformation((int)BehaviorNetworkImpl.getCycle() - 1);
        
        if(winner != null)
        {
            cycle.setWinner(winner.getName());
            cycle.setFiringEnergy(winner.getAlpha());
        }        
        return cycle;
    }
    
    public void reset()
    {
        winner = null;
        
        net.restoreTheta();
        
        trace = new Vector();
        summary = new RunSummary(run);  
    }
    
    public int getRunID()
    {
        return run;
    }
    
    public void finalize()
    {
        try
        {
          //  LogManager.closeConnections();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public BehaviorNetworkImpl getNet()
    {
        return net;
    }        
    
    public void report()
    {
        System.out.println("FIRING REPORT");
        {                   
            for(Enumeration enumeration = trace.elements(); enumeration.hasMoreElements();)
            {
                CycleInformation cycle = (CycleInformation)enumeration.nextElement();
                
                if(cycle.getWinner() != null)
                {
                    System.out.println(cycle.getCycle() + "\t" + cycle.getWinner() + "\t" + cycle.getFiringEnergy());
                }
            }
        }
    }        
}
