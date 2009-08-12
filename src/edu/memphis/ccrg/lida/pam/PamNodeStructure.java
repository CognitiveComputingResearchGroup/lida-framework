package edu.memphis.ccrg.lida.pam;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.Linkable;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
//TODO: Check this class
public class PamNodeStructure extends NodeStructureImpl{
	
	private Double upscaleFactor = 0.7;
	private Double downscaleFactor = 0.5;
	private Double selectivityThreshold = 0.8;
	
	public PamNodeStructure(){
		super("PamNodeImpl", "LinkImpl");
	}

	public PamNodeStructure(String defaultPamNode, String defaultLink) {
		super(defaultPamNode, defaultLink);
	}

	public void setUpscale(Double d) {
		upscaleFactor  = d;		
	}

	public void setDownscale(Double d) {
		downscaleFactor  = d;		
	}

	public void setSelectivity(Double d) {
		selectivityThreshold = d;		
	}
	
	/**
	 * 
	 */	
	public void addPamNodes(Collection<PamNode> nodes){
		for(Node n: nodes)
			addNode(n);

		updateActivationThresholds(upscaleFactor, selectivityThreshold);
	}//method	

	/**
	 * Update the min and max activations and selection threshold
	 * of the Linkables in the layermap
	 * 
	 * @param upscale
	 * @param selectivity
	 */
	private void updateActivationThresholds(double upscale, double selectivity){
		Collection<Node> nodes = getNodes();
        for(Node n: nodes){
        	PamNode pNode = (PamNode)n;
        	updateMinActivation(pNode, upscale);
        	updateMaxActivation(pNode, upscale);
        	updateSelectionThreshold(pNode, selectivity);
        }//for	
    }//method

    /**
     * Calc min activation based on the number of children and upscale
     */
    private void updateMinActivation(PamNode n, double upscale) {
        if(isAtBottom(n))
        	n.setMinActivation(n.getDefaultMinActivation());
        else{
        	double sumOfChildMinActiv = 0.0;
        	Set<PamNode> children = getChildren(n);
            for(PamNode child: children)
            	sumOfChildMinActiv += child.getMinActivation();
            
            n.setMinActivation(sumOfChildMinActiv * upscale);            
        }  
    }//method
	
    /**
     * Calc max activation based on the number of children and upscale
     */
	private void updateMaxActivation(PamNode n, double upscale){
	    if(isAtBottom(n))
	        n.setMaxActivation(n.getDefaultMaxActivation());
	    else{
	    	double sumOfChildMaxActiv = 0.0;
	    	Set<PamNode> children = getChildren(n);
	    	for(PamNode child: children)
	        	sumOfChildMaxActiv += child.getMaxActivation();
	        
	        n.setMaxActivation(sumOfChildMaxActiv * upscale);       
	    }    
	}//method
	
	/**
     * Calc selection threshold based on the selectivity and min and max activ.
     */
	private void updateSelectionThreshold(PamNode n, double selectivity){
		double min = n.getMinActivation();
		double max = n.getMaxActivation();
		double threshold = selectivity*(max - min) + min;
		n.setSelectionThreshold(threshold);
	}   
	
	/** 
	 * @param n
	 * @return true if n has no children (it is at the 'bottom' of the network)
	 */
    public boolean isAtBottom(Linkable n) {
		Set<Link> links = getLinkableMap().get(n);
		if(links != null){
			for(Link link: links){
				Linkable source = link.getSource();
				//if source is a child of n
				if(source instanceof PamNode && !source.equals(n))
					return false;
			}//for
		}//
		return true;
	}//method
    
	/** 
	 * @param n
	 * @return true if n has no parent (it is at the 'top' of the network)
	 */
	public boolean isAtTop(Linkable n) {
		Set<Link> links = getLinkableMap().get(n);
		if(links != null){
			for(Link l: links){
				Linkable sink = l.getSink();
				//if sink is a parent of n
				if(sink instanceof PamNode && !sink.equals(n))
					return false;
			}//for
		}
		return true;
	}//method
	
	//************END OF ADDING NODE RELATED METHODS*************	

	public void setNodesExciteBehavior(ExciteBehavior behavior) {
    	for(Node n: getNodes())
    		n.setExciteBehavior(behavior);
	}//method
	
	public void setNodesDecayBehavior(DecayBehavior behavior) {
    	for(Node n: getNodes())
    		n.setDecayBehavior(behavior);
	}//method
	
	/**
	 * Pass activation upward starting from the set of nodes
	 * provided in the argument.
	 * 
	 * TODO: Vunerable to cyclic node connections. Ensure elsewhere this won't happen?  
	 * 
	 * @param layerOfNodes
	 */
	public void passActivationUpward(Set<PamNode> layerOfNodes) {
		Set<PamNode> parents = new HashSet<PamNode>();
		while(layerOfNodes.size() != 0){	
			for(PamNode n: layerOfNodes){
				n.synchronize();
		  		double currentActivation = n.getActivation();
		  		parents = getParents(n);
		  		for(PamNode parent: parents){ 
		  			parent.excite(currentActivation * upscaleFactor);
		  			parents.add(parent);
		  		}//for each parent
		  		layerOfNodes = parents;
			}//for 
		}//while
	}//method
	
	public void passActivationDownward(Set<PamNode> layerOfNodes) {
		Set<PamNode> children = new HashSet<PamNode>();
		while(layerOfNodes.size() != 0){	
			for(PamNode n: layerOfNodes){
				n.synchronize();
		  		double currentActivation = n.getActivation();
		  		children = getChildren(n);
		  		for(PamNode child: children){ 
		  			child.excite(currentActivation * downscaleFactor);
		  			children.add(child);
		  		}//for each parent
		  		layerOfNodes = children;
			}//for 
		}//while
	}//method

	/**
	 * Get parents of this linkable. 
	 * O(l) where l = number of links connected to n.
	 * 
	 * @param n
	 * @return
	 */
	public Set<PamNode> getParents(Node n) {
		Set<PamNode> parents = new HashSet<PamNode>();
		Set<Link> links = getLinkableMap().get(n);
		if(links != null){
			for(Link l: links){
				Linkable sink = l.getSink();//Sinks are 'above' this node. 
				if(sink instanceof PamNode && !sink.equals(n))
					parents.add((PamNode) sink);
			}
		}
		return parents;
	}//method 
	
	/**
	 * Get children of this linkable. 
	 * O(l) where l = number of links connected to n.
	 * 
	 * @param n
	 * @return set of child nodes
	 */
	public Set<PamNode> getChildren(Linkable n) {
		Set<PamNode> children = new HashSet<PamNode>();
		Set<Link> links = getLinkableMap().get(n);
		if(links != null){
			for(Link l: links){
				Linkable source = l.getSource();//Sources are 'below' this node.
				if(source instanceof PamNode && !source.equals(n))
					children.add((PamNode)source);			
			}
		}
		return children;
	}//method

	public void decayNodes(){
		for(Node n: getNodes())
			n.decay();
	}//method
	
	/**
	 * Simple utility method
	 */
	public void printPamNodeActivations() {
		for(Node n: getNodes())
			((PamNodeImpl)n).printActivationString();
	}//method

}//class