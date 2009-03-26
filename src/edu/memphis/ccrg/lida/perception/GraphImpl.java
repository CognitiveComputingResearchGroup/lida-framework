package edu.memphis.ccrg.lida.perception;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.perception.interfaces.PamNode;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Linkable;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeFactory;
import edu.memphis.ccrg.lida.shared.NodeStructure;
   
public class GraphImpl implements NodeStructure{
	private double upscale = 0.5;
	private double selectivity = 0.9;	
	private int linkCount = 0;//How many links have been added to this linkMap
	
	private Set<Node> nodes;
	private Map<Linkable, Set<Link>> linkMap;
	private Map<Integer, Set<Node>> layerMap;
	
	public GraphImpl(double upscale, double selectivity){
		this.upscale = upscale;
		this.selectivity = selectivity;		
		
		nodes = new HashSet<Node>();
		linkMap = new HashMap<Linkable, Set<Link>>();
		layerMap = new HashMap<Integer, Set<Node>>();		
	}//public LinkMap()
	
	public GraphImpl(GraphImpl oldGraph){
		this.upscale = oldGraph.upscale;
		this.selectivity = oldGraph.selectivity;
		this.linkCount = oldGraph.linkCount;
		
		nodes = new HashSet<Node>();
		linkMap = new HashMap<Linkable, Set<Link>>();
		layerMap = new HashMap<Integer, Set<Node>>();	
		
		Set<Node> oldNodes = oldGraph.getNodes();
		if(oldNodes != null)
			for(Node n: oldNodes)
				this.nodes.add(n);//NodeFactory.getInstance().getNode(n)

		Map<Linkable, Set<Link>> oldLinkMap = oldGraph.getLinkMap();
		if(oldLinkMap != null){
			Set<Linkable> oldKeys = oldLinkMap.keySet();
			if(oldKeys != null){
				for(Linkable l: oldKeys){
					if(l instanceof LinkImpl){
						LinkImpl castLink = (LinkImpl)l;
						this.linkMap.put(new LinkImpl(castLink), new HashSet<Link>());
					}else if(l instanceof PamNodeImpl){
						PamNodeImpl castNode = (PamNodeImpl)l;
						this.linkMap.put(new PamNodeImpl(castNode), new HashSet<Link>());
					}
				}
			}
		}
		
		//TODO: COPY LINKS INTO THE LINKMAP
		
	}//public LinkMap
	
	public void addLinkSet(Set<Link> links){
		for(Link l: links){
			LinkImpl toAdd = (LinkImpl)l;
			addLink(new LinkImpl(toAdd));
		}
	}//public void addLinkSet(Set<Link> links)

	public boolean addLink(Link l){
		boolean result1 = false;
		boolean result2 = false;
		Linkable end = l.getSource();
		Set<Link> tempLinks = linkMap.get(end);
		if(tempLinks == null){
			tempLinks=new HashSet<Link>();
			linkMap.put(end, tempLinks);
		}
		result1 = tempLinks.add(l);
		
		end = l.getSink();
		tempLinks = linkMap.get(end);
		if(tempLinks == null){
			tempLinks = new HashSet<Link>();
			linkMap.put(end, tempLinks);
		}
		result2 = tempLinks.add(l);
		boolean result = result1 || result2;
		if(result)
			linkCount++;
		return result;
	}//public boolean addLink(Link l)
	
	public boolean addNode(Node n) {
		return nodes.add(n);
	}
	
	public void addNodes(Set<Node> nodesToAdd) {
		for(Node n: nodesToAdd){
			PamNodeImpl toAdd = (PamNodeImpl)n;
			nodes.add(toAdd);
			//updateLayerDepth(n);//TODO:  Currently layer depth is set manually.
		}
		createLayerMap();
		updateActivationThresholds();
	}
	
	//TODO: How will this be called in deleteLinkable(), addParent(), addChild()?
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
	
