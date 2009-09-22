package edu.memphis.ccrg.lida.framework.gui.events;

import edu.memphis.ccrg.lida.framework.Module;

public class TaskCountEvent extends FrameworkGuiEvent{

	public TaskCountEvent(Module m, String count) {
		super(m, count, null);
	}
	public TaskCountEvent(Module m, int count) {
		super(m, count + "", null);
	}

}
