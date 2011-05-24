/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
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

import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.ConcurrentHashSet;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

/**
 * Default implementation of {@link ProceduralMemory}. Indexes scheme by context
 * elements for quick access.
 *
 */
public class ProceduralMemoryImpl extends FrameworkModuleImpl implements ProceduralMemory, BroadcastListener {

	private static final Logger logger = Logger.getLogger(ProceduralMemoryImpl.class.getCanonicalName());

	/**
	 * Schemes indexed by Linkables in their context. Operations on
	 * ConcurrentHashmap do not block but they may not reflect the true state of
	 * the Map if multiple operations are concurrent.
	 * 
	 */
	private Map<Object, Set<Scheme>> contextSchemeMap;
	
//	TODO support for Node desirability
//	private Map<Object, Set<Scheme>> resultSchemeMap;

	/**
	 * Convenient for decaying the schemes
	 */
	private Set<Scheme> schemeSet;

	/**
	 * Determines how scheme are given activation and whether they should be
	 * instantiated
	 */
	private SchemeActivationStrategy schemeActivationStrategy;

	/**
	 * Listeners of this Procedural Memory
	 */
	private List<ProceduralMemoryListener> proceduralMemoryListeners;

	private DecayStrategy behaviorDecayStrategy;

	private ExciteStrategy behaviorExciteStrategy;
	
	private static final ElementFactory factory = ElementFactory.getInstance();

	public ProceduralMemoryImpl() {
		contextSchemeMap = new ConcurrentHashMap<Object, Set<Scheme>>();
//		resultSchemeMap = new ConcurrentHashMap<Object, Set<Scheme>>();
		schemeSet = new HashSet<Scheme>();
		schemeActivationStrategy = new BasicSchemeActivationStrategy(this);
		proceduralMemoryListeners = new ArrayList<ProceduralMemoryListener>();
	}

	/**
	 * This module can accept parameters for the decay and excite strategies for
	 * behaviors instantiated in this module.  The parameters names are:<br>
	 * 
	 * <b>proceduralMemory.behaviorDecayStrategy</b> - name (a String) of the {@link Strategy} in the {@link ElementFactory} <br>
	 * <b>proceduralMemory.behaviorExciteStrategy</b> - name (a String) of the {@link Strategy} in the {@link ElementFactory} 
	 * 
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModuleImpl#init()
	 */
	@Override
	public void init() {		
		String decayName = (String) getParam("proceduralMemory.behaviorDecayStrategy", factory.getDefaultDecayType());
		behaviorDecayStrategy = factory.getDecayStrategy(decayName);
		
		String exciteName = (String) getParam("proceduralMemory.behaviorExciteStrategy", factory.getDefaultExciteType());
		behaviorExciteStrategy = factory.getExciteStrategy(exciteName);
	}
	
	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ProceduralMemoryListener) {
			proceduralMemoryListeners.add((ProceduralMemoryListener) listener);
		}else{
			logger.log(Level.WARNING, "Try to add wrong listener type", 
					TaskManager.getCurrentTick());
		}
	}

	@Override
	public void setSchemeActivationStrategy(SchemeActivationStrategy strategy) {
		schemeActivationStrategy = strategy;
	}
	@Override
	public SchemeActivationStrategy getSchemeActivationStrategy() {
		return schemeActivationStrategy;
	}

	@Override
	public void addSchemes(Collection<Scheme> schemes) {
		for (Scheme scheme : schemes){
			addScheme(scheme);
		}
	}

	@Override
	public void addScheme(Scheme scheme) {
		schemeSet.add(scheme);
		indexSchemeByElements(scheme, scheme.getContext().getLinkables(), contextSchemeMap);
//		indexSchemeByElements(scheme, scheme.getAddingResult().getLinkables(), resultSchemeMap);
//		indexSchemeByElements(scheme, scheme.getDeletingResult().getLinkables(), resultSchemeMap);
	}
	
	/*
	 * For every element in elements, adds an entry to map where the key is an element
	 * and the value is scheme.
	 * @param scheme
	 * @param elements
	 * @param map
	 */
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
	
	@Override
	public void activateSchemes(NodeStructure broadcast) {
		logger.log(Level.FINEST, "Procedural memory activates schemes", TaskManager.getCurrentTick());
		schemeActivationStrategy.activateSchemesWithBroadcast(broadcast, contextSchemeMap);
	}
	
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		logger.log(Level.FINEST, "Procedural memory receives broadcast", TaskManager.getCurrentTick());
		synchronized (this) {
			ProcessBroadcastTask task = new ProcessBroadcastTask(((NodeStructure) bc).copy());		
			taskSpawner.addTask(task);
		}
	}
	
	//inner class
	private class ProcessBroadcastTask extends FrameworkTaskImpl{		
		private NodeStructure broadcast;
		public ProcessBroadcastTask(NodeStructure broadcast) {
			super();
			this.broadcast = broadcast;
		}
		@Override
		protected void runThisFrameworkTask() {
			activateSchemes(broadcast);			
			setTaskStatus(TaskStatus.FINISHED);
		}	
		@Override
		public String toString() {
			return ProceduralMemoryImpl.class.getSimpleName() + "Broadcast";
		}
	}

	@Override
	public void learn(BroadcastContent content) {
		Collection<Node> nodes = ((NodeStructure) content).getNodes();
		for (Node n : nodes) {
			// learning algorithm
			n.getId();
		}
	}

	/*
	 * Impl. of observer pattern. Sends scheme to all registered ProceduralMemory
	 * Listeners
	 */
	@Override
	public void createInstantiation(Scheme s) {
		logger.log(Level.FINE, "Sending scheme from procedural memory",
				TaskManager.getCurrentTick());
		Behavior b = s.getInstantiation();
		b.setDecayStrategy(behaviorDecayStrategy);
		b.setExciteStrategy(behaviorExciteStrategy);
		for (ProceduralMemoryListener listener : proceduralMemoryListeners) {
			listener.receiveBehavior(b);
		}
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemory#containsScheme(edu.memphis.ccrg.lida.proceduralmemory.Scheme)
	 */
	@Override
	public boolean containsScheme(Scheme s) {
		return schemeSet.contains(s);
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemory#getSchemeCount()
	 */
	@Override
	public int getSchemeCount() {
		return schemeSet.size();
	}

	@Override
	public void decayModule(long ticks) {
		for (Scheme s : schemeSet){
			s.decay(ticks);
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
	
}