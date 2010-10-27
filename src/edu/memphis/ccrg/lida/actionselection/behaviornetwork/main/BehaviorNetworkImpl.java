/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/

package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicCandidationThresholdReducer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.CandidateThresholdReducer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicSelector;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Selector;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.ConcurrentHashSet;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;
import edu.memphis.ccrg.lida.proceduralmemory.Stream;

//TODO how to deliberate about schemes? 
//send behaviors under deliberation to pam via preafferance. 
//behaviors' context and result have pam groundings. 
//actions as well but have to consider who is the actor of the action

/**
 * 
 * @author Ryan J McCall, Javier Snaider, Sidney D'Mello
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

	private List<PreafferenceListener> preafferenceListeners = new ArrayList<PreafferenceListener>();

	/**
	 * All the behaviors currently in this behavior network.
	 */
	private ConcurrentMap<Long, Behavior> behaviors = new ConcurrentHashMap<Long, Behavior>();

	/**
	 * Map of behaviors indexed by the elements appearing in their context
	 * conditions.
	 */
	private ConcurrentMap<Node, Set<Behavior>> behaviorsByContextCondition = new ConcurrentHashMap<Node, Set<Behavior>>();

	/**
	 * Map of behaviors indexed by the elements appearing in their add list.
	 */
	private ConcurrentMap<Node, Set<Behavior>> behaviorsByAddingItem = new ConcurrentHashMap<Node, Set<Behavior>>();

	/**
	 * Map of behaviors indexed by the elements appearing in their delete list.
	 */
	private ConcurrentMap<Node, Set<Behavior>> behaviorsByDeletingItem = new ConcurrentHashMap<Node, Set<Behavior>>();

	/**
	 * Likely the action selection driver
	 */
	private TaskSpawner taskSpawner;

	/**
	 * Threshold to identify goals
	 */
	private static final double GOAL_THRESHOLD = 0.5;
	
	private enum ConditionSet{
		CONTEXT, ADDING_LIST, DELETING_LIST
	}

	/**
	 * Default constructor
	 */
	public BehaviorNetworkImpl() {
		super();
	}

	public void setTaskSpawner(TaskSpawner ts) {
		this.taskSpawner = ts;
		// if(ts instanceof ActionSelectionDriver)
		// this.taskSpawner = (ActionSelectionDriver) ts;
		// else
		// logger.log(Level.SEVERE,
		// "Expected ActionSelectionDriver as the taskSpawner but got " +
		// ts.toString(), LidaTaskManager.getActualTick());
	}

	@Override
	public void init(Map<String, ?> params) {
		// TODO set parameters for activation passing
	}

	// TODO check this everywhere.
	public void receiveBroadcast(BroadcastContent bc) {
		synchronized (this) {
			currentBroadcast = ((NodeStructure) bc).copy();
		}
		LidaTask activationFromBroadcastTask = new PassActivationFromBroadcastTask(
				this);
		taskSpawner.addTask(activationFromBroadcastTask);
		LidaTask activationAmongBehaviorsTask = new PassActivationAmongBehaviorsTask(
				this);
		taskSpawner.addTask(activationAmongBehaviorsTask);

		runActivationTriggers();
	}

	/**
	 * Theory says receivers of the broadcast should learn from it.
	 */
	public void learn() {
	}

	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void addPreafferenceListener(PreafferenceListener listener) {
		this.preafferenceListeners.add(listener);
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ActionSelectionListener)
			addActionSelectionListener((ActionSelectionListener) listener);
		else if (listener instanceof PreafferenceListener)
			addPreafferenceListener((PreafferenceListener) listener);
	}

	@Override
	// TODO This way permits multiple instantiations of the same behavior
	// because each one will
	// have a different ID even if it was instantiated from the same Scheme.
	// This can be resolved by either slowing the scheme activation rate or by
	// having Behavior
	// have a originatingScheme field.
	public void receiveBehavior(Behavior newBehavior) {
		indexBehaviorByElements(newBehavior,
				newBehavior.getContextConditions(), behaviorsByContextCondition);
		indexBehaviorByElements(newBehavior, newBehavior.getAddingList(),
				behaviorsByAddingItem);
		indexBehaviorByElements(newBehavior, newBehavior.getDeletingList(),
				behaviorsByDeletingItem);

		behaviors.put(newBehavior.getId(), newBehavior);
		runActivationTriggers();
	}

	private void runActivationTriggers() {
		// actionSelectionDriver.newBehaviorEvent(this.behaviors.values());
	}

	// If behavior is going to be indexed then why does a stream even need to
	// keep track of it's successors? as long as the context and add lists are
	// correct
	@Override
	public void receiveStream(Stream stream) {
		for (Behavior behavior : stream.getBehaviors())
			receiveBehavior(behavior);
	}

	/**
	 * Abstractly defined utility method to index the behaviors into a map by
	 * elements
	 */
	private void indexBehaviorByElements(Behavior behavior,
			Collection<Node> elements, Map<Node, Set<Behavior>> map) {
		for (Node element : elements) {
			synchronized (element) {
				Set<Behavior> values = map.get(element);
				if (values == null) {
					values = new ConcurrentHashSet<Behavior>();
					map.put(element, values);
				}
				values.add(behavior);
			}

		}// for
	}

	/**
	 * Called by action selection triggers
	 * 
	 */
	@Override
	public void triggerActionSelection() {
		// TODO atomic boolean
		// Call resetTriggers somewhere too
		selectAction();
	}

	// public void p(String s){System.out.println(s);}

	public Set<Behavior> getSatisfiedBehaviors() {
		Set<Behavior> satisfiedBehaviors = new HashSet<Behavior>();

		for (Behavior b : getBehaviors()) {
			if (b.isAllContextConditionsSatisfied()) {
				satisfiedBehaviors.add(b);
			}
		}
		return satisfiedBehaviors;
	}

	/**
	 * Called when a new conscious broadcast arrives. Method actually called by
	 * ActivatedBehaviorsTask on a separate thread.
	 */
	public void passActivationAmongBehaviors() {
		// TODO consider alternative ways to iterate over the behaviors so that
		// the order changes from iteration to iteration
		for (Behavior behavior : getBehaviors()) {
			if (behavior.isAllContextConditionsSatisfied())
				spreadActivationToSuccessors(behavior);
			else
				spreadActivationToPredecessors(behavior);
			spreadActivationToConflictors(behavior);
		}
	}

	/**
	 * Only excite successor if precondition is not yet satisfied.
	 * 
	 * @param behavior
	 * 
	 */
	private void spreadActivationToSuccessors(Behavior behavior) {
		for (Node addProposition : behavior.getAddingList()) {
			Set<Behavior> successors = getSuccessors(addProposition);
			if (successors != null) {
				for (Behavior successor : successors) {
					// Grant activation to a successor if its precondition has
					// not yet been satisfied
					if (successor.isContextConditionSatisfied(addProposition) == false) {
						double amount = behavior.getActivation() * successorExcitationFactor
								/ successor.getUnsatisfiedContextCount();
						successor.excite(amount);
						logger.log(Level.FINEST, behavior.getLabel() + "-->"
								+ amount + " to " + successor + " for "
								+ addProposition, LidaTaskManager
								.getActualTick());
					}
				}
			}
		}
	}// method

	private Set<Behavior> getSuccessors(Node addProposition) {
		return behaviorsByContextCondition.get(addProposition);
	}

	private ExciteStrategy predecessorExcite = new PredecessorExciteStrategy();

	/**
	 * Don't bother exciting a predecessor for a precondition that is already
	 * satisfied.
	 * 
	 * @param behavior
	 */
	private void spreadActivationToPredecessors(Behavior behavior) {
		for (Node contextCondition : behavior.getContextConditions()) {
			if (behavior.isContextConditionSatisfied(contextCondition) == false) {
				Set<Behavior> predecessors = getPredecessors(contextCondition);
				if (predecessors != null) {
					for (Behavior predecessor : predecessors) {
						double granted = (behavior.getActivation() * predecessorExcitationFactor)
								/ behavior.getUnsatisfiedContextCount();
						predecessor.excite(granted);
						logger.log(Level.FINEST, behavior.getActivation() + " "
								+ behavior.getLabel() + "<--" + granted
								+ " to " + predecessor + " for "
								+ contextCondition, LidaTaskManager
								.getActualTick());
					}
				}
			}
		}
	}

	private Set<Behavior> getPredecessors(Node precondition) {
		return behaviorsByAddingItem.get(precondition);
	}

	// TODO This method cannot be rechecked enough!
	private void spreadActivationToConflictors(Behavior behavior) {
		boolean isMutualConflict = false;
		for (Node contextCondition : behavior.getContextConditions()) {
			Set<Behavior> conflictors = getConflictors(contextCondition);
			if (conflictors != null) {
				for (Behavior conflictor : conflictors) {
					isMutualConflict = false;
					if(behavior.getActivation() < conflictor.getActivation()){
						// for each conflictor context condition
						for (Node conflictorPreCondition : conflictor.getContextConditions()) {
							Set<Behavior> conflictorsConflictors = getConflictors(conflictorPreCondition);
							// if there is a mutual conflict
							if (conflictorsConflictors != null) {
								isMutualConflict = conflictorsConflictors.contains(behavior);
								if(isMutualConflict)
									break;
							}
						}					
					}
					// No mutual conflict then inhibit the conflictor of behavior
					if (isMutualConflict == false)
						auxSpreadConflictorActivation(behavior, conflictor);
				}// for each conflictor
			}
		}// for
	}// method

	private Set<Behavior> getConflictors(Node condition) {
		return behaviorsByDeletingItem.get(condition);
	}

	private void auxSpreadConflictorActivation(Behavior behavior,
			Behavior conflictor) {
		double inhibitionAmount = -(behavior.getActivation() * conflictorExcitationFactor)
				/ behavior.getContextSize();
		conflictor.excite(inhibitionAmount);
		logger.log(Level.FINEST, behavior.getLabel() + " inhibits "
				+ conflictor.getLabel() + " amount " + inhibitionAmount,
				LidaTaskManager.getActualTick());
	}// method


	/**
	 * For each proposition in the current broadcast get the behaviors indexed
	 * by that proposition For each behavior, excite it
	 */
	public void passActivationFromBroadcast() {
		for (Node broadcastNode : currentBroadcast.getNodes()) {
			if (broadcastNode.getGoalDegree() < GOAL_THRESHOLD) {
				if (behaviorsByContextCondition.containsKey(broadcastNode)) {
					passActivationToContextOrResult(broadcastNode, behaviorsByContextCondition, ConditionSet.CONTEXT);
				}
			} else{
				if(behaviorsByAddingItem.containsKey(broadcastNode)){
					passActivationToContextOrResult(broadcastNode, behaviorsByAddingItem, ConditionSet.ADDING_LIST);
				}else if(behaviorsByDeletingItem.containsKey(broadcastNode)){
					passActivationToContextOrResult(broadcastNode, behaviorsByDeletingItem, ConditionSet.DELETING_LIST);
				}
			}
		}// for
	}// method


	/**
	 * @param broadcastNode
	 * @param context 
	 * @param map 
	 */
	private void passActivationToContextOrResult(Node broadcastNode, ConcurrentMap<Node, Set<Behavior>> map, ConditionSet condition) {
		double excitationAmount = broadcastNode.getTotalActivation()
				* broadcastExcitationFactor;
		Set<Behavior> behaviors = map.get(broadcastNode);
		for (Behavior behavior : behaviors) {
			switch(condition){
				case CONTEXT:
					behavior.updateContextCondition(broadcastNode);
					behavior.excite(excitationAmount / behavior.getContextSize());
					break;
				case ADDING_LIST:
					behavior.updateAddingCondition(broadcastNode);
					behavior.excite(excitationAmount / behavior.getResultSize());
					break;
				case DELETING_LIST:
					behavior.updateDeletingCondition(broadcastNode);
					behavior.excite(excitationAmount / behavior.getResultSize());
					break;
			}
			
			logger.log(Level.FINEST, behavior.toString() + " "
					+ excitationAmount / behavior.getContextSize() + " for "
					+ broadcastNode, LidaTaskManager.getActualTick());
		}
	}

	/**
	 * Select one action to be executed
	 */
	public void selectAction() {
		winningBehavior = selectorStrategy.selectSingleBehavior(
				getSatisfiedBehaviors(), candidateBehaviorThreshold);
		processWinner();
	}// method

	private void processWinner() {
		if (winningBehavior != null) {
			sendPreafference(winningBehavior);
			sendAction();
			resetCandidateBehaviorThreshold();
			winningBehavior.setActivation(0.0);
		} else {
			reduceCandidateBehaviorThreshold();
		}
	}

	private void sendPreafference(Behavior winningBehavior) {
		logger.log(Level.FINEST, "Sending preafference for "
				+ winningBehavior.getLabel(), LidaTaskManager.getActualTick());
		for (PreafferenceListener l : preafferenceListeners)
			l.receivePreafference(winningBehavior.getAddingList(),
					winningBehavior.getDeletingList());
	}

	private void reduceCandidateBehaviorThreshold() {
		candidateBehaviorThreshold = candidateThresholdReducer
				.reduceActivationThreshold(candidateBehaviorThreshold);
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
				logger.log(Level.FINER, "Removing behavior: "
						+ behavior.getLabel(), LidaTaskManager.getActualTick());
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
		for (Node precondition : behavior.getContextConditions())
			behaviorsByContextCondition.get(precondition).remove(behavior);

		for (Node addItem : behavior.getAddingList())
			behaviorsByAddingItem.get(addItem).remove(behavior);

		for (Node deleteItem : behavior.getDeletingList())
			behaviorsByDeletingItem.get(deleteItem).remove(behavior);

		behaviors.remove(behavior.getId());
	}

	private Collection<Behavior> getBehaviors() {
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
	public Object getModuleContent(Object... params) {
		return null;
	}

	/**
	 * @return
	 */
	public double getBehaviorActivationLowerBound() {
		return behaviorActivationLowerBound;
	}

	/**
	 * @param behaviorActivationLowerBound
	 */
	public void setBehaviorActivationLowerBound(
			double behaviorActivationLowerBound) {
		this.behaviorActivationLowerBound = behaviorActivationLowerBound;
	}

	/**
	 * @return
	 */
	public double getSuccessorExcitationFactor() {
		return successorExcitationFactor;
	}

	public void setSuccessorExcitationFactor(double successorExcitationFactor) {
		this.successorExcitationFactor = successorExcitationFactor;
	}

	public double getPredecessorExcitationFactor() {
		return predecessorExcitationFactor;
	}

	public void setPredecessorExcitationFactor(
			double predecessorExcitationFactor) {
		this.predecessorExcitationFactor = predecessorExcitationFactor;
	}

	public double getConflictorExcitationFactor() {
		return conflictorExcitationFactor;
	}

	public void setConflictorExcitationFactor(double conflictorExcitationFactor) {
		this.conflictorExcitationFactor = conflictorExcitationFactor;
	}

	public Object getState() {
		Object[] state = new Object[4];
		state[0] = this.behaviors;
		state[1] = this.behaviorsByAddingItem;
		state[2] = this.behaviorsByContextCondition;
		state[3] = this.behaviorsByDeletingItem;
		return state;
	}

	@SuppressWarnings("unchecked")
	public boolean setState(Object content) {
		if (content instanceof Object[]) {
			Object[] state = (Object[]) content;
			if (state.length == 4) {
				try {
					this.behaviors = (ConcurrentMap<Long, Behavior>) state[0];
					this.behaviorsByAddingItem = (ConcurrentMap<Node, Set<Behavior>>) state[1];
					this.behaviorsByContextCondition = (ConcurrentMap<Node, Set<Behavior>>) state[2];
					this.behaviorsByDeletingItem = (ConcurrentMap<Node, Set<Behavior>>) state[3];
					return true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
	}

}// class