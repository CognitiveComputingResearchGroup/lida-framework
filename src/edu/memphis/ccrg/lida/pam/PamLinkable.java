package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.framework.shared.Activatible;
import edu.memphis.ccrg.lida.framework.shared.Linkable;

public interface PamLinkable extends Linkable, Activatible{
	
	/**
	 * Determines if this linkable is relevant. A linkable is relevant if its total
	 * activation is greater or equal to the selection threshold.
	 * 
	 * @return <code>true</code> if this linkable is relevant
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
	 * Set the threshold
	 * @param threshold
	 */
	public abstract void setSelectionThreshold(double threshold);

	/**
	 * Returns final min activation variable
	 * @return
	 */
	public abstract double getMinActivation();

	/**
	 * Returns final max activation variable
	 * @return
	 */
	public abstract double getMaxActivation();

}
