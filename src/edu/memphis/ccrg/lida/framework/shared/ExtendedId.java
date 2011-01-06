package edu.memphis.ccrg.lida.framework.shared;

public class ExtendedId {

	private int typeOfLink;
	private int sourceNodeId;
	private int typeOfSink;
	private int sinkNode1Id;
	private int sinkNode2Id;
	
	public ExtendedId(int typeOfLink, int sourceNodeId, ExtendedId sinkId) {
		super();
		this.typeOfLink = typeOfLink;
		this.sourceNodeId = sourceNodeId;
		this.typeOfSink = sinkId.typeOfLink;
		this.sinkNode1Id = sinkId.sourceNodeId;
		this.sinkNode2Id = sinkId.sinkNode1Id;
	}
	
	public ExtendedId(int typeOfLink, int sourceNodeId, int typeOfSink,
			int sinkNode1Id, int sinkNode2Id) {
		super();
		this.typeOfLink = typeOfLink;
		this.sourceNodeId = sourceNodeId;
		this.typeOfSink = typeOfSink;
		this.sinkNode1Id = sinkNode1Id;
		this.sinkNode2Id = sinkNode2Id;
	}
	
	@Override
	public boolean equals(Object o){
		if(o instanceof ExtendedId){
			ExtendedId id = (ExtendedId) o;
			return (typeOfLink == id.typeOfLink && sourceNodeId == id.sourceNodeId &&
			   typeOfSink == id.typeOfSink && sinkNode1Id == id.sinkNode1Id &&
			   sinkNode2Id == id.sinkNode2Id);
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return (typeOfLink ^ sourceNodeId ^ typeOfSink ^ sinkNode1Id ^ sinkNode2Id);
	}
	
	@Override
	public String toString(){
		return "[" + typeOfLink + "," + sourceNodeId + "," + typeOfSink + "," +
				sinkNode1Id + "," + sinkNode2Id + "]";
	}
	
	public int getSourceNodeId(){
		return sourceNodeId;
	}
	
	public boolean isNode(){
		return (typeOfLink==0);
	}
}
