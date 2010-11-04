package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An immutable NodeStructureImpl.  Throws UnsupportedOprationException if modifying methods
 * are called.  Get, is, has, contains, methods call their inherited counterparts.
 */
public class StaticNodeStructureImpl extends NodeStructureImpl implements NodeStructure {
	
	/**
	 * Calls NodeStrucutreImpl's copy constructor
	 * @param ns source NodeStructure
	 */
	public StaticNodeStructureImpl(NodeStructure ns){
		super(ns);
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Link addLink(Link l) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Link addLink(String idSource, String idSink, LinkCategory type,
			double activation) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void addLinks(Collection<Link> links) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Node addNode(Node n) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Node addNode(Node n, String factoryName) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void addNodes(Collection<Node> nodes) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	@Override
	public NodeStructure copy() {
		return new StaticNodeStructureImpl(this);
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void deleteLink(Link l) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void deleteLinkable(Linkable l) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void deleteNode(Node n) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void clearNodes() {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Collection<Link> getLinks() {
		return super.getLinks();
	}

	@Override
	public Set<Link> getLinks(Linkable l) {
		return super.getLinks(l);
	}

	@Override
	public Set<Link> getLinks(Linkable NorL, LinkCategory type) {
		return super.getLinks(NorL, type);
	}

	@Override
	public Set<Link> getLinks(LinkCategory type) {
		return super.getLinks(type);
	}

	@Override
	public Collection<Node> getNodes() {
		return super.getNodes();
	}
	
	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void setDefaultLink(String linkClassName) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void setDefaultNode(String nodeClassName) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	@Override
	public Node getNode(long id) {
		return super.getNode(id);
	}

	@Override
	public Node getNode(String ids) {
		return super.getNode(ids);
	}

	@Override
	public Link getLink(String ids) {
		return super.getLink(ids);
	}

	@Override
	public Linkable getLinkable(String ids) {
		return super.getLinkable(ids);
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void mergeWith(NodeStructure ns) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	@Override
	public Map<Linkable, Set<Link>> getLinkableMap() {
		return super.getLinkableMap();
	}

	@Override
	public boolean containsNode(Node n) {
		return super.containsNode(n);
	}

	@Override
	public boolean containsLink(Link l) {
		return super.containsLink(l);
	}

	@Override
	public int getNodeCount() {
		return super.getNodeCount();
	}

	@Override
	public int getLinkCount() {
		return super.getLinkCount();
	}

	@Override
	public int getLinkableCount() {
		return super.getLinkableCount();
	}

	@Override
	public Collection<Linkable> getLinkables() {
		return super.getLinkables();
	}

	@Override
	public String getDefaultNodeType() {
		return super.getDefaultNodeType();
	}

	@Override
	public String getDefaultLinkType() {
		return super.getDefaultLinkType();
	}

	@Override
	public String getNodeAndLinkCount() {
		return super.getNodeAndLinkCount();
	}

}
