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
	private int sinkCategory;
	private int sinkNode1Id;
	private int sinkNode2Id;
	
	/*
	 * For the Link constructor there are 2 cases.
	 * 
	 * Case 1: The sink is a Node.  
	 * Then {@link #sinkCategory} and {@link #sinkNode2Id} are 0.
	 *{@link #sinkNode1Id} is the id of the Node.
	 *
	 * Case 2: The sink is a {@link Link}, call it BOB.
	 * Then {@link #sinkCategory} is the {@link LinkCategory} of BOB. 
	 * Then {@link #sinkNode1Id} is BOB's {@link #sourceNodeId} and {@link #sinkNode2Id} is BOB's {@link #sinkNode1Id}
	 * 
	 * In either case {@link #linkCategory} is set using the first argument
	 * and {@link #sourceNodeId} is set using the second.
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
		this.sinkCategory = sinkId.linkCategory;
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
		this.linkCategory = Integer.MIN_VALUE;
		this.sourceNodeId = nodeId;
		this.sinkCategory = Integer.MIN_VALUE;
		this.sinkNode1Id = Integer.MIN_VALUE;
		this.sinkNode2Id = Integer.MIN_VALUE;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof ExtendedId) {
			ExtendedId id = (ExtendedId) o;
			return (linkCategory == id.linkCategory	&& 
					sourceNodeId == id.sourceNodeId && 
					sinkCategory == id.sinkCategory && 
					sinkNode1Id == id.sinkNode1Id && 
					sinkNode2Id == id.sinkNode2Id);
		}
		return false;
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
		return (linkCategory == Integer.MIN_VALUE);
	}
	
	@Override
	public int hashCode() {
		return (linkCategory ^ sourceNodeId ^ sinkCategory ^ sinkNode1Id ^ sinkNode2Id);
	}

	@Override
	public String toString() {
		return "[" + linkCategory + "," + sourceNodeId + "," + sinkCategory
				+ "," + sinkNode1Id + "," + sinkNode2Id + "]";
	}
	
}
