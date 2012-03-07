/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Condition;
import edu.memphis.ccrg.lida.framework.shared.RootableNode;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryImpl.ConditionType;

/**
 * Default implementation of {@link Scheme}
 * @author Ryan J. McCall
 * @author Javier Snaider
 */
public class SchemeImpl extends LearnableImpl implements Scheme {
	
	private static final Logger logger = Logger.getLogger(SchemeImpl.class.getCanonicalName());
	private static final double DEFAULT_RELIABILITY_THRESHOLD = 0.5; //TODO this can be a property of procedural memory
	private static long idCounter = 0;//TODO Factory support for Scheme
		
	private String label;
	private long id;
	private boolean innate;
	private int numberOfExecutions;
	private int successfulExecutions;
	private double reliabilityThreshold = DEFAULT_RELIABILITY_THRESHOLD; 
	
	private Action action;
	private Map<Object,Condition> context = new ConcurrentHashMap<Object,Condition>();
	private Map<Object,Condition> addingList = new ConcurrentHashMap<Object,Condition>();
	private Map<Object,Condition> deletingList = new ConcurrentHashMap<Object,Condition>();
//	private Map<Object,Condition> negContext = new ConcurrentHashMap<Object,Condition>();
	private ProceduralMemoryImpl pm;
	/*
	 * The weight of the context in the calculation of scheme salience
	 */
	private static double contextWeight = 1.0;
	/*
	 * The weight of the adding list in the calculation of scheme salience
	 */
	private static double addingListWeight = 1.0;

	/**
	 * Constructs a new scheme with default values
	 */
	SchemeImpl(){
		id = idCounter++;
	}
	
	/**
	 * Intended for testing only
	 * @param label {@link String}
	 * @param a {@link Action}
	 */
	SchemeImpl(String label, Action a){
		this.label = label;
		this.action = a;
	}
	
	/**
	 * Intended for testing only
	 * @param id Scheme's id
	 */
	SchemeImpl(long id){
		this();
		this.id = id;
	}
	
	/**
	 * Sets the {@link ProceduralMemoryImpl} to which this {@link SchemeImpl} belongs.
	 * @param pm a {@link ProceduralMemoryImpl}
	 */
	void setProceduralMemory(ProceduralMemoryImpl pm){
		this.pm = pm;
	}
	
