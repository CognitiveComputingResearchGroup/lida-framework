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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Condition;
import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.ConcurrentHashSet;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;

/**
 * Default implementation of {@link ProceduralMemory}. Indexes scheme by context
 * elements for quick access.
 * @author Ryan J. McCall
 *
 */
public class ProceduralMemoryImpl extends FrameworkModuleImpl implements ProceduralMemory, BroadcastListener {

	private static final Logger logger = Logger.getLogger(ProceduralMemoryImpl.class.getCanonicalName());
	private static final ElementFactory factory = ElementFactory.getInstance();

	/*
	 * Schemes indexed by Linkables in their context.
	 */
	private Map<Object, Set<Scheme>> contextSchemeMap = new ConcurrentHashMap<Object, Set<Scheme>>();
//	TODO support for Node desirability, index by result
//	private Map<Object, Set<Scheme>> resultSchemeMap;

	/*
	 * Convenient for decaying the schemes
	 */
	private Set<Scheme> schemeSet = new ConcurrentHashSet<Scheme>();
	
	/*
	 * A pool of all conditions in all schemes in the procedural memory
	 */
	private Map<Object,Condition> conditionPool = new HashMap<Object,Condition>();
	
	/*
	 * Recent contents of consciousness that have not yet decayed away.
	 */
	private NodeStructure broadcastBuffer = new NodeStructureImpl();

	/*
	 * Listeners of this Procedural Memory
	 */
	private List<ProceduralMemoryListener> proceduralMemoryListeners = new ArrayList<ProceduralMemoryListener>();
	
	/*
	 * Factory name of default scheme activation strategy
	 */
	private static final String DEFAULT_SCHEME_ACTIVATION_STRATEGY = "BasicSchemeActivationStrategy";
	/*
	 * Determines how scheme are given activation and whether they should be
	 * instantiated
	 */
	private SchemeActivationStrategy schemeActivationStrategy;	

	
	/**
	 * This module can accept parameters for the decay and excite strategies for
	 * behaviors instantiated in this module.  The parameters names are:<br><br/>
	 * 
	 * <b>proceduralMemory.schemeActivationStrategy</b> - name of {@link SchemeActivationStrategy} in the {@link ElementFactory}<br/>
	 * 
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModuleImpl#init()
	 */
	@Override
	public void init() {	
		String strategyName = (String) getParam("proceduralMemory.schemeActivationStrategy", DEFAULT_SCHEME_ACTIVATION_STRATEGY);
		schemeActivationStrategy = (SchemeActivationStrategy) factory.getStrategy(strategyName);
		if(schemeActivationStrategy != null){
			schemeActivationStrategy.setProceduralMemory(this);
		}else{
			logger.log(Level.SEVERE, "unable to get scheme activation strategy {0} from factory", 
					strategyName);
		}
	}
	
