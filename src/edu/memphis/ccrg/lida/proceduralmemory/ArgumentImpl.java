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
import edu.memphis.ccrg.lida.framework.shared.NodeType;
import edu.memphis.ccrg.lida.framework.shared.RootableNode;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * This class implement method of interface argument, which plays the role of node's interface.
 * This class supports methods to access node's methods directly.  
 * @author Javier Snaider
 */
public class ArgumentImpl implements Argument {

	private int argumentId;
	private RootableNode node;

	/**
	 * Default constructor
	 * @param argumentId id
	 */
	public ArgumentImpl(long argumentId) {
		if (argumentId <= 0) {
			throw new IllegalArgumentException(
					"The argument's argumentId must be positive");
		}
	}

	@Override
	public long getArgumentId() {
		return argumentId;
	}

	@Override
	public void bindNode(RootableNode node) {
		this.node = node;
	}

	@Override
	public RootableNode getBoundNode() {
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

	@Override
	public boolean hasBoundNode() {
		return (node != null);
	}

	@Override
	public PamNode getGroundingPamNode() {
		if (node != null) {
			return node.getGroundingPamNode();
		}
		return null;
	}

	@Override
	public void setId(int id) {
	}

	@Override
	public void setLabel(String label) {
	}

	@Override
	public void setGroundingPamNode(PamNode n) {
	}

	@Override
	public String getLabel() {
		if (node != null) {
			return node.getLabel();
		}
		return null;
	}

	@Override
	public void decay(long ticks) {
		if (node != null) {
			node.decay(ticks);
		}
	}

	@Override
	public void excite(double amount) {
		if (node != null) {
			node.excite(amount);
		}
	}

	@Override
	public double getActivation() {
		if (node != null) {
			return node.getActivation();
		}
		return 0.0;
	}

	@Override
	public DecayStrategy getDecayStrategy() {
		if (node != null) {
			return node.getDecayStrategy();
		}
		return null;
	}

	@Override
	public ExciteStrategy getExciteStrategy() {
		if (node != null) {
			return node.getExciteStrategy();
		}
		return null;
	}

	@Override
	public double getTotalActivation() {
		if (node != null) {
			return node.getTotalActivation();
		}
		return 0.0;
	}

	@Override
	public void setActivation(double activation) {
		if (node != null) {
			node.setActivation(activation);
		}

	}

	@Override
	public void setDecayStrategy(DecayStrategy strategy) {
	}

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
	
	@Override
	public void init(Map<String, ?> params) {
	}
	
	@Override
	public void init() {
	}
	
	@Override
	public <T> T getParam(String name, T defaultValue) {
		return null;
	}
	
	@Override
	public boolean containsParameter(String l){
		return false;
	}

	@Override
	public double getActivatibleRemovalThreshold() {
		return 0;
	}

	@Override
	public boolean isRemovable() {
		return false;
	}

	@Override
	public void setActivatibleRemovalThreshold(double threshold) {
	}
	
	@Override
	public void updateNodeValues(Node n) {
	}

	@Override
	public Map<String, ?> getParameters() {
		return null;
	}

	@Override
	public Object getConditionId() {
		return null;
	}

	@Override
	public double getNetDesirability() {
		return 0;
	}

	@Override
	public double getWeight() {
		return 0;
	}

	@Override
	public void setWeight(double w) {
	}

	@Override
	public NodeType getNodeType() {
		return node.getNodeType();
	}

	@Override
	public void setNodeType(NodeType t) {
	}

	@Override
	public String getFactoryType() {
		// TODO implement
		return null;
	}

	@Override
	public void setFactoryType(String n) {
		// TODO implement
		
	}
}