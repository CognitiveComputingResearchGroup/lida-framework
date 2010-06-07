package edu.memphis.ccrg.lida.proceduralmemory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

public class ProceduralMemoryImpl extends LidaModuleImpl implements
		ProceduralMemory, BroadcastListener {
	private static Logger logger = Logger.getLogger("lida.proceduralmemory");

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

	/**
	 * Determines how scheme are given activation and whether they should be
	 * instantiated
	 */
	SchemeActivationBehavior schemeActivationBehavior = new BasicSchemeActivationBehavior(
			this);

	/**
	 * Listeners of this Procedural Memory
	 */
	private List<ProceduralMemoryListener> listeners = new ArrayList<ProceduralMemoryListener>();

	public ProceduralMemoryImpl() {
		super(ModuleName.ProceduralMemory);
		// TODO Auto-generated constructor stub
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
		for (Scheme s : schemes) {
			List<NodeStructure> context = s.getContextConditions();
			for (NodeStructure ns : context) {
				for (Linkable ln : ns.getLinkableMap().keySet()) {
					Set<Scheme> existingSchemes = schemeMap.get(ln);
					if (existingSchemes == null) {
						existingSchemes = new HashSet<Scheme>();
						schemeMap.put(ln, existingSchemes);
					}
					existingSchemes.add(s);
				}
			}// for schemes
		}
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
		System.out.println("in pm the broadcast has " + ns.getNodeCount());
		
		for(Node n: ns.getNodes()){
			System.out.println("in pm " +n.getLabel());
		}
		
		schemeActivationBehavior.activateSchemesWithBroadcast(ns, schemeMap);
	}// method

	/**
	 * Impl. of observer pattern. Send s to all registered ProceduralMemory
	 * Listeners
	 */
	@Override
	public void sendInstantiatedScheme(Scheme s) {
		logger.log(Level.FINE, "Scheme is sent", LidaTaskManager
				.getActualTick());
		for (ProceduralMemoryListener listener : listeners)
			listener.receiveScheme(s);
	}

	@Override
	public Object getModuleContent() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ProceduralMemoryListener) {
			addProceduralMemoryListener((ProceduralMemoryListener) listener);
		}
	}

}// class