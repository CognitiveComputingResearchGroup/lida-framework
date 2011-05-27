/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.initialization.CodeletDef;
import edu.memphis.ccrg.lida.framework.initialization.LinkableDef;
import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.DefaultTotalActivationStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.NoDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.NoExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.pam.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;


/**
 * Standard factory for the basic elements of the framework. Support for
 * {@link Node}s, {@link Link}s, {@link Codelet}s, and {@link NodeStructure}s
 * 
 * @author Javier Snaider, Ryan J. McCall
 */
public class ElementFactory {

	private static final Logger logger = Logger
			.getLogger(ElementFactory.class.getCanonicalName());

	/**
	 * Used to assign unique IDs to elements.
	 */
	private static int elementIdCount;

	/**
	 * Used to retrieve default decay strategy from 'decayStrategies' map.
	 */
	private String defaultDecayType;

	/**
	 * Used to retrieve default excite strategy from 'exciteStrategies' map.
	 */
	private String defaultExciteType;

	/**
	 * Used to retrieve default link class from 'linkClasses' map. e.g.
	 * edu.memphis.ccrg.lida.framework.shared.LinkImpl
	 */
	private String defaultLinkClassName;

	/**
	 * Specifies default link type used by the factory. e.g. "LinkImpl"
	 */
	private String defaultLinkType;

	/**
	 * Used to retrieve default node class from 'nodeClasses' map. e.g.
	 * edu.memphis.ccrg.lida.framework.shared.NodeImpl
	 */
	private String defaultNodeClassName;

	/**
	 * Specifies default node type used by the factory. e.g. "NodeImpl"
	 */
	private String defaultNodeType;

	/**
	 * Map of all the ExciteStrategies available to this factory
	 */
	private Map<String, StrategyDef> exciteStrategies = new HashMap<String, StrategyDef>();

	/**
	 * Map of all the DecayStrategies available to this factory
	 */
	private Map<String, StrategyDef> decayStrategies = new HashMap<String, StrategyDef>();

	/**
	 * Map of all the strategies (of any type) available to this factory
	 */
	private Map<String, StrategyDef> strategies = new HashMap<String, StrategyDef>();

	/**
	 * Map of LinkableDefs for the Link types available to this factory indexed
	 * by their linkFactoryName.
	 */
	private Map<String, LinkableDef> linkClasses = new HashMap<String, LinkableDef>();

	/**
	 * Map of LinkableDefs for the Node types available to this factory indexed
	 * by their nodeFactoryName.
	 */
	private Map<String, LinkableDef> nodeClasses = new HashMap<String, LinkableDef>();

	/**
	 * Map of CodeletDefs for the Codelet types available to this factory
	 * indexed by name as specified in factories data.
	 */
	private Map<String, CodeletDef> codelets = new HashMap<String, CodeletDef>();

	/**
	 * Sole instance of this class that will be used.
	 */
	private static ElementFactory instance = new ElementFactory();

	/**
	 * Name of decay strategy type
	 * 
	 * @see LidaFactories.xsd
	 */
	private static final String decayStrategyType = "decay";

	/**
	 * Name of excite strategy type
	 * 
	 * @see LidaFactories.xsd
	 */
	private static final String exciteStrategyType = "excite";
	
	//TODO Implement PropagationStrategy in Pam in a generic way
	/**
	 * Name of propagation strategy type
	 * 
	 * @see LidaFactories.xsd
	 */
	@SuppressWarnings("unused")
	private static final String propagationStrategyType = "propagation";

	/**
	 * Returns the sole instance of this factory. Implements the Singleton
	 * pattern.
	 * 
	 * @return NodeFactory sole instance of this class
	 */
	public static ElementFactory getInstance() {
		return instance;
	}

