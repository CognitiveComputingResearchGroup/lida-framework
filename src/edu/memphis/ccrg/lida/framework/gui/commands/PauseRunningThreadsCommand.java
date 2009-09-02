/**
 * 
 */
package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 * @author Javier Snaider
 *
 */
public class PauseRunningThreadsCommand extends GenericCommandImpl {

	public void execute(Lida lida) {
		lida.getTaskManager().pauseSpawnedTasks();
	}
}
