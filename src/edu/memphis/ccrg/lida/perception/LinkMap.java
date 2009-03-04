package edu.memphis.ccrg.lida.perception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import edu.memphis.ccrg.lida.util.M;
   
public class LinkMap {
	private Map<Linkable, Set<Link>> linkMap;
	private int linkCount = 0;//How many links have been added to this linkMap
	private Set<Node> nodes;
	private Map<Integer, List<Node>> layerMap;
	private double upscale = 0.5;
	private double selectivity = 0.9;

	public LinkMap(double upscale, double selectivity){
		linkMap = new HashMap<Linkable, Set<Link>>();
		nodes = new HashSet<Node>();
		layerMap = new HashMap<Integer, List<Node>>();
		this.upscale = upscale;
		this.selectivity = selectivity;
	}//public LinkMap()
	
	public LinkMap(LinkMap map){
		this(map.upscale, map.selectivity);
		this.linkCount = map.linkCount;
		Set<Linkable> keys = map.linkMap.keySet();
		Map<Node,Node> tempMap= new HashMap<Node, Node>();
		for(Linkable l: keys){
			Linkable newL = null;
			if(l instanceof Node){
				newL = new Node((Node)l);						
			
				Set<Link> newLinks = new HashSet<Link>();
				this.linkMap.put(newL,newLinks);
				tempMap.put((Node)l, (Node)newL);
			}
		}		
	}//public LinkMap
	
	public void addLinkSet(Set<Link> links){
		for(Link l: links)
			addLink(new Link(l));
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
	
	public void addNodes(Set<Node> nodesToAdd) {
		for(Node n: nodesToAdd){
			nodes.add(new Node(n));
			refresh();
		}
	}
	
	private void refresh() {        
        for(Integer layerDepth:(new TreeSet<Integer>(this.layerMap.keySet()))) 
           for(Node node:this.layerMap.get(layerDepth))
               refreshActivationParameters(node);           
    }//refresh
	
    public void refreshActivationParameters(Node n) {
        updateMinActivation(n);
        updateMaxActivation(n);
        updateSelectionThreshold(n);
    }
    
    /**
     * Updates the minimum activation possible for this node.
     * <p>
     * Since this method recursively invokes getMinActivation from its children,
     * it assumes that the children have already been updated.
     */
    private void updateMinActivation(Node n) {
        if(isBottomNode(n))
        	n.setMinActivation(n.MIN_NODE_ACTIVATION);
        else{
        	double sumOfChildMinActiv = 0.0;
        	Set<Node> children = getChildren(n);
            for(Node child: children)
            	sumOfChildMinActiv += child.getMinActivation();
            
            n.setMinActivation(sumOfChildMinActiv * upscale);            
        }  
    }
	
	private void updateMaxActivation(Node n){
	    if(isBottomNode(n))
	        n.setMaxActivation(n.MAX_NODE_ACTIVATION);
	    else{
	    	double sumOfChildMaxActiv = 0.0;
	    	Set<Node> children = getChildren(n);
	    	for(Node child: children)
	        	sumOfChildMaxActiv += child.getMaxActivation();
	        
	        n.setMaxActivation(sumOfChildMaxActiv * upscale);       
	    }    
	}//updateMaxActivation
	
	private void updateSelectionThreshold(Node n){
		//M.p(n.getLabel() + " is updating selection threshold");
		
		double min = n.getMinActivation();
		double max = n.getMaxActivation();
		double threshold = selectivity*(max - min) + min;
		n.setSelectionThreshold(threshold);
	}    	
	
	//TODO: How will this be called in deleteLinkable(), addParent(), addChild()?
    public int updateLayerDepth(Node n) {
        n.setLayerDepth(0);
        
        if(isBottomNode(n))
            n.setLayerDepth(0);
        else{
        	Set<Node> children = getChildren(n);
            int layerDepth[] = new int[children.size()];
            int ild = 0;
            for(Node child: children) {
                layerDepth[ild] = updateLayerDepth(child);
                ild++;
            }
            Arrays.sort(layerDepth);
            n.setLayerDepth(layerDepth[layerDepth.length - 1] + 1);
        }
        return n.getLayerDepth();
    }	
	
    protected Map<Integer, List<Node>> getLayerMap(){
        Map<Integer, List<Node>> layerMap = new HashMap<Integer, List<Node>>();
        for(Node node: this.nodes){
            Integer layerDepth = new Integer(node.getLayerDepth());
            List<Node> layerNodes = layerMap.get(layerDepth);
            
            if(layerNodes == null) {
                layerNodes = new ArrayList<Node>();
                layerMap.put(layerDepth, layerNodes);
            }
            layerNodes.add(node);
        }
        M.p(layerMap.size() + " is size of layerMap " + nodes.size() + " nodes" + linkMap.size() + " map size");
        return layerMap; 
    }//buildLayerMap
	
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
		linkMap.remove(n);//finally remove the linkable and its links		
	}//public void deleteNode(Linkable n)
	
	public void addChild(Node child, Node parent){	
		Link l = new Link(child, parent, LinkType.child, nextLinkID());
		
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
		Link l = new Link(child, parent, LinkType.child, nextLinkID());
		
		if(linkMap.get(child).add(l))
			linkCount++;
		
		Set<Link> parentsLinks = linkMap.get(parent);
		if(parentsLinks.equals(null)){
			parentsLinks = new HashSet<Link>();
			linkMap.put(parent, parentsLinks);
		}
		parentsLinks.add(l);	
		//TODO:DOUBLE CHECK
		updateLayerDepth(child);
		updateLayerDepth(parent);		
	}//addParent
	
	public long nextLinkID(){
		//TODO: Work on linkCount!!!!
		return linkCount;
	}
    
    public Set<Node> getNodes(){//TODO: will this be used to check the nodes supplied?
    	Set<Linkable> keys = linkMap.keySet();
    	Set<Node> nodes = new HashSet<Node>();
    	for(Linkable l: keys)
    		if(l instanceof Node)
    			nodes.add((Node)l);
    	return nodes;
    }
    
    public boolean isBottomNode(Node n) {
		Set<Link> links = linkMap.get(n);
		for(Link link: links){
			Linkable source = link.getSource();
			if(source instanceof Node && !source.equals(n))//if source is a child of n
				return false;
		}//for
		return true;
	}
    
	public boolean isTopNode(Node n) {
		Set<Link> links = linkMap.get(n);
		for(Link link: links){
			Linkable sink = link.getSink();
			if(sink instanceof Node && !sink.equals(n))//if source is a child of n
				return false;
		}//for
		return true;
	}

	public Set<Node> getChildren(Node n) {
		Set<Link> links = linkMap.get(n);
		Set<Node> children = new HashSet<Node>();
		for(Link link: links){
			Linkable source = link.getSource();
			if(source instanceof Node && !source.equals(n))
				children.add((Node)source);			
		}
		return children;		
	}
	
	public Set<Node> getParents(Node n) {
		Set<Link> links = linkMap.get(n);
		Set<Node> parents = new HashSet<Node>();
		for(Link link: links){
			Linkable sink = link.getSink();
			if(sink instanceof Node && !sink.equals(n))
				parents.add((Node)sink);			
		}
		return parents;
	}

	public void setSelectivity(Double o) {
		selectivity = o;		
	}

}//public class LinkMap
