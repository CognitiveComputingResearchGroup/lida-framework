package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

public class ProceduralMemoryImpl implements ProceduralMemory, BroadcastListener{

	/**
	 * Shared variable to store the asynchronously arriving broadcast
	 */
	private NodeStructure broadcastContent = new NodeStructureImpl();
	
	/**
	 * Schemes indexed by Linkables in their context.
	 * TODO: check concurrent hash map
	 */
	private Map<Linkable, Set<Scheme>> schemeMap = new ConcurrentHashMap<Linkable, Set<Scheme>>();

	/**
	 * Listeners of this Procedural Memory
	 */
	private List<ProceduralMemoryListener> listeners = new ArrayList<ProceduralMemoryListener>();

	private double schemeSelectionThreshold = 0.6;

	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		listeners.add(listener);
	}

	public void addSchemes(Collection<Scheme> schemes) {
		for(Scheme s: schemes){
			NodeStructure context = s.getContext();
			for(Node n: context.getNodes()){
				Set<Scheme> existingSchemes = schemeMap.get(n);
				if(existingSchemes == null){
					//TODO: find a concurrent data structure instead of HashSet
					existingSchemes = new HashSet<Scheme>();
					schemeMap.put(n, existingSchemes);
				}
				existingSchemes.add(s);
			}
			for(Link l: context.getLinks()){
				Set<Scheme> existingSchemes = schemeMap.get(l);
				if(existingSchemes == null){
					existingSchemes = new HashSet<Scheme>();
					schemeMap.put(l, existingSchemes);
				}
				existingSchemes.add(s);
			}//for links
		}//for schemes
	}
	
	/**
	 * TODO: Consider other ways of storing the incoming broadcast.
	 */
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}
	public void learn(){
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {n.getId();}
	}

	/**
	 * 
	 */
	public void activateSchemesWithBroadcast() {
		Collection<Node> nodes = null;
		Collection<Link> links = null;
		synchronized(this){
			nodes = broadcastContent.getNodes();
			links = broadcastContent.getLinks();
		}
		for(Node n: nodes)
			auxActivateSchemes(n);
		for(Link l: links)
			auxActivateSchemes(l);
	}//method
	public void auxActivateSchemes(Linkable l){
		if(schemeMap.containsKey(l)){
			Set<Scheme> schemes = schemeMap.get(l);
			for(Scheme s: schemes){
				int contextCount = s.getContext().getNodeCount();
				s.excite(1.0 / (contextCount * 1.0));
				if(s.getActivation() > schemeSelectionThreshold){
					//Copy?
					sendInstantiatedScheme(s);
				}	
			}//for
		}
	}//method

	/**
	 * Impl. of observer pattern.  Send s to all registered ProceduralMemory Listeners 
	 */
	public void sendInstantiatedScheme(Scheme s) {
		for(ProceduralMemoryListener listener: listeners)
			listener.receiveScheme(s);
	}

}// class