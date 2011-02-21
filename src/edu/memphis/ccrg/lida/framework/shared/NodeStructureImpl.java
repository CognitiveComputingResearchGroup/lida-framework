/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;

/**
 * Default implementation of {@link NodeStructure}. The source and sink of a link must be present before
 * it can be added. Links can connect two nodes or a node and a link.   
 * Nodes and links are copied when added.  This prevents having the same node (object) in two different NS.
 * @author Javier Snaider, Ryan J. McCall
 *
 */
public class NodeStructureImpl implements NodeStructure, BroadcastContent, WorkspaceContent, Serializable {

	private static final Logger logger = Logger.getLogger(NodeStructureImpl.class.getCanonicalName());
	
	/*
	 * Standard factory for new objects.  Used to create copies when adding linkables to this NodeStructure
	 */
	private static LidaElementFactory factory;
	
	private static final double DEFAULT_REMOVABLE_THRESHOLD = -1.0;
		
	/*
	 * Threshold for a Linkable to remain in this structure.
	 */
	private double removableThreshold= DEFAULT_REMOVABLE_THRESHOLD;
	
	/*
	 * Nodes contained in this NodeStructure indexed by their id
	 */
	private ConcurrentMap<Integer, Node> nodes;
	
	/*
	 * Links contained in this NodeStructure indexed by their id String.
	 */
	private ConcurrentMap<ExtendedId, Link> links;
	
	/*
	 * Links that each Linkable (Node or Link) has.
	 */
	private ConcurrentMap<Linkable, Set<Link>> linkableMap;

	/*
	 * Default Node type used.
	 */
	private String defaultNodeType;
	
	/*
	 * Default Link type used.
	 */
	private String defaultLinkType;

	/**
	 * Default constructor. Uses the default node and link types of the factory
	 */
	public NodeStructureImpl() {
		factory = LidaElementFactory.getInstance();
		linkableMap = new ConcurrentHashMap<Linkable, Set<Link>>();
		nodes = new ConcurrentHashMap<Integer, Node>();
		links = new ConcurrentHashMap<ExtendedId, Link>();
		defaultNodeType = factory.getDefaultNodeType();
		defaultLinkType = factory.getDefaultLinkType();
	}

	/*
	 * Creates a new NodeStructureImpl with specified default Node type and link Type.
	 * If either is not in the factory the factory's defaults are used.
	 * @param defaultNode kind of node used in this NodeStructure
	 * @param defaultLink kind of link used in this NodeStructure
	 * @see LidaElementFactory
	 */
	NodeStructureImpl(String defaultNode, String defaultLink) {
		this();
		setDefaultNodeType(defaultNode);		
		setDefaultLinkType(defaultLink);
	}
	

	/**
	 * Copy constructor.  Specifies Node and Link types used to copy Node and Links.
	 * Specified types are the default types for the copy.
	 * @param original original NodeStructure
	 * @param defaultNodeType copy's default node type
	 * @param defaultLinkType copy's default node type
	 * @see #copy()
	 */
	protected NodeStructureImpl(NodeStructure original){
		this(original.getDefaultNodeType(), original.getDefaultLinkType());

		// Copy nodes
		Collection<Node> oldNodes = original.getNodes();
		if (oldNodes != null)
			for (Node n : oldNodes)
				nodes.put(n.getId(), getNewNode(n, defaultNodeType));

		// Copy Links but with Source and Sink pointing the old ones.
		Collection<Link> oldLinks = original.getLinks();
		if (oldLinks != null)
			for (Link l : oldLinks) {
				links.put(l.getExtendedId(), generateNewLink(defaultLinkType, l.getSource(), l.getSink(), l.getCategory(), l.getActivation()));
			}

		// Fix Source and Sinks now that all new Nodes and Links have been
		// copied
		for (ExtendedId ids : links.keySet()) {
			Link l = links.get(ids);
			Node lso=l.getSource();
			Linkable lsi=l.getSink();
			
			l.setSource(nodes.get(lso.getId()));

			if (lsi instanceof Node) {
				l.setSink(nodes.get(((Node) lsi).getId()));
			} else {
				l.setSink(links.get(lsi.getExtendedId()));
			}
		}

		// Generate LinkableMap
		Map<Linkable, Set<Link>> oldlinkableMap = original.getLinkableMap();
		if (oldlinkableMap != null) {
			Set<Linkable> oldKeys = oldlinkableMap.keySet();
			if (oldKeys != null) {
				for (Linkable l : oldKeys) {
					Set<Link> newLinks = null;
					Set<Link> llinks = oldlinkableMap.get(l);
					if (llinks != null) {
						newLinks = new HashSet<Link>();
						for (Link link : llinks) {
							newLinks.add(links.get(link.getExtendedId()));
						}
					} else {
						newLinks = null;
					}
					if (l instanceof Link) {
						linkableMap.put(links.get(l.getExtendedId()), newLinks);
					} else if (l instanceof Node) {
						this.linkableMap.put(nodes.get(((Node) l).getId()),
								newLinks);
					}
				}
			}
		}
	}
	
