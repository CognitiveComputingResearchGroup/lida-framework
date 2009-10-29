package edu.memphis.ccrg.lida.framework.gui.events;

import edu.memphis.ccrg.lida.framework.ModuleType;

public class TaskCountEvent extends FrameworkGuiEvent{

	public TaskCountEvent(ModuleType m, String count) {
		super(m, count, null);
	}
	public TaskCountEvent(ModuleType m, int count) {
		super(m, count + "", null);
	}

}
