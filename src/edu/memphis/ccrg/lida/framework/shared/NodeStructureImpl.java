package edu.memphis.ccrg.lida.framework.shared;

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
public class NodeStructureImpl implements NodeStructure, BroadcastContent, WorkspaceContent{

	private Map<Long, Node> nodes;
	private Map<String, Link> links;
	private Map<Linkable, Set<Link>> linkableMap;
	private static NodeFactory factory = NodeFactory.getInstance();
	private String defaultNodeType;
	private String defaultLinkType;
	
	private static Logger logger = Logger.getLogger("edu.memphis.ccrg.lida.framework.shared");

	public NodeStructureImpl() {
		linkableMap = new ConcurrentHashMap<Linkable, Set<Link>>();
		nodes = new ConcurrentHashMap<Long, Node>();
		links = new ConcurrentHashMap<String, Link>();
		defaultNodeType = factory.getDefaultNodeType();
		defaultLinkType = factory.getDefaultLinkType();
	}

	public NodeStructureImpl(String defaultNode, String defaultLink) {
		this();
		if(factory.containsNodeType(defaultNode))
			this.defaultNodeType = defaultNode;
		else{
			logger.log(Level.WARNING, 
					  "Constructor: specified node type is not registered in the node factory", 
					  LidaTaskManager.getActualTick());
		}
		
		if(factory.containsLinkType(defaultLink))
			this.defaultLinkType = defaultLink;
		else{
			logger.log(Level.WARNING, 
					  "Constructor: specified link type is not registered in the node factory ", 
					  LidaTaskManager.getActualTick());
		}
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
				links.put(l.getIds(), getNewLink(l.getSource(), l.getSink(), l
						.getCategory()));
			}

