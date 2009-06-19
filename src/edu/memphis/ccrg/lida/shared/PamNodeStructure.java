package edu.memphis.ccrg.lida.shared;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.perception.PamNodeImpl;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;

public class PamNodeStructure extends NodeStructureImpl{
	
	private Map<Long, PamNode> nodes = new HashMap<Long, PamNode>();
	private Map<String, Link> links = new HashMap<String, Link>();
	private Map<Linkable, Set<Link>> linkableMap = new HashMap<Linkable, Set<Link>>();
	private NodeFactory factory = NodeFactory.getInstance();
	private String defaultNode = "edu.memphis.ccrg.lida.perception.PamNodeImpl";
	
	private Map<Integer, Set<PamNode>> layerDepthMap = new HashMap<Integer, Set<PamNode>>();
	private Double upscaleFactor = 0.7;
	private Double downscaleFactor = 0.5;
	private Double selectivityThreshold = 0.8;

	public void setUpscale(Double d) {
		upscaleFactor  = d;		
	}

	public void setDownscale(Double d) {
		downscaleFactor  = d;		
	}

	public void setSelectivity(Double d) {
		selectivityThreshold = d;		
	}
	
	private Node addPamNode(PamNode n) {
		if (!nodes.keySet().contains(n.getId())){
			nodes.put(n.getId(), n);
			linkableMap.put(n, null);
			return n;
		}
		return null;
	}//method
	
	/**
	 * 
	 */	
	public void addPamNodes(Collection<Node> nodes){
		for(Node n: nodes){
			PamNodeImpl temp = (PamNodeImpl)n;
			if(temp.getLayerDepth() == PamNodeImpl.DEFAULT_DEPTH){
				try {
					throw new Exception();
				}catch(Exception e){
					System.out.println("Cant add pam node w/o first setting its layer depth");
					e.printStackTrace();
				}
			}else
				addPamNode(temp);
		}//for
		
		refreshLayerMap();
		updateActivationThresholds(upscaleFactor, selectivityThreshold);
	}//method	
	
//	/**
//   * Will need this eventually if we learn new nodes while the system runs
//	 */
//    public int updateLayerDepth(PamNode n) {
//        n.setLayerDepth(0);
//        
//        if(isAtBottom(n))
//            n.setLayerDepth(0);
//        else{
//        	Set<PamNode> children = getChildren(n);
//            int layerDepth[] = new int[children.size()];
//            int ild = 0;
//            for(PamNode child: children) {
//                layerDepth[ild] = updateLayerDepth(child);
//                ild++;
//            }
//            Arrays.sort(layerDepth);
//            n.setLayerDepth(layerDepth[layerDepth.length - 1] + 1);
//        }
//        return n.getLayerDepth();
//    }	
    
	/**
	 * 
	 */
	public void refreshLayerMap() {
		layerDepthMap.values().clear();
		for(Node node: nodes.values()){
            int layerDepth = ((PamNode)node).getLayerDepth();
            Set<PamNode> layerNodes = layerDepthMap.get(layerDepth);
            if(layerNodes == null) {
                layerNodes = new HashSet<PamNode>();
                layerDepthMap.put(layerDepth, layerNodes);
            }
            layerNodes.add((PamNode) node);
        }//for
	}//method

	/**
	 * Update the min and max activations and selection threshold
	 * of the Linkables in the layermap* 
	 * 
	 * @param upscale
	 * @param selectivity
	 */
	private void updateActivationThresholds(double upscale, double selectivity){	
		int layers = layerDepthMap.keySet().size();
        for(int i = 0; i < layers; i++){
           for(Linkable n: layerDepthMap.get(i)){
        	   PamNode pNode = (PamNode)n;
        	   updateMinActivation(pNode, upscale);
        	   updateMaxActivation(pNode, upscale);
        	   updateSelectionThreshold(pNode, selectivity);
           }//for
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
		Set<Link> links = linkableMap.get(n);
		if(links != null){
			for(Link link: links){
				Linkable source = link.getSource();
				//if source is a child of n
				if(source instanceof PamNode && !source.equals(n))
					return false;
			}//for
		}
		return true;
	}//method
    
	/** 
	 * @param n
	 * @return true if n has no parent (it is at the 'top' of the network)
	 */
	public boolean isAtTop(Linkable n) {
		Set<Link> links = linkableMap.get(n);
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

	public void setExciteBehavior(ExciteBehavior behavior) {
    	for(PamNode n: nodes.values())
    		n.setExciteBehavior(behavior);
	}
	
	public void setDecayBehavior(DecayBehavior behavior) {
    	for(PamNode n: nodes.values())
    		n.setDecayBehavior(behavior);
	}
	
	public void passActivation() {
		int layers = layerDepthMap.keySet().size();
        for(int i = 0; i < layers; i++){
        	Set<PamNode> layerNodes = layerDepthMap.get(i);
            for(PamNode n: layerNodes){
      			double currentActivation = n.getActivation();
      			Set<PamNode> parents = getParents(n);
      			for(PamNode parent: parents){ 
      				parent.excite(currentActivation * upscaleFactor);
      			}//for each parent
      		}//for each node
      	}//for each layer
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
		Set<Link> links = linkableMap.get(n);
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
		Set<Link> links = linkableMap.get(n);
		if(links != null){
			for(Link l: links){
				Linkable source = l.getSource();//Sources are 'below' this node.
				if(source instanceof PamNode && !source.equals(n))
					children.add((PamNode)source);			
			}
		}
		return children;
	}//method
	
	public void clearNodes(){
		nodes.clear();
	}

	/**
	 * Simple utility method
	 */
	public void printPamNodeActivations() {
		for(Node n: nodes.values())
			((PamNodeImpl)n).printActivationString();
	}//method

	public void decayNodes() {
		for(PamNode n: nodes.values())
			n.decay();
	}

}//class