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

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.dao.Saveable;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.activation.Learnable;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.pam.tasks.AddToPerceptTask;
import edu.memphis.ccrg.lida.pam.tasks.ExcitationTask;
import edu.memphis.ccrg.lida.pam.tasks.DetectionAlgorithm;
import edu.memphis.ccrg.lida.pam.tasks.BasicDetectionAlgorithm;
import edu.memphis.ccrg.lida.pam.tasks.PropagationTask;

/**
 * A main LIDA module which contains feature detectors, nodes, and links.
 * @author Ryan J. McCall
 */
public interface PerceptualAssociativeMemory extends FrameworkModule, Saveable {
	
    /**
     * Convenience method for initializing {@link PerceptualAssociativeMemory}
     * before runtime, does not check for duplicates when adding.
     * Creates and adds a new Node to this {@link PerceptualAssociativeMemory} with specified label.
     * The new node is returned.  Default PamNode type is used.
     * Node is given base-level activation and base-level activation removal threshold
     * based on default of Learnable. 
     * @param label label of new node
     * @see Learnable
     * @return the new node
     */
	public PamNode addNewNode(String label);
		
	/**
	 * Convenience method for initializing {@link PerceptualAssociativeMemory}
     * before runtime, does not check for duplicates when adding.
	 * Creates and adds a new Node to this {@link PerceptualAssociativeMemory} of specified type, with 
	 * specified label, base-level activation, and base-level removal threshold 
     * New node is returned.
	 * @param label label of new node
	 * @param baseLevelActivation initial base-level activation
	 * @param baseLevelRemovalThreshold initial base-level removal threshold
	 * @param baseLevelExciteStrategy {@link ExciteStrategy} for base-level activation
	 * @param baseLevelDecayStrategy {@link DecayStrategy} for base-level activation
	 * @return the new {@link PamNode} stored in this PAM.
	 */
	public PamNode addNewNode(String label, double baseLevelActivation, 
			double baseLevelRemovalThreshold, String baseLevelExciteStrategy, String baseLevelDecayStrategy);
	
	/**
     * Convenience method for initializing {@link PerceptualAssociativeMemory}
     * before runtime, does not check for duplicates when adding.
     * Creates and adds a new Link to this {@link PerceptualAssociativeMemory} with specified 
     * source, sink and category. Default {@link PamLink} type is used.
     * The new Link is returned.  
     * Node is given base-level activation and base-level activation removal threshold
     * based on default of Learnable. 
	 * @param source source for link
	 * @param sink sink for link
	 * @param category category for link
     * @see Learnable
     * @return the new Link
     */
	public Link addNewLink(Node source, Linkable sink, LinkCategory category);
	
	/**
	 * Convenience method for initializing {@link PerceptualAssociativeMemory}
     * before runtime, does not check for duplicates.
	 * Creates and adds a new default {@link PamLink} with specified attributes. 
	 *
	 * @param source source for link
	 * @param sink sink for link
	 * @param category category for link
	 * @param baseLevelActivation initial activation for link
	 * @param baseLevelRemovalThreshold amount of base-level activation link must have to remain in this PAM.
	 * @param baseLevelExciteStrategy {@link ExciteStrategy} for base-level activation
	 * @param baseLevelDecayStrategy {@link DecayStrategy} for base-level activation
	 * @return the new {@link PamLink} stored in this PAM.
	 */
	public Link addNewLink(Node source, Linkable sink, LinkCategory category, double baseLevelActivation, double baseLevelRemovalThreshold, String baseLevelExciteStrategy, String baseLevelDecayStrategy);

	/**
	 * Convenience method for initializing {@link PerceptualAssociativeMemory}
     * before runtime, does not check for duplicates.
	 * Creates and adds a new default {@link PamLink} with specified attributes. 
	 *
	 * @param sourceId Source's {@link ExtendedId}
	 * @param sinkId Sink's {@link ExtendedId}
	 * @param category Link's LinkCategory
	 * @param baseLevelActivation initial activation
	 * @param baseLevelRemovalThreshold amount of base-level activation link must have to remain in PAM.
	 * @param baseLevelExciteStrategy {@link ExciteStrategy} for base-level activation
	 * @param baseLevelDecayStrategy {@link DecayStrategy} for base-level activation
	 * @return the new {@link PamLink} stored in this PAM.
	 */
	public Link addNewLink(int sourceId, ExtendedId sinkId, LinkCategory category, double baseLevelActivation, double baseLevelRemovalThreshold, String baseLevelExciteStrategy, String baseLevelDecayStrategy);

	/**
	 * Adds a COPY of specified node to this {@link PerceptualAssociativeMemory}.
	 * Node will be of Pam's default type. 
	 * @param node PamNode
	 * @return Copied PamNode actually stored in this PAM.
	 */
	public PamNode addDefaultNode(Node node);
	