	/**
	 * @param nodeType
	 *            the defaultNode to set
	 */
	@Override
	public void setDefaultNodeType(String nodeType) {
		if(factory.containsNodeType(nodeType))
			this.defaultNodeType = nodeType;
		else
			logger.log(Level.WARNING, "Factory does not contain specified node type!  Must specify all types in factoriesData.xml", LidaTaskManager.getCurrentTick());
	}

	/**
	 * @param linkType
	 *            the defaultLink to set
	 */
	@Override
	public void setDefaultLinkType(String linkType) {	
		if(factory.containsLinkType(linkType))
			this.defaultLinkType = linkType;
		else
			logger.log(Level.WARNING, "Factory does not contain specified link type!  Must specify all types in factoriesData.xml", LidaTaskManager.getCurrentTick());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#addLink(edu.memphis.ccrg.lida
	 * .shared.Link)
	 */
	@Override
	public synchronized Link addDefaultLink(Link l) {		
		double newActiv = l.getActivation();
		Link oldLink = links.get(l.getExtendedId());
		if (oldLink != null) { // if the link already exists in this node structure
			//update activation
			if (oldLink.getActivation() < newActiv) 
				oldLink.setActivation(newActiv);
			return oldLink;
		}
		Node source = l.getSource();
		Linkable sink = l.getSink();

		Node newSource = null;
		Linkable newSink = null;

		//If link's source is a node, but that node
		//in not in this nodestructure, return null
		newSource = nodes.get(source.getId());
		if (newSource == null) {
			logger.log(Level.WARNING, "Source node is not present in this NodeStructure", LidaTaskManager.getCurrentTick());
			return null;
		}
		
		if (sink instanceof Node) {
			Node snode = (Node) sink;
			newSink = nodes.get(snode.getId());
			if (newSink == null) {
				logger.log(Level.WARNING, "Sink is not present in this NodeStructure", LidaTaskManager.getCurrentTick());
				return null;
			}
		}else{
			newSink = links.get(sink.getExtendedId());
			if (newSink == null) {
				logger.log(Level.WARNING, "Sink is not present in this NodeStructure", LidaTaskManager.getCurrentTick());
				return null;
			}
		}
		return generateNewLink(defaultLinkType, newSource, newSink, l.getCategory(), newActiv);
	}

	@Override
	public synchronized Link addLink(ExtendedId sourceId, ExtendedId sinkId, LinkCategory category, double activation) {
		Node source = getNode(sourceId);
		Linkable sink = getLinkable(sinkId);
		if (source == null || sink == null) {
			logger.log(Level.WARNING, "Source and/or Sink are not present in this NodeStructure", LidaTaskManager.getCurrentTick());
			return null;
		}
		return generateNewLink(defaultLinkType, source, sink, category, activation);
	}

	@Override
	public synchronized Link addLink(int sourceId, ExtendedId sinkId, LinkCategory category,
			double activation) {
		Node source = getNode(sourceId);
		Linkable sink = getLinkable(sinkId);
		if (source == null || sink == null) {
			logger.log(Level.WARNING, "Source and/or Sink are not present in this NodeStructure", LidaTaskManager.getCurrentTick());
			return null;
		}
		return generateNewLink(defaultLinkType, source, sink, category, activation);
	}

