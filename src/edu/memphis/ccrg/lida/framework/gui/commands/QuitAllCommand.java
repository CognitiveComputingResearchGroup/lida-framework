package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;

public class QuitAllCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		LidaTaskManager tm = lida.getTaskManager();
		tm.pauseSpawnedTasks();
		try {
			Thread.sleep(100);
		}catch(InterruptedException e){
			e.printStackTrace();
		}
		tm.stopRunning();
	}

}
