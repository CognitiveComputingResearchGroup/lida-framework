/**
 * 
 */
package edu.memphis.ccrg.lida.framework.tasks;

import edu.memphis.ccrg.lida.framework.LidaModule;

/**
 * @author Javier Snaider
 *
 */
public interface Codelet extends LidaTask {
	public abstract void setAssociatedModule(LidaModule module);
	
}