		// Fix Source and Sinks now that all new Nodes and Links have been
		// copied
		for (String ids : links.keySet()) {
			Link l = links.get(ids);
			Linkable lso=l.getSource();
			Linkable lsi=l.getSink();
			if (lso instanceof Node) {
				l.setSource(nodes.get(((Node) lso).getId()));
			} else {
				l.setSource(links.get(lso.getIds()));
			}

			if (lsi instanceof Node) {
				l.setSink(nodes.get(((Node) lsi).getId()));
			} else {
				l.setSink(links.get(lsi.getIds()));
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
							newLinks.add(links.get(link.getIds()));
						}
					} else {
						newLinks = null;
					}
					if (l instanceof Link) {
						linkableMap.put(links.get(l.getIds()), newLinks);
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
	public void setDefaultLink(String defaultLink) {
//		
//		System.out.println("Links");
//		Map<String, LinkableDef> slm = factory.getStupidLinkMap();
//		for(String s: slm.keySet())
//			System.out.println(s + " " + slm.get(s));
//		
//		System.out.println("Nodes");
//		Map<String, LinkableDef> snm = factory.getStupidNodeMap();
//		for(String s: snm.keySet())
//			System.out.println(s + " " + snm.get(s));
		
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
	public Link addLink(Link l) {		
		double newActiv = l.getActivation();
		Link oldLink = links.get(l.getIds());
		if (oldLink != null) { // if the link already exists in this node structure
			//update activation
			if (oldLink.getActivation() < newActiv) 
				oldLink.setActivation(newActiv);
			return oldLink;
		}
		Linkable source = l.getSource();
		Linkable sink = l.getSink();

		Linkable newSource = null;
		Linkable newSink = null;

		//If link's source is a node, but that node
		//in not in this nodestructure, return null
		if (source instanceof Node) {
			Node snode = (Node) source;
			newSource = nodes.get(snode.getId());
			if (newSource == null) {
				return null;
			}
		}

		if (sink instanceof Node) {
			Node snode = (Node) sink;
			newSink = nodes.get(snode.getId());
			if (newSink == null) {
				return null;
			}
		}

		if (source instanceof Link) {
			newSource = links.get(source.getIds());
			if (newSource == null) {
				return null;
			}
		}

		if (sink instanceof Link) {
			newSink = links.get(sink.getIds());
			if (newSink == null) {
				return null;
			}
		}
		//System.out.println("about to generate new link with activation " + newActiv);
		return generateNewLink(newSource, newSink, l.getCategory(), newActiv);
	}

	public Link addLink(String sourceId, String sinkId, LinkCategory category, double activation) {
		Linkable source = getLinkable(sourceId);
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
	private Link generateNewLink(Linkable newSource, Linkable newSink,
								 LinkCategory type, double activation) {
		Link newLink = getNewLink(newSource, newSink, type);
		newLink.setActivation(activation);
		links.put(newLink.getIds(), newLink);
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
	public void addLinks(Collection<Link> links) {
		for (Link l : links)
			addLink(l);
	}
	
	public Node addNode(Node n){
		return addNode(n, n.getFactoryName());
	}

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
	protected Link getNewLink(Linkable source, Linkable sink, LinkCategory category) {
		return factory.getLink(defaultLinkType, source, sink, category);
	}

	public NodeStructure copy() {
		logger.finer("Copying NodeStructure " + this);
		return new NodeStructureImpl(this, defaultNodeType, defaultLinkType);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#deleteLink(edu.memphis.ccrg.lida.shared.Link)
	 */
	public void deleteLink(Link l) {
		deleteLinkable(l);
	}// method

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#deleteLinkable(edu.memphis
	 * .ccrg.lida.shared.Linkable)
	 */
	public void deleteLinkable(Linkable n) {
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
			Link aux = links.get(n.getIds());
			links.remove(aux.getIds());
			Set<Link> sourceLinks = linkableMap.get(aux.getSource());
			Set<Link> sinkLinks = linkableMap.get(aux.getSink());

			if (sourceLinks != null)
				sourceLinks.remove(aux);

			if (sinkLinks != null)
				sinkLinks.remove(aux);
		}

	}// method

	public void deleteNode(Node n) {
		deleteLinkable(n);
	}

	public Link getLink(String ids) {
		return links.get(ids);
	}

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
	public Set<Link> getLinks(Linkable NorL, LinkCategory type) {
		Set<Link> temp = linkableMap.get(NorL);
		Set<Link> result = new HashSet<Link>();
		if (temp != null) {
			for (Link l : temp) {
				if (l.getCategory() == type)// add only decired type
					result.add(l);
			}// for each link
		}// result != null
		return result;
	}// method

	public Set<Link> getLinks(LinkCategory type) {
		Set<Link> result = new HashSet<Link>();
		if (links != null) {
			for (Link l : links.values()) {
				if (l.getCategory() == type) {
					result.add((Link) l);
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
	public Map<Linkable, Set<Link>> getLinkableMap() {
		return linkableMap;
	}

	public Node getNode(long id) {
		return nodes.get(id);
	}

	public Node getNode(String id) {
		Long idl = 0L;
		try {
			idl = new Long(id);
		} catch (NumberFormatException e) {
			return null;
		}
		return nodes.get(idl);
	}

	public int getLinkCount() {
		return links.size();
	}

	public int getNodeCount() {
		return nodes.size();
	}
	public int getLinkableCount() {
		return linkableMap.size();
	}

	public void mergeWith(NodeStructure ns) {
		addNodes(ns.getNodes());
		Collection<Link> cl = ns.getLinks();
//		for(Link l: cl){
//			System.out.println(l.getLabel() + " " + l.getIds());
//			
//		}
		boolean pending = true;
		while (pending) {
			pending = false;
			for (Link l : cl) {
				//System.out.println("add link is " + addLink(l));
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

	public void clearNodes() {
		nodes = new ConcurrentHashMap<Long, Node>();
	}

	public boolean hasLink(Link l) {
		return links.containsKey(l.getIds());
	}

	public boolean hasNode(Node n) {
		return nodes.containsKey(n.getId());
	}

	public Linkable getLinkable(String ids) {
		Linkable linkable = getNode(ids);
		if (linkable == null) {
			linkable = getLink(ids);
		}
		return linkable;
	}

	/**
	 * Returns true if both NodeStructures have the same Nodes and Links.
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		boolean result = false;
		if ((o != null) && (o instanceof NodeStructure)) {
			NodeStructure ns = (NodeStructure) o;
			Set<Linkable> nsLinkables = ns.getLinkableMap().keySet();
			Set<Linkable> thisLinkables = linkableMap.keySet();
			if (thisLinkables.size() == nsLinkables.size()
					&& thisLinkables.containsAll(nsLinkables)) {
				result = true;
			}
		}
		return result;
	}

	@Override
	public String getDefaultLinkType() {
		return defaultLinkType;
	}

	@Override
	public String getDefaultNodeType() {
		return defaultNodeType;
	}

	public void print() {
		System.out.println("\nNODES");
		for(Node n: nodes.values())
			System.out.println(n.getLabel() + "--" + n.getId());
		
		System.out.println("\nLINKS");
		for(Link l: links.values())
			System.out.println(l.getLabel() + "--" + l.getIds());
	}

	@Override
	public Collection<Linkable> getLinkables() {
		return Collections.unmodifiableCollection(linkableMap.keySet());
	}

	@Override
	public String getNodeAndLinkCount() {
		return "Nodes: " + getNodeCount() + " Links: " + getLinkCount();
	}

}// class
