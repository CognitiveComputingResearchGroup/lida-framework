package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.ModuleName;

public class ResetEnvironmentCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		lida.getSubmodule(ModuleName.Environment);
	}

}
