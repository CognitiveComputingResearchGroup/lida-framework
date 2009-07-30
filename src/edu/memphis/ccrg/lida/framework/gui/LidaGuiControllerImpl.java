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
		lida.getTaskManager().pauseSpawnedThreads();
	}

	public void quitAll() {
		lida.getTaskManager().stopRunning();
	}

	public void resetEnvironment() {
		lida.getEnvironment().resetEnvironment();

	}

	public void resumeRunningThreads() {
		lida.getTaskManager().resumeSpawnedThreads();
	}

	public void setSleepTime(int sleepTime) {
		lida.getTaskManager().setSleepTime(sleepTime);

	}

	public void registrerLida(Lida lida) {
		this.lida = lida;

	}

}
