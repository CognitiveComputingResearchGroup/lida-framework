package edu.memphis.ccrg.lida.framework.mockclasses;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.triggers.BroadcastTrigger;

public class MockGlobalWorkspaceImpl implements GlobalWorkspace {

	@Override
	public void addBroadcastListener(BroadcastListener bl) {
		
		
	}

	@Override
	public void addBroadcastTrigger(BroadcastTrigger t) {
		
		
	}

	@Override
	public boolean addCoalition(Coalition coalition) {
		
		return false;
	}

	@Override
	public void start() {
		
		
	}

	@Override
	public void addListener(ModuleListener listener) {
		
		
	}

	@Override
	public void addSubModule(LidaModule lm) {
		
		
	}

	@Override
	public void decayModule(long ticks) {
		
		
	}

	@Override
	public Object getModuleContent(Object... params) {
		
		return null;
	}

	@Override
	public ModuleName getModuleName() {
		
		return null;
	}

	@Override
	public LidaModule getSubmodule(ModuleName name) {
		
		return null;
	}

	@Override
	public void setModuleName(ModuleName moduleName) {
		
		
	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		
		return null;
	}

	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		
		
	}

	@Override
	public void triggerBroadcast() {
		System.out.println("Boradcast started at tick: "+LidaTaskManager.getActualTick());
		System.out.println();
	}

	@Override
	public void setAssistingTaskSpawner(TaskSpawner ts) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(Map<String, ?> parameters) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public TaskSpawner getAssistingTaskSpawner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

}
