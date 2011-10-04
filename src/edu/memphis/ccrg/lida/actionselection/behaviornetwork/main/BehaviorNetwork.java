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

import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.strategies.BasicCandidationThresholdReducer;
import edu.memphis.ccrg.lida.actionselection.strategies.BasicSelector;
import edu.memphis.ccrg.lida.actionselection.strategies.CandidateThresholdReducer;
import edu.memphis.ccrg.lida.actionselection.strategies.PredecessorExciteStrategy;
import edu.memphis.ccrg.lida.actionselection.strategies.Selector;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;

/**
 * 
 * @author Javier Snaider, Ryan J McCall, Sidney D'Mello
 * 
 */
public class BehaviorNetwork {

	private static final Logger logger = Logger.getLogger(BehaviorNetwork.class
			.getCanonicalName());

	/**
	 * Starting value for candidateBehaviorThreshold
	 * 
	 */
	private final double INITIAL_CANDIDATE_BEHAVIOR_THRESHOLD = 0.9;

	/**
	 * Current threshold for becoming active (akin to THETA from Maes'
	 * Implementation)
	 */
	private double candidateBehaviorThreshold = INITIAL_CANDIDATE_BEHAVIOR_THRESHOLD;

	/**
	 * Amount of excitation by Environment (akin to PHI from Maes'
	 * Implementation)
	 */
	private double environmentExcitationFactor = 0.05; // was .2

	/**
	 * Scales the strength of activation passed from behavior to successor.
	 */
	private double successorExcitationFactor = .04; // was .15

	/**
	 * Scales the strength of activation passed from behavior to predecessor.
	 */
	private double predecessorExcitationFactor = .1; // was .35

	/**
	 * Scales the strength of activation passed from behavior to conflictor.
	 */
	private double conflictorExcitationFactor = .04; // was .15

	/**
	 * The media activation of behaviors. It is used for the Normalization (akin
	 * to PI from Maes' implementation)
	 */
	private double mediaActivation = 0.05;
	/**
	 * Threshold to identify goals
	 */
	private double goalThreshold = 0.5;

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

	// TODO
	private ExciteStrategy predecessorExcite = new PredecessorExciteStrategy();

	private Map<String, ?> parameters;

	/**
	 * Current conscious broadcast
	 */
	private Collection<Condition> envirnmentConditions = null;

	/**
	 * Listeners of this action selection
	 */
	private List<ActionSelectionListener> listeners = new ArrayList<ActionSelectionListener>();

	/**
	 * All the behaviors currently in this behavior network.
	 */
	private ConcurrentMap<Long, Behavior> behaviors = new ConcurrentHashMap<Long, Behavior>();

	/**
	 * Map of behaviors indexed by the elements appearing in their context
	 * conditions.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByContextCondition = new ConcurrentHashMap<Condition, Set<Behavior>>();

	/**
	 * Map of behaviors indexed by the elements appearing in their negated
	 * context conditions.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByNegContextCondition = new ConcurrentHashMap<Condition, Set<Behavior>>();

	/**
	 * Map of behaviors indexed by the elements appearing in their add list.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByAddingItem = new ConcurrentHashMap<Condition, Set<Behavior>>();

	/**
	 * Map of behaviors indexed by the elements appearing in their delete list.
	 */
	private ConcurrentMap<Condition, Set<Behavior>> behaviorsByDeletingItem = new ConcurrentHashMap<Condition, Set<Behavior>>();

	private AtomicBoolean actionSelectionStarted = new AtomicBoolean(false);

	private enum ConditionSet {
		CONTEXT, NEGCONTEXT, ADDING_LIST, DELETING_LIST
	}

	/**
	 * Default constructor
	 */
	public BehaviorNetwork() {
		super();
	}

	public void addActionSelectionListener(ActionSelectionListener listener) {
		listeners.add(listener);
	}

	// This way permits multiple instantiations of the same behavior
	// because each one will
	// have a different ID even if it was instantiated from the same Scheme.
	// This can be resolved by either slowing the scheme activation rate or by
	// having Behavior
	// have a originatingScheme field.
	public void receiveBehavior(Behavior newBehavior) {
		logger.log(Level.INFO, "Adding behavior: " + newBehavior);
		indexBehaviorByElements(newBehavior,
				newBehavior.getContextConditions(), behaviorsByContextCondition);
		indexBehaviorByElements(newBehavior, newBehavior
				.getNegatedContextConditions(), behaviorsByNegContextCondition);
		indexBehaviorByElements(newBehavior, newBehavior.getAddingList(),
				behaviorsByAddingItem);
		indexBehaviorByElements(newBehavior, newBehavior.getDeletingList(),
				behaviorsByDeletingItem);

		behaviors.put(newBehavior.getId(), newBehavior);
	}

