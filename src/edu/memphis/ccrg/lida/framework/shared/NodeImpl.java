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
	private String factoryName;
	
	//TODO change to set of attribute nodes
	private double goalDegree;
	
	protected PamNode groundingPamNode;
	
	public NodeImpl(){
		super();
	}

	public NodeImpl(NodeImpl n) {
		super(n.getActivation(), n.getExciteStrategy(), n.getDecayStrategy());
		this.id = n.id;
		this.eId = n.eId;
		this.groundingPamNode = n.groundingPamNode;
	}
	
	@Override
	public void initLinkable(Map<String, Object> params) {
	}
	
	@Override
	public ExtendedId getExtendedId() {
		return eId;
	}
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
		eId = new ExtendedId(0, id, 0,0,0);
	}
	public void setExtendedId(ExtendedId eId) {
		this.eId = eId;
	}

	@Override
	public String getLabel() {
		return label;
	}
	@Override
	public void setLabel(String label) {
		this.label=label;
	}

	@Override
	public PamNode getGroundingPamNode() {
		return groundingPamNode;
	}
	@Override
	public void setGroundingPamNode(PamNode n) {
		groundingPamNode = n;
	}

	public String getIds() {
		return getId() + "";
	}

	/**
	 * This method compares this object with any kind of Node. returns true if
	 * the id of both are the same.
	 */
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Node)) {
			return false;
		}
		return ((Node) o).getId() == id;
	}
	/**
	 *
	 */
	@Override
	public int hashCode() {
		return (id);
	}
	@Override
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
