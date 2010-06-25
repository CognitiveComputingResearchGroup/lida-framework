package edu.memphis.ccrg.lida.sensorymemory;

import java.util.ArrayList;
import java.util.List;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.environment.Environment;
import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.sensorymemory.SensoryMemory;

public abstract class SensoryMemoryImpl extends LidaModuleImpl 
										implements SensoryMemory{

	private List<SensoryMemoryListener> listeners = new ArrayList<SensoryMemoryListener>();
	protected Environment environment;
	protected Object sensoryMemoryContent;

	public SensoryMemoryImpl() {
		super(ModuleName.SensoryMemory);
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof SensoryMemoryListener) {
			addSensoryMemoryListener((SensoryMemoryListener) listener);
		}
	}
	
	@Override
	public void addSensoryMemoryListener(SensoryMemoryListener l) {
		listeners.add(l);
	}
	
	@Override
	public void setAssociatedModule(LidaModule module) {
		if (module != null) {
			if (module instanceof Environment && 
				module.getModuleName() == ModuleName.Environment) {
				environment = (Environment) module;
			}
		}
	}//
	
	public void receiveAction(LidaAction a){
		//TODO: What to do here is a research question.
	}
		
}// class
