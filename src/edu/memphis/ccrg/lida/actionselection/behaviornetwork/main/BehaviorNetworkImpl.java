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
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicThetaReductionStrategy;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.ReinforcementStrategy;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicSelector;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.SelectorStrategy;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.ThetaReductionStrategy;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

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
     * Reset value for behaviorActivationThreshold
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
    
    /**
	 * Percent to reduce the behavior activation threshold by if no behavior is selected
	 */
    private final double activationThresholdReduction  = 10;  
    
    /**
     * function by which the behavior activation threshold is reduced
     */
	private ThetaReductionStrategy thetaReducer = new BasicThetaReductionStrategy();
    
	/**
	 * Way that a winning behavior is chosen among those over threshold 
	 */
    private SelectorStrategy selectorStrategy = new BasicSelector();
    
    /**
     * How behavior's base-level activation are reinforced
     */
    private ReinforcementStrategy reinforcementStrategy = new BasicReinforcementStrategy();
    
    /**
     * Listeners of this action selection
     */
    private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
    
    /**
     * Goals currently in play in this behavior network
     */
    private Queue<Goal> goals = new ConcurrentLinkedQueue<Goal>();
    
    /**
     * All the streams currently in this behavior network
     */
    private Queue<Stream> streams = new ConcurrentLinkedQueue<Stream>();  
    
    
    /**
     * Currently selected behavior
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
     * this is similar to our procedural memory
     */
    private ConcurrentMap<Node, List<Behavior>> preconditionBehaviorsMap = new ConcurrentHashMap<Node, List<Behavior>>();
    
    public BehaviorNetworkImpl() {    
    	super();
    }
    
    @Override
    public void init(Map<String, ?> params) {
    	
    }
    
    //*** Module communication methods
    
    public void receiveBroadcast(BroadcastContent bc){
    	currentState = (NodeStructure) bc;
    	//TODO call grantActivationFromBroadcast() right now
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
	public void receiveBehavior(Behavior newBehavior){
		indexByPrecondition(newBehavior);        
        createExcitatoryGoalLinks(newBehavior); 
        createInhibitoryGoalLinks(newBehavior);    
        createInterBehaviorLinks(newBehavior);
        Stream newStream = new Stream();
        newStream.addBehavior(newBehavior);
        streams.add(newStream);
	}
	
	//*** Linking methods
	
	/**
	 * Index the behaviors by their precondition
	 */
	public void indexByPrecondition(Behavior newBehavior){
		for(Node precondition: newBehavior.getPreconditions()){      
			List<Behavior> behaviors = preconditionBehaviorsMap.get(precondition);
            if(behaviors == null){
                behaviors = new ArrayList<Behavior>();
                preconditionBehaviorsMap.put(precondition, behaviors);
            }                  
            behaviors.add(newBehavior);
		}
	}
    
	//Goals keep track of which behaviors will add things that they like
    public void createExcitatoryGoalLinks(Behavior newBehavior){
    	for(Node addItem: newBehavior.getAddList()){
            for(Goal goal: goals){       
                if(goal.containsExcitatoryElement(addItem)){                	
                	List<Behavior> behaviors = goal.getExcitatoryBehaviors(addItem);
                    if(behaviors == null){                                
                        behaviors = new ArrayList<Behavior>();        
                        goal.addExcitatoryBehavior(addItem, behaviors);
                    }                            
                    behaviors.add(newBehavior);                                                                    
                }                        
            }
        }
    }
    
    //The upshot is that Protected goals store the behaviors who might delete things they like
    public void createInhibitoryGoalLinks(Behavior newBehavior){
    	for(Node deleteItem: newBehavior.getDeleteList()){//iterate over the delete list
            for(Goal goal: goals){
                if(goal instanceof ProtectedGoal){ //only protected goals inhibit 
                	ProtectedGoal pGoal = (ProtectedGoal) goal;
                	Map<Node, List<Behavior>> inhibitoryProps = pGoal.getInhibitoryPropositions();                  
                    if(pGoal.containsExcitatoryProposition(deleteItem)){
                        List<Behavior> behaviors = inhibitoryProps.get(deleteItem);
                        if(behaviors == null){
                            behaviors = new ArrayList<Behavior>();
                            inhibitoryProps.put(deleteItem, behaviors);
                        }
                        behaviors.add(newBehavior);
                    }                            
                }                        
            }//for goals
        }
    }

    public void createInterBehaviorLinks(Behavior newBehavior){
    	for(Stream currentStream: streams){            
            for(Behavior existingBehavior: currentStream.getBehaviors()){
            	buildSuccessorLinks(newBehavior, existingBehavior);
            	buildSuccessorLinks(existingBehavior, newBehavior);
            	//TODO check if this is redundant
            	buildPredecessorLinks(newBehavior, existingBehavior);
            	buildPredecessorLinks(existingBehavior, newBehavior);
            	//
            	buildConflictorLinks(newBehavior, existingBehavior);
            	buildConflictorLinks(existingBehavior, newBehavior);
            }
    	}
    }
    
//    /**
//     * Old way of building links bw behaviors.  it's inefficient
//     * TODO remove
//     */
//    public void buildInterBehaviorLinks(){
//        logger.info("BEHAVIOR LINKS");
//        
//        for(Stream currentStream: streams){            
//            for(Behavior firstBehavior: currentStream.getBehaviors()){                           
//                for(Behavior secondBehavior: currentStream.getBehaviors()){         //iterate over current stream
//                    if(!firstBehavior.equals(secondBehavior)){
//                        buildSuccessorLinks(firstBehavior, secondBehavior);
//                        buildPredecessorLinks(firstBehavior, secondBehavior);
//                        buildConflictorLinks(firstBehavior, secondBehavior);
//                    }
//                }
//            }
//        }   
//    }
    
    private void buildSuccessorLinks(Behavior firstBehavior, Behavior secondBehavior){                
        for(Node addItem: firstBehavior.getAddList()){              //iterate over add propositions of first behavior
            for(Node precondition: secondBehavior.getPreconditions()){//iterate over preconditions of second behavior
                if(addItem.equals(precondition)){
                    List<Behavior> behaviors = firstBehavior.getSuccessors(addItem);
                    if(behaviors == null){
                        behaviors = new ArrayList<Behavior>();
                        firstBehavior.addSuccessors(addItem, behaviors);
                    }
                    behaviors.add(secondBehavior);
                }
            }
        }       
    }//method
    
    private void buildPredecessorLinks(Behavior firstBehavior, Behavior secondBehavior){                        
        for(Node precondition: firstBehavior.getPreconditions()){        //iterate over preconditon of first behavior
            for(Node addItem: secondBehavior.getAddList()){               //iterate over addlist of second behavior
            	if(precondition.equals(addItem)){
                    List<Behavior> behaviors = firstBehavior.getPredecessors(precondition);
                    if(behaviors == null){
                        behaviors = new ArrayList<Behavior>();
                        firstBehavior.addPredecessors(precondition, behaviors);
                    }
                    behaviors.add(secondBehavior);
                }
            }
        }               
    }//method
    
    private void buildConflictorLinks(Behavior firstBehavior, Behavior secondBehavior){                        
        for(Node precondition: firstBehavior.getPreconditions()){
            for(Node deleteItem: secondBehavior.getDeleteList()){
                if(precondition.equals(deleteItem)){
                    List<Behavior> behaviors = firstBehavior.getConflictors(precondition);
                    if(behaviors == null){
                        behaviors = new ArrayList<Behavior>();                        
                        firstBehavior.addConflictors(precondition, behaviors);
                    }
                    behaviors.add(secondBehavior);
                    
                }
            }
        }
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
        if(winner != null){
            winner.deactivatePreconditions();  
            winner.resetActivation();
            reinforcementStrategy.reinforce(winner, currentState);        
        } 
        winner = null;
        
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
                b.spreadInhibition(currentState, goalExcitationAmount, protectedGoalInhibitionAmount);
                //((Behavior)bi.next()).spreadExcitation();
        	}
        }
        
