package edu.memphis.ccrg.lida.pam;

import java.util.Map;

public interface PropagationBehavior {
	
	

	public abstract double getActivationToPropagate(Map<String, Object> params);

}
