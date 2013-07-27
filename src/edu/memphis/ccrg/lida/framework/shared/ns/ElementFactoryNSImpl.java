/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.shared.ns;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import edu.memphis.ccrg.lida.framework.factories.ElementFactory;
import edu.memphis.ccrg.lida.framework.initialization.LinkableDef;
import edu.memphis.ccrg.lida.framework.shared.CognitiveContent;
import edu.memphis.ccrg.lida.framework.shared.CognitiveContentStructure;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

/**
 * @author Sean Kugele
 *
 */
public class ElementFactoryNSImpl implements ElementFactory {

    /*
     * Used to assign unique IDs to nodes.
     */
    private static int nodeIdCount;

    /*
     * Used to assign unique IDs to schemes.
     */
    private static int behaviorIdCount;

    /*
     * Used to retrieve default link class from 'linkClasses' map. e.g.
     * edu.memphis.ccrg.lida.framework.shared.LinkImpl
     */
    private String defaultLinkClassName = LinkImpl.class.getCanonicalName();

    /*
     * Specifies default link type used by the factory. e.g. "LinkImpl"
     */
    private String defaultLinkType = LinkImpl.class.getSimpleName();

    /*
     * Used to retrieve default node class from 'nodeClasses' map. e.g.
     * edu.memphis.ccrg.lida.framework.shared.NodeImpl
     */
    private String defaultNodeClassName = NodeImpl.class.getCanonicalName();

    /*
     * Specifies default node type used by the factory. e.g. "NodeImpl"
     */
    private String defaultNodeType = NodeImpl.class.getSimpleName();
    
    /*
     * Map of LinkableDefs for the Link types available to this factory indexed
     * by their linkFactoryName.
     */
    private Map<String, LinkableDef> linkClasses = new HashMap<String, LinkableDef>();

