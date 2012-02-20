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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.PreafferenceListener;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

/**
 * A Behavior Network implementation of {@link ActionSelection} based on the
 * ideas of Maes' original. This implementation is integrated into the
 * framework, operating asynchronously and also taking the conscious broadcast
 * as input. Maes' called the input "environment".
 * 
 * @author Ryan J. McCall
 * @author Javier Snaider
 */
public class BehaviorNetwork extends FrameworkModuleImpl implements
		ActionSelection, ProceduralMemoryListener {

	private static final Logger logger = Logger.getLogger(BehaviorNetwork.class.getCanonicalName());
	private static final double DEFAULT_INITIAL_CANDIDATE_THRESHOLD = 0.9;
	private static final double DEFAULT_BROADCAST_EXCITATION_FACTOR = 0.05;
	private static final double DEFAULT_SUCCESSOR_EXCITATION_FACTOR = 0.04;
	private static final double DEFAULT_PREDECESSOR_EXCITATION_FACTOR = 0.1;
	private static final double DEFAULT_CONFLICTOR_EXCITATION_FACTOR = 0.04;
	private static final double DEFAULT_CONTEXT_SATISFACTION_THRESHOLD = 0.0;
	private static final String DEFAULT_CANDIDATE_THRESHOLD_DECAY = "defaultDecay";

	/*
	 * Current threshold for a behavior to be a candidate for selection (or to
	 * be 'active') (akin to THETA from Maes' Implementation)
	 */
	private double candidateThreshold;

	/*
	 * The initial value for candidate threshold. candidate threshold is reset
	 * to this initial value after every action selection.
	 */
	private double initialCandidateThreshold = DEFAULT_INITIAL_CANDIDATE_THRESHOLD;

	/*
	 * Amount of excitation from broadcast (environment) (akin to PHI from Maes'
	 * Implementation)
	 */
	private double broadcastExcitationFactor = DEFAULT_BROADCAST_EXCITATION_FACTOR;

	/*
	 * Scales the strength of activation passed from behavior to successor.
	 */
	private double successorExcitationFactor = DEFAULT_SUCCESSOR_EXCITATION_FACTOR;

	/*
	 * Scales the strength of activation passed from behavior to predecessor.
	 */
	private double predecessorExcitationFactor = DEFAULT_PREDECESSOR_EXCITATION_FACTOR;

	/*
	 * Scales the strength of activation passed from behavior to conflictor.
	 */
	private double conflictorExcitationFactor = DEFAULT_CONFLICTOR_EXCITATION_FACTOR;
	
	/* 
	 * Amount of activation a context condition must have to be satisfied
	 */
	private double contextSatisfactionThreshold = DEFAULT_CONTEXT_SATISFACTION_THRESHOLD;

	/*
	 * Function by which the behavior activation threshold is reduced
	 */
	private DecayStrategy thresholdReductionStrategy;

	/*
	 * Listeners of this action selection
	 */
	private List<ActionSelectionListener> actionSelectionListeners = new ArrayList<ActionSelectionListener>();

	/*
	 * Preafference listeners of this action selection
	 */
	private List<PreafferenceListener> preafferenceListeners = new ArrayList<PreafferenceListener>();

	/*
	 * All the behaviors currently in this behavior network.
	 */
	private ConcurrentMap<Long, Behavior> behaviors = new ConcurrentHashMap<Long, Behavior>();

	/*
	 * Map of behaviors indexed by the elements appearing in their context
	 * conditions.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByContextCondition = new ConcurrentHashMap<Condition, Set<Behavior>>();

	/*
	 * Map of behaviors indexed by the elements appearing in their add list.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByAddingItem = new ConcurrentHashMap<Condition, Set<Behavior>>();

	/*
	 * Map of behaviors indexed by the elements appearing in their delete list.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByDeletingItem = new ConcurrentHashMap<Condition, Set<Behavior>>();

	// /*
	// * Map of behaviors indexed by the elements appearing in their negated
	// * context conditions.
	// */
	// private ConcurrentMap<Condition, Set<Behavior>>
	// behaviorsByNegContextCondition = new ConcurrentHashMap<Condition,
	// Set<Behavior>>();

	/**
	 * Default constructor
	 */
	public BehaviorNetwork() {
		super();
	}

	/**
	 * This module can initialize the following parameters:<br><br/>
	 * 
	 * <b>actionselection.broadcastExcitationFactor</b> - double, the percentage of the activation that broadcast elements send to behaviors whose context and/or result intersect with said elements<br/>
	 * <b>actionselection.successorExcitationFactor</b> - double, the percentage of the activation that behaviors receive from their successors<br/>
	 * <b>actionselection.predecessorExcitationFactor</b> - double, the percentage of activation that behaviors receive from their predecessors<br/>
	 * <b>actionselection.conflictorExcitationFactor</b> - double, the percent of activation behaviors receive from conflicting behaviors<br/>
	 * <b>actionselection.contextSatisfactionThreshold</b> - double, amount of activation a context condition must have to be satisfied<br/>
	 * <b>actionselection.initialCandidateThreshold</b> - double, the initial value for candidate threshold. candidate threshold is reset to this initial value after every action selection<br/>
	 * <b>actionselection.candidateThresholdDecay</b> - string, factory name of the candidate threshold DecayStrategy<br/>
	 * 
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModuleImpl#init()
	 */
	@Override
	public void init() {
		broadcastExcitationFactor = getParam(
				"actionselection.broadcastExcitationFactor",
				DEFAULT_BROADCAST_EXCITATION_FACTOR);
		successorExcitationFactor = getParam(
				"actionselection.successorExcitationFactor",
				DEFAULT_SUCCESSOR_EXCITATION_FACTOR);
		predecessorExcitationFactor = getParam(
				"actionselection.predecessorExcitationFactor",
				DEFAULT_PREDECESSOR_EXCITATION_FACTOR);
		conflictorExcitationFactor = getParam(
				"actionselection.conflictorExcitationFactor",
				DEFAULT_CONFLICTOR_EXCITATION_FACTOR);
		contextSatisfactionThreshold = getParam("actionselection.contextSatisfactionThreshold",
					DEFAULT_CONTEXT_SATISFACTION_THRESHOLD);		
		initialCandidateThreshold = getParam(
				"actionselection.initialCandidateThreshold",
				DEFAULT_INITIAL_CANDIDATE_THRESHOLD);
		candidateThreshold = initialCandidateThreshold;
		ElementFactory factory = ElementFactory.getInstance();
		String name = getParam(
				"actionselection.candidateThresholdDecay",
				DEFAULT_CANDIDATE_THRESHOLD_DECAY);
		thresholdReductionStrategy = factory.getDecayStrategy(name);
		if (thresholdReductionStrategy == null) {
			logger.log(Level.SEVERE,
							"Could not get candidate threshold reducer strategy with name {1} from the factory",
							new Object[] {TaskManager.getCurrentTick(), name });
		}

		taskSpawner.addTask(new BehaviorNetworkBackgroundTask());
	}

	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		actionSelectionListeners.add(listener);
	}

	@Override
	public void addPreafferenceListener(PreafferenceListener listener) {
		preafferenceListeners.add(listener);
	}

	@Override
	public void receiveBehavior(Behavior b) {
		logger.log(Level.FINEST, "Received behavior: {1}", new Object[] {
				TaskManager.getCurrentTick(), b });

		behaviors.put(b.getId(), b);
		indexBehaviorByElements(b, b.getContextConditions(),
				behaviorsByContextCondition);
		indexBehaviorByElements(b, b.getAddingList(), behaviorsByAddingItem);
		indexBehaviorByElements(b, b.getDeletingList(), behaviorsByDeletingItem);
		// indexBehaviorByElements(b, b.getNegatedContextConditions(),
		// behaviorsByNegContextCondition);
	}

	/*
	 * Utility method to index the behaviors into a map by elements.
	 */
	private void indexBehaviorByElements(Behavior behavior,
			Collection<Condition> elements, Map<Condition, Set<Behavior>> map) {
		for (Condition element : elements) {
			synchronized (element) {
				Set<Behavior> values = map.get(element);
				if (values == null) {
					values = new HashSet<Behavior>();
					map.put(element, values);
				}
				values.add(behavior);
			}
		}
	}

	private class BehaviorNetworkBackgroundTask extends FrameworkTaskImpl {
		public void runThisFrameworkTask() {
			passActivationFromSchemes();
			passActivationAmongBehaviors();
			attemptActionSelection();
			logger.log(Level.FINEST, "BehaviorNetwork completes one execution cycle.",
					TaskManager.getCurrentTick());
		}
	}

	private void passActivationFromSchemes() {
		for (Behavior b : behaviors.values()) {
			double amount = b.getGeneratingScheme().getActivation()
					* broadcastExcitationFactor;
			b.excite(amount);
		}
	}

	/*
	 * This implementation is different to the original Maes' code. Here the
	 * activation is updated directly and the new value is used to compute the
	 * passing activation.
	 */
	private void passActivationAmongBehaviors() {
		Long[] keyPermutation = getRandomPermutation();
		for (Long key : keyPermutation) {
			Behavior b = behaviors.get(key);
			if (isAllContextConditionsSatisfied(b)) {
				passActivationToSuccessors(b);
			} else {
				passActivationToPredecessors(b);
			}
			passActivationToConflictors(b);
		}
	}
	
	/*
	 * Returns a random permutation of the keys of the behaviors in the network.
	 */
	private Long[] getRandomPermutation() {
		Long[] keys = (Long[]) behaviors.keySet().toArray();
		for (int i = 0; i < keys.length - 1; i++) {
			int swapPosition = (int) (Math.random() * (keys.length - i)) + i;
			Long stored = keys[i];
			keys[i] = keys[swapPosition];
			keys[swapPosition] = stored;
		}
		return keys;
	}

	/*
	 * Only excite successor if precondition is not yet satisfied.
	 */
	private void passActivationToSuccessors(Behavior b) {
		// For successors with positive conditions
		for (Condition addCondition : b.getAddingList()) {
			if(isContextConditionSatisfied(addCondition) == false){
				Set<Behavior> successors = getSuccessors(addCondition);
				if (successors != null) {
					auxPassActivationToSuccessors(b, addCondition, successors);
				}
			}
		}
		// // For successors with negated conditions
		// for (Condition deleteProposition : behavior.getDeletingList()) {
		// Set<Behavior> successors = getSuccessors(deleteProposition);
		// if (successors != null) {
		// spreadActivationSucc(behavior, deleteProposition, successors);
		// }
		// }
	}

	private Set<Behavior> getSuccessors(Condition addingCondition) {
		return behaviorsByContextCondition.get(addingCondition);
	}

	/*
	 * Passes activation from b to its successors for a particular condition.
	 * Condition has been determined to be satisfied.
	 */
	private void auxPassActivationToSuccessors(Behavior b,Condition c,Set<Behavior> successors) {
		for (Behavior successor : successors) {
			if (successor != b) { //perhaps a behavior can use up one of its context condition
				double amount = b.getActivation()/getUnsatisfiedContextCount(successor)*successorExcitationFactor;
				successor.excite(amount);
				logger.log(Level.FINEST,
						   "Behavior {1} excites successor, {2}, amount: {3} because of {4}",
						   new Object[]{TaskManager.getCurrentTick(),b,successor,amount,c});
			}
		}
	}

	/*
	 * Don't bother exciting a predecessor for a precondition that is already
	 * satisfied.
	 */
	private void passActivationToPredecessors(Behavior b) {
		// For positive conditions
		for (Condition contextCond : b.getContextConditions()) {
			if (isContextConditionSatisfied(contextCond) == false) {
				Set<Behavior> predecessors = behaviorsByAddingItem.get(contextCond);
				if (predecessors != null) {
					auxPassActivationPredecessors(b, contextCond, predecessors);
				}
			}
		}
		// // For negated conditions
		// for (Condition contextCondition : behavior
		// .getNegatedContextConditions()) {
		// if (!behavior.isContextConditionSatisfied(contextCondition)) {
		// Set<Behavior> predecessors = null;
		// predecessors = behaviorsByDeletingItem.get(contextCondition);
		//
		// if (predecessors != null) {
		// spreadActivationPred(behavior, contextCondition,
		// predecessors);
		// }
		// }
		// }
	}

	private void auxPassActivationPredecessors(Behavior b, Condition c,
			Set<Behavior> predecessors) {
		for (Behavior predecessor : predecessors) {
			//think about being your own predecessor
			double amount = b.getActivation()/getUnsatisfiedContextCount(b)*predecessorExcitationFactor;
			predecessor.excite(amount);
			logger.log(Level.FINEST,
							"Behavior {1} excites predecessor, {2}, amount: {3} because of {4}",
							new Object[] { TaskManager.getCurrentTick(), b,
									predecessor, amount, c });
		}
	}

	private void passActivationToConflictors(Behavior b) {
		boolean isMutualConflict = false;
		for (Condition condition : b.getContextConditions()) {
			Set<Behavior> conflictors = behaviorsByDeletingItem.get(condition);
			if (conflictors != null) {
				for (Behavior conflictor : conflictors) {
					isMutualConflict = false;
					if ((b != conflictor)
							&& (b.getActivation() < conflictor.getActivation())) {
						for (Condition conflictorPreCondition : conflictor
								.getContextConditions()) {
							Set<Behavior> conflictorConflictors = behaviorsByDeletingItem
									.get(conflictorPreCondition);
							if (conflictorConflictors != null) {
								// check if there is a mutual conflict
								isMutualConflict = conflictorConflictors
										.contains(b);
								if (isMutualConflict) {
									break;
								}
							}
						}
						// // for each conflictor negated context condition
						// if (!isMutualConflict) {
						// for (Condition conflictorPreCondition : conflictor
						// .getNegatedContextConditions()) {
						// Set<Behavior> conflictorConflictors =
						// behaviorsByAddingItem
						// .get(conflictorPreCondition);
						// // if there is a mutual conflict
						// if (conflictorConflictors != null) {
						// isMutualConflict = conflictorConflictors
						// .contains(behavior);
						// if (isMutualConflict)
						// break;
						// }
						// }
						// }
					}
					// No mutual conflict then inhibit the conflictor of behavior
					if (!isMutualConflict) {
						auxPassActivationToConflictor(b, conflictor);
					}
				}//for each conflictor
			}
		}// for each context condition
	}

	private void auxPassActivationToConflictor(Behavior b, Behavior conflictor) {
		double inhibitionAmount = -(b.getActivation() * conflictorExcitationFactor)
				/ b.getContextSize();
		conflictor.excite(inhibitionAmount);
		logger.log(Level.FINEST, "{1} inhibits conflictor {2} amount {3}",
				new Object[] { TaskManager.getCurrentTick(), b.getLabel(),
						conflictor.getLabel(), inhibitionAmount });
	}

	/*
	 * Select one behavior to be executed. The chosen behavior action is
	 * executed, its activation is set to 0.0 and the candidate threshold is
	 * reseted. If no behavior is selected, the candidate threshold is reduced.
	 */
	private void attemptActionSelection() {
		Behavior winningBehavior = selectBehavior(getSatisfiedBehaviors(),
				candidateThreshold);
		if (winningBehavior != null) {
			sendAction(winningBehavior);
			resetCandidateThreshold();
			winningBehavior.setActivation(0.0);
			Action a = winningBehavior.getAction();
			logger.log(Level.FINER, "Selected behavior: {1} with action: {2}",
					new Object[] { TaskManager.getCurrentTick(),
							winningBehavior, a });
		} else {
			reduceCandidateThreshold();
		}
	}

	private Set<Behavior> getSatisfiedBehaviors() {
		Set<Behavior> result = new HashSet<Behavior>();
		for (Behavior b : behaviors.values()) {
			if (isAllContextConditionsSatisfied(b)) {
				result.add(b);
			}
		}
		return result;
	}
	
	private boolean isContextConditionSatisfied(Condition c) {
		return c.getActivation() >= contextSatisfactionThreshold;
	}
