package edu.memphis.ccrg.lida.wumpusWorld.d_perception;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.shared.Link;
import edu.memphis.ccrg.lida.shared.LinkImpl;
import edu.memphis.ccrg.lida.shared.LinkType;
import edu.memphis.ccrg.lida.shared.Linkable;
import edu.memphis.ccrg.lida.shared.Node;
import edu.memphis.ccrg.lida.shared.NodeStructure;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
   
public class RyanNodeStructure implements NodeStructure, WorkspaceContent, BroadcastContent{

	/**
	 * TODO: the counting functionality needs work
	 * How many links have been added to this linkMap
	 */
	private int linkCount = 0;
	
	/**
	 * Nodes of the graph
	 */
	private Set<Node> nodes;
	
	/**
	 * Map of the nodes by ID
	 */
	private Map<Long, Node> nodeMap;
	
	/**
	 * 
	 */
	private Map<String, Link> linkMap;
	
	/**
	 * Nodes and Links are keys and the values are their links
	 */
	private Map<Linkable, Set<Link>> linkableMap;
	
	/**
	 * For each integer key there is a set of nodes that are
	 * at 
	 */
	private Map<Integer, Set<Node>> layerMap;

	private String defaultLinkName = "LinkImpl";

	private String defaultNodeName = "PamNodeImplW";
	
	/**
	 * Default Constructor
	 */
	public RyanNodeStructure() {
		nodes = new HashSet<Node>();
		nodeMap = new HashMap<Long, Node>();
		linkMap =  new HashMap<String, Link>();
		linkableMap = new HashMap<Linkable, Set<Link>>();
		layerMap = new HashMap<Integer, Set<Node>>();	
	}
	
	/**
	 * Copy constructor 
	 * @param oldGraph
	 */	
	public RyanNodeStructure(RyanNodeStructure oldGraph){
		this.linkCount = oldGraph.linkCount;
		
		nodes = new HashSet<Node>();
		nodeMap = new HashMap<Long, Node>();
		linkMap = new HashMap<String, Link>();
		linkableMap = new HashMap<Linkable, Set<Link>>();
		layerMap = new HashMap<Integer, Set<Node>>();	
		
		Collection<Node> oldNodes = oldGraph.getNodes();
		if(oldNodes != null){
			for(Node n: oldNodes){
				this.nodes.add(n);//NodeFactory.getInstance().getNode(n)
				nodeMap.put(n.getId(), n);
			}
		}
		
		Collection<Link> oldLinks = oldGraph.getLinks();
		if(oldLinks != null){
			for(Link l: oldLinks){
				LinkImpl temp = (LinkImpl)l;
				linkMap.put(temp.getId(), temp);				
			}
		}

		Map<Linkable, Set<Link>> oldLinkMap = oldGraph.getLinkableMap();
		if(oldLinkMap != null){
			Set<Linkable> oldKeys = oldLinkMap.keySet();
			if(oldKeys != null){
				for(Linkable l: oldKeys){
					if(l instanceof LinkImpl){
						LinkImpl castLink = (LinkImpl)l;
						this.linkableMap.put(new LinkImpl(castLink), new HashSet<Link>());
					}else if(l instanceof RyanPamNode){
						RyanPamNode castNode = (RyanPamNode)l;
						this.linkableMap.put(new RyanPamNode(castNode), new HashSet<Link>());
					}
				}
			}
		}
		//TODO: COPY LINKS INTO THE LINKMAP
	}//constructor

	/**
	 * Add multiple links to this Graph
	 */
	public void addLinks(Collection<Link> links){
		for(Link l: links)
			addLink(l);
	}//public void addLinkSet(Set<Link> links)

	/**
	 * Adds specified link to the LinkMap
	 * For both ends of the link (the source and the sink) which are linkables, new 
	 * entries are added to linkMap if the linkables are not currently present in the linkmap
	 * otherwise if the linkable is present, then add this link to the set of links (value of the linkMap)
	 * Finally, put the link in the linkMap if it isnt already there and up the link count.
	 * 
	 *  @param Link l 
	 */
	public Link addLink(Link l){
		boolean result1 = false;
		boolean result2 = false;
		Linkable end = l.getSource();
		Set<Link> tempLinks = linkableMap.get(end);
		if(tempLinks == null){
			tempLinks=new HashSet<Link>();
			linkableMap.put(end, tempLinks);
		}
		result1 = tempLinks.add(l);
		
		end = l.getSink();
		tempLinks = linkableMap.get(end);
		if(tempLinks == null){
			tempLinks = new HashSet<Link>();
			linkableMap.put(end, tempLinks);
		}
		result2 = tempLinks.add(l);
		boolean result = result1 || result2;
		
		if(!linkableMap.containsKey(l))
			linkableMap.put(l, new HashSet<Link>());
		
		LinkImpl temp = (LinkImpl)l;
		if(!linkMap.containsKey(temp.getId())){
			linkMap.put(temp.getId(), temp);
		}else
			System.out.println(l.getIds() + " result was not added");
		
		if(result){
			linkCount++;
			return l;
		}else{
			return null;
		}
	}//method
	
