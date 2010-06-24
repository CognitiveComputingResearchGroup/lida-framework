package edu.memphis.ccrg.lida.pam.featuredetector;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.pam.PamNode;

public interface FeatureDetector extends LidaTask {
	
	public abstract double detect(); 
	public abstract void detectMultipleNodes();
	public abstract Collection<PamNode> getPamNodes();
	public abstract void addPamNode(PamNode node);
	public abstract void excitePam(double detectionActivation);
}
