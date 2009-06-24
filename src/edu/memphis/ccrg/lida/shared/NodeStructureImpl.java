/**
 * 
 */
package edu.memphis.ccrg.lida.shared;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;

/**
 * @author Javier Snaider
 * 
 */
public class NodeStructureImpl implements NodeStructure, WorkspaceContent, BroadcastContent {
	
	private Map<Long, Node> nodes;
	private Map<String, Link> links;
	private Map<Linkable, Set<Link>> linkableMap;
	private NodeFactory factory = NodeFactory.getInstance();
	private String defaultNodeType;
	private String defaultLinkType;

	public NodeStructureImpl() {
		linkableMap = new HashMap<Linkable, Set<Link>>();
		nodes = new HashMap<Long, Node>();
		links = new HashMap<String, Link>();
		defaultNodeType = factory.getDefaultNodeType();
		defaultLinkType = factory.getDefaultLinkType();
	}

	public NodeStructureImpl(String defaultNode, String defaultLink) {
		this();
		this.defaultNodeType = defaultNode;
		this.defaultLinkType = defaultLink;
	}

	public NodeStructureImpl(NodeStructure oldGraph) {		
		nodes = new HashMap<Long, Node>();
		links = new HashMap<String, Link>();
		linkableMap = new HashMap<Linkable, Set<Link>>();	
		
		Collection<Node> oldNodes = oldGraph.getNodes();
		if(oldNodes != null)
			for(Node n: oldNodes)
				nodes.put(n.getId(), factory.getNode(n));
		
		Collection<Link> oldLinks = oldGraph.getLinks();
		if(oldLinks != null)
			for(Link l: oldLinks)
				links.put(l.getIds(), l);				
			
		Map<Linkable, Set<Link>> oldlinkableMap = oldGraph.getLinkableMap();
		if(oldlinkableMap != null){
			Set<Linkable> oldKeys = oldlinkableMap.keySet();
			if(oldKeys != null){
				for(Linkable l: oldKeys){
					if(l instanceof LinkImpl){
						LinkImpl castLink = (LinkImpl)l;
						this.linkableMap.put(new LinkImpl(castLink), new HashSet<Link>());
					}else if(l instanceof PamNodeImpl){
						PamNodeImpl castNode = (PamNodeImpl)l;
						this.linkableMap.put(new PamNodeImpl(castNode), new HashSet<Link>());
					}
				}
			}
		}
	}
	
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

		Link newLink = getNewLink(l, newSource, newSink, l.getType());
		links.put(newLink.getIds(), newLink);

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
		if (!nodes.keySet().contains(n.getId())) {
			Node newNode = getNewNode(n);
			nodes.put(newNode.getId(), newNode);
			linkableMap.put(newNode, null);
			return newNode;
		}
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#addNodes(java.util.Set)
	 */
	public void addNodes(Collection<Node> nodesToAdd) {
		for(Node n : nodesToAdd)
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
	 * @param l
	 *            The original Link
	 * @param source
	 *            The new source
	 * @param sink
	 *            The new sink
	 * @param type
	 *            the type of the link
	 * @return The link to be used in this NodeStructure
	 */
	protected Link getNewLink(Link l, Linkable source, Linkable sink,
			LinkType type) {
		return factory.getLink(defaultLinkType, source, sink, l.getType());
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
	}//method

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

	}//method

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
		Set<Link> aux = linkableMap.get(l);
		if(aux == null) 
			return null;
	    else 
			return Collections.unmodifiableSet(aux); // This returns the
		// set of Links but it prevents to be modified
	}//method

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
	}//method

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
		if(aux == null)
			return null;
		else
			return Collections.unmodifiableCollection(aux);
		
	}//method

	public Object getContent() {
		return this;
	}

	/**
	 * 
	 */
	public Map<Linkable, Set<Link>> getLinkableMap(){
		return linkableMap;
	}
	
	public Node getNode(long id) {
		return nodes.get(id);
	}

	public int getLinkCount() {
		return links.size();
	}
	
	public int getNodeCount(){
		return nodes.size();
	}
	
	public void mergeNodeStructure(NodeStructure ns) {
		addNodes(ns.getNodes());
		addLinks(ns.getLinks());
	}

	public void printLinkMap() {
		Set<Linkable> keys = linkableMap.keySet();
		for(Linkable key: keys){
			Set<Link> links = linkableMap.get(key);
			for(Link l: links)
				System.out.println("Source: " + l.getSource().toString() + " sink " + l.getSink().toString());
			System.out.println();
		}
	}//method

	public Set<Link> getLinksByType(LinkType type) {
		Set<Link> result = new HashSet<Link>();
		for(Link l: links.values()){
			if(l.getType() == type)
				result.add(l);
		}//for
		
		return result;
	}//method

	public void clearNodes() {
		nodes = new HashMap<Long, Node>();		
	}

	public boolean hasLink(Link l) {
		return links.containsKey(l.getIds());
	}

	public boolean hasNode(Node n) {
		return nodes.containsKey(n.getId());
	}

}//class
