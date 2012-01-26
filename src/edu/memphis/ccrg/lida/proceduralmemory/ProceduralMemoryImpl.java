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
		taskSpawner.addTask(new ProceduralMemoryBackgroundTask());
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
		schemeSet.add(scheme);//for decaying scheme's BLA
		
		//add context and adding list to condition pool
		for(Condition c: scheme.getContextConditions()){
			//TODO in future,case when Condition is NodeStructure
			if(c instanceof Node){
				addCondition(c);
			}
		}
		for(Condition c: scheme.getAddingList()){
			//TODO in future,case when Condition is NodeStructure
			if(c instanceof Node){
				addCondition(c);
			}
		}
		
		indexSchemeByElements(scheme, scheme.getContextConditions(), contextSchemeMap);
		indexSchemeByElements(scheme, scheme.getAddingList(), addingSchemeMap);
	}
	
//	@Override
	private Condition addCondition(Condition c){
		Condition result = conditionPool.get(c.getConditionId());
		if(result == null){
			logger.log(Level.FINE,"Condition {1} added.",
					new Object[]{TaskManager.getCurrentTick(), c});
			conditionPool.put(c.getConditionId(),c);
			result = c;
		}
		return result;
	}
	
//	@Override
//	public Condition getCondition(Object id){
//		return conditionPool.get(id);
//	}
	
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
			Node conditionNode = (Node) conditionPool.get(broadcastNode.getConditionId());
			if(conditionNode != null){ //TODO discuss == null case, perhaps the Node should still be added to the buffer? 
				if(broadcastBuffer.containsNode(conditionNode)){ // node already in buffer
					if(broadcastNode.getActivation() > conditionNode.getActivation()){
						conditionNode.setActivation(broadcastNode.getActivation());
					}
					if(broadcastNode instanceof UnifyingNode){
						UnifyingNode uBroadcastNode = (UnifyingNode) broadcastNode;
						if(conditionNode instanceof UnifyingNode){
							UnifyingNode uConditionNode = (UnifyingNode)conditionNode;
							if(uBroadcastNode.getDesirability() > uConditionNode.getDesirability()){
								uConditionNode.setDesirability(uBroadcastNode.getDesirability());
							}	
						}else{
							//TODO
							logger.log(Level.WARNING, "", TaskManager.getCurrentTick());
						}
					}
					
				}else{ 
					broadcastBuffer.addNode(conditionNode, false);
				}
			}
		}	
		learn(coalition);
	}
	
	@Override
	public void learn(Coalition coalition) {
//		NodeStructure ns = (NodeStructure) coalition.getContent();
		//TODO
	}
	
	//TODO could make a separate class and remove 'scheme activation strategy'
	private class ProceduralMemoryBackgroundTask extends FrameworkTaskImpl {
		@Override
		protected void runThisFrameworkTask() {
			activateSchemes(broadcastBuffer);
		}
	}
	
	//TODO if we decide against scheme activation strategy then this becomes a parameter of the module.
	private double schemeSelectionThreshold = 0.0;
	
	@Override
	public void activateSchemes(NodeStructure ns) {
		//To prevent a scheme from being instantiated multiple times all schemes over threshold are stored in a set
		Set<Scheme> toInstantiate = new HashSet<Scheme>();
		for (Node n: ns.getNodes()) {	//TODO consider links
			Set<Scheme> schemes = contextSchemeMap.get(n.getConditionId());
			if (schemes != null) {
				for (Scheme s : schemes) {
					if (s.getActivation() >= schemeSelectionThreshold) {
						toInstantiate.add(s);
					}
				}
			}
			
			if(n instanceof UnifyingNode){
				UnifyingNode uNode = (UnifyingNode) n;
				if(uNode.getNetDesirability() > 0.0){//TODO boolean method?
					schemes = addingSchemeMap.get(uNode.getConditionId());
					if (schemes != null) {
						for (Scheme s : schemes) {
							if (s.getActivation() >= schemeSelectionThreshold) {
								toInstantiate.add(s);
							}
						}
					}
				}
			}
		}
		
		for(Scheme s: toInstantiate){
			createInstantiation(s);
		}
	}

	@Override
	public void createInstantiation(Scheme s) {
		logger.log(Level.FINE, "Instantiating scheme {1} in Procedural Memory",
				new Object[]{TaskManager.getCurrentTick(),s});
		Behavior b = factory.getBehavior(s);
		for (ProceduralMemoryListener listener : proceduralMemoryListeners) {
			listener.receiveBehavior(b);
		}
	}

	@Override
	public void decayModule(long ticks){
		broadcastBuffer.decayNodeStructure(ticks);
		
		for (Scheme s : schemeSet){
			s.decayBaseLevelActivation(ticks);
			if(s.isRemovable()){
				//TODO test first
//				removeScheme(s);
			}
		}
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