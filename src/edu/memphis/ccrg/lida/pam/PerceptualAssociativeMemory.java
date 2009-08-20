/**
 * PAMinterface.java
 */
package edu.memphis.ccrg.lida.pam;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.TaskSpawner;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

/**
 * @author Ryan McCall
 */
public interface PerceptualAssociativeMemory{
	
	/**
	 * Updates PAM's parameters from the supplied map
	 */
	public void setParameters(Map<String, Object> parameters);
	
	/**
	 * Changes how the nodes in this PAM are excited.
	 * 
	 * @param behavior
	 */
	public void setExciteBehavior(ExciteBehavior behavior);
	public void setDecayBehavior(DecayBehavior c);
	
	public void addNodes(Set<PamNode> nodes);
	
	public boolean addFeatureDetector(FeatureDetector fd);
	
	public void addLinks(Set<Link> links);
	
	public void setTaskSpawner(TaskSpawner spawner);
	
	/**
	 * Send Percept to PAM Listeners
	 */
	public void sendOutPercept();	
	
	/**
	 * Decay activations of PAM nodes and/or links
	 */
	public void decayPam();
	
	public void receiveActivationBurst(PamNode pNode, double amount);
	public void receiveActivationBurst(Set<PamNode> nodes, double amount);
	
//	public PamNode getPamNode(long id);
	//TODO: unless there's a need for getPamNode, I'm commenting it out.
	
	public Collection<FeatureDetector> getFeatureDetectors();
	
	public void addPamListener(PamListener pl);

	public void addToPercept(PamNode pamNode);

}//interface PAMinterface