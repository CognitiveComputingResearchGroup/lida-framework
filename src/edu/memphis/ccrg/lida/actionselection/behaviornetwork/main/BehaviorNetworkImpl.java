/**
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
import java.util.Set;
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

	//TODO Decay for behavior network
	//TODO Removing behaviors 
	
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
    private ConcurrentMap<Node, List<Behavior>> behaviorsByPrecondition = new ConcurrentHashMap<Node, List<Behavior>>();
    
    private ConcurrentMap<Node, List<Behavior>> behaviorsByDeleteItem = new ConcurrentHashMap<Node, List<Behavior>>();

	private ConcurrentMap<Node, List<Behavior>> behaviorsByAddItem = new ConcurrentHashMap<Node, List<Behavior>>();
    
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
		indexByDeleteListItem(newBehavior);
		indexByAddListItem(newBehavior);
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
			List<Behavior> behaviors = behaviorsByPrecondition.get(precondition);
            if(behaviors == null){
                behaviors = new ArrayList<Behavior>();
                behaviorsByPrecondition.put(precondition, behaviors);
            }                  
            behaviors.add(newBehavior);
		}
	}
	
	/**
	 * Index the behaviors by their delete list items
	 */
	public void indexByDeleteListItem(Behavior newBehavior){
		for(Node deleteItem: newBehavior.getDeleteList()){      
			List<Behavior> behaviors = behaviorsByDeleteItem.get(deleteItem);
            if(behaviors == null){
                behaviors = new ArrayList<Behavior>();
                behaviorsByDeleteItem.put(deleteItem, behaviors);
            }                  
            behaviors.add(newBehavior);
		}
	}
	
	/**
	 * Index the behaviors by their delete list items
	 */
	public void indexByAddListItem(Behavior newBehavior){
		for(Node addItem: newBehavior.getAddList()){      
			List<Behavior> behaviors = behaviorsByAddItem.get(addItem);
            if(behaviors == null){
                behaviors = new ArrayList<Behavior>();
                behaviorsByAddItem.put(addItem, behaviors);
            }                  
            behaviors.add(newBehavior);
		}
	}
	
	public void createInterBehaviorLinks(Behavior newBehavior){
		
		//Go through the add items and create all predecessor/successor links 
		//as required by behaviors whose preconditions overlap with these items
		for(Node addItem: newBehavior.getAddList()){
			List<Behavior> behaviors = behaviorsByPrecondition.get(addItem);
			for(Behavior successorBehavior: behaviors){
				successorBehavior.addPredecessor(addItem, newBehavior);
				newBehavior.addSuccessor(addItem, successorBehavior);
			}
		}
		
		//Add this new behavior as a conflictor of whatever behaviors stopped by new behavior
		for(Node deleteItem: newBehavior.getDeleteList()){
			List<Behavior> behaviorsAffectedByDelete = behaviorsByPrecondition.get(deleteItem);
			for(Behavior b: behaviorsAffectedByDelete){
				b.addConflictor(deleteItem, newBehavior);
			}
		}
		
		for(Node precondition: newBehavior.getPreconditions()){
			//Find all of the new behavior's conflictors - behaviors that 
			//hurt its chances of activating
			List<Behavior> deletors = behaviorsByDeleteItem.get(precondition);
			newBehavior.addConflictors(precondition, deletors);
			
			//Successor/ predecessor again
			List<Behavior> adders = behaviorsByAddItem.get(precondition);
			for(Behavior adder: adders){
				newBehavior.addPredecessor(precondition, adder);
				adder.addSuccessor(precondition, newBehavior);
			}
		}
	}

