package edu.memphis.ccrg.perception;

/**
 *
 * @author Ryan McCall
 */
public class Link {
    
    private Node sink;    
    private Node source;    
    private LinkType type; 
    private long linkID;
    
    public Link(Node source, Node sink, LinkType type, long id){        
        this.source = source;
        this.sink = sink;   
        this.type = type;
        linkID = id;
    }
    
    public Link(Link l){
    	sink = l.sink;
    	source = l.source;
    	type = l.type;
    	linkID = l.linkID;
    }
    
    public boolean equals(Link l){
		return linkID == l.linkID;
	}
	
    public int hashCode(){   	     	 
    	return (int)linkID % 982451653;
    }
   
    public Node getSource() {
        return source;
    }
    
    public Node getSink() {
        return sink;
    }
    
    public LinkType getType(){
    	return type;
    }
}
