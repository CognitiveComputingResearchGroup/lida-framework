/**
 * 
 */
package edu.memphis.ccrg.lida.framework.tasks;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * 
 * @author Javier Snaider
 */
public interface Codelet extends LidaTask {
	
	/**
	 * 
	 * @param module
	 */
	public abstract void setAssociatedModule(LidaModule module);
	
}
