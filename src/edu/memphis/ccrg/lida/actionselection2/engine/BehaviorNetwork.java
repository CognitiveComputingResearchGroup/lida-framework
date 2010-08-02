/*
 * BehaviorNet.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:25 PM
 */

package edu.memphis.ccrg.lida.actionselection2.engine;

import java.util.*;
import java.util.logging.Logger;

public class BehaviorNetwork
{    

	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Net");
    public final static double THETA_REDUCTION  = 10;   //percent to reduce the threshold 
    
    private static double theta;    //threshold for execution
    private static double phi;      //amount of excitation by environment
    private static double gamma;    //amount of excitation by goals
    private static double delta;    //amount of inhibition by protected goal
    private static double pi;       //mean activation
    private static double omega;    //amplification factor for base level activation    
    
    private static Environment environment;
    private static LinkedList goals;
    private static LinkedList streams;        
    
    private Linker linker;
    private Normalizer normalizer;
    private Selector selector;
    private Reinforcer reinforcer;
    private Decayer decayer;
    
    private static long cycle;
    
    private static Behavior winner;        
    private double threshold;       //used as a backup copy for the 
                                    //threshold when thresholds are lowered
    
    private LinkedList listeners;
    
    public BehaviorNetwork() 
    {
        setConstants(0, 0, 0, 0, 0, 0);        
        
        environment = new Environment();
        goals = new LinkedList();
        streams = new LinkedList();
        
        linker = new Linker(this);
        link();
        
        normalizer = new Normalizer(streams);
        selector = new Selector();
        reinforcer = new Reinforcer();
        decayer = new Decayer(streams);
                
        listeners = new LinkedList();
    }
    
    public BehaviorNetwork(Environment environment, LinkedList goals, LinkedList streams)
               throws NullPointerException
    {
        if(environment != null && goals != null && streams != null)
        {
            this.environment = environment;
            this.goals = goals;
            this.streams = streams;
            
            linker = new Linker(this);
            link();
            
            normalizer = new Normalizer(streams);
            selector = new Selector();
            reinforcer = new Reinforcer();
            decayer = new Decayer(streams);
            
            cycle = 0;
            winner = null;
            threshold = theta;                        
        }
        else
            throw new NullPointerException();
    }
    
    public void link()
    {
        linker.buildLinks();
    }
    
    public static Behavior getFiredBehavior()
    {
        return winner;
    }
       
    public void updateState(Hashtable state)
    {
        environment.updateState(state);
    }
    
    public void updateGoals(Hashtable goals)
    {
        environment.updateGoals(goals);
    }
    
    public void runOneCycle()
    {
        select();
    }
        
    public void run()
    {    
        if(winner != null)
        {
            restoreTheta();
        }
        else
        {
            reduceTheta();                                                      //reduce threshold for firing 
            select();
        }        
    }    
    
/*
 *   
 *  Selection Algorithm
 *
 *  1.  Reinfrocement Phase
 *      If the winner is not null:
 *          a. Reset its activation
 *          b. Reinforce the winner
 *
 *  2.  Initialization Phase:
 *          a. Increment the time stamp
 *          b. Set the winner to null.
 *          c. Reset the selector
 *
 *  3.  Activation Spreading Phase
 *          a. Add activation from the wnvironment.
 *          b. Add activation from the goals.
 *          c. Add excitation from internal spreading by the behaviors.
 *          d. Add inhibition.
 *
 *  4.  Merging Phase
 *          a. Add reinforcement contribution to activation.
 *
 *  5.  Normalization Phase:
 *          a. Scan the streams.
 *          b. Normalize
 *
 *  6.  Selection Phase
 *          a. Add Behaviors to the selectors if:
 *              i.  They are executable (active)
 *              ii. They meet the threshold conditions
 *          b. Select a winner
 *
 *  7.  Deactivation Phase:
 *          a. Deactivate all Behaviors except the winner.
 *
 *  8.  Decay Phase:
 *          a. Decay base level activation of all behaviors.
 *
 *  9.  Preparation Phase:
 *          a. If a winner emerged, let him prepare to fire, bind necessary 
 *             variables.
 */   
    protected void select()
    {           
        report();
        if(winner != null)                                                      //phase 1
        {
            winner.record();
            winner.deactivate();
            
            winner.resetActivation();
            //reinforcer.reinforce(winner);        
        }
                
        cycle ++;                                                                //phase 2
        
        winner = null;
        selector.reset();        
                            
        environment.grantActivation();                                          //phase 3
        
        Iterator gi = Environment.getCurrentGoals().keySet().iterator();    
        while(gi.hasNext())
        {
            String name = (String)gi.next();
            Goal goal = getGoal(name);
            if(goal != null)
                goal.grantActivation();
            else
                logger.warning("UNRECOGNIZED GOAL : " + name);
        }        
                
        
        Iterator si = (Iterator)streams.iterator();
        while(si.hasNext())
        {
            Iterator bi = (Iterator)((Stream)si.next()).getBehaviors().iterator();
            while(bi.hasNext())
            {
                Behavior b = (Behavior)bi.next();
                b.spreadExcitation();
                b.spreadInhibition();

                //((Behavior)bi.next()).spreadExcitation();

            }
        }        
                                
        si = (Iterator)streams.iterator();                                      //phase 4
        while(si.hasNext())
        {
            Iterator bi = (Iterator)((Stream)si.next()).getBehaviors().iterator();
            while(bi.hasNext())
            {
                ((Behavior)bi.next()).merge();
            }
        }                
        
        if(cycle != 1)
        {
            normalizer.scan();                                                  //phase 5
            normalizer.normalize(); 
            normalizer.scan();
        }  
        
        si = (Iterator)streams.iterator();                                      //phase 6
        while(si.hasNext())
        {
            Iterator bi = (Iterator)((Stream)si.next()).getBehaviors().iterator();
            while(bi.hasNext())
            {
                Behavior behavior = (Behavior)bi.next();                
                if(behavior.isActive())
                {
                    if(behavior.getAlpha() >= theta)
                      selector.addCompetitor(behavior);                    
                }                
            }
            winner = selector.evaluateAbsoluteWinner();            
        }  
        
        si = (Iterator)streams.iterator();                                      //phase 7
        while(si.hasNext())
        {
            Iterator bi = (Iterator)((Stream)si.next()).getBehaviors().iterator();
            while(bi.hasNext())
            {
                Behavior behavior = (Behavior)bi.next();
                if(!behavior.equals(winner))
                {
                    behavior.record();
                    behavior.deactivate();
                }
            }            
        }
        
        decayer.decay();                                                        //phase 8
        
        if(winner != null)                                                      //phase 9
            winner.prepareToFire();                
       
        report();
    }    
    
