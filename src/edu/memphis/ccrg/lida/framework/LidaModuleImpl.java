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

	private ModuleName lidaModule;
	private Properties lidaProperties;
	private Map<ModuleName,LidaModule> submodules=new ConcurrentHashMap<ModuleName,LidaModule>();
	
	public LidaModuleImpl(ModuleName lidaModule){
		this.lidaModule = lidaModule;
	}
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#getModuleType()
	 */
	public ModuleName getModuleName() {
		return lidaModule;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.LidaModule#init(java.util.Properties)
	 */
	public void init(Properties lidaProperties) {
		this.lidaProperties=lidaProperties;

	}

	public LidaModule getSubmodule(ModuleName type) {
		return submodules.get(type);
	}

	protected Map<ModuleName,LidaModule> getSubmodules(){
		return submodules;
	}
}
