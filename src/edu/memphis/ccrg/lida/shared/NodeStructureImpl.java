/**
 * 
 */
package edu.memphis.ccrg.lida.shared;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.perception.PamNode;
import edu.memphis.ccrg.lida.util.Printer;
import edu.memphis.ccrg.lida.workspace.main.WorkspaceContent;
import edu.memphis.ccrg.lida.wumpusWorld.d_perception.RyanPamNode;

/**
 * @author Javier Snaider
 * 
 */
public class NodeStructureImpl implements NodeStructure, WorkspaceContent,BroadcastContent {
	private Map<Linkable, Set<Link>> linkMap;
	private Map<Long, Node> nodes;
	private Map<String, Link> links;
	private String defaultNode;
	private String defaultLink;
	private NodeFactory factory = NodeFactory.getInstance();
	private Map<Linkable, Set<Link>> linkableMap;

	/**
	 * @param defaultNode
	 *            the defaultNode to set
	 */
	public void setDefaultNode(String defaultNode) {
		this.defaultNode = defaultNode;
	}

	/**
	 * @param defaultLink
	 *            the defaultLink to set
	 */
	public void setDefaultLink(String defaultLink) {
		this.defaultLink = defaultLink;
	}

	public NodeStructureImpl() {
		linkMap = new HashMap<Linkable, Set<Link>>();
		nodes = new HashMap<Long, Node>();
		links = new HashMap<String, Link>();

	}

	public NodeStructureImpl(String defaultNode, String defaultLink) {
		this();

		this.defaultNode = defaultNode;
		this.defaultLink = defaultLink;
	}

	public NodeStructureImpl(NodeStructure struct) {
//		this.linkCount = oldGraph.linkCount;
//		
//		nodes = new HashSet<Node>();
//		nodeMap = new HashMap<Long, Node>();
//		linkMap = new HashMap<String, Link>();
//		linkableMap = new HashMap<Linkable, Set<Link>>();
//		layerMap = new HashMap<Integer, Set<Node>>();	
//		
//		Collection<Node> oldNodes = oldGraph.getNodes();
//		if(oldNodes != null){
//			for(Node n: oldNodes){
//				this.nodes.add(n);//NodeFactory.getInstance().getNode(n)
//				nodeMap.put(n.getId(), n);
//			}
//		}
//		
//		Collection<Link> oldLinks = oldGraph.getLinks();
//		if(oldLinks != null){
//			for(Link l: oldLinks){
//				LinkImpl temp = (LinkImpl)l;
//				linkMap.put(temp.getId(), temp);				
//			}
//		}
//
//		Map<Linkable, Set<Link>> oldLinkMap = oldGraph.getLinkableMap();
//		if(oldLinkMap != null){
//			Set<Linkable> oldKeys = oldLinkMap.keySet();
//			if(oldKeys != null){
//				for(Linkable l: oldKeys){
//					if(l instanceof LinkImpl){
//						LinkImpl castLink = (LinkImpl)l;
//						this.linkableMap.put(new LinkImpl(castLink), new HashSet<Link>());
//					}else if(l instanceof RyanPamNode){
//						RyanPamNode castNode = (RyanPamNode)l;
//						this.linkableMap.put(new RyanPamNode(castNode), new HashSet<Link>());
//					}
//				}
//			}
//		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#addLink(edu.memphis.ccrg.lida
	 * .shared.Link)
	 */
	public Link addLink(Link l) {

		boolean result = true;

		Linkable source = l.getSource();
		Linkable sink = l.getSink();

		Linkable newSource = null;
		Linkable newSink = null;

		if (source instanceof Node) {
			Node snode = (Node) source;
			newSource = nodes.get(snode.getId());
			if (newSource == null) {
				return null;
			}
		}

		if (sink instanceof Node) {
			Node snode = (Node) sink;
			newSink = nodes.get(snode.getId());
			if (newSink == null) {
				return null;
			}
		}

		if (source instanceof Link) {
			newSource = links.get(source.getIds());
			if (newSource == null) {
				return null;
			}
		}

		if (sink instanceof Link) {
			newSink = links.get(sink.getIds());
			if (newSink == null) {
				return null;
			}
		}

		Link newLink = getNewLink(l, newSource, newSink, l.getType());
		links.put(newLink.getIds(), newLink);

		Set<Link> tempLinks = linkMap.get(source);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkMap.put(source, tempLinks);
		}
		tempLinks.add(newLink);

