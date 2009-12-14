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

	private ModuleType lidaModule;
	private Properties lidaProperties;
	private Map<ModuleType,LidaModule> submodules=new ConcurrentHashMap<ModuleType,LidaModule>();
	
	public LidaModuleImpl(ModuleType lidaModule){
		this.lidaModule = lidaModule;
	}
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#getModuleType()
	 */
	public ModuleType getModuleType() {
		return lidaModule;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#init(java.util.Properties)
	 */
	public void init(Properties lidaProperties) {
		this.lidaProperties=lidaProperties;

	}

	public LidaModule getSubmodule(ModuleType type) {
		return submodules.get(type);
	}

	protected Map<ModuleType,LidaModule> getSubmodules(){
		return submodules;
	}
}
