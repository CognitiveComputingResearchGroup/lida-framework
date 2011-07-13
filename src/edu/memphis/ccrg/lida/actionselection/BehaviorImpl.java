/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * Behavior.java
 *
 * @author Ryan J. McCall 
 */
package edu.memphis.ccrg.lida.actionselection;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * Basic implementation of {@link Behavior}
 * @author Ryan J McCall
 *
 */
public class BehaviorImpl extends ActivatibleImpl implements Behavior {

	private static final Logger logger = Logger
			.getLogger(BehaviorImpl.class.getCanonicalName());
	
	private static final double DEFAULT_CONTEXT_SATISFACTION_THRESHOLD = 0.5;
	private static long idCounter = 0;

	/*
	 * Label for description
	 */
	private String label = "blank behavior";

	/*
	 * Context for this behavior
	 */
	private NodeStructure context = new NodeStructureImpl();

	/*
	 * Set of nodes that this behavior adds
	 */
	private NodeStructure addingList = new NodeStructureImpl();

	/*
	 * Set of nodes that this behavior deletes
	 */
	private NodeStructure deletingList = new NodeStructureImpl();

	/*
	 * Id of the action(s) in sensory-motor to be taken if this behavior
	 * executes
	 */
	private Action action;

	/*
	 * unique identifier
	 */
	private long id;

	/*
	 * optimization for checking if context is all satisfied
	 */
	private AtomicInteger unsatisfiedContextConditionCount = new AtomicInteger(0);

	private double contextSatisfactionThreshold = DEFAULT_CONTEXT_SATISFACTION_THRESHOLD;

	private String contextNodeType = "NodeImpl";

	private Scheme generatingScheme;
	
	/**
	 * Default constructor
	 */
	public BehaviorImpl(){
		id = idCounter++;
	}
	
	/**
	 * 
	 * @param action {@link Action} 
	 */
	public BehaviorImpl(Action action){
		this();
		this.action = action;
	}

	// Precondition methods
	@Override
	public void deactivateAllContextConditions() {
		for (Node s : context.getNodes()){
			s.setActivation(0.0);
		}
		unsatisfiedContextConditionCount.set(getContextSize());
	}

	@Override
	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public boolean isContextConditionSatisfied(Node prop) {
		if (context.containsNode(prop)){
			return context.getNode(prop.getId()).getActivation() > contextSatisfactionThreshold;
		}
		return false;
	}

	@Override
	public boolean isAllContextConditionsSatisfied() {
		return unsatisfiedContextConditionCount.get() == 0;
	}

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
			logger.log(Level.WARNING, "BN asked to update a context condition {1} but it wasn't in the context of behavior {2}",
						new Object[]{TaskManager.getCurrentTick(),broadcastCondition.getLabel(),label});
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
			logger.log(Level.WARNING, "BN asked to update a result condition but it wasn't in the result of behavior ",
					TaskManager.getCurrentTick());
			
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
	@Override
	public boolean addContextCondition(Node condition) {
		logger.log(Level.FINEST, "Adding context condition {1} to  {2}", new Object[]{TaskManager.getCurrentTick(), condition, label});
		if(condition.getActivation() < contextSatisfactionThreshold){
			unsatisfiedContextConditionCount.incrementAndGet();
		}
		
		return (context.addDefaultNode(condition) != null);
	}

	@Override
	public boolean addToAddingList(Node addResult) {
		logger.log(Level.FINEST, "Adding add result {1} to {2}", new Object[]{TaskManager.getCurrentTick(), addResult, label});
		return addingList.addDefaultNode(addResult) != null;
	}

	@Override
	public boolean addToDeletingList(Node deleteResult) {
		logger.log(Level.FINEST, "Adding delete result {1} to {2}", new Object[]{TaskManager.getCurrentTick(), deleteResult, label});
		return deletingList.addDefaultNode(deleteResult) != null;
	}

	// Get methods
	@Override
	public Collection<Node> getContextNodes() {
		return context.getNodes();
	}

	@Override
	public NodeStructure getAddingList() {
		return addingList;
	}

	@Override
	public NodeStructure getDeletingList() {
		return deletingList;
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
	public Action getAction() {
		return action;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof Behavior){
			Behavior behavior = (Behavior) o;
			return behavior.getId() == id;
		}
		return false;		
	}

	@Override
	public int hashCode() {
		return (int)id;
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
	
	@Override
	public String toString(){
		return getLabel() + "-" + getId();
	}

	@Override
	public NodeStructure getContext() {
		return context;
	}

}