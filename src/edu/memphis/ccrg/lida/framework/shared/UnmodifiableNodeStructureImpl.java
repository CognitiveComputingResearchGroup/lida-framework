package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * An immutable NodeStructureImpl.  Throws UnsupportedOprationException if modifying methods
 * are called.  Get, is, has, contains, methods call their inherited counterparts.
 */
public class UnmodifiableNodeStructureImpl extends NodeStructureImpl implements NodeStructure {
	
	/**
	 * 
	 * @param sourceNodeStructure supplied NodeStructure
	 */
	public UnmodifiableNodeStructureImpl(NodeStructure sourceNodeStructure){
		super(sourceNodeStructure);
	}
	
	/**
	 * Returns true if both NodeStructures have the same nodes and links
	 * and 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	public boolean equals(Object o) {
		if(!(o instanceof NodeStructureImpl)){
			return false;
		}
		NodeStructureImpl otherNS = (NodeStructureImpl) o;
		
		if(this.getNodeCount() != otherNS.getNodeCount()){
//			System.out.println("This " + getNodeCount() + " other " + otherNS.getNodeCount());
			return false;
		}else if(this.getLinkCount() != otherNS.getLinkCount()){
			return false;
		}
		
		//Iterate through other's nodes checking for equality
		for(Node otherNode: otherNS.getNodes()){
			if(this.containsNode(otherNode)){ //this checks for the node by id
				//okay
			}else{
				return false;
			}
		}

		//Iterate through other's link checking for equality
		for(Link otherLink: otherNS.getLinks()){
			if(this.containsLink(otherLink)){
				//okay
			}else{
				return false;
			}
		}
		return true;
	}

	@Override
	public int hashCode() {
		//Generate a long value for nodes based on individual node id and
		//the number of nodes total.
		Long aggregateNodeHash = 0L;
		for(Node n: super.getNodes()){
			aggregateNodeHash += n.hashCode();			
		}	
		aggregateNodeHash = aggregateNodeHash * 31 + super.getNodeCount() * 37;
		
		//Generate a long value for links based on individual link id and
		//the number of links total.
		Long aggregateLinkHash = 0L;
		for(Link l: super.getLinks()){
			aggregateLinkHash += l.hashCode();
		}
		aggregateLinkHash = aggregateLinkHash * 41 + super.getLinkCount() * 43;
			
		int hash = 47 + aggregateNodeHash.hashCode();
		return hash * 53 + aggregateLinkHash.hashCode();
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
	public Link addLink(ExtendedId idSource, ExtendedId idSink, LinkCategory type,
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
		return new UnmodifiableNodeStructureImpl(this);
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void removeLink(Link l) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void removeLinkable(Linkable l) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void removeNode(Node n) {
		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
	}

//	/**
//	 * @throws UnsupportedOperationException Cannot modify this object once created.
//	 */
//	@Override
//	public void clearNodes() {
//		throw new UnsupportedOperationException("StaticNodeStructure cannot be modified");
//	}

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

	@Override
	public Node getNode(int id) {
		return super.getNode(id);
	}

	@Override
	public Node getNode(ExtendedId ids) {
		return super.getNode(ids);
	}

	@Override
	public Link getLink(ExtendedId ids) {
		return super.getLink(ids);
	}

	@Override
	public Linkable getLinkable(ExtendedId ids) {
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

}
