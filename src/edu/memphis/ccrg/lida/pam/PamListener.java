package edu.memphis.ccrg.lida.pam;

import edu.memphis.ccrg.lida.shared.NodeStructure;

/**
 * @author Ryan McCall
 * 
 */
public interface PamListener {
	/**
	 * This method should return as possible in order to 
	 * no delay the rest of the broadcasting.
	 * A good implementation should just store the content in a buffer and return.
	 * @param sc the Content of the Broadcast
	 */
	public void receivePamContent(NodeStructure pc);
}