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
		// Using the current broadcast content, activate schemes in the scheme map
		//TODO: Instantiate schemes
		Scheme s = new SchemeImpl(101, new NodeStructureImpl(), null, new NodeStructureImpl());
		sendInstantiatedScheme(s);
	}

	public void sendInstantiatedScheme(Scheme s) {
		for (ProceduralMemoryListener listener : listeners)
			listener.receiveScheme(s);
	}


}// class
