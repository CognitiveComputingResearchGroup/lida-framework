/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * This class implement method of interface argument, which plays the role of node's interface.
 * This class supports methods to access node's methods directly.  
 * @author Javier Snaider
 * 
 */
public class ArgumentImpl implements Argument {

	private int argumentId;
	private Node node;
	protected Map<String, ?> parameters;

	public ArgumentImpl(long argumentId) {
		if (argumentId <= 0) {
			throw new IllegalArgumentException(
					"The argument argumentId must be positive");
		}
	}

	@Override
	public long getArgumentId() {
		return argumentId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.proceduralmemory.Argument#bindNode(edu.memphis.
	 * ccrg.lida.framework.shared.Node)
	 */
	@Override
	public void bindNode(Node node) {
		this.node = node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Argument#getBindedNode()
	 */
	@Override
	public Node getBoundNode() {
		return node;
	}


	/**
	 * Returns the id of the Attribute negated (* -1)
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Argument#getId()
	 */
	@Override
	public int getId() {
		return -argumentId;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.proceduralmemory.Argument#isBinded()
	 */
	@Override
	public boolean isBound() {
		return (node != null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Node#getReferencedNode()
	 */
	@Override
	public PamNode getGroundingPamNode() {
		if (node != null) {
			return node.getGroundingPamNode();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Node#setId(long)
	 */
	@Override
	public void setId(int id) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Node#setLabel(java.lang.String)
	 */
	@Override
	public void setLabel(String label) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Node#setReferencedNode(edu.memphis
	 * .ccrg.lida.pam.PamNode)
	 */
	@Override
	public void setGroundingPamNode(PamNode n) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Linkable#getLabel()
	 */
	@Override
	public String getLabel() {
		if (node != null) {
			return node.getLabel();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Activatible#decay(long)
	 */
	@Override
	public void decay(long ticks) {
		if (node != null) {
			node.decay(ticks);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Activatible#excite(double)
	 */
	@Override
	public void excite(double amount) {
		if (node != null) {
			node.excite(amount);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.shared.Activatible#getActivation()
	 */
	@Override
	public double getActivation() {
		if (node != null) {
			return node.getActivation();
		}
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#getDecayStrategy()
	 */
	@Override
	public DecayStrategy getDecayStrategy() {
		if (node != null) {
			return node.getDecayStrategy();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#getExciteStrategy()
	 */
	@Override
	public ExciteStrategy getExciteStrategy() {
		if (node != null) {
			return node.getExciteStrategy();
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#getTotalActivation()
	 */
	@Override
	public double getTotalActivation() {
		if (node != null) {
			return node.getTotalActivation();
		}
		return 0.0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#setActivation(double)
	 */
	@Override
	public void setActivation(double activation) {
		if (node != null) {
			node.setActivation(activation);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#setDecayStrategy(edu
	 * .memphis.ccrg.lida.framework.strategies.DecayStrategy)
	 */
	@Override
	public void setDecayStrategy(DecayStrategy strategy) {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.shared.Activatible#setExciteStrategy(
	 * edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy)
	 */
	@Override
	public void setExciteStrategy(ExciteStrategy strategy) {
	}


	/** 
	 * If the Object o is an instance of Argument, 
	 * the argumentId is used to check the equals condition.
	 * If the Object o is an instance of Node (but not the Argument), then the 
	 * id of the binded Node is used to check the equals condition.
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o != null) {
			if (o instanceof Argument) {
				Argument a = (Argument) o;
				if (a.getArgumentId() == this.argumentId) {
					return true;
				}
			} else if (o instanceof Node && node != null) {
				Node n=(Node)o;
				if (n.getId()==node.getId()){
					return true;
				}
			}
		}
		return false;
	}

	public static String getFactoryNodeType() {
		return ArgumentImpl.class.getSimpleName();
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public double getDesirability() {
		return node.getDesirability();
	}

	@Override
	public void setDesirability(double degree) {
		node.setDesirability(degree);
		
	}

	@Override
	public ExtendedId getExtendedId() {
		return node.getExtendedId();
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

	@Override
	public double getActivatibleRemovalThreshold() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isRemovable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setActivatibleRemovalThreshold(double threshold) {
		// TODO Auto-generated method stub
		
	}

//	@Override
//	public Node copy() {
//		// TODO Auto-generated method stub
//		return null;
//	}

	@Override
	public void updateNodeValues(Node n) {
		// TODO Auto-generated method stub
		
	}

}