//	private boolean isNegContextConditionSatisifed(Condition c){
//		return (1.0 - negContext.get(prop.getConditionId()).getActivation()) >= contextSatisfactionThreshold;
//		return false;
//	}

	private boolean isAllContextConditionsSatisfied(Behavior b) {
		for(Condition c: b.getContextConditions()){
			if(c.getActivation() < contextSatisfactionThreshold){
				return false;
			}
		}
//		for(Condition c:negContext.values()){
//			if((1.0 - c.getActivation()) < contextSatisfactionThreshold){
//				return false;
//			}
//		}
		return true;
	}
	
	private int getUnsatisfiedContextCount(Behavior b) {
		int count = 0;
		for(Condition c: b.getContextConditions()){
			if(c.getActivation() < contextSatisfactionThreshold){
				count++;
			}
		}
//		for(Condition c:negContext.values()){
//			if((1-c.getActivation()) < contextSatisfactionThreshold){
//				count++;
//			}
//		}
		return count;
	}

	/**
	 * Selects a behavior (containing an action) for execution. This
	 * implementation picks a behavior over candidate threshold and that has
	 * maximum activation (alpha) among all behaviors. If there is a tie then
	 * one behavior is selection randomly.
	 * 
	 * @param behaviors
	 *            {@link Collection} of behaviors currently available in the
	 *            module
	 * @param candidateThreshold
	 *            threshold for a behavior to be a candidate
	 * @return winning Behavior or null if none was chosen
	 */
	@Override
	public Behavior selectBehavior(Collection<Behavior> behaviors,
			double candidateThreshold) {
		double maxActivation = 0.0;
		List<Behavior> winners = new ArrayList<Behavior>();
		for (Behavior b : behaviors) {
			double currentActivation = b.getTotalActivation();
			if (currentActivation > candidateThreshold) {
				if (currentActivation > maxActivation) {
					winners.clear();
					winners.add(b);
					maxActivation = currentActivation;
				} else if (currentActivation == maxActivation) {
					winners.add(b);
				}
			}
		}
		Behavior winner = null;
		switch (winners.size()) {
		case 0:
			winner = null;
			break;
		case 1:
			winner = winners.get(0);
			logger.log(Level.FINER, "Winner: {1}  activation: {2}",
					new Object[] { TaskManager.getCurrentTick(),
							winner.getLabel(), maxActivation });
			break;
		default:
			winner = winners.get((int) (Math.random() * winners.size()));
			logger.log(Level.FINER, "Winner: {1}  activation: {2}",
					new Object[] { TaskManager.getCurrentTick(),
							winner.getLabel(), maxActivation });
		}
		return winner;
	}

	private void reduceCandidateThreshold() {
		candidateThreshold = thresholdReductionStrategy.decay(
				candidateThreshold, 1);
		logger.log(Level.FINEST, "Candidate threshold REDUCED to {1}",
						new Object[] { TaskManager.getCurrentTick(),
								candidateThreshold });
	}

	private void resetCandidateThreshold() {
		candidateThreshold = initialCandidateThreshold;
		logger.log(Level.FINEST, "Candidate threshold RESET to {1}",
						new Object[] { TaskManager.getCurrentTick(),
								candidateThreshold });
	}

	private void sendAction(Behavior b) {
		for (ActionSelectionListener l : actionSelectionListeners) {
			l.receiveAction(b.getAction());
		}
	}

	@Override
	public void decayModule(long t) {
		for (Behavior b : behaviors.values()) {
			b.decay(t);
			if (b.isRemovable()) {
				removeBehavior(b);
			}
		}
	}

	/*
	 * Removes specified behavior from the behavior net, severing all links to
	 * other behaviors and removing it from the specified stream which contained
	 * it.
	 */
	private void removeBehavior(Behavior b) {
		for (Condition c : b.getContextConditions()) {
			behaviorsByContextCondition.get(c).remove(b);
		}
		for (Condition c : b.getAddingList()) {
			behaviorsByAddingItem.get(c).remove(b);
		}
		for (Condition c : b.getDeletingList()) {
			behaviorsByDeletingItem.get(c).remove(b);
		}
		behaviors.remove(b.getId());
		// for (Condition precondition :
		// behavior.getNegatedContextConditions()){
		// behaviorsByContextCondition.get(precondition).remove(behavior);
		// }
		logger.log(Level.FINEST, "Behavior {1} was removed from BehaviorNet.",
				new Object[] { TaskManager.getCurrentTick(), b });
	}

	@Override
	public Collection<Behavior> getBehaviors() {
		Collection<Behavior> aux = behaviors.values();
		if(aux == null){
			return null;
		}
		return Collections.unmodifiableCollection(aux);
	}

	// /*
	// * comment after settling code using these
	// */
	// private enum ConditionSet {
	// CONTEXT, NEGCONTEXT, ADDING_LIST, DELETING_LIST
	// }
	/*
	 * For each proposition in the current environment get the behaviors indexed
	 * by that proposition For each behavior, excite it.
	 */
	// private void passActivationFromBroadcast() {
	// for (Behavior b : behaviors.values()) {
	// // Another option is to use GoalDegree and Activation
	// if (condition.getNetDesirability() < desirabilityThreshold) {
	// if (behaviorsByContextCondition.containsKey(condition)) {
	// passActivationToContextOrResult(condition,
	// behaviorsByContextCondition, ConditionSet.CONTEXT);
	// } else if (behaviorsByNegContextCondition
	// .containsKey(condition)) {
	// passActivationToContextOrResult(condition,
	// behaviorsByNegContextCondition,
	// ConditionSet.NEGCONTEXT);
	// }
	// } else {
	// if (behaviorsByAddingItem.containsKey(condition)) {
	// passActivationToContextOrResult(condition,
	// behaviorsByAddingItem, ConditionSet.ADDING_LIST);
	// } else if (behaviorsByDeletingItem.containsKey(condition)) {
	// // Change this if protected goals are used
	// passActivationToContextOrResult(condition,
	// behaviorsByDeletingItem, ConditionSet.DELETING_LIST);
	// }
	// }
	// }
	// }
	// private void passActivationToContextOrResult(Condition condition,
	// ConcurrentMap<Condition, Set<Behavior>> map,
	// ConditionSet conditionSetType) {
	// double excitationAmount = 0.0;
	// Condition c;
	// Set<Behavior> behaviors = map.get(condition);
	// for (Behavior behavior : behaviors) {
	// switch (conditionSetType) {
	// case CONTEXT:
	// c = behavior.getContextCondition(condition.getConditionId());
	//
	// excitationAmount = (condition.getTotalActivation()
	// * broadcastExcitationFactor * c.getWeight() / behavior
	// .getContextSize());
	//
	// behavior.excite(excitationAmount);
	// break;
	// case NEGCONTEXT:
	// c = behavior.getContextCondition(condition.getConditionId());
	// excitationAmount = ((1.0 - condition.getTotalActivation())
	// * broadcastExcitationFactor * c.getWeight())
	// / behavior.getContextSize();
	//
	// behavior.excite(excitationAmount);
	// break;
	// case ADDING_LIST:
	// // excitationAmount = condition.getNetDesirability()
	// // * broadcastExcitationFactor;
	// behavior.excite(excitationAmount);
	// break;
	// case DELETING_LIST:
	// // Check this
	// // behavior.excite(-excitationAmount /
	// // behavior.getResultSize());
	// // behavior.excite(-excitationAmount);
	// break;
	// }
	// logger.log(Level.FINEST, "{1}: {2} for {3}",
	// new
	// Object[]{TaskManager.getCurrentTick(),behavior,excitationAmount,condition});
	// }
	// }

	// private static final double DEFAULT_MEAN_ACTIVATION = 0.05;
	/*
	 * The mean activation of behaviors. It is used for the Normalization (akin
	 * to PI from Maes' implementation)
	 */
	// private double meanActivation = DEFAULT_MEAN_ACTIVATION;
	/*
	 * normalize the behaviors' activation.
	 */
	// private void normalizeActivation() {
	// Double totalNetAct = 0.0;
	// Double nCount = mediaActivation * getBehaviors().size();
	// for (Behavior behavior : getBehaviors()) {
	// totalNetAct += behavior.getActivation();
	// }
	//
	// for (Behavior behavior : getBehaviors()) {
	// double activation = behavior.getActivation() * totalNetAct / nCount;
	// // behavior.decay(1L,totalNetAct,nCount);
	// behavior.setActivation(activation);
	//		
	// logger.log(Level.FINEST,
	// "Decaying behavior: " + behavior.getActivation());
	// }
	// }
}