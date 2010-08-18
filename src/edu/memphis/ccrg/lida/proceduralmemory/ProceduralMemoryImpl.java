package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorImpl;
import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

public class ProceduralMemoryImpl extends LidaModuleImpl implements ProceduralMemory, BroadcastListener {
	private static Logger logger = Logger.getLogger("lida.proceduralmemory.ProceduralMemoryImpl");

	/**
	 * Shared variable to store the asynchronously arriving broadcast
	 */
	private NodeStructure broadcastContent = new NodeStructureImpl();

	/**
	 * Schemes indexed by Linkables in their context. Operations on
	 * ConcurrentHashmap do not block but they may not reflect the true state of
	 * the Map if multiple operations are concurrent.
	 */
	private Map<Linkable, Set<Scheme>> schemeMap = new ConcurrentHashMap<Linkable, Set<Scheme>>();
	
	//TODO: equals, hashcode
	private Set<Scheme> schemeSet = new HashSet<Scheme>();
	

	/**
	 * Determines how scheme are given activation and whether they should be
	 * instantiated
	 */
	SchemeActivationBehavior schemeActivationBehavior = new BasicSchemeActivationBehavior(
			this, 0.6);

	/**
	 * Listeners of this Procedural Memory
	 */
	private List<ProceduralMemoryListener> listeners = new ArrayList<ProceduralMemoryListener>();

	public ProceduralMemoryImpl() {
		super(ModuleName.ProceduralMemory);
	}

	@Override
	public void addProceduralMemoryListener(ProceduralMemoryListener listener) {
		listeners.add(listener);
	}

	@Override
	public void setSchemeActivationBehavior(SchemeActivationBehavior b) {
		schemeActivationBehavior = b;
	}

	@Override
	public void addSchemes(Collection<Scheme> schemes) {
	//	System.out.println("Add schemes called " + schemes.size());
		for (Scheme s : schemes) {
			schemeSet.add(s);
			List<NodeStructure> contextConditions = s.getContextConditions();
			//System.out.println("num contexts " + contextConditions.size());
			for (NodeStructure ns : contextConditions) {
				
				for (Linkable ln : ns.getLinkableMap().keySet()) {
					Set<Scheme> existingSchemes = schemeMap.get(ln);
					//System.out.println("node in context " + ln.getLabel() + " exisiting? " + existingSchemes);
					if (existingSchemes == null) {
						existingSchemes = new HashSet<Scheme>();
						schemeMap.put(ln, existingSchemes);
					}
					existingSchemes.add(s);
				}
			}// for schemes
		}
	}
	
	public void addScheme(Scheme s){
		schemeSet.add(s);
		List<NodeStructure> contextConditions = s.getContextConditions();
		//System.out.println("num contexts " + contextConditions.size());
		for (NodeStructure ns : contextConditions) {
			
			for (Linkable ln : ns.getLinkableMap().keySet()) {
				Set<Scheme> existingSchemes = schemeMap.get(ln);
				//System.out.println("node in context " + ln.getLabel() + " exisiting? " + existingSchemes);
				if (existingSchemes == null) {
					existingSchemes = new HashSet<Scheme>();
					schemeMap.put(ln, existingSchemes);
				}
				existingSchemes.add(s);
			}
		}// for schemes
	}
	
	@Override
	public void decayModule(long ticks) {
		for(Scheme s: schemeSet)
			s.decay(ticks);
	}

	/**
	 * TODO: Consider other ways of storing the incoming broadcast.
	 */
	@Override
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		broadcastContent = (NodeStructure) bc;
	}

	@Override
	public void learn() {
		Collection<Node> nodes = broadcastContent.getNodes();
		for (Node n : nodes) {
			n.getId();
		}
	}

	/**
	 * 
	 */
	@Override
	public void activateSchemes() {
		NodeStructure ns = null;
		synchronized (this) {
			ns = broadcastContent.copy();
		}		
		schemeActivationBehavior.activateSchemesWithBroadcast(ns, schemeMap);
	}// method

	/**
	 * Impl. of observer pattern. Send s to all registered ProceduralMemory
	 * Listeners
	 */
	@Override
	public void sendInstantiatedScheme(Scheme s) {
		logger.log(Level.FINE, "Sending scheme from procedural memory", LidaTaskManager.getActualTick());
		for (ProceduralMemoryListener listener : listeners){
			Behavior b = new BehaviorImpl(s.getId(), s.getSchemeActionId());
			listener.receiveBehavior(b);
		}
	}

	@Override
	public Object getModuleContent() {
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ProceduralMemoryListener) {
			addProceduralMemoryListener((ProceduralMemoryListener) listener);
		}
	}

}// class