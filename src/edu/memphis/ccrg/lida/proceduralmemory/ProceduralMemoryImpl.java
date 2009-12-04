package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
	 */
	private Map<Linkable, Set<Scheme>> schemeMap = new HashMap<Linkable, Set<Scheme>>();

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
				if(schemeMap.containsKey(n)){
					Set<Scheme> existingSchemes = schemeMap.get(n);
					existingSchemes.add(s);
					schemeMap.put(n, existingSchemes);
				}else{
					Set<Scheme> indexedSchemes = new HashSet<Scheme>();
					indexedSchemes.add(s);
					schemeMap.put(n, indexedSchemes);
				}
			}
			for(Link l: context.getLinks()){
				if(schemeMap.containsKey(l)){
					Set<Scheme> existingSchemes = schemeMap.get(l);
					existingSchemes.add(s);
					schemeMap.put(l, existingSchemes);
				}else{
					Set<Scheme> indexedSchemes = new HashSet<Scheme>();
					indexedSchemes.add(s);
					schemeMap.put(l, indexedSchemes);
				}
			}
		}
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
		//TODO: synchronize this?
		Collection<Node> nodes = broadcastContent.getNodes();
		Collection<Link> links = broadcastContent.getLinks();
		//
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
				if(s.getActivation() > schemeSelectionThreshold)
					sendInstantiatedScheme(s);
			}
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
