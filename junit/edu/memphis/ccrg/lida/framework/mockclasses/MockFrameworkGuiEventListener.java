package edu.memphis.ccrg.lida.framework.mockclasses;

import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class MockFrameworkGuiEventListener implements FrameworkGuiEventListener {

	public FrameworkGuiEvent event;
	public long tick;
	@Override
	public void receiveFrameworkGuiEvent(FrameworkGuiEvent event) {
		this.event = event;
		tick=TaskManager.getCurrentTick();
	}

}
