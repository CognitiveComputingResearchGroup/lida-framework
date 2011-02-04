/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A NodeStructure holds a collection of {@link Node}s an {@link Link}s. It is used
 * as a main conceptual representation among LIDA modules.
 * 
 * @author Javier Snaider, Ryan J. McCall
 * @see NodeStructureImpl
 */
public interface NodeStructure {

	/**
	 * Copies specified Link and then adds the copy to this NodeStructure. If Link with the same 
	 * id already exists then the old Link's activation is updated.
	 * @param l Link to copy and add.
	 * @return the copied Link that is actually stored in this NodeStructure, or the existing link.
	 */
	public Link addLink(Link l);
	
	/**
	 * Copies specified Links and then adds the copies to this NodeStructure.
	 * If any Link with the same 
	 * id already exists then the old Link's activation is updated.
	 * @param links Links to copy and add.
	 * @return the copied Links that are actually stored in this NodeStructure, or any existing links.
	 */
	public Collection<Link> addLinks(Collection<Link> links);

	/**
	 * TODO could we add the same link multiple times?
	 * Creates and adds a new Link with specified attributes.  Source and sink must
	 * already be in this NodeStructure.
	 * @param idSource {@link ExtendedId} of link's source
	 * @param idSink {@link ExtendedId} of link's sink
	 * @param type Link's {@link LinkCategory}
	 * @param activation initial link activation
	 * @return created Link or null if either source or sink are not present. 
	 */
	public Link addLink(ExtendedId idSource, ExtendedId idSink,
			LinkCategory type, double activation);

	/**
	 * Creates and adds a new Link with specified attributes.  Source and sink must
	 * already be in this NodeStructure.
	 * @param idSource id of link's source
	 * @param idSink {@link ExtendedId} of link's sink
	 * @param type Link's {@link LinkCategory}
	 * @param activation initial link activation
	 * @return created Link or null if either source or sink are not present. 
	 */
	public Link addLink(int idSource, ExtendedId idSink,
			LinkCategory type, double activation);

	/**
	 * Creates and adds a new Link with specified attributes.  Source and sink must
	 * already be in this NodeStructure.
	 * @param idSource id of link's source
	 * @param idSink id of link's sink
	 * @param type Link's {@link LinkCategory}
	 * @param activation initial link activation
	 * @return created Link or null if either source or sink are not present. 
	 */
	public Link addLink(int idSource, int idSink,
			LinkCategory type, double activation);

	/**
	 * Copies specified Node and adds the copy to this NodeStructure.  If Node with the same
	 * id already exists the the old node's activation is update.
	 * @param n Node to add.
	 * @return The copied Node that is stored in this NodeStructure or the existing, updated, Node already there.
	 */
	public Node addNode(Node n);

	/**
	 * Add a Node of a specified factory type to this NodeStructure
	 * @param n Node
	 * @param factoryNodeType name of node's type in the factory
	 * @return copy of node actually added.
	 */
	public Node addNode(Node n, String factoryNodeType);

	/**
	 * @return copied and/or updated nodes that are now present in this nodestructure
	 * @see #addNode(Node)
	 * @param nodes Node to be added.
	 */
	public Collection<Node> addNodes(Collection<Node> nodes);

	/**
	 * Removes specified {@link Link} if present.
	 * @param l Link to remove.
	 */
	public void removeLink(Link l);

	/**
	 * Removes specified {@link Node} if present.
	 * @param n Node to remove.
	 */
	public void removeNode(Node n);

	/**
	 * Removes specified {@link Linkable} if present.
	 * @param l Linkable to remove.
	 */
	public void removeLinkable(Linkable l);

	/**
	 * Removes {@link Linkable} with specified {@link ExtendedId} if present.
	 * @param id ExtendedId of Linkable to remove.
	 */
	public void removeLinkable(ExtendedId id);

	/**
	 * Removes all links from this {@link NodeStructure}
	 */
	public void clearLinks();

	/**
	 * Removes all nodes and links from this nodestructure
	 */
	public void clearNodeStructure();

	/**
	 * Returns whether this NodeStructure contains specified Node.
	 * @param n Node checked for.
	 * @return true if contains a Node with the same id.
	 */
	public boolean containsNode(Node n);

	/**
	 * Returns whether this NodeStructure contains Node with specified id.
	 * @param id id of Node checked for.
	 * @return true if contains a Node with the same id.
	 */
	public boolean containsNode(int id);

	/**
	 * Returns whether this NodeStructure contains Node with specified ExtendedId.
	 * @param id ExtendedId of Node checked for.
	 * @return true if contains a Node with the same ExtendedId.
	 */
	public boolean containsNode(ExtendedId id);

	/**
	 * Returns whether this NodeStructure contains specified Link.
	 * @param l Link checked for.
	 * @return true if contains a Link with the same id.
	 */
	public boolean containsLink(Link l);
	