	@Override
	public void addListener(ModuleListener listener) {
		if (listener instanceof ProceduralMemoryListener) {
			proceduralMemoryListeners.add((ProceduralMemoryListener) listener);
		}else{
			logger.log(Level.WARNING, "Requires ProceduralMemoryListener but received {1}", 
					new Object[]{TaskManager.getCurrentTick(), listener});
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
		if(schemes != null){
			for (Scheme scheme : schemes){
				addScheme(scheme);
			}
		}else{
			logger.log(Level.WARNING, "Cannot add null Collection", TaskManager.getCurrentTick());
		}
	}

	@Override
	public void addScheme(Scheme scheme) {
		schemeSet.add(scheme);
		indexSchemeByElements(scheme, scheme.getContextConditions(), contextSchemeMap);
		for(Condition c: scheme.getContextConditions()){
			if(c instanceof Node){
				addCondition(c);
			}else if(c instanceof NodeStructure){
				Collection<Node> nodes = ((NodeStructure) c).getNodes();
				for(Node n: nodes){
					if(!(n instanceof Argument)){
						addCondition(n);
					}
				}
			}
		}
		//TODO index by result 
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
	private void indexSchemeByElements(Scheme scheme, Collection<Condition> elements, 
									   Map<Object, Set<Scheme>> map) {
		for (Condition element : elements) {
			synchronized (element) {
				Object id = element.getConditionId();
				Set<Scheme> values = map.get(id);
				if (values == null) {
					values = new ConcurrentHashSet<Scheme>();
					map.put(id, values);
				}
				values.add(scheme);
			}
		}
	}
	
	@Override
	public Condition addCondition(Condition c){
		Condition result = conditionPool.get(c.getConditionId());
		if(result == null){
			logger.log(Level.FINE,"Condition {1} added.",
					new Object[]{TaskManager.getCurrentTick(), c});
			conditionPool.put(c.getConditionId(),c);
			result = c;
		}
		return result;
	}
	
	@Override
	public Condition getCondition(Object id){
		return conditionPool.get(id);
	}
	
	@Override
	public void receiveBroadcast(Coalition coalition) {
		NodeStructure ns = (NodeStructure) coalition.getContent();
		broadcastBuffer.mergeWith(ns);	
		activateSchemes(ns);
	}
	
	@Override
	public void activateSchemes(NodeStructure ns) {
		logger.log(Level.FINEST, "Procedural memory activates schemes", TaskManager.getCurrentTick());
		
		//this may be a new scheme activation strategy
		for(Node n: ns.getNodes()){
//			// TODO: Another option is to use GoalDegree and Activation
//			if (condition.getNetDesirability() < goalThreshold) {
//				if (behaviorsByContextCondition.containsKey(condition)) {
//					passActivationToContextOrResult(condition,
//							behaviorsByContextCondition, ConditionSet.CONTEXT);
//				} else if (behaviorsByNegContextCondition
//						.containsKey(condition)) {
//					passActivationToContextOrResult(condition,
//							behaviorsByNegContextCondition,
//							ConditionSet.NEGCONTEXT);
//				}
//			} else {
//				if (behaviorsByAddingItem.containsKey(condition)) {
//					passActivationToContextOrResult(condition,
//							behaviorsByAddingItem, ConditionSet.ADDING_LIST);
//				} else if (behaviorsByDeletingItem.containsKey(condition)) {
//					// TODO: Change this if protected goals are used
//					passActivationToContextOrResult(condition,
//							behaviorsByDeletingItem, ConditionSet.DELETING_LIST);
//				}
//			}
			
			
			
			Condition c = conditionPool.get(n.getExtendedId());
			if(c != null){
				c.setActivation(n.getActivation());
				Set<Scheme> schemes = contextSchemeMap.get(n);
				if(schemes != null){
					for(Scheme s: schemes){
						Collection<Condition> conditions = s.getContextConditions();
						for(Condition c2: conditions){
							if(c2 instanceof NodeStructure){
								NodeStructure conditionNs = (NodeStructure) c2;
								for(Node conditionNode: conditionNs.getNodes()){
									
								}
								//if c2 contains an argument then instantiate
							}
						}
					}
				}
			}
		}	
		
		schemeActivationStrategy.activateSchemesWithBroadcast(ns, contextSchemeMap);
	}

	@Override
	public void learn(Coalition coalition) {
		//TODO 
	}
	
	@Override
	public void decayModule(long ticks){
		for (Condition c: conditionPool.values()){
			c.decay(ticks);
		}
		
		for (Scheme s : schemeSet){
			s.decayBaseLevelActivation(ticks);
			if(s.isRemovable()){
				//TODO test
//				removeScheme(s);
			}
		}
	}
	
	@Override
	public void createInstantiation(Scheme s) {
		logger.log(Level.FINE, "Sending scheme from procedural memory",
				TaskManager.getCurrentTick());
		Behavior b = s.getInstantiation();
		for (ProceduralMemoryListener listener : proceduralMemoryListeners) {
			listener.receiveBehavior(b);
		}
	}

	@Override
	public boolean containsScheme(Scheme s) {
		return schemeSet.contains(s);
	}

	@Override
	public int getSchemeCount() {
		return schemeSet.size();
	}
	
	@Override
	public void removeScheme(Scheme scheme) {
		schemeSet.remove(scheme);
		removeFromMap(scheme, scheme.getContextConditions(), contextSchemeMap);
	}
	
	private <E> void removeFromMap(Scheme scheme, Collection<E> keys, Map<?, Set<Scheme>> map){
		for(E key: keys){
			if(map.containsKey(key)){
				map.get(key).remove(scheme);
			}
		}
	}

	@Override
	public Object getModuleContent(Object... params) {
		if("schemes".equals(params[0])){
			return Collections.unmodifiableCollection(schemeSet);
		}
		return null;
	}	
}