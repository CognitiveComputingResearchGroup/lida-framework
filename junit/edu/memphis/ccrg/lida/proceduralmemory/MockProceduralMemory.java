package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class MockProceduralMemory implements ProceduralMemory {

	@Override
	public void activateSchemes(NodeStructure broadcastContent) {
		// not implemented

	}

	@Override
	public void addScheme(Scheme s) {
		// not implemented

	}

	@Override
	public void addSchemes(Collection<Scheme> schemes) {
		// not implemented

	}

	@Override
	public boolean containsScheme(Scheme s) {
		// not implemented
		return false;
	}

	@Override
	public SchemeActivationBehavior getSchemeActivationBehavior() {
		// not implemented
		return null;
	}

	@Override
	public int getSchemeCount() {
		// not implemented
		return 0;
	}
	
	public Collection<Scheme> getTestInstantiated(){
		return instantiated;
	}
	
	public void clearInstantiated(){
		instantiated.clear();
	}
	
	private Collection<Scheme> instantiated = new ArrayList<Scheme>();

	@Override
	public void sendInstantiatedScheme(Scheme s) {
		instantiated.add(s);
	}

	@Override
	public void setSchemeActivationBehavior(SchemeActivationBehavior b) {
		// not implemented

	}

	@Override
	public void addListener(ModuleListener listener) {
		// not implemented

	}

	@Override
	public void addSubModule(FrameworkModule lm) {
		// not implemented

	}

	@Override
	public void decayModule(long ticks) {
		// not implemented

	}

	@Override
	public TaskSpawner getAssistingTaskSpawner() {
		// not implemented
		return null;
	}

	@Override
	public Object getModuleContent(Object... params) {
		// not implemented
		return null;
	}

	@Override
	public ModuleName getModuleName() {
		// not implemented
		return null;
	}

	@Override
	public FrameworkModule getSubmodule(ModuleName name) {
		// not implemented
		return null;
	}

	@Override
	public void setAssistingTaskSpawner(TaskSpawner ts) {
		// not implemented

	}

	@Override
	public void setModuleName(ModuleName moduleName) {
		// not implemented

	}

	@Override
	public void setAssociatedModule(FrameworkModule module, String moduleUsage) {
		// not implemented

	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		// not implemented
		return null;
	}

	@Override
	public void init(Map<String, ?> parameters) {
		// not implemented

	}

	@Override
	public void init() {
		// not implemented

	}

	@Override
	public Object getState() {
		// not implemented
		return null;
	}

	@Override
	public boolean setState(Object content) {
		// not implemented
		return false;
	}

	@Override
	public FrameworkModule getSubmodule(String name) {
		// not implemented
		return null;
	}

}
