/*
 * BehaviorNet.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:25 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Reinforcer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Selector;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.util.Normalizer;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * From "How to Do the Right Thing" by Maes
 * The global algorithm performs a loop, in which at each timestep the following takes place over all
 * of the competence modules:
 * 1. The impact of the state, goals and protected goals on the activation level of behaviors is computed
 * 2. Activation and/or inhibition is spread through successor, predecessor, and conflicter links of behaviors
 * 3. Decay ensures overall activation level remains constant
 * 4. Behavior becomes active if
 * 	i. it is executable (all preconditions are satisfied)
 *  ii. its activation is over a certain threshold
 *  iii. have more activation than other behaviors fulfulling (i) and (ii)
 * - Break ties randomly
 * - If nothing fulfills i and ii then lower threshold by 10% 
 * 
 * @author Sidney D'Mello, Ryan J McCall
 *
 */
public class BehaviorNetworkImpl extends LidaModuleImpl implements ActionSelection, ProceduralMemoryListener, BroadcastListener{    

	private static Logger logger = Logger.getLogger("lida.behaviornetwork.engine.Net");
	
	/**
	 * Percent to reduce the theta threshold by if no behavior is selected
	 */
    public final double activationThresholdReduction  = 10;  
    
    /**
     * Reset value for theta
     */
    private final double initialActivationThreshold = 0.9;
    
    /**
     * Current threshold for becoming active (THETA)
     */
    private double activationThreshold = initialActivationThreshold;
    
    /**
     * mean level of activation (PI)
     */
    private double meanActivation = 0.0;       
    
    /**
     * Amount of excitation by conscious broadcast
     */
    private double broadcastExcitationAmount = 0.0;      
    
    /**
     * Amount of excitation by a goal (GAMMA)
     */
    private double goalExcitationAmount = 0.0;    
    
    /**
     * Amount of inhibition a protected goal takes away (DELTA)
     */
    private double protectedGoalInhibition = 0.0;   
    
    /**
     * amplification factor for base level activation
     */
    private double baseLevelActivationAmplicationFactor = 0.0;        
    
    private List<Goal> goals = new ArrayList<Goal>();
    private List<Stream> streams = new ArrayList<Stream>();  
    private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
    
    /**
     * utility to normalize
     */
    private Normalizer normalizer = new Normalizer(streams);
    private Selector selector = new Selector();
    
    //TODO make strategy
    private Reinforcer reinforcer = new Reinforcer();
    
    private Behavior winner = null;        
//    private double threshold = theta;       //used as a backup copy for the 
                                    //threshold when thresholds are lowered
    
    /**
     * Current conscious broadcast
     */
    private NodeStructure currentState = new NodeStructureImpl();
    
    /**
     * Map of behaviors indexed by the propositions appearing in their pre conditions
     * Stores environmental links.
     * 
     * this is similar to our 
     */
    private Map<Node, List<Behavior>> behaviorsByPropositionMap = new HashMap<Node, List<Behavior>>();
    
    public BehaviorNetworkImpl() {        
        buildEnvironmentalLinks();        
        buildExcitatoryGoalLinks(); 
        buildInhibitoryGoalLinks();    
        buildBehaviorLinks();
    }
    
    /**
     * Builds up propositions from all the streams.
     * Propositions is a map where the key is a proposition and the value is a list
     * of behaviors who have the key as a precondition
     * 
     */
    public void buildEnvironmentalLinks(){        
        logger.info("ENVIRONMENTAL LINKS");
        
        for(Stream s: streams){
        	for(Behavior behavior: s.getBehaviors()){
                for(Node proposition: behavior.getPreconditions()){                   
                    
                    List<Behavior> behaviors = behaviorsByPropositionMap.get(proposition);
                    if(behaviors == null){
                        behaviors = new ArrayList<Behavior>();
                        behaviorsByPropositionMap.put( proposition, behaviors);
                    }                  
                    behaviors.add(behavior);                                                                        
                }
            }
        }     
    }//method
    
    public void buildExcitatoryGoalLinks(){                                
    	for(Stream s: streams){
        	for(Behavior behavior: s.getBehaviors()){                             
                for(Node proposition: behavior.getAddList()){
                    for(Goal goal: goals){                        
                    	Map<Node, List<Behavior>> propositions = goal.getExcitatoryPropositions();
                        
                        if(propositions.containsKey(proposition)){
                        	List<Behavior> behaviors = propositions.get(proposition);
                            if(behaviors == null){                                
                                behaviors = new ArrayList<Behavior>();                               
                                propositions.put( proposition, behaviors);
                            }                            
                            behaviors.add(behavior);                                                                    
                            
                        }                        
                    }
                }
            }
        }           
    }//method

