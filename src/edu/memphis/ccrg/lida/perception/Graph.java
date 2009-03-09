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
   
public class Graph {
	private Map<Linkable, Set<Link>> linkMap;
	private int linkCount = 0;//How many links have been added to this linkMap
	private Set<Node> nodes;
	private Map<Integer, Set<Linkable>> layerMap;
	private double upscale = 0.5;
	private double selectivity = 0.9;

	public Graph(double upscale, double selectivity){
		linkMap = new HashMap<Linkable, Set<Link>>();
		nodes = new HashSet<Node>();
		layerMap = new HashMap<Integer, Set<Linkable>>();
		this.upscale = upscale;
		this.selectivity = selectivity;
	}//public LinkMap()
	
	public Graph(Graph map){
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
			nodes.add(n);
			//updateLayerDepth(n);//TODO:  Currently layer depth is set manually.
		}
		createLayerMap();
		updateActivationThresholds();
	}
	
	//TODO: How will this be called in deleteLinkable(), addParent(), addChild()?
    public int updateLayerDepth(Node n) {
        n.setLayerDepth(0);
        
        if(isBottomLinkable(n))
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
	
	public Map<Integer, Set<Linkable>> createLayerMap(){
        for(Node node: nodes){
            int layerDepth = node.getLayerDepth();
            Set<Linkable> layerNodes = layerMap.get(layerDepth);
            
            if(layerNodes == null) {
                layerNodes = new HashSet<Linkable>();
                layerMap.put(layerDepth, layerNodes);
            }
            layerNodes.add(node);
        }
        return layerMap;
    }//createLayerMap

	private void updateActivationThresholds(){		
        for(Integer layerDepth: layerMap.keySet()){
           for(Linkable n: layerMap.get(layerDepth)){
        	   updateMinActivation(n);
               updateMaxActivation(n);
               updateSelectionThreshold(n);        
           }
        }	
    }//updateActivationThresholds

    /**
     * Updates the minimum activation possible for this node.
     * <p>
     * Since this method recursively invokes getMinActivation from its children,
     * it assumes that the children have already been updated.
     */
    private void updateMinActivation(Linkable n) {
    	
        if(isBottomLinkable(n))
        	n.setMinActivation(n.getDefaultMinActivation());
        else{
        	double sumOfChildMinActiv = 0.0;
        	Set<Node> children = getChildren(n);
            for(Node child: children)
            	sumOfChildMinActiv += child.getMinActivation();
            
            n.setMinActivation(sumOfChildMinActiv * upscale);            
        }  
    }
	
	private void updateMaxActivation(Linkable n){
	    if(isBottomLinkable(n))
	        n.setMaxActivation(n.getDefaultMaxActivation());
	    else{
	    	double sumOfChildMaxActiv = 0.0;
	    	Set<Node> children = getChildren(n);
	    	for(Node child: children)
	        	sumOfChildMaxActiv += child.getMaxActivation();
	        
	        n.setMaxActivation(sumOfChildMaxActiv * upscale);       
	    }    
	}//updateMaxActivation
	
	private void updateSelectionThreshold(Linkable n){
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
				if(source instanceof Node && !source.equals(n))//if source is a child of n
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
				if(sink instanceof Node && !sink.equals(n))//if source is a child of n
					return false;
			}//for
		}
		return false;
	}

	public Set<Node> getChildren(Linkable n) {
		Set<Link> links = linkMap.get(n);
		Set<Node> children = new HashSet<Node>();
		for(Link link: links){
			Linkable source = link.getSource();
			if(source instanceof Node && !source.equals(n))
				children.add((Node)source);			
		}
		return children;		
	}
	
	public Set<Linkable> getParents(Linkable l) {
		String s =  l.getLabel();
		
		Set<Linkable> keys = linkMap.keySet();
		Linkable whatIwant = null;

		for(Linkable l2: keys){
//			if(l2.equals(1))
//				System.out.println("SDFSDSDFSDFA");
			if(l2.getID() == l.getID())
				whatIwant = l2;
		}
		
		//Set<Link> links = linkMap.get(l);
		Set<Link> links = linkMap.get(whatIwant);
		Set<Linkable> parents = new HashSet<Linkable>();
		if(links != null){
			for(Link link: links){
				
				Linkable sink = link.getSink();
				Linkable source = link.getSource();
				
				//System.out.println("Linkable is " + whatIwant.getLabel() + " link has source " + source.getLabel() + " has parent " + sink.getLabel());
				
				if(sink instanceof Node && (whatIwant.getID() != sink.getID())/*sink.equals(l)*/){
					//M.p(link.getSource().getLabel() + " has parent " + sink.getLabel());
					parents.add((Node)sink);			
				}
			}
			//M.p("");
		}
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
		Link l = new Link(child, parent, LinkType.child, (int)(99999*Math.random()));
		
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
		Link l = new Link(child, parent, LinkType.child, (int)(99999*Math.random()));
		
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
			System.out.println("Linkable " + key.getLabel() + " has links ");
			Set<Link> links = linkMap.get(key);
			for(Link l: links)
				System.out.println("Source: " + l.getSource().toString() + " sink " + l.getSink().toString());
			System.out.println();
		}
		
	}
}//public class LinkMap
