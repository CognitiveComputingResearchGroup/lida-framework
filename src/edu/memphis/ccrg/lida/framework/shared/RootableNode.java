package edu.memphis.ccrg.lida.framework.shared;

/**
 * A {@link Node} which unifies multiple nodes attached to it. 
 * @author Javier Snaider
 * @author Ryan J. McCall
 */
public interface RootableNode extends Node {
	
	/**
	 * Sets Node type
	 * @param t {@link NodeType}
	 */
	public void setNodeType(NodeType t);
	
	/**
	 * Gets Node Type
	 * @return {@link NodeType}
	 */
	public NodeType getNodeType(); 
	
	/**
	 * Returns desirability of this node.
	 * @return a double signifying the degree to which this node is desired by the agent
	 */
	public double getDesirability();
	
	/**
	 * Sets node desirability.
	 * @param d degree to which this node is desired by the agent
	 */
	public void setDesirability(double d);
	
	/**
	 * Gets net desirability
	 * @return the difference between desirability and activation
	 */
	public double getNetDesirability();	

}
