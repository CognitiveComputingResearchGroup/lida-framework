package edu.memphis.ccrg.lida.shared;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.util.Printer;

public class PamNodeStructure extends NodeStructureImpl{
	
	private Map<Long, Node> nodes = new HashMap<Long, Node>();
	private Map<String, Link> links = new HashMap<String, Link>();
	private Map<Linkable, Set<Link>> linkableMap = new HashMap<Linkable, Set<Link>>();
	/**
	 * For each integer key there is a set of nodes that are
	 * at 
	 */
	private Map<Integer, Set<Node>> layerMap;
	
	
	/**
	 * This method is for adding nodes from perceptual memory.  It requires
	 * upscale and selectivity values from PAM to calculate the nodes' activation
	 * thresholds. 
	 */	
	public void addNodes(Set<Node> nodesToAdd, double upscale, double selectivity) {
		for(Node n: nodesToAdd){
			PamNodeImpl toAdd = (PamNodeImpl)n;
			addNode(toAdd);
			updateLayerDepth(toAdd);//TODO:  Currently layer depth is set manually.
		}
		createLayerMap();
		updateActivationThresholds(upscale, selectivity);
	}//method
	
	public Map<Integer, Set<Node>> createLayerMap() {
		for(Node node: nodes.values()){
            int layerDepth = ((PamNode)node).getLayerDepth();
            Set<Node> layerNodes = layerMap.get(layerDepth);
            
            if(layerNodes == null) {
                layerNodes = new HashSet<Node>();
                layerMap.put(layerDepth, layerNodes);
            }
            layerNodes.add(node);
        }
        return layerMap;
	}
	
	/**
	 * This method is suppose to magically take care of updating the layer depth
	 * of the supplied node.
	 * TODO: This method is unfinished/non-working.
	 * TODO: How will this be called in deleteLinkable(), addParent(), addChild()?
	 * @param n
	 * @return
	 */
    public int updateLayerDepth(PamNode n) {
        n.setLayerDepth(0);
        
        if(isBottomLinkable(n))
            n.setLayerDepth(0);
        else{
        	Set<PamNode> children = getChildren(n);
            int layerDepth[] = new int[children.size()];
            int ild = 0;
            for(PamNode child: children) {
                layerDepth[ild] = updateLayerDepth(child);
                ild++;
            }
            Arrays.sort(layerDepth);
            n.setLayerDepth(layerDepth[layerDepth.length - 1] + 1);
        }
        return n.getLayerDepth();
    }	

	/**
	 * Update the min and max activations and selection threshold
	 * of the Linkables in the layermap* 
	 * 
	 * @param upscale
	 * @param selectivity
	 */
	private void updateActivationThresholds(double upscale, double selectivity){		
        for(Integer layerDepth: layerMap.keySet()){
           for(Linkable n: layerMap.get(layerDepth)){
        	   PamNode temp = null;
        	   if(n instanceof PamNode){
        		   temp = (PamNode)n;
        		   updateMinActivation(temp, upscale);
        		   updateMaxActivation(temp, upscale);
        		   updateSelectionThreshold(temp, selectivity);      
        	   }  
           }
        }	
    }//updateActivationThresholds

    /**
     * Calc min activation based on the number of children and upscale
     */
    private void updateMinActivation(PamNode n, double upscale) {
    	
        if(isBottomLinkable(n))
        	n.setMinActivation(n.getDefaultMinActivation());
        else{
        	double sumOfChildMinActiv = 0.0;
        	Set<PamNode> children = getChildren(n);
            for(PamNode child: children)
            	sumOfChildMinActiv += child.getMinActivation();
            
            n.setMinActivation(sumOfChildMinActiv * upscale);            
        }  
    }
	
    /**
     * Calc max activation based on the number of children and upscale
     */
	private void updateMaxActivation(PamNode n, double upscale){
	    if(isBottomLinkable(n))
	        n.setMaxActivation(n.getDefaultMaxActivation());
	    else{
	    	double sumOfChildMaxActiv = 0.0;
	    	Set<PamNode> children = getChildren(n);
	    	for(PamNode child: children)
	        	sumOfChildMaxActiv += child.getMaxActivation();
	        
	        n.setMaxActivation(sumOfChildMaxActiv * upscale);       
	    }    
	}//updateMaxActivation
	
	/**
     * Calc selection threshold based on the selectivity and min and max activ.
     */
	private void updateSelectionThreshold(PamNode n, double selectivity){
		//M.p(n.getLabel() + " is updating selection threshold");
		
		double min = n.getMinActivation();
		double max = n.getMaxActivation();
		double threshold = selectivity*(max - min) + min;
		n.setSelectionThreshold(threshold);
	}   
	
	/** 
	 * @param n
	 * @return true if n has no children (it is at the 'bottom' of the network)
	 */
    public boolean isBottomLinkable(Linkable n) {
		Set<Link> links = linkableMap.get(n);
		if(links != null){
			for(Link link: links){
				Linkable source = link.getSource();
				if(source instanceof PamNode && !source.equals(n))//if source is a child of n
					return false;
			}//for
		}
		return true;
	}
    
	/** 
	 * @param n
	 * @return true if n has no parent (it is at the 'top' of the network)
	 */
	public boolean isTopLinkable(Linkable n) {
		Set<Link> links = linkableMap.get(n);
		if(links != null){
			for(Link link: links){
				Linkable sink = link.getSink();
				if(sink instanceof PamNode && !sink.equals(n))//if source is a child of n
					return false;
			}//for
		}
		return false;
	}

	public Set<Node> getParents(Node n) {
		Printer.p("getParents not impl!");
		return null;
	}
	
	/**
	 * Get children of this linkable
	 * @param n
	 * @return
	 */
	public Set<PamNode> getChildren(Linkable n) {
		Set<Link> links = linkableMap.get(n);
		Set<PamNode> children = new HashSet<PamNode>();
		if(links != null){
			for(Link link: links){
				Linkable source = link.getSource();
				if(source instanceof PamNodeImpl && !source.equals(n))
					children.add((PamNodeImpl)source);			
			}
		}
		return children;
	}

	//**PRINTING	
	public void printPamNodeActivations() {
		for(Node n: nodes.values())
			((PamNodeImpl)n).printActivationString();
	}

}//class
