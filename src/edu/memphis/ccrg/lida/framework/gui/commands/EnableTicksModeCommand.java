package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;

public class EnableTicksModeCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		Boolean b= (Boolean)getParameter("enable");
		LidaTaskManager.setInTicksMode(b);
	}

}
