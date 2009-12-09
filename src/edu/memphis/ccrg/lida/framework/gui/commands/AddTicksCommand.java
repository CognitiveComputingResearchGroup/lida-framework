package edu.memphis.ccrg.lida.framework.gui.commands;

import edu.memphis.ccrg.lida.framework.Lida;
import edu.memphis.ccrg.lida.framework.LidaTaskManager;

public class AddTicksCommand extends GenericCommandImpl {

	@Override
	public void execute(Lida lida) {
		int ticks= (Integer)getParameter("ticks");
		long actualTicks=LidaTaskManager.getActualTick();
		lida.getTaskManager().setLapTicks(ticks+actualTicks);
		
	}

}
