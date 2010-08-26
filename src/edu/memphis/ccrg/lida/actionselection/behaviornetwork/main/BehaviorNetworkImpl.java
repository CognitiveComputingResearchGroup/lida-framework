/**
 * BehaviorNet.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:25 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
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
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
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
 * @author Ryan J McCall, Sidney D'Mello
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
     * mean level of activation allowed (PI)
     */
    private double meanActivation = 0.0;       
    
    /**
     * Amount of excitation by conscious broadcast (PHI)
     */
    private double broadcastExcitationAmount = 0.0;      
    
    /**
     * amplification factor for base level activation (OMEGA)
     */
    private double baseLevelActivationAmplicationFactor = 0.0;        
    
    /**
	 * If behaviors' activation falls below this threshold after they are decayed then the behavior
	 * will be removed from the behavior network.
	 */
	private double activationLowerBound = 0.0;
	
    //TODO consider this
    private double successorExcitationFactor = 0.9;

    //TODO consider this
    private double predecessorExcitationFactor = 0.9;
    
    //TODO 
    private double conflictorExcitationFactor = 0.9;   
    
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
     * Currently selected behavior
     */
    private Behavior winner = null;        
    
    /**
     * Current conscious broadcast
     */
    private NodeStructure currentState = new NodeStructureImpl();
    
    /**
     * Listeners of this action selection
     */
    private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
       
    /**
     * All the streams currently in this behavior network
     */
    private Queue<Stream> streams = new ConcurrentLinkedQueue<Stream>();  
    
    /**
     * Map of behaviors indexed by the propositions appearing in their pre conditions
     * Stores environmental links.
     * 
     * this is similar to our procedural memory
     */
    private ConcurrentMap<Node, Set<Behavior>> behaviorsByPrecondition = new ConcurrentHashMap<Node, Set<Behavior>>();
    
    private ConcurrentMap<Node, Set<Behavior>> behaviorsByDeleteItem = new ConcurrentHashMap<Node, Set<Behavior>>();

	private ConcurrentMap<Node, Set<Behavior>> behaviorsByAddItem = new ConcurrentHashMap<Node, Set<Behavior>>();
	
	private TaskSpawner taskSpawner;
    
    public BehaviorNetworkImpl() {    
    	super();
    }
    
    //TODO make sure this is set up by initializer
    public void setTaskSpawner(TaskSpawner ts){
    	this.taskSpawner = ts;
    }
    
    @Override
    public void init(Map<String, ?> params) {
    	
    }
    
    //*** Module communication methods
    
    public void receiveBroadcast(BroadcastContent bc){
    	currentState = (NodeStructure) bc;
    	
    	//TODO make sure this method or this line runs in a separate thread
    	grantActivationFromBroadcast();
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
		indexBehaviorByElements(newBehavior, newBehavior.getContextConditions(), behaviorsByPrecondition);      
		indexBehaviorByElements(newBehavior, newBehavior.getAddList(), behaviorsByAddItem);
		indexBehaviorByElements(newBehavior, newBehavior.getDeleteList(), behaviorsByDeleteItem);
        createInterBehaviorLinks(newBehavior);
        //
        Stream newStream = new Stream();
        newStream.addBehavior(newBehavior);
        streams.add(newStream);
	}
	
	public void indexBehaviorByElements(Behavior behavior, Set<Node> elements, Map<Node, Set<Behavior>> map){
		for(Node element: elements){
			Set<Behavior> values = map.get(element);
			if(values == null){
				values = new HashSet<Behavior>();
				map.put(element, values);
			}
			values.add(behavior);
		}
	}
	
	public void createInterBehaviorLinks(Behavior newBehavior){
		
		//Go through the add items and create all predecessor/successor links 
		//as required by behaviors whose preconditions overlap with these items
		for(Node addItem: newBehavior.getAddList()){
			Set<Behavior> behaviors = behaviorsByPrecondition.get(addItem);
			for(Behavior successorBehavior: behaviors){
				//Create predecessor link for other behavior
				successorBehavior.addPredecessor(addItem, newBehavior);
				//Create successor link for this behavior
				newBehavior.addSuccessor(addItem, successorBehavior);
			}
		}
		
		//Add this new behavior as a conflictor of whatever behaviors stopped by new behavior
		for(Node deleteItem: newBehavior.getDeleteList()){
			Set<Behavior> behaviorsAffectedByDelete = behaviorsByPrecondition.get(deleteItem);
			for(Behavior affectedBehavior: behaviorsAffectedByDelete){
				affectedBehavior.addConflictor(deleteItem, newBehavior);
			}
		}
		
		for(Node condition: newBehavior.getContextConditions()){
			//Find all of the new behavior's conflictors - behaviors that 
			//hurt its chances of activating
			Set<Behavior> deletors = behaviorsByDeleteItem.get(condition);
			if(deletors != null)
				newBehavior.addConflictors(condition, deletors);
			
			//Create successor links for other behaviors
			//Create predecessor links for this behavior
			Set<Behavior> addingBehaviors = behaviorsByAddItem.get(condition);
			if(addingBehaviors != null){
				for(Behavior adder: addingBehaviors){
					newBehavior.addPredecessor(condition, adder);
					adder.addSuccessor(condition, newBehavior);
				}
			}
		}//for
	}//method

	@Override
	public void triggerActionSelection() {
		selectAction();	
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
        //spread activation and inhibition among behaviors        
        passActivationAmongBehaviors();
        //Essentially readjusts the activations of all behaviors
        normalizeActivations(); 
        
        //TODO add to selector whenever over threshold?
        //TODO collapse this into the Selector!
        chooseBehaviorsForSelection();
        
        //Select winner
        winner = selectorStrategy.selectBehavior();
    	processWinner();
        
    	//Deactivate preconditions
    	deactivateAllPreconditions();
    }//method 

    /**
     * 
     */
    private void passActivationAmongBehaviors(){
    	for(Stream stream: streams){
        	for(Behavior behavior: stream.getBehaviors()){
        		 if(behavior.isAllContextConditionsSatisfied())
        	         spreadSuccessorActivation(behavior);        
        	     else
        	    	 spreadPredecessorActivation(behavior);
        		 spreadConflictorActivation(behavior);
        	}
        }
    }
    
    /**
     * Only excite successor if precondition is not yet satisfied
     * @param behavior
     */
    private void spreadSuccessorActivation(Behavior behavior){           
        for(Node addProposition: behavior.getAddList()){
            Set<Behavior> behaviors = behavior.getSuccessors(addProposition);
            for(Behavior successor: behaviors){
            	//Should only grant activation to a successor if its precondition
            	//has not yet been satisfied
                if(successor.isContextConditionSatisfied(addProposition) == false){
                	double granted = (behavior.getActivation() * successorExcitationFactor) / successor.getContextSize();
                   //oldway: double granted = ((behavior.getActivation() * broadcastExcitationAmount) / (goalExcitationAmount * behaviors.size() * successor.getPreconditionCount());
                    successor.excite(granted);
                    logger.info("\t:+" + behavior.getLabel() + "-->" + granted + " to " +
                                    successor + " for " + addProposition);
                }                
            }
        }        
    }//method
    
    /**
     * Don't bother exciting a predecessor for a precondition that 
     * is already satisfied.
     * @param behavior
     */
    public void spreadPredecessorActivation(Behavior behavior){             
        for(Node precondition: behavior.getContextConditions()){
            if(behavior.isContextConditionSatisfied(precondition) == false){
            	Set<Behavior> predecessors = behavior.getPredecessors(precondition);    
            	for(Behavior predecessor: predecessors){
            		double granted = (behavior.getActivation() * predecessorExcitationFactor) / (predecessor.getAddListCount() * predecessors.size());                        
                    predecessor.excite(granted);
                    logger.info("\t:+" + behavior.getActivation() + " " + behavior.getLabel() + "<--" + granted + " to " +
                                        predecessor + " for " + precondition);
                    
                }
            }
        }        
    } 
    
    //TODO Double check I converted this monster correctly
    public void spreadConflictorActivation(Behavior behavior){
        for(Node stateNode: currentState.getNodes()){
        	Set<Behavior> behaviors = behavior.getConflictors(stateNode); 
            for(Behavior conflictor: behaviors){
            	//between conflictor and behaivor
              	boolean mutualConflict = false;
               	double inhibitionAmount = -1.0 * (behavior.getActivation() * conflictorExcitationFactor) / (conflictor.getDeleteListCount());
                //oldway:double inhibited = (getTotalActivation(b) * fraction) / (behaviors.size() * conflictor.getDeleteList().size());

                for(Node conflictorPreCondition: conflictor.getContextConditions()){
                   	if(conflictor.isContextConditionSatisfied(conflictorPreCondition) == false){
                   		for(Node behaviorDeleteItem: behavior.getDeleteList()){
                   			if(conflictorPreCondition.equals(behaviorDeleteItem)){
                   				mutualConflict = true;
                   				if(conflictor.getActivation() < behavior.getActivation()){
                                    conflictor.excite(inhibitionAmount);
                                    logger.info(behavior.getLabel() + " inhibited " + conflictor + 
                                    		" amount " + inhibitionAmount + " for " + stateNode);                                
                                }
                   				break;
                   			}
                   		}
                   	}
                    if(mutualConflict)
                       	break;
                }//for   
                
                if(!mutualConflict){
                    conflictor.excite(inhibitionAmount);
                    logger.info(behavior.getLabel() + " inhibited " + conflictor + 
                    		" amount " + inhibitionAmount + " for " + stateNode);                                      
                }
                    
            }//for each conflictor
        }//for nodes in current state    
    }//method    
    
    private double getTotalActivation(Behavior b){
    	return b.getActivation() + 
 	   			b.getBaseLevelActivation() * baseLevelActivationAmplicationFactor;
    }

    //TODO rework? remove?
    public void normalizeActivations(){
        int behaviorCount = 0;
        int aggregateActivationofBehaviors = 0;
        for(Stream s: streams){
        	behaviorCount += s.getBehaviorCount();
        	for(Behavior b: s.getBehaviors())
        		aggregateActivationofBehaviors += b.getActivation();
        }
        
        double n_sum = meanActivation * behaviorCount;
        for(Stream s: streams){
            for(Behavior behavior: s.getBehaviors()){   
            	
                double strength = behavior.getActivation() / aggregateActivationofBehaviors;
                double n_activation = strength * n_sum;
                
                behavior.setActivation(n_activation);
                /*
                //appeared commented out in Sidney's original code 
                double change = n_activation - activation;                
                
                if(change > 0)
                    behavior.excite(change);
                else if (change < 0)
                    behavior.inhibit(change);
                 */
            }
        }
    }
    
	//TODO move this inside selector?
    public void chooseBehaviorsForSelection(){
    	for(Stream s: streams)		
        	for(Behavior b: s.getBehaviors())
        		if(b.isAllContextConditionsSatisfied() && 
        		   getTotalActivation(b) >= behaviorActivationThreshold)
        			selectorStrategy.addCompetitor(b);

    }
    
    
    /**
     * For each proposition get the behaviors indexed by that proposition
     * For each behavior, excite it an amount equal to 
     * (phi)/(num behaviors indexed at current proposition * # of preconditions in behavior)
	 */
    public void grantActivationFromBroadcast(){
        logger.info("ENVIRONMENT : EXCITATION");
        for(Node proposition: currentState.getNodes()){
        	if(behaviorsByPrecondition.containsKey(proposition)){
        		Set<Behavior> behaviors = behaviorsByPrecondition.get(proposition);
                double excitationAmount = broadcastExcitationAmount / behaviors.size();
                for(Behavior b: behaviors){
                	b.satisfyContextCondition(proposition);
                	//TODO use excite strategy
                    b.excite(excitationAmount / b.getContextSize());       
                    logger.info("\t-->" + b.toString() + " " + excitationAmount / b.getContextSize() + " for " + proposition);
                }
        	}
            
        }//for
    }//method
    
    public void processWinner(){
    	if(winner != null){                                                    
            prepareToFire(winner);
            sendAction();
            restoreTheta();
            winner.setActivation(0.0);
            reinforcementStrategy.reinforce(winner, currentState, taskSpawner);
        }else       
            reduceTheta();
    }
    private void prepareToFire(Behavior b){
        logger.info("BEHAVIOR : PREPARE TO FIRE " + b.getLabel());
        //TODO spawn expectation codelets looking for results
    }
    
    public void reduceTheta(){
    	behaviorActivationThreshold = thetaReducer.reduce(behaviorActivationThreshold);
        logger.info("NET : THETA REDUCED TO " + behaviorActivationThreshold);
    }
    public void restoreTheta(){
        behaviorActivationThreshold = initialActivationThreshold;
        logger.info("NET : THETA RESTORED TO " + behaviorActivationThreshold);
    }
       
	private void sendAction(long actionId) {
        for(ActionSelectionListener l: listeners)
        	l.receiveActionId(actionId);
    }

	private void sendAction() {
		sendAction(winner.getSchemeActionId());
	}
	
	public void deactivateAllPreconditions(){
		 for(Stream s: streams)				
	        for(Behavior b: s.getBehaviors())
	        	b.deactivateContext();   
	}
	
	/**
	 * Decay all the behaviors in all the schemes.  
	 * Remove the behavior after decay if its activation is below the lower bound.
	 */
	@Override
	public void decayModule(long ticks){
		for(Stream stream: streams){
			for(Behavior behavior: stream.getBehaviors()){
				behavior.decay(ticks);
				if(behavior.getActivation() <= activationLowerBound){
					logger.log(Level.FINER, "Removing behavior: " + behavior.getLabel(), LidaTaskManager.getActualTick());
					removeBehavior(stream, behavior);
				}
			}
		}
	}
	
	/**
	 * Removes specified behavior from the behavior net, severing all links to other
	 * behaviors and removing it from the specified stream which contained it.
	 * @param containingStream
	 * @param behavior
	 */
	private void removeBehavior(Stream containingStream, Behavior behavior){
		
		//remove behavior as a successor of other behaviors
		for(Node precondition: behavior.getContextConditions()){
			Set<Behavior> addersOfPrecondition = behaviorsByAddItem.get(precondition);
			for(Behavior adder: addersOfPrecondition)
				adder.removeSuccessor(precondition, behavior);
			
			Set<Behavior> behaviors = behaviorsByPrecondition.get(precondition);
			behaviors.remove(behavior);
		}
		
		//remove behavior as a predecessor of other behaviors
		for(Node addItem: behavior.getAddList()){
			Set<Behavior> benefactors = behaviorsByPrecondition.get(addItem);
			for(Behavior benefactor: benefactors){
				benefactor.removePredecessor(addItem, behavior);
			}
				
			Set<Behavior> behaviors = behaviorsByAddItem.get(addItem);
			behaviors.remove(behavior);
		}
		
		
		//remove behavior as a conflictor of other behaviors
		for(Node deleteItem: behavior.getDeleteList()){
			Set<Behavior> conflicteds = behaviorsByPrecondition.get(deleteItem);
			for(Behavior conflicted: conflicteds)
				conflicted.removeConflictor(deleteItem, behavior);
			
			//Remove behavior from delete item map
			Set<Behavior> behaviors = behaviorsByDeleteItem.get(deleteItem);
			behaviors.remove(behavior);
		}
		
		//remove behavior from stream
		containingStream.removeBehavior(behavior);
	}
	
    public void setReinforcementStrategy(ReinforcementStrategy reinforcer){
        this.reinforcementStrategy = reinforcer;
    }
    
	public void setBehaviorActivationThreshold(double theta){
		this.behaviorActivationThreshold = theta;
	}
	public void setBroadcastExcitationAmount(double phi){
		this.broadcastExcitationAmount = phi;
	}
                           
	public void setMeanActivation(double pi){
		this.meanActivation = pi;
	}                   
	public void setBaseLevelActivationAmplificationFactor(double omega){
		this.baseLevelActivationAmplicationFactor = omega;
	}
	
	//*** Gets ***
	public Behavior getBehavior(long id, long actionId) {
		for(Stream s: streams){
			for(Behavior b: s.getBehaviors()){
				if(b.getId() == id && b.getSchemeActionId() == actionId)
					return b;
			}
		}
		return null;		
	}
    
    public double getBehaviorActivationThreshold(){
        return behaviorActivationThreshold;
    }
    public double getBroadcastExcitationAmount(){
        return broadcastExcitationAmount;                
    }

    public double getBaseLevelActivationAmplicationFactor(){
        return baseLevelActivationAmplicationFactor;
    }            
    
    public Queue<Stream> getStreams(){
        return streams;        
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
	
	@Override
	public Object getModuleContent() {
		return null;
	}

}//class