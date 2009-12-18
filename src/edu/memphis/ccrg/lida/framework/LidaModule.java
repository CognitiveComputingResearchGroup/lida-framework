package edu.memphis.ccrg.lida.framework;

import java.util.Properties;

import edu.memphis.ccrg.lida.framework.shared.NodeStructure;

/**
 * Generic Module Interface in LIDA.
 * Mostly initialization tasks.
 * @author Javier Snaider
 *
 */
public interface LidaModule {

	/**
	 * This metod initializaes the module using properties from LIDA Properties File
	 * @param lidaProperties
	 */
	public abstract void init (Properties lidaProperties);
	
	/**
	 * @return ModuleType
	 */
	public abstract ModuleName getModuleName();
	
	public abstract LidaModule getSubmodule(ModuleName name);
	
	public abstract Object getModuleContent();
}
