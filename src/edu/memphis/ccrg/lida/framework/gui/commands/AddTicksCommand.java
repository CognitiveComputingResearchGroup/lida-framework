package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;

public class AddTicksCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		int ticks= (Integer)getParameter("ticks");
		lida.getTaskManager().addTicks(ticks);

	}

}