    /*
     * Map of LinkableDefs for the Node types available to this factory indexed
     * by their nodeFactoryName.
     */
    private Map<String, LinkableDef> nodeClasses = new HashMap<String, LinkableDef>();
    
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
     * @param removalThreshold
     *            threshold to remain in {@link NodeStructure}
     * @return new Link
     */
    public Link getLink(Node source, Linkable sink, LinkCategory category, double activation,
            double removalThreshold) {
        return getLink(defaultLinkType, source, sink, category, defaultDecayType,
                defaultExciteType, activation, removalThreshold);
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
        return getLink(defaultLinkType, source, sink, category, defaultDecayType,
                defaultExciteType, Activatible.DEFAULT_ACTIVATION,
                Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
    }

    /**
     * Checks if desiredType is-a requiredType. Creates and returns a Link of
     * desiredType with specified parameters.
     * 
     * @param requiredType
     *            Required Link type for {@link NodeStructure}
     * @param desiredType
     *            Desired Link type for returned Link. Must be a subtype of
     *            required type.
     * @param source
     *            Link's source
     * @param sink
     *            Link's sink
     * @param category
     *            Link's {@link LinkCategory}
     * @return new {@link Link} with specified attributes.
     */
    public Link getLink(String requiredType, String desiredType, Node source, Linkable sink,
            LinkCategory category) {
        LinkableDef requiredDef = linkClasses.get(requiredType);
        if (requiredDef == null) {
            logger.log(Level.WARNING, "Factory does not contain link type: {1}", new Object[] {
                    TaskManager.getCurrentTick(), requiredType });
            return null;
        }
        LinkableDef desiredDef = linkClasses.get(desiredType);
        if (desiredDef == null) {
            logger.log(Level.WARNING, "Factory does not contain link type: {1}", new Object[] {
                    TaskManager.getCurrentTick(), desiredType });
            return null;
        }

        Link l = null;
        try {
            Class<?> required = Class.forName(requiredDef.getClassName());
            Class<?> desired = Class.forName(desiredDef.getClassName());

            if (required != null && required.isAssignableFrom(desired)) {
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
     * @param linkType
     *            the link type
     * @param source
     *            Node that is link's source
     * @param sink
     *            Linkable that is link's sink
     * @param category
     *            LinkCategory
     * @return new Link
     */
    public Link getLink(String linkType, Node source, Linkable sink, LinkCategory category) {
        LinkableDef linkDef = linkClasses.get(linkType);
        if (linkDef == null) {
            logger.log(Level.WARNING, "Link type {1} does not exist.", new Object[] {
                    TaskManager.getCurrentTick(), linkType });
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

        return getLink(linkType, source, sink, category, decayB, exciteB,
                Activatible.DEFAULT_ACTIVATION,
                Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
    }

    /**
     * Creates and returns a new Link of specified type with specified source,
     * sink, LinkCategory, DecayStrategy, ExciteStrategy, and category.
     * 
     * @param linkType
     *            Link type
     * @param source
     *            Link's source
     * @param sink
     *            Link's sink
     * @param category
     *            Link's category
     * @param decayStrategy
     *            Link's {@link DecayStrategy}
     * @param exciteStrategy
     *            Link's {@link ExciteStrategy}
     * @param activation
     *            initial activation
     * @param removalThreshold
     *            threshold of activation required to remain active
     * @return new Link
     */
    public Link getLink(String linkType, Node source, Linkable sink, LinkCategory category,
            String decayStrategy, String exciteStrategy, double activation,
            double removalThreshold) {

        if (source == null) {
            logger.log(Level.WARNING, "Cannot create a link with a null source.",
                    TaskManager.getCurrentTick());
            return null;
        }
        if (sink == null) {
            logger.log(Level.WARNING, "Cannot create a link with a null sink.",
                    TaskManager.getCurrentTick());
            return null;
        }
        if (category == null) {
            logger.log(Level.WARNING, "Cannot create a link with a null category.",
                    TaskManager.getCurrentTick());
            return null;
        }

        Link link = null;
        try {
            LinkableDef linkDef = linkClasses.get(linkType);
            if (linkDef == null) {
                logger.log(Level.WARNING, "Link type {1} does not exist.", new Object[] {
                        TaskManager.getCurrentTick(), linkType });
                return null;
            }

            String className = linkDef.getClassName();
            link = (Link) Class.forName(className).newInstance();
            link.setFactoryType(linkType);
            link.setSource(source);
            link.setSink(sink);
            link.setCategory(category);
            link.setActivation(activation);
            link.setActivatibleRemovalThreshold(removalThreshold);
            setActivatibleStrategies(link, decayStrategy, exciteStrategy);
            link.init(linkDef.getParams());

        } catch (InstantiationException e) {
            logger.log(Level.WARNING, "InstantiationException creating Link.",
                    TaskManager.getCurrentTick());
        } catch (IllegalAccessException e) {
            logger.log(Level.WARNING, "IllegalAccessException creating Link.",
                    TaskManager.getCurrentTick());
        } catch (ClassNotFoundException e) {
            logger.log(Level.WARNING, "ClassNotFoundException creating Link.",
                    TaskManager.getCurrentTick());
        }
        return link;
    }

    /**
     * Creates a default node with the default strategies and default
     * activation.
     * 
     * @see Activatible
     * 
     * @return the node
     */
    public Node getNode() {
        return getNode(defaultNodeType, defaultDecayType, defaultExciteType, "Node",
                Activatible.DEFAULT_ACTIVATION,
                Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
    }

    /**
     * Creates a copy of the supplied node with the default strategies. Note
     * that the new node is of a default type regardless of the node passed in
     * the parameter.
     * 
     * @param oNode
     *            supplied node
     * @return the node
     */
    public Node getNode(Node oNode) {
        return getNode(oNode, defaultNodeType, defaultDecayType, defaultExciteType);
    }

    /**
     * Creates a copy of specified {@link Node}. The second argument specifies
     * the type of the new node. The {@link Activatible} strategies of the new
     * node are based on those specified by the {@link Node} type's
     * {@link LinkableDef} (specified by factoriesData.xml) If the {@link Node}
     * type does not specify default {@link Activatible} strategies then the
     * default strategies are used. All other values of the specified
     * {@link Node} are copied to the new {@link Node}, e.g. activation.
     * 
     * @param oNode
     *            supplied node
     * @param nodeType
     *            type of returned node
     * @return the node
     */
    public Node getNode(Node oNode, String nodeType) {
        if (oNode == null) {
            logger.log(Level.WARNING, "Supplied node is null", TaskManager.getCurrentTick());
            return null;
        }
        LinkableDef nodeDef = nodeClasses.get(nodeType);
        if (nodeDef == null) {
            logger.log(Level.WARNING, "Node type {1} does not exist.", new Object[] {
                    TaskManager.getCurrentTick(), nodeType });
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
     * Creates new node of specified type with specified label. Uses strategies
     * based on specified node type, or the default strategies if the node type
     * has no strategies defined.
     * 
     * @param type
     *            type of new node
     * @param label
     *            label of new node
     * @return the node
     */
    public Node getNode(String type, String label) {
        LinkableDef nodeDef = nodeClasses.get(type);
        if (nodeDef == null) {
            logger.log(Level.WARNING, "Node type {1} does not exist.", new Object[] {
                    TaskManager.getCurrentTick(), type });
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

        Node n = getNode(type, decayB, exciteB, label, Activatible.DEFAULT_ACTIVATION,
                Activatible.DEFAULT_ACTIVATIBLE_REMOVAL_THRESHOLD);
        return n;
    }

    /**
     * Creates a copy of specified node of desired type. Desired type must pass
     * is-a test with requireType.
     * 
     * @param requiredType
     *            Default node type of {@link NodeStructure}
     * @param oNode
     *            {@link Node} to be copied.
     * @param desiredType
     *            type of copied node
     * @return copy of oNode of desired type, or a new node of desired type, or
     *         null
     */
    public Node getNode(String requiredType, Node oNode, String desiredType) {
        LinkableDef requiredDef = nodeClasses.get(requiredType);
        if (requiredDef == null) {
            logger.log(Level.WARNING, "Factory does not contain node type: {1}", new Object[] {
                    TaskManager.getCurrentTick(), requiredType });
            return null;
        }
        LinkableDef desiredDef = nodeClasses.get(desiredType);
        if (desiredDef == null) {
            logger.log(Level.WARNING, "Factory does not contain node type: {1}", new Object[] {
                    TaskManager.getCurrentTick(), desiredType });
            return null;
        }

        Node newNode = null;
        try {
            Class<?> required = Class.forName(requiredDef.getClassName());
            Class<?> desired = Class.forName(desiredDef.getClassName());

            if (required != null && required.isAssignableFrom(desired)) {
                if (oNode == null) {// Get a new Node from scratch
                    newNode = getNode(desiredType, "Node");
                } else { // Get a new Node based on oNode
                    newNode = getNode(oNode, desiredType);
                }
            }
        } catch (ClassNotFoundException exc) {
            logger.log(Level.SEVERE, "Cannot find Class type.", TaskManager.getCurrentTick());
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
     * Creates a copy of oNode with specified node type. Copy will have Decay
     * and Excite as specified in this method's parameters, not according to the
     * default for the Node type.
     * 
     * @param oNode supplied node
     * 
     * @param nodeType type for new node
     * 
     * @param decayStrategy decayStrategy new node's decay strategy
     * 
     * @param exciteStrategy exciteStrategy new node's excite strategy
     * 
     * @return the node
     */
    private Node getNode(Node oNode, String nodeType, String decayStrategy,
            String exciteStrategy) {
        if (oNode == null) {
            logger.log(Level.WARNING, "Specified node is null", TaskManager.getCurrentTick());
            return null;
        }
        Node n = getNode(nodeType, decayStrategy, exciteStrategy, oNode.getLabel(),
                oNode.getActivation(), oNode.getActivatibleRemovalThreshold());
        n.setGroundingPamNode(oNode.getGroundingPamNode());
        n.setId(oNode.getId()); // sets extended id as well.
        n.updateNodeValues(oNode);
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
     * @param removalThreshold
     *            threshold node needs to remain in containing
     *            {@link NodeStructure}s
     * @return the node
     */
    public Node getNode(String nodeType, String decayStrategy, String exciteStrategy,
            String nodeLabel, double activation, double removalThreshold) {
        Node n = null;
        try {
            LinkableDef nodeDef = nodeClasses.get(nodeType);
            if (nodeDef == null) {
                logger.log(Level.WARNING, "Node type {1} does not exist.", new Object[] {
                        TaskManager.getCurrentTick(), nodeType });
                return null;
            }

            String className = nodeDef.getClassName();
            n = (Node) Class.forName(className).newInstance();

            n.setId(nodeIdCount++);
            n.setFactoryType(nodeType);
            n.setLabel(nodeLabel);
            n.setActivation(activation);
            n.setActivatibleRemovalThreshold(removalThreshold);
            setActivatibleStrategies(n, decayStrategy, exciteStrategy);
            n.init(nodeDef.getParams());
        } catch (InstantiationException e) {
            logger.log(Level.WARNING, "InstantiationException creating Node.",
                    TaskManager.getCurrentTick());
        } catch (IllegalAccessException e) {
            logger.log(Level.WARNING, "IllegalAccessException creating Node.",
                    TaskManager.getCurrentTick());
        } catch (ClassNotFoundException e) {
            logger.log(Level.WARNING, "ClassNotFoundException creating Node.",
                    TaskManager.getCurrentTick());
        }
        return n;
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
        linkClasses.put(typeName, new LinkableDef(className, new HashMap<String, String>(),
                typeName, new HashMap<String, Object>()));
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
        nodeClasses.put(typeName, new LinkableDef(className, new HashMap<String, String>(),
                typeName, new HashMap<String, Object>()));
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
     * Returns whether this factory contains specified {@link Node} type.
     * 
     * @param nodeTypeName
     *            name of node type
     * @return true if factory contains type or false if not
     */
    public boolean containsNodeType(String nodeTypeName) {
        return nodeClasses.containsKey(nodeTypeName);
    }

    /**
     * Returns whether this factory contains specified {@link Link} type.
     * 
     * @param linkTypeName
     *            name of Link type
     * @return true if factory contains type or false if not
     */
    public boolean containsLinkType(String linkTypeName) {
        return linkClasses.containsKey(linkTypeName);
    }
 
    
    
    
    
    
    
    
    
    
    
    @Override
    public CognitiveContent getCognitiveContent(String type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CognitiveContent getCognitiveContent(String type,
            Map<String, ? extends Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CognitiveContentStructure getCognitiveContentStructure(String type) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CognitiveContentStructure getCognitiveContentStructure(String type,
            Map<String, ? extends Object> params) {
        // TODO Auto-generated method stub
        return null;
    }

}
