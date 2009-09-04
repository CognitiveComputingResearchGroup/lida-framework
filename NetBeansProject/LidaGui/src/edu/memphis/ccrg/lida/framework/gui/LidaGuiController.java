/**
 *
 */
package edu.memphis.ccrg.lida.framework.gui;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 * @author Javier Snaider
 *
 */
public interface LidaGuiController {

	void pauseRunningThreads();

	void resetEnvironment();

	void resumeRunningThreads();

	void quitAll();

	void setSleepTime(int sleepTime);
	
	void registrerLida(Lida lida);
	
	boolean isSystemPaused();
	
}
