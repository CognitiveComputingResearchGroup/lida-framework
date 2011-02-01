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
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.pam.tasks.FeatureDetector;

/**
 * A main LIDA module which contains feature detectors, nodes, and links.
 * @author Ryan J. McCall
 */
public interface PerceptualAssociativeMemory extends LidaModule, Saveable {
	
    /**
     * Creates and adds a new Node to this {@link PerceptualAssociativeMemory} with specified label.
     * New node is returned.  Default PamNode type is used.
     * @param label label of new node
     * @return the new node
     */
	public PamNode addNewNode(String label);
	
	/**
	 * Creates and adds a new Node to this {@link PerceptualAssociativeMemory} with specified label and 
	 * PamNode type.  New node is returned.
	 * 
	 * @param pamNodeType type of PamNode used.
	 * @param label label of new node
	 * @return the new node
	 */
	public PamNode addNewNode(String pamNodeType, String label);

	/**
	 * Adds a COPY of specified node to this {@link PerceptualAssociativeMemory}.
	 * @param node PamNode
	 * @return Copied PamNode actually stored in this PAM.
	 */
	public PamNode addNode(PamNode node);
	
	/**
	 * Adds a COPY of a collection of PamNodes to this PAM.
	 *
	 * @param nodes nodes to add
	 * @return Copied PamNodes actually stored in this PAM
	 */
	public Set<PamNode> addNodes(Set<PamNode> nodes);
	
	/**
	 * Creates and adds a new link to this PAM.
	 *
	 * @param source source for link
	 * @param sink sink for link
	 * @param type type for link
	 * @param activation initial activation for link
	 * @return created link
	 */
	public Link addNewLink(PamNode source, PamNode sink, LinkCategory type, double activation);

	/**
	 * Creates and adds a new link to this PAM.
	 *
	 * @param sourceId Source's {@link ExtendedId}
	 * @param sinkId Sink's {@link ExtendedId}
	 * @param type Link's LinkCategory
	 * @param activation initial activation
	 * @return created Link
	 */
	public Link addNewLink(ExtendedId sourceId, ExtendedId sinkId, LinkCategory type, double activation);
	
	/**
	 * Adds a COPY of a collection of PamLinks to this PAM.
	 *
	 * @param links  PamLinks to add
	 * @return Copied PamLinks actually stored in this PAM
	 */
	public Set<PamLink> addLinks(Set<PamLink> links);
	
	/**
	 * Adds and runs specified FeatureDetector.
	 * @param fd {@link FeatureDetector}
	 */
	public void addFeatureDetector(FeatureDetector fd);
		
	/**
	 * Adds {@link PamListener}.
	 *
	 * @param pl listener
	 */
	public void addPamListener(PamListener pl);
	
	
	/**
	 * Set the default type of {@link PamLink}Link used in this PAM.
	 *
	 * @param type new default PamLink type
	 */
	//TODO force class?
	public void setNewLinkType(String type);
	
	/**
	 * Set the type of {@link PamNode} used in this PAM.
	 *
	 * @param type new default {@link PamNode} type
	 */
    public void setNewNodeType(String type);
    
	/**
	 * Changes the {@link ExciteStrategy} used for {@link PamLinkable}s 
	 * in this PAM.
	 * @param strat ExciteStrategy
	 */
	public void setExciteStrategy(ExciteStrategy strat);
	
	/**
	 * Change the {@link DecayStrategy} used for {@link PamLinkable}s 
	 * in this PAM.
	 * @param strat DecayStrategy
 	 */
	public void setDecayStrategy(DecayStrategy strat);
	
	/**
	 * Sets {@link PropagationBehavior} governing how activation is propagated in this PAM.
	 *
	 * @param b PropagationBehavior
	 */
	public void setPropagationBehavior(PropagationBehavior b);
	
	/**
	 * Excites specified {@link PamNode} an amount of activation.
	 * @param node The node to receiving the activation
	 * @param amount amount of activation to excite
	 */
	public void receiveActivationBurst(PamNode node, double amount);
	
