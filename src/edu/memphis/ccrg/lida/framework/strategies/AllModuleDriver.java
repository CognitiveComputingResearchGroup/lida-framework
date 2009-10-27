/**
 * 
 */
package edu.memphis.ccrg.lida.framework.strategies;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.ModuleDriverImpl;
import edu.memphis.ccrg.lida.framework.TaskSpawnerImpl;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

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
		return "All Module Driver";
	}


}
