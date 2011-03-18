package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class MockProceduralMemory implements ProceduralMemory {

	@Override
	public void activateSchemes(NodeStructure broadcastContent) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addScheme(Scheme s) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSchemes(Collection<Scheme> schemes) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean containsScheme(Scheme s) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public SchemeActivationBehavior getSchemeActivationBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getSchemeCount() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub

	}

	@Override
	public void addListener(ModuleListener listener) {
		// TODO Auto-generated method stub

	}

	@Override
	public void addSubModule(LidaModule lm) {
		// TODO Auto-generated method stub

	}

	@Override
	public void decayModule(long ticks) {
		// TODO Auto-generated method stub

	}

	@Override
	public TaskSpawner getAssistingTaskSpawner() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getModuleContent(Object... params) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ModuleName getModuleName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public LidaModule getSubmodule(ModuleName name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setAssistingTaskSpawner(TaskSpawner ts) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setModuleName(ModuleName moduleName) {
		// TODO Auto-generated method stub

	}

	@Override
	public void setAssociatedModule(LidaModule module, int moduleUsage) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getParam(String name, Object defaultValue) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void init(Map<String, ?> parameters) {
		// TODO Auto-generated method stub

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean setState(Object content) {
		// TODO Auto-generated method stub
		return false;
	}

}
