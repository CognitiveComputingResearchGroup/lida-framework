/**
 * 
 */
package edu.memphis.ccrg.lida.framework.strategies;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.TaskSpawnerImpl;

/**
 * @author Javier Snaider
 *
 */
public class AllModuleDriver extends TaskSpawnerImpl {

	
	public AllModuleDriver(LidaTaskManager tm){
	super(1,tm);
	}

	@Override
	protected void runThisLidaTask() {
	}

	@Override
	public String toString() {
		return "All ModuleType Driver";
	}


}