    public void reduceTheta()
    {        
        theta = theta - (theta * (THETA_REDUCTION / 100));
        logger.info("NET : THETA REDUCED TO " + theta);
    }
    
    public void restoreTheta()
    {
        theta = threshold;
        logger.info("NET : THETA RESTORED TO " + theta);
    }
    
    
    public void addNetEventListener(NetEventListener listener)
    {
	if(listener != null)
            listeners.add(listener);
        else
            throw new NullPointerException();
    }
    
    public void removeNetEventListener(NetEventListener listener)
    {
        listeners.remove(listener);
    }
    
    private void firecycleStartEvent()
    {        
        NetEvent event = new NetEvent(this);
        
        for(Iterator i = listeners.iterator(); i.hasNext();)
        {
            ((NetEventListener)i.next()).cycleStart(event);            
        }
    }
    
    public Linker getLinker()
    {
        return linker;
    }
    
    public Normalizer getNormalizer()
    {
        return normalizer;
    }
    
    public Reinforcer getReinforcer()
    {
        return reinforcer;
    }
    
    public Goal getGoal(String name) throws NullPointerException
    {
        Goal goal = null;
        
        if(name != null)
        {
            ListIterator iterator = goals.listIterator();            
            while(iterator.hasNext() && goal == null)
            {                
                if(((Goal)iterator.next()).getName().compareTo(name) == 0)
                {
                    goal = (Goal)iterator.previous();
                }
            }
        }
        else
            throw new NullPointerException();
        
        return goal;
    }
    
    public Stream getStream(String name) throws NullPointerException
    {
        Stream stream = null;
        
        if(name != null)
        {
            ListIterator iterator = streams.listIterator();            
            while(iterator.hasNext() && stream == null)
            {                
                if(((Stream)iterator.next()).getName().compareTo(name) == 0)
                {
                    stream = (Stream)iterator.previous();
                }
            }
        }
        else
            throw new NullPointerException();
        
        return stream;
    }
    
    public static double getTheta()
    {
        return theta;
    }
    
    public static double getPhi()
    {
        return phi;                
    }
    
    public static double getGamma()
    {
        return gamma;
    }
    
    public static double getDelta()
    {
        return delta;
    }
    
    public static double getPi()
    {
        return pi;
    }
    
    public static double getOmega()
    {
        return omega;
    }            
        
    public static Environment getEnvironment()
    {
        return environment;
    }
    
    public LinkedList getGoals()
    {
        return goals;
    }
    
    public LinkedList getStreams()
    {
        return streams;        
    }        
    
    public static long getCycle()
    {
        return cycle;
    }
    
    public void setConstants(double theta, double phi, double gamma, 
                             double delta, double pi, double omega)
    {
        setTheta(theta);
        setPhi(phi);
        setGamma(gamma);
        setDelta(delta);
        setPi(pi);   
        setOmega(omega);
    }    
    
    public void setTheta(double theta)
    {
        logger.info("CONSTANT-CHANGE: theta:\t" + getTheta() + "--> " + theta);
        this.theta = theta;
        threshold = theta;
    }
    
    public void setPhi(double phi)
    {
        logger.info("CONSTANT-CHANGE: phi:\t" + getPhi() + "--> " + phi);
        this.phi = phi;
    }
    
    public void setGamma(double gamma)
    {
        logger.info("CONSTANT-CHANGE: gamma:\t" + getGamma() + "--> " + gamma);
        this.gamma = gamma;
    }
    
    public void setDelta(double delta)
    {
        logger.info("CONSTANT-CHANGE: delta:\t" + getDelta() + "--> " + delta);
        this.delta = delta;
    }                            
    
    public void setPi(double pi)
    {
        logger.info("CONSTANT-CHANGE: pi:\t" + getPi() + "--> " + pi);
        this.pi = pi;
    }                   
    
    public void setOmega(double omega)
    {
        logger.info("CONSTANT-CHANGE: omega:\t" + getOmega() + "--> " + omega);
        this.omega = omega;
    }                   
    
    public void setReinforcer(Reinforcer reinforcer)
    {
        if(reinforcer != null)
            this.reinforcer = reinforcer;
        else
            throw new NullPointerException();
    }
    
    private void report()
    {
        Iterator si = streams.iterator();
        while(si.hasNext())
        {
            Iterator bi = ((Stream)si.next()).getBehaviors().iterator();
            while(bi.hasNext())
            {
                Behavior behavior = (Behavior)bi.next();
                logger.info(behavior.getName() + "::" + behavior.getAlpha());
            }
        }
    }
    
    public void reset()
    {
        cycle = 0;
        
        winner = null;        
        threshold = theta;   
        
        for(Iterator i = streams.iterator(); i.hasNext();)
            ((Stream)i.next()).reset();
    }           
}