	/*
	 * Creates the Factory and adds default Node, Link and Strategies to the
	 * Maps in the Factory.
	 */
	private ElementFactory() {
		defaultNodeType = NodeImpl.class.getSimpleName();
		defaultNodeClassName = NodeImpl.class.getCanonicalName();
		addNodeType(defaultNodeType, defaultNodeClassName);
		//
		addNodeType(PamNodeImpl.class.getSimpleName(), PamNodeImpl.class
				.getCanonicalName());

		defaultLinkType = LinkImpl.class.getSimpleName();
		defaultLinkClassName = LinkImpl.class.getCanonicalName();
		addLinkType(defaultLinkType, defaultLinkClassName);
		//		
		addLinkType(PamLinkImpl.class.getSimpleName(), PamLinkImpl.class
				.getCanonicalName());

		defaultDecayType = "defaultDecay";
		addDecayStrategy(defaultDecayType, new StrategyDef(
				LinearDecayStrategy.class.getCanonicalName(), defaultDecayType,
				new HashMap<String, Object>(), decayStrategyType, true));

		defaultExciteType = "defaultExcite";
		addExciteStrategy(defaultExciteType, new StrategyDef(
				LinearExciteStrategy.class.getCanonicalName(),
				defaultExciteType, new HashMap<String, Object>(),
				exciteStrategyType, true));
		
		String strategyName="noDecay";
		addDecayStrategy(strategyName, new StrategyDef(
				NoDecayStrategy.class.getCanonicalName(), strategyName,
				new HashMap<String, Object>(), decayStrategyType, true));
		
		strategyName="noExcite";
		addExciteStrategy(strategyName, new StrategyDef(
				NoExciteStrategy.class.getCanonicalName(), strategyName,
				new HashMap<String, Object>(), exciteStrategyType, true));

		Map<String, String> defaultStrategies = new HashMap<String, String>();
		defaultStrategies.put("decay", "defaultDecay");
		defaultStrategies.put("excite", "defaultExcite");
		
		Map<String, Object> params= new HashMap<String, Object>();
		params.put("baseLevelDecayStrategy", "noDecay");
		params.put("baseLevelExciteStrategy", "noExcite");
		params.put("baseLevelRemovalThreshold", -1.0);
		params.put("baseLevelActivation", 0.0);
		LinkableDef newNodeDef = new LinkableDef(PamNodeImpl.class.getCanonicalName(), defaultStrategies, "NoDecayPamNode", params);
		addNodeType(newNodeDef);
		
		LinkableDef newLinkDef = new LinkableDef(PamLinkImpl.class.getCanonicalName(), defaultStrategies, "NoDecayPamLink", params);
		addLinkType(newLinkDef);
		
		strategyName="DefaultTotalActivation";
		StrategyDef strategyDef = new StrategyDef(DefaultTotalActivationStrategy.class.getCanonicalName(), 
				strategyName, new HashMap<String, Object>(), "other", true);
		addStrategy(strategyName, strategyDef);
	}

	/**
	 * Adds a {@link DecayStrategy} indexed by specified name.
	 * 
	 * @param name
	 *            the name used to refer to the strategy
	 * @param decayDef
	 *            the decay strategy's {@link StrategyDef}
	 */
	public void addDecayStrategy(String name, StrategyDef decayDef) {
		decayStrategies.put(name, decayDef);
		strategies.put(name, decayDef);
	}

	/**
	 * Adds an excite strategy indexed by specified name.
	 * 
	 * @param name
	 *            the name used to reference the strategy 
	 * @param exciteDef
	 *            the excite strategy {@link StrategyDef}
	 */
	public void addExciteStrategy(String name, StrategyDef exciteDef) {
		exciteStrategies.put(name, exciteDef);
		strategies.put(name, exciteDef);
	}

	/**
	 * Adds a strategy to this factory indexed by specified name.
	 * 
	 * @param name
	 *            the name
	 * @param strategyDef
	 *            the {@link StrategyDef}
	 */
	public void addStrategy(String name, StrategyDef strategyDef) {
		strategies.put(name, strategyDef);
	}

	/**
	 * Adds a Link type to this factory
	 * 
	 * @param linkDef
	 *            the link def
	 */
	public void addLinkType(LinkableDef linkDef) {
		linkClasses.put(linkDef.getName(), linkDef);
	}

	/**
	 * Adds a link type indexed by specified typeName
	 * 
	 * @param typeName
	 *            the link type
	 * @param className
	 *            the class name
	 */
	public void addLinkType(String typeName, String className) {
		linkClasses.put(typeName, new LinkableDef(className,
				new HashMap<String, String>(), typeName,
				new HashMap<String, Object>()));
	}

