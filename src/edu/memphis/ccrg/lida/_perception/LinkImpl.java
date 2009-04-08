package edu.memphis.ccrg.lida._perception;

import java.util.Map;

import edu.memphis.ccrg.lida._perception.LinkImpl;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Linkable;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;


/**
 *
 * @author Ryan McCall
 */
public class LinkImpl implements Link, Node{
    
    private Linkable sink;    
    private Linkable source;    
    private long linkID;
    
    private LinkType type; 
	private double activation = 0.0;
	private final double MIN_ACTIVATION = 0.0;
	private final double MAX_ACTIVATION = 1.0;
    
    public LinkImpl(Linkable source, Linkable sink, LinkType type, long id){        
        this.source = source;
        this.sink = sink;   
        this.type = type;
        linkID = id;
    }
    
    public LinkImpl(LinkImpl l){
    	sink = l.sink;
    	source = l.source;
    	type = l.type;
    	linkID = l.linkID;
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

    
    public LinkImpl copy(LinkImpl l){
    	return new LinkImpl(l);
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

	public long getId() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Link copy(Link l) {
		// TODO Auto-generated method stub
		return null;
	}

	public Node copy() {
		// TODO Auto-generated method stub
		return null;
	}

	public void decay() {
		// TODO Auto-generated method stub
		
	}

	public DecayBehavior getDecayBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public ExciteBehavior getExciteBehavior() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getIdentifier() {
		// TODO Auto-generated method stub
		return 0;
	}

	public double getImportance() {
		// TODO Auto-generated method stub
		return 0;
	}

	public Node getReferencedNode() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isRelevant() {
		// TODO Auto-generated method stub
		return false;
	}

	public void setActivation(double d) {
		// TODO Auto-generated method stub
		
	}

	public void setDecayBehavior(DecayBehavior c) {
		// TODO Auto-generated method stub
		
	}

	public void setExciteBehavior(ExciteBehavior behavior) {
		// TODO Auto-generated method stub
		
	}

	public void setReferencedNode(Node n) {
		// TODO Auto-generated method stub
		
	}

	public void setValue(Map<String, Object> values) {
		// TODO Auto-generated method stub
		
	}

	public void synchronize() {
		// TODO Auto-generated method stub
		
	}

	public void setID(long id) {
		// TODO Auto-generated method stub
		
	}

	public void setLabel(String label) {
		// TODO Auto-generated method stub
		
	}

	public void decay(DecayBehavior decayBehavior) {
		// TODO Auto-generated method stub
		
	}

	public int getLayerDepth() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void printActivationString() {
		// TODO Auto-generated method stub
		
	}
	
	public String toString(){
		String s = "Source: ";
		s += source.getLabel();	
		s += ". Sink: ";
		s += sink.getLabel();	
		return s;
	}
	
	public String getLabel(){
		return "link of type " + type + " id " + linkID;
	}

}
