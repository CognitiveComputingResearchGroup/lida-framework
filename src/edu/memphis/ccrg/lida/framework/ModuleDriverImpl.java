package edu.memphis.ccrg.lida.framework;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();


	public ModuleDriverImpl(int ticksPerCycle, LidaTaskManager tm){
		super(ticksPerCycle, tm);
	}

	protected void runThisLidaTask(){
		runThisDriver();
	}
	protected abstract void runThisDriver();
	/**
	 * Add a gui panel
	 */
	public void addFrameworkGuiEventListener(FrameworkGuiEventListener listener) {
		guis.add(listener);
	}
	
	/**
	 * Send a gui event
	 */
	public void sendEvent(FrameworkGuiEvent evt) {
		for (FrameworkGuiEventListener gui : guis)
			gui.receiveGuiEvent(evt);
	}// method	
}// class