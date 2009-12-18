package edu.memphis.ccrg.lida.framework.gui.events;

import edu.memphis.ccrg.lida.framework.ModuleName;

public class TaskCountEvent extends FrameworkGuiEvent{

	public TaskCountEvent(ModuleName m, String count) {
		super(m, count, null);
	}
	public TaskCountEvent(ModuleName m, int count) {
		super(m, count + "", null);
	}

}
