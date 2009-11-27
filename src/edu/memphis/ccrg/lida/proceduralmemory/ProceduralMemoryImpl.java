package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	
	private Map<NodeStructure, Scheme> schemeMap = new HashMap<NodeStructure, Scheme>();

	/**
	 * Listeners of this Procedural Memory
	 */
	private List<ProceduralMemoryListener> listeners = new ArrayList<ProceduralMemoryListener>();

	private double schemeSelectionThreshold = 0.6;

	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		listeners.add(listener);
	}

	public void addSchemes(Collection<Scheme> schemes) {
		for(Scheme s: schemes)
			schemeMap.put(s.getContext(), s);		
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
	 * Updates the list of schemes to send with a 
	 */
	public void activateSchemesWithBroadcast() {
		//Currently just get the nodes from the broadcast.
		Collection<Node> nodes = broadcastContent.getNodes();
		//Iterate over the nodes
		Scheme s = null;
		for(NodeStructure ns: schemeMap.keySet()){
			//For each node check if a key contains it.
			for(Node n: nodes){
				//If the NodeStructure contains it 
				if(ns.containsNode(n)){
					//The excite the scheme and see if that puts it over threshold
					if(s == null)
						s = schemeMap.get(ns);
					
					s.excite(1.0 / (ns.getNodeCount() * 1.0));
					//If over threshold the send
					if(s.getActivation() > schemeSelectionThreshold )
						sendInstantiatedScheme(s);
				}
			}
			s = null;
		}
	}//method

	public void sendInstantiatedScheme(Scheme s) {
		for(ProceduralMemoryListener listener: listeners)
			listener.receiveScheme(s);
	}


}// class
