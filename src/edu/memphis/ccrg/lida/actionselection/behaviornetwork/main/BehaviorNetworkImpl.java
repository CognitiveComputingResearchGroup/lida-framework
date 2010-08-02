/*
 * BehaviorNet.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:25 PM
 */

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class BehaviorNetworkImpl extends LidaModuleImpl implements ActionSelection, ProceduralMemoryListener{    

	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Net");
    public final static double THETA_REDUCTION  = 10;   //percent to reduce the threshold 
    
    private static double theta;    //threshold for execution
    private static double phi;      //amount of excitation by environment
    private static double gamma;    //amount of excitation by goals
    private static double delta;    //amount of inhibition by protected goal
    private static double pi;       //mean activation
    private static double omega;    //amplification factor for base level activation    
    
    private static Environment environment;
    private static List<Goal> goals = new ArrayList<Goal>();
    private static List<Stream> streams = new ArrayList<Stream>();  
    
    private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
    
    /**
     * Creates links between behaviors
     */
    private Linker linker;
    
    /**
     * utility to normalize
     */
    private Normalizer normalizer;
    private Selector selector;
    private Reinforcer reinforcer;
    
    private static long cycle;
    
    private static Behavior winner;        
    private double threshold;       //used as a backup copy for the 
                                    //threshold when thresholds are lowered
    
    public BehaviorNetworkImpl() 
    {
        setConstants(0, 0, 0, 0, 0, 0);        
        
        environment = new Environment();
        
        linker = new Linker(this);
        link();
        
        normalizer = new Normalizer(streams);
        selector = new Selector();
        reinforcer = new Reinforcer();
    }
    
    public BehaviorNetworkImpl(Environment environment, LinkedList goals, LinkedList streams)
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
    
    public Behavior getFiredBehavior()
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
        
    public void run()
    {    
        if(winner != null)
        {
            restoreTheta();
        }
        else
        {
            reduceTheta();                                                      //reduce threshold for firing 
            selectAction();
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
    public void selectAction()
    {           
        report();
        if(winner != null)                                                      //phase 1
        {
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
                    behavior.deactivate();
                }
            }            
        }
                                                         //phase 8
        
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
    
    
    public void addActionSelectionListener(ActionSelectionListener listener){
         listeners.add(listener);
    }
    
	@Override
	public void sendAction(long actionId) {
        for(ActionSelectionListener l: listeners)
        	l.receiveActionId(actionId);
                       
        if(winner == null)          
            reduceTheta();                                        
        else
            restoreTheta();
    }
    
    public Linker getLinker(){
        return linker;
    }
    public Normalizer getNormalizer(){
        return normalizer;
    }
    public Reinforcer getReinforcer(){
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
    
    public List<Goal> getGoals()
    {
        return goals;
    }
    
    public List<Stream> getStreams()
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

	@Override
	public Object getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveScheme(Scheme scheme) {
		// TODO Auto-generated method stub
		
	}


      
}
