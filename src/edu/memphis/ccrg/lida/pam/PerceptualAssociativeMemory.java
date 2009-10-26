/**
 * PAMinterface.java
 */
package edu.memphis.ccrg.lida.pam;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.TaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.framework.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;

/**
 * @author Ryan McCall
 */
public interface PerceptualAssociativeMemory{
	
	public void setTaskManager(LidaTaskManager tm);
	public void setTaskSpawner(TaskSpawner spawner);
	//
	public void addNodes(Set<PamNode> nodes);
	public void addLinks(Set<Link> links);
	public boolean addFeatureDetector(FeatureDetector fd);
	//
	public void addPamListener(PamListener pl);
	
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
	
	/**
	 * Decay activations of PAM nodes and/or links
	 */
	public void decayPam();
	
	public void receiveActivationBurst(PamNode pNode, double amount);
	public void receiveActivationBurst(Set<PamNode> nodes, double amount);
	
	public void checkIfOverThreshold(PamNode pamNode);
	public void addNodeToPercept(PamNode pamNode);
	
	public PamNodeStructure getNodeStructure();
	public Collection<FeatureDetector> getFeatureDetectors();
}//interface PAMinterface