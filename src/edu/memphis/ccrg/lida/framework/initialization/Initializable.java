package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Map;

/**
 * An object which can be configured in particular ways.
 * @author Ryan J. McCall
 *
 */
public interface Initializable {

	/**
	 * This method initializes the module with parameters specified in LIDA.xml
	 * 
	 * @param parameters Map of parameters indexed by String names
	 */
	public void init(Map<String, ?> parameters);
//	TODO add another map for default parameters. first will be runtime-specified parameters.

	/**
	 * This is a convenience method to initialize Tasks. It is called from init(Map<String, Object> parameters). 
	 * Subclasses can overwrite this method in order to initialize the FrameworkTask
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