/**
 * PAMinterface.java
 */
package edu.memphis.ccrg.lida.pam;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.framework.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;

/**
 * @author Ryan McCall
 */
public interface PerceptualAssociativeMemory extends LidaModule{

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
	
	/**
	 * Change how nodes and links are decayed
	 * @param c
	 */
	public void setDecayBehavior(DecayBehavior c);
	
	/**
	 * Send a burst of activation to a node.
	 * @param pNode The node to receiving the activation
	 * @param amount The amount of activation
	 */
	public void receiveActivationBurst(PamNode pNode, double amount);
	
	/**
	 * Send a burst of activation to a Set of node.
	 * @param nodes Nodes to receive activation
	 * @param amount Amount of activation
	 */
	public void receiveActivationBurst(Set<PamNode> nodes, double amount);
	
	/**
	 * Propagates activation from a PamNode to its parents
	 * @param pamNode The node to propagate activation from.
	 */
	public void sendActivationToParentsOf(PamNode pamNode);
	
	/**
	 * Set the behavior governing how activation is propagated
	 * @param b
	 */
	public void setPropagationBehavior(PropagationBehavior b);
	
	/**
	 * Put a PamNode into the percept
	 * @param pamNode
	 */
	public void addNodeToPercept(PamNode pamNode);
	
	/**
	 * Put a Link into the percept
	 * @param l
	 */
	public void addLinkToPercept(Link l);
	
	/**
	 * Put a NodeStructure into the percept
	 * @param ns
	 */
	public void addNodeStructureToPercept(NodeStructure ns);
	
	/**
	 * Get Pam represented as a node structure
	 * @return
	 */
	public PamNodeStructure getNodeStructure();
	
	/**
	 * Get the running feature detectors
	 * @return
	 */
	public Collection<FeatureDetector> getFeatureDetectors();

	public PamNode getNode(long id);
}//interface PAMinterface