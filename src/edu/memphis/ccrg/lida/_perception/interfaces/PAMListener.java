package edu.memphis.ccrg.lida._perception.interfaces;

import edu.memphis.ccrg.lida._perception.PAMContentImpl;

/**
 * @author Ryan McCall
 * 
 */
public interface PAMListener {
	/**
	 * This method should return as possible in order to 
	 * no delay the rest of the broadcasting.
	 * A good implementation should just store the content in a buffer and return.
	 * @param sc the Content of the Broadcast
	 */
	public void receivePAMContent(PAMContentImpl pc);
}