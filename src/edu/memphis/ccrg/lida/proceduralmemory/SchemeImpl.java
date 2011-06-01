/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 *  which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.actionselection.AgentAction;
import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.actionselection.BehaviorImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;

/**
 * Default implementation of {@link Scheme}
 * @author Ryan J. McCall, Javier Snaider
 *
 */
public class SchemeImpl extends LearnableImpl implements Scheme {

	private static final double RELIABILITY_THRESHOLD = 0.5;
	
	private boolean innate;
	private int numberOfExecutions;
	private int successfulExecutions;
	
	private NodeStructure context;
	private NodeStructure addingResult;
	private NodeStructure deletingResult;

	private AgentAction action;
	private String label;
	private long id;
	private static long idGenerator = 0;
	
	/**
	 * For testing only
	 * @param id Scheme's id
	 */
	SchemeImpl(long id){
		this();
		this.id = id;
	}
	
	/**
	 * Default constructor
	 */
	public SchemeImpl(){
		this.id = idGenerator++;
		context = new NodeStructureImpl();
		addingResult = new NodeStructureImpl();
		deletingResult = new NodeStructureImpl();
	}

	/**
	 * constructor
	 * @param label Scheme's name
	 * @param a scheme's {@link AgentAction}
	 */
	public SchemeImpl(String label, AgentAction a) {
		this();
		this.label = label;
		this.action = a;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Scheme#actionExecuted()
	 */
	@Override
	public void actionExecuted() {
		numberOfExecutions++;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Scheme#actionSuccessful()
	 */
	@Override
	public void actionSuccessful() {
		successfulExecutions++;		
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Scheme#getReliability()
	 */
	@Override
	public double getReliability() {
		return (numberOfExecutions > 0) ? ((double) successfulExecutions)
				/ numberOfExecutions : 0.0;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Scheme#isReliable()
	 */
	@Override
	public boolean isReliable() {
		return (numberOfExecutions > 0)
				&& ((((double) successfulExecutions) / numberOfExecutions) > RELIABILITY_THRESHOLD);
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Scheme#getInstantiation()
	 */
	@Override
	public Behavior getInstantiation() {
		Behavior b = new BehaviorImpl(getAction());
		b.setLabel(getLabel());
		b.setActivation(getTotalActivation());
		for (Node n : getContext().getNodes()) {
			b.addContextCondition(n);
		}
		for (Node n : getAddingResult().getNodes()) {
			b.addToAddingList(n);
		}
		for (Node n : getDeletingResult().getNodes()) {
			b.addToDeletingList(n);
		}
		b.setGeneratingScheme(this);
		return b;
	}
	
	@Override
	public String toString(){
		return getLabel() + " " + getId(); 
	}

	/*
	 * @param o
	 * @return
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Scheme)) {
			return false;
		}
		return ((Scheme) o).getId() == id;
	}
	@Override
	public int hashCode() {
		return (int) id % Integer.MAX_VALUE;
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
	public void setAction(AgentAction action) {
		this.action = action;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(String label) {
		this.label = label;
	}

	@Override
	public NodeStructure getDeletingResult() {
		return deletingResult;
	}

	@Override
	public void setDeletingResult(NodeStructure ns) {
		this.deletingResult = ns;
	}

	@Override
	public void setAddingResult(NodeStructure ns) {
		this.addingResult = ns;
	}

	@Override
	public NodeStructure getContext() {
		return context;
	}

	@Override
	public void setContext(NodeStructure ns) {
		this.context = ns;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public int getExecutions() {
		return numberOfExecutions;
	}

	@Override
	public NodeStructure getAddingResult() {
		return addingResult;
	}

	@Override
	public AgentAction getAction() {
		return action;
	}
	
}
