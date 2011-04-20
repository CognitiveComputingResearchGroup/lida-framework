/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.activation.ActivatibleImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * Default Node Implementation
 *  
 * WARNING:	Renaming this class requires renaming values in
 * 	  configs/factoriesData.xml
 *
 * @author Javier Snaider
 */
public class NodeImpl extends ActivatibleImpl implements Node {

	private static final Logger logger = Logger.getLogger(NodeImpl.class.getCanonicalName());
	
	private int id;
	private ExtendedId extendedId;
	private String label;
	private double desirability;
	protected PamNode groundingPamNode;
	protected Map<String, ?> parameters;
	
	public NodeImpl(){
		super();
	}

	public NodeImpl(NodeImpl n) {
		super(n.getActivation(), n.getActivatibleRemovalThreshold(), n.getExciteStrategy(), n.getDecayStrategy());
		this.id = n.id;
		this.extendedId = n.extendedId;
		this.groundingPamNode = n.groundingPamNode;
		this.label=n.label;
		this.desirability=n.desirability;
		this.parameters = n.parameters;
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
	}
	
	public void setExtendedId(ExtendedId eid) {
		if(eid.isNodeId()){
			this.extendedId = eid;
			this.id = eid.getSourceNodeId();
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
		return getLabel() + " ["+getId()+"] ";
	}

	@Override
	public double getDesirability() {
		return desirability;
	}

	@Override
	public void setDesirability(double d) {
		if(d <= 0.0){
			desirability = 0.0;
		}else if (d >= 1.0){
			desirability = 1.0;
		}else{
			desirability = d;
		}
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.FrameworkModule#init(java.util.Properties)
	 */
	@Override
	public void init(Map<String, ?> params) {
		this.parameters = params;
		init();
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.FrameworkModule#init()
	 */
	@Override
	public void init() {
	}
	
	@Override
	public Object getParam(String name, Object defaultValue) {
		Object value = null;
		if (parameters != null) {
			value = parameters.get(name);
		}
		if (value == null) {
			value = defaultValue;
		}
		return value;
	}
	
	/**
	 * Updates the values of this NodeImpl based on the passed in Node.  
	 * Node must be a NodeImpl.
	 * Does not copy superclass attributes, e.g. ActivatibleImpl, only those of this class.
	 */
	@Override
	public void updateNodeValues(Node n) {
		if(n instanceof NodeImpl){
			NodeImpl other = (NodeImpl) n;
			id = other.id;
			extendedId = other.extendedId;
			label = other.label;
			desirability = other.desirability;
			groundingPamNode = other.groundingPamNode;
			parameters = other.parameters;
		} 
	}

}
