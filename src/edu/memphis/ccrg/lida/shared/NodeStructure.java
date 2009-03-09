package edu.memphis.ccrg.lida.shared;

import java.util.Set;

public interface NodeStructure {

	public abstract boolean addLink(Link l);

	public abstract void addLinkSet(Set<Link> links);

	public abstract boolean addNode(Node n);

	public abstract void addNodes(Set<Node> nodesToAdd);

	public NodeStructure copy();

	public abstract void deleteLink(Link l);
	
	public abstract void deleteLinkable(Linkable l);

	public abstract void deleteNode(Node n);

	public abstract Set<Link> getLinks();

	public abstract Set<Link> getLinks(Linkable l);

	public abstract Set<Link> getLinks(Linkable NorL, LinkType type);

	public abstract Set<Link> getLinks(LinkType type);

	public abstract Set<Node> getNodes();

	public void setDefaultLink(String linkClassName);

	public void setDefaultNode(String nodeClassName);

}