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
 * @author Ryan J. McCall 
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.main;

import java.util.Collection;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;

/**
 * Default implementation of {@link Behavior}
 * @author Ryan J. McCall
 * @author Javier Snaider
 */
public class BehaviorImpl extends ActivatibleImpl implements Behavior{

	/*
	 * Unique identifier
	 */
	private long behaviorId;

	private Scheme scheme;
	
	public BehaviorImpl(){
		super();
	}
	
	public BehaviorImpl(Scheme s){
		super();
		scheme = s;
	}

	/**
	 * Sets id
	 * @param id unique id
	 */
	public void setId(long id) {
		this.behaviorId = id;
	}

	@Override
	public long getId() {
		return behaviorId;
	}

	@Override
	public Scheme getGeneratingScheme() {
		return scheme;
	}

	@Override
	public void setGeneratingScheme(Scheme s) {
		scheme  = s;
	}
	
	//TODO discuss
	private double rho = 0.5;
	
	@Override
	public double getActivation(){
		//expression below equivalent to: (1 - rho) activation + rho (scheme-activation)
		return getActivation() + rho * (scheme.getActivation() - getActivation());
	}
	
	@Override
	public String toString(){
		return getLabel() + "-" + getId();
	}
	
	//ProceduralUnit methods

	@Override
	public void setAction(Action a) {
		throw new UnsupportedOperationException("Cannot modify a Behavior, only a Scheme");
//		scheme.setAction(a);
	}	
	
	@Override
	public boolean isContextConditionSatisfied(Condition c) {
		return scheme.isContextConditionSatisfied(c);
	}

	@Override
	public boolean isAllContextConditionsSatisfied() {
		return scheme.isAllContextConditionsSatisfied();
	}

	@Override
	public boolean addContextCondition(Condition c) {
		throw new UnsupportedOperationException("Cannot modify a Behavior, only a Scheme");
//		return scheme.addContextCondition(c);
	}

//	@Override
//	public boolean addContextCondition(Condition c, boolean negated) {
//		return scheme.addContextCondition(c, negated);
//	}

	@Override
	public boolean addToAddingList(Condition c) {
		throw new UnsupportedOperationException("Cannot modify a Behavior, only a Scheme");
	}

	@Override
	public boolean addToDeletingList(Condition c) {
		throw new UnsupportedOperationException("Cannot modify a Behavior, only a Scheme");
	}

	@Override
	public Collection<Condition> getContextConditions() {
		return scheme.getContextConditions();
	}

//	@Override
//	public Collection<Condition> getNegatedContextConditions() {
//		return scheme.getNegatedContextConditions();
//	}

	@Override
	public Collection<Condition> getAddingList() {
		return scheme.getAddingList();
	}

	@Override
	public Collection<Condition> getDeletingList() {
		return scheme.getDeletingList();
	}

	@Override
	public int getContextSize() {
		return scheme.getContextSize();
	}

	@Override
	public double getAddingListCount() {
		return scheme.getAddingListCount();
	}

	@Override
	public double getDeletingListCount() {
		return scheme.getDeletingListCount();
	}

	@Override
	public Action getAction() {
		return scheme.getAction();
	}

	@Override
	public String getLabel() {
		return scheme.getLabel();
	}

	@Override
	public boolean containsContextCondition(Condition c) {
		return scheme.containsContextCondition(c);
	}

//	@Override
//	public boolean containsNegatedContextCondition(Condition c) {
//		return scheme.containsNegatedContextCondition(c);
//	}

	@Override
	public boolean containsAddingItem(Condition c) {
		return scheme.containsAddingItem(c);
	}

	@Override
	public boolean containsDeletingItem(Condition c) {
		return scheme.containsDeletingItem(c);
	}

	@Override
	public double getResultSize() {
		return scheme.getResultSize();
	}

	@Override
	public int getUnsatisfiedContextCount() {
		return scheme.getUnsatisfiedContextCount();
	}

	@Override
	public Condition getContextCondition(Object id) {
		return scheme.getContextCondition(id);
	}
}