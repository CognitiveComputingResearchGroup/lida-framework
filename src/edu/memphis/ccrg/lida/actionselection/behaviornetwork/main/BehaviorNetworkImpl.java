/**
 * BehaviorNet.java
 *
 * Sidney D'Mello
 * Created on December 10, 2003, 6:25 PM
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicCandidationThresholdReducer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.CandidateThresholdReducer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicSelector;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Selector;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

/**
 * 
 * @author Ryan J McCall, Sidney D'Mello
 * 
 */
public class BehaviorNetworkImpl extends LidaModuleImpl implements
		ActionSelection, ProceduralMemoryListener, BroadcastListener {

	private static Logger logger = Logger
			.getLogger("lida.behaviornetwork.engine.Net");

	/**
	 * Starting value for candidateBehaviorThreshold
	 */
	private final double INITIAL_CANDIDATE_BEHAVIOR_THRESHOLD = 0.9;

	/**
	 * Current threshold for becoming active (akin to THETA from Maes'
	 * Implementation)
	 */
	private double candidateBehaviorThreshold = INITIAL_CANDIDATE_BEHAVIOR_THRESHOLD;

	/**
	 * Amount of excitation by conscious broadcast (akin to PHI from Maes'
	 * Implementation)
	 */
	private double broadcastExcitationFactor = 0.5;

	/**
	 * If behaviors' activation falls below this threshold after they are
	 * decayed then the behavior will be removed from the behavior network.
	 */
	private double behaviorActivationLowerBound = 0.0;

	/**
	 * Scales the strength of activation passed from behavior to successor.
	 */
	private double successorExcitationFactor = 1.0;

	/**
	 * Scales the strength of activation passed from behavior to predecessor.
	 */
	private double predecessorExcitationFactor = 1.0;

	/**
	 * Scales the strength of activation passed from behavior to conflictor.
	 */
	private double conflictorExcitationFactor = 1.0;

	/**
	 * Function by which the behavior activation threshold is reduced
	 */
	private CandidateThresholdReducer candidateThresholdReducer = new BasicCandidationThresholdReducer();

	/**
	 * Strategy to specify the way a winning behavior is chosen.
	 */
	private Selector selectorStrategy = new BasicSelector();

	/**
	 * Currently selected behavior
	 */
	private Behavior winningBehavior = null;

	/**
	 * Current conscious broadcast
	 */
	private NodeStructure currentBroadcast = null;

	/**
	 * Listeners of this action selection
	 */
	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();
	
	private BehaviorAttentionListener behaviorAttentionListener;

	/**
	 * All the behaviors currently in this behavior network.
	 */
	private ConcurrentMap<Long, Behavior> behaviors = new ConcurrentHashMap<Long, Behavior>();

	/**
	 * Map of behaviors indexed by the elements appearing in their context
	 * conditions. 
	 */
	private ConcurrentMap<Node, WeakHashSet<Behavior>> behaviorsByContextCondition = new ConcurrentHashMap<Node, WeakHashSet<Behavior>>();

	/**
	 * Map of behaviors indexed by the elements appearing in their add list.
	 */
	private ConcurrentMap<Node, WeakHashSet<Behavior>> behaviorsByAddItem = new ConcurrentHashMap<Node, WeakHashSet<Behavior>>();
	
	/**
	 * Map of behaviors indexed by the elements appearing in their delete list.
	 */
	private ConcurrentMap<Node, WeakHashSet<Behavior>> behaviorsByDeleteItem = new ConcurrentHashMap<Node, WeakHashSet<Behavior>>();

	/**
	 * Likely the action selection driver
	 */
	private TaskSpawner taskSpawner;

	public BehaviorNetworkImpl() {
		super();
	}

	// TODO make sure this is set up by initializer
	public void setTaskSpawner(TaskSpawner ts) {
		this.taskSpawner = ts;
	}

	@Override
	public void init(Map<String, ?> params) {

	}

	// *** Module communication methods
	// TODO check this everywhere.
	public void receiveBroadcast(BroadcastContent bc) {
		synchronized(this){
			currentBroadcast = (NodeStructure) bc;
		}

		LidaTask activationFromBroadcastTask = new PassActivationFromBroadcastTask(this);
		taskSpawner.addTask(activationFromBroadcastTask);
		LidaTask activationAmongBehaviorsTask = new PassActivationAmongBehaviorsTask(this);
		taskSpawner.addTask(activationAmongBehaviorsTask);
	}

	/**
	 * Theory says receivers of the broadcast should learn from it.
	 */
	public void learn() {
	}

	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}
	
	public void setBehaviorAttentionListener(BehaviorAttentionListener listener){
		this.behaviorAttentionListener = listener;
	}

	//TODO add these to lida.xml
	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ActionSelectionListener)
			addActionSelectionListener((ActionSelectionListener) listener);
		
		if(listener instanceof BehaviorAttentionListener)
			setBehaviorAttentionListener((BehaviorAttentionListener) listener);
	}

	@Override
	public void receiveBehavior(Behavior newBehavior) {
		indexBehaviorByElements(newBehavior,
				newBehavior.getContextConditions(), behaviorsByContextCondition);
		indexBehaviorByElements(newBehavior, newBehavior.getAddList(),
				behaviorsByAddItem);
		indexBehaviorByElements(newBehavior, newBehavior.getDeleteList(),
				behaviorsByDeleteItem);

		behaviors.put(newBehavior.getId(), newBehavior);
	}
	
	//TODO if behavior is going to be indexed then why does a stream even need to 
	//keep track of it's successors?  as long as the context and add lists are correct
	@Override
	public void receiveStream(Stream stream) {
		for(Behavior behavior: stream.getBehaviors())
			receiveBehavior(behavior);
	}

	/**
	 * Utility method to index the behaviors into a map by elements 
	 */
	private void indexBehaviorByElements(Behavior behavior, Collection<Node> elements,
										 Map<Node, WeakHashSet<Behavior>> map) {
		for (Node element : elements) {
			synchronized (element) {
				WeakHashSet<Behavior> values = map.get(element);
				if (values == null) {
					values = new WeakHashSet<Behavior>();
					map.put(element, values);
				}
				values.add(behavior);
			}

		}//for
	}

	/**
	 * Called by action selection triggers
	 * @Override
	 */
	public void triggerActionSelection() {
		selectAction();
	}

	/**
	 * Select one action to be executed
	 */
	public void selectAction() {
		winningBehavior = selectorStrategy.selectBehavior(getBehaviors(), candidateBehaviorThreshold);
		processWinner();
	}// method

	/**
     * Called when a new conscious broadcast arrives.  Method actually called by ActivatedBehaviorsTask on a separate thread.
     */
	public void passActivationAmongBehaviors() {
		for (Behavior behavior : getBehaviors()) {
			if (behavior.isAllContextConditionsSatisfied())
				spreadSuccessorActivation(behavior);
			else
				spreadPredecessorActivation(behavior);
			spreadConflictorActivation(behavior);
		}
	}

	/**
	 * Only excite successor if precondition is not yet satisfied.
	 * 
	 * @param behavior
	 * 
	 */
	private void spreadSuccessorActivation(Behavior behavior) {
		for (Node addProposition: behavior.getAddList()) {
			Set<Behavior> successors = getSuccessors(addProposition);
			for (Behavior successor: successors) {
				// Grant activation to a successor if its precondition has not yet been satisfied
				if (successor.isContextConditionSatisfied(addProposition) == false) {
					double amount = (behavior.getActivation() * successorExcitationFactor) /
							         (successor.getContextSize() * successors.size());
					successor.excite(amount);
					logger.log(Level.FINEST, behavior.getLabel() + "-->"
							+ amount + " to " + successor + " for "
							+ addProposition, LidaTaskManager.getActualTick());
				}
			}
		}
	}// method

	private Set<Behavior> getSuccessors(Node addProposition) {
		return behaviorsByContextCondition.get(addProposition);
	}

	/**
	 * Don't bother exciting a predecessor for a precondition that is already
	 * satisfied.
	 * 
	 * @param behavior
	 */
	private void spreadPredecessorActivation(Behavior behavior) {
		for (Node contextCondition : behavior.getContextConditions()) {
			if (behavior.isContextConditionSatisfied(contextCondition) == false) {
				Set<Behavior> predecessors = getPredecessors(contextCondition);
				for (Behavior predecessor : predecessors) {
					double granted = (behavior.getActivation() * predecessorExcitationFactor) / 
							         (predecessor.getAddListCount() * predecessors.size());
					predecessor.excite(granted);
					logger.log(Level.FINEST, behavior.getActivation() + " "
							+ behavior.getLabel() + "<--" + granted + " to "
							+ predecessor + " for " + contextCondition,
							LidaTaskManager.getActualTick());
				}
			}
		}
	}

	private Set<Behavior> getPredecessors(Node precondition) {
		return behaviorsByAddItem.get(precondition);
	}

	// TODO This method cannot be rechecked enough!
	private void spreadConflictorActivation(Behavior behavior) {
		boolean isMutualConflict = false;
		for (Node contextCondition : behavior.getContextConditions()) {
			Set<Behavior> conflictors = getConflictors(contextCondition);
			for (Behavior conflictor : conflictors) {
				// for each conflictor context condition
				for (Node conflictorPreCondition : conflictor.getContextConditions()) {
					// if conflictor context condition is not satisfied
					if (conflictor.isContextConditionSatisfied(conflictorPreCondition) == false) {
						Set<Behavior> conflictorsConflictors = getConflictors(conflictorPreCondition);
						// if there is a mutual conflict
						isMutualConflict = conflictorsConflictors.contains(behavior);
						if (isMutualConflict) {
							if (behavior.getActivation() > conflictor.getActivation())
								auxSpreadConflictorActivation(behavior, conflictor, conflictors.size());
						}
					}
				}

				// No mutual conflict then inhibit the conflictor of behavior
				if (isMutualConflict == false)
					auxSpreadConflictorActivation(behavior, conflictor, conflictors.size());
				else
					isMutualConflict = false;
			}// for each conflictor
		}// for
	}// method
	
	private Set<Behavior> getConflictors(Node condition) {
		return behaviorsByDeleteItem.get(condition);
	}
	private void auxSpreadConflictorActivation(Behavior behavior, Behavior conflictor, int numConflictors) {
		double inhibitionAmount = -1.0 * (behavior.getActivation() * conflictorExcitationFactor) /
								         (conflictor.getDeleteListCount() * numConflictors);
		conflictor.excite(inhibitionAmount);
		logger.log(Level.FINEST, behavior.getLabel() + " inhibits " +
				   conflictor.getLabel() + " amount " + inhibitionAmount,
				   LidaTaskManager.getActualTick());
	}// method

	/**
	 * For each proposition in the current broadcast 
	 * get the behaviors indexed by that proposition For
	 * each behavior, excite it
	 */
	public void passActivationFromBroadcast() {
		for (Node broadcastNode : currentBroadcast.getNodes()) {
			if (behaviorsByContextCondition.containsKey(broadcastNode)) {
				double excitationAmount = broadcastNode.getTotalActivation() * broadcastExcitationFactor;
				Set<Behavior> behaviors = behaviorsByContextCondition.get(broadcastNode);	
				for (Behavior behavior : behaviors) {
					behavior.updateContextCondition(broadcastNode);
					behavior.excite(excitationAmount / behavior.getContextSize());
					logger.log(Level.FINEST, behavior.toString() + " "
							+ excitationAmount / behavior.getContextSize() + " for "
							+ broadcastNode, LidaTaskManager.getActualTick());
				}
			}
		}// for
	}// method

	private void processWinner() {
		if (winningBehavior != null) {
			spawnExpectationCodelets(winningBehavior);
			sendAction();
			resetCandidateBehaviorThreshold();
			winningBehavior.setActivation(0.0);
		} else {
			reduceCandidateBehaviorThreshold();
		}
	}

	private void spawnExpectationCodelets(Behavior winningBehavior) {
		logger.log(Level.FINEST, "Spawning attention codelet for " + winningBehavior.getLabel(),
						LidaTaskManager.getActualTick());
		behaviorAttentionListener.receiveBehaviorAttentionContent(winningBehavior.getAddList(), winningBehavior.getDeleteList());
	}

	private void reduceCandidateBehaviorThreshold() {
		candidateBehaviorThreshold = candidateThresholdReducer
				.reduce(candidateBehaviorThreshold);
		logger.log(Level.FINEST, "Candidate behavior threshold REDUCED to "
				+ candidateBehaviorThreshold, LidaTaskManager.getActualTick());
	}

	private void resetCandidateBehaviorThreshold() {
		candidateBehaviorThreshold = INITIAL_CANDIDATE_BEHAVIOR_THRESHOLD;
		logger.log(Level.FINEST, "Candidate behavior threshold RESET to  "
				+ candidateBehaviorThreshold, LidaTaskManager.getActualTick());
	}

	private void sendAction(long actionId) {
		for (ActionSelectionListener l : listeners)
			l.receiveActionId(actionId);
	}

	private void sendAction() {
		sendAction(winningBehavior.getActionId());
	}

	/**
	 * Decay all the behaviors in all the schemes. Remove the behavior after
	 * decay if its activation is below the lower bound.
	 */
	@Override
	public void decayModule(long ticks) {
		for (Behavior behavior : getBehaviors()) {
			behavior.decay(ticks);
			if (behavior.getActivation() <= behaviorActivationLowerBound) {
				logger.log(Level.FINER, "Removing behavior: " + behavior.getLabel(),
						   		LidaTaskManager.getActualTick());
				removeBehavior(behavior);
			}
		}			
	}

	/**
	 * Removes specified behavior from the behavior net, severing all links to
	 * other behaviors and removing it from the specified stream which contained
	 * it.
	 * 
	 * @param containingStream
	 * @param behavior
	 */
	private void removeBehavior(Behavior behavior) {
		behaviors.remove(behavior.getId());
	}
	
	private Collection<Behavior> getBehaviors(){
		return behaviors.values();
	}

	public void setBehaviorActivationThreshold(double threshold) {
		this.candidateBehaviorThreshold = threshold;
	}

	public void setBroadcastExcitationAmount(double broadcastExciation) {
		this.broadcastExcitationFactor = broadcastExciation;
	}

	public double getBehaviorActivationThreshold() {
		return candidateBehaviorThreshold;
	}

	public double getBroadcastExcitationAmount() {
		return broadcastExcitationFactor;
	}

	public CandidateThresholdReducer getCandidateThresholdReducer() {
		return candidateThresholdReducer;
	}

	public void setCandidateThresholdReducer(CandidateThresholdReducer reducer) {
		this.candidateThresholdReducer = reducer;
	}

	public Selector getSelectorStrategy() {
		return selectorStrategy;
	}

	public void setSelectorStrategy(Selector selector) {
		this.selectorStrategy = selector;
	}

	@Override
	public Object getModuleContent() {
		return null;
	}

	public double getBehaviorActivationLowerBound() {
		return behaviorActivationLowerBound;
	}

	public void setBehaviorActivationLowerBound(
			double behaviorActivationLowerBound) {
		this.behaviorActivationLowerBound = behaviorActivationLowerBound;
	}

	public double getSuccessorExcitationFactor() {
		return successorExcitationFactor;
	}

	public void setSuccessorExcitationFactor(double successorExcitationFactor) {
		this.successorExcitationFactor = successorExcitationFactor;
	}

	public double getPredecessorExcitationFactor() {
		return predecessorExcitationFactor;
	}

	public void setPredecessorExcitationFactor(double predecessorExcitationFactor) {
		this.predecessorExcitationFactor = predecessorExcitationFactor;
	}

	public double getConflictorExcitationFactor() {
		return conflictorExcitationFactor;
	}

	public void setConflictorExcitationFactor(double conflictorExcitationFactor) {
		this.conflictorExcitationFactor = conflictorExcitationFactor;
	}

}// class