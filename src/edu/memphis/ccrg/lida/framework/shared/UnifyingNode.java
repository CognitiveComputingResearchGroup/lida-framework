package edu.memphis.ccrg.lida.framework.shared;

public interface UnifyingNode extends Node{
	
	public void setNodeType(NodeType t);
	
	/**
	 * Gets Node Type
	 * @return {@link NodeType}
	 */
	public NodeType getNodeType(); 
	
	/**
	 * Returns desirability of this node.
	 * @return a double signifying the degree to which this node is a goal of the agent
	 */
	public double getDesirability();
	
	/**
	 * Sets node desirability.
	 * @param degree  degree to which this node is a goal of the agent
	 */
	public void setDesirability(double degree);
	
	/**
	 * Gets net desirability
	 * @return the difference between desirability and activation
	 */
	public double getNetDesirability();	

}
