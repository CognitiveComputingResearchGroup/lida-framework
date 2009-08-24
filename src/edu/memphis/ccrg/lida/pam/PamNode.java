package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.Node;

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

	public abstract double getBaselevelActivation();

	public abstract double getTotalActivation();

	public abstract double getMinActivation();

	public abstract double getMaxActivation();

	public abstract double getDefaultMinActivation();

	public abstract double getDefaultMaxActivation();

	public abstract void setMinActivation(double amount);

	public abstract void setMaxActivation(double amount);

	public abstract void setSelectionThreshold(double threshold);

	public abstract void synchronize();

}// interface