	/**
	 * Adds specified node to the set of nodes. Returns true if node not already present.
	 */
	public Node addNode(Node n) {
		boolean result = nodes.add(n);
		boolean result2 = false;
		if(null == nodeMap.put(n.getId(), n))
			result2 = true;
		
		if(result && result2)
			return n;
		else
			return null;
	}

	public void addNodes(Collection<Node> nodesToAdd) {
		System.out.println("not implemented for this class");		
	}
	
	/**
	 * This method is for adding nodes from perceptual memory.  It requires
	 * upscale and selectivity values from PAM to calculate the nodes' activation
	 * thresholds. 
	 */	
	public void addNodes(Set<Node> nodesToAdd, double upscale, double selectivity) {
		for(Node n: nodesToAdd){
			RyanPamNode toAdd = (RyanPamNode)n;
			nodes.add(toAdd);
			nodeMap.put(n.getId(), n);
			//updateLayerDepth(n);//TODO:  Currently layer depth is set manually.
		}
		createLayerMap();
		updateActivationThresholds(upscale, selectivity);
	}//method
	
	public void addChild(RyanPamNode child, RyanPamNode parent){	
		LinkImpl l = new LinkImpl(child, parent, LinkType.CHILD, (int)(99999*Math.random()) + "222222222222222");
		linkMap.put(l.getId(), l);
		
		if(linkableMap.get(parent).add(l))//Add new link to parent's links
			linkCount++;
			
		Set<Link> childsLinks = linkableMap.get(child);
		if(childsLinks.equals(null)){//If child is not in the map
			childsLinks = new HashSet<Link>();
			linkableMap.put(child, childsLinks);
		}		
		childsLinks.add(l);
		
		//TODO:DOUBLE CHECK
		updateLayerDepth(child);
		updateLayerDepth(parent);
	}//addChild
	
	public void addParent(Node parent, Node child){
		LinkImpl l = new LinkImpl(child, parent, LinkType.CHILD, (int)(99999*Math.random()) + "");
		linkMap.put(l.getId(), l);
		
		if(linkableMap.get(child).add(l))
			linkCount++;
		
		Set<Link> parentsLinks = linkableMap.get(parent);
		if(parentsLinks.equals(null)){
			parentsLinks = new HashSet<Link>();
			linkableMap.put(parent, parentsLinks);
		}
		parentsLinks.add(l);	
		//TODO:DOUBLE CHECK
//		updateLayerDepth(child);
//		updateLayerDepth(parent);		
	}//addParent
	
