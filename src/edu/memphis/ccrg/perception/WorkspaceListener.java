package edu.memphis.ccrg.perception;

/**
 * @author Ryan McCall
 * 
 */
public interface WorkspaceListener {
	/**
	 * This method should return as possible in order to 
	 * no delay the rest of the broadcasting.
	 * A good implementation should just store the content in a buffer and return.
	 * @param sc the Content of the Broadcast
	 */
	public void receiveWMContent(WMtoPAMContent w2pc);
}