	/**
	 * Adds a COPY of a collection of Nodes to this PAM.
	 * Nodes will be of Pam's default type.
	 * @param nodes nodes to add
	 * @return Copied PamNodes actually stored in this PAM
	 */
	public Set<PamNode> addDefaultNodes(Set<? extends Node> nodes);
	
	/**
	 * Adds a COPY of specified link to this PAM.
	 * Link will be of Pam's default type.
	 *
	 * @param link  PamLink to add
	 * @return Copied PamLink actually stored in this PAM
	 */
	public PamLink addDefaultLink(Link link);
	
	/**
	 * Adds a COPY of specified collection of PamLinks to this PAM.
	 * Links will be of Pam's default type.
	 *
	 * @param links  PamLinks to add
	 * @return Copied PamLinks actually stored in this PAM
	 */
	public Set<PamLink> addDefaultLinks(Set<? extends Link> links);
	
	/**
	 * Adds specified FeatureDetector to be run.
	 * @param fd {@link DetectionAlgorithm}
	 */
	public void addPerceptualAlgorithm(DetectionAlgorithm fd);
		
	/**
	 * Adds {@link PamListener}.
	 *
	 * @param pl listener
	 */
	public void addPamListener(PamListener pl);	
	
	/**
	 * Sets {@link PropagationStrategy} governing how activation is propagated in this PAM.
	 *
	 * @param strategy {@link PropagationStrategy}
	 */
	public void setPropagationStrategy(PropagationStrategy strategy);
	
	/**
	 * Excites specified {@link PamNode} an amount of activation.
	 * @param linkable Id of the node receiving the activation
	 * @param amount amount of activation to excite
	 * @see ExcitationTask {@link BasicDetectionAlgorithm}
	 */
	public void receiveActivationBurst(PamLinkable linkable, double amount);
	
	/**
	 * Excites PamNodes with an amount of activation.
	 * @param linkables Ids of PamNodes to be excited
	 * @param amount amount of activation
	 */
	public void receiveActivationBurst(Set<PamLinkable> linkables, double amount);
	
	/**
	 * Propagate activation from a PamNode to another PamNode along a PamLink.
	 * Excites both link and sink
	 * @param source source of activation
	 * @param sink recipient of activation
	 * @param link link between source and sink
	 * @param amount activation sent
	 * @see #propagateActivationToParents(PamNode)
	 */
	public void propagateActivation(PamNode source, PamNode sink, PamLink link, double amount);

	/**
	 * Propagates activation from a PamNode to its parents.
	 *
	 * @param pamNode The PamNode to propagate activation from.
	 * @see ExcitationTask
	 * @see PropagationTask
	 */
	public void propagateActivationToParents(PamNode pamNode);

	/**
	 * Adds a NodeStructure to the percept.
	 *
	 * @param ns NodeStructure
	 * @see AddToPerceptTask
	 */
	public void addNodeStructureToPercept(NodeStructure ns);
	
	/**
	 * Returns LinkCategory with specified id.
	 * @param id id of LinkCategory sought
	 * @return LinkCategory or null if category does not exist in PAM.
	 */
	public LinkCategory getLinkCategory(int id);
	
	/**
	 * Returns all categories in this Pam
	 * @return Collection of all {@link LinkCategory}
	 */
	public Collection<LinkCategory> getLinkCategories();
	
	/**
	 * Adds a COPY of specified LinkCategory to this {@link PerceptualAssociativeMemory}.
	 * Category must also be a node in order to be added. Node will be of Pam's default type. 
	 * @param cat {@link LinkCategory}
	 * @return Copied LinkCategory actually stored in this PAM.
	 */
	public LinkCategory addLinkCategory(LinkCategory cat);
	
	/**
	 * Returns true if this PAM contains specified PamNode.
	 *
	 * @param node the node
	 * @return true, if successful
	 */
	public boolean containsNode(Node node);
	
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
	public boolean containsLink(Link link);
	
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
	public Node getNode(int id);
	
	/**
	 * Returns copy of the {@link PamNode} with specified {@link ExtendedId} or null
	 * @param id sought {@link ExtendedId}
	 * @return PamNode  Copy of the actual Node.  The copy is of 
	 * the default type of Pam and not necessarily the type of the actual Node. 
	 */
	public Node getNode(ExtendedId id);
	
	/**
	 * 
	 * @param id link's eid
	 * @return Copy of the {@link PamLink} with specified id from this PAM or null. The copy is of 
	 * the default type of Pam and not necessarily the type of the actual Link. 
	 */
	public Link getLink(ExtendedId id);
	
	/**
	 * Returns an unmodifiable collection of the {@link PamNode}s in this PAM as {@link Node}s.
	 *
	 * @return the PamNodes of this PAM
	 */
	public Collection<Node> getNodes();
	
	/**
	 * Returns an unmodifiable collection of the {@link PamLink}s in this PAM as {@link Link}s.
	 *
	 * @return the PamLink of this PAM
	 */
	public Collection<Link> getLinks();
		
} 