	/**
	 * Adds a {@link Node} type to this factory
	 * 
	 * @param nodeDef
	 *            the node def
	 */
	public void addNodeType(LinkableDef nodeDef) {
		nodeClasses.put(nodeDef.getName(), nodeDef);
	}

	/**
     * Adds a Node type indexed by specified typeName
	 * 
	 * @param typeName
	 *            the simple node name
	 * @param className
	 *            the canonical node name
	 */
	public void addNodeType(String typeName, String className) {
		nodeClasses.put(typeName, new LinkableDef(className,
				new HashMap<String, String>(), typeName,
				new HashMap<String, Object>()));
	}

	/**
	 * Adds the codelet type.
	 * 
	 * @param codeletDef
	 *            the codelet def
	 */
	public void addCodeletType(CodeletDef codeletDef) {
		codelets.put(codeletDef.getName(), codeletDef);
	}

	/**
	 * Adds the codelet type.
	 * 
	 * @param typeName
	 *            the simple codelet name
	 * @param className
	 *            the canonical codelet name
	 */
	public void addCodeletType(String typeName,
			String className) {
		codelets.put(typeName, new CodeletDef(className,
				new HashMap<String, String>(), typeName,
				new HashMap<String, Object>()));
	}

	/**
	 * Gets default link type.
	 * 
	 * @return the default link type
	 */
	public String getDefaultLinkType() {
		return defaultLinkType;
	}

	/**
	 * Gets default node type.
	 * 
	 * @return the default node type
	 */
	public String getDefaultNodeType() {
		return defaultNodeType;
	}
	/**
	 * Returns whether this factory contains specified {@link Strategy} type.
	 * @param strategyType name of strategy type
	 * @return true if factory contains type or false if not
	 */
	public boolean containsStrategy(String strategyType){
		return strategies.containsKey(strategyType);
	}

	/**
	 * Returns whether this factory contains specified {@link Node} type.
	 * @param nodeType name of node type
	 * @return true if factory contains type or false if not
	 */
	public boolean containsNodeType(String nodeType) {
		return nodeClasses.containsKey(nodeType);
	}

	/**
	 * Returns whether this factory contains specified {@link Link} type.
	 * @param linkType name of Link type
	 * @return true if factory contains type or false if not
	 */
	public boolean containsLinkType(String linkType) {
		return linkClasses.containsKey(linkType);
	}
	
	/**
	 * Returns whether this factory contains specified {@link Codelet} type.
	 * @param type String
	 * @return true if factory contains type or false if not
	 */
	public boolean containsCodeletType(String type) {
		return codelets.containsKey(type);
	}

	/**
	 * Gets decay strategy.
	 * 
	 * @param name
	 *            Name of DecayStrategy
	 * @return the decay strategy
	 */
	public DecayStrategy getDecayStrategy(String name) {
		DecayStrategy d = null;
		StrategyDef sd = decayStrategies.get(name);
		if (sd == null) {
			sd = decayStrategies.get(defaultDecayType);
			logger.log(Level.WARNING, "Strategy " + defaultExciteType
					+ " does not exist. Default used instead.", TaskManager
					.getCurrentTick());
		}
		d = (DecayStrategy) sd.getInstance();
		return d;
	}

	/**
	 * Gets excite strategy.
	 * 
	 * @param name
	 *            the name
	 * @return the excite strategy
	 */
	public ExciteStrategy getExciteStrategy(String name) {
		ExciteStrategy d = null;
		StrategyDef sd = exciteStrategies.get(name);
		if (sd == null) {
			sd = exciteStrategies.get(defaultExciteType);
			logger.log(Level.WARNING, "Strategy " + name
					+ " does not exist. Default used instead.", TaskManager
					.getCurrentTick());
		}
		d = (ExciteStrategy) sd.getInstance();
		return d;
	}

	/**
	 * Get a strategy by name.
	 * 
	 * @param name
	 *            Name of sought strategy.
	 * @return Strategy if found or null.
	 */
	public Strategy getStrategy(String name) {
		Strategy d = null;
		StrategyDef sd = strategies.get(name);
		if (sd != null) {
			d = sd.getInstance();
		}
		return d;
	}

