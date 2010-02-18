package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

/**
 * @author Javier Snaider
 * 
 */
public class NodeStructureImpl implements NodeStructure, BroadcastContent,
		WorkspaceContent {

	private Map<Long, Node> nodes;
	private Map<String, Link> links;
	private Map<Linkable, Set<Link>> linkableMap;
	private static NodeFactory factory = NodeFactory.getInstance();
	private String defaultNodeType;
	private String defaultLinkType;

	public NodeStructureImpl() {
		linkableMap = new ConcurrentHashMap<Linkable, Set<Link>>();
		nodes = new ConcurrentHashMap<Long, Node>();
		links = new ConcurrentHashMap<String, Link>();
		defaultNodeType = factory.getDefaultNodeType();
		defaultLinkType = factory.getDefaultLinkType();
	}

	public NodeStructureImpl(String defaultNode, String defaultLink) {
		this();
		this.defaultNodeType = defaultNode;
		this.defaultLinkType = defaultLink;
	}

	public NodeStructureImpl(NodeStructure oldGraph) {
		nodes = new ConcurrentHashMap<Long, Node>();
		links = new ConcurrentHashMap<String, Link>();
		linkableMap = new ConcurrentHashMap<Linkable, Set<Link>>();
		this.defaultNodeType = ((NodeStructureImpl) oldGraph).defaultNodeType;
		this.defaultLinkType = ((NodeStructureImpl) oldGraph).defaultLinkType;

		// Copy nodes
		Collection<Node> oldNodes = oldGraph.getNodes();
		if (oldNodes != null)
			for (Node n : oldNodes)
				nodes.put(n.getId(), getNewNode(n));

		// Copy Links but with Source and Sink pointing the old ones.
		Collection<Link> oldLinks = oldGraph.getLinks();
		if (oldLinks != null)
			for (Link l : oldLinks) {
				links.put(l.getIds(), getNewLink(l.getSource(),l.getSink(),l.getType()));
			}

		// Fix Source and Sinks now that all new Nodes and Links have been
		// copied
		for (String ids : links.keySet()) {
			Link l = links.get(ids);
			if (l.getSource() instanceof Node) {
				l.setSource(nodes.get(((Node) l).getId()));
			} else {
				l.setSource(links.get(l.getIds()));
			}

			if (l.getSink() instanceof Node) {
				l.setSink(nodes.get(((Node) l).getId()));
			} else {
				l.setSink(links.get(l.getIds()));
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

	/**
	 * @param defaultNode
	 *            the defaultNode to set
	 */
	public void setDefaultNode(String defaultNode) {
		this.defaultNodeType = defaultNode;
	}

	/**
	 * @param defaultLink
	 *            the defaultLink to set
	 */
	public void setDefaultLink(String defaultLink) {
		this.defaultLinkType = defaultLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#addLink(edu.memphis.ccrg.lida
	 * .shared.Link)
	 */
	public Link addLink(Link l) {

		Link oldLink = links.get(l.getIds());
		if (oldLink != null) { // if the link already exists only actualize the
								// activation.
			//if link already there update activation 
			double newActiv = l.getActivation();
			if (oldLink.getActivation() < newActiv) {
				oldLink.setActivation(newActiv);
			}
			return oldLink;
		}
		Linkable source = l.getSource();
		Linkable sink = l.getSink();

		Linkable newSource = null;
		Linkable newSink = null;

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

		Link newLink = getNewLink( newSource, newSink, l.getType());
		links.put(newLink.getIds(), newLink);
		linkableMap.put(newLink, new HashSet<Link>());

		Set<Link> tempLinks = linkableMap.get(source);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkableMap.put(source, tempLinks);
		}
		tempLinks.add(newLink);

		tempLinks = linkableMap.get(sink);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkableMap.put(sink, tempLinks);
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

	public Node addNode(Node n) {
		Node node = nodes.get(n.getId());
		if (node == null) {
			node = getNewNode(n);
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
		for (Node n : nodesToAdd)
			addNode(n);
	}

	/**
	 * This method can be overwritten to customize the Node Creation.
	 * 
	 * @param n
	 *            The original Node
	 * @return The Node to be used in this NodeStructure
	 */
	protected Node getNewNode(Node n) {
		return factory.getNode(n, defaultNodeType);
	}

	/**
	 * This method can be overwritten to customize the Link Creation. some of
	 * the parameter could be redundant in some cases.
	 * 
	 * @param source
	 *            The new source
	 * @param sink
	 *            The new sink
	 * @param type
	 *            the type of the link
	 * @return The link to be used in this NodeStructure
	 */
	protected Link getNewLink( Linkable source, Linkable sink,
			LinkType type) {
		return factory.getLink(defaultLinkType, source, sink, type);
	}

	public NodeStructure copy() {
		return new NodeStructureImpl(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#deleteLink(edu.memphis.ccrg
	 * .lida.shared.Link)
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
		if (l == null) {
			return null;
		}
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
	public Set<Link> getLinks(Linkable NorL, LinkType type) {
		Set<Link> temp = linkableMap.get(NorL);
		Set<Link> result = new HashSet<Link>();
		if (temp != null) {
			for (Link l : temp) {
				if (l.getType() == type)// add only decired type
					result.add(l);
			}// for each link
		}// result != null
		return result;
	}// method

	public Set<Link> getLinks(LinkType type) {
		Set<Link> result = new HashSet<Link>();
		if (links != null) {
			for (Link l : links.values()) {
				if (l.getType() == type) {
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

	public int getLinkCount() {
		return links.size();
	}

	public int getNodeCount() {
		return nodes.size();
	}

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


	public Set<Link> getLinksByType(LinkType type) {
		Set<Link> result = new HashSet<Link>();
		for (Link l : links.values()) {
			if (l.getType() == type)
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

}// class
