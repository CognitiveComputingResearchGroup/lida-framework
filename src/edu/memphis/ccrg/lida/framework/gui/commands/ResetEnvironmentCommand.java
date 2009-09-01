package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;

public class ResetEnvironmentCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		lida.getEnvironment().resetEnvironment();
	}

}