    /**
     * Based on the layer depth stored in the PamNodes of this 
     * graph, create a Map where the keys are the layer depth
     * and the values are the nodes at that layer depth
     * 
     * @return the layer map 
     */
	public Map<Integer, Set<Node>> createLayerMap(){
        for(Node node: nodes){
            int layerDepth = ((PamNode)node).getLayerDepth();
            Set<Node> layerNodes = layerMap.get(layerDepth);
            
            if(layerNodes == null) {
                layerNodes = new HashSet<Node>();
                layerMap.put(layerDepth, layerNodes);
            }
            layerNodes.add(node);
        }
        return layerMap;
    }//createLayerMap
	
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
				if(source instanceof RyanPamNode && !source.equals(n))//if source is a child of n
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
				if(sink instanceof RyanPamNode && !sink.equals(n))//if source is a child of n
					return false;
			}//for
		}
		return false;
	}
	
	public NodeStructure copy() {
		return new RyanNodeStructure(this);
	}
	
	//TODO: Happens to the other nodes if we delete a Linkable that connects them?
	//TODO: What is their layer depth then?
	public void deleteLinkable(Linkable n){
		Set<Link> tempLinks = linkableMap.get(n);
		Set<Link> otherLinks;
		Linkable other;
		
		for(Link l: tempLinks){
			other = l.getSink(); 
			if(!other.equals(n)){ 
				otherLinks = linkableMap.get(other);
				if(otherLinks != null)
					otherLinks.remove(l);				
			}			
			other = l.getSource();
			if(!other.equals(n)){
				otherLinks = linkableMap.get(other);
				if(otherLinks != null)
					otherLinks.remove(l);
			}						
		}//for all of the links connected to n		
		if(linkableMap.remove(n) != null && n instanceof LinkImpl){//finally remove the linkable and its links
			linkCount--;
			linkMap.remove(n);
		}
	}//method

	public void deleteNode(Node n) {
		deleteLinkable(n);		
	}
	
	/**
	 * Remove all reference to l from the linkmap
	 * 
	 * @param l
	 */
	public void deleteLink(Link l){
		Set<Link> sourceLinks = linkableMap.get(l.getSource());
		Set<Link> sinkLinks = linkableMap.get(l.getSink());
		
		if(sourceLinks != null)
			sourceLinks.remove(l);
		if(sinkLinks != null)
			sinkLinks.remove(l);	
		
		linkableMap.remove(l);	
		linkMap.remove(l);
	}//public void deleteLink(Link l)
	
	//**GETTING
	
	/**
	 * 
	 */
	public Map<Linkable, Set<Link>> getLinkableMap(){
		return linkableMap;
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
				if(source instanceof RyanPamNode && !source.equals(n))
					children.add((RyanPamNode)source);			
			}
		}
		return children;		
	}
	
	/**
	 * Get parents of this linkable
	 * @param n
	 * @return
	 */
	public Set<Node> getParents(Node n) {		
		Set<Link> links = linkableMap.get(n);
		Set<Node> parents = new HashSet<Node>();
		
		if(links != null){
			for(Link link: links){				
				Linkable sink = link.getSink();
				if(sink instanceof RyanPamNode && !sink.equals(n))
					parents.add((RyanPamNode)sink);							
			}//for each link of n
		}
		return parents;	
	}	
	
	//GET NODE METHODS	
    public Collection<Node> getNodes(){   
		if(nodes == null)
			return null;
		else 
			return Collections.unmodifiableCollection(nodes);
    }//method
    
    public Map<Long, Node> getNodeMap(){
    	return nodeMap;
    }//method

	public int getNodeCount() {
		if(nodes.size() != nodeMap.size()){
			try{throw new Exception();}
			catch(Exception e){System.out.println("ur nodes in struct are out of sync dude");}
		}
		return nodes.size();
	}

	public Node getNode(long id) {
		return nodeMap.get(id);
	}
	
	//LINK GET METHODS
	public Map<String, Link> getLinkMap(){
		return linkMap;
	}
	
	public int getLinkCount(){
		if(linkCount != linkMap.size()){
			try{throw new Exception();}
			catch(Exception e){System.out.println("ur links in struct are out of sync dude");}
		}
		
		return linkCount;
	}//method

	public Link getLink(String id) {
		return linkMap.get(id);		
	}
	
	/**
	 * Go through the linkables in linkmap
	 * and return those that are Link
	 */
	public Collection<Link> getLinks() {
		return linkMap.values();
	}
	
	//TODO:
	/**
	 * Just a little change, can the method return 
	 * a Set<Link> and you pass to the method the LinkType 
	 * that you want? I think this is more standard and the 
	 * search is done by the class and not outside it. You can 
	 * return an unmodifiable Set in this case.
	 */
	public Set<Link> getLinksByType(LinkType type){
		Set<Link> result = new HashSet<Link>();
		Collection<Link> allLinks = getLinks();
		for(Link l: allLinks)
			if(l.getType() == type)
				result.add(l);
		return Collections.unmodifiableSet(result);
	}//
	
	/**
	 * Return all the links currently in the linkMap for supplied linkable
	 * 
	 */
	public Set<Link> getLinks(Linkable l){
		return linkableMap.get(l);	
	}//public Set<Link> getLinks(Node n)
	
	/**
	 * Go through the linkables in linkmap
	 * and return those that are Link and match
	 * type
	 */
	public Set<Link> getLinks(LinkType type) {
		Set<Link> links = new HashSet<Link>();
		Set<Linkable> linkables = linkableMap.keySet();
		for(Linkable l: linkables){
			if(l instanceof Link){
				Link temp = (Link)l;
				if(temp.getType() == type)
					links.add((Link)l);				
			}
		}		
		return links;
	}//method
	
	public Set<Link> getLinks(Linkable NorL, LinkType type){
		Set<Link> result = linkableMap.get(NorL);
		if(result != null){
			for(Link l: result){
				if(l.getType() != type)//remove links that don't match specified type
					result.remove(l);
			}//for each link
		}//result != null
		return result;
	}//method

	//OTHER GET METHOD
	public Object getContent() {	
		return this;
	}

	//**SETTING METHODS
	public void setDefaultLink(String linkClassName) {
		defaultLinkName = linkClassName;		
	}

	public void setDefaultNode(String nodeClassName) {
		defaultNodeName = nodeClassName;		
	}
	
	//**OTHER
	public void combineNodeStructure (NodeStructure ns){
		//TODO: what is the function?
	}

	//**PRINTING	
	public void printPamNodeActivations() {
		for(Node n: nodes)
			((RyanPamNode)n).printActivationString();
	}

	public void printLinkMap() {
		Set<Linkable> keys = linkableMap.keySet();
		for(Linkable key: keys){
			Set<Link> links = linkableMap.get(key);
			for(Link l: links)
				System.out.println("Source: " + l.getSource().toString() + " sink " + l.getSink().toString());
			System.out.println();
		}
	}//method


}//class