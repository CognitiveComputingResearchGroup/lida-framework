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
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryImpl.ConditionType;

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

	/*
	 * The scheme from which this behavior was instantiated. 
	 */
	private Scheme scheme;
	
	/**
	 * Construct a new behavior with default parameters
	 */
	public BehaviorImpl(){
		super();
	}

	//Behavior methods
	@Override
	public void setId(int id) {
		behaviorId = id;
	}

	@Override
	public long getId() {
		return behaviorId;
	}

	@Override
	public Scheme getScheme() {
		return scheme;
	}

	@Override
	public void setScheme(Scheme s) {
		scheme  = s;
	}
	
	//Object method
	@Override
	public String toString(){
		return getLabel() + "-" + getId();
	}
	
	
	@Override
	public void setAction(Action a) {
		throw new UnsupportedOperationException("Cannot modify a Behavior, only a Scheme");
	}	
	
//	@Override
//	public boolean isContextConditionSatisfied(Condition c) {
//		return scheme.isContextConditionSatisfied(c);
//	}
//
//	@Override
//	public boolean isAllContextConditionsSatisfied() {
//		return scheme.isAllContextConditionsSatisfied();
//	}
//
//	@Override
//	public int getUnsatisfiedContextCount() {
//		return scheme.getUnsatisfiedContextCount();
//	}

	@Override
	public Collection<Condition> getContextConditions() {
		return scheme.getContextConditions();
	}

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
	public Condition getContextCondition(Object id) {
		return scheme.getContextCondition(id);
	}

	@Override
	public boolean addCondition(Condition c, ConditionType type) {
		throw new UnsupportedOperationException("Method not supported");
	}

//	@Override
//	public boolean containsNegatedContextCondition(Condition c) {
//		return scheme.containsNegatedContextCondition(c);
//	}

//	@Override
//	public boolean addContextCondition(Condition c, boolean negated) {
//		return scheme.addContextCondition(c, negated);
//	}

//	@Override
//	public Collection<Condition> getNegatedContextConditions() {
//		return scheme.getNegatedContextConditions();
//	}
}