	/**
	 * Creates and returns a new Link with specified source, sink, category, and
	 * activation.
	 * 
	 * @param source
	 *            Node that is link's source
	 * @param sink
	 *            Linkable that is link's sink
	 * @param category
	 *            LinkCategory
	 * @param activation
	 *            initial activation
	 * @param removalThreshold threshold to remain in {@link NodeStructure}
	 * @return new Link
	 */
	public Link getLink(Node source, Linkable sink, LinkCategory category,double activation, double removalThreshold) {
		return getLink(defaultLinkType, source, sink, category,
				defaultDecayType, defaultExciteType, activation, removalThreshold);
	}

	/**
	 * Creates and returns a new Link with specified source, sink, and category.
	 * Zero initial activation.
	 * 
	 * @param source
	 *            Node that is link's source
	 * @param sink
	 *            Linkable that is link's sink
	 * @param category
	 *            LinkCategory
	 * @return new Link
	 */
	public Link getLink(Node source, Linkable sink, LinkCategory category) {
		return getLink(defaultLinkType, source, sink, category,
				defaultDecayType, defaultExciteType, 
				Activatible.DEFAULT_ACTIVATION, Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
	}

	/**
	 * Checks if desiredType is-a requiredType.  Creates and returns
	 * a Link of desiredType with specified parameters.
	 * @param requiredType Required Link type for {@link NodeStructure}
	 * @param desiredType Desired Link type for returned Link. Must be a subtype of required type.
	 * @param source Link's source
	 * @param sink Link's sink
	 * @param category Link's {@link LinkCategory}
	 * @return new {@link Link} with specified attributes.
	 */
	public Link getLink(String requiredType, String desiredType, 
						Node source, Linkable sink, LinkCategory category) {
		LinkableDef requiredDef = linkClasses.get(requiredType);
		if(requiredDef == null){
			logger.log(Level.WARNING, "Factory does not contain link type: " + requiredType, TaskManager.getCurrentTick());
			return null;
		}
		LinkableDef desiredDef = linkClasses.get(desiredType);
		if(desiredDef == null){
			logger.log(Level.WARNING, "Factory does not contain link type: " + desiredType, TaskManager.getCurrentTick());
			return null;
		}
		
		Link l = null;
		try {
			Class<?> required = Class.forName(requiredDef.getClassName());
			Class<?> desired = Class.forName(desiredDef.getClassName());
			
			if(required != null && required.isAssignableFrom(desired)){
				l = getLink(desiredType, source, sink, category);
			}
		} catch (ClassNotFoundException exc) {
			exc.printStackTrace();
		}
		return l;
	}
	
	
	/**
	 * Creates and returns a new Link with specified type, source, sink, and
	 * category.
	 * 
	 * @param linkT
	 *            the link t
	 * @param source
	 *            Node that is link's source
	 * @param sink
	 *            Linkable that is link's sink
	 * @param category
	 *            LinkCategory
	 * @return new Link
	 */
	public Link getLink(String linkT, Node source, Linkable sink,
						LinkCategory category) {
		LinkableDef linkDef = linkClasses.get(linkT);
		if (linkDef == null) {
			logger.log(Level.WARNING, "LinkName " + linkT + " does not exist.",
					TaskManager.getCurrentTick());
			return null;
		}
		
		String decayB = linkDef.getDefaultStrategies().get(decayStrategyType);
		if (decayB == null) {
			decayB = defaultDecayType;
		}
		String exciteB = linkDef.getDefaultStrategies().get(exciteStrategyType);
		if (exciteB == null) {
			exciteB = defaultExciteType;
		}

		return getLink(linkT, source, sink, category, decayB, exciteB, 
				Activatible.DEFAULT_ACTIVATION, Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
	}

	/**
	 * Creates and returns a new Link of specified type with specified source,
	 * sink, LinkCategory, DecayStrategy, ExciteStrategy, and category.
	 * 
	 * @param linkT
	 *            Link type
	 * @param source
	 *            Link's source
	 * @param sink
	 *            Link's sink
	 * @param category
	 *            Link's category
	 * @param decayStrategy
	 *            Link's DecayStrategy
	 * @param exciteStrategy
	 *            Link's ExciteStrategy
	 * @param activation
	 *            initial activation
	 * @param removalThreshold threshold of activation required to remain active
	 * @return new Link
	 */
	public Link getLink(String linkT, Node source, Linkable sink,
			LinkCategory category, String decayStrategy, String exciteStrategy,
			double activation, double removalThreshold) {
		
		if(source == null){
			logger.log(Level.WARNING, "Cannot create a link with a null source.",
					TaskManager.getCurrentTick());
			return null;
		}
		if(sink == null){
			logger.log(Level.WARNING, "Cannot create a link with a null sink.",
					TaskManager.getCurrentTick());
			return null;
		}
		if(category == null){
			logger.log(Level.WARNING, "Cannot create a link with a null category.",
					TaskManager.getCurrentTick());
			return null;
		}
		
		Link link = null;
		try {
			LinkableDef linkDef = linkClasses.get(linkT);
			if (linkDef == null) {
				logger.log(Level.WARNING, "LinkName " + linkT
						+ " does not exist.", TaskManager.getCurrentTick());
				return null;
			}

			String className = linkDef.getClassName();
			link = (Link) Class.forName(className).newInstance();
			link.setSource(source);
			link.setSink(sink);
			link.setCategory(category);
			link.setActivation(activation);
			link.setActivatibleRemovalThreshold(removalThreshold);
			setActivatibleStrategies(link, decayStrategy, exciteStrategy);
			link.init(linkDef.getParams());

		} catch (InstantiationException e) {
			logger.log(Level.WARNING, "Error creating Link.", TaskManager
					.getCurrentTick());
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Error creating Link.", TaskManager
					.getCurrentTick());
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Error creating Link.", TaskManager
					.getCurrentTick());
		}
		return link;
	}
	
