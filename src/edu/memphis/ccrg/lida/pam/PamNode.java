package edu.memphis.ccrg.lida.pam;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.shared.Node;

/**
 * A PamNode extends nodes.  The added functionalities are mainly due to the fact that
 * PamNodes are involved in activation passing where Nodes are not. 
 * @author Ryan J McCall, Javier Snaider
 *
 */
public interface PamNode extends Node{

	/**
	 * Determines if this node is relevant. A node is relevant if its total
	 * activation is greater or equal to the selection threshold.
	 * 
	 * @return <code>true</code> if this node is relevant
	 * @see #selectionThreshold
	 */
	public abstract boolean isOverThreshold();

	/**
	 * returns selection threshold
	 * 
	 * @return Selection threshold
	 */
	public abstract double getSelectionThreshold();

	/**
	 * Returns base level activation. 
	 * @return
	 */
	public abstract double getBaselevelActivation();

	/**
	 * Returns sum of base and current activation.
	 * @return
	 */
	public abstract double getTotalActivation();

	//TODO: Remove
	public abstract double getMinActivation();

	//TODO: Remove
	public abstract double getMaxActivation();

	//TODO: Remove
	public abstract double getDefaultMinActivation();

	//TODO: Remove
	public abstract double getDefaultMaxActivation();

	//TODO: Remove
	public abstract void setMinActivation(double amount);

	//TODO: Remove
	public abstract void setMaxActivation(double amount);

	public abstract void setSelectionThreshold(double threshold);
     
	public abstract void synchronize();

}// interface