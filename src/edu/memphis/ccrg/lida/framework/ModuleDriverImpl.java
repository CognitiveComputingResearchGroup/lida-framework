package edu.memphis.ccrg.lida.framework;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEvent;
import edu.memphis.ccrg.lida.framework.gui.events.FrameworkGuiEventListener;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl;

public abstract class ModuleDriverImpl extends TaskSpawnerImpl implements ModuleDriver {
	protected static final int DEFAULT_TICKS_PER_CYCLE = 10;
	private List<FrameworkGuiEventListener> guis = new ArrayList<FrameworkGuiEventListener>();
	private ModuleName moduleName;
	protected Properties lidaProperties;

	public ModuleDriverImpl(int ticksPerCycle, LidaTaskManager tm,ModuleName moduleName){
		super(ticksPerCycle, tm);
		this.moduleName= moduleName;
	}
	public ModuleDriverImpl(int ticksPerCycle,ModuleName moduleName){
		super(ticksPerCycle);
		this.moduleName= moduleName;
	}
	public ModuleDriverImpl(){
		super();
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
	
	public ModuleName getModuleName(){
		return moduleName;
	}
	public void setModuleName(ModuleName moduleName) {
		this.moduleName = moduleName;
	}
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.framework.LidaModule#init(java.util.Properties)
	 */
	public void init(Properties lidaProperties) {
			this.lidaProperties = lidaProperties;
	}

	public void setAssociatedModule(LidaModule module) {
	}

	
}// class