//    public void createInterBehaviorLinks(Behavior newBehavior){
//    	for(Stream currentStream: streams){            
//            for(Behavior existingBehavior: currentStream.getBehaviors()){
//            	buildSuccessorLinks(newBehavior, existingBehavior);
//            	buildSuccessorLinks(existingBehavior, newBehavior);
//            	//TODO check if this is redundant
//            	buildPredecessorLinks(newBehavior, existingBehavior);
//            	buildPredecessorLinks(existingBehavior, newBehavior);
//            	//
//            	buildConflictorLinks(newBehavior, existingBehavior);
//            	buildConflictorLinks(existingBehavior, newBehavior);
//            }
//    	}
//    }
//    private void buildSuccessorLinks(Behavior firstBehavior, Behavior secondBehavior){                
//        for(Node addItem: firstBehavior.getAddList()){              //iterate over add propositions of first behavior
//            for(Node precondition: secondBehavior.getPreconditions()){//iterate over preconditions of second behavior
//                if(addItem.equals(precondition)){
//                    List<Behavior> behaviors = firstBehavior.getSuccessors(addItem);
//                    if(behaviors == null){
//                        behaviors = new ArrayList<Behavior>();
//                        firstBehavior.addSuccessors(addItem, behaviors);
//                    }
//                    behaviors.add(secondBehavior);
//                }
//            }
//        }       
//    }//method
//    private void buildPredecessorLinks(Behavior firstBehavior, Behavior secondBehavior){                        
//        for(Node precondition: firstBehavior.getPreconditions()){        //iterate over preconditon of first behavior
//            for(Node addItem: secondBehavior.getAddList()){               //iterate over addlist of second behavior
//            	if(precondition.equals(addItem)){
//                    List<Behavior> behaviors = firstBehavior.getPredecessors(precondition);
//                    if(behaviors == null){
//                        behaviors = new ArrayList<Behavior>();
//                        firstBehavior.addPredecessors(precondition, behaviors);
//                    }
//                    behaviors.add(secondBehavior);
//                }
//            }
//        }               
//    }//method
//    private void buildConflictorLinks(Behavior firstBehavior, Behavior secondBehavior){                        
//        for(Node precondition: firstBehavior.getPreconditions()){
//            for(Node deleteItem: secondBehavior.getDeleteList()){
//                if(precondition.equals(deleteItem)){
//                    List<Behavior> behaviors = firstBehavior.getConflictors(precondition);
//                    if(behaviors == null){
//                        behaviors = new ArrayList<Behavior>();                        
//                        firstBehavior.addConflictors(precondition, behaviors);
//                    }
//                    behaviors.add(secondBehavior);
//                    
//                }
//            }
//        }
//    }//method

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
    	
        grantActivationFromBroadcast();
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
        		 if(behavior.isAllPreconditionsSatisfied())
        	         spreadSuccessorActivation(behavior);        
        	     else
        	    	 spreadPredecessorActivation(behavior);
        		 spreadConflictorActivation(behavior);
        	}
        }
    }
    
    //TODO consider this
    private double successorExcitationFactor = 0.9;
    
    private void spreadSuccessorActivation(Behavior behavior){           
        for(Node addProposition: behavior.getAddList()){
            List<Behavior> behaviors = behavior.getSuccessors(addProposition);
            for(Behavior successor: behaviors){
            	//Should only grant activation to a successor if its precondition
            	//has not yet been satisfied
                if(successor.isPreconditionSatisfied(addProposition) == false){
                	double granted = (behavior.getActivation() * successorExcitationFactor) / successor.getPreconditionCount();
                   //oldway: double granted = ((behavior.getActivation() * broadcastExcitationAmount) / (goalExcitationAmount * behaviors.size() * successor.getPreconditionCount());
                    successor.excite(granted);
                    logger.info("\t:+" + behavior.getLabel() + "-->" + granted + " to " +
                                    successor + " for " + addProposition);
                }                
            }
        }        
    }//method
    
    public void spreadPredecessorActivation(Behavior behavior){             
        for(Node precondition: behavior.getPreconditions()){
            if(!behavior.isPreconditionSatisfied(precondition)){
            	List<Behavior> behaviors = behavior.getPredecessors(precondition);    
            	for(Behavior predecessor: behaviors){
            		double granted = (getTotalActivation(behavior) / predecessor.getAddListCount()) / behaviors.size();                        
                    predecessor.excite(granted);
                    logger.info("\t:+" + getTotalActivation(behavior) + " " + behavior.getLabel() + "<--" + granted + " to " +
                                        predecessor + " for " + precondition);
                    
                }
            }
        }        
    }
    
    //TODO consider this
    private double predecessorExcitationFactor = 0.9;
    
    //TODO Double check I converted this monster correctly
    public void spreadConflictorActivation(Behavior b){
        for(Node precondition: b.getPreconditions()){
            if(currentState.hasNode(precondition)){
                List<Behavior> behaviors = b.getConflictors(precondition); 
                for(Behavior conflictor: behaviors){
                	boolean mutualConflict = false;
                	double inhibitionAmount = (b.getActivation() * predecessorExcitationFactor) / (conflictor.getDeleteListCount());
                    //oldway:double inhibited = (getTotalActivation(b) * fraction) / (behaviors.size() * conflictor.getDeleteList().size());
                    
                    Set<Node> preconds = conflictor.getPreconditions();
                    for(Node conflictorPreCondition: preconds){
                    	if(conflictor.isPreconditionSatisfied(conflictorPreCondition) == false){
                    		for(Node deleteItem: b.getDeleteList()){
                    			if(conflictorPreCondition.equals(deleteItem)){
                    				mutualConflict = true;
                    				break;
                    			}
                    		}
                    	}
                        if(mutualConflict)
                        	break;
                    }   
                    if(mutualConflict){
                        if(getTotalActivation(conflictor) < getTotalActivation(b)){
                                conflictor.excite(inhibitionAmount*-1);
                                logger.info("\t:-" + b.getLabel() + "---" + 
                                                inhibitionAmount + " to " + conflictor 
                                                + " for " + precondition);                                
                        }
                    }else{
                            conflictor.excite(inhibitionAmount*-1);
                            logger.info("\t:-" + b.getLabel() + "---" + inhibitionAmount + 
                                            " to " + conflictor + " for " + precondition);                                
                    }
                    
                }//for behaviors
            }                
        }//for preconditions        
    }//method    
    
    private double getTotalActivation(Behavior b){
   	 return b.getActivation() + 
 	   			b.getBaseLevelActivation() * baseLevelActivationAmplicationFactor;
    }

    //TODO rework
    public void normalizeActivations(){
        int behaviorCount = 0, totalActivationSum = 0;
        for(Stream s: streams){
        	behaviorCount += s.getBehaviorCount();
        	for(Behavior b: s.getBehaviors()){
        		totalActivationSum += getTotalActivation(b);
        	}
        }
        
        double n_sum = meanActivation * behaviorCount;
        for(Stream s: streams){
            for(Behavior behavior: s.getBehaviors()){   
            	
                double activation = getTotalActivation(behavior);
                double strength = activation / totalActivationSum;
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
    
    public void chooseBehaviorsForSelection(){
    	for(Stream s: streams){				
        	for(Behavior b: s.getBehaviors()){
        		//TODO move this inside selector?
        		if(b.isAllPreconditionsSatisfied() && getTotalActivation(b) >= behaviorActivationThreshold){
        			selectorStrategy.addCompetitor(b);
        		}
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
        	if(behaviorsByPrecondition.containsKey(proposition)){
        		List<Behavior> behaviors = behaviorsByPrecondition.get(proposition);
                double excitationAmount = broadcastExcitationAmount / behaviors.size();
                for(Behavior b: behaviors){
                	b.satisfyPrecondition(proposition);
                	//TODO use excite strategy
                    b.excite(excitationAmount / b.getPreconditionCount());       
                    logger.info("\t-->" + b.toString() + " " + excitationAmount / b.getPreconditionCount() + " for " + proposition);
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
            reinforcementStrategy.reinforce(winner, currentState);
        }else       
            reduceTheta();
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
	        	b.deactivateAllPreconditions();   
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

    public double getBaseLevelActivationAmplicationFactor(){
        return baseLevelActivationAmplicationFactor;
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
	
    public void prepareToFire(Behavior b){
        logger.info("BEHAVIOR : PREPARE TO FIRE " + b.getLabel());
        
//        TODO find out what the properties are for
//        for(BehaviorCodelet codelet: behaviorCodelets){
//        	Map<String, String> properties = codelet.getProperties();
//            for(String name: properties.keySet()){
//                String value = state.getNode(name).getLabel();
//                if(value != null)
//                    codelet.addProperty(name, value );
//            }
//        }        
    }

}//class