	/**
	 * Creates a default node with the default strategies and default activation.
	 * @see Activatible
	 * 
	 * @return the node
	 */
	public Node getNode() {
		return getNode(defaultNodeType, defaultDecayType, defaultExciteType,
				"Node", Activatible.DEFAULT_ACTIVATION, Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
	}

	/**
	 * Creates a copy of the supplied node with the default strategies. Note that
	 * the new node is of a default type regardless of the node passed in the
	 * parameter.
	 * 
	 * @param oNode
	 *            supplied node
	 * @return the node
	 */
	public Node getNode(Node oNode) {
		return getNode(oNode, defaultNodeType, defaultDecayType,
				defaultExciteType);
	}

	/**
	 * Creates a copy of specified {@link Node}. The second argument specifies the type of
	 * the new node. The {@link Activatible} strategies of the
	 * new node are based on those specified by the {@link Node} type's {@link LinkableDef} 
	 * (specified by factoriesData.xml) If the {@link Node} type
	 * does not specify default {@link Activatible} strategies then the default strategies are used.
	 * All other values of the specified {@link Node} are copied to the new {@link Node}, e.g. activation.
	 * 
	 * @param oNode
	 *            supplied node
	 * @param nodeType
	 *            type of returned node
	 * @return the node
	 */
	public Node getNode(Node oNode, String nodeType) {
		if(oNode == null){
			logger.log(Level.WARNING, "Supplied node is null", TaskManager.getCurrentTick());
			return null;
		}		
		LinkableDef nodeDef = nodeClasses.get(nodeType);
		if (nodeDef == null) {
			logger.log(Level.WARNING, "nodeType " + nodeType
					+ " does not exist.", TaskManager.getCurrentTick());
			return null;
		}
		String decayB = nodeDef.getDefaultStrategies().get(decayStrategyType);
		String exciteB = nodeDef.getDefaultStrategies().get(exciteStrategyType);
		if (decayB == null) {
			decayB = defaultDecayType;
		}
		if (exciteB == null) {
			exciteB = defaultExciteType;
		}
		return getNode(oNode, nodeType, decayB, exciteB);
	}

	/**
	 * Creates a copy of specified node of desired type.  Desired type
	 * must pass is-a test with requireType.
	 * @param requiredType Default node type of {@link NodeStructure} 
	 * @param oNode {@link Node} to be copied.
	 * @param desiredType type of copied node
	 * @return copy of oNode of desired type or null
	 */
	public Node getNode(String requiredType, Node oNode, String desiredType) {
		LinkableDef requiredDef = nodeClasses.get(requiredType);
		if(requiredDef == null){
			logger.log(Level.WARNING, "Factory does not contain node type: " + requiredType, TaskManager.getCurrentTick());
			return null;
		}
		LinkableDef desiredDef = nodeClasses.get(desiredType);
		if(desiredDef == null){
			logger.log(Level.WARNING, "Factory does not contain node type: " + desiredType, TaskManager.getCurrentTick());
			return null;
		}
		
		Node newNode = null;
		try {
			Class<?> required = Class.forName(requiredDef.getClassName());
			Class<?> desired = Class.forName(desiredDef.getClassName());
			
			if(required != null && required.isAssignableFrom(desired)){
				newNode = getNode(oNode, desiredType);
			}
		} catch (ClassNotFoundException exc) {
			exc.printStackTrace();
		} 
		return newNode;
	}

	/**
	 * Creates a copy of oNode with the specified decay and excite strategies.
	 * The type of the new node will be the default node type.
	 * 
	 * @param oNode
	 *            supplied node
	 * @param decayStrategy
	 *            new node's decay strategy
	 * @param exciteStrategy
	 *            new node's excite strategy
	 * @return the node
	 */
	public Node getNode(Node oNode, String decayStrategy, String exciteStrategy) {
		return getNode(oNode, defaultNodeType, decayStrategy, exciteStrategy);
	}

	/*
	 * Creates a copy of oNode with specified node type. Copy will have
	 * Decay and Excite as specified in this method's parameters, not according
	 * to the default for the Node type.
	 * 
	 * @param oNode
	 *            supplied node
	 * @param nodeType
	 *            type for new node
	 * @param decayStrategy
	 *            decayStrategy new node's decay strategy
	 * @param exciteStrategy
	 *            exciteStrategy new node's excite strategy
	 * @return the node
	 */
	private Node getNode(Node oNode, String nodeType, String decayStrategy, String exciteStrategy) {
		if(oNode == null){
			logger.log(Level.WARNING, "Supplied node is null", TaskManager.getCurrentTick());
			return null;
		}
		Node n = getNode(nodeType,  decayStrategy, exciteStrategy, oNode.getLabel(),oNode.getActivation(), oNode.getActivatibleRemovalThreshold());
		n.updateNodeValues(oNode);
		n.setGroundingPamNode(oNode.getGroundingPamNode());
		n.setId(oNode.getId());	//sets extended id as well.		
		n.setDesirability(oNode.getDesirability());
		return n;
	}

	/**
	 * Creates new node of specified type. Uses strategies based on specified
	 * node type, or the default strategies if the node type has no strategies
	 * defined.
	 * 
	 * @param nodeType
	 *            type of desired node
	 * @return the node
	 */
	public Node getNode(String nodeType) {
		return getNode(nodeType, "Node");
	}

	/**
	 * Creates new node of specified type with specified label. Uses strategies
	 * based on specified node type, or the default strategies if the node type
	 * has no strategies defined.
	 * 
	 * @param nodeType
	 *            type of new node
	 * @param nodeLabel
	 *            label of new node
	 * @return the node
	 */
	public Node getNode(String nodeType, String nodeLabel) {
		LinkableDef nodeDef = nodeClasses.get(nodeType);
		if (nodeDef == null) {
			logger.log(Level.WARNING, "nodeType " + nodeType
					+ " does not exist.", TaskManager.getCurrentTick());
			return null;
		}
		String decayB = nodeDef.getDefaultStrategies().get(decayStrategyType);
		String exciteB = nodeDef.getDefaultStrategies().get(exciteStrategyType);
		if (decayB == null) {
			decayB = defaultDecayType;
		}
		if (exciteB == null) {
			exciteB = defaultExciteType;
		}

		Node n = getNode(nodeType, decayB, exciteB, nodeLabel, 
				Activatible.DEFAULT_ACTIVATION, Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
		return n;
	}

	/**
	 * Creates a new node of specified type, strategies, label, and initial
	 * activation.
	 * 
	 * @param nodeType
	 *            type of new node
	 * @param decayStrategy
	 *            decay strategy of new node
	 * @param exciteStrategy
	 *            excite strategy of new node
	 * @param nodeLabel
	 *            label of new node
	 * @param activation
	 *            activation of new node
	 * @param removableThreshold threshold node needs to remain in containing {@link NodeStructure}s
	 * @return the node
	 */
	public Node getNode(String nodeType, String decayStrategy, String exciteStrategy, 
					    String nodeLabel, double activation, double removableThreshold) {
		Node n = null;
		try {
			LinkableDef nodeDef = nodeClasses.get(nodeType);
			if (nodeDef == null) {
				logger.log(Level.WARNING, "NodeName " + nodeType
						+ " does not exist.", TaskManager.getCurrentTick());
				return null;
			}

			String className = nodeDef.getClassName();
			n = (Node) Class.forName(className).newInstance();

			n.setLabel(nodeLabel);
			n.setId(elementIdCount++);
			n.setActivation(activation);
			n.setActivatibleRemovalThreshold(removableThreshold);
			setActivatibleStrategies(n, decayStrategy, exciteStrategy);
			n.init(nodeDef.getParams());
			
		} catch (InstantiationException e) {
			logger.log(Level.WARNING, "Error creating Node.", TaskManager
					.getCurrentTick());
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Error creating Node.", TaskManager
					.getCurrentTick());
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Error creating Node.", TaskManager
					.getCurrentTick());
		}
		return n;
	}

