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
import java.util.HashSet;
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
import edu.memphis.ccrg.lida.framework.shared.UnifyingNode;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;

/**
 * Default implementation of {@link ProceduralMemory}. Indexes scheme by context
 * elements for quick access. Assumes that the {@link Condition} of {@link Scheme} are {@link Node} only.
 * @author Ryan J. McCall
 *
 */
public class ProceduralMemoryImpl extends FrameworkModuleImpl implements ProceduralMemory, BroadcastListener {

	private static final Logger logger = Logger.getLogger(ProceduralMemoryImpl.class.getCanonicalName());
	
	private static final ElementFactory factory = ElementFactory.getInstance();

	/*
	 * Schemes indexed by Nodes in their context.
	 */
	private Map<Object, Set<Scheme>> contextSchemeMap = new ConcurrentHashMap<Object, Set<Scheme>>();

	/*
	 * Schemes indexed by Nodes in their adding list.
	 */
	private Map<Object, Set<Scheme>> addingSchemeMap = new ConcurrentHashMap<Object, Set<Scheme>>();

	/*
	 * Set of all schemes current in the module. Convenient for decaying the schemes' bla.
	 */
	private Set<Scheme> schemeSet = new ConcurrentHashSet<Scheme>();
	
	/*
	 * A pool of all conditions (context and adding) in all schemes in the procedural memory
	 */
	private Map<Object,Condition> conditionPool = new HashMap<Object,Condition>();
	
	/*
	 * Recent contents of consciousness that have not yet decayed away.
	 */
	private InternalNodeStructure broadcastBuffer = new InternalNodeStructure();
	
	/**
	 * Allows Nodes to be added without copying. 
	 * Warning: doing so allows the same java object of Node to exist in multiple places. 
	 * @author Ryan J. McCall
	 * @see NodeStructureImpl
	 */
	protected class InternalNodeStructure extends NodeStructureImpl {
		@Override
		public Node addNode(Node n, boolean copy) {
			return super.addNode(n, copy);
		}
	}

	/*
	 * Listeners of this Procedural Memory
	 */
	private List<ProceduralMemoryListener> proceduralMemoryListeners = new ArrayList<ProceduralMemoryListener>();

	private static final double DEFAULT_SCHEME_SELECTION_THRESHOLD = 0.0;
	
	/*
	 * Determines how much activation a scheme should have to be instantiated
	 */
	private double schemeSelectionThreshold;
	
