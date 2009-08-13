package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;

public class ResetEnvironmetCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		lida.getEnvironment().resetEnvironment();
	}

}