	/*
	 * Assigns specified decay and excite strategies to supplied Activatible
	 * 
	 */
	private void setActivatibleStrategies(Activatible activatible, String decayStrategy, 
										  String exciteStrategy) {
		DecayStrategy decayB = getDecayStrategy(decayStrategy);
		activatible.setDecayStrategy(decayB);
		ExciteStrategy exciteB = getExciteStrategy(exciteStrategy);
		activatible.setExciteStrategy(exciteB);
	}

	/**
	 * Set the default Link type used by this factory.
	 * 
	 * @param defaultLinkType
	 *            type of links created by this factory
	 */
	public void setDefaultLinkType(String defaultLinkType) {
		if (linkClasses.containsKey(defaultLinkType)){
			this.defaultLinkType = defaultLinkType;
		}else{
			logger.log(Level.WARNING, "Factory does not contain link type, so it cannot be used as default.", TaskManager.getCurrentTick());
		}
	}

	/**
	 * Set the default Node type used by this factory.
	 * 
	 * @param defaultNodeType
	 *            type of nodes created by this factory
	 */
	public void setDefaultNodeType(String defaultNodeType) {
		if (nodeClasses.containsKey(defaultNodeType)){
			this.defaultNodeType = defaultNodeType;
		}else{
			logger.log(Level.WARNING, "Factory does not contain node type, so it cannot be used as default.", TaskManager.getCurrentTick());
		}
	}
	