	/**
	 * This module can accept parameters for the decay and excite strategies for
	 * behaviors instantiated in this module.  The parameters names are:<br><br/>
	 * 
	 * <b>proceduralMemory.schemeSelectionThreshold</b> - amount of activation schemes must have to be instantiated<br/>
	 * 
	 * @see edu.memphis.ccrg.lida.framework.FrameworkModuleImpl#init()
	 */
	@Override
	public void init() {	
		schemeSelectionThreshold = getParam("proceduralMemory.schemeSelectionThreshold", DEFAULT_SCHEME_SELECTION_THRESHOLD);
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
	public void addSchemes(Collection<Scheme> schemes) {
		if(schemes != null){
			for (Scheme scheme : schemes){
				addScheme(scheme);
			}
		}else{
			logger.log(Level.WARNING, "Cannot add null Collection", TaskManager.getCurrentTick());
		}
	}

	/**
	 * Adds specified scheme to this {@link ProceduralMemory}.</br>
	 * Note: all conditions (context or adding) in the Scheme should have already been added as conditions 
	 * using {@link #addCondition(Condition)}. The correct way to create a Scheme is to first add its conditions to
	 * {@link ProceduralMemory} using {@link #addCondition(Condition)} AND then, use the returned references as the new
	 * Conditions of the Scheme. Finally this method should be called to add the Scheme to this {@link ProceduralMemory}.</br>
	 * The {@link BasicProceduralMemoryInitializer} uses this procedure for adding new Schemes.
	 * @param scheme the {@link Scheme} to be added
	 * @see BasicProceduralMemoryInitializer
	 */
	@Override
	public void addScheme(Scheme scheme) {
		schemeSet.add(scheme);//for decaying scheme's BLA
		
		//TODO in future,cases when Condition is NodeStructure
		//add context and adding list to condition pool
		for(Condition c: scheme.getContextConditions()){
			if(c instanceof Node){
				addCondition(c);
			}
		}
		for(Condition c: scheme.getAddingList()){
			if(c instanceof Node){
				addCondition(c);
			}
		}
		
		indexSchemeByElements(scheme, scheme.getContextConditions(), contextSchemeMap);
		indexSchemeByElements(scheme, scheme.getAddingList(), addingSchemeMap);
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
	public Condition getCondition(Object conditionId){
		return conditionPool.get(conditionId);
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
	
	/*
	 * Assumes Conditions are Nodes only 
	 */
	@Override
	public void receiveBroadcast(Coalition coalition) {
		NodeStructure ns = (NodeStructure) coalition.getContent();
		for(Node broadcastNode: ns.getNodes()){		
			//For each broadcast node check if it is in the condition pool 
			//i.e. there is at least 1 scheme that has context or result condition including this node
			Node conditionNode = (Node) conditionPool.get(broadcastNode.getConditionId());
			if(conditionNode != null){
				//If there is such a node check if the Node is already in the broadcast buffer.
				boolean isNotInBuffer = !broadcastBuffer.containsNode(conditionNode);
				if(isNotInBuffer){
					//Adds a reference to the condition Node (from condition pool) 
					//to the broadcast buffer without copying
					broadcastBuffer.addNode(conditionNode, false);
				}
				//Update the activation of the condition-pool/broadcast-buffer node if needed 
				if(broadcastNode.getActivation() > conditionNode.getActivation() || isNotInBuffer){
					conditionNode.setActivation(broadcastNode.getActivation());
				}
				//Update the desirability of the condition-pool/broadcast-buffer node if needed
				if(broadcastNode instanceof UnifyingNode){
					UnifyingNode uBroadcastNode = (UnifyingNode) broadcastNode;
					if(conditionNode instanceof UnifyingNode){
						UnifyingNode uConditionNode = (UnifyingNode)conditionNode;
						if(uBroadcastNode.getDesirability() > uConditionNode.getDesirability()||isNotInBuffer){
							uConditionNode.setDesirability(uBroadcastNode.getDesirability());
						}	
					}else{
						logger.log(Level.WARNING, "Expected condition to be UnifyingNode but was {1}", 
								new Object[]{TaskManager.getCurrentTick(),conditionNode.getClass()});
					}
				}
			}
		}	
		learn(coalition);
		//Spawn a new task to activate and instantiate relevant schemes.
		//This task runs only once in the next tick
		taskSpawner.addTask(new FrameworkTaskImpl() {
			@Override
			protected void runThisFrameworkTask() {
				activateSchemes();
				setTaskStatus(TaskStatus.FINISHED);
			}
		});
	}
	
	@Override
	public void learn(Coalition coalition) {
//		NodeStructure ns = (NodeStructure) coalition.getContent();
		// make sure to use the correct way of adding new schemes see addScheme
		//TODO
	}
	
	@Override
	public void activateSchemes() {
		//To prevent a scheme from being instantiated multiple times all schemes over threshold are stored in a set
		Set<Scheme> relevantSchemes = new HashSet<Scheme>();
		for (Node n: broadcastBuffer.getNodes()) {	//TODO consider links
			//Get all schemes that contain Node n in their context and add them to relevantSchemes
			Set<Scheme> schemes = contextSchemeMap.get(n.getConditionId());
			if (schemes != null) {
				relevantSchemes.addAll(schemes);
			}
			//If Node n has positive desirability, 
			//get the schemes that have n in their adding list and add them to relevantSchemes
			if(n instanceof UnifyingNode){
				UnifyingNode uNode = (UnifyingNode) n;
				if(uNode.getNetDesirability() > 0.0){//TODO boolean method?
					schemes = addingSchemeMap.get(uNode.getConditionId());
					if (schemes != null) {
						relevantSchemes.addAll(schemes);
					}
				}
			}
		}
		//For each relevant scheme, check if it should be instantiated, if so instantiate.
		for(Scheme s: relevantSchemes){
			if(shouldInstantiate(s, broadcastBuffer)){
				createInstantiation(s);
			}
		}
	}
	
	private double goalOrientedness = 0.5;//TODO parameter
	
	@Override
	public boolean shouldInstantiate(Scheme s, NodeStructure broadcastBuffer){
		double overallSalience = ((1-goalOrientedness)*s.getAverageContextActivation()+
				goalOrientedness*s.getAverageAddingListNetDesirability())/2;
		return overallSalience >= schemeSelectionThreshold;
	}

	@Override
	public void createInstantiation(Scheme s) {
		logger.log(Level.FINE, "Instantiating scheme: {1} in ProceduralMemory",
				new Object[]{TaskManager.getCurrentTick(),s});
		Behavior b = factory.getBehavior(s);
		for (ProceduralMemoryListener listener : proceduralMemoryListeners) {
			listener.receiveBehavior(b);
		}
	}

	@Override
	public void decayModule(long ticks){
		broadcastBuffer.decayNodeStructure(ticks);

		//TODO implement along with learning
//		for (Scheme s : schemeSet){
//			s.decayBaseLevelActivation(ticks);
//			if(s.isRemovable()){
//				removeScheme(s);
//			}
//		}
	}
	
	@Override
	public void removeScheme(Scheme scheme) {
		schemeSet.remove(scheme);
		removeFromMap(scheme, scheme.getContextConditions(), contextSchemeMap);
		removeFromMap(scheme, scheme.getAddingList(), addingSchemeMap);
	}
	
	private <E> void removeFromMap(Scheme scheme, Collection<E> keys, Map<?, Set<Scheme>> map){
		for(E key: keys){
			if(map.containsKey(key)){
				map.get(key).remove(scheme);
			}
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
	public Object getModuleContent(Object... params) {
		if("schemes".equals(params[0])){
			return Collections.unmodifiableCollection(schemeSet);
		}
		return null;
	}	
}