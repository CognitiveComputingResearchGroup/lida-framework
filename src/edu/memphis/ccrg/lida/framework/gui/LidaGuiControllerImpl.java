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
		lida.getTimer().pauseSpawnedThreads();
	}

	public void quitAll() {
		lida.getTimer().stopSpawnedThreads();
	}

	public void resetEnvironment() {
		lida.getEnvironment().resetEnvironment();

	}

	public void resumeRunningThreads() {
		lida.getTimer().resumeSpawnedThreads();
	}

	public void setSleepTime(int sleepTime) {
		lida.getTimer().setSleepTime(sleepTime);

	}

	public void registrerLida(Lida lida) {
		this.lida = lida;

	}

}
