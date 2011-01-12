package edu.memphis.ccrg.lida.framework.shared;

import java.util.Collection;

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
	@Override
	public boolean equals(Object o) {
		if(!(o instanceof NodeStructure)){
			return false;
		}
		NodeStructure otherNS = (NodeStructure) o;
		if(this.getNodeCount() != otherNS.getNodeCount()){
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

	@Override
	public NodeStructure copy(){
		return new UnmodifiableNodeStructureImpl(this);
	}
	
	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Link addLink(Link l) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Link addLink(ExtendedId idSource, ExtendedId idSink, LinkCategory type,
			double activation) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}
	
	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Link addLink(int idSource, ExtendedId idSink,
			LinkCategory type, double activation){
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Link addLink(int idSource, int idSink,
			LinkCategory type, double activation){
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void addLinks(Collection<Link> links) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Node addNode(Node n) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public Node addNode(Node n, String factoryNodeType) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void addNodes(Collection<Node> nodes) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void removeLink(Link l) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void removeLinkable(Linkable l) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void removeNode(Node n) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void removeLinkable(ExtendedId id){
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
		
	}
	
	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void clearLinks(){
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void clearNodeStructure(){
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}

	/**
	 * @throws UnsupportedOperationException Cannot modify this object once created.
	 */
	@Override
	public void mergeWith(NodeStructure ns) {
		throw new UnsupportedOperationException("UnmodifiableNodeStructure cannot be modified");
	}
}
