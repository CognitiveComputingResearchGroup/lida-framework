package edu.memphis.ccrg.lida.pam;

import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Calculates a new activation using the upscale parameter.
 * 
 * @author Ryan J. McCall
 *
 */
public class UpscalePropagationBehavior implements PropagationBehavior{
	
	private Logger logger = Logger.getLogger("lida.pam.UpscalePropagationBehavior");

	/**
	 * Calculate and return an activation to propagate.
	 */
	public double getActivationToPropagate(Map<String, Object> params) {
		Double totalActiv = (Double) params.get("totalActivation");
		Double upscale = (Double) params.get("upscale");
		
		if(totalActiv == null || upscale == null){
			logger.log(Level.WARNING,"Unable to obtain parameters",LidaTaskManager.getActualTick());
			return 0;
		}else
			return totalActiv * upscale;
	}
}