//   	 *  4.  Merging Phase
//	     *      a. Add reinforcement contribution to activation.
        //TODO this phase should happen automatically when exciting occurs
        for(Stream s: streams){//Phase 4
        	for(Behavior b: s.getBehaviors()){
        		b.merge(baseLevelActivationAmplicationFactor);
        	}
        } 
        
//   	 *  5.  Normalization Phase:
//   		 *          a. Scan the streams.
//   		 *          b. Normalize
                                                  //phase 5
        normalize(); 
        
        for(Stream s: streams){				//phase 6
        	for(Behavior b: s.getBehaviors()){
        		if(b.isActive() && b.getAlpha() >= behaviorActivationThreshold){
        			selectorStrategy.addCompetitor(b);
        		}
        	}
        	//TODO why rewrite over the winner of the last stream?
        	winner = selectorStrategy.selectBehavior();
        	sendAction();
        }
        
        for(Stream s: streams)				//phase 7
        	for(Behavior b: s.getBehaviors())
        		if(!b.equals(winner))
        			b.deactivatePreconditions();       
        
        //phase 8
        //phase 9
        if(winner != null){                                                    
            winner.prepareToFire(currentState);
            restoreTheta();
        }else       
            reduceTheta();
    }//method 
    
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
    
    /**
	 *  Spreads activation to Behaviors in the propositions Hashtable for
	 *  true propositions as specified by the state.
	 *
	 *  The state Hashtable consists of proposition as a key and value, as
	 *  a value that may be required by the winning Behavior as an Object.
	 *  
	 *  For true preconditions with no value: "true" is the value.
	 *  False preconditions do not appear in the state Hashtable.
	 *  
	 *  Iterate through the propositions in the current state.
    //For each proposition get the behaviors indexed by that proposition
    //For each behavior, excite it an amount equal to (phi)/(num behaviors indexed at current proposition * # of preconditions in behavior)
	 */
    public void grantActivationFromBroadcast(){
        logger.info("ENVIRONMENT : EXCITATION");
        for(Node proposition: currentState.getNodes()){
        	if(preconditionBehaviorsMap.containsKey(proposition)){
        		List<Behavior> behaviors = preconditionBehaviorsMap.get(proposition);
                double excitationAmount = broadcastExcitationAmount / behaviors.size();
                for(Behavior b: behaviors){
                	b.satisfyPrecondition(proposition);
                	//TODO use excitestrategy
                    b.excite(excitationAmount / b.getPreconditionCount());       
                    logger.info("\t-->" + b.toString() + " " + excitationAmount / b.getPreconditionCount() + " for " + proposition);
                }
        	}
            
        }//for
    }//method
    
    public void reduceTheta(){
    	behaviorActivationThreshold = thetaReducer.reduce(behaviorActivationThreshold, activationThresholdReduction);
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
	
	//*** set methods
	
    public void setReinforcementStrategy(ReinforcementStrategy reinforcer){
        this.reinforcementStrategy = reinforcer;
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
	public void setStreams(Queue<Stream> streams) {
		this.streams = streams;
	}
    public Queue<Stream> getStreams(){
        return streams;        
    }        
	@Override
	public Object getModuleContent() {
		return null;
	}

	public ThetaReductionStrategy getThetaReducer() {
		return thetaReducer;
	}

	public void setThetaReducer(ThetaReductionStrategy thetaReducer) {
		this.thetaReducer = thetaReducer;
	}

	public SelectorStrategy getSelectorStrategy() {
		return selectorStrategy;
	}

	public void setSelectorStrategy(SelectorStrategy selector) {
		this.selectorStrategy = selector;
	}

	public ReinforcementStrategy getReinforcementStrategy() {
		return reinforcementStrategy;
	}

	public Behavior getBehavior(long id, long actionId) {
		for(Stream s: streams){
			for(Behavior b: s.getBehaviors()){
				if(b.getId() == id && b.getSchemeActionId() == actionId)
					return b;
			}
		}
		return null;		
	}

}//class