	/**
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

		}// for
	}

	public void triggerActionSelection() {
		if (actionSelectionStarted.compareAndSet(false, true)) {
			selectAction();
			actionSelectionStarted.set(false);
		}
	}

	public Set<Behavior> getSatisfiedBehaviors() {
		Set<Behavior> satisfiedBehaviors = new HashSet<Behavior>();

		for (Behavior b : getBehaviors()) {
			if (b.isAllContextConditionsSatisfied()) {
				satisfiedBehaviors.add(b);
			}
		}
		return satisfiedBehaviors;
	}

	public void passActivationAmongBehaviors() {
		// TODO This implementation is different to the original Maes' code.
		// Here the activation is updated directly and the new value is used to
		// compute the passing activation.

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
	}// method

	/**
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

	/**
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

	/**
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

	// private Set<Behavior> getPredecessors(Condition precondition) {
	// return (!precondition.isNegated()) ?
	// behaviorsByAddingItem.get(precondition) :
	// behaviorsByDeletingItem.get(precondition);
	// }

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
	}// method

	// private Set<Behavior> getConflictors(Condition condition) {
	// return (condition.isNegated()) ? behaviorsByAddingItem.get(condition) :
	// behaviorsByDeletingItem.get(condition);
	// }

	private void auxSpreadConflictorActivation(Behavior behavior,
			Behavior conflictor) {
		// TODO: Check this formula
		double inhibitionAmount = -(behavior.getActivation() * conflictorExcitationFactor)
				/ behavior.getContextSize();
		conflictor.excite(inhibitionAmount);
		logger.log(Level.FINEST, behavior.getLabel() + " inhibits "
				+ conflictor.getLabel() + " amount " + inhibitionAmount);
	}// method

	/**
	 * For each proposition in the current environment get the behaviors indexed
	 * by that proposition For each behavior, excite it.
	 */
	public void passActivationFromEnvironment() {
		for (Condition condition : envirnmentConditions) {
			// TODO: Another option is to use GoalDegree and Activation
			if (condition.getNetDesirability() < goalThreshold) {
				if (behaviorsByContextCondition.containsKey(condition)) {
					passActivationToContextOrResult(condition,
							behaviorsByContextCondition, ConditionSet.CONTEXT);
				} else if (behaviorsByNegContextCondition
						.containsKey(condition)) {
					passActivationToContextOrResult(condition,
							behaviorsByNegContextCondition,
							ConditionSet.NEGCONTEXT);
				}
			} else {
				if (behaviorsByAddingItem.containsKey(condition)) {
					passActivationToContextOrResult(condition,
							behaviorsByAddingItem, ConditionSet.ADDING_LIST);
				} else if (behaviorsByDeletingItem.containsKey(condition)) {
					// TODO: Change this if protected goals are used
					passActivationToContextOrResult(condition,
							behaviorsByDeletingItem, ConditionSet.DELETING_LIST);
				}
			}
		}// for
	}// method

	/**
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
				c = behavior.getContextCondition(condition.getId());

				excitationAmount = (condition.getTotalActivation()
						* environmentExcitationFactor * c.getWeight() / behavior
						.getContextSize());

				behavior.excite(excitationAmount);
				break;
			case NEGCONTEXT:
				c = behavior.getContextCondition(condition.getId());
				excitationAmount = ((1.0 - condition.getTotalActivation())
						* environmentExcitationFactor * c.getWeight())
						/ behavior.getContextSize();

				behavior.excite(excitationAmount);
				break;

			case ADDING_LIST:
				excitationAmount = condition.getNetDesirability()
						* environmentExcitationFactor;
				behavior.excite(excitationAmount);
				break;
			case DELETING_LIST:
				// TODO: Check this!!!
				// behavior.excite(-excitationAmount /
				// behavior.getResultSize());
				// behavior.excite(-excitationAmount);
				break;
			}

			logger.log(Level.FINEST, behavior.toString() + " "
					+ excitationAmount + " for " + condition);
		}
	}

	/**
	 * Select one behavior to be executed. The chosen behavior action is
	 * executed, its activation is set to 0.0 and the BehaviorThreshold is
	 * reseted. If no behavior is selected, the BehaviorThreshold is reduced.
	 */
	public void selectAction() {
		winningBehavior = selectorStrategy.selectSingleBehavior(
				getSatisfiedBehaviors(), candidateBehaviorThreshold);

		if (winningBehavior != null) {
			logger.log(Level.FINER, "Behavior selected: " + winningBehavior);
			sendAction(winningBehavior);
			resetCandidateBehaviorThreshold();
			winningBehavior.setActivation(0.0);
			decayBehaviors();
		} else {
			reduceCandidateBehaviorThreshold();
		}
	}

