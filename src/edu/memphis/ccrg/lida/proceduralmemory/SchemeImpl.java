/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Condition;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;

/**
 * Default implementation of {@link Scheme}
 * @author Ryan J. McCall, Javier Snaider
 *
 */
public class SchemeImpl extends LearnableImpl implements Scheme {

	private static final Logger logger = Logger.getLogger(SchemeImpl.class.getCanonicalName());
	
	private static final double DEFAULT_CS_THRESHOLD = 0.5;
	private static final double DEFAULT_RELIABILITY_THRESHOLD = 0.5;
	
	private static long idGenerator = 0;	
	
	private boolean innate;
	private int numberOfExecutions;
	private int successfulExecutions;
	private double reliabilityThreshold = DEFAULT_RELIABILITY_THRESHOLD; 
	
	private Action action;
	private String label;
	private long id;

	private Map<Object,Condition> context = new HashMap<Object,Condition>();

	private Map<Object,Condition> negContext = new HashMap<Object,Condition>();

	private Map<Object,Condition> addingList = new HashMap<Object,Condition>();

	private Map<Object,Condition> deletingList = new HashMap<Object,Condition>();

	private double contextSatisfactionThreshold = DEFAULT_CS_THRESHOLD;

	/**
	 * @param id unique id
	 */
	public void setId(long id) {
		this.id = id;
	}

	public void setAction(Action action) {
		this.action = action;
	}

	@Override
	public boolean isContextConditionSatisfied(Condition prop) {
		if (context.containsKey(prop.getConditionId()))
			return context.get(prop.getConditionId()).getActivation() >= contextSatisfactionThreshold;
		if (negContext.containsKey(prop.getConditionId()))
				return (1.0 - negContext.get(prop.getConditionId()).getActivation()) >= contextSatisfactionThreshold;
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
		return (context.put(condition.getConditionId(),condition) == null);
	}

	@Override
	public boolean addContextCondition(Condition condition, boolean negated) {
		logger.log(Level.FINEST, "Adding context condition " +
								 condition + " to " + label);
		if(!negated){
			return (context.put(condition.getConditionId(),condition) == null);
		}else{
			return (negContext.put(condition.getConditionId(),condition) == null);			
		}
	}

	@Override
	public boolean addToAddingList(Condition addResult) {
		logger.log(Level.FINEST, "Adding add result " +
				 addResult + " to " + label);
		return (addingList.put(addResult.getConditionId(),addResult) == null);
	}

	@Override
	public boolean addToDeletingList(Condition deleteResult) {
		logger.log(Level.FINEST, "Adding delete result " +
				 deleteResult + " to " + label);
		return (deletingList.put(deleteResult.getConditionId(),deleteResult) == null);
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

	@Override
	public boolean containsContextCondition(Condition contextCondition) {
		return context.containsKey(contextCondition.getConditionId());
	}

	@Override
	public boolean containsNegatedContextCondition(Condition contextCondition) {
		return negContext.containsKey(contextCondition.getConditionId());
	}

	@Override
	public boolean containsAddingItem(Condition addItem) {
		return addingList.containsKey(addItem.getConditionId());
	}

	@Override
	public boolean containsDeletingItem(Condition deleteItem) {
		return deletingList.containsKey(deleteItem.getConditionId());
	}

	/**
	 * @param label the label to set
	 */
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

	@Override
	public Condition getContextCondition(Object id) {
		Condition c = context.get(id);
		if (c == null){
			c= negContext.get(id);
		}
		return c;
	}
	
	/**
	 * For testing only
	 * @param id Scheme's id
	 */
	SchemeImpl(long id){
		this();
		this.id = id;
	}
	
	/**
	 * Constructs a new scheme with default values
	 */
	public SchemeImpl(){
		this.id = idGenerator++;
	}

	/**
	 * Constructs a new scheme with specified label and action
	 * @param label Scheme's name
	 * @param a scheme's {@link Action}
	 */
	public SchemeImpl(String label, Action a) {
		this();
		this.label = label;
		this.action = a;
	}

	@Override
	public void actionExecuted() {
		numberOfExecutions++;
	}

	@Override
	public void actionSuccessful() {
		successfulExecutions++;		
	}

	@Override
	public double getReliability() {
		return (numberOfExecutions > 0) ? 
				((double) successfulExecutions)/numberOfExecutions : 0.0;
	}

	@Override
	public boolean isReliable() {
		return getReliability() >= reliabilityThreshold;
	}

	@Override
	public Behavior getInstantiation() {
		Behavior b = new BehaviorImpl(this);//TODO factory
		b.setAction(action);
		b.setActivation(getTotalActivation());
		return b;
	}
	
	@Override
	public double getActivation(){
		if(context.size() == 0){
			return 0.0;
		}
		
		double totalContextActivation = 0.0;
		for(Condition c: context.values()){
			totalContextActivation += c.getActivation();
		}
		//TODO adding and deleting activation too
		return totalContextActivation / context.size();
	}
	
	@Override
	public void setInnate(boolean innate) {
		this.innate = innate;
	}

	@Override
	public boolean isInnate() {
		return innate;
	}

	@Override
	public int getExecutions() {
		return numberOfExecutions;
	}
	
	@Override
	public double getReliabilityThreshold() {
		return reliabilityThreshold;
	}

	@Override
	public void setReliabilityThreshold(double threshold) {
		this.reliabilityThreshold = threshold;
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Scheme) {
			return ((Scheme) o).getId() == id;
		}
		return false;		
	}
	@Override
	public int hashCode() {
		return (int) id;
	}
	
}