	public Map<Integer, Set<Node>> createLayerMap(){
        for(Node node: nodes){
            int layerDepth = node.getLayerDepth();
            Set<Node> layerNodes = layerMap.get(layerDepth);
            
            if(layerNodes == null) {
                layerNodes = new HashSet<Node>();
                layerMap.put(layerDepth, layerNodes);
            }
            layerNodes.add(node);
        }
        return layerMap;
    }//createLayerMap

	private void updateActivationThresholds(){		
        for(Integer layerDepth: layerMap.keySet()){
           for(Linkable n: layerMap.get(layerDepth)){
        	   PamNode temp = null;
        	   if(n instanceof PamNode){
        		   temp = (PamNode)n;
        		   updateMinActivation(temp);
        		   updateMaxActivation(temp);
        		   updateSelectionThreshold(temp);      
        	   }  
           }
        }	
    }//updateActivationThresholds

    /**
     * Updates the minimum activation possible for this node.
     * <p>
     * Since this method recursively invokes getMinActivation from its children,
     * it assumes that the children have already been updated.
     */
    private void updateMinActivation(PamNode n) {
    	
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
	
	private void updateMaxActivation(PamNode n){
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
	
	private void updateSelectionThreshold(PamNode n){
		//M.p(n.getLabel() + " is updating selection threshold");
		
		double min = n.getMinActivation();
		double max = n.getMaxActivation();
		double threshold = selectivity*(max - min) + min;
		n.setSelectionThreshold(threshold);
	}    
	
    public boolean isBottomLinkable(Linkable n) {
		Set<Link> links = linkMap.get(n);
		if(links != null){
			for(Link link: links){
				Linkable source = link.getSource();
				if(source instanceof PamNodeImpl && !source.equals(n))//if source is a child of n
					return false;
			}//for
		}
		return true;
	}
    
	public boolean isTopNode(Linkable n) {
		Set<Link> links = linkMap.get(n);
		if(links != null){
			for(Link link: links){
				Linkable sink = link.getSink();
				if(sink instanceof PamNodeImpl && !sink.equals(n))//if source is a child of n
					return false;
			}//for
		}
		return false;
	}

	public Set<PamNode> getChildren(Linkable n) {
		Set<Link> links = linkMap.get(n);
		Set<PamNode> children = new HashSet<PamNode>();
		for(Link link: links){
			Linkable source = link.getSource();
			if(source instanceof PamNodeImpl && !source.equals(n))
				children.add((PamNodeImpl)source);			
		}
		return children;		
	}
	
	public Set<Node> getParents(Node n) {
//		String s =  l.getLabel();
//		
//		Set<Linkable> keys = linkMap.keySet();
//		Linkable whatIwant = null;
//
//		for(Linkable l2: keys){
////			if(l2.equals(1))
////				System.out.println("SDFSDSDFSDFA");
//			if(l2.getID() == l.getID())
//				whatIwant = l2;
//		}
//		Set<LinkImpl> links = linkMap.get(whatIwant);
		
		Set<Link> links = linkMap.get(n);

		Set<Node> parents = new HashSet<Node>();
		if(links != null){
			for(Link link: links){
				
				Linkable sink = link.getSink();
				Linkable source = link.getSource();
		
				if(sink instanceof PamNodeImpl && !sink.equals(n)){
					//M.p(link.getSource().getLabel() + " has parent " + sink.getLabel());
					parents.add((PamNodeImpl)sink);			
				}
			}//for each link of n
		}//if links is not null
		return parents;
	}
	
	
	/**
	 * You would still need to delete the link object l!
	 * 
	 * @param l
	 */
	public void deleteLink(Link l){
		Set<Link> sourceLinks = linkMap.get(l.getSource());
		Set<Link> sinkLinks = linkMap.get(l.getSink());
		
		if(sourceLinks != null)
			sourceLinks.remove(l);
		
		if(sinkLinks != null)
			sinkLinks.remove(l);	
	
	}//public void deleteLink(Link l)
	
	public int getLinkCount(){
		return linkCount;
	}
	
	public Set<Link> getLinks(Linkable l){
		return linkMap.get(l);	
	}//public Set<Link> getLinks(Node n)
	
	public Set<Link> getLinks(Linkable NorL, LinkType type){
		Set<Link> result = linkMap.get(NorL);
		if(result != null){
			for(Link l: result){
				if(l.getType() != type)//remove links that don't match specified type
					result.remove(l);
			}//for each link
		}//result != null
		return result;
	}//public Set<Link> getLinks(Node n, LinkType type)
 	
	//TODO: Happens to the other nodes if we delete a Linkable that connects them?
	//TODO: What is their layer depth then?22
	public void deleteLinkable(Linkable n){
		Set<Link> tempLinks = linkMap.get(n);
		Set<Link> otherLinks;
		Linkable other;
		
		for(Link l: tempLinks){
			other = l.getSink(); 
			if(!other.equals(n)){ 
				otherLinks = linkMap.get(other);
				if(otherLinks != null)
					otherLinks.remove(l);
			}			
			other = l.getSource();
			if(!other.equals(n)){
				otherLinks = linkMap.get(other);
				if(otherLinks != null)
					otherLinks.remove(l);
			}						
		}//for all of the links connected to n		
		if(linkMap.remove(n) != null && n instanceof LinkImpl)//finally remove the linkable and its links
			linkCount--;
	}//public void deleteNode(Linkable n)
	
	public void addChild(PamNodeImpl child, PamNodeImpl parent){	
		LinkImpl l = new LinkImpl(child, parent, LinkType.child, (int)(99999*Math.random()));
		
		if(linkMap.get(parent).add(l))//Add new link to parent's links
			linkCount++;
			
		Set<Link> childsLinks = linkMap.get(child);
		if(childsLinks.equals(null)){//If child is not in the map
			childsLinks = new HashSet<Link>();
			linkMap.put(child, childsLinks);
		}		
		childsLinks.add(l);
		
		//TODO:DOUBLE CHECK
		updateLayerDepth(child);
		updateLayerDepth(parent);
	}//addChild
	
	public void addParent(Node parent, Node child){
		LinkImpl l = new LinkImpl(child, parent, LinkType.child, (int)(99999*Math.random()));
		
		if(linkMap.get(child).add(l))
			linkCount++;
		
		Set<Link> parentsLinks = linkMap.get(parent);
		if(parentsLinks.equals(null)){
			parentsLinks = new HashSet<Link>();
			linkMap.put(parent, parentsLinks);
		}
		parentsLinks.add(l);	
		//TODO:DOUBLE CHECK
//		updateLayerDepth(child);
//		updateLayerDepth(parent);		
	}//addParent
    
    public Set<Node> getNodes(){    	
    	return nodes;
    }
    
    public boolean nodesMatchLinkMap(){
//    	Set<Linkable> keys = linkMap.keySet();
//    	Set<Node> nodes = new HashSet<Node>();
//    	for(Linkable l: keys)
//    		if(l instanceof Node)
//    			nodes.add((Node)l);
//    	
    	
    	return true;
    }
    

	public void setSelectivity(Double o) {
		selectivity = o;		
	}

	public void printNodeActivations() {
		for(Node n: nodes)
			n.printActivationString();
	}

	public void printLinkMap() {
		Set<Linkable> keys = linkMap.keySet();
		
		for(Linkable key: keys){
		//	System.out.println("Linkable " + key.getLabel() + " has links ");
			Set<Link> links = linkMap.get(key);
			for(Link l: links)
				System.out.println("Source: " + l.getSource().toString() + " sink " + l.getSink().toString());
			System.out.println();
		}
		
	}

	public NodeStructure copy() {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteNode(Node n) {
		// TODO Auto-generated method stub
		
	}

	//???
	public Set<Link> getLinks() {
		return null;
	}

	public Set<Link> getLinks(LinkType type) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Map<Linkable, Set<Link>> getLinkMap(){
		return linkMap;
	}

	public void setDefaultLink(String linkClassName) {
		// TODO Auto-generated method stub
		
	}

	public void setDefaultNode(String nodeClassName) {
		// TODO Auto-generated method stub
		
	}
}//public class LinkMap
