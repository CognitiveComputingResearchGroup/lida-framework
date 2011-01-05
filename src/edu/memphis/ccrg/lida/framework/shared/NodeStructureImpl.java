/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

/**
 * @author Javier Snaider, Ryan McCall
 *
 */
public class NodeStructureImpl implements NodeStructure, BroadcastContent, WorkspaceContent, Serializable {

	private static Logger logger = Logger.getLogger(NodeStructureImpl.class.getCanonicalName());

	/**
	 * Nodes contained in this NodeStructure indexed by their id
	 */
	private Map<Integer, Node> nodes;
	
	/**
	 * Links contained in this NodeStructure indexed by their id String.
	 */
	private Map<ExtendedId, Link> links;
	
	/**
	 * Links that each Linkable (Node or Link) has.
	 */
	private Map<Linkable, Set<Link>> linkableMap;
	
	/**
	 * Standard factory for new objects.  Used to create copies when adding linkables to this NodeStructure
	 */
	private static NodeFactory factory = NodeFactory.getInstance();
	private String defaultNodeType;
	private String defaultLinkType;

	/**
	 * Default constructor uses the default node and link types of the factory
	 */
	public NodeStructureImpl() {
		linkableMap = new ConcurrentHashMap<Linkable, Set<Link>>();
		nodes = new ConcurrentHashMap<Integer, Node>();
		links = new ConcurrentHashMap<ExtendedId, Link>();
		defaultNodeType = factory.getDefaultNodeType();
		defaultLinkType = factory.getDefaultLinkType();
	}

	/**
	 * Creates a new NodeStructureImpl with specified default Node type and link Type.
	 * If either is not in the factory the factory's defaults are used.
	 * @param defaultNode kind of node used in this NodeStructure
	 * @param defaultLink kind of link used in this NodeStructure
	 */
	public NodeStructureImpl(String defaultNode, String defaultLink) {
		this();
		setDefaultNode(defaultNode);		
		setDefaultLink(defaultLink);
	}

	public NodeStructureImpl(NodeStructure oldGraph, String defaultNodeType, String defaultLinkType) {
		this(defaultNodeType, defaultLinkType);

		// Copy nodes
		Collection<Node> oldNodes = oldGraph.getNodes();
		if (oldNodes != null)
			for (Node n : oldNodes)
				nodes.put(n.getId(), getNewNode(n, defaultNodeType));

		// Copy Links but with Source and Sink pointing the old ones.
		Collection<Link> oldLinks = oldGraph.getLinks();
		if (oldLinks != null)
			for (Link l : oldLinks) {
				links.put(l.getExtendedId(), generateNewLink(l.getSource(), l.getSink(), l.getCategory(), l.getActivation()));
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
		Map<Linkable, Set<Link>> oldlinkableMap = oldGraph.getLinkableMap();
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
	}// constructor

	public NodeStructureImpl(NodeStructure oldGraph){
		this(oldGraph, oldGraph.getDefaultNodeType(), oldGraph.getDefaultLinkType());
	}
	
	/**
	 * @param defaultNode
	 *            the defaultNode to set
	 */
	@Override
	public void setDefaultNode(String defaultNode) {
		if(factory.containsNodeType(defaultNode))
			this.defaultNodeType = defaultNode;
		else
			logger.log(Level.WARNING, "Factory does not contain specified node type!  Must specify all types in factoriesData.xml", LidaTaskManager.getActualTick());
	}

	/**
	 * @param defaultLink
	 *            the defaultLink to set
	 */
	@Override
	public void setDefaultLink(String defaultLink) {	
		if(factory.containsLinkType(defaultLink))
			this.defaultLinkType = defaultLink;
		else
			logger.log(Level.WARNING, "Factory does not contain specified link type!  Must specify all types in factoriesData.xml", LidaTaskManager.getActualTick());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#addLink(edu.memphis.ccrg.lida
	 * .shared.Link)
	 */
	@Override
	public Link addLink(Link l) {		
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
				return null;
			}
		

		if (sink instanceof Node) {
			Node snode = (Node) sink;
			newSink = nodes.get(snode.getId());
			if (newSink == null) {
				return null;
			}
		}else{
			newSink = links.get(sink.getExtendedId());
			if (newSink == null) {
				return null;
			}
		}
		//System.out.println("about to generate new link with activation " + newActiv);
		return generateNewLink(newSource, newSink, l.getCategory(), newActiv);
	}

	@Override
	public Link addLink(ExtendedId sourceId, ExtendedId sinkId, LinkCategory category, double activation) {
		Node source = getNode(sourceId);
		Linkable sink = getLinkable(sinkId);
		if (source == null || sink == null) {
			return null;
		}
		return generateNewLink(source, sink, category, activation);
	}

