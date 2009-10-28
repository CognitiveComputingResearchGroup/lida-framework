package edu.memphis.ccrg.lida.pam;

import java.util.Map;
import java.util.logging.Logger;

public class UpscalePropagationBehavior implements PropagationBehavior{
	
	private Logger logger = Logger.getLogger("lida.pam.UpscalePropagationBehavior");

	public double getActivationToPropagate(Map<String, Object> params) {
		
		Double totalActiv = (Double) params.get("totalActivation");
		Double upscale = (Double) params.get("upscale");
		if(totalActiv == null || upscale == null){
			logger.warning("Unable to obtain parameters");
			return 0;
		}else
			return totalActiv * upscale;
	}
}
