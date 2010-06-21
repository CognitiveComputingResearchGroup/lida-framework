/**
 * PAMinterface.java
 */
package edu.memphis.ccrg.lida.pam;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkType;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;

/**
 * @author Ryan McCall
 */
public interface PerceptualAssociativeMemory extends LidaModule{

	public void setTaskSpawner(TaskSpawner spawner);
	public void addNode(PamNode node);
	public void addNodes(Set<PamNode> nodes);
	public void addLinks(Set<Link> links);
	public boolean addFeatureDetector(FeatureDetector fd);
	public void addPamListener(PamListener pl);
	
	/**
	 * Updates PAM's parameters from the supplied map
	 */
	public void setParameters(Map<String, ?> parameters);
	
	/**
	 * Changes how the nodes in this PAM are excited.
	 * 
	 * @param behavior
	 */
	public void setExciteStrategy(ExciteStrategy behavior);
	
	/**
	 * Change how nodes and links are decayed
	 * @param c
	 */
	public void setDecayStrategy(DecayStrategy c);
	
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
	
	public void addLink(PamNode source, PamNode sink, LinkType type, double activation);

	public void addLink(String sourceId, String sinkId, LinkType type, double activation);
	
}//interface PAMinterface