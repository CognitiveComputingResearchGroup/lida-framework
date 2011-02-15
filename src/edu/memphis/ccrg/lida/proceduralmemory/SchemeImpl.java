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

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.BehaviorImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl;

public class SchemeImpl extends LearnableImpl implements Scheme {

	private static final double RELIABILITY_THRESHOLD = 0.5;
	private long id;
	private boolean intrinsicBehavior;
	private int numberOfExecutions;
	private int successExecutions;
	private double curiosity;
	private NodeStructure context;
	private NodeStructure addingResult;
	private NodeStructure deletingResult;
	private ConcurrentMap<Long, Argument> arguments;
	private ConcurrentMap<Long, List<NodeStructure>> argumentsCC;
	private ConcurrentMap<Long, List<NodeStructure>> argumentsRC;
	private List<Scheme> stream;

	private long actionId;
	private String label;
	

	//TODO Fix
	public SchemeImpl(String label, long id, long actionId) {
		this.label = label;
		this.id = id;
		this.actionId = actionId;
		context = new NodeStructureImpl();
		addingResult = new NodeStructureImpl();
		deletingResult = new NodeStructureImpl();
		arguments = new ConcurrentHashMap<Long, Argument>();
		argumentsCC = new ConcurrentHashMap<Long, List<NodeStructure>>();
		argumentsRC = new ConcurrentHashMap<Long, List<NodeStructure>>();
		stream = new ArrayList<Scheme>();
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

	@Override
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
	
	@Override
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

	@Override
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
				&& ((((double) successExecutions) / numberOfExecutions) > RELIABILITY_THRESHOLD);
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
	public List<Behavior> getInstantiation() {
		//TODO review
		List<Behavior> instantiation = new ArrayList<Behavior>();
		for(Scheme s: stream){
			Behavior b = new BehaviorImpl(s.getSchemeActionId());
			b.setLabel("scheme: " + s.getLabel() +  " behavior id: " + b.getId());
			b.setActivation(s.getTotalActivation());
			for(Node n: s.getContext().getNodes()){
				b.addContextCondition(n);
			}
			//TODO check
			b.setContextNodeType(s.getContext().getDefaultNodeType());
			for (Node n : s.getAddingResult().getNodes()) {
				b.addToAddingList(n);
			}
			for(Node n: s.getDeletingResult().getNodes()){
				b.addToDeletingList(n);
			}
			b.setGeneratingScheme(this);
			instantiation.add(b);
		}
		return instantiation;
	}

	@Override
	public synchronized void addToStream(Scheme s) {
		stream.add(s);
		this.addingResult = s.getAddingResult();
		this.deletingResult = s.getDeletingResult();
	}

	@Override
	public Collection<Scheme> getStream() {
		return Collections.unmodifiableCollection(stream);
	}
	@Override
	public synchronized void setStream(Collection<Scheme> s) {
		stream = new ArrayList<Scheme>(s);
		this.context = stream.get(0).getContext();
		Scheme last = stream.get(stream.size() - 1);
		this.addingResult = last.getAddingResult();
		this.deletingResult = last.getDeletingResult();
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.activation.LearnableImpl#decay(long)
	 */
	@Override
	public void decay(long ticks){
		super.decay(ticks);
		for(Scheme s: stream)
			s.decay(ticks);
	}

}
