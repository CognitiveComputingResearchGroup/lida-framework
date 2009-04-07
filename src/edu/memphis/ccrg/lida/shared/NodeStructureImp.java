/**
 * 
 */
package edu.memphis.ccrg.lida.shared;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//import edu.memphis.ccrg.lida.perception.LinkImp;
import edu.memphis.ccrg.lida._perception.PamNodeImpl;

/**
 * @author Javier Snaider
 * 
 */
public class NodeStructureImp implements NodeStructure {
	private Map<Linkable, Set<Link>> linkMap;
	private Set<Node> nodes;
	private int linkCount = 0;// How many links have been added to this linkMap
	private String defaultNode;
	private String defaultLink;
	private NodeFactory factory = NodeFactory.getInstance();
	
	/**
	 * @param defaultNode the defaultNode to set
	 */
	public void setDefaultNode(String defaultNode) {
		this.defaultNode = defaultNode;
	}

	/**
	 * @param defaultLink the defaultLink to set
	 */
	public void setDefaultLink(String defaultLink) {
		this.defaultLink = defaultLink;
	}


	public NodeStructureImp() {
		linkMap = new HashMap<Linkable, Set<Link>>();
		nodes = new HashSet<Node>();
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#addLink(edu.memphis.ccrg.lida
	 * .shared.Link)
	 */
	public boolean addLink(Link l) {
		
		boolean result=false;
		
		Linkable source = l.getSource();
		Linkable sink = l.getSink();
		if((source instanceof Node) && (!nodes.contains((Node)source))){
			return false;
		}
		
		if((source instanceof Link) && (!linkMap.keySet().contains(source))){
			return false;
		}
		if((sink instanceof Link) && (!linkMap.keySet().contains(sink))){
			return false;
		}
				
		Set<Link> tempLinks = linkMap.get(source);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkMap.put(source, tempLinks);
		}
		
	//	Link newLink = factory.
	//	tempLinks.add(l);

		tempLinks = linkMap.get(sink);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkMap.put(sink, tempLinks);
		}
		
	//	linkMap.put()
		linkCount++;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#addLinkSet(java.util.Set)
	 */
	public void addLinkSet(Set<Link> links) {
		for (Link l : links){}
		//	addLink(new LinkImp((LinkImp) l));
	}// public void addLinkSet(Set<Link> links)

	public boolean addNode(Node n) {
		if (!nodes.contains(n)){// check this
			nodes.add(factory.getNode(n));
			linkMap.put(n, null);
		}
		
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#addNodes(java.util.Set)
	 */
	public void addNodes(Set<Node> nodesToAdd, double upscale, double selectivity) {
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
		Set<Link> sourceLinks = linkMap.get(l.getSource());
		Set<Link> sinkLinks = linkMap.get(l.getSink());

		if (sourceLinks != null)
			sourceLinks.remove(l);

		if (sinkLinks != null)
			sinkLinks.remove(l);

	}// public void deleteLink(Link l)

	// TODO: Happens to the other nodes if we delete a Linkable that connects
	// them?
	// TODO: What is their layer depth then?22
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
		linkMap.remove(n);// finally remove the linkable and its links
	}// public void deleteNode(Linkable n)

	public void deleteNode(Node n) {
		// TODO Auto-generated method stub
		
	}

	public Set<Link> getLinks() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#getLinks(edu.memphis.ccrg.
	 * lida.shared.Linkable)
	 */
	public Set<Link> getLinks(Linkable l) {
		return linkMap.get(l);
	}// public Set<Link> getLinks(PamNodeImpl n)

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#getLinks(edu.memphis.ccrg.
	 * lida.shared.Linkable, edu.memphis.ccrg.lida.shared.LinkType)
	 */
	public Set<Link> getLinks(Linkable NorL, LinkType type) {
		Set<Link> result = linkMap.get(NorL);
		if (result != null) {
			for (Link l : result) {
				if (l.getType() != type)// remove links that don't match
					// specified type
					result.remove(l);
			}// for each link
		}// result != null
		return result;
	}// public Set<Link> getLinks(PamNodeImpl n, LinkType type)

	public Set<Link> getLinks(LinkType type) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#getNodes()
	 */
	public Set<Node> getNodes() {
		return nodes;
	}


}
