package edu.memphis.ccrg.lida.framework.initialization;

import java.util.Map;

import edu.memphis.ccrg.lida.framework.Lida;

/**
 * An initializer performs configuration and set up on a particular module.  
 * @author ryanjmccall
 *
 */
public interface Initializer {
	
	/**
	 * Initializer is passed a particular module to configure.
	 * It can use the Lida object and a map of parameters to do so.  
	 * The variables in params are exactly those specified in Lida.xml under the module.
	 * 
	 * @param module
	 * @param lida
	 * @param params
	 */
	public abstract void initModule(Initializable module, Lida lida, Map<String, ?> params); 

}
