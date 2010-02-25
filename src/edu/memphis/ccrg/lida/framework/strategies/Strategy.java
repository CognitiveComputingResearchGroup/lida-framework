/**
 * 
 */
package edu.memphis.ccrg.lida.framework.strategies;

import java.util.Map;

/**
 * @author Javier Snaider
 *
 */
public interface Strategy {
	/**
	 * Receives a Map of init parameters for initalization.
	 * @param parameters the Map with the parameters.
	 */
	public void init(Map<String, ? extends Object> parameters);

}