	/**
	 * Returns whether this NodeStructure contains Link with specified {@link ExtendedId}.
	 * @param id Link checked for.
	 * @return true if contains a {@link Link} with the same {@link ExtendedId}.
	 */
	public boolean containsLink(ExtendedId id);

	/**
	 * Returns whether this NodeStructure contains specified {@link Linkable}.
	 * @param l {@link Linkable} checked for.
	 * @return true if contains a {@link Linkable} with the same {@link ExtendedId}.
	 */
	public boolean containsLinkable(Linkable l);
	
	/**
	 * Returns whether this NodeStructure contains {@link Linkable} with specified {@link ExtendedId}.
	 * @param id {@link Linkable} checked for.
	 * @return true if contains a {@link Linkable} with the same {@link ExtendedId}.
	 */
	public boolean containsLinkable(ExtendedId id);
	
	/**
	 * Merges specified NodeStructure into this one.  Adds all nodes and adds all Links.
	 * Activations are updated if Linkable is already present.
	 * @param ns NodeStructure
	 */
	public void mergeWith(NodeStructure ns);

	/**
	 * Returns a copy of this {@link NodeStructure}
	 * @return {@link NodeStructure}
	 */
	public NodeStructure copy();
	
	/**
	 * Decays the {@link Linkable}s of this {@link NodeStructure}.
	 * 
	 * @param ticks
	 *            the number of ticks to decay for. 
	 */
	public void decayNodeStructure(long ticks);
	
	/**
	 * Gets lowerActivationBound
	 * @return Amount of activation a {@link Linkable} must have in order to remain in this NodeStructure
	 * after being decayed.
	 */
	public double getLowerActivationBound();
	
	/**
	 * Sets lowerActivationBound
	 * @param lowerActivationBound Amount of activation a {@link Linkable} must have in order to remain in this NodeStructure
	 * after being decayed.
	 */
	public void setLowerActivationBound(double lowerActivationBound);

	/**
	 * Gets Link with specified ExtendedId if present.
	 * @param ids {@link ExtendedId} of sought Link.
	 * @return Link or null if no Link exists.
	 */
	public Link getLink(ExtendedId ids);

	/**
	 * Returns the Links of this NodeStructure
	 * @return an unmodifiable Collection of all Links
	 */
	public Collection<Link> getLinks();
	
	/**
	 * Returns all Links of this NodeStructure with specified {@link LinkCategory}
	 * @param cat LinkCategory to search for. 
	 * @return Links having specified {@link LinkCategory}
	 */
	public Set<Link> getLinks(LinkCategory cat);

	/**
	 * Gets all {@link Link}s connected to specified Linkable.
	 * @param l Linkable to find Links to and from.
	 * @return an unmodifiable Set of all Links connected to specified Linkable.
	 */
	public Set<Link> getConnectedLinks(Linkable l);

	/**
	 * Gets all {@link Link}s connected to specified Linkable with specified {@link LinkCategory}
	 * @param lnk a Linkable
	 * @param cat LinkCategory
	 * @return Links
	 */
	public Set<Link> getConnectedLinks(Linkable lnk, LinkCategory cat);

	/**
	 * Returns node with specified id
	 * @param id id of node
	 * @return Node with specified id or null if not present
	 */
	public Node getNode(int id);

	/**
	 * Returns node with specified ExtendedId
	 * @param eid ExtendedId of node
	 * @return Node with specified ExtendedId or null if not present
	 */
	public Node getNode(ExtendedId eid);

	/**
	 * Returns all Nodes
	 * @return All nodes of this nodestructures
	 */
	public Collection<Node> getNodes();

	/**
	 * Gets linkable with specified {@link ExtendedId}
	 * @param eid {@link ExtendedId}
	 * @return a Linkable
	 */
	public Linkable getLinkable(ExtendedId eid);

	/**
	 * Returns all Linkables, all Nodes and Links, in this {@link NodeStructure}
	 * @return all Linkables
	 */
	public Collection<Linkable> getLinkables();

	/**
	 * Returns linkableMap
	 * @return An unmodifiable Map of the linkables of this NodeStructure and their links.s
	 */
	public Map<Linkable, Set<Link>> getLinkableMap();

	/**
	 * Returns a count of nodes
	 * @return number of nodes
	 */
	public int getNodeCount();

	/**
	 * returns a count of links
	 * @return number of links
	 */
	public int getLinkCount();

	/**
	 * Returns count of linkables
	 * @return number of linkables
	 */
	public int getLinkableCount();

//TODO what is the point of the attribute again?  It is only being used in the impl then the ndoestucture is being copied
	// shouldn't it be used when adding new nodes? creating new ndoes?
	public String getDefaultNodeType();
	
	public void setDefaultNode(String nodeName);

	public String getDefaultLinkType();

	//TODO class??
	public void setDefaultLink(String linkName);

	

}