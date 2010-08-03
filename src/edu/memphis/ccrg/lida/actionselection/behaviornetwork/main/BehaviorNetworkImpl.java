/*
 * BehaviorNet.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:25 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Reinforcer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Selector;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class BehaviorNetworkImpl extends LidaModuleImpl implements ActionSelection, ProceduralMemoryListener, BroadcastListener{    

	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Net");
    public final double THETA_REDUCTION  = 10;   //percent to reduce the threshold 
    
    private double theta;    //threshold for execution
    private double phi;      //amount of excitation by environment
    private double gamma;    //amount of excitation by goals
    private double delta;    //amount of inhibition by protected goal
    private double pi;       //mean activation
    private double omega;    //amplification factor for base level activation    
    
    private List<Goal> goals = new ArrayList<Goal>();
    private List<Stream> streams = new ArrayList<Stream>();  
    private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
    
    /**
     * Creates links between behaviors
     */
    private Linker linker = new Linker(this);
    
    /**
     * utility to normalize
     */
    private Normalizer normalizer = new Normalizer(streams);
    private Selector selector = new Selector();
    private Reinforcer reinforcer = new Reinforcer();
    
    private long cycle = 0;
    
    private Behavior winner = null;        
    private double threshold = theta;       //used as a backup copy for the 
                                    //threshold when thresholds are lowered
    
    private NodeStructure currentState = new NodeStructureImpl();
    
    public BehaviorNetworkImpl() {
        setConstants(0, 0, 0, 0, 0, 0);     
        linker.buildLinks();
    }

    public Behavior getFiredBehavior(){
        return winner;
    }
    
    public void receiveBroadcast(BroadcastContent bc){
    	currentState = (NodeStructure) bc;
    }
	/**
	 * Theory says receivers of the broadcast should learn from it.
	 */
	public void learn(){}
    
    
    public void updateGoals(List<Goal> goals){
        this.goals = goals;
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
    public void selectAction(){   
    	
//   	   1.  Reinforcement Phase
//	       If the winner is not null:
//	           a. Reset its activation
//	           b. Reinforce the winner
        report();
        if(winner != null){
            winner.deactivate();  
            winner.resetActivation();
            reinforcer.reinforce(winner, currentState);        
        }
            
//   	 *  2.  Initialization Phase:
//   		 *          a. Increment the time stamp
//   		 *          b. Set the winner to null.
//   		 *          c. Reset the selector
        cycle ++;                                                                //phase 2 
        winner = null;
        selector.reset();
        
//   	 *  3.  Activation Spreading Phase
//	 *          a. Add activation from the environment.
//	 *          b. Add activation from the goals.
//	 *          c. Add excitation from internal spreading by the behaviors.
//	 *          d. Add inhibition.
        grantActivationFromEnvironment();                                          //phase 3
        
        for(Goal goal: goals)
        	goal.grantActivation(gamma);
        
        for(Stream s: streams){
        	for(Behavior b: s.getBehaviors()){
        		b.spreadExcitation(phi);
                b.spreadInhibition(currentState);
                //((Behavior)bi.next()).spreadExcitation();
        	}
        }
        
//   	 *  4.  Merging Phase
//	     *      a. Add reinforcement contribution to activation.
        for(Stream s: streams){//Phase 4
        	for(Behavior b: s.getBehaviors()){
        		b.merge(omega);
        	}
        } 
        
//   	 *  5.  Normalization Phase:
//   		 *          a. Scan the streams.
//   		 *          b. Normalize
        if(cycle != 1){
            normalizer.scan();                                                  //phase 5
            normalizer.normalize(this.pi); 
            normalizer.scan();
        }  
        
        for(Stream s: streams){				//phase 6
        	for(Behavior b: s.getBehaviors()){
        		if(b.isActive() && b.getAlpha() >= theta){
        			selector.addCompetitor(b);
        		}
        	}
        	winner = selector.evaluateAbsoluteWinner();   
        }
        
        for(Stream s: streams){				//phase 7
        	for(Behavior b: s.getBehaviors()){
        		if(!b.equals(winner)){
        			b.deactivate();
        		}
        	}
        }
        
        //phase 8
        //phase 9
        if(winner != null)                                                      
            winner.prepareToFire(currentState);                
       
        report();
    }   //method 
    
    private Map<Linkable, List<Behavior>> propositions = new HashMap<Linkable, List<Behavior>>();
    
    /*
	 *  Spreads activation to Behaviors in the propositions Hashtable for
	 *  true propositions as specified by the state.
	 *
	 *  The state Hashtable consists of proposition as a key and value, as
	 *  a value that may be required by the winning Behavior as an Object.
	 *  
	 *  For true preconditions with no value: "true" is the value.
	 *  False preconditions do not appear in the state Hashtable.
	 */ 
    //Iterate through the propositions in the current state.
    //For each proposition get the behaviors indexed by that proposition
    //For each behavior, excite it an amount equal to (phi)/(num behaviors indexed at current proposition * # of preconditions in behavior)
    public void grantActivationFromEnvironment(){
        logger.info("ENVIRONMENT : EXCITATION");
        
        for(Linkable proposition: currentState.getLinkables()){

            List<Behavior> behaviors = propositions.get(proposition);
            double granted = phi / behaviors.size();
            for(Behavior b: behaviors){
                 
                 b.getPreconditions().put(proposition, new Boolean(true));
                 b.excite(granted / b.getPreconditions().size());       
                 logger.info("\t-->" + b.getName() + " " + granted / b.getPreconditions().size() + " for " + proposition);
            }

        }
    }
    
    public void reduceTheta(){
    	//TODO Strategy pattern
        theta = theta - (theta * (THETA_REDUCTION / 100));
        logger.info("NET : THETA REDUCED TO " + theta);
    }
    public void restoreTheta(){
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
    
    public Goal getGoal(String name){
    	for(Goal g: goals)
        	if(g.getName().equalsIgnoreCase(name))
        		return g;
        return null;
    }
    
    public Stream getStream(String name){
        for(Stream s: streams)
        	if(s.getName().equalsIgnoreCase(name))
        		return s;
        return null;
    }
    
    //TODO combine gets and sets
    public double getTheta(){
        return theta;
    }
    public double getPhi(){
        return phi;                
    }
    public double getGamma(){
        return gamma;
    }
    public double getDelta(){
        return delta;
    }
    public double getPi(){
        return pi;
    }
    public double getOmega(){
        return omega;
    }            
    public void setConstants(double theta, double phi, double gamma, 
            double delta, double pi, double omega){
		setTheta(theta);
		setPhi(phi);
		setGamma(gamma);
		setDelta(delta);
		setPi(pi);   
		setOmega(omega);
	}    
	public void setTheta(double theta){
		logger.info("CONSTANT-CHANGE: theta:\t" + getTheta() + "--> " + theta);
		this.theta = theta;
		threshold = theta;
	}
	public void setPhi(double phi){
		logger.info("CONSTANT-CHANGE: phi:\t" + getPhi() + "--> " + phi);
		this.phi = phi;
	}
	public void setGamma(double gamma){
		logger.info("CONSTANT-CHANGE: gamma:\t" + getGamma() + "--> " + gamma);
		this.gamma = gamma;
	}
	public void setDelta(double delta){
		logger.info("CONSTANT-CHANGE: delta:\t" + getDelta() + "--> " + delta);
		this.delta = delta;
	}                            
	public void setPi(double pi){
		logger.info("CONSTANT-CHANGE: pi:\t" + getPi() + "--> " + pi);
		this.pi = pi;
	}                   
	public void setOmega(double omega){
		logger.info("CONSTANT-CHANGE: omega:\t" + getOmega() + "--> " + omega);
		this.omega = omega;
	}

    public List<Goal> getGoals(){
        return goals;
    } 
    public List<Stream> getStreams(){
        return streams;        
    }        
    public long getCycle(){
        return cycle;
    }                   
    
    public void setReinforcer(Reinforcer reinforcer){
        if(reinforcer != null)
            this.reinforcer = reinforcer;
    }
    
    private void report(){        
        for(Stream s: streams){
        	for(Behavior b: s.getBehaviors()){
        		logger.info(b.getName() + "::" + b.getAlpha());
        	}
        }
    }
    
    public void reset(){
        cycle = 0;
        winner = null;        
        threshold = theta;   
        
        for(Stream s: streams)
            s.reset();
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

	public Map<Linkable, List<Behavior>> getPropositions() {
		return this.propositions;
	} 
	
}//class
