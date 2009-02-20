package edu.memphis.ccrg.sensoryMemory;

/**
 * @author Ryan McCall
 * 
 */
public interface SensoryListener {
	/**
	 * This method should return as possible in order to 
	 * no delay the rest of the broadcasting.
	 * A good implementation should just store the content in a buffer and return.
	 * @param sc the Content of the Broadcast
	 */
	public void receiveSense(SensoryContent sc);
}