/*
 * BehaviorNet.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:25 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicReinforcementStrategy;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.ReinforcementStrategy;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Selector;
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
    private double behaviorActivationThreshold = initialActivationThreshold;
    
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
    private double protectedGoalInhibitionAmount = 0.0;   
    
    /**
     * amplification factor for base level activation
     */
    private double baseLevelActivationAmplicationFactor = 0.0;        
    
    private Queue<Goal> goals = new ConcurrentLinkedQueue<Goal>();
    private Queue<Stream> streams = new ConcurrentLinkedQueue<Stream>();  
    private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
    
    //TODO review this
    private Selector selector = new Selector();
    
    //TODO make strategy
    private ReinforcementStrategy reinforceStrat = new BasicReinforcementStrategy();
    
    /**
     * Currently selected behavior
     * TODO make volatile?
     */
    private Behavior winner = null;        
    
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
    private ConcurrentMap<Node, List<BehaviorImpl>> behaviorsByPropositionMap = new ConcurrentHashMap<Node, List<BehaviorImpl>>();
    
    public BehaviorNetworkImpl() {    
    	super();
    }
    
    //*** Module communication methods
    
    public void receiveBroadcast(BroadcastContent bc){
    	currentState = (NodeStructure) bc;
    }
	/**
	 * Theory says receivers of the broadcast should learn from it.
	 */
	public void learn(){}
	
    public void addActionSelectionListener(ActionSelectionListener listener){
        listeners.add(listener);
   }
    
	@Override
	public void addListener(ModuleListener listener) {
	}

	@Override
	public void receiveSchemes(List<Scheme> schemes) {
		for(Scheme s: schemes)
			receiveScheme(s);		
	}
	
	@Override
	public void receiveScheme(Scheme scheme){		
		createLinksToBroadcast(scheme);        
        createExcitatoryGoalLinks(scheme); 
        createInhibitoryGoalLinks(scheme);    
        createInterBehaviorLinks(scheme);
	}

	@Override
	public void receiveStream(Stream s) {
	}

	@Override
	public void receiveStreams(List<Stream> streams) {
	}
	
	//*** Linking methods
	
	public void createLinksToBroadcast(Scheme newScheme){
		//TODO
	}
    
    /**
     * Builds up propositions from all the streams.
     * Propositions is a map where the key is a proposition and the value is a list
     * of behaviors who have the key as a precondition
     * TODO Remove after creating new method
     */
    public void buildBroadcastLinks(){        
        logger.info("ENVIRONMENTAL LINKS");
        
        for(Stream s: streams){
        	for(BehaviorImpl behavior: s.getBehaviors()){
                for(Node proposition: behavior.getPreconditions()){                   
                    
                    List<BehaviorImpl> behaviors = behaviorsByPropositionMap.get(proposition);
                    if(behaviors == null){
                        behaviors = new ArrayList<BehaviorImpl>();
                        behaviorsByPropositionMap.put( proposition, behaviors);
                    }                  
                    behaviors.add(behavior);                                                                        
                }
            }
        }     
    }//method
    
    public void createExcitatoryGoalLinks(Scheme scheme){
    	//TODO
    }
    
    public void buildExcitatoryGoalLinks(){                                
    	for(Stream s: streams){
        	for(BehaviorImpl behavior: s.getBehaviors()){                             
                for(Node proposition: behavior.getAddList()){
                    for(Goal goal: goals){                        
                    	Map<Node, List<BehaviorImpl>> propositions = goal.getExcitatoryPropositions();
                        
                        if(propositions.containsKey(proposition)){
                        	List<BehaviorImpl> behaviors = propositions.get(proposition);
                            if(behaviors == null){                                
                                behaviors = new ArrayList<BehaviorImpl>();                               
                                propositions.put( proposition, behaviors);
                            }                            
                            behaviors.add(behavior);                                                                    
                            
                        }                        
                    }
                }
            }
        }           
    }//method
    
    public void createInhibitoryGoalLinks(Scheme scheme){
    	
    }

    /**
     * Check every delete list item and if there's a protected goal 
     * whose excitatory proposition is in the delete list the add the behavior
     * to the protected goal's map
     */
    public void buildInhibitoryGoalLinks(){                                        
    	for(Stream s: streams){
        	for(BehaviorImpl behavior: s.getBehaviors()){                
                for(Node proposition: behavior.getDeleteList()){//iterate over the delete list
                    for(Goal goal: goals){
                        if(goal instanceof ProtectedGoal){ //only protected goals inhibit 
                        	ProtectedGoal pGoal = (ProtectedGoal) goal;
                        	Map<Node, List<BehaviorImpl>> inhibitoryProps = pGoal.getInhibitoryPropositions();                  
                            if(pGoal.containsExcitatoryProposition(proposition)){
                                List<BehaviorImpl> behaviors = inhibitoryProps.get(proposition);
                                if(behaviors == null){
                                    behaviors = new ArrayList<BehaviorImpl>();
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
    
    public void createInterBehaviorLinks(Scheme scheme){
    	
    }
    
    public void buildInterBehaviorLinks(){
        logger.info("BEHAVIOR LINKS");
        
        for(Stream currentStream: streams){            
            for(BehaviorImpl firstBehavior: currentStream.getBehaviors()){                           
                for(BehaviorImpl secondBehavior: currentStream.getBehaviors()){         //iterate over current stream
                    if(!firstBehavior.equals(secondBehavior)){
                        buildSuccessorLinks(firstBehavior, secondBehavior);
                        buildPredecessorLinks(firstBehavior, secondBehavior);
                        buildConflictorLinks(firstBehavior, secondBehavior);
                    }
                }
//                logger.info("BEHAVIOR : " + firstBehavior);
//                logger.info("SUCCESSOR LINKS");
//                report(null, firstBehavior.getSuccessors());
//                logger.info("");
//                
//                logger.info("PREDECESSOR LINKS");
//                report(null, firstBehavior.getPredecessors());
//                logger.info("");
//                
//                logger.info("CONFLICTOR LINKS");
//                report(null, firstBehavior.getConflictors());
//                logger.info("\n");
            }
        }   
    }
    
    private void buildSuccessorLinks(BehaviorImpl firstBehavior, BehaviorImpl secondBehavior){                
        for(Node addProposition: firstBehavior.getAddList()){              //iterate over add propositions of first behavior
            for(Node pc: secondBehavior.getPreconditions()){//iterate over preconditions of second behavior
                if(addProposition.equals(pc))
                {
                    List<BehaviorImpl> behaviors = firstBehavior.getSuccessors().get(addProposition);
                    if(behaviors == null)
                    {
                        behaviors = new ArrayList<BehaviorImpl>();
                        behaviors.add(secondBehavior);
                        firstBehavior.getSuccessors().put(addProposition, behaviors);
                    }
                    else if(!behaviors.contains(secondBehavior))
                        behaviors.add(secondBehavior);
                        
                    
                }
            }
        }       
    }//method
    
    private void buildPredecessorLinks(BehaviorImpl firstBehavior, BehaviorImpl secondBehavior){                        
        for(Node precondition: firstBehavior.getPreconditions()){        //iterate over preconditon of first behavior
            for(Node p2: secondBehavior.getAddList()){               //iterate over addlist of second behavior
            	if(precondition.equals(p2)){
                    List<BehaviorImpl> behaviors = firstBehavior.getPredecessors().get(precondition);
                    if(behaviors == null){
                        behaviors = new ArrayList<BehaviorImpl>();
                        behaviors.add(secondBehavior);
                        firstBehavior.getPredecessors().put(precondition, behaviors);
                    }
                    else if(!behaviors.contains(secondBehavior))
                        behaviors.add(secondBehavior);
                }
            }
        }               
    }//method
    
    private void buildConflictorLinks(BehaviorImpl firstBehavior, BehaviorImpl secondBehavior){                        
        for(Node precondition1: firstBehavior.getPreconditions()){
            for(Node deleteItem2: secondBehavior.getDeleteList()){
                if(precondition1.equals(deleteItem2)){
                    List<BehaviorImpl> behaviors = firstBehavior.getConflictors(precondition1);
                    if(behaviors == null){
                        behaviors = new ArrayList<BehaviorImpl>();
                        behaviors.add(secondBehavior);
                        firstBehavior.addConflictors(precondition1, behaviors);
                    }
                    else if(!behaviors.contains(secondBehavior))
                        behaviors.add(secondBehavior);
                    
                }
            }
        }//for
        
    }//method
    
    public void addGoals(Queue<Goal> goals){
        this.goals = goals;
    }
    public void addGoal(Goal g){
    	goals.add(g);
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
//        report();
        if(winner != null){
            winner.deactivate();  
            winner.resetActivation();
            reinforceStrat.reinforce(winner, currentState);        
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
        	for(BehaviorImpl b: s.getBehaviors()){
        		b.spreadExcitation(broadcastExcitationAmount, goalExcitationAmount);
                b.spreadInhibition(currentState, goalExcitationAmount, protectedGoalInhibitionAmount);
                //((Behavior)bi.next()).spreadExcitation();
        	}
        }
        
//   	 *  4.  Merging Phase
//	     *      a. Add reinforcement contribution to activation.
        for(Stream s: streams){//Phase 4
        	for(BehaviorImpl b: s.getBehaviors()){
        		b.merge(baseLevelActivationAmplicationFactor);
        	}
        } 
        
//   	 *  5.  Normalization Phase:
//   		 *          a. Scan the streams.
//   		 *          b. Normalize
                                                  //phase 5
        normalize(); 

        
        for(Stream s: streams){				//phase 6
        	for(BehaviorImpl b: s.getBehaviors()){
        		if(b.isActive() && b.getAlpha() >= behaviorActivationThreshold){
        			selector.addCompetitor(b);
        		}
        	}
        	winner = selector.evaluateAbsoluteWinner();   
        }
        
        for(Stream s: streams){				//phase 7
        	for(BehaviorImpl b: s.getBehaviors()){
        		if(!b.equals(winner)){
        			b.deactivate();
        		}
        	}
        }
        
        //phase 8
        //phase 9
        if(winner != null)  {                                                    
            winner.prepareToFire(currentState);
            restoreTheta();
        }else       
            reduceTheta();
       
//        report();
    }   //method 
    
  //TODO what is pi?
    public void normalize(){
        int behaviorCount = 0, alphaActivationSum = 0;
        for(Stream s: streams){
        	behaviorCount += s.getBehaviorCount();
        	for(Behavior b: s.getBehaviors()){
        		alphaActivationSum += b.getAlpha();
        	}
        }
        
        double n_sum = meanActivation * behaviorCount;
        
        for(Stream s: streams){
            for(Behavior behavior: s.getBehaviors()){   
            	
                double activation = behavior.getAlpha();
                double strength = activation / alphaActivationSum;
                double n_activation = strength * n_sum;
                
                behavior.decay(n_activation);
                /*
                double change = n_activation - activation;                
                
                if(change > 0)
                    behavior.excite(change);
                else if (change < 0)
                    behavior.inhibit(change);
                 */
            }
        }
    }
    
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
            List<BehaviorImpl> behaviors = behaviorsByPropositionMap.get(proposition);
            double granted = broadcastExcitationAmount / behaviors.size();
            for(BehaviorImpl b: behaviors){
            	//TODO remove comment below
                // b.getPreconditions().put(proposition, new Boolean(true));
                 b.excite(granted / b.getPreconditions().size());       
                 logger.info("\t-->" + b.toString() + " " + granted / b.getPreconditions().size() + " for " + proposition);
            }

        }
    }
    
    public void reduceTheta(){
    	//TODO Strategy pattern
        behaviorActivationThreshold = behaviorActivationThreshold - (behaviorActivationThreshold * (activationThresholdReduction / 100));
        logger.info("NET : THETA REDUCED TO " + behaviorActivationThreshold);
    }
    public void restoreTheta(){
        behaviorActivationThreshold = initialActivationThreshold;
        logger.info("NET : THETA RESTORED TO " + behaviorActivationThreshold);
    }
       
	@Override
	public void sendAction(long actionId) {
        for(ActionSelectionListener l: listeners)
        	l.receiveActionId(actionId);
    }

	@Override
	public void sendAction() {
		sendAction(winner.getSchemeActionId());
	}
	
    public void setReinforcementStrategy(ReinforcementStrategy reinforcer){
        this.reinforceStrat = reinforcer;
    }
    
	public void setBehaviorActivationThreshold(double theta){
		this.behaviorActivationThreshold = theta;
	}
	public void setBroadcastExcitationAmount(double phi){
		this.broadcastExcitationAmount = phi;
	}
	public void setGoalExcitationAmount(double gamma){
		this.goalExcitationAmount = gamma;
	}
	public void setProtectedGoalInhibition(double delta){
		this.protectedGoalInhibitionAmount = delta;
	}                            
	public void setMeanActivation(double pi){
		this.meanActivation = pi;
	}                   
	public void setBaseLevelActivationAmplificationFactor(double omega){
		this.baseLevelActivationAmplicationFactor = omega;
	}
	
	//*** Gets ***
    
    public double getBehaviorActivationThreshold(){
        return behaviorActivationThreshold;
    }
    public double getBroadcastExcitationAmount(){
        return broadcastExcitationAmount;                
    }
    public double getGoalExcitationAmount(){
        return goalExcitationAmount;
    }
    public double getProtectedGoalInhibition(){
        return protectedGoalInhibitionAmount;
    }
    public double getMeanActivation(){
        return meanActivation;
    }
    public double getBaseLevelActivationAmplicationFactor(){
        return baseLevelActivationAmplicationFactor;
    }            

    public Queue<Goal> getGoals(){
        return goals;
    } 
    public Queue<Stream> getStreams(){
        return streams;        
    }        
    
	@Override
	public Object getModuleContent() {
		return null;
	}

}//class