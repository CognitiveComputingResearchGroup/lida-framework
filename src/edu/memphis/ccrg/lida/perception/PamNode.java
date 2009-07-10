package edu.memphis.ccrg.lida.perception;

import java.util.Map;

import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

public interface PamNode extends Node {

	/**
	 * Determines if this node is relevant. A node is relevant if its total
	 * activation is greater or equal to the selection threshold.
	 * 
	 * @return <code>true</code> if this node is relevant
	 * @see #selectionThreshold
	 */
	public abstract boolean isRelevant();

	/**
	 * Standard getter for importance.
	 * 
	 * @return a double value for importance between 0 and 1.
	 */
	public abstract double getImportance();

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

	void setSelectionThreshold(double threshold);

	public abstract void synchronize();

}// interface