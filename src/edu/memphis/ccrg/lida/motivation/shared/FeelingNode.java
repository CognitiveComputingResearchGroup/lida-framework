package edu.memphis.ccrg.lida.motivation.shared;

import edu.memphis.ccrg.lida.framework.shared.Node;


/**
 * A {@link Node} having a valence. Can be marked a drive feeling or not.
 * @author Ryan J McCall
 */
public interface FeelingNode extends Valenceable, Node {

	/**
	 * Gets the current affective valence.
	 * @return a double value representing the object's affective valence.
	 */
	public double getAffectiveValence();
	
	/**
	 * Returns whether this feeling is a drive, i.e., concerning the agent's internal state.
	 * @return true if the feeling is a drive feeling
	 */
	public boolean isDrive();

	/**
	 * Sets the flag specifying whether this feeling is a drive, i.e., concerning the agent's internal state.
	 * @param d true if the feeling is a drive feeling; false otherwise.
	 */
	public void setDrive(boolean d);
}