	private void decayBehaviors() {
		for (Behavior behavior : getBehaviors()) {
			behavior.decay(1);
		}
	}

	private void reduceCandidateBehaviorThreshold() {
		candidateBehaviorThreshold = candidateThresholdReducer
				.reduceActivationThreshold(candidateBehaviorThreshold);
		logger.log(Level.FINEST, "Candidate behavior threshold REDUCED to "
				+ candidateBehaviorThreshold);
	}

	private void resetCandidateBehaviorThreshold() {
		candidateBehaviorThreshold = INITIAL_CANDIDATE_BEHAVIOR_THRESHOLD;
		logger.log(Level.FINEST, "Candidate behavior threshold RESET to  "
				+ candidateBehaviorThreshold);
	}

	private void sendAction(Behavior b) {
		for (ActionSelectionListener l : listeners)
			l.receiveAction(b.getAction());
	}

	/**
	 * Decay all the behaviors.
	 */
	public void normalizeActivation() {
	}

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

	/**
	 * Removes specified behavior from the behavior net, severing all links to
	 * other behaviors and removing it from the specified stream which contained
	 * it.
	 * 
	 * @param behavior
	 */
	private void removeBehavior(Behavior behavior) {

		for (Condition precondition : behavior.getContextConditions())
			behaviorsByContextCondition.get(precondition).remove(behavior);

		for (Condition precondition : behavior.getNegatedContextConditions())
			behaviorsByContextCondition.get(precondition).remove(behavior);

		for (Condition addItem : behavior.getAddingList())
			behaviorsByAddingItem.get(addItem).remove(behavior);

		for (Condition deleteItem : behavior.getDeletingList())
			behaviorsByDeletingItem.get(deleteItem).remove(behavior);

		behaviors.remove(behavior.getId());
		logger.log(Level.FINEST, "Behavior " + behavior
				+ " was removed from BehaviorNet.");
	}

	public Collection<Behavior> getBehaviors() {
		return behaviors.values();
	}

	public void setBehaviorActivationThreshold(double threshold) {
		this.candidateBehaviorThreshold = threshold;
	}

	public void setBroadcastExcitationAmount(double broadcastExciation) {
		this.environmentExcitationFactor = broadcastExciation;
	}

	public double getBehaviorActivationThreshold() {
		return candidateBehaviorThreshold;
	}

	public double getBroadcastExcitationAmount() {
		return environmentExcitationFactor;
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

	public void runOneCycle() {
		// envirnmentConditions= readEnvironment();
		passActivationFromEnvironment();
		passActivationAmongBehaviors();
		normalizeActivation();
		selectAction();
		logger.log(Level.FINEST, "BehaviorNet executed one cycle.");
	}

	public void setEnvironment(Collection<Condition> environment) {
		envirnmentConditions = environment;
	}

	public void init(Map<String, ?> parameters) {
		this.parameters = parameters;
		init();
	}

	/**
	 * This is a convenience method to initialize Tasks. It is called from
	 * init(Map<String, Object> parameters). Subclasses can overwrite this
	 * method in order to initialize the LidaTask
	 */
	public void init() {
	}

	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (parameters != null) {
			value = parameters.get(name);
		}
		if (value == null) {
			logger.log(Level.WARNING,
					"Missing parameter, check factories data or first parameter: "
							+ name);
			value = defaultValue;
		}
		return value;
	}

	public Collection<Condition> getEnvironment() {
		return envirnmentConditions;
	}

}// class