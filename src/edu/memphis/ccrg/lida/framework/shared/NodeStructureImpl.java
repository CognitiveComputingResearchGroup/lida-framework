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
import edu.memphis.ccrg.lida.pam.PamLink;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;

/**
 * Default implementation of {@link NodeStructure}. The source and sink of a
 * link must be present before it can be added. Links can connect two nodes (simple link) or can connect
 * a node and another SIMPLE link. Nodes and links are copied when added. This prevents having
 * the same node (object) in two different NS.
 * 
 * @see ExtendedId
 * @author Javier Snaider, Ryan J. McCall
 * 
 */
public class NodeStructureImpl implements NodeStructure, BroadcastContent,
		WorkspaceContent, Serializable {

	private static final Logger logger = Logger
			.getLogger(NodeStructureImpl.class.getCanonicalName());

	/*
	 * Standard factory for new objects. Used to create copies when adding
	 * linkables to this NodeStructure
	 */
	private static LidaElementFactory factory = LidaElementFactory.getInstance();

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
		linkableMap = new ConcurrentHashMap<Linkable, Set<Link>>();
		nodes = new ConcurrentHashMap<Integer, Node>();
		links = new ConcurrentHashMap<ExtendedId, Link>();
		defaultNodeType = factory.getDefaultNodeType();
		defaultLinkType = factory.getDefaultLinkType();
	}

	/*
	 * Creates a new NodeStructureImpl with specified default Node type and link
	 * Type. If either is not in the factory the factory's defaults are used.
	 * 
	 * @param defaultNode kind of node used in this NodeStructure
	 * 
	 * @param defaultLink kind of link used in this NodeStructure
	 * 
	 * @see LidaElementFactory
	 */
	public NodeStructureImpl(String defaultNode, String defaultLink) {
		this();
		if(factory.containsNodeType(defaultNode)){
			this.defaultNodeType = defaultNode;
		}else{
			logger.log(Level.SEVERE, "Unsupported Node type: " + defaultNode, LidaTaskManager.getCurrentTick());
			throw new IllegalArgumentException();
		}
		
		if(factory.containsLinkType(defaultLink)){
			this.defaultLinkType = defaultLink;
		}else{
			logger.log(Level.SEVERE, "Unsupported Link type: " + defaultLink, LidaTaskManager.getCurrentTick());
			throw new IllegalArgumentException();
		}
	}

	/**
	 * Copy constructor. Specifies Node and Link types used to copy Node and
	 * Links. Specified types are the default types for the copy.
	 * 
	 * @param original
	 *            original NodeStructure
	 * @see #mergeWith(NodeStructure)
	 */
	public NodeStructureImpl(NodeStructure original) {
		this(original.getDefaultNodeType(), original.getDefaultLinkType());
		internalMerge(original);
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#copy()
	 */
	@Override
	public NodeStructure copy() {
		logger.log(Level.FINER, "Copying NodeStructure " + this,
				LidaTaskManager.getCurrentTick());
		return new NodeStructureImpl(this);
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
		double removalThreshold = l.getActivatibleRemovalThreshold();
		Link oldLink = links.get(l.getExtendedId());
		if (oldLink != null) { // if the link already exists in this node structure			
			oldLink.setActivatibleRemovalThreshold(removalThreshold);
			//TODO update activation? 
			if (oldLink.getActivation() < newActiv){
				oldLink.setActivation(newActiv);
			}
			return oldLink;
		}
		Node source = l.getSource();
		Linkable sink = l.getSink();

		Node newSource = null;
		Linkable newSink = null;

		// If link's source is a node, but that node in not in this nodestructure, return null
		newSource = nodes.get(source.getId());
		if (newSource == null) {
			logger.log(Level.FINER,
					"Source: " + source + " is not present in this NodeStructure, Link will not be added.",
					LidaTaskManager.getCurrentTick());
			return null;
		}

		if (sink instanceof Node) {
			Node snode = (Node) sink;
			newSink = nodes.get(snode.getId());
			if (newSink == null) {
				logger.log(Level.FINER, 
						"Sink: " + sink + " is not present in this NodeStructure, Link will not be added.",
						LidaTaskManager.getCurrentTick());
				return null;
			}
		} else {
			newSink = links.get(sink.getExtendedId());
			if (newSink == null) {
				logger.log(Level.FINER,
						"Sink: " + sink + " is not present in this NodeStructure, Link will not be added.",
						LidaTaskManager.getCurrentTick());
				return null;
			}
		}
		return generateNewLink(defaultLinkType, newSource, newSink, l
				.getCategory(), newActiv, removalThreshold, l.getGroundingPamLink());
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#addDefaultLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId, edu.memphis.ccrg.lida.framework.shared.ExtendedId, edu.memphis.ccrg.lida.framework.shared.LinkCategory, double, double)
	 */
	@Override
	public synchronized Link addDefaultLink(ExtendedId sourceId, ExtendedId sinkId,
			LinkCategory category, double activation, double removalThreshold) {
		if(sourceId.equals(sinkId)){
			throw new IllegalArgumentException("Cannot create a link with the same source and sink.");
		}
		if(sinkId.isComplexLink()){
			logger.log(Level.WARNING, "Sink cannot be a complex link. Must be a node or simple link.", 
						LidaTaskManager.getCurrentTick());
			return null;
		}
		Node source = getNode(sourceId);
		Linkable sink = getLinkable(sinkId);
		if (source == null || sink == null) {
			logger.log(Level.WARNING,
					"Source and/or Sink are not present in this NodeStructure",
					LidaTaskManager.getCurrentTick());
			return null;
		}
		return generateNewLink(defaultLinkType, source, sink, category,
				activation, removalThreshold, null);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#addDefaultLink(int, edu.memphis.ccrg.lida.framework.shared.ExtendedId, edu.memphis.ccrg.lida.framework.shared.LinkCategory, double, double)
	 */
	@Override
	public synchronized Link addDefaultLink(int sourceId, ExtendedId sinkId,
			LinkCategory category, double activation, double removalThreshold) {
		if(sinkId.isNodeId() && sourceId == sinkId.getSourceNodeId()){
			throw new IllegalArgumentException("Cannot create a link with the same source and sink.");
		}
		if(sinkId.isComplexLink()){
			logger.log(Level.WARNING, "Sink cannot be a complex link. Must be a node or simple link.", 
						LidaTaskManager.getCurrentTick());
			return null;
		}
		Node source = getNode(sourceId);
		Linkable sink = getLinkable(sinkId);
		if (source == null || sink == null) {
			logger.log(Level.WARNING,
					"Source and/or Sink are not present in this NodeStructure",
					LidaTaskManager.getCurrentTick());
			return null;
		}
		return generateNewLink(defaultLinkType, source, sink, category,
				activation, removalThreshold, null);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#addDefaultLink(int, int, edu.memphis.ccrg.lida.framework.shared.LinkCategory, double, double)
	 */
	@Override
	public synchronized Link addDefaultLink(int sourceId, int sinkId,
			LinkCategory category, double activation, double removalThreshold) {
		if(sourceId == sinkId){
			throw new IllegalArgumentException("Cannot create a link with the same source and sink.");
		}
		Node source = getNode(sourceId);
		Linkable sink = getNode(sinkId);
		if (source == null || sink == null) {
			logger.log(Level.WARNING,
					"Source and/or Sink are not present in this NodeStructure",
					LidaTaskManager.getCurrentTick());
			return null;
		}
		return generateNewLink(defaultLinkType, source, sink, category,
				activation, removalThreshold, null);
	}
	
	@Override
	public synchronized Link addDefaultLink(Node source, Linkable sink, LinkCategory category, double activation, double removalThreshold){
		return addDefaultLink(source.getId(), sink.getExtendedId(), category, activation, removalThreshold);
	}

	/*
	 * Generates a new Link with specified type and values.
	 * @param newSource
	 * @param newSink
	 * @param type
	 * @param activation
	 * @param removalThreshold
	 */
	private Link generateNewLink(String linkType, Node newSource,
			Linkable newSink, LinkCategory type, double activation,
			double removalThreshold, PamLink groundingPamLink) {
		Link newLink = getNewLink(linkType, newSource, newSink, type);
		newLink.setActivation(activation);
		newLink.setActivatibleRemovalThreshold(removalThreshold);
		newLink.setGroundingPamLink(groundingPamLink);

		links.put(newLink.getExtendedId(), newLink);
		if (!linkableMap.containsKey(newLink)){
			linkableMap.put(newLink, new HashSet<Link>());
		}

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
		//Add simple links
		for (Link l : links) {
			if(l.isSimpleLink()){
				copiedLinks.add(addDefaultLink(l));
			}
		}
		
		//Add complex links
		for (Link l : links) {
			if(l.isSimpleLink() == false){
				copiedLinks.add(addDefaultLink(l));
			}
		}
		return copiedLinks;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#addDefaultNode(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public Node addDefaultNode(Node n) {
		return addNode(n, defaultNodeType);
	}

	// @Override
	// public synchronized Link addLink(Link l, String linkType) {
	// if(factory.containsLinkType(linkType) == false){
	// logger.log(Level.WARNING, "Tried to add link of type " + linkType +
	// " but factory does not contain that node type.  Make sure the node type is defined in "
	// +
	// " factoriesData.xml", LidaTaskManager.getCurrentTick());
	// return null;
	// }
	// //TODO use other link method
	// Link link;
	// return link;
	// }

//	@Override
//	public synchronized Node addNode(Node n, String nodeType) {
//		if (factory.containsNodeType(nodeType) == false) {
//			logger.log(Level.WARNING, "Cannot add node of type "
//									+ nodeType
//									+ " because factory doesn't contain that node type.  Make sure the node type is defined in "
//									+ " factoriesData.xml", LidaTaskManager
//									.getCurrentTick());
//			return null;
//		}
//		
//		//Check instance of default node.
//		String defaultCl = factory.getNodeLinkableDef(this.defaultNodeType).getClassName();
//		Class<?> cls = Class.forName(defaultCl);
//		if(cls.isInstance(n)){
//			//TODO check
//			String cl = factory.getNodeLinkableDef(nodeType).getClassName();
//			Class<?> clss = Class.forName(cl);
//			if (clss.isInstance(n)){
//				Node node = nodes.get(n.getId());
//				if (node == null) {
//					node = n.copy();
//					nodes.put(node.getId(), node);
//					linkableMap.put(node, new HashSet<Link>());
//					return node;
//				}else {
//					Node existing = this.getNode(n.getId());
//					//TODO
////					existing.updateValues(n);
//					return existing;
//				}
//			}else{ 	//have factory create node
//				Node node = nodes.get(n.getId());
//				if (node == null) {
//					node = getNewNode(n, nodeType);
//					nodes.put(node.getId(), node);
//					linkableMap.put(node, new HashSet<Link>());
//					return node;
//				}else {
//					//TODO
////					Node existing = this.getNode(n.getId());
////					existing.updateValues(n);
//					node.setActivatibleRemovalThreshold(n.getActivatibleRemovalThreshold());
//					double newActiv = n.getActivation();
//					if (node.getActivation() < newActiv) {
//						node.setActivation(newActiv);
//					}
//					return node;
//				}
//			}
//			
//		}else{
//			logger.log(Level.WARNING, "Cannot add a node that doesn't have default node type as a parent", LidaTaskManager.getCurrentTick());
//			return null;
//		}
////
////		Node node = nodes.get(n.getId());
////		if (node == null) {
////			node = getNewNode(n, nodeType);
////			nodes.put(node.getId(), node);
////			linkableMap.put(node, new HashSet<Link>());
////		} else {
////			double newActiv = n.getActivation();
////			if (node.getActivation() < newActiv) {
////				node.setActivation(newActiv);
////			}
////		}
////		return node;
//	} 
	
	/* 
	 * @param n
	 * @param nodeType
	 * @return
	 */
	private synchronized Node addNode(Node n, String nodeType) {		
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
			//TODO Is this what we want? or just set with no check?
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
	public Collection<Node> addDefaultNodes(Collection<Node> nodes) {
		Collection<Node> storedNodes = new ArrayList<Node>();
		for (Node n : nodes) {
			Node stored = addNode(n, defaultNodeType);
			storedNodes.add(stored);
		}
		return storedNodes;
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
	protected Link getNewLink(String linkType, Node source, Linkable sink,
			LinkCategory category) {
		return factory.getLink(linkType, source, sink, category);
	}
	
	@Override
	public void mergeWith(NodeStructure ns) {
		internalMerge(ns);
	}

	/*
	 * This allows subclasses of NodeStructure to override merge but still gives 
	 * this class a merge to be called from the constructor.
	 * TODO do this for addNode and AddDefaultLink??????
	 * @param ns
	 */
	private void internalMerge(NodeStructure ns) {
		//Add nodes
		addDefaultNodes(ns.getNodes());
		
		Collection<Link> cl = ns.getLinks();
		//Add simple links
		for (Link l : cl) {
			if(l.isSimpleLink()){
				addDefaultLink(l);
			}
		}
		
		//Add complex links
		for (Link l : cl) {
			if(l.isSimpleLink() == false){
				addDefaultLink(l);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#deleteLink(edu.memphis.ccrg
	 * .lida.shared.Link)
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
	public synchronized void removeLinkable(Linkable linkable) {
		//First check if the NS actually contains specified linkable to prevent null pointers.
		if(!containsLinkable(linkable)){
			return;
		}
		
		//Need to remove all links connected to the linkable specified to be removed.
		Set<Link> connectedLinks = new HashSet<Link>(linkableMap.get(linkable));
		if (connectedLinks != null) {
			for (Link connectedLink : connectedLinks) {// for all of the links connected to n				
				removeLinkable(connectedLink);
			}
		}
		
		// finally remove the linkable and its links
		linkableMap.remove(linkable);
		if (linkable instanceof Node) {
			nodes.remove(((Node) linkable).getId());
		} else if (linkable instanceof Link) {
			//if removing a link then must remove the 2 references to the link
			//1 for the source and 1 for the sink
			Link aux = links.get(linkable.getExtendedId());
			links.remove(linkable.getExtendedId());

			Set<Link> sourceLinks = linkableMap.get(aux.getSource());
			if (sourceLinks != null){
				sourceLinks.remove(aux);
			}

			Set<Link> sinkLinks = linkableMap.get(aux.getSink());
			if (sinkLinks != null){
				sinkLinks.remove(aux);
			}
		}
	}

//	/*
//	 * (non-Javadoc)
//	 * 
//	 * @see
//	 * edu.memphis.ccrg.lida.shared.NodeStructure#deleteLinkable(edu.memphis
//	 * .ccrg.lida.shared.Linkable)
//	 */
//	@Override
//	public synchronized void removeLinkable(Linkable linkable) {
//		//First check if the NS actually contains specified linkable to prevent null pointers.
//		if(!containsLinkable(linkable)){
//			return;
//		}
//		
//		//Need to remove all links connected to the linkable specified to be removed.
//		Set<Link> connectedLinks = linkableMap.get(linkable);
//		Set<Link> otherLinks;
//		Linkable otherLinkable;
//
//		if (connectedLinks != null) {
//			for (Link connectedLink : connectedLinks) {// for all of the links connected to n
//				otherLinkable = connectedLink.getSink();
//				//Get the other Linkable for this link we are about to remove.
//				//we have to remove the other end's reference to this link
//				if(otherLinkable.equals(linkable)){
//					otherLinkable = connectedLink.getSource();
//					otherLinks = linkableMap.get(otherLinkable);
//					if (otherLinks != null) {
//						otherLinks.remove(connectedLink);
//					} else {
//						logger.log(Level.WARNING, "Expected other end of link "
//								+ otherLinkable.getLabel() + " to have link "
//								+ connectedLink.toString(), LidaTaskManager
//								.getCurrentTick());
//					}
//				}else{
//					otherLinks = linkableMap.get(otherLinkable);
//					if (otherLinks != null) {
//						otherLinks.remove(connectedLink);
//					} else {
//						logger.log(Level.WARNING, "Expected other end of link "
//								+ otherLinkable.getLabel() + " to have link "
//								+ connectedLink.toString(), LidaTaskManager
//								.getCurrentTick());
//					}
//				}
//				//If the link we are removing has other Links attached to it then 
//				// those attached links must be removed as well
//				Set<Link> attachedLinks = getAttachedLinks(connectedLink);
//				for(Link attachedLink: attachedLinks){
//					//We know the source of attched links is a node.
//					Node sourceOfAttached = attachedLink.getSource();
//					//Remove source node's reference to the attached link
//					linkableMap.get(sourceOfAttached).remove(attachedLink);
//					
//					//Now we can remove the attached link completely
//					links.remove(attachedLink.getExtendedId());
//					linkableMap.remove(attachedLink);
//				}
//				
//				//Now we can remove the connected link completely
//				links.remove(connectedLink.getExtendedId());
//				linkableMap.remove(connectedLink);
//				
////				removeLinkable(connectedLink);
//			}
//		}
//		
//		// finally remove the linkable and its links
//		linkableMap.remove(linkable);
//		if (linkable instanceof Node) {
//			nodes.remove(((Node) linkable).getId());
//		} else if (linkable instanceof Link) {
//			//if removing a link then must remove 2 references to the link
//			//1 for the source and 1 for the sink
//			Link aux = links.get(linkable.getExtendedId());
//			Set<Link> sourceLinks = linkableMap.get(aux.getSource());
//			Set<Link> sinkLinks = linkableMap.get(aux.getSink());
//			links.remove(linkable.getExtendedId());
//
//			if (sourceLinks != null){
//				sourceLinks.remove(aux);
//			}
//
//			if (sinkLinks != null){
//				sinkLinks.remove(aux);
//			}
//		}
//
//	}

	@Override
	public void removeLinkable(ExtendedId id) {
		if(!containsLinkable(id)){
			return;
		}
		
		if (id.isNodeId()) {
			removeLinkable(nodes.get(id.getSourceNodeId()));
		} else {
			removeLinkable(links.get(id));
		}
	}

	@Override
	public void removeNode(Node n) {
		removeLinkable(n);
	}
	
	
	@Override
	public void clearLinks() {		
		for (Link l : links.values()){
			removeLink(l);
		}
		this.links.clear();
	}

	@Override
	public void clearNodeStructure() {
		this.linkableMap.clear();
		this.nodes.clear();
		this.links.clear();
	}
	
	@Override
	public void decayNodeStructure(long ticks) {
		for (Linkable linkable : linkableMap.keySet()) {
			Activatible a = (Activatible) linkable;
			a.decay(ticks);
			if (a.isRemovable()) {
				removeLinkable(linkable);
			}
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public Link getLink(ExtendedId ids) {
		return links.get(ids);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getLinks()
	 */
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
	public Set<Link> getAttachedLinks(Linkable linkable) {
		if (linkable == null){
			return null;
		}

		Set<Link> aux = linkableMap.get(linkable);
		if (aux == null){
			return null;
		}else{
			return Collections.unmodifiableSet(aux);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#getLinks(edu.memphis.ccrg.
	 * lida.shared.Linkable, edu.memphis.ccrg.lida.shared.LinkType)
	 */
	@Override
	public Set<Link> getAttachedLinks(Linkable linkable, LinkCategory category) {
		if (linkable == null){
			return null;
		}
		Set<Link> temp = linkableMap.get(linkable);
		if(temp == null){
			return null;
		}
		Set<Link> attachedLinks = new HashSet<Link>();
		for (Link l : temp) {
			if (l.getCategory() == category){
				attachedLinks.add(l);
			}
		}		
		return Collections.unmodifiableSet(attachedLinks);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getNodes()
	 */
	@Override
	public Collection<Node> getNodes() {
		Collection<Node> aux = nodes.values();
		if (aux == null){
			return null;
		}else{
			return Collections.unmodifiableCollection(aux);
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getLinkableMap()
	 */
	@Override
	public Map<Linkable, Set<Link>> getLinkableMap() {
		return Collections.unmodifiableMap(linkableMap);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getNode(int)
	 */
	@Override
	public Node getNode(int id) {
		return nodes.get(id);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getNode(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public Node getNode(ExtendedId id) {
		if (id == null) {
			return null;
		}
		if(id.isNodeId()){
			return nodes.get(id.getSourceNodeId());
		}else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getLinkCount()
	 */
	@Override
	public int getLinkCount() {
		return links.size();
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getNodeCount()
	 */
	@Override
	public int getNodeCount() {
		return nodes.size();
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getLinkableCount()
	 */
	@Override
	public int getLinkableCount() {
		return linkableMap.size();
	}	

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getLinks(edu.memphis.ccrg.lida.framework.shared.LinkCategory)
	 */
	@Override
	public Set<Link> getLinks(LinkCategory category) {
		Set<Link> result = new ConcurrentHashSet<Link>();
		if (links != null) {
			for (Link l : links.values()) {
				if (l.getCategory() == category) {
					result.add(l);
				}
			}
		}
		return Collections.unmodifiableSet(result);
	}

	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getLinkable(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public Linkable getLinkable(ExtendedId ids) {
		if (ids.isNodeId()) {
			return getNode(ids);
		} else {
			return getLink(ids);
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getDefaultLinkType()
	 */
	@Override
	public String getDefaultLinkType() {
		return defaultLinkType;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getDefaultNodeType()
	 */
	@Override
	public String getDefaultNodeType() {
		return defaultNodeType;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getLinkables()
	 */
	@Override
	public Collection<Linkable> getLinkables() {
		return Collections.unmodifiableCollection(linkableMap.keySet());
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getParentLinkMap(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public Map<Linkable, Link> getConnectedSinks(Node n) {
		Map<Linkable, Link> sinkLinkMap = new HashMap<Linkable, Link>();
		Set<Link> candidateLinks = linkableMap.get(n);
		if (candidateLinks != null) {
			for (Link link : candidateLinks) {
				Linkable sink = link.getSink();
				if (!sink.equals(n)) {
					sinkLinkMap.put(sink, link);
				}
			}
		}
		return Collections.unmodifiableMap(sinkLinkMap);
	}
	
	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#getConnectedSources(edu.memphis.ccrg.lida.framework.shared.Linkable)
	 */
	@Override
	public Map<Node, Link> getConnectedSources(Linkable linkable) {
		Map<Node, Link> sourceLinkMap = new HashMap<Node, Link>();
		Set<Link> candidateLinks = linkableMap.get(linkable);
		if (candidateLinks != null) {
			for (Link link : candidateLinks) {
				Node source = link.getSource();
				if(!source.equals(linkable)){
					sourceLinkMap.put(source, link);
				}
			}
		}
		return Collections.unmodifiableMap(sourceLinkMap);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#containsLink(edu.memphis.ccrg.lida.framework.shared.Link)
	 */
	@Override
	public boolean containsLink(Link l) {
		return links.containsKey(l.getExtendedId());
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#containsLink(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsLink(ExtendedId id) {
		return links.containsKey(id);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#containsLinkable(edu.memphis.ccrg.lida.framework.shared.Linkable)
	 */
	@Override
	public boolean containsLinkable(Linkable l) {
		return linkableMap.containsKey(l);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#containsLinkable(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsLinkable(ExtendedId id) {
		return (containsNode(id) || containsLink(id));
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#containsNode(int)
	 */
	@Override
	public boolean containsNode(int id) {
		return nodes.containsKey(id);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#containsNode(edu.memphis.ccrg.lida.framework.shared.ExtendedId)
	 */
	@Override
	public boolean containsNode(ExtendedId id) {
		return id.isNodeId() && nodes.containsKey(id.getSourceNodeId());
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.shared.NodeStructure#containsNode(edu.memphis.ccrg.lida.framework.shared.Node)
	 */
	@Override
	public boolean containsNode(Node n) {
		return nodes.containsKey(n.getId());
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		String s = "NODES\n";
		for (Node n : nodes.values()){
			s = s + (n.getLabel() + "--" + n.getId() + "\n");
		}
			
		s = s + "LINKS\n";
		for (Link l : links.values()){
			s = s + (l.getLabel() + "--" + l.getExtendedId() + "\n");
		}
		return s;
	}

	/**
	 * Returns true if two NodeStructures are meaningfully equal, else false.
	 * Two NodeStructures are equal if they have the same exact nodes and links and 
	 * the nodes and links are of the same type.
	 * 
	 * @param ns1
	 *            first {@link NodeStructure}
	 * @param ns2
	 *            second {@link NodeStructure}
	 * @return boolean if the {@link NodeStructure}s are equal
	 */
	public static boolean compareNodeStructures(NodeStructure ns1,
			NodeStructure ns2) {
		if (ns1.getNodeCount() != ns2.getNodeCount()) {
			return false;
		}
		if (ns1.getLinkCount() != ns2.getLinkCount()) {
			return false;
		}
		for (Node n1 : ns1.getNodes()) {
			Node n2 = ns2.getNode(n1.getId());
			if (n2 == null || !n1.equals(n2) || !n2.equals(n1)){
				return false;
			}
		}
		for (Link l1 : ns1.getLinks()) {
			Link l2 = ns2.getLink(l1.getExtendedId());
			if (l2 == null || !l1.equals(l2) || !l2.equals(l1)) {
				return false;
			}
		}

		return true;
	}
	
}