	/**
	 * @param newSource
	 * @param newSink
	 * @param type
	 * @param activation
	 */
	private Link generateNewLink(Node newSource, Linkable newSink,
								 LinkCategory type, double activation) {
		Link newLink = getNewLink(newSource, newSink, type);
		newLink.setActivation(activation);
		links.put(newLink.getExtendedId(), newLink);
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
			tempLinks.add(newLink);
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
	public void addLinks(Collection<Link> links) {
		for (Link l : links)
			addLink(l);
	}
	
	@Override
	public Node addNode(Node n){
		return addNode(n, n.getFactoryName());
	}

	@Override
	public Node addNode(Node n, String factoryName) {
		if(factory.containsNodeType(factoryName) == false){
			logger.log(Level.WARNING, "Tried to add node of type " + factoryName + " to NodeStructure. " +
					" the factory does not contain that node type.  Make sure the node type is defined in " +
					" factoriesData.xml", LidaTaskManager.getActualTick());
			return null;
		}
		
		Node node = nodes.get(n.getId());
		if (node == null) {
			node = getNewNode(n, factoryName);
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
	public void addNodes(Collection<Node> nodesToAdd) {
		for (Node n : nodesToAdd){
			addNode(n, n.getFactoryName());
		}
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
	protected Link getNewLink(Node source, Linkable sink, LinkCategory category) {
		return factory.getLink(defaultLinkType, source, sink, category);
	}

	@Override
	public NodeStructure copy() {
		logger.finer("Copying NodeStructure " + this);
		return new NodeStructureImpl(this, defaultNodeType, defaultLinkType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#deleteLink(edu.memphis.ccrg.lida.shared.Link)
	 */
	@Override
	public void removeLink(Link l) {
		removeLinkable(l);
	}// method

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#deleteLinkable(edu.memphis
	 * .ccrg.lida.shared.Linkable)
	 */
	@Override
	public void removeLinkable(Linkable n) {
		Set<Link> tempLinks = linkableMap.get(n);
		Set<Link> otherLinks;
		Linkable other;

		if (tempLinks != null) {
			for (Link l : tempLinks) {
				other = l.getSink();
				if (!other.equals(n)) {
					otherLinks = linkableMap.get(other);
					if (otherLinks != null)
						otherLinks.remove(l);
				}
				other = l.getSource();
				if (!other.equals(n)) {
					otherLinks = linkableMap.get(other);
					if (otherLinks != null)
						otherLinks.remove(l);
				}
			}// for all of the links connected to n
		}

		linkableMap.remove(n);// finally remove the linkable and its links
		if (n instanceof Node) {
			nodes.remove(((Node) n).getId());
		} else if (n instanceof Link) {
			Link aux = links.get(n.getExtendedId());
			links.remove(aux.getExtendedId());
			Set<Link> sourceLinks = linkableMap.get(aux.getSource());
			Set<Link> sinkLinks = linkableMap.get(aux.getSink());

			if (sourceLinks != null)
				sourceLinks.remove(aux);

			if (sinkLinks != null)
				sinkLinks.remove(aux);
		}

	}// method

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
	public Set<Link> getLinks(Linkable l) {
		if (l == null) 
			return null;

		Set<Link> aux = linkableMap.get(l);
		if (aux == null)
			return null;
		else
			return Collections.unmodifiableSet(aux); // This returns the
		// set of Links but it prevents to be modified
	}// method

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#getLinks(edu.memphis.ccrg.
	 * lida.shared.Linkable, edu.memphis.ccrg.lida.shared.LinkType)
	 */
	@Override
	public Set<Link> getLinks(Linkable NorL, LinkCategory type) {
		Set<Link> temp = linkableMap.get(NorL);
		Set<Link> result = new HashSet<Link>();
		if (temp != null) {
			for (Link l : temp) {
				if (l.getCategory() == type)// add only desired type
					result.add(l);
			}// for each link
		}// result != null
		return result;
	}// method
	
	@Override
	public Set<Link> getLinks(LinkCategory type) {
		Set<Link> result = new HashSet<Link>();
		if (links != null) {
			for (Link l : links.values()) {
				if (l.getCategory() == type) {
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

	}// method

	public Object getContent() {
		return this;
	}

	/**
	 * 
	 */
	@Override
	public Map<Linkable, Set<Link>> getLinkableMap() {
		return linkableMap;
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
		addNodes(ns.getNodes());
		Collection<Link> cl = ns.getLinks();
		boolean pending = true;
		while (pending) {
			pending = false;
			for (Link l : cl) {
				if (addLink(l) == null) {
					pending = true;
				}
			}
		}
		// TODO: Must add links differently than above statement.
	}

	public Set<Link> getLinksByType(LinkCategory type) {
		Set<Link> result = new HashSet<Link>();
		for (Link l : links.values()) {
			if (l.getCategory() == type)
				result.add(l);
		}// for

		return result;
	}// method

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
	public Linkable getLinkable(ExtendedId ids) {
		Linkable linkable = getNode(ids);
		if (linkable == null) {
			linkable = getLink(ids);
		}
		return linkable;
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

}// class
