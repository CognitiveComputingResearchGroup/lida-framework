package edu.memphis.ccrg.lida.pam.featuredetector;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.pam.PamNode;

/**
 * This interface makes more sense if you look at the implementation of it
 * @author Javier Snaider
 * @see FeatureDetectorImpl
 */
public interface FeatureDetector extends LidaTask {
	
	public abstract double detect(); 
	
	/**
	 * Get nodes that this detector is looking for.
	 * @return
	 */
	public abstract Collection<PamNode> getPamNodes();
	
	/**
	 * 
	 * @param node
	 */
	public abstract void addPamNode(PamNode node);
	
	public abstract void excitePam(double detectionActivation);
}
