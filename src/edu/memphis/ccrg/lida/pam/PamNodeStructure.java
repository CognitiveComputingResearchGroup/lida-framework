package edu.memphis.ccrg.lida.pam;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.shared.LearnableActivatible;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * A node structure that supports activation passing between linkables.
 * @author Ryan J McCall
 *
 */
public class PamNodeStructure extends NodeStructureImpl{
	
	private static Logger logger = Logger.getLogger("lida.pam.PamNodeStructure");
	private Double upscaleFactor = 0.9;
	private Double downscaleFactor = 0.5;
	private Double selectivityThreshold = 0.8;

	/**
	 * If a node is below this threshold after being decayed it is deleted
	 */
	//TODO: think about how pam is decayed.  probably wont have this variable here
	//private double nodeRemovalThreshold = 0.01;
	
	public PamNodeStructure(){
		super("PamNodeImpl", "PamLinkImpl");
	}

	public PamNodeStructure(String defaultPamNode, String defaultLink) {
		super(defaultPamNode, defaultLink);
	}

	public void setUpscale(Double d) {
		upscaleFactor  = d;		
	}
	public double getUpscale(){
		return upscaleFactor;
	}

	/**
	 * Set downscale factor
	 * @param d
	 */
	public void setDownscale(Double d) {
		downscaleFactor  = d;		
	}
	public double getDownscale(){
		return downscaleFactor;
	}

	/**
	 * Set selectivity threshold
	 * @param s
	 */
	public void setSelectivity(Double s) {
		selectivityThreshold = s;		
	}
	public double getSelectivity() {
		return selectivityThreshold;
	}
	
	/**
	 * Add a collection of PamLinks to this pam node structure
	 */	
	public Set<PamLink> addPamLinks(Collection<PamLink> links){
		Set<PamLink> returnedLinks = new HashSet<PamLink>();
		for(PamLink l: links)
			returnedLinks.add((PamLink) addLink(l));
			
		updateActivationThresholds(upscaleFactor, selectivityThreshold);
		return returnedLinks;
	}
 	
	/**
	 * Add a collection of PamNodes to this pam node structure
	 */	
	public Set<PamNode> addPamNodes(Collection<PamNode> nodes){
		Set<PamNode> returnedNodes = new HashSet<PamNode>();
		for(Node n: nodes)
			returnedNodes.add((PamNode) addNode(n));

		updateActivationThresholds(upscaleFactor, selectivityThreshold);
		return returnedNodes;
	}//method
	/**
	 * Add a single PamNode to this pam node structure
	 * @param node
	 */
	public Node addNode(Node node){
		Node n=super.addNode(node);
		updateActivationThresholds(upscaleFactor, selectivityThreshold);
		return n;
	}
	
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
        	PamNode pamNode = (PamNode)n;
        	updateSelectionThreshold(pamNode, selectivity);
        }//for	
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
	 * Determines if linkable has no children.
	 * @param n
	 * @return true if n has no children (it is at the 'bottom' of the network)
	 */
    public boolean hasNoChildren(Linkable n) {
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
	 * Determine if linkable has no parents.
	 * @param n
	 * @return true if n has no parent (it is at the 'top' of the network)
	 */
	public boolean hasNoParents(Linkable n) {
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
	
	//************END OF METHODS RELATED TO NODE ADDING*************	

	/**
	 * Set the excite behavior for all nodes
	 */
	public void setNodesExciteStrategy(ExciteStrategy behavior) {
    	for(Node n: getNodes())
    		n.setExciteStrategy(behavior);
	}//method
	
	/**
	 * Set the decay behavior for all nodes.
	 * @param behavior
	 */
	public void setNodesDecayStrategy(DecayStrategy behavior) {
    	for(Node n: getNodes())
    		n.setDecayStrategy(behavior);
	}//method
	
	/**
	 * Get parents of this linkable. 
	 * O(l) where l = number of links connected to n.
	 * 
	 * @param n
	 * @return parents of n
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
	 * When you excite the parents of a node you might want to excite the connecting links too.
	 * Thus this method find the parents and all the links between supplied node and them
	 * @param n
	 * @return map of parents and links connecting node to them
	 */
	public Map<PamNode, PamLink> getParentsAndConnectingLinks(Node n){
		Map<PamNode, PamLink> results = new HashMap<PamNode, PamLink>();
		Set<Link> candidateLinks = getLinkableMap().get(n);
		if(candidateLinks != null){
			for(Link l: candidateLinks){
				Linkable sink = l.getSink();//Sinks are "higher than" node n, i.e. the parents of this node. 
				if(!sink.equals(n)){
					//System.out.println(n.getLabel() + " has parent " + sink.getLabel() + " via link " + l.getLabel());
					results.put((PamNode) sink, (PamLink) l);	
				}
			}//for
		}
		return results;
	}
	
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

	/**
	 * Decay the nodes of this pam node structure
	 * 
	 */
	public void decayLinkables(long ticks){
		logger.log(Level.FINE,"Decaying the Pam NodeStructure",LidaTaskManager.getActualTick());
		for(Linkable l: getLinkables()){
			LearnableActivatible la = (LearnableActivatible) l;
			la.decay(ticks);
			la.decayBaseLevelActivation(ticks);
		}
	}//method
	
	/**
	 * Simple utility method
	 */
	public void printPamNodeActivations() {
		for(Node n: getNodes())
			((PamNodeImpl)n).printActivationString();
	}//method

}//class