    /**
     * Check every delete list item and if there's a protected goal 
     * whose excitatory proposition is in the delete list the add the behavior
     * to the protected goal's map
     */
    public void buildInhibitoryGoalLinks(){                                        
    	for(Stream s: streams){
        	for(Behavior behavior: s.getBehaviors()){                
                for(Node proposition: behavior.getDeleteList()){//iterate over the delete list
                    for(Goal goal: goals){
                        if(goal instanceof ProtectedGoal){ //only protected goals inhibit 
                        	ProtectedGoal pGoal = (ProtectedGoal) goal;
                        	Map<Node, List<Behavior>> inhibitoryProps = pGoal.getInhibitoryPropositions();                  
                            if(pGoal.containsExcitatoryProposition(proposition)){
                                List<Behavior> behaviors = inhibitoryProps.get(proposition);
                                if(behaviors == null){
                                    behaviors = new ArrayList<Behavior>();
                                    inhibitoryProps.put(proposition, behaviors);
                                }
                                behaviors.add(behavior);
                            }                            
                        }                        
                    }//for goals
                }
            }
        }   
    }//method
    
    public void buildBehaviorLinks(){
        logger.info("BEHAVIOR LINKS");
        
        for(Stream currentStream: streams){            
            for(Behavior firstBehavior: currentStream.getBehaviors()){                           
                for(Behavior secondBehavior: currentStream.getBehaviors()){         //iterate over current stream
                    if(!firstBehavior.equals(secondBehavior)){
                        buildSuccessorLinks(firstBehavior, secondBehavior);
                        buildPredecessorLinks(firstBehavior, secondBehavior);
                        buildConflictorLinks(firstBehavior, secondBehavior);
                    }
                }
                logger.info("BEHAVIOR : " + firstBehavior);
                logger.info("SUCCESSOR LINKS");
                report(null, firstBehavior.getSuccessors());
                logger.info("");
                
                logger.info("PREDECESSOR LINKS");
                report(null, firstBehavior.getPredecessors());
                logger.info("");
                
                logger.info("CONFLICTOR LINKS");
                report(null, firstBehavior.getConflictors());
                logger.info("\n");
            }
        }   
    }
    
    private void buildSuccessorLinks(Behavior firstBehavior, Behavior secondBehavior){                
        for(Node addProposition: firstBehavior.getAddList()){              //iterate over add propositions of first behavior
            for(Node pc: secondBehavior.getPreconditions()){//iterate over preconditions of second behavior
                if(addProposition.equals(pc))
                {
                    List<Behavior> behaviors = firstBehavior.getSuccessors().get(addProposition);
                    if(behaviors == null)
                    {
                        behaviors = new ArrayList<Behavior>();
                        behaviors.add(secondBehavior);
                        firstBehavior.getSuccessors().put(addProposition, behaviors);
                    }
                    else if(!behaviors.contains(secondBehavior))
                        behaviors.add(secondBehavior);
                        
                    
                }
            }
        }       
    }//method
    
    private void buildPredecessorLinks(Behavior firstBehavior, Behavior secondBehavior){                        
        for(Node precondition: firstBehavior.getPreconditions()){        //iterate over preconditon of first behavior
            for(Node p2: secondBehavior.getAddList()){               //iterate over addlist of second behavior
            	if(precondition.equals(p2)){
                    List<Behavior> behaviors = firstBehavior.getPredecessors().get(precondition);
                    if(behaviors == null){
                        behaviors = new ArrayList<Behavior>();
                        behaviors.add(secondBehavior);
                        firstBehavior.getPredecessors().put(precondition, behaviors);
                    }
                    else if(!behaviors.contains(secondBehavior))
                        behaviors.add(secondBehavior);
                }
            }
        }               
    }//method
    
    private void buildConflictorLinks(Behavior firstBehavior, Behavior secondBehavior){                        
        for(Node precondition1: firstBehavior.getPreconditions()){
            for(Node deleteItem2: secondBehavior.getDeleteList()){
                if(precondition1.equals(deleteItem2)){
                    List<Behavior> behaviors = firstBehavior.getConflictors(precondition1);
                    if(behaviors == null){
                        behaviors = new ArrayList<Behavior>();
                        behaviors.add(secondBehavior);
                        firstBehavior.addConflictors(precondition1, behaviors);
                    }
                    else if(!behaviors.contains(secondBehavior))
                        behaviors.add(secondBehavior);
                    
                }
            }
        }//for
        
    }//method
    
	@Override
	public void receiveScheme(Scheme scheme) {
		// TODO Important!
		
	}
    
