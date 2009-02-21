/**
 * 
 */
package edu.memphis.ccrg.globalworkspace;

/**
 * Modules that need to receive Broadcast must implement this interface. It will receive each 
 * Broadcast Content that will be sent by the GlobalWrokspace.
 * 
 * @author Javier Snaider
 * 
 */
public interface BroadcastListener {
	/**
	 * This method should return as possible in order to no delay the rest of the broadcasting.
	 * A good implementation should just store the content in a buffer and return.
	 * @param bc the Content of the Broadcast
	 */
	public void receiveBroadcast(BroadcastContent bc);
}