	@Override
	public synchronized Link addLink(int sourceId, int sinkId, LinkCategory category,
			double activation) {
		Node source = getNode(sourceId);
		Linkable sink = getNode(sinkId);
		if (source == null || sink == null) {
			logger.log(Level.WARNING, "Source and/or Sink are not present in this NodeStructure", LidaTaskManager.getCurrentTick());
			return null;
		}
		return generateNewLink(defaultLinkType, source, sink, category, activation);
	}

	/**
	 * @param newSource
	 * @param newSink
	 * @param type
	 * @param activation
	 */
	private Link generateNewLink(String linkType, Node newSource, Linkable newSink,
								 LinkCategory type, double activation) {
		Link newLink = getNewLink(linkType, newSource, newSink, type);
		newLink.setActivation(activation);
		links.put(newLink.getExtendedId(), newLink);
		if(!linkableMap.containsKey(newLink))
			linkableMap.put(newLink, new HashSet<Link>());

		Set<Link> tempLinks = linkableMap.get(newSource);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkableMap.put(newSource, tempLinks);
		}
		tempLinks.add(newLink);

		tempLinks = linkableMap.get(newSink);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkableMap.put(newLink, tempLinks);
		}
		tempLinks.add(newLink);

		return newLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#addLinkSet(java.util.Set)
	 */
	@Override
	public Collection<Link> addDefaultLinks(Collection<Link> links) {
		Collection<Link> copiedLinks = new ArrayList<Link>();
		for (Link l : links){
			copiedLinks.add(addDefaultLink(l));
		}
		return copiedLinks;
	}
	
	@Override
	public Node addDefaultNode(Node n){
		return addNode(n, defaultNodeType);
	}


