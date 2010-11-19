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
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

public class ProceduralMemoryImpl extends LidaModuleImpl implements
		ProceduralMemory, BroadcastListener {

	private static final int DEFAULT_TICKS_PER_RUN = 10;

	private static Logger logger = Logger
			.getLogger("lida.proceduralmemory.ProceduralMemoryImpl");

	/**
	 * Shared variable to store the asynchronously arriving broadcast
	 */
	private NodeStructure currentBroadcast = new NodeStructureImpl();

	/**
	 * Schemes indexed by Linkables in their context. Operations on
	 * ConcurrentHashmap do not block but they may not reflect the true state of
	 * the Map if multiple operations are concurrent.
	 * 
	 * TODO: allow STREAMS in addition to SCHEME
	 */
	private Map<Object, Set<Scheme>> schemeMap = new ConcurrentHashMap<Object, Set<Scheme>>();

	/**
	 * Convenient for decaying the schemes
	 */
	private Set<Scheme> schemeSet = new HashSet<Scheme>();

	/**
	 * Determines how scheme are given activation and whether they should be
	 * instantiated
	 */
	private SchemeActivationBehavior schemeActivationBehavior = new BasicSchemeActivationBehavior(
			this);

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
		for (Scheme scheme : schemes)
			addScheme(scheme);
	}

	/**
	 * Adds scheme
	 */
	public void addScheme(Scheme scheme) {
		schemeSet.add(scheme);
		NodeStructure context = scheme.getContext();
		for (Linkable nodeOrLink : context.getLinkables()) {
			Set<Scheme> existingSchemes = schemeMap.get(nodeOrLink);
			if (existingSchemes == null) {
				existingSchemes = new HashSet<Scheme>();
				schemeMap.put(nodeOrLink, existingSchemes);
			}
			existingSchemes.add(scheme);
		}// for
	}

	@Override
	public void decayModule(long ticks) {
		for (Scheme s : schemeSet)
			s.decay(ticks);
	}

	/**
	 * 
	 */
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		logger.log(Level.FINEST, "Procedural memory receives broadcast", LidaTaskManager.getActualTick());
		synchronized (this) {
			currentBroadcast = ((NodeStructure) bc).copy();
		}

		LidaTask broadcastTask = new LidaTaskImpl() {
			protected void runThisLidaTask() {
				activateSchemes();
				setTaskStatus(LidaTaskStatus.FINISHED);
			}
		};
		taskSpawner.addTask(broadcastTask);
	}

	@Override
	public void learn() {
		Collection<Node> nodes = currentBroadcast.getNodes();
		for (Node n : nodes) {
			// TODO
			n.getId();
		}
	}

	/**
	 * 
	 */
	@Override
	public void activateSchemes() {
		logger.log(Level.FINEST, "Procedural memory activates schemes", LidaTaskManager.getActualTick());
		schemeActivationBehavior.activateSchemesWithBroadcast(currentBroadcast, schemeMap);
	}

	/**
	 * Impl. of observer pattern. Send s to all registered ProceduralMemory
	 * Listeners
	 */
	@Override
	public void sendInstantiatedScheme(Scheme s) {
		logger.log(Level.FINE, "Sending scheme from procedural memory",
				LidaTaskManager.getActualTick());
		for (ProceduralMemoryListener listener : listeners) {
			listener.receiveBehavior(s.getBehavior());
		}
	}

	@Override
	public Object getModuleContent(Object... params) {
		return null;
	}

	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ProceduralMemoryListener) {
			addProceduralMemoryListener((ProceduralMemoryListener) listener);
		}
	}

	public Object getState() {
		Object[] state = new Object[2];
		state[0] = this.schemeMap;
		state[1] = this.schemeSet;
		return state;
	}

	@SuppressWarnings("unchecked")
	public boolean setState(Object content) {
		if (content instanceof Object[]) {
			Object[] state = (Object[]) content;
			if (state.length == 2 && state[0] instanceof Map
					&& state[1] instanceof Set) {
				try {
					this.schemeMap = (Map<Object, Set<Scheme>>) state[0];
					this.schemeSet = (Set<Scheme>) state[1];
					return true;
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		}
		return false;
	}

	@Override
	public void init() {
		int ticksPerRun = (Integer) getParam("ticksPerRun", DEFAULT_TICKS_PER_RUN);
		System.out.println("pm: tpr " + ticksPerRun);
		super.taskSpawner.addTask(new BackgroundTask(ticksPerRun));
	}

	private class BackgroundTask extends LidaTaskImpl {

		public BackgroundTask(int ticksForCycle) {
			setNumberOfTicksPerRun(ticksForCycle);
		}

		@Override
		protected void runThisLidaTask() {
			activateSchemes();
		}
	}
	
}// class