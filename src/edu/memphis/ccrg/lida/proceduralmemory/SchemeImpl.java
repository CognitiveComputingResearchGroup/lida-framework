/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorImpl;
import edu.memphis.ccrg.lida.framework.shared.LearnableActivatibleImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class SchemeImpl extends LearnableActivatibleImpl implements Scheme {

	private static final double RELIABLE_TRESHOLD = 0.5;
	private long id;
	private boolean intrinsicBehavior;
	private int numberOfExecutions;
	private int successExecutions;
	private double curiosity;
	private NodeStructure context;
	private NodeStructure addingResult;
	private NodeStructure deletingResult;
	private ConcurrentMap<Long, Argument> arguments = new ConcurrentHashMap<Long, Argument>();
	private ConcurrentMap<Long, List<NodeStructure>> argumentsCC = new ConcurrentHashMap<Long, List<NodeStructure>>();
	private ConcurrentMap<Long, List<NodeStructure>> argumentsRC = new ConcurrentHashMap<Long, List<NodeStructure>>();

	private long actionId;
	private String label;
	

	public SchemeImpl(String label, long id, long actionId) {
		this.label = label;
		this.id = id;
		this.actionId = actionId;
		context = new NodeStructureImpl();
		addingResult = new NodeStructureImpl();
		deletingResult = new NodeStructureImpl();
	}

	@Override
	public synchronized void addArgument(Argument a) {
		arguments.putIfAbsent(a.getArgumentId(), a);
		argumentsCC.putIfAbsent(a.getArgumentId(),
				new ArrayList<NodeStructure>());
		argumentsRC.putIfAbsent(a.getArgumentId(),
				new ArrayList<NodeStructure>());
	}

//	@Override
//	public synchronized void addContextCondition(long argumentId,
//			NodeStructure ns) {
//		if (ns != null) {
//			if (argumentId == 0) {// condition without argument.
//				contextConditions.add(ns);
//			} else {
//				Argument a = arguments.get(argumentId);
//				if (a != null) {
//					contextConditions.add(ns);
//					List<NodeStructure> conditions = argumentsCC.get(argumentId);
//					conditions.add(ns);
//				}
//			}
//		}
//	}

//	@Override
//	public void addContextCondition(NodeStructure ns) {
//		addContextCondition(0L,ns);
//	}

//	@Override
//	public synchronized void addResultConditions(long argumentId,
//			NodeStructure ns) {
//		if (ns != null) {
//			if (argumentId == 0) {// condition without argument.
//				resultConditions.add(ns);
//			} else {
//				Argument a = arguments.get(argumentId);
//				if (a != null) {
//					resultConditions.add(ns);
//					List<NodeStructure> conditions = argumentsRC.get(argumentId);
//					conditions.add(ns);
//				}
//			}
//		}
//	}

	@Override
	public void setAddingResult(NodeStructure ns) {
		this.addingResult = ns;	
	}

	@Override
	public void decayCuriosity() {
	}

	public boolean equals(Object o) {
		if (!(o instanceof Scheme)) {
			return false;
		}
		return ((Scheme) o).getId() == id;
	}

	@Override
	public Argument getArgument(long argumentId) {
		return arguments.get(argumentId);
	}

	@Override
	public Collection<Argument> getArguments() {
		return Collections.unmodifiableCollection(arguments.values());
	}

	@Override
	public NodeStructure getContext() {
		return context;
	}
	
	public void setContext(NodeStructure ns){
		this.context = ns;
	}

	@Override
	public double getCuriosity() {
		return curiosity;
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public int getNumberOfExecutions() {
		return numberOfExecutions;
	}

	@Override
	public double getReliability() {
		return (numberOfExecutions > 0) ? ((double) successExecutions)
				/ numberOfExecutions : 0.0;
	}

	@Override
	public NodeStructure getAddingResult() {
		return addingResult;
	}

//	@Override
//	public List<NodeStructure> getResultConditions(long argumentId) {
//		List<NodeStructure> conditions = argumentsRC.get(argumentId);
//		if (conditions != null) {
//			return Collections.unmodifiableList(conditions);
//		}
//		return null;
//	}

	@Override
	public long getSchemeActionId() {
		return actionId;
	}

	public int hashCode() {
		return ((int) id % 31);
	}

	@Override
	public void incrementNumberOfExecutions() {
		numberOfExecutions++;
	}

	@Override
	public boolean isIntrinsicBehavior() {
		return intrinsicBehavior;
	}

	@Override
	public boolean isReliable() {
		return (numberOfExecutions > 0)
				&& ((((double) successExecutions) / numberOfExecutions) > RELIABLE_TRESHOLD);
	}

	@Override
	public void setCuriosity(double curiosity) {
		this.curiosity = curiosity;
	}

	@Override
	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void setIntrinsicBehavior(boolean intrinsicBehavior) {
		this.intrinsicBehavior = intrinsicBehavior;
	}

	@Override
	public void setNumberOfExecutions(int numberOfExecutions) {
		this.numberOfExecutions = numberOfExecutions;
	}

	@Override
	public void setSchemeActionId(long actionId) {
		this.actionId = actionId;
	}
	//TODO: FIX
	public boolean isContextCompleted(){
		return true;
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
	public Behavior getBehavior() {
		//TODO review
		
		Behavior b = new BehaviorImpl(this.actionId);
		b.setLabel("scheme: " + this.label +  " behavior id: " + b.getId());
		b.setActivation(getTotalActivation());
		for(Node n: context.getNodes())
			b.addContextCondition(n);
		b.setContextNodeType(context.getDefaultNodeType());
		for (Node n : getAddingResult().getNodes()) 
			b.addToAddingList(n);
		for(Node n: getDeletingResult().getNodes())
			b.addToDeletingList(n);
		b.isAllContextConditionsSatisfied();
		b.setGeneratingScheme(this);
		
		return b;
	}

}