//	@Override
//	public synchronized Link addLink(Link l, String linkType) {
//		if(factory.containsLinkType(linkType) == false){
//			logger.log(Level.WARNING, "Tried to add link of type " + linkType +
//					" but factory does not contain that node type.  Make sure the node type is defined in " +
//					" factoriesData.xml", LidaTaskManager.getCurrentTick());
//			return null;
//		}
//		//TODO use other link method
//		Link link;		
//		return link;
//	}
	
	@Override
	public synchronized Node addNode(Node n, String nodeType) {
		if(factory.containsNodeType(nodeType) == false){
			logger.log(Level.WARNING, "Tried to add node of type " + nodeType +
					" but factory does not contain that node type.  Make sure the node type is defined in " +
					" factoriesData.xml", LidaTaskManager.getCurrentTick());
			return null;
		}
		
		Node node = nodes.get(n.getId());
		if (node == null) {
			node = getNewNode(n, nodeType);
			nodes.put(node.getId(), node);
			linkableMap.put(node, new HashSet<Link>());
		} else {
			double newActiv = n.getActivation();
			if (node.getActivation() < newActiv) {
				node.setActivation(newActiv);
			}
		}
		return node;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#addNodes(java.util.Set)
	 */
	@Override
	public Collection<Node> addDefaultNodes(Collection<Node> nodesToAdd) {
		Collection<Node> copiedNodes = new ArrayList<Node>();
		for (Node n : nodesToAdd){
			copiedNodes.add(addNode(n, defaultNodeType ));
		}
		return copiedNodes;
	}

	/**
	 * This method can be overwritten to customize the Node Creation.
	 * 
	 * @param n
	 *            The original Node
	 * @return The Node to be used in this NodeStructure
	 */
	protected Node getNewNode(Node n, String factoryName) {
		return factory.getNode(n, factoryName);
	}

	/**
	 * This method can be overwritten to customize the Link Creation. some of
	 * the parameter could be redundant in some cases.
	 * 
	 * @param source
	 *            The new source
	 * @param sink
	 *            The new sink
	 * @param category
	 *            the type of the link
	 * @return The link to be used in this NodeStructure
	 */
	protected Link getNewLink(String linkType, Node source, Linkable sink, LinkCategory category) {
		return factory.getLink(linkType, source, sink, category);
	}

	@Override
	public NodeStructure copy() {
		logger.log(Level.FINER, "Copying NodeStructure " + this, LidaTaskManager.getCurrentTick());
		return new NodeStructureImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#deleteLink(edu.memphis.ccrg.lida.shared.Link)
	 */
	@Override
	public void removeLink(Link l) {
		removeLinkable(l);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#deleteLinkable(edu.memphis
	 * .ccrg.lida.shared.Linkable)
	 */
	@Override
	public synchronized void removeLinkable(Linkable n) {
		Set<Link> tempLinks = linkableMap.get(n);
		Set<Link> otherLinks;
		Linkable other;

		if (tempLinks != null) {
			for (Link l : tempLinks) {// for all of the links connected to n
				other = l.getSink();
				if (!other.equals(n)) {
					otherLinks = linkableMap.get(other);
					if (otherLinks != null){
						otherLinks.remove(l);
					}else{
						logger.log(Level.WARNING, "Expected other end of link " + other.getLabel() + 
								" to have link " + l.toString(), LidaTaskManager.getCurrentTick());						
					}
				}else{
					other = l.getSource();
					otherLinks = linkableMap.get(other);
					if (otherLinks != null){
						otherLinks.remove(l);
					}else{
						logger.log(Level.WARNING, "Expected other end of link " + other.getLabel() + 
								" to have link " + l.toString(), LidaTaskManager.getCurrentTick());
					}
				}
			}
		}

		linkableMap.remove(n);// finally remove the linkable and its links
		if (n instanceof Node) {
			nodes.remove(((Node) n).getId());
		} else if (n instanceof Link) {
			Link aux = links.get(n.getExtendedId());
			Set<Link> sourceLinks = linkableMap.get(aux.getSource());
			Set<Link> sinkLinks = linkableMap.get(aux.getSink());
			links.remove(n.getExtendedId());

			if (sourceLinks != null)
				sourceLinks.remove(aux);

			if (sinkLinks != null)
				sinkLinks.remove(aux);
		}

	}
	@Override
	public void removeLinkable(ExtendedId id) {
		if(id.isNodeId()){
			removeLinkable(nodes.get(id.getSourceNodeId()));
		}else{
			removeLinkable(links.get(id));
		}
	}

	@Override
	public void removeNode(Node n) {
		removeLinkable(n);
	}

	@Override
	public Link getLink(ExtendedId ids) {
		return links.get(ids);
	}

	@Override
	public Collection<Link> getLinks() {
		Collection<Link> aux = links.values();
		if (aux == null) {
			return null;
		} else {
			return Collections.unmodifiableCollection(aux);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#getLinks(edu.memphis.ccrg.
	 * lida.shared.Linkable)
	 */
	@Override
	public Set<Link> getAttachedLinks(Linkable lnk) {
		if (lnk == null) 
			return null;

		Set<Link> aux = linkableMap.get(lnk);
		if (aux == null)
			return null;
		else
			return Collections.unmodifiableSet(aux); // This returns the
		// set of Links but it prevents to be modified
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#getLinks(edu.memphis.ccrg.
	 * lida.shared.Linkable, edu.memphis.ccrg.lida.shared.LinkType)
	 */
	@Override
	public Set<Link> getAttachedLinks(Linkable lnk, LinkCategory category) {
		Set<Link> temp = linkableMap.get(lnk);
		Set<Link> result = new HashSet<Link>();
		if (temp != null) {
			for (Link l : temp) {
				if (l.getCategory() == category)// add only desired category
					result.add(l);
			}// for each link
		}// result != null
		return result;
	}
	
	@Override
	public Set<Link> getLinks(LinkCategory category) {
		Set<Link> result = new HashSet<Link>();
		if (links != null) {
			for (Link l : links.values()) {
				if (l.getCategory() == category) {
					result.add(l);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#getNodes()
	 */
	@Override
	public Collection<Node> getNodes() {
		Collection<Node> aux = nodes.values();
		if (aux == null)
			return null;
		else
			return Collections.unmodifiableCollection(aux);

	}

	/**
	 * 
	 */
	@Override
	public Map<Linkable, Set<Link>> getLinkableMap() {
		return Collections.unmodifiableMap(linkableMap);
	}

	@Override
	public Node getNode(int id) {
		return nodes.get(id);
	}

	@Override
	public Node getNode(ExtendedId id) {
		if (id == null) {
			return null;
		}
		return nodes.get(id.getSourceNodeId());
	}

	@Override
	public int getLinkCount() {
		return links.size();
	}

	@Override
	public int getNodeCount() {
		return nodes.size();
	}
	@Override
	public int getLinkableCount() {
		return linkableMap.size();
	}

	@Override
	public void mergeWith(NodeStructure ns) {
		addDefaultNodes(ns.getNodes());
		Collection<Link> cl = ns.getLinks();
		boolean pending = true;
		while (pending) {
			pending = false;
			for (Link l : cl) {
				if (addDefaultLink(l) == null) {
					pending = true;
				}
			}
		}
		// TODO: Must add links differently than above statement.
	}

	public Set<Link> getLinksByCategory(LinkCategory type) {
		Set<Link> result = new HashSet<Link>();
		for (Link l : links.values()) {
			if (l.getCategory() == type)
				result.add(l);
		}// for

		return result;
	}

	@Override
	public void clearLinks(){
		for(Link l: links.values())
			removeLink(l);
	}
	
	@Override
	public void clearNodeStructure(){
		for(Linkable l: linkableMap.keySet())
			removeLinkable(l);		
	}

	@Override
	public boolean containsLink(Link l) {
		return links.containsKey(l.getExtendedId());
	}

	@Override
	public boolean containsNode(Node n) {
		return nodes.containsKey(n.getId());
	}

	@Override
	public boolean containsLink(ExtendedId id) {
		return links.containsKey(id);
	}

	@Override
	public boolean containsLinkable(Linkable l) {
		return linkableMap.containsKey(l);
	}

	@Override
	public boolean containsLinkable(ExtendedId id) {
		return (containsLink(id)||containsNode(id));
	}

	@Override
	public boolean containsNode(int id) {
		return nodes.containsKey(id);
	}

	@Override
	public boolean containsNode(ExtendedId id) {
		return id.isNodeId() && nodes.containsKey(id.getSourceNodeId());
	}
	
	@Override
	public Linkable getLinkable(ExtendedId ids) {
		if(ids.isNodeId()){
			return getNode(ids);
		}else{
			return getLink(ids);
		}
	}

	@Override
	public String getDefaultLinkType() {
		return defaultLinkType;
	}

	@Override
	public String getDefaultNodeType() {
		return defaultNodeType;
	}

	@Override
	public String toString() {
		String aux = "NODES\n";
		for(Node n: nodes.values())
			aux = aux + (n.getLabel() + "--" + n.getId() + "\n");
		
		aux = aux + "LINKS\n";
		for(Link l: links.values())
			aux = aux + (l.getLabel() + "--" + l.getExtendedId() + "\n");
		return aux;
	}

	@Override
	public Collection<Linkable> getLinkables() {
		return Collections.unmodifiableCollection(linkableMap.keySet());
	}

	@Override
	public void decayNodeStructure(long ticks) {
		for(Linkable lnk: linkableMap.keySet()){
			Activatible a = (Activatible) lnk;
			a.decay(ticks);
			if (a.isRemovable()){
				removeLinkable(lnk);
			}
		}	
	}

	@Override
	public double getLowerActivationBound() {
		return removableThreshold;
	}

	@Override
	public void setLowerActivationBound(double lowerActivationBound) {
		this.removableThreshold = lowerActivationBound;
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getParentLinkMap(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public Map<Node, Link> getParentLinkMap(Node child){
		Map<Node, Link> parentLinkMap = new HashMap<Node, Link>();
		Set<Link> candidateLinks = getLinkableMap().get(child);
		if(candidateLinks != null){
			for(Link link: candidateLinks){
				//Sinks receive activation and are "higher than" node n, i.e. sink are the parents of this node.
				Linkable sink = link.getSink(); 
				if(!sink.equals(child)){
					parentLinkMap.put((Node) sink, link);	
				}
			}
		}
		return parentLinkMap;
	}
	
	/**
	 * Returns true if two NodeStructures are equal, else returns false
	 * Two NodeStructures are equal if they have the same nodes and links
	 * @param ns1 first nodestructure
	 * @param ns2 second nodestructure
	 * @return boolean
	 */
	public static boolean compareNodeStructures(NodeStructure ns1,NodeStructure ns2) {
		Collection<Node> nodes = ns1.getNodes();
		Collection<Link> links = ns1.getLinks();
		
		for (Node n : nodes)
			if (!ns2.containsNode(n))
				return false;

		for (Link l : links)
			if (!ns2.containsLink(l))
				return false;
			
		return true;		
	}
}
