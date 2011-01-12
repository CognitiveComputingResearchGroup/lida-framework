/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
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

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.ConcurrentHashSet;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

public class ProceduralMemoryImpl extends LidaModuleImpl implements ProceduralMemory, BroadcastListener {

	private static final Logger logger = Logger.getLogger(ProceduralMemoryImpl.class.getCanonicalName());
	
	/**
	 * Default ticks per run of tasks spawned from this Module
	 */
	private static final int DEFAULT_TICKS_PER_RUN = 1;

	/**
	 * Shared variable to store the asynchronously arriving broadcast
	 */
	private NodeStructure currentBroadcast;

	/**
	 * Schemes indexed by Linkables in their context. Operations on
	 * ConcurrentHashmap do not block but they may not reflect the true state of
	 * the Map if multiple operations are concurrent.
	 * 
	 * TODO: allow STREAMS in addition to SCHEME
	 */
	private Map<Object, Set<Scheme>> contextSchemeMap;
	
	private Map<Object, Set<Scheme>> resultSchemeMap;

	/**
	 * Convenient for decaying the schemes
	 */
	private Set<Scheme> schemeSet;

	/**
	 * Determines how scheme are given activation and whether they should be
	 * instantiated
	 */
	private SchemeActivationBehavior schemeActivationBehavior = new BasicSchemeActivationBehavior(
			this);

	/**
	 * Listeners of this Procedural Memory
	 */
	private List<ProceduralMemoryListener> proceduralMemoryListeners = new ArrayList<ProceduralMemoryListener>();

	public ProceduralMemoryImpl() {
		super(ModuleName.ProceduralMemory);
		currentBroadcast = new NodeStructureImpl();
		contextSchemeMap = new ConcurrentHashMap<Object, Set<Scheme>>();
		resultSchemeMap = new ConcurrentHashMap<Object, Set<Scheme>>();
		schemeSet = new HashSet<Scheme>();
	}
	
	@Override
	public void init() {
		int ticksPerRun = (Integer) getParam("ticksPerRun", DEFAULT_TICKS_PER_RUN);
		taskSpawner.addTask(new BackgroundTask(ticksPerRun));
	}
	private class BackgroundTask extends LidaTaskImpl {
		public BackgroundTask(int ticksForCycle) {
			setNumberOfTicksPerRun(ticksForCycle);
		}
		@Override
		protected void runThisLidaTask() {
			activateSchemes();
		}
		public String toString(){
			return ProceduralMemoryImpl.class.getSimpleName() + " background task";
		}
	}
	@Override
	public void activateSchemes() {
		logger.log(Level.FINEST, "Procedural memory activates schemes", LidaTaskManager.getActualTick());
		schemeActivationBehavior.activateSchemesWithBroadcast(currentBroadcast, contextSchemeMap);
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ProceduralMemoryListener) {
			proceduralMemoryListeners.add((ProceduralMemoryListener) listener);
		}else{
			logger.log(Level.WARNING, "Try to add wrong listener type", 
					LidaTaskManager.getActualTick());
		}
	}

	@Override
	public void setSchemeActivationBehavior(SchemeActivationBehavior b) {
		schemeActivationBehavior = b;
	}

	@Override
	public void addSchemes(Collection<Scheme> schemes) {
		for (Scheme scheme : schemes)
			addScheme(scheme);
	}

	/**
	 * Adds scheme
	 */
	@Override
	public void addScheme(Scheme scheme) {
		schemeSet.add(scheme);
//		for (Linkable nodeOrLink : scheme.getContext().getLinkables()) {
//			Set<Scheme> existingSchemes = contextSchemeMap.get(nodeOrLink);
//			if (existingSchemes == null) {
//				existingSchemes = new HashSet<Scheme>();
//				contextSchemeMap.put(nodeOrLink, existingSchemes);
//			}
//			existingSchemes.add(scheme);
//		}
		indexSchemeByElements(scheme, scheme.getContext().getLinkables(), contextSchemeMap);
		indexSchemeByElements(scheme, scheme.getAddingResult().getLinkables(), resultSchemeMap);
		indexSchemeByElements(scheme, scheme.getDeletingResult().getLinkables(), resultSchemeMap);
	}
	
	private void indexSchemeByElements(Scheme scheme, Collection<Linkable> elements, 
									   Map<Object, Set<Scheme>> map) {
		for (Linkable element : elements) {
			synchronized (element) {
				Set<Scheme> values = map.get(element);
				if (values == null) {
					values = new ConcurrentHashSet<Scheme>();
					map.put(element, values);
				}
				values.add(scheme);
			}
		}
	}
	
	private class ProcessBroadcastTask extends LidaTaskImpl{		
		private NodeStructure broadcast;
		public ProcessBroadcastTask(NodeStructure broadcast) {
			super();
			this.broadcast = broadcast;
		}
		@Override
		protected void runThisLidaTask() {
			activateSchemes();
			//new task for this
			learn((BroadcastContent) broadcast);
			setTaskStatus(LidaTaskStatus.FINISHED);
		}	
	}

	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		logger.log(Level.FINEST, "Procedural memory receives broadcast", LidaTaskManager.getActualTick());
		synchronized (this) {
			ProcessBroadcastTask task = new ProcessBroadcastTask(((NodeStructure) bc).copy());		
			taskSpawner.addTask(task);
		}
	}

	@Override
	public void learn(BroadcastContent content) {
		Collection<Node> nodes = ((NodeStructure) content).getNodes();
		for (Node n : nodes) {
			// TODO
			n.getId();
		}
	}

	@Override
	public void decayModule(long ticks) {
		for (Scheme s : schemeSet)
			s.decay(ticks);
	}

	/**
	 * Impl. of observer pattern. Send s to all registered ProceduralMemory
	 * Listeners
	 */
	@Override
	public void sendInstantiatedScheme(Scheme s) {
		logger.log(Level.FINE, "Sending scheme from procedural memory",
				LidaTaskManager.getActualTick());
		for (ProceduralMemoryListener listener : proceduralMemoryListeners) {
			listener.receiveBehavior(s.getBehavior());
		}
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public Object getState() {
		Object[] state = new Object[2];
		state[0] = this.contextSchemeMap;
		state[1] = this.schemeSet;
		return state;
	}

	@Override
	@SuppressWarnings("unchecked")
	public boolean setState(Object content) {
		if (content instanceof Object[]) {
			Object[] state = (Object[]) content;
			if (state.length == 2 && state[0] instanceof Map
					&& state[1] instanceof Set) {
				try {
					this.contextSchemeMap = (Map<Object, Set<Scheme>>) state[0];
					this.schemeSet = (Set<Scheme>) state[1];
					return true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
	}
	
}// class