	/**
	 * Excites PamNodes with an amount of activation.
	 * @param nodes PamNodes to be excited
	 * @param amount amount of activation
	 */
	public void receiveActivationBurst(Set<PamNode> nodes, double amount);
	
	/**
	 * Propagate activation from a PamNode to another PamNode along a PamLink.
	 * Excites both link and sink
	 * @param source source of activation
	 * @param link link between source and sink
	 * @param sink recipient of activation
	 * @param amount activation sent
	 */
	public void propagateActivation(PamNode source, PamLink link, PamNode sink, double amount);

	/**
	 * Propagates activation from a PamNode to its parents.
	 *
	 * @param pamNode The PamNode to propagate activation from.
	 */
	public void propagateActivationToParents(PamNode pamNode);
	
	/**
	 * Add a PamNode to the percept.
	 *
	 * @param pamNode PamNode to add.
	 */
	public void addNodeToPercept(PamNode pamNode);
	
	/**
	 * Add a PamLink to the percept.
	 *
	 * @param l Link to add.
	 */
	public void addLinkToPercept(PamLink l);
	
	/**
	 * Add a NodeStructure to the percept.
	 *
	 * @param ns NodeStructure
	 */
	public void addNodeStructureToPercept(NodeStructure ns);
	
	/**
	 * Returns true if this PAM contains specified PamNode.
	 *
	 * @param node the node
	 * @return true, if successful
	 */
	public boolean containsNode(PamNode node);
	
	/**
	 * Contains node.
	 *
	 * @param id ExtendedId of sought node
	 * @return true if PAM contains the node with this id.
	 */
	public boolean containsNode(ExtendedId id);
	
	/**
	 * Returns true if this PAM contains specified PamLink.
	 *
	 * @param link the link
	 * @return true, if successful
	 */
	public boolean containsLink(PamLink link);
	
	/**
	 * Contains link.
	 *
	 * @param id ExtendedId of sought link
	 * @return true if PAM contains the link with this id.
	 */
	public boolean containsLink(ExtendedId id);
	
	/**
	 * Sets perceptThreshold
	 * @param t threshold for a {@link Linkable} to become part of the percept
	 */
	public void setPerceptThreshold(double t);
	
	/**
	 * Gets perceptThreshold
	 * @return threshold for a {@link Linkable} to become part of the percept
	 */
	public double getPerceptThreshold();
	
	/**
	 * Sets upscaleFactor
	 * @param f scale factor for feed-forward activation propagation
	 */
	public void setUpscaleFactor(double f);
	
	/**
	 * Gets upscaleFactor
	 * @return scale factor for feed-forward activation propagation
	 */
	public double getUpscaleFactor();
	
	/**
	 * Sets downscaleFactor 
	 * @param f scale factor for top-down activation propagation
	 */
	public void setDownscaleFactor(double f);

	/**
	 * Gets downscaleFactor
	 * @return scale factor for top-down activation propagation
	 */
	public double getDownscaleFactor();
	
	/**
	 * Returns whether PamLinkable is above percept threshold.
	 * @param l a PamLinkable
	 * @return true if PamLinkable's total activation is above percept threshold 
	 */
	public boolean isOverPerceptThreshold(PamLinkable l);
	
	/**
	 * Returns a copy of {@link PamNode} with specified id from this PAM or null.
	 *
	 * @param id the id
	 * @return the pam node
	 */
	public Node getPamNode(int id);
	
	/**
	 * Returns {@link PamNode} with specified {@link ExtendedId} or null
	 * @param id sought {@link ExtendedId}
	 * @return PamNode
	 */
	public Node getPamNode(ExtendedId id);
	
	/**
	 * 
	 * @param id
	 * @return Copy of {@link PamLink} with specified id from this PAM or null. 
	 *
	 * @return the pam nodes
	 */
	public Link getPamLink(ExtendedId id);
	
	/**
	 * Returns an unmodifiable collection of the {@link PamNode}s in this PAM as {@link Node}s.
	 *
	 * @return the feature detectors
	 */
	public Collection<Node> getPamNodes();
		
} 