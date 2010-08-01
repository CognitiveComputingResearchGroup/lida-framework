package edu.memphis.ccrg.lida.pam;

import java.util.Map;

/**
 * A behavior that calculates a new activation to pass.
 * @author ryanjmccall
 *
 */
public interface PropagationBehavior {
	/**
	 * Depending on the behavior different parameters can be pass in params for the 
	 * calculation.
	 * @param params
	 */
	public abstract double getActivationToPropagate(Map<String, Object> params);
}
