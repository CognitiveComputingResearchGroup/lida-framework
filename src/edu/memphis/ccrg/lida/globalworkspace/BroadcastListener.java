/**
 * 
 */
package edu.memphis.ccrg.lida.globalworkspace;

import edu.memphis.ccrg.lida.framework.ModuleListener;


/**
 * Modules that need to receive Broadcast must implement this interface. It will receive each 
 * Broadcast Content that will be sent by the GlobalWrokspace.
 * 
 * @author Javier Snaider
 * 
 */
public interface BroadcastListener extends ModuleListener{
	/**
	 * This method should return as possible in order to no delay the rest of the broadcasting.
	 * A good implementation should just store the content in a buffer and return.
	 * @param bc the Content of the Broadcast
	 */
	public abstract void receiveBroadcast(BroadcastContent bc);
	
	public abstract void learn();
}
