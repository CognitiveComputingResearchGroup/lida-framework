/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;

/**
 * Default {@link Node} implementation.
 *
 * @author Javier Snaider
 * @see ElementFactory
 */
public class NodeImpl extends ActivatibleImpl implements Node {

	private static final Logger logger = Logger.getLogger(NodeImpl.class.getCanonicalName());
	private static final double DEFAULT_WEIGHT = 1.0;
	
	private int id;
	private ExtendedId extendedId;
	private String label ="Node";
	private String name;
	private String factoryName;
	
	/**
	 * {@link PamNode} in {@link PerceptualAssociativeMemory} which grounds this {@link Node}
	 */
	protected PamNode groundingPamNode;
	
	/**
	 * @param n the factoryName to set
	 */
	@Override
	public void setFactoryType(String n) {
		factoryName = n;
	}

	/**
	 * @return the factoryName
	 */
	@Override
	public String getFactoryType() {
		return factoryName;
	}

	/**
	 * Default constructor
	 */
	public NodeImpl(){
		super();
		name = label + "["+id+"]";
	}

	/**
	 * Copy constructor
	 * @param n source {@link NodeImpl}
	 */
	public NodeImpl(NodeImpl n) {
		super(n.getActivation(), n.getActivatibleRemovalThreshold(), n.getExciteStrategy(), n.getDecayStrategy());
		this.id = n.id;
		this.extendedId = n.extendedId;
		this.groundingPamNode = n.groundingPamNode;
		this.label=n.label;
		name = label + "["+id+"]";
	}
	
	@Override
	public ExtendedId getExtendedId() {
		return extendedId;
	}
	
	@Override
	public int getId() {
		return id;
	}
	
	@Override
	public void setId(int id) {
		this.id = id;
		extendedId = new ExtendedId(id);
		name = label + "["+id+"]";
	}
	
	/**
	 * Convenience method to set Node's {@link ExtendedId}.  Also sets node's id.
	 * @param eid {@link ExtendedId}
	 */
	public void setExtendedId(ExtendedId eid) {
		if(eid == null){
			logger.log(Level.WARNING, "Supplied ExtendedId was null. ExtendedId not set.", TaskManager.getCurrentTick());
			return;
		}
		
		if(eid.isNodeId()){
			this.extendedId = eid;
			this.id = eid.getSourceNodeId();
			name = label + "["+id+"]";
		}else{
			logger.log(Level.WARNING, "Cannot give a node a Link's extended id", TaskManager.getCurrentTick());
		}
	}

	@Override
	public String getLabel() {
		return label;
	}
	@Override
	public void setLabel(String label) {
		this.label=label;
		name = label + "["+id+"]";
	}

	@Override
	public PamNode getGroundingPamNode() {
		return groundingPamNode;
	}
	@Override
	public void setGroundingPamNode(PamNode n) {
		groundingPamNode = n;
	}

	/**
	 * This method compares this object with any kind of Node. returns true if
	 * the id of both are the same.
	 * @param o Object
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Node) {
			return ((Node) o).getId() == id;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (id);
	}
	@Override
	public String toString(){
		return name;
	}
	
	/**
	 * Updates the values of this NodeImpl based on the passed in Node.  
	 * Node must be a NodeImpl.
	 * Does not copy superclass attributes, e.g. ActivatibleImpl, only those of this class.
	 */
	@Override
	public void updateNodeValues(Node n) {
//		if(n instanceof NodeImpl){
//			NodeImpl other = (NodeImpl) n;
//			id = other.id;
//			extendedId = other.extendedId;
//			label = other.label;
//			groundingPamNode = other.groundingPamNode;
//		} 
	}

	@Override
	public double getWeight() {
		return DEFAULT_WEIGHT;
	}

	@Override
	public void setWeight(double w) {
	}

	@Override
	public ExtendedId getConditionId() {
		return extendedId;
	}

}
