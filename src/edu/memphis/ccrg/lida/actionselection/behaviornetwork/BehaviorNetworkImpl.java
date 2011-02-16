/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.actionselection.behaviornetwork;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicCandidateThresholdReducer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BasicSelector;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.BehaviorExciteStrategy;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.CandidateThresholdReducer;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.ConflictorExciteStrategy;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.PredecessorExciteStrategy;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.Selector;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.strategies.SuccessorExciteStrategy;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.ConcurrentHashSet;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

//TODO how to deliberate about schemes? 
// Send behaviors under deliberation to pam via preafferance. 
// behaviors' context and result have pam groundings. 
// actions as well but have to consider who is the actor of the action

/**
 * The Class BehaviorNetworkImpl.
 * 
 * @author Ryan J McCall, Javier Snaider
 */
public class BehaviorNetworkImpl extends LidaModuleImpl implements
		ActionSelection, ProceduralMemoryListener {

	private static final Logger logger = Logger
			.getLogger(BehaviorNetworkImpl.class.getCanonicalName());

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
	private CandidateThresholdReducer candidateThresholdReducer = new BasicCandidateThresholdReducer();

	/**
	 * Strategy to specify the way a winning behavior is chosen.
	 */
	private Selector selectorStrategy = new BasicSelector();

	/**
	 * Currently selected behavior
	 */
	private Behavior winningBehavior = null;

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

	private BehaviorExciteStrategy successorExciteStrategy = new SuccessorExciteStrategy();

	private BehaviorExciteStrategy predecessorExciteStrategy = new PredecessorExciteStrategy();

	private BehaviorExciteStrategy conflictorExciteStrategy = new ConflictorExciteStrategy();

	/**
	 * Threshold to identify goals
	 */
	private static final double GOAL_THRESHOLD = 0.5;

	private enum ConditionSet {
		CONTEXT, ADDING_LIST, DELETING_LIST
	}

	/**
	 * Default constructor.
	 */
	public BehaviorNetworkImpl() {
		super();
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModuleImpl#init()
	 */
	@Override
	public void init() {
		// I think I've done this slightly incorrectly again.
		Map<String, Object> exciteParameters = new HashMap<String, Object>();
		exciteParameters.put("factor", predecessorExcitationFactor);
		predecessorExciteStrategy.init(exciteParameters);

		exciteParameters.put("factor", successorExcitationFactor);
		successorExciteStrategy.init(exciteParameters);

		exciteParameters.put("factor", conflictorExcitationFactor);
		conflictorExciteStrategy.init(exciteParameters);

		// TODO set parameters for activation passing
		LidaTask backgroundTask = new LidaTaskImpl() {
			@Override
			protected void runThisLidaTask() {
				passActivationAmongBehaviors();
			}
			@Override
			public String toString() {
				return BehaviorNetworkImpl.class.getSimpleName() + " pass activation task";
			}
		};
		taskSpawner.addTask(backgroundTask);
		
		LidaTask selectionTask = new LidaTaskImpl() {
			@Override
			protected void runThisLidaTask() {
				selectAction();
			}
			@Override
			public String toString() {
				return BehaviorNetworkImpl.class.getSimpleName()
						+ " selection task";
			}
		};
		taskSpawner.addTask(selectionTask);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.globalworkspace.BroadcastListener#receiveBroadcast(edu.memphis.ccrg.lida.globalworkspace.BroadcastContent)
	 */
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		synchronized (this) {
			ProcessBroadcastTask task = new ProcessBroadcastTask(
					((NodeStructure) bc).copy());
			taskSpawner.addTask(task);
		}
	}
	private class ProcessBroadcastTask extends LidaTaskImpl {
		private NodeStructure broadcast;

		public ProcessBroadcastTask(NodeStructure broadcast) {
			super();
			this.broadcast = broadcast;
		}

		@Override
		protected void runThisLidaTask() {
			passActivationFromBroadcast(broadcast);
			setTaskStatus(LidaTaskStatus.FINISHED);
		}
	}

	/**
	 * Theory says receivers of the broadcast should learn from it.
	 * 
	 * @param content
	 *            the content
	 */
	@Override
	public void learn(BroadcastContent content) {
		//not implemented yet.
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.actionselection.ActionSelection#addActionSelectionListener(edu.memphis.ccrg.lida.actionselection.ActionSelectionListener)
	 */
	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.actionselection.ActionSelection#addPreafferenceListener(edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.PreafferenceListener)
	 */
	@Override
	public void addPreafferenceListener(PreafferenceListener listener) {
		this.preafferenceListeners.add(listener);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#addListener(edu.memphis.ccrg.lida.framework.ModuleListener)
	 */
	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ActionSelectionListener)
			addActionSelectionListener((ActionSelectionListener) listener);
		else if (listener instanceof PreafferenceListener)
			addPreafferenceListener((PreafferenceListener) listener);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener#receiveBehavior(edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior)
	 */
	// TODO This way permits multiple instantiations of the same behavior
	// because each one will
	// have a different ID even if it was instantiated from the same Scheme.
	// This can be resolved by either slowing the scheme activation rate or by
	// having Behavior
	// have a originatingScheme field.
	public void receiveBehavior(Behavior b) {
		indexBehaviorByElements(b,
				b.getContextConditions(), behaviorsByContextCondition);
		indexBehaviorByElements(b, b.getAddingList()
				.getNodes(), behaviorsByAddingItem);
		indexBehaviorByElements(b, b.getDeletingList()
				.getNodes(), behaviorsByDeletingItem);

		behaviors.put(b.getId(), b);
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
	 * Gets satisfied behaviors.
	 * 
	 * @return the satisfied behaviors
	 */
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
	 */
	private void spreadActivationToSuccessors(Behavior behavior) {
		for (Node addProposition : behavior.getAddingList().getNodes()) {
			Set<Behavior> successors = getSuccessors(addProposition);
			if (successors != null) {
				for (Behavior successor : successors) {
					// Grant activation to a successor if its precondition has
					// not yet been satisfied
					if (successor.isContextConditionSatisfied(addProposition) == false) {
						successorExciteStrategy.exciteBehavior(behavior,
								successor);
					}
				}
			}
		}
	}

	private Set<Behavior> getSuccessors(Node addProposition) {
		return behaviorsByContextCondition.get(addProposition);
	}

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
						predecessorExciteStrategy.exciteBehavior(behavior,
								predecessor);
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
					if (behavior.getActivation() < conflictor.getActivation()) {
						// for each conflictor context condition
						for (Node conflictorPreCondition : conflictor
								.getContextConditions()) {
							Set<Behavior> conflictorsConflictors = getConflictors(conflictorPreCondition);
							// if there is a mutual conflict
							if (conflictorsConflictors != null) {
								isMutualConflict = conflictorsConflictors
										.contains(behavior);
								if (isMutualConflict)
									break;
							}
						}
					}
					// No mutual conflict then inhibit the conflictor of
					// behavior
					if (isMutualConflict == false)
						auxSpreadConflictorActivation(behavior, conflictor);
				}// for each conflictor
			}
		}// for
	}

	private Set<Behavior> getConflictors(Node condition) {
		return behaviorsByDeletingItem.get(condition);
	}

	private void auxSpreadConflictorActivation(Behavior behavior,
			Behavior conflictor) {
		conflictorExciteStrategy.exciteBehavior(behavior, conflictor);
	}

	/**
	 * For each proposition in the current broadcast get the behaviors indexed
	 * by that proposition For each behavior, excite it.
	 * 
	 * @param currentBroadcast
	 *            the current broadcast
	 */
	public void passActivationFromBroadcast(NodeStructure currentBroadcast) {
		for (Node broadcastNode : currentBroadcast.getNodes()) {
			if (broadcastNode.getDesirability() < GOAL_THRESHOLD) {
				if (behaviorsByContextCondition.containsKey(broadcastNode)) {
					passActivationToContextOrResult(broadcastNode,
							behaviorsByContextCondition, ConditionSet.CONTEXT);
				}
			} else {
				if (behaviorsByAddingItem.containsKey(broadcastNode)) {
					passActivationToContextOrResult(broadcastNode,
							behaviorsByAddingItem, ConditionSet.ADDING_LIST);
				} else if (behaviorsByDeletingItem.containsKey(broadcastNode)) {
					passActivationToContextOrResult(broadcastNode,
							behaviorsByDeletingItem, ConditionSet.DELETING_LIST);
				}
			}
		}// for
	}

	/**
	 * @param broadcastNode
	 * @param context
	 * @param map
	 */
	private void passActivationToContextOrResult(Node broadcastNode,
			ConcurrentMap<Node, Set<Behavior>> map, ConditionSet condition) {
		double excitationAmount = broadcastNode.getTotalActivation()
				* broadcastExcitationFactor;
		Set<Behavior> behaviors = map.get(broadcastNode);
		for (Behavior behavior : behaviors) {
			switch (condition) {
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
					+ broadcastNode, LidaTaskManager.getCurrentTick());
		}
	}

	/**
	 * Select one action to be executed.
	 */
	@Override
	public void selectAction() {
		winningBehavior = selectorStrategy.selectSingleBehavior(
				getSatisfiedBehaviors(), candidateBehaviorThreshold);

		if (winningBehavior != null) {
			sendPreafference(winningBehavior);
			sendAction(winningBehavior.getAction());
			resetCandidateBehaviorThreshold();
			winningBehavior.setActivation(0.0);
		} else {
			reduceCandidateBehaviorThreshold();
		}
	}

	private void sendPreafference(Behavior winningBehavior) {
		logger.log(Level.FINEST,
				"Sending preafference for " + winningBehavior.getLabel(),
				LidaTaskManager.getCurrentTick());
		for (PreafferenceListener l : preafferenceListeners)
			l.receivePreafference(winningBehavior.getAddingList(),
					winningBehavior.getDeletingList());
	}

	private void reduceCandidateBehaviorThreshold() {
		candidateBehaviorThreshold = candidateThresholdReducer
				.reduceActivationThreshold(candidateBehaviorThreshold);
		logger.log(Level.FINEST, "Candidate behavior threshold REDUCED to "
				+ candidateBehaviorThreshold, LidaTaskManager.getCurrentTick());
	}

	private void resetCandidateBehaviorThreshold() {
		candidateBehaviorThreshold = INITIAL_CANDIDATE_BEHAVIOR_THRESHOLD;
		logger.log(Level.FINEST, "Candidate behavior threshold RESET to  "
				+ candidateBehaviorThreshold, LidaTaskManager.getCurrentTick());
	}

	private void sendAction(LidaAction action) {
		for (ActionSelectionListener l : listeners)
			l.receiveAction(action);
	}

	/**
	 * Decay all the behaviors in all the schemes. Remove the behavior after
	 * decay if its activation is below the lower bound.
	 * 
	 * @param ticks
	 *            the ticks
	 */
	@Override
	public void decayModule(long ticks) {
		super.decayModule(ticks);
		for (Behavior behavior : getBehaviors()) {
			behavior.decay(ticks);
			if (behavior.getActivation() <= behaviorActivationLowerBound) {
				logger.log(Level.FINER,
						"Removing behavior: " + behavior.getLabel(),
						LidaTaskManager.getCurrentTick());
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

		for (Node addItem : behavior.getAddingList().getNodes())
			behaviorsByAddingItem.get(addItem).remove(behavior);

		for (Node deleteItem : behavior.getDeletingList().getNodes())
			behaviorsByDeletingItem.get(deleteItem).remove(behavior);

		behaviors.remove(behavior.getId());
	}

	private Collection<Behavior> getBehaviors() {
		return behaviors.values();
	}

	/**
	 * Sets behavior activation threshold.
	 * 
	 * @param threshold
	 *            the new behavior activation threshold
	 */
	public void setBehaviorActivationThreshold(double threshold) {
		this.candidateBehaviorThreshold = threshold;
	}

	/**
	 * Sets broadcast excitation amount.
	 * 
	 * @param broadcastExciation
	 *            the new broadcast excitation amount
	 */
	public void setBroadcastExcitationAmount(double broadcastExciation) {
		this.broadcastExcitationFactor = broadcastExciation;
	}

	/**
	 * Gets behavior activation threshold.
	 * 
	 * @return the behavior activation threshold
	 */
	public double getBehaviorActivationThreshold() {
		return candidateBehaviorThreshold;
	}

	/**
	 * Gets broadcast excitation amount.
	 * 
	 * @return the broadcast excitation amount
	 */
	public double getBroadcastExcitationAmount() {
		return broadcastExcitationFactor;
	}

	/**
	 * Gets candidate threshold reducer.
	 * 
	 * @return the candidate threshold reducer
	 */
	public CandidateThresholdReducer getCandidateThresholdReducer() {
		return candidateThresholdReducer;
	}

	/**
	 * Sets candidate threshold reducer.
	 * 
	 * @param reducer
	 *            the new candidate threshold reducer
	 */
	public void setCandidateThresholdReducer(CandidateThresholdReducer reducer) {
		this.candidateThresholdReducer = reducer;
	}

	/**
	 * Gets selector strategy.
	 * 
	 * @return the selector strategy
	 */
	public Selector getSelectorStrategy() {
		return selectorStrategy;
	}

	/**
	 * Sets selector strategy.
	 * 
	 * @param selector
	 *            the new selector strategy
	 */
	public void setSelectorStrategy(Selector selector) {
		this.selectorStrategy = selector;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModuleImpl#getModuleContent(java.lang.Object[])
	 */
	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	/**
	 * Gets behavior activation lower bound.
	 * 
	 * @return the behavior activation lower bound
	 */
	public double getBehaviorActivationLowerBound() {
		return behaviorActivationLowerBound;
	}

	/**
	 * Sets behavior activation lower bound.
	 * 
	 * @param behaviorActivationLowerBound
	 *            amount of activation behavior needs to remain in the network
	 *            should be the same as the activation requirement in procedural
	 *            memory
	 */
	public void setBehaviorActivationLowerBound(
			double behaviorActivationLowerBound) {
		this.behaviorActivationLowerBound = behaviorActivationLowerBound;
	}

	/**
	 * Gets successor excitation factor.
	 * 
	 * @return the successor excitation factor
	 */
	public double getSuccessorExcitationFactor() {
		return successorExcitationFactor;
	}

	/**
	 * Sets successor excitation factor.
	 * 
	 * @param successorExcitationFactor
	 *            the new successor excitation factor
	 */
	public void setSuccessorExcitationFactor(double successorExcitationFactor) {
		this.successorExcitationFactor = successorExcitationFactor;
	}

	/**
	 * Gets predecessor excitation factor.
	 * 
	 * @return the predecessor excitation factor
	 */
	public double getPredecessorExcitationFactor() {
		return predecessorExcitationFactor;
	}

	/**
	 * Sets predecessor excitation factor.
	 * 
	 * @param predecessorExcitationFactor
	 *            the new predecessor excitation factor
	 */
	public void setPredecessorExcitationFactor(
			double predecessorExcitationFactor) {
		this.predecessorExcitationFactor = predecessorExcitationFactor;
	}

	/**
	 * Gets conflictor excitation factor.
	 * 
	 * @return the conflictor excitation factor
	 */
	public double getConflictorExcitationFactor() {
		return conflictorExcitationFactor;
	}

	/**
	 * Sets conflictor excitation factor.
	 * 
	 * @param conflictorExcitationFactor
	 *            the new conflictor excitation factor
	 */
	public void setConflictorExcitationFactor(double conflictorExcitationFactor) {
		this.conflictorExcitationFactor = conflictorExcitationFactor;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.dao.Saveable#getState()
	 */
	@Override
	public Object getState() {
		Object[] state = new Object[4];
		state[0] = this.behaviors;
		state[1] = this.behaviorsByAddingItem;
		state[2] = this.behaviorsByContextCondition;
		state[3] = this.behaviorsByDeletingItem;
		return state;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.dao.Saveable#setState(java.lang.Object)
	 */
	@Override
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

}