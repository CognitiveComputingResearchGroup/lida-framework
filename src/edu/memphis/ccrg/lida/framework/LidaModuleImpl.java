/**
 * 
 */
package edu.memphis.ccrg.lida.framework;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Javier Snaider
 * 
 */
public abstract class LidaModuleImpl implements LidaModule {

	private ModuleName moduleName;
	protected Properties lidaProperties;
	private Map<ModuleName, LidaModule> submodules = new ConcurrentHashMap<ModuleName, LidaModule>();

	public LidaModuleImpl(ModuleName moduleName) {
		this.moduleName = moduleName;
	}

	public LidaModuleImpl() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#getModuleType()
	 */
	public ModuleName getModuleName() {
		return moduleName;
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

	public LidaModule getSubmodule(ModuleName type) {
		return submodules.get(type);
	}

	public void addModule(LidaModule lm) {
		submodules.put(lm.getModuleName(), lm);
	}

	protected Map<ModuleName, LidaModule> getSubmodules() {
		return submodules;
	}

	/**
	 * Decay the module and all the submodules. Subclasses must call this
	 * implementation in order to have all submodules decayed.
	 * 
	 * @param ticks
	 *            number of ticks to decay.
	 */
	public void decayModule(long ticks) {
		for (LidaModule lm : submodules.values()) {
			lm.decayModule(ticks);
		}
	} 

	public void setModuleName(ModuleName moduleName) {
		this.moduleName = moduleName;
	}
	

	public void setAssociatedModule(LidaModule module) {
	}

}