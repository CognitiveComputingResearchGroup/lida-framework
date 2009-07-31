package edu.memphis.ccrg.lida.framework.gui;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 * @author Javier Snaider
 * 
 */
public class LidaGuiControllerImpl implements LidaGuiController {

	private Lida lida;

	/**
	 * @param lida
	 */
	public LidaGuiControllerImpl(Lida lida) {
		super();
		this.lida = lida;
	}

	public void pauseRunningThreads() {
		lida.getTaskManager().pauseSpawnedTasks();
	}

	public void quitAll() {
		lida.getTaskManager().stopRunning();
	}

	public void resetEnvironment() {
		lida.getEnvironment().resetEnvironment();

	}

	public void resumeRunningThreads() {
		lida.getTaskManager().resumeSpawnedTasks();
	}

	public void setSleepTime(int sleepTime) {
		lida.getTaskManager().setTimeScale(sleepTime);

	}

	public void registrerLida(Lida lida) {
		this.lida = lida;

	}

}
