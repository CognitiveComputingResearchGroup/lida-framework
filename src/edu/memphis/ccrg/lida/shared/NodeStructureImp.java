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
//import edu.memphis.ccrg.lida.wumpusWorld.d_perception.PamNodeImplW;


/**
 * @author Javier Snaider
 * 
 */
public class NodeStructureImp implements NodeStructure {
	private Map<Linkable, Set<Link>> linkMap;
	private Map<Long, Node> nodes;
	private Map<String, Link> links;
	private int linkCount = 0;// How many links have been added to this linkMap
	private String defaultNode;
	private String defaultLink;
	private NodeFactory factory = NodeFactory.getInstance();

	/**
	 * @param defaultNode
	 *            the defaultNode to set
	 */
	public void setDefaultNode(String defaultNode) {
		this.defaultNode = defaultNode;
	}

	/**
	 * @param defaultLink
	 *            the defaultLink to set
	 */
	public void setDefaultLink(String defaultLink) {
		this.defaultLink = defaultLink;
	}

	public NodeStructureImp() {
		linkMap = new HashMap<Linkable, Set<Link>>();
		nodes = new HashMap<Long, Node>();
		links = new HashMap<String, Link>();

	}

	public NodeStructureImp(String defaultNode, String defaultLink) {
		this();

		this.defaultNode = defaultNode;
		this.defaultLink = defaultLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#addLink(edu.memphis.ccrg.lida
	 * .shared.Link)
	 */
	public boolean addLink(Link l) {

		boolean result = false;

		Linkable source = l.getSource();
		Linkable sink = l.getSink();

		Linkable newSource = null;
		Linkable newSink = null;

		if (source instanceof Node) {
			Node snode = (Node) source;
			newSource = nodes.get(snode.getId());
			if (newSource == null) {
				return false;
			}
		}

		if (sink instanceof Node) {
			Node snode = (Node) sink;
			newSink = nodes.get(snode.getId());
			if (newSink == null) {
				return false;
			}
		}

		if (source instanceof Link) {
			newSource = links.get(source.getIds());
			if (newSource == null) {
				return false;
			}
		}

		if (sink instanceof Link) {
			newSink = links.get(sink.getIds());
			if (newSink == null) {
				return false;
			}
		}

		Link newLink = getNewLink(l, newSource, newSink, l.getType());
		links.put(newLink.getIds(), newLink);

		Set<Link> tempLinks = linkMap.get(source);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkMap.put(source, tempLinks);
		}
		tempLinks.add(newLink);

		tempLinks = linkMap.get(sink);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkMap.put(sink, tempLinks);
			tempLinks.add(newLink);
		}
		tempLinks.add(newLink);

		// linkMap.put()
		linkCount++;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#addLinkSet(java.util.Set)
	 */
	public void addLinks(Collection<Link> links) {
		for (Link l : links) {
			addLink(l);
		}
	}// public void addLinkSet(Set<Link> links)

	public boolean addNode(Node n) {
		if (!nodes.keySet().contains(n.getId())) {
			Node newNode = getNewNode(n);
			nodes.put(newNode.getId(), newNode);
			linkMap.put(newNode, null);
			return true;
		}
		return false;
	}//

	/**
	 * This method can be overwritten to customize the Node Creation.
	 * 
	 * @param n
	 *            The original Node
	 * @return The Node to be used in this NodeStructure
	 */
	protected Node getNewNode(Node n) {
		return factory.getNode(n, defaultNode);
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
		return factory.getLink(defaultLink, source, sink, l.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#addNodes(java.util.Set)
	 */
	public void addNodes(Set<Node> nodesToAdd, double upscale,
			double selectivity) {
		for (Node n : nodesToAdd) {
			addNode(n);
			// refresh();
		}
	}

	public NodeStructure copy() {
		// TODO Auto-generated method stub
		return null;
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

	}// public void deleteLink(Link l)

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#deleteLinkable(edu.memphis
	 * .ccrg.lida.shared.Linkable)
	 */
	public void deleteLinkable(Linkable n) {
		Set<Link> tempLinks = linkMap.get(n);
		Set<Link> otherLinks;
		Linkable other;

		if (tempLinks != null) {
			for (Link l : tempLinks) {
				other = l.getSink();
				if (!other.equals(n)) {
					otherLinks = linkMap.get(other);
					if (otherLinks != null)
						otherLinks.remove(l);
				}
				other = l.getSource();
				if (!other.equals(n)) {
					otherLinks = linkMap.get(other);
					if (otherLinks != null)
						otherLinks.remove(l);
				}
			}// for all of the links connected to n
		}

		linkMap.remove(n);// finally remove the linkable and its links
		if (n instanceof Node) {
			nodes.remove(((Node) n).getId());
		} else if (n instanceof Link) {
			Link aux = links.get(n.getIds());
			links.remove(aux.getIds());
			Set<Link> sourceLinks = linkMap.get(aux.getSource());
			Set<Link> sinkLinks = linkMap.get(aux.getSink());

			if (sourceLinks != null)
				sourceLinks.remove(aux);

			if (sinkLinks != null)
				sinkLinks.remove(aux);
		}

	}// public void deleteNode(Linkable n)

	public void deleteNode(Node n) {
		deleteLinkable(n);

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
		Set<Link> aux = linkMap.get(l);
		if (aux == null) {
			return null;
		} else {
			return Collections.unmodifiableSet(aux); // This returns the
		}// set of Links but
		// it prevents to be
		// modified

	}// public Set<Link> getLinks(PamNodeImplW n)

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#getLinks(edu.memphis.ccrg.
	 * lida.shared.Linkable, edu.memphis.ccrg.lida.shared.LinkType)
	 */
	public Set<Link> getLinks(Linkable NorL, LinkType type) {
		Set<Link> temp = linkMap.get(NorL);
		Set<Link> result = new HashSet<Link>();
		if (temp != null) {
			for (Link l : temp) {
				if (l.getType() == type)// add only decired type
					result.add(l);
			}// for each link
		}// result != null
		return result;
	}// public Set<Link> getLinks(PamNodeImplW n, LinkType type)

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
		if (aux == null) {
			return null;
		} else {
			return Collections.unmodifiableCollection(aux);
		}
	}

	public Object getContent() {
		return this;
	}

	public void combineNodeStructure(NodeStructure ns) {
		// TODO Auto-generated method stub

	}

	public Link getLink(String ids) {
		return links.get(ids);
	}

	public Node getNode(long id) {
		return nodes.get(id);
	}

}
