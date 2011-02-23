/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.BehaviorImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;

public class SchemeImpl extends LearnableImpl implements Scheme {

	private static final double RELIABILITY_THRESHOLD = 0.5;
	private long id;
	private boolean innate;
	private int numberOfExecutions;
	private int successfulExecutions;
	private NodeStructure context;
	private NodeStructure addingResult;
	private NodeStructure deletingResult;

	private LidaAction action;
	private String label;

	public SchemeImpl(String label, long id, LidaAction a) {
		this.label = label;
		this.id = id;
		this.action = a;
		context = new NodeStructureImpl();
		addingResult = new NodeStructureImpl();
		deletingResult = new NodeStructureImpl();
	}

	@Override
	public void setAddingResult(NodeStructure ns) {
		this.addingResult = ns;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Scheme)) {
			return false;
		}
		return ((Scheme) o).getId() == id;
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

	public int getExecutions() {
		return numberOfExecutions;
	}

	@Override
	public double getReliability() {
		return (numberOfExecutions > 0) ? ((double) successfulExecutions)
				/ numberOfExecutions : 0.0;
	}

	@Override
	public NodeStructure getAddingResult() {
		return addingResult;
	}

	@Override
	public LidaAction getAction() {
		return action;
	}

	@Override
	public int hashCode() {
		return ((int) id % 31);
	}

	@Override
	public void actionWasExecuted() {
		numberOfExecutions++;
	}

	@Override
	public boolean isInnate() {
		return innate;
	}

	@Override
	public boolean isReliable() {
		return (numberOfExecutions > 0)
				&& ((((double) successfulExecutions) / numberOfExecutions) > RELIABILITY_THRESHOLD);
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void setInnate(boolean innate) {
		this.innate = innate;
	}

	@Override
	public void setAction(LidaAction action) {
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
	public Behavior getInstantiation() {
		Behavior b = new BehaviorImpl(getAction());
		b.setLabel("scheme: " + getLabel() + " behavior id: " + b.getId());
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
}