	/**
	 * Gets default decay type.
	 * 
	 * @return the defaultDecayType
	 */
	public String getDefaultDecayType() {
		return defaultDecayType;
	}
	
	/**
	 * Returns the default {@link DecayStrategy}
	 * @return Factory's default {@link DecayStrategy} 
	 */
	public DecayStrategy getDefaultDecayStrategy(){
		return getDecayStrategy(defaultDecayType);
	}

	/**
	 * Sets default decay type.
	 * 
	 * @param defaultDecayType
	 *            DecayType to be used
	 */
	public void setDefaultDecayType(String defaultDecayType) {
		if (decayStrategies.containsKey(defaultDecayType)) {
			this.defaultDecayType = defaultDecayType;
		}else{
			logger.log(Level.WARNING, "Factory does not contain decay strategy, so it cannot be used as default.", TaskManager.getCurrentTick());
		}
	}

	/**
	 * Gets default excite type.
	 * 
	 * @return defaultExciteType ExciteType to be used
	 */
	public String getDefaultExciteType() {
		return defaultExciteType;
	}
	
	/**
	 * Returns the default {@link ExciteStrategy}
	 * @return Factory's default excite strategy
	 */
	public ExciteStrategy getDefaultExciteStrategy(){
		return getExciteStrategy(defaultExciteType);
	}

	/**
	 * Sets default excite type.
	 * 
	 * @param defaultExciteType
	 *            the defaultExciteType to set
	 */
	public void setDefaultExciteType(String defaultExciteType) {
		if (exciteStrategies.containsKey(defaultExciteType)){
			this.defaultExciteType = defaultExciteType;
		}else{
			logger.log(Level.WARNING, "Factory does not contain excite strategy, so it cannot be used as default.", TaskManager.getCurrentTick());
		}
	}
	
