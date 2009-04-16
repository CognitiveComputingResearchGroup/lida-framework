package edu.memphis.ccrg.lida.shared;

import java.util.Map;

import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

/**
 *
 * @author Ryan McCall
 */
public class LinkImpl implements Link, Node{
    
	private final double MIN_ACTIVATION = 0.0;
	private final double MAX_ACTIVATION = 1.0;
	
    private Linkable sink;    
    private Linkable source;    
    private long linkID;
    private String label = "linkImpl";
    private LinkType type; 
	private double activation = 0.0;
	
	
    public LinkImpl(Linkable source, Linkable sink, LinkType type, long id){        
        this.source = source;
        this.sink = sink;   
        this.type = type;
        linkID = id;
    }
    
    public LinkImpl(LinkImpl l){
    	sink = l.sink;
    	source = l.source;
    	linkID = l.linkID;
    	label = l.label;
    	type = l.type;
    	activation = l.activation;    	
    }

	public boolean equals(Object obj){
		if(!(obj instanceof LinkImpl)){
			return false;    	
    	}    	
    	LinkImpl other = (LinkImpl)obj;
		return (linkID == other.linkID) && (type == other.type);
	}
    
    public int hashCode() { 
        int hash = 1;
        hash = hash * 31 + (new Long(linkID)).hashCode();
        hash = hash * 31 + (type == null ? 0 : type.hashCode());
        return hash;
    }
    
    //LINK METHODS

    public Link copy(Link l){
    	if(l instanceof LinkImpl)
    		return new LinkImpl((LinkImpl)l);
    	else
    		return null;
    }

	public Linkable getSink() {
		return sink;
	}

	public Linkable getSource() {
		return source;
	}

	public LinkType getType() {
		return type;
	}//

	public void setSink(Linkable sink) {
		this.sink = sink;
	}//

	public void setSource(Linkable source) {
		this.source = source;
	}//

	public void setType(LinkType type) {
		this.type = type;
	}//
    
	public String toString(){
		String s = label + " Source: ";
		s += source.getLabel();	
		s += ". Sink: ";
		s += sink.getLabel();	
		return s;
	}//
	
	//NODE METHODS
	public String getLabel() {
		return label;
	}

	public void decay() {
		System.out.println("decay not implemented");	
	}

	public void excite(double amount) {
		System.out.println("excite not implemented");
		//activation = amount;		
	}

	public double getCurrentActivation() {
		return activation;
	}

	public DecayBehavior getDecayBehavior() {
		System.out.println("decay not implemented");
		return null;
	}

	public ExciteBehavior getExciteBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getId() {
		return linkID;
	}

	public Node getReferencedNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public void setActivation(double d) {
		activation = d;
	}

	public void setDecayBehavior(DecayBehavior c) {
		// TODO Auto-generated method stub
		
	}

	public void setExciteBehavior(ExciteBehavior behavior) {
		// TODO Auto-generated method stub
		
	}

	public void setId(long id) {
		this.linkID = id;		
	}

	public void setLabel(String label) {
		this.label = label;
		
	}

	public void setReferencedNode(Node n) {
		// TODO Auto-generated method stub
		
	}

	public void setValue(Map<String, Object> values) {
		// TODO Auto-generated method stub
		
	}

	public Node copy(Node n) {
		// TODO Auto-generated method stub
		return null;
	}

}//class