		tempLinks = linkMap.get(sink);
		if (tempLinks == null) {
			tempLinks = new HashSet<Link>();
			linkMap.put(sink, tempLinks);
			tempLinks.add(newLink);
		}
		tempLinks.add(newLink);

		return newLink;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#addLinkSet(java.util.Set)
	 */
	public void addLinks(Collection<Link> links) {
		for (Link l : links) {
			addLink(l);
		}
	}// public void addLinkSet(Set<Link> links)

	public Node addNode(Node n) {
		if (!nodes.keySet().contains(n.getId())) {
			Node newNode = getNewNode(n);
			nodes.put(newNode.getId(), newNode);
			linkMap.put(newNode, null);
			return newNode;
		}
		return null;
	}
	
	/**
	 * This method is for adding nodes from perceptual memory.  It requires
	 * upscale and selectivity values from PAM to calculate the nodes' activation
	 * thresholds. 
	 */	
	public void addNodes(Set<Node> nodesToAdd, double upscale, double selectivity) {
//		for(Node n: nodesToAdd){
//			RyanPamNode toAdd = (RyanPamNode)n;
//			nodes.add(toAdd);
//			nodeMap.put(n.getId(), n);
//			//updateLayerDepth(n);//TODO:  Currently layer depth is set manually.
//		}
//		createLayerMap();
//		updateActivationThresholds(upscale, selectivity);
	}//method

	/**
	 * This method can be overwritten to customize the Node Creation.
	 * 
	 * @param n
	 *            The original Node
	 * @return The Node to be used in this NodeStructure
	 */
	protected Node getNewNode(Node n) {
		return factory.getNode(n, defaultNode);
	}

	/**
	 * This method can be overwritten to customize the Link Creation. some of
	 * the parameter could be redundant in some cases.
	 * 
	 * @param l
	 *            The original Link
	 * @param source
	 *            The new source
	 * @param sink
	 *            The new sink
	 * @param type
	 *            the type of the link
	 * @return The link to be used in this NodeStructure
	 */
	protected Link getNewLink(Link l, Linkable source, Linkable sink,
			LinkType type) {
		return factory.getLink(defaultLink, source, sink, l.getType());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#addNodes(java.util.Set)
	 */
	public void addNodes(Collection<Node> nodesToAdd) {
		for (Node n : nodesToAdd) {
			addNode(n);
			// refresh();
		}
	}

	public NodeStructure copy() {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#deleteLink(edu.memphis.ccrg
	 * .lida.shared.Link)
	 */
	public void deleteLink(Link l) {
		deleteLinkable(l);

	}// public void deleteLink(Link l)

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#deleteLinkable(edu.memphis
	 * .ccrg.lida.shared.Linkable)
	 */
	public void deleteLinkable(Linkable n) {
		Set<Link> tempLinks = linkMap.get(n);
		Set<Link> otherLinks;
		Linkable other;

		if (tempLinks != null) {
			for (Link l : tempLinks) {
				other = l.getSink();
				if (!other.equals(n)) {
					otherLinks = linkMap.get(other);
					if (otherLinks != null)
						otherLinks.remove(l);
				}
				other = l.getSource();
				if (!other.equals(n)) {
					otherLinks = linkMap.get(other);
					if (otherLinks != null)
						otherLinks.remove(l);
				}
			}// for all of the links connected to n
		}

		linkMap.remove(n);// finally remove the linkable and its links
		if (n instanceof Node) {
			nodes.remove(((Node) n).getId());
		} else if (n instanceof Link) {
			Link aux = links.get(n.getIds());
			links.remove(aux.getIds());
			Set<Link> sourceLinks = linkMap.get(aux.getSource());
			Set<Link> sinkLinks = linkMap.get(aux.getSink());

			if (sourceLinks != null)
				sourceLinks.remove(aux);

			if (sinkLinks != null)
				sinkLinks.remove(aux);
		}

	}// public void deleteNode(Linkable n)

