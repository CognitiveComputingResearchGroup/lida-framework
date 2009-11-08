package edu.memphis.ccrg.lida.framework;

import java.util.Properties;

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
	public abstract ModuleType getModuleType();
}