	//Scheme methods
	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public long getId() {
		return id;
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
	public void setInnate(boolean in) {
		innate = in;
	}

	@Override
	public boolean isInnate() {
		return innate;
	}
	

	/**
	 * Gets the average activation of this unit's context conditions.
	 * @return average activation of unit's context
	 */
	protected double getAverageContextActivation(){
		if(context.size() == 0){
			return 0.0;
		}
		double aggregateActivation = 0.0;
		for(Condition c: context.values()){
			//if required to use Condition weight, use it here
			aggregateActivation += c.getActivation();
		}
		return aggregateActivation / context.size();
	}
		
	/**
	 * Gets the average net desirability of this unit's adding list
	 * @return average net desirability of this unit's adding list
	 */
	protected double getAverageAddingListNetDesirability(){
		int numConditions = 0;
		double aggregateNetDesirability = 0.0;
		for(Condition c: addingList.values()){
			if(c instanceof RootableNode){
				//if required to use Condition weight, use it here
				aggregateNetDesirability += ((RootableNode) c).getNetDesirability(); 
				numConditions++;
			}
		}
		if(numConditions == 0){
			return 0.0;
		}else{
			return aggregateNetDesirability / numConditions;
		}
	}
	
	@Override
	public double getActivation(){
		double overallSalience = contextWeight * getAverageContextActivation()+
									addingListWeight * getAverageAddingListNetDesirability();
		return (overallSalience > 1.0)? 1.0 :overallSalience;		
	}
	
	//Learnable override
	@Override
	public void decayBaseLevelActivation(long ticks){
		if(!innate){
			super.decayBaseLevelActivation(ticks);
		}
	}
	
	//ProceduralUnit methods

	@Override
	public int getExecutions() {
		return numberOfExecutions;
	}
	
	@Override
	public double getReliabilityThreshold() {
		return reliabilityThreshold;
	}

	@Override
	public void setReliabilityThreshold(double t) {
		reliabilityThreshold = t;
	}

	@Override
	public void setAction(Action a) {
		action = a;
	}

	@Override
	public boolean addCondition(Condition c,ConditionType type) {
		boolean wasAdded = false;
		Condition condition = pm.addCondition(c);
		if(condition != null){
			Map<Object,Condition> map = null;
			switch(type){
				case CONTEXT:
					map = context;
					break;
				case ADDINGLIST:
					map = addingList;
					break;
				case DELETINGLIST:
					map = deletingList;
					break;
				case NEGATEDCONTEXT:
					break;
			}
			if(map != null){
				wasAdded = (map.put(condition.getConditionId(),condition) == null);
			}
			if(!wasAdded){
				logger.log(Level.WARNING, "Error adding condition {1}. Condition added to ProceduralMemory but not to Scheme.", 
						new Object[]{TaskManager.getCurrentTick(),c});
			}else{
				pm.indexScheme(this,condition,type);
			}
		}
		return wasAdded;
	}

	@Override
	public Collection<Condition> getContextConditions() {
		Collection<Condition> aux = context.values();
		return (aux == null)? null : Collections.unmodifiableCollection(aux);
	}

	@Override
	public Collection<Condition> getAddingList() {
		Collection<Condition> aux = addingList.values();
		return (aux == null)? null : Collections.unmodifiableCollection(aux);
	}

	@Override
	public Collection<Condition> getDeletingList() {
		Collection<Condition> aux = deletingList.values();
		return (aux == null)? null : Collections.unmodifiableCollection(aux);
	}

	@Override
	public int getContextSize() {
		return context.size();
//		return context.size() + negContext.size();
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
	public String getLabel() {
		return label;
	}

	@Override
	public boolean containsContextCondition(Condition c) {
		return context.containsKey(c.getConditionId());
	}

	@Override
	public boolean containsAddingItem(Condition c) {
		return addingList.containsKey(c.getConditionId());
	}

	@Override
	public boolean containsDeletingItem(Condition c) {
		return deletingList.containsKey(c.getConditionId());
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
	public Condition getContextCondition(Object id) {
		Condition c = context.get(id);
//		if (c == null){
//			c= negContext.get(id);
//		}
		return c;
	}
	
//	@Override
//	public boolean addContextCondition(Condition condition, boolean negated) {
//		logger.log(Level.FINEST, "Adding context condition " +
//								 condition + " to " + label);
//		if(!negated){
//			return (context.put(condition.getConditionId(),condition) == null);
//		}else{
//			return (negContext.put(condition.getConditionId(),condition) == null);			
//		}
//	}

//	@Override
//	public Collection<Condition> getNegatedContextConditions() {
//		return negContext.values();
//	}

//	@Override
//	public boolean containsNegatedContextCondition(Condition contextCondition) {
//		return negContext.containsKey(contextCondition.getConditionId());
//	}
	
	//Object methods
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
	
	@Override
	public String toString(){
		return getLabel() + "-" + getId();
	}

	/**
	 * @param w the contextWeight to set
	 */
	static void setContextWeight(double w) {
		if(w >= 0.0){
			SchemeImpl.contextWeight = w;
		}else{
			logger.log(Level.WARNING, "Context weight must be positive", TaskManager.getCurrentTick());
		}
	}

	/**
	 * @return the contextWeight
	 */
	static double getContextWeight() {
		return contextWeight;
	}

	/**
	 * @param w the addingListWeight to set
	 */
	static void setAddingListWeight(double w) {
		if(w >= 0.0){
			SchemeImpl.addingListWeight = w;
		}else{
			logger.log(Level.WARNING, "Adding list weight must be positive", TaskManager.getCurrentTick());
		}
	}

	/**
	 * @return the addingListWeight
	 */
	static double getAddingListWeight() {
		return addingListWeight;
	}
}