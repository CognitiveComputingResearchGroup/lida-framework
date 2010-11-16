/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Map;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * Default Node Implementation
 * 
 * @author Javier Snaider
 * 
 */
public class NodeImpl extends ActivatibleImpl implements Node {

	private int id;
	private ExtendedId eId;
	private String label = "";
//	private double importance;
	private String factoryName;
	//TODO remove
	private double goalDegree;
	
	protected PamNode refNode;
	
	public NodeImpl(){
		super();
	}

	public NodeImpl(NodeImpl n) {
		super(n.getActivation(), n.getExciteStrategy(), n.getDecayStrategy());
		this.id = n.id;
		this.eId = n.eId;
		this.refNode = n.refNode;
	}
	
	public void init(Map<String, Object> params) {
	}
	
	@Override
	public ExtendedId getExtendedId() {
		return eId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
		eId = new ExtendedId(0, id, 0,0,0);
	}
	public void setExtendedId(ExtendedId eId) {
		this.eId = eId;
	}

	public String getLabel() {
		return label;
	}
	public void setLabel(String label) {
		this.label=label;
	}

//	public double getImportance() {
//		return importance;
//	}
//	public void setImportance(double importance) {
//		this.importance = importance;
//	}

	public PamNode getReferencedNode() {
		return refNode;
	}
	public void setReferencedNode(PamNode n) {
		refNode = n;
	}

	public String getIds() {
		return "" + getId();
	}

	/**
	 * This method compares this object with any kind of Node. returns true if
	 * the id of both are the same.
	 */
	public boolean equals(Object o) {
		if (!(o instanceof Node)) {
			return false;
		}
		return ((Node) o).getId() == id;
	}
	/**
	 *
	 */
	public int hashCode() {
		return (int) (id);
	}
	public String toString(){
		return "Node: " + getLabel() + " ["+getId()+"] ";
	}

	@Override
	public String getFactoryName() {
		return factoryName;
	}

	@Override
	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
	}

	@Override
	public double getGoalDegree() {
		return goalDegree;
	}

	@Override
	public void setGoalDegree(double degree) {
		this.goalDegree = degree;
	}

}//class
