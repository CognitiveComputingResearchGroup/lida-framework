package edu.memphis.ccrg.lida.framework.shared;

/**
 * Generalized Id for Both {@link Node}s and {@link Link}s.
 * Link's source must be a {@link Node}. Link's sink can be a Node or a Link.
 * 
 * @author Javier Snaider, Ryan McCall
 *
 */
public class ExtendedId {

	private int linkCategory;
	private int sourceNodeId;
	private int sinkLinkCategory;
	private int sinkNode1Id;
	private int sinkNode2Id;
	
	private static final int UNDEFINED = Integer.MIN_VALUE;
	
	/*
	 * For the Link constructor there are 3 categories of possible input.
	 * 
	 * In each case linkCategory is set using the first argument
	 * and sourceNodeId is set using the second.
	 * 
	 * Case 1: The sink is a Node.  
	 * Then sinkCategory and sinkNode2Id are UNDEFINED.
	 * while sinkNode1Id is the id of the Node.
	 *
	 * Case 2: The sink is a Link, call it BOB.
	 * Then sinkLinkCategory is the LinkCategory of BOB. 
	 * Then sinkNode1Id is BOB's sourceNodeId.
	 * 
	 * 	Subcase 2a:  BOB's sink is a node.
	 * 	Then sinkNode2Id is BOB's sinkNode1Id
	 * 
	 * 	Subcase 2b: BOB's sink is a link. Call this other link ROB.
	 * 	Then sinkNode2Id is set to the sinkNode1Id of BOB which is ROB's sourceNodeId.
	 * 
	 */

	/**
	 * Constructs an ExtendedId for a {@link Link}.
	 * 
	 * @param category Link's category
	 * @param sourceNodeId Node's id
	 * @param sinkId Sink's id
	 */
	public ExtendedId(int category, int sourceNodeId, ExtendedId sinkId) {
		super();
		this.linkCategory = category;
		this.sourceNodeId = sourceNodeId;
		this.sinkLinkCategory = sinkId.linkCategory;
		this.sinkNode1Id = sinkId.sourceNodeId;
		this.sinkNode2Id = sinkId.sinkNode1Id;
	}

	/**
	 * Constructs an ExtendedId for a {@link Node}
	 * 
	 * @param nodeId Node's id
	 */
	public ExtendedId(int nodeId) {
		super();
		this.linkCategory = UNDEFINED;
		this.sourceNodeId = nodeId;
		this.sinkLinkCategory = UNDEFINED;
		this.sinkNode1Id = UNDEFINED;
		this.sinkNode2Id = UNDEFINED;
	}

	/**
	 * Returns source Node id
	 * 
	 * @return id of the source Node
	 */
	public int getSourceNodeId() {
		return sourceNodeId;
	}

	/**
	 * Returns whether id is for a node.
	 * 
	 * @return true if this ExtendedId is for a Node.
	 */
	public boolean isNodeId() {
		return linkCategory == UNDEFINED;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ExtendedId) {
			ExtendedId otherId = (ExtendedId) o;
			return (linkCategory == otherId.linkCategory && 
					sourceNodeId == otherId.sourceNodeId && 
					sinkLinkCategory == otherId.sinkLinkCategory && 
					sinkNode1Id == otherId.sinkNode1Id   && 
					sinkNode2Id == otherId.sinkNode2Id);
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (linkCategory ^ sourceNodeId ^ sinkLinkCategory ^ sinkNode1Id ^ sinkNode2Id);
	}

	@Override
	public String toString() {
		return "[Cat: " + linkCategory + ", SrcId: " + sourceNodeId + "," + sinkLinkCategory
				+ "," + sinkNode1Id + "," + sinkNode2Id + "]";
	}
	
}
