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
	private double activation;
	private final double MIN_ACTIVATION = 0.0;
	private final double MAX_ACTIVATION = 1.0;
    
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
		return (linkID == l.linkID) && (type == l.type);
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

	public void excite(double amount) {
		activation += amount;		
	}

	public double getMaxActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getMinActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setMaxActivation(double amount) {
		// TODO Auto-generated method stub
		
	}

	public void setMinActivation(double amount) {
		// TODO Auto-generated method stub
		
	}

	public void setSelectionThreshold(double threshold) {
		// TODO Auto-generated method stub
		
	}

	public double getDefaultMaxActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getDefaultMinActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getCurrentActivation() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLabel() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getID() {
		// TODO Auto-generated method stub
		return 0;
	}
}