    private void report(String header, Map<Node, List<Behavior>> links){
        logger.info(header);
        for(Node o: links.keySet())
            logger.info("\t" + o + " --> " + links.get(o));
            
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
    //    cycle ++;                                                                //phase 2 
        winner = null;
        selector.reset();
        
//   	 *  3.  Activation Spreading Phase
//	 *          a. Add activation from the environment.
//	 *          b. Add activation from the goals.
//	 *          c. Add excitation from internal spreading by the behaviors.
//	 *          d. Add inhibition.
        grantActivationFromBroadcast();                                          //phase 3
        
        for(Goal goal: goals)
        	goal.grantActivation(goalExcitationAmount);
        
        for(Stream s: streams){
        	for(Behavior b: s.getBehaviors()){
        		b.spreadExcitation(broadcastExcitationAmount, goalExcitationAmount);
                b.spreadInhibition(currentState, goalExcitationAmount, protectedGoalInhibition);
                //((Behavior)bi.next()).spreadExcitation();
        	}
        }
        
//   	 *  4.  Merging Phase
//	     *      a. Add reinforcement contribution to activation.
        for(Stream s: streams){//Phase 4
        	for(Behavior b: s.getBehaviors()){
        		b.merge(baseLevelActivationAmplicationFactor);
        	}
        } 
        
//   	 *  5.  Normalization Phase:
//   		 *          a. Scan the streams.
//   		 *          b. Normalize
     //   if(cycle != 1){
            normalizer.scan();                                                  //phase 5
            normalizer.normalize(this.meanActivation); 
            normalizer.scan();
     //   }  
        
        for(Stream s: streams){				//phase 6
        	for(Behavior b: s.getBehaviors()){
        		if(b.isActive() && b.getAlpha() >= activationThreshold){
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
    public void grantActivationFromBroadcast(){
        logger.info("ENVIRONMENT : EXCITATION");
        for(Linkable proposition: currentState.getLinkables()){
            List<Behavior> behaviors = behaviorsByPropositionMap.get(proposition);
            double granted = broadcastExcitationAmount / behaviors.size();
            for(Behavior b: behaviors){
            	//TODO remove comment below
                // b.getPreconditions().put(proposition, new Boolean(true));
                 b.excite(granted / b.getPreconditions().size());       
                 logger.info("\t-->" + b.getName() + " " + granted / b.getPreconditions().size() + " for " + proposition);
            }

        }
    }
    
    public void reduceTheta(){
    	//TODO Strategy pattern
        activationThreshold = activationThreshold - (activationThreshold * (activationThresholdReduction / 100));
        logger.info("NET : THETA REDUCED TO " + activationThreshold);
    }
    public void restoreTheta(){
        activationThreshold = initialActivationThreshold;
        logger.info("NET : THETA RESTORED TO " + activationThreshold);
    }
   
    public void addActionSelectionListener(ActionSelectionListener listener){
         listeners.add(listener);
    }
    
	@Override
	public void sendAction(long actionId) {
        for(ActionSelectionListener l: listeners)
        	l.receiveActionId(actionId);
                       
        if(winner == null)          
            reduceTheta();         //TODO this should be done elsewhere                               
        else
            restoreTheta();
    }

	@Override
	public void sendAction() {
		sendAction(getFiredBehavior().getActionId());
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
    
    public double getTheta(){
        return activationThreshold;
    }
    public double getPhi(){
        return broadcastExcitationAmount;                
    }
    public double getGamma(){
        return goalExcitationAmount;
    }
    public double getDelta(){
        return protectedGoalInhibition;
    }
    public double getMeanActivation(){
        return meanActivation;
    }
    public double getOmega(){
        return baseLevelActivationAmplicationFactor;
    }            

	public void setTheta(double theta){
		logger.info("CONSTANT-CHANGE: theta:\t" + getTheta() + "--> " + theta);
		this.activationThreshold = theta;
	}
	public void setPhi(double phi){
		logger.info("CONSTANT-CHANGE: phi:\t" + getPhi() + "--> " + phi);
		this.broadcastExcitationAmount = phi;
	}
	public void setGamma(double gamma){
		logger.info("CONSTANT-CHANGE: gamma:\t" + getGamma() + "--> " + gamma);
		this.goalExcitationAmount = gamma;
	}
	public void setDelta(double delta){
		logger.info("CONSTANT-CHANGE: delta:\t" + getDelta() + "--> " + delta);
		this.protectedGoalInhibition = delta;
	}                            
	public void setPi(double pi){
		logger.info("CONSTANT-CHANGE: pi:\t" + getMeanActivation() + "--> " + pi);
		this.meanActivation = pi;
	}                   
	public void setOmega(double omega){
		logger.info("CONSTANT-CHANGE: omega:\t" + getOmega() + "--> " + omega);
		this.baseLevelActivationAmplicationFactor = omega;
	}

    public List<Goal> getGoals(){
        return goals;
    } 
    public List<Stream> getStreams(){
        return streams;        
    }                       
    
    public void setReinforcer(Reinforcer reinforcer){
        this.reinforcer = reinforcer;
    }
    
    private void report(){        
        for(Stream s: streams)
        	for(Behavior b: s.getBehaviors())
        		logger.info(b.getName() + "::" + b.getAlpha());
        	
    }

	@Override
	public Object getModuleContent() {
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
	}


}//class
