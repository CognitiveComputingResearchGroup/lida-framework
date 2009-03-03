package edu.memphis.ccrg.lida.perception;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
   
public class LinkMap {
	private Map<Linkable, Set<Link>> linkMap;
	private int linkCount = 0;//How many links have been added to this linkMap

	public LinkMap(){
		linkMap = new HashMap<Linkable, Set<Link>>();
	}//public LinkMap()
	
	public LinkMap(LinkMap map){
		this();
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
			addLink(l);
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
        Set<Node> nodes = getNodes();
        for(Node node: nodes){
            Integer layerDepth = new Integer(node.getLayerDepth());
            List<Node> layerNodes = layerMap.get(layerDepth);
            
            if(layerNodes == null) {
                layerNodes = new ArrayList<Node>();
                layerMap.put(layerDepth, layerNodes);
            }
            layerNodes.add(node);
        }
        
        return layerMap; 
    }//buildLayerMap
    
    public Set<Node> getNodes(){
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

}//public class LinkMap
