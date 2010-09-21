/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import java.util.Collection;
import java.util.Set;

import edu.memphis.ccrg.lida.framework.LidaModule;
import edu.memphis.ccrg.lida.framework.dao.Saveable;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.featuredetector.FeatureDetector;

/**
 * @author Ryan McCall
 */
public interface PerceptualAssociativeMemory extends LidaModule, Saveable {

	//**** Adding methods
	
    /**
     * Add a new node to this PAM with specified label
     * @param label
     * @return the new node
     */
	public PamNode addNewNode(String label);

	/**
	 * 
	 * @param node
	 * @return new copy of the pam node !
	 */
	public PamNode addNode(PamNode node);
	
	/**
	 * Add a collection of nodes
	 * @param nodes
	 * @return actual node stored in this PAM
	 */
	public Set<PamNode> addNodes(Set<PamNode> nodes);
	
	/**
	 * Add a new link to this PAM
	 * @param source
	 * @param sink
	 * @param type
	 * @param activation
	 * @return created link
	 */
	public Link addNewLink(PamNode source, PamNode sink, LinkCategory type, double activation);

	public Link addNewLink(String sourceId, String sinkId, LinkCategory type, double activation);
	
	public Set<PamLink> addLinks(Set<PamLink> links);
	
	public void addFeatureDetector(FeatureDetector fd);
		
	/**
	 * 
	 * @param pl
	 */
	public void addPamListener(PamListener pl);
	
	//*** Setting parameters	
	
	/**
	 * Set the type of Link used in this PAM
	 * @param type
	 */
	public void setNewLinkType(String type);
	
	public void setTaskSpawner(TaskSpawner spawner);
	
	/**
	 * Set the type of Node used in this PAM
	 * @param type
	 */
    public void setNewNodeType(String type);
	
    //*** Set strategies
    
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
	 * Set the behavior governing how activation is propagated
	 * @param b
	 */
	public void setPropagationBehavior(PropagationBehavior b);
	
	//****Activation spreading methods
	
	/**
	 * Send a burst of activation to a node.
	 * @param node The node to receiving the activation
	 * @param amount The amount of activation
	 */
	public void receiveActivationBurst(PamNode node, double amount);
	
	/**
	 * Amount is coming from source.  Link and sink should be excited amount
	 * @param source
	 * @param link
	 * @param sink
	 * @param amount
	 */
	public void propagateActivation(PamNode source, PamLink link, PamNode sink, double amount);
	
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
	public void sendActivationToParents(PamNode pamNode);
	
	//*** Percept methods
	
	/**
	 * Put a PamNode into the percept
	 * @param pamNode
	 */
	public void addNodeToPercept(PamNode pamNode);
	
	/**
	 * Put a Link into the percept
	 * @param l
	 */
	public void addLinkToPercept(PamLink l);
	
	/**
	 * Put a NodeStructure into the percept
	 * @param ns
	 */
	public void addNodeStructureToPercept(NodeStructure ns);
	
	//Contains methods
	
	public boolean containsNode(PamNode node);
	
	public boolean containsLink(PamLink link);
	
	
	//**** Get Methods

	public Node getNode(long id);
	
	/**
	 * Returns an unmodifiable collection of the PAM nodes as nodes
	 */
	public Collection<Node> getNodes();
	
	/**
	 * Get the running feature detectors
	 */
	public Collection<FeatureDetector> getFeatureDetectors();
	
}//interface 