package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;

public class SetTimeScaleCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		Integer timeScale = (Integer) getParameter("timeScale");
		if (timeScale != null) {
			lida.getTaskManager().setTimeScale(timeScale);
		}
	}

}