	public void deleteNode(Node n) {
		deleteLinkable(n);

	}
	
	/**
	 * Get children of this linkable
	 * @param n
	 * @return
	 */
	public Set<PamNode> getChildren(Linkable n) {
//		Set<Link> links = linkableMap.get(n);
//		Set<PamNode> children = new HashSet<PamNode>();
//		if(links != null){
//			for(Link link: links){
//				Linkable source = link.getSource();
//				if(source instanceof RyanPamNode && !source.equals(n))
//					children.add((RyanPamNode)source);			
//			}
//		}
//		return children;
		return null;
		//TODO
	}

	public Collection<Link> getLinks() {
		Collection<Link> aux = links.values();
		if (aux == null) {
			return null;
		} else {
			return Collections.unmodifiableCollection(aux);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#getLinks(edu.memphis.ccrg.
	 * lida.shared.Linkable)
	 */
	public Set<Link> getLinks(Linkable l) {
		Set<Link> aux = linkMap.get(l);
		if (aux == null) {
			return null;
		} else {
			return Collections.unmodifiableSet(aux); // This returns the
		}// set of Links but
		// it prevents to be
		// modified

	}// public Set<Link> getLinks(PamNodeImplW n)

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.memphis.ccrg.lida.shared.NodeStructure#getLinks(edu.memphis.ccrg.
	 * lida.shared.Linkable, edu.memphis.ccrg.lida.shared.LinkType)
	 */
	public Set<Link> getLinks(Linkable NorL, LinkType type) {
		Set<Link> temp = linkMap.get(NorL);
		Set<Link> result = new HashSet<Link>();
		if (temp != null) {
			for (Link l : temp) {
				if (l.getType() == type)// add only decired type
					result.add(l);
			}// for each link
		}// result != null
		return result;
	}// public Set<Link> getLinks(PamNodeImplW n, LinkType type)

	public Set<Link> getLinks(LinkType type) {
		Set<Link> result = new HashSet<Link>();
		if (links != null) {
			for (Link l : links.values()) {
				if (l.getType() == type) {
					result.add((Link) l);
				}
			}
		}
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.memphis.ccrg.lida.shared.NodeStructure#getNodes()
	 */
	public Collection<Node> getNodes() {
		Collection<Node> aux = nodes.values();
		if (aux == null) {
			return null;
		} else {
			return Collections.unmodifiableCollection(aux);
		}
	}

	public Object getContent() {
		return this;
	}

	/**
	 * 
	 */
	public Map<Linkable, Set<Link>> getLinkableMap(){
		return linkableMap;
	}
	
	public void combineNodeStructure(NodeStructure ns) {
		// TODO Auto-generated method stub

	}

	public Link getLink(String ids) {
		return links.get(ids);
	}

	public Node getNode(long id) {
		return nodes.get(id);
	}

	public int getLinkCount() {
		return links.size();
	}
	
	public Map<Linkable, Set<Link>> getLinkMap(){
		return linkMap;
	}

	public Map<Integer, Set<Node>> createLayerMap() {
//		for(Node node: nodes){
//            int layerDepth = ((PamNode)node).getLayerDepth();
//            Set<Node> layerNodes = layerMap.get(layerDepth);
//            
//            if(layerNodes == null) {
//                layerNodes = new HashSet<Node>();
//                layerMap.put(layerDepth, layerNodes);
//            }
//            layerNodes.add(node);
//        }
//        return layerMap;
		//TODO
		return null;
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
//        for(Integer layerDepth: layerMap.keySet()){
//           for(Linkable n: layerMap.get(layerDepth)){
//        	   PamNode temp = null;
//        	   if(n instanceof PamNode){
//        		   temp = (PamNode)n;
//        		   updateMinActivation(temp, upscale);
//        		   updateMaxActivation(temp, upscale);
//        		   updateSelectionThreshold(temp, selectivity);      
//        	   }  
//           }
//        }	
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

	public Set<Node> getParents(Node n) {
		Printer.p("getParents not impl!");
		return null;
	}


	//**PRINTING	
	public void printPamNodeActivations() {
//		for(Node n: nodes)
//			((RyanPamNode)n).printActivationString();
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

}
