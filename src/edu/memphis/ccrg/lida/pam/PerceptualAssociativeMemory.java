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
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetector;

/**
 * @author Ryan J. McCall
 */
public interface PerceptualAssociativeMemory extends LidaModule, Saveable {

	//Adding methods
	
    /**
     * Add a new node to this PAM with specified label
     * @param label label of new node
     * @return the new node
     */
	public PamNode addNewNode(String label);
	
	public PamNode addNewNode(String pamNodeType, String label);

	/**
	 * 
	 * @param node PamNode
	 * @return new copy of the pam node
	 */
	public PamNode addNode(PamNode node);
	
	/**
	 * Add a collection of nodes
	 * @param nodes nodes to add
	 * @return actual node stored in this PAM
	 */
	public Set<PamNode> addNodes(Set<PamNode> nodes);
	
	/**
	 * Add a new link to this PAM
	 * @param source source for link
	 * @param sink sink for link
	 * @param type type for link
	 * @param activation initial activation for link
	 * @return created link
	 */
	public Link addNewLink(PamNode source, PamNode sink, LinkCategory type, double activation);

	public Link addNewLink(ExtendedId sourceId, ExtendedId sinkId, LinkCategory type, double activation);
	
	public Set<PamLink> addLinks(Set<PamLink> links);
	
	public void addFeatureDetector(FeatureDetector fd);
		
	/**
	 * 
	 * @param pl listener
	 */
	public void addPamListener(PamListener pl);
	
	//*** Setting parameters	
	
	/**
	 * Set the type of Link used in this PAM
	 * @param type link type
	 */
	public void setNewLinkType(String type);
	
	/**
	 * Set the type of Node used in this PAM
	 * @param type node type
	 */
    public void setNewNodeType(String type);
	
    //*** Set strategies
    
	/**
	 * Changes how the nodes in this PAM are excited.
	 * 
	 * @param strat ExciteStrategy
	 */
	public void setExciteStrategy(ExciteStrategy strat);
	
	/**
	 * Change how nodes and links are decayed
	 * @param strat DecayStrategy
 	 */
	public void setDecayStrategy(DecayStrategy strat);
	
	/**
	 * Set the behavior governing how activation is propagated
	 * @param b PropagationBehavior
	 */
	public void setPropagationBehavior(PropagationBehavior b);
	
	//Activation spreading methods
	
	/**
	 * Send a burst of activation to a node.
	 * @param node The node to receiving the activation
	 * @param amount The amount of activation
	 */
	public void receiveActivationBurst(PamNode node, double amount);
	
	/**
	 * Amount is coming from source.  Link and sink should be excited amount
	 * @param source source of activation
	 * @param link link between source and sink
	 * @param sink recipient of activation
	 * @param amount activation sent
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
	 * @param pamNode PamNode
	 */
	public void addNodeToPercept(PamNode pamNode);
	
	/**
	 * Put a Link into the percept
	 * @param l Link
 	 */
	public void addLinkToPercept(PamLink l);
	
	/**
	 * Put a NodeStructure into the percept
	 * @param ns NodeStructure
	 */
	public void addNodeStructureToPercept(NodeStructure ns);
	
	//Contains methods
	
	public boolean containsNode(PamNode node);
	
	public boolean containsLink(PamLink link);
	
	//**** Get Methods
	//TODO immutable nodes?
	public Node getNode(int id);
	
	/**
	 * Returns an unmodifiable collection of the PAM nodes as nodes
	 */
	public Collection<Node> getNodes();
	
	public NodeStructure getPamNodeStructure();
	
	/**
	 * Get the running feature detectors
	 */
	public Collection<FeatureDetector> getFeatureDetectors();
	
	public boolean isOverPerceptThreshold(PamLinkable l);
	
} 