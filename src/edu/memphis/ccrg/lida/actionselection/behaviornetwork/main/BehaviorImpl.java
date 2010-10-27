/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * Behavior.java
 *
 * @author ryanjmccall 
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.ConcurrentHashSet;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.Stream;

public class BehaviorImpl extends ActivatibleImpl implements Behavior {

	private static Logger logger = Logger
			.getLogger("lida.behaviornetwork.main.Behavior");

	/**
	 * Label for description
	 */
	private String label = "blank behavior";

	/**
	 * Context for this behavior
	 */
	private NodeStructure context = new NodeStructureImpl();

	/**
	 * Set of nodes that this scheme adds
	 */
	private NodeStructure addingList = new NodeStructureImpl();

	/**
     * 
     */
	private NodeStructure deletingList = new NodeStructureImpl();

	/**
	 * Id of the action(s) in sensory-motor to be taken if this behavior
	 * executes
	 */
	private long actionId;

	/**
	 * unique identifier
	 */
	private long id;

	/**
	 * optimization for checking if context is all satisfied
	 */
	private AtomicInteger unsatisfiedContextConditionCount = new AtomicInteger(0);

	/**
	 * The streams that contains this behavior
	 */
	private Set<Stream> containingStreams = null;

	private double contextSatisfactionThreshold = DEFAULT_CS_THRESHOLD;

	private String contextNodeType = null;

	private Scheme generatingScheme = null;

	private static final double DEFAULT_CS_THRESHOLD = 0.5;

	private static long idCounter = 0;
	
	public BehaviorImpl(long actionId){
		this(idCounter++, actionId);
	}

	public BehaviorImpl(long id, long actionId) {
		this.id = id;
		this.actionId = actionId;
	}

	// Precondition methods
	public void deactivateAllContextConditions() {
		for (Node s : context.getNodes())
			s.setActivation(0.0);
		unsatisfiedContextConditionCount.set(getContextSize());
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setActionId(long actionId) {
		this.actionId = actionId;
	}

	public boolean isContextConditionSatisfied(Node prop) {
		if (context.containsNode(prop))
			return context.getNode(prop.getId()).getActivation() > contextSatisfactionThreshold;
		return false;
	}

	public boolean isAllContextConditionsSatisfied() {
		return (unsatisfiedContextConditionCount.get() == 0);
	}

	/**
	 * Update the activation of the context condition from the broadcast
	 */
	@Override
	public void updateContextCondition(Node broadcastCondition) {
		Node existingCondition = context.getNode(broadcastCondition.getId());
		if (existingCondition != null) { //Check if this behavior has the condition
			
			double newActivation = broadcastCondition.getActivation();
			existingCondition.setActivation(newActivation);
			if (newActivation < contextSatisfactionThreshold) {
				unsatisfiedContextConditionCount.incrementAndGet();
			}else{
				unsatisfiedContextConditionCount.decrementAndGet();
			}
		}else{
			logger.log(Level.WARNING, "BN asked to update a context condition " + 
						broadcastCondition.getLabel() + " but it wasn't in the context of behavior "
						+ label, LidaTaskManager.getActualTick());
		}
	}
	
	@Override
	public void updateAddingCondition(Node broadcastNode) {
		auxUpdateResultCondition(broadcastNode, addingList);		
	}

	@Override
	public void updateDeletingCondition(Node broadcastNode) {
		auxUpdateResultCondition(broadcastNode, deletingList);
	}
	
	private void auxUpdateResultCondition(Node condition, NodeStructure resultList){
		Node existingCondition = resultList.getNode(condition.getId());
		if (existingCondition != null) { //Check if this behavior has the condition
			double newActivation = condition.getActivation();
			existingCondition.setActivation(newActivation);
		}else{
			logger.log(Level.WARNING, "BN asked to update a result condition " + 
						condition.getLabel() + " but it wasn't in the result list of behavior "
						+ label, LidaTaskManager.getActualTick());
		}
	}

	@Override
	public void deactiveContextCondition(Node condition) {
		if ((condition = context.getNode(condition.getId())) != null) {
			condition.setActivation(0.0);
			unsatisfiedContextConditionCount.incrementAndGet();
		}
	}

	// start add methods
	public boolean addContextCondition(Node condition) {
		logger.log(Level.FINEST, "Adding context condition " +
								 condition.getLabel() + " to " + label);
		if(condition.getActivation() < this.contextSatisfactionThreshold)
			unsatisfiedContextConditionCount.incrementAndGet();
		
		return (context.addNode(condition) != null);
	}

	public boolean addToAddingList(Node addResult) {
		logger.log(Level.FINEST, "Adding add result " +
				 addResult.getLabel() + " to " + label);
		return addingList.addNode(addResult) != null;
	}

	public boolean addToDeletingList(Node deleteResult) {
		logger.log(Level.FINEST, "Adding delete result " +
				 deleteResult.getLabel() + " to " + label);
		return deletingList.addNode(deleteResult) != null;
	}

	// Get methods
	public Collection<Node> getContextConditions() {
		return context.getNodes();
	}

	public Collection<Node> getAddingList() {
		return addingList.getNodes();
	}

	public Collection<Node> getDeletingList() {
		return deletingList.getNodes();
	}

	@Override
	public int getContextSize() {
		return context.getNodeCount();
	}

	@Override
	public double getAddingListCount() {
		return addingList.getNodeCount();
	}

	@Override
	public double getDeletingListCount() {
		return deletingList.getNodeCount();
	}

	@Override
	public long getActionId() {
		return actionId;
	}

	public long getId() {
		return id;
	}

	public String getLabel() {
		return label;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Behavior))
			return false;

		Behavior behavior = (Behavior) o;
		return behavior.getId() == id && behavior.getActionId() == actionId;
	}

	public int hashCode() {
		int hash = 1;
		Long v1 = new Long(id);
		Long v2 = new Long(actionId);
		hash = hash * 31 + v2.hashCode();
		hash = hash * 31 + (v1 == null ? 0 : v1.hashCode());
		return hash;
	}

	public void addContainingStream(Stream stream) {
		containingStreams.add(stream);
	}

	@Override
	public void removeContainingStream(Stream stream) {
		containingStreams.remove(stream);
	}

	public Set<Stream> getContainingStreams() {
		return containingStreams;
	}

	@Override
	public void decay(long ticks) {
		super.decay(ticks);
		for (Node contextNode : context.getNodes()) {
			contextNode.decay(ticks);
			if (contextNode.getActivation() < contextSatisfactionThreshold)
				unsatisfiedContextConditionCount.incrementAndGet();
		}
	}

	@Override
	public void setContextNodeType(String nodeType) {
		this.contextNodeType = nodeType;
	}

	@Override
	public String getContextNodeType() {
		return contextNodeType;
	}

	@Override
	public boolean containsContextCondition(Node contextCondition) {
		return context.containsNode(contextCondition);
	}

	@Override
	public boolean containsAddingItem(Node addItem) {
		return addingList.containsNode(addItem);
	}

	@Override
	public boolean containsDeletingItem(Node deleteItem) {
		return deletingList.containsNode(deleteItem);
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public Scheme getGeneratingScheme() {
		return generatingScheme;
	}

	@Override
	public void setGeneratingScheme(Scheme s) {
		generatingScheme  = s;
	}

	@Override
	public double getResultSize() {
		return addingList.getNodeCount() + deletingList.getNodeCount();
	}

	@Override
	public int getUnsatisfiedContextCount() {
		return unsatisfiedContextConditionCount.get();
	}

	

}// class