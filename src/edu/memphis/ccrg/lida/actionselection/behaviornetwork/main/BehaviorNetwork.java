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
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionSelection;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.PreafferenceListener;
import edu.memphis.ccrg.lida.actionselection.strategies.Selector;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryListener;

/**
 * 
 * @author Javier Snaider, Ryan J McCall, Sidney D'Mello
 * 
 */
public class BehaviorNetwork extends FrameworkModuleImpl implements
		ActionSelection, ProceduralMemoryListener {

	private static final Logger logger = Logger.getLogger(BehaviorNetwork.class
			.getCanonicalName());

	/*
	 * Starting value for candidateBehaviorThreshold
	 */
	private static final double DEFAULT_MAX_CANDIDATE_THRESHOLD = 0.9;

	/*
	 * Current threshold for becoming active (akin to THETA from Maes'
	 * Implementation)
	 */
	private double candidateThreshold;
	
	private double maxCandidateThreshold = DEFAULT_MAX_CANDIDATE_THRESHOLD;

	private static final double DEFAULT_BROADCAST_EXCITATION_FACTOR = 0.05;
	
	private static final double DEFAULT_MEAN_ACTIVATION = 0.05;
	/*
	 * The mean activation of behaviors. It is used for the Normalization (akin
	 * to PI from Maes' implementation)
	 */
	private double meanActivation = DEFAULT_MEAN_ACTIVATION;
	
	//TODO init for these two
	private static final double DEFAULT_DESIRABILITY_THRESHOLD = 0.0;
	/*
	 * Threshold to identify goals
	 */
	private double desirabilityThreshold = DEFAULT_DESIRABILITY_THRESHOLD;

	/*
	 * Amount of excitation from broadcast (environment) (akin to PHI from Maes'
	 * Implementation)
	 */
	private double broadcastExcitationFactor = DEFAULT_BROADCAST_EXCITATION_FACTOR;

	private static final double DEFAULT_SUCCESSOR_EXCITATION_FACTOR = 0.04;

	/*
	 * Scales the strength of activation passed from behavior to successor.
	 */
	private double successorExcitationFactor = DEFAULT_SUCCESSOR_EXCITATION_FACTOR;

	private static final double DEFAULT_PREDECESSOR_EXCITATION_FACTOR = 0.1;

	/*
	 * Scales the strength of activation passed from behavior to predecessor.
	 */
	private double predecessorExcitationFactor = DEFAULT_PREDECESSOR_EXCITATION_FACTOR;

	private static final double DEFAULT_CONFLICTOR_EXCITATION_FACTOR = 0.04;

	/*
	 * Scales the strength of activation passed from behavior to conflictor.
	 */
	private double conflictorExcitationFactor = DEFAULT_CONFLICTOR_EXCITATION_FACTOR;

	private static final String DEFAULT_CANDIDATE_THRESHOLD_REDUCER_STRATEGY = "BasicCandidationThresholdReducer";

	private static final String DEFAULT_SELECTOR_STRATEGY = "BasicSelector";

	private static final String DEFAULT_PREDECESSOR_EXCITE_STRATEGY = "PredecessorExciteStrategy";

	/*
	 * Function by which the behavior activation threshold is reduced
	 */
	private DecayStrategy candidateThresholdReducer;

	/*
	 * Strategy to specify the way a winning behavior is chosen.
	 */
	private Selector selectorStrategy;

	// TODO use this
	private ExciteStrategy predecessorExciteStrategy;


	/*
	 * Pool of conditions in Procedural Memory
	 */
	//private ConditionPool conditionPool;

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
	 * Map of behaviors indexed by the elements appearing in their negated
	 * context conditions.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByNegContextCondition = new ConcurrentHashMap<Condition, Set<Behavior>>();

	/*
	 * Map of behaviors indexed by the elements appearing in their add list.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByAddingItem = new ConcurrentHashMap<Condition, Set<Behavior>>();

	/*
	 * Map of behaviors indexed by the elements appearing in their delete list.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByDeletingItem = new ConcurrentHashMap<Condition, Set<Behavior>>();

	private AtomicBoolean actionSelectionStarted = new AtomicBoolean(false);

	/*
	 * TODO comment
	 */
	private enum ConditionSet {
		CONTEXT, NEGCONTEXT, ADDING_LIST, DELETING_LIST
	}

	/**
	 * Default constructor
	 */
	public BehaviorNetwork() {
		super();
	}

	/**
	 * TODO
	 */
	@Override
	public void init() {
		maxCandidateThreshold = getParam(
				"actionselection.maxCandidateThreshold",
				DEFAULT_MAX_CANDIDATE_THRESHOLD);
		candidateThreshold = maxCandidateThreshold;
		
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
		meanActivation = getParam("actionselection.meanActivation",
				DEFAULT_MEAN_ACTIVATION);
		desirabilityThreshold = getParam(
				"actionselection.desirabilityThreshold",
				DEFAULT_DESIRABILITY_THRESHOLD);
		
		ElementFactory factory = ElementFactory.getInstance();
		String name = getParam(
				"actionselection.candidateThresholdReducerStrategy",
				DEFAULT_CANDIDATE_THRESHOLD_REDUCER_STRATEGY);
		candidateThresholdReducer = factory.getDecayStrategy(name);
		if (candidateThresholdReducer == null) {
			logger
					.log(
							Level.SEVERE,
							"Could not get candidate threshold reducer strategy with name {1} from the factory",
							new Object[] { TaskManager.getCurrentTick(), name });
		}

		name = getParam("actionselection.selectorStrategy",
				DEFAULT_SELECTOR_STRATEGY);
		selectorStrategy = (Selector) factory.getStrategy(name);
		if (selectorStrategy == null) {
			logger
					.log(
							Level.SEVERE,
							"Could not get selector strategy with name {1} from the factory",
							new Object[] { TaskManager.getCurrentTick(), name });
		}

		name = getParam("actionselection.predecessorExciteStrategy",
				DEFAULT_PREDECESSOR_EXCITE_STRATEGY);
		predecessorExciteStrategy = factory.getExciteStrategy(name);
		if (predecessorExciteStrategy == null) {
			logger
					.log(
							Level.SEVERE,
							"Could not get predecessor excite strategy with name {1} from the factory",
							new Object[] { TaskManager.getCurrentTick(), name });
		}
	}

	@Override
	public void addActionSelectionListener(ActionSelectionListener listener) {
		actionSelectionListeners.add(listener);
	}
	
	@Override
	public void addPreafferenceListener(PreafferenceListener listener) {
		preafferenceListeners.add(listener);
	}

	// This way permits multiple instantiations of the same behavior
	// because each one will
	// have a different ID even if it was instantiated from the same Scheme.
	// This can be resolved by either slowing the scheme activation rate or by
	// having Behavior
	// have a originatingScheme field.
	@Override
	public void receiveBehavior(Behavior b) {
		logger.log(Level.FINER, "Received behavior: {1}", new Object[] {
				TaskManager.getCurrentTick(), b });
		indexBehaviorByElements(b, b.getContextConditions(),
				behaviorsByContextCondition);
		indexBehaviorByElements(b, b.getNegatedContextConditions(),
				behaviorsByNegContextCondition);
		indexBehaviorByElements(b, b.getAddingList(), behaviorsByAddingItem);
		indexBehaviorByElements(b, b.getDeletingList(), behaviorsByDeletingItem);

		behaviors.put(b.getId(), b);
	}

	/*
	 * Abstractly defined utility method to index the behaviors into a map by
	 * elements
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

	/*
	 * TODO
	 */
	private Set<Behavior> getSatisfiedBehaviors() {
		Set<Behavior> satisfiedBehaviors = new HashSet<Behavior>();
		for (Behavior b : behaviors.values()) {
			if (b.isAllContextConditionsSatisfied()) {
				satisfiedBehaviors.add(b);
			}
		}
		return satisfiedBehaviors;
	}

	/*
	 * TODO 
	 */
	private void passActivationAmongBehaviors() {
		// TODO This implementation is different to the original Maes' code.
		// Here the activation is updated directly and the new value is used to
		// compute the passing activation.

		// TODO consider alternative ways to iterate over the behaviors so that
		// the order changes from iteration to iteration
		for (Behavior behavior : behaviors.values()) {
			if (behavior.isAllContextConditionsSatisfied()){
				spreadActivationToSuccessors(behavior);
			}else{
				spreadActivationToPredecessors(behavior);
			}
			spreadActivationToConflictors(behavior);
		}
	}

	/*
	 * Only excite successor if precondition is not yet satisfied.
	 * 
	 * @param behavior 
	 */
	private void spreadActivationToSuccessors(Behavior behavior) {
		// For successors with positive conditions
		for (Condition addProposition : behavior.getAddingList()) {
			Set<Behavior> successors = getSuccessors(addProposition);
			if (successors != null) {
				spreadActivationSucc(behavior, addProposition, successors);
			}
		}
		// For successors with negated conditions
		for (Condition deleteProposition : behavior.getDeletingList()) {
			Set<Behavior> successors = getSuccessors(deleteProposition);
			if (successors != null) {
				spreadActivationSucc(behavior, deleteProposition, successors);
			}
		}
	}

	/*
	 * @param behavior
	 * @param condition
	 * @param successors
	 */
	private void spreadActivationSucc(Behavior behavior, Condition condition,
			Set<Behavior> successors) {
		for (Behavior successor : successors) {
			// Grant activation to a successor if its precondition has
			// not yet been satisfied
			if (successor != behavior) {
				if (!successor.isContextConditionSatisfied(condition)) {
					// TODO: Check this formula
					double amount = behavior.getActivation()
							* successorExcitationFactor
							/ successor.getUnsatisfiedContextCount();
					successor.excite(amount);
					logger
							.log(Level.FINEST, behavior.getLabel() + "-->"
									+ amount + " to " + successor + " for "
									+ condition);
				}
			}
		}
	}

	private Set<Behavior> getSuccessors(Condition addProposition) {
		return behaviorsByContextCondition.get(addProposition);
	}

	/*
	 * Don't bother exciting a predecessor for a precondition that is already
	 * satisfied.
	 * 
	 * @param behavior
	 */
	private void spreadActivationToPredecessors(Behavior behavior) {

		// For positive conditions
		for (Condition contextCondition : behavior.getContextConditions()) {
			if (!behavior.isContextConditionSatisfied(contextCondition)) {
				Set<Behavior> predecessors = null;
				predecessors = behaviorsByAddingItem.get(contextCondition);
				if (predecessors != null) {
					spreadActivationPred(behavior, contextCondition,
							predecessors);
				}
			}
		}
		// For negated conditions
		for (Condition contextCondition : behavior
				.getNegatedContextConditions()) {
			if (!behavior.isContextConditionSatisfied(contextCondition)) {
				Set<Behavior> predecessors = null;
				predecessors = behaviorsByDeletingItem.get(contextCondition);

				if (predecessors != null) {
					spreadActivationPred(behavior, contextCondition,
							predecessors);
				}
			}
		}
	}

	/*
	 * @param behavior
	 * @param contextCondition
	 * @param predecessors
	 */
	private void spreadActivationPred(Behavior behavior,
			Condition contextCondition, Set<Behavior> predecessors) {
		for (Behavior predecessor : predecessors) {
			// TODO: Check this formula
			double granted = (behavior.getActivation() * predecessorExcitationFactor)
					/ behavior.getUnsatisfiedContextCount();
			predecessor.excite(granted);
			logger.log(Level.FINEST, behavior.getActivation() + " "
					+ behavior.getLabel() + "<--" + granted + " to "
					+ predecessor + " for " + contextCondition);
		}
	}

	private void spreadActivationToConflictors(Behavior behavior) {
		boolean isMutualConflict = false;
		for (Condition condition : behavior.getContextConditions()) {
			Set<Behavior> conflictors = behaviorsByDeletingItem.get(condition);
			if (conflictors != null) {
				for (Behavior conflictor : conflictors) {
					isMutualConflict = false;
					if ((behavior != conflictor)
							&& (behavior.getActivation() < conflictor
									.getActivation())) {

						// TODO put all of the conflictors in a conflictor Map
						// for each conflictor context condition
						for (Condition conflictorPreCondition : conflictor
								.getContextConditions()) {
							Set<Behavior> conflictorConflictors = behaviorsByDeletingItem
									.get(conflictorPreCondition);
							// if there is a mutual conflict
							if (conflictorConflictors != null) {
								isMutualConflict = conflictorConflictors
										.contains(behavior);
								if (isMutualConflict)
									break;
							}
						}
						// for each conflictor negated context condition
						if (!isMutualConflict) {
							for (Condition conflictorPreCondition : conflictor
									.getNegatedContextConditions()) {
								Set<Behavior> conflictorConflictors = behaviorsByAddingItem
										.get(conflictorPreCondition);
								// if there is a mutual conflict
								if (conflictorConflictors != null) {
									isMutualConflict = conflictorConflictors
											.contains(behavior);
									if (isMutualConflict)
										break;
								}
							}
						}
					}
					// No mutual conflict then inhibit the conflictor of
					// behavior
					if (!isMutualConflict)
						auxSpreadConflictorActivation(behavior, conflictor);
				}// for each conflictor
			}
		}// for
	}

	private void auxSpreadConflictorActivation(Behavior behavior,
			Behavior conflictor) {
		// TODO: Check this formula
		double inhibitionAmount = -(behavior.getActivation() * conflictorExcitationFactor)
				/ behavior.getContextSize();
		conflictor.excite(inhibitionAmount);
		logger.log(Level.FINEST, "{1} inhibits {2} amount {3}", 
				new Object[]{TaskManager.getCurrentTick(), behavior.getLabel(),conflictor.getLabel(),inhibitionAmount});
	}
	
	/*
	 * For each proposition in the current environment get the behaviors indexed
	 * by that proposition For each behavior, excite it.
	 */
	private void passActivationFromBroadcast() {
		for (Behavior b : behaviors.values()) {
			//TODO rework this
//			// TODO: Another option is to use GoalDegree and Activation
//			if (condition.getNetDesirability() < desirabilityThreshold) {
//				if (behaviorsByContextCondition.containsKey(condition)) {
//					passActivationToContextOrResult(condition,
//							behaviorsByContextCondition, ConditionSet.CONTEXT);
//				} else if (behaviorsByNegContextCondition
//						.containsKey(condition)) {
//					passActivationToContextOrResult(condition,
//							behaviorsByNegContextCondition,
//							ConditionSet.NEGCONTEXT);
//				}
//			} else {
//				if (behaviorsByAddingItem.containsKey(condition)) {
//					passActivationToContextOrResult(condition,
//							behaviorsByAddingItem, ConditionSet.ADDING_LIST);
//				} else if (behaviorsByDeletingItem.containsKey(condition)) {
//					// TODO: Change this if protected goals are used
//					passActivationToContextOrResult(condition,
//							behaviorsByDeletingItem, ConditionSet.DELETING_LIST);
//				}
//			}
		}
	}

	/*
	 * @param condition
	 * @param context
	 * @param map
	 */
	// FIXME
	private void passActivationToContextOrResult(Condition condition,
			ConcurrentMap<Condition, Set<Behavior>> map,
			ConditionSet conditionSetType) {

		double excitationAmount = 0.0;
		Condition c;

		Set<Behavior> behaviors = map.get(condition);
		for (Behavior behavior : behaviors) {
			switch (conditionSetType) {
			case CONTEXT:
				c = behavior.getContextCondition(condition.getConditionId());

				excitationAmount = (condition.getTotalActivation()
						* broadcastExcitationFactor * c.getWeight() / behavior
						.getContextSize());

				behavior.excite(excitationAmount);
				break;
			case NEGCONTEXT:
				c = behavior.getContextCondition(condition.getConditionId());
				excitationAmount = ((1.0 - condition.getTotalActivation())
						* broadcastExcitationFactor * c.getWeight())
						/ behavior.getContextSize();

				behavior.excite(excitationAmount);
				break;

			case ADDING_LIST:
				//TODO review
//				excitationAmount = condition.getNetDesirability()
//						* broadcastExcitationFactor;
				behavior.excite(excitationAmount);
				break;
			case DELETING_LIST:
				// TODO: Check this!!!
				// behavior.excite(-excitationAmount /
				// behavior.getResultSize());
				// behavior.excite(-excitationAmount);
				break;
			}

			logger.log(Level.FINEST, "{1}: {2} for {3}",
					new Object[]{TaskManager.getCurrentTick(),behavior,excitationAmount,condition});
		}
	}

	/**
	 * Select one behavior to be executed. The chosen behavior action is
	 * executed, its activation is set to 0.0 and the BehaviorThreshold is
	 * reseted. If no behavior is selected, the BehaviorThreshold is reduced.
	 */
	@Override
	public Action selectAction() {
		Action a = null;
		if (actionSelectionStarted.compareAndSet(false, true)) {
			Behavior winningBehavior = selectorStrategy.selectBehavior(getSatisfiedBehaviors(), candidateThreshold);
			if (winningBehavior != null) {
				sendAction(winningBehavior);
				resetCandidateThreshold();
				winningBehavior.setActivation(0.0);
				a = winningBehavior.getAction();
				logger.log(Level.FINER, "Selected behavior: {1} with action: {2}",
						new Object[]{TaskManager.getCurrentTick(),winningBehavior, a});
			} else {
				reduceCandidateThreshold();
			}
			actionSelectionStarted.set(false);
		}
		return a;
	}

	private void reduceCandidateThreshold() {
		candidateThreshold = candidateThresholdReducer.decay(candidateThreshold, 1);
		logger.log(Level.FINEST, "Candidate threshold REDUCED to {1}",
				new Object[]{TaskManager.getCurrentTick(), candidateThreshold});
	}

	private void resetCandidateThreshold() {
		candidateThreshold = maxCandidateThreshold;
		logger.log(Level.FINEST, "Candidate threshold RESET to {1}",
				new Object[]{TaskManager.getCurrentTick(), candidateThreshold});
	}

	private void sendAction(Behavior b) {
		for (ActionSelectionListener l : actionSelectionListeners){
			l.receiveAction(b.getAction());
		}
	}

	/*
	 * normalize the behaviors' activation.
	 */
	private void normalizeActivation() {
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
	}

	/*
	 * Removes specified behavior from the behavior net, severing all links to
	 * other behaviors and removing it from the specified stream which contained
	 * it.
	 * 
	 * @param behavior
	 */
	private void removeBehavior(Behavior behavior) {
		for (Condition precondition : behavior.getContextConditions()){
			behaviorsByContextCondition.get(precondition).remove(behavior);
		}
		for (Condition precondition : behavior.getNegatedContextConditions()){
			behaviorsByContextCondition.get(precondition).remove(behavior);
		}
		for (Condition addItem : behavior.getAddingList()){
			behaviorsByAddingItem.get(addItem).remove(behavior);
		}
		for (Condition deleteItem : behavior.getDeletingList()){
			behaviorsByDeletingItem.get(deleteItem).remove(behavior);
		}
		behaviors.remove(behavior.getId());
		logger.log(Level.FINEST, "Behavior {1} was removed from BehaviorNet.",
				new Object[]{TaskManager.getCurrentTick(),behavior});
	}

	//TODO tasks?
	/**
	 * 
	 */
	public void runOneCycle() {
		// envirnmentConditions= readEnvironment();
//		passActivationFromBroadcast();
		passActivationAmongBehaviors();
		normalizeActivation();
		selectAction();
		logger.log(Level.FINEST, "BehaviorNet executed one cycle.", TaskManager
				.getCurrentTick());
	}

	@Override
	public void decayModule(long ticks) {
		for (Behavior behavior : behaviors.values()) {
			behavior.decay(ticks);
			if(behavior.isRemovable()){
				removeBehavior(behavior);
			}
		}
	}

	@Override
	public void learn(Coalition coalition) {
	}

	@Override
	public void receiveBroadcast(Coalition coalition) {
	}
}