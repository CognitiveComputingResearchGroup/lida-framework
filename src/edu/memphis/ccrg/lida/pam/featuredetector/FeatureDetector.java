package edu.memphis.ccrg.lida.pam.featuredetector;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.pam.PamNode;

public interface FeatureDetector extends LidaTask {
	
	public abstract double detect(); 
	public abstract PamNode getPamNode();
	public abstract void setPamNode(PamNode node);
	public abstract void excitePam(double detectionActivation);
}