	/**
	 * Returns a new {@link Codelet} having specified attributes.  Codelet will have strategies
	 * specified for the codeletType
	 * @param codeletType type of codelet
	 * @param ticksPerStep execution frequency 
	 * @param activation initial activation
	 * @param removalThreshold activation needed to remain active
	 * @param params optional parameters to be set in object's init method
	 * @return the new Codelet
	 */
	public Codelet getCodelet(String codeletType, int ticksPerStep, double activation, double removalThreshold, Map<String,?extends Object> params){
		CodeletDef codeletDef = codelets.get(codeletType);		
		if (codeletDef == null) {
			logger.log(Level.WARNING, "Asked for codelet " + codeletType + 
					" but factory does not have such a codelet. Check factoriesData.xml", TaskManager.getCurrentTick());
			return null;
		}
		String decayB = codeletDef.getDefaultStrategies().get(decayStrategyType);
		String exciteB = codeletDef.getDefaultStrategies().get(exciteStrategyType);
		if(decayB == null){
			decayB=defaultDecayType;
		}
		if(exciteB == null){
			exciteB=defaultExciteType;
		}
	
		return getCodelet(codeletType,decayB,exciteB,ticksPerStep,activation,removalThreshold,params);
	}
	
	/**
	 * Returns a new codelet with specified attributes.
	 * 
	 * @param codeletName
	 *            label for codelet
	 * @param decayStrategy
	 *            DecayStrategy used by codelet
	 * @param exciteStrategy
	 *            ExciteStrategy used by codelet
	 * @param ticksPerStep
	 *            execution frequency
	 * @param activation
	 *            initial activation
	 * @param removalThreshold activation needed to remain active
	 * @param params
	 *            optional parameters to be set in object's init method
	 * @return new Codelet
	 */
	public Codelet getCodelet(String codeletName, String decayStrategy, String exciteStrategy, 
							  int ticksPerStep, double activation, double removalThreshold, Map<String, ? extends Object> params){
		Codelet codelet = null;
		try {
			CodeletDef codeletDef = codelets.get(codeletName);
			if (codeletDef == null) {
				logger.log(Level.WARNING, "CodeletName " + codeletName
						+ " does not exist.", TaskManager.getCurrentTick());
			}

			String className = codeletDef.getClassName();
			codelet = (Codelet) Class.forName(className).newInstance();

			codelet.setTicksPerStep(ticksPerStep);
			codelet.setActivation(activation);
			codelet.setActivatibleRemovalThreshold(removalThreshold);
			setActivatibleStrategies(codelet, decayStrategy, exciteStrategy);
			
			if (params != null){
				codelet.init(params);
			}else{ //Use default parameters from the factoriesData.xml file
				codelet.init(codeletDef.getParams());
			}
			
		} catch (InstantiationException e) {
			logger.log(Level.WARNING, "Error creating codelet " + e.toString(), TaskManager
					.getCurrentTick());
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Error creating codelet " + e.toString(), TaskManager
					.getCurrentTick());
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Error creating codelet " + e.toString(), TaskManager
					.getCurrentTick());
		}
		return codelet;
	}

	/**
	 * 
	 * Returns a new default NodeStructure.
	 * 
	 * @return a new NodeStructure with default {@link Node} type and default
	 *         {@link Link} type.
	 */
	public NodeStructure getNodeStructure() {
		return getNodeStructure(defaultNodeType, defaultLinkType);
	}

	/**
	 * Returns a new NodeStructure with specified {@link Node} and {@link Link} types.
	 * 
	 * @param nodeType
	 *            type of node in returned {@link NodeStructure}
	 * @param linkType
	 *            type of Link in returned {@link NodeStructure}
	 * @return a new NodeStructure with specified node type and specified link
	 *         type or null if types do not exist in this factory.
	 */
	public NodeStructure getNodeStructure(String nodeType, String linkType) {
		if (containsNodeType(nodeType)) {
			if (containsLinkType(linkType)) {
				return new NodeStructureImpl(nodeType, linkType);
			}
			logger.log(Level.WARNING, "Factory does not have linkType: "
					+ linkType, TaskManager.getCurrentTick());
		}
		logger.log(Level.WARNING,
				"Factory does not have nodeType: " + nodeType, TaskManager
						.getCurrentTick());
		return null;
	}
}
