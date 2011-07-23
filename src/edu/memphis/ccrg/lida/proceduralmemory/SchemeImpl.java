/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionImpl;
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

	private static final double DEFAULT_RELIABILITY_THRESHOLD = 0.5;
	private static long idGenerator = 0;
	
	private boolean innate;
	private int numberOfExecutions;
	private int successfulExecutions;
	private double reliabilityThreshold = DEFAULT_RELIABILITY_THRESHOLD; 
	
	private NodeStructure context;
	private NodeStructure addingResult;
	private NodeStructure deletingResult;

	private Action action;
	private String label;
	private long id;
	
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
		context = new NodeStructureImpl();
		addingResult = new NodeStructureImpl();
		deletingResult = new NodeStructureImpl();
		action = new ActionImpl();
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
		Behavior b = new BehaviorImpl(getAction());
		b.setLabel(getLabel());
		b.setActivation(getTotalActivation());
		for (Node n: getContext().getNodes()) {
			b.addContextCondition(n);
		}
		for (Node n: getAddingResult().getNodes()) {
			b.addToAddingList(n);
		}
		for (Node n: getDeletingResult().getNodes()) {
			b.addToDeletingList(n);
		}
		b.setGeneratingScheme(this);
		return b;
	}
	
	@Override
	public String toString(){
		return getLabel() + " " + getId(); 
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
	public void setAction(Action action) {
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
	public Action getAction() {
		return action;
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
