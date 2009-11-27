package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * A NodeStructure holds a collection of Nodes an Links. An implementation of this interface
 * could be used as the "Common Currency" between the Modules of LIDA
 * 
 * 
 * 
 * @author Javier Snaider
 *
 */
public interface NodeStructure {

	public abstract Link addLink(Link l);

	public abstract void addLinks(Collection<Link> links);

	public abstract Node addNode(Node n);

	public abstract void addNodes(Collection<Node> nodes);

	public abstract NodeStructure copy();

	public abstract void deleteLink(Link l);
	
	public abstract void deleteLinkable(Linkable l);

	public abstract void deleteNode(Node n);
	
	public abstract void clearNodes();

	public abstract Collection<Link> getLinks();

	public abstract Set<Link> getLinks(Linkable l);

	public abstract Set<Link> getLinks(Linkable NorL, LinkType type);

	public abstract Set<Link> getLinks(LinkType type);

	public abstract Collection<Node> getNodes();

	public abstract void setDefaultLink(String linkClassName);

	public abstract void setDefaultNode(String nodeClassName);

	public abstract Node getNode(long id);

	public abstract Link getLink (String ids);
	
	public abstract void mergeWith (NodeStructure ns);

	public abstract Map<Linkable, Set<Link>> getLinkableMap();

	public abstract boolean hasNode(Node n);

	public abstract boolean hasLink(Link l);

	public abstract int getNodeCount();

	public abstract int getLinkCount();

	public abstract void mergeWith(Link l);

	public abstract void mergeWith(Node node);

	public abstract boolean containsNode(Node n);
	
	public abstract boolean containsLink(Link n);
	
}