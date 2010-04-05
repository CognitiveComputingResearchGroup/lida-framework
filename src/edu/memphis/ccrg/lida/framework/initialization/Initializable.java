package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.LidaModule;

public interface Initializable {
	/**
	 * This method initializes the module using properties from LIDA Properties File
	 * @param lidaProperties
	 */
	public abstract void init (Map<String,?> lidaProperties);
	/**
	 * A convenience method to read parameters from the Map of properties set 
	 * with the init() method.
	 * @param name the parameter name
	 * @param defaultValue the default value to be returned if the parameter doesn't exist.
	 * @return the value of the parameter or the default value.
	 */
	public abstract Object getParam(String name,Object defaultValue);
	/**
	 * Sets an associated LidaModule.
	 * @param module the module to be associated.
	 */
	public abstract void setAssociatedModule(LidaModule module);
}
