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

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

public class BehaviorImpl extends ActivatibleImpl implements Behavior{

	private static final Logger logger = Logger.getLogger(BehaviorImpl.class.getCanonicalName());
	private static final double DEFAULT_CS_THRESHOLD = 0.5;

	private static long idCounter = 0;

	/**
	 * Label for description
	 */
	private String label = "blank behavior";

	/**
	 * Context for this behavior
	 */
	private Map<Object,Condition> context = new HashMap<Object,Condition>();

	/**
	 * Context for this behavior
	 */
	private Map<Object,Condition> negContext = new HashMap<Object,Condition>();

	/**
	 * Set of nodes that this scheme adds
	 */
	private Map<Object,Condition> addingList = new HashMap<Object,Condition>();

	/**
     * 
     */
	private Map<Object,Condition> deletingList = new HashMap<Object,Condition>();

	/**
	 * Id of the action(s) in sensory-motor to be taken if this behavior
	 * executes
	 */
	private Action action;

	/**
	 * unique identifier
	 */
	private long id;

	private double contextSatisfactionThreshold = DEFAULT_CS_THRESHOLD;

	private Scheme generatingScheme;
	
	
	public BehaviorImpl(Action action){
		this(idCounter++, action);
	}

	public BehaviorImpl(long id, Action action) {
		this.id = id;
		this.action = action;
	}

	/**
	 * @param id
	 */
	public void setId(long id) {
		this.id = id;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public boolean isContextConditionSatisfied(Condition prop) {
		if (context.containsKey(prop.getId()))
			return context.get(prop.getId()).getActivation() >= contextSatisfactionThreshold;
		if (negContext.containsKey(prop.getId()))
				return (1.0 - negContext.get(prop.getId()).getActivation()) >= contextSatisfactionThreshold;
		return false;
	}

	@Override
	public boolean isAllContextConditionsSatisfied() {
		for(Condition c:context.values()){
			if(c.getActivation() < contextSatisfactionThreshold){
				return false;
			}
		}
		for(Condition c:negContext.values()){
			if((1.0 - c.getActivation()) < contextSatisfactionThreshold){
				return false;
			}
		}
		return true;
	}

	// start add methods
	@Override
	public boolean addContextCondition(Condition condition) {
		logger.log(Level.FINEST, "Adding context condition " +
								 condition + " to " + label);
		return (context.put(condition.getId(),condition) == null);
	}

	@Override
	public boolean addContextCondition(Condition condition, boolean negated) {
		logger.log(Level.FINEST, "Adding context condition " +
								 condition + " to " + label);
		if(!negated){
			return (context.put(condition.getId(),condition) == null);
		}else{
			return (negContext.put(condition.getId(),condition) == null);			
		}
	}

	@Override
	public boolean addToAddingList(Condition addResult) {
		logger.log(Level.FINEST, "Adding add result " +
				 addResult + " to " + label);
		return (addingList.put(addResult.getId(),addResult) == null);
	}

	@Override
	public boolean addToDeletingList(Condition deleteResult) {
		logger.log(Level.FINEST, "Adding delete result " +
				 deleteResult + " to " + label);
		return (deletingList.put(deleteResult.getId(),deleteResult) == null);
	}

	// Get methods
	@Override
	public Collection<Condition> getContextConditions() {
		return context.values();
	}

	@Override
	public Collection<Condition> getNegatedContextConditions() {
		return negContext.values();
	}

	@Override
	public Collection<Condition> getAddingList() {
		return addingList.values();
	}

	@Override
	public Collection<Condition> getDeletingList() {
		return deletingList.values();
	}

	@Override
	public int getContextSize() {
		return context.size() + negContext.size();
	}

	@Override
	public double getAddingListCount() {
		return addingList.size();
	}

	@Override
	public double getDeletingListCount() {
		return deletingList.size();
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

	public boolean equals(Object o) {
		if (!(o instanceof Behavior))
			return false;

		Behavior behavior = (Behavior) o;
		return behavior.getId() == id && behavior.getAction() == action;
	}

	public int hashCode() {
		int hash = 1;
		Long v1 = new Long(id);
		Long v2 = new Long(action.getId());
		hash = hash * 31 + v2.hashCode();
		hash = hash * 31 + (v1 == null ? 0 : v1.hashCode());
		return hash;
	}

	@Override
	public boolean containsContextCondition(Condition contextCondition) {
		return context.containsKey(contextCondition.getId());
	}

	@Override
	public boolean containsNegatedContextCondition(Condition contextCondition) {
		return negContext.containsKey(contextCondition.getId());
	}

	@Override
	public boolean containsAddingItem(Condition addItem) {
		return addingList.containsKey(addItem.getId());
	}

	@Override
	public boolean containsDeletingItem(Condition deleteItem) {
		return deletingList.containsKey(deleteItem.getId());
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public double getResultSize() {
		return addingList.size() + deletingList.size();
	}

	@Override
	public int getUnsatisfiedContextCount() {
		int count=0;
		for(Condition c:context.values()){
			if(c.getActivation() < contextSatisfactionThreshold){
				count++;
			}
		}
		for(Condition c:negContext.values()){
			if((1-c.getActivation()) < contextSatisfactionThreshold){
				count++;
			}
		}
		return count;
	}
	
	@Override
	public String toString(){
		return getLabel() + "-" + getId();
	}


//	@Override
//	public boolean isContextConditionNegated(Condition contextCondition) {
//		Condition c = context.get(contextCondition.getId());
//		return (c!=null && c.isNegated());
//	}
	@Override
	public Condition getContextCondition(Object id) {
		Condition c = context.get(id);
		if (c == null){
			c= negContext.get(id);
		}
		return c;
	}

	@Override
	public Scheme getGeneratingScheme() {
		return generatingScheme;
	}

	@Override
	public void setGeneratingScheme(Scheme s) {
		generatingScheme  = s;
	}
	
}// class