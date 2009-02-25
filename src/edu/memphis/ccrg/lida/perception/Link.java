package edu.memphis.ccrg.lida.perception;

import edu.memphis.ccrg.lida.perception.Link;
import edu.memphis.ccrg.lida.perception.LinkType;
import edu.memphis.ccrg.lida.perception.Linkable;

/**
 *
 * @author Ryan McCall
 */
public class Link implements Linkable{
    
    private Linkable sink;    
    private Linkable source;    
    private LinkType type; 
    private long linkID;
    
    public Link(Linkable source, Linkable sink, LinkType type, long id){        
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
    	if(!(l instanceof Link))
			return false;    	
		return linkID == l.linkID;
	}
    
    public int hashCode() { 
        int hash = 1;
        hash = hash * 31 + (new Long(linkID)).hashCode();
        hash = hash * 31 + (type == null ? 0 : type.hashCode());
        return hash;
    }

    
    public Link copy(Link l){
    	return new Link(l);
    }
   
    public Linkable getSource() {
        return source;
    }
    
    public Linkable getSink() {
        return sink;
    }
    
    public LinkType getType(){
    	return type;
    }
}
