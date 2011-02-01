package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Map;

public interface Initializable {

	/**
	 * This method initializes the module with parameters specified in LIDA.xml
	 * @param parameters Map of parameters indexed by String names
	 */
	public void init(Map<String, ?> parameters);

	/**
	 * This is a convenience method to initialize Tasks. It is called from init(Map<String, Object> parameters). 
	 * Subclasses can overwrite this method in order to initialize the LidaTask
	 */
	public void init();

	/**
	 * A convenience method to read parameters from the Map of properties set 
	 * with the init() method.
	 * @param name the parameter name
	 * @param defaultValue the default value to be returned if the parameter doesn't exist.
	 * @return the value of the parameter or the default value.
	 */
	public Object getParam(String name, Object defaultValue);

}