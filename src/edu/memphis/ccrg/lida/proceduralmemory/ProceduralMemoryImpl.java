package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

public class ProceduralMemoryImpl extends LidaModuleImpl implements ProceduralMemory, BroadcastListener{

	/**
	 * Shared variable to store the asynchronously arriving broadcast
	 */
	private NodeStructure broadcastContent = new NodeStructureImpl();
	
	/**
	 * Schemes indexed by Linkables in their context.
	 * Operations on ConcurrentHashmap do not block but they may not reflect 
	 * the true state of the Map if multiple operations are concurrent. 
	 */
	private Map<Linkable, Queue<Scheme>> schemeMap = new ConcurrentHashMap<Linkable, Queue<Scheme>>();
	
	/**
	 * Determines how scheme are given activation and whether they should be instantiated
	 */
	SchemeActivationBehavior schemeActivationBehavior = new BasicSchemeActivationBehavior(this);

	/**
	 * Listeners of this Procedural Memory
	 */
	private List<ProceduralMemoryListener> listeners = new ArrayList<ProceduralMemoryListener>();

	public ProceduralMemoryImpl() {
		super(ModuleName.ProceduralMemory);
		// TODO Auto-generated constructor stub
	}
	
	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		listeners.add(listener);
	}
	
	public void setSchemeActivationBehavior(SchemeActivationBehavior b){
		schemeActivationBehavior = b;
	}

	public void addSchemes(Collection<Scheme> schemes) {
		for(Scheme s: schemes){
			NodeStructure context = s.getContext();
			for(Node n: context.getNodes()){
				Queue<Scheme> existingSchemes = schemeMap.get(n);
				if(existingSchemes == null){
					existingSchemes = new ConcurrentLinkedQueue<Scheme>();
					schemeMap.put(n, existingSchemes);
				}
				existingSchemes.add(s);
			}
			for(Link l: context.getLinks()){
				Queue<Scheme> existingSchemes = schemeMap.get(l);
				if(existingSchemes == null){
					existingSchemes = new ConcurrentLinkedQueue<Scheme>();
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
	public void activateSchemes() {
		NodeStructure ns = null;
		synchronized(this){
			ns = broadcastContent.copy();
		}
		schemeActivationBehavior.activateSchemesWithBroadcast(ns, schemeMap);
	}//method

	/**
	 * Impl. of observer pattern.  Send s to all registered ProceduralMemory Listeners 
	 */
	public void sendInstantiatedScheme(Scheme s) {
		for(ProceduralMemoryListener listener: listeners)
			listener.receiveScheme(s);
	}

	public Object getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}
	public void addListener(ModuleListener listener) {
		if (listener instanceof ProceduralMemoryListener){
			addProceduralMemoryListener((ProceduralMemoryListener)listener);
		}
	}

}// class