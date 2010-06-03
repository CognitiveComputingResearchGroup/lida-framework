package edu.memphis.ccrg.lida.framework.shared;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.framework.initialization.CodeletDef;
import edu.memphis.ccrg.lida.framework.initialization.LinkableDef;
import edu.memphis.ccrg.lida.framework.initialization.StrategyDef;
import edu.memphis.ccrg.lida.framework.strategies.DecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.ExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * Standard factory for the framework
 * @author Javier Snaider
 */
public class NodeFactory {
	
	/**
	 * Instance of this class
	 */
	private static NodeFactory instance = new NodeFactory();
	
	/**
	 * Logger 
	 */
	private static Logger logger = Logger.getLogger("lida.framework.shared.NodeFactory");

	/**
	 * Used to assign unique IDs to nodes. 
	 */
	private static long nodeIdCount = 0;
	
	/**
	 * TODO: document
	 * ?????????????????????????????????
	 */
	private static long actionIdCount = 0;

	/**
	 * This static method returns the instance of the factory. Implements the
	 * Singleton pattern.
	 * 
	 * @return
	 */
	public static NodeFactory getInstance() {
		return instance;
	}

	/**
	 * Used to retrieve default decay strategy from 'decayStrategies' map.
	 */
	private String defaultDecayType;
	
	/**
	 * Used to retrieve default excite strategy from 'exciteStrategies' map.  
	 */
	private String defaultExciteType;
	
	/**
	 * Used to retrieve default link class from 'linkClasses' map.
	 * e.g. edu.memphis.ccrg.lida.framework.shared.LinkImpl
	 */
	private String defaultLinkClassName;
	
	/**
	 * Specifies default link type used by the factory.
	 * e.g. "LinkImpl"
	 */
	private String defaultLinkType;
	
	/**
	 * Used to retrieve default node class from 'nodeClasses' map.
	 * e.g. edu.memphis.ccrg.lida.framework.shared.NodeImpl
	 */
	private String defaultNodeClassName;
	
	/**
	 * Specifies default node type used by the factory.
	 * e.g. "NodeImpl"
	 */
	private String defaultNodeType;
	
	/**
	 * Map of excite behaviors available to this factory
	 */
	private Map<String, StrategyDef> exciteStrategies = new HashMap<String, StrategyDef>();
	/**
	 * Map of excite behaviors available to this factory
	 */
	private Map<String, StrategyDef> decayStrategies = new HashMap<String, StrategyDef>();
	private Map<String, StrategyDef> strategies = new HashMap<String, StrategyDef>();

	private Map<String, LinkableDef> linkClasses = new HashMap<String, LinkableDef>();
	private Map<String, LinkableDef> nodeClasses = new HashMap<String, LinkableDef>();
	private Map<String, CodeletDef> codelets = new HashMap<String, CodeletDef>();

	private Map<Long, LidaAction> actionsCache = new HashMap<Long, LidaAction>();

	/**
	 * @param nextId the next nodeId
	 */
	public static void setNextNodeId(long nextId) {
		NodeFactory.nodeIdCount = nextId;
	}

	/**
	 * @param nextId the next actionId
	 */
	public static void setNextActionId(long nextId) {
		NodeFactory.actionIdCount = nextId;
	}

	private NodeFactory() {
		defaultNodeClassName = "edu.memphis.ccrg.lida.framework.shared.NodeImpl";
		defaultLinkClassName = "edu.memphis.ccrg.lida.framework.shared.LinkImpl";
		defaultNodeType = "NodeImpl";
		defaultLinkType = "LinkImpl";
		defaultDecayType="defaultDecay";
		defaultExciteType="defaultExcite";

		nodeClasses.put(defaultNodeType, new LinkableDef(defaultNodeClassName,
				new HashMap<String, String>(), defaultNodeType,
				new HashMap<String, Object>()));
		linkClasses.put(defaultLinkType, new LinkableDef(defaultLinkClassName,
				new HashMap<String, String>(), defaultLinkType,
				new HashMap<String, Object>()));
		nodeClasses.put("PamNodeImpl", new LinkableDef(
				"edu.memphis.ccrg.lida.pam.PamNodeImpl",
				new HashMap<String, String>(), "PamNodeImpl",
				new HashMap<String, Object>()));

		addDecayStrategy(defaultDecayType, new StrategyDef("edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy",
				defaultDecayType,new HashMap<String, Object>(),"decay",true));
		
		addExciteStrategy(defaultExciteType, new StrategyDef("edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy",
				defaultExciteType,new HashMap<String, Object>(),"excite",true));
	}

	public void addDecayStrategy(String name, StrategyDef decay) {
		decayStrategies.put(name, decay);
		strategies.put(name, decay);
	}

	public void addExciteStrategy(String name, StrategyDef excite) {
		exciteStrategies.put(name, excite);
		strategies.put(name, excite);
	}

	public void addStrategy(String name, StrategyDef strategyDef) {
		strategies.put(name, strategyDef);
	}

	public void addLinkType(LinkableDef linkDef) {
		linkClasses.put(linkDef.getName(), linkDef);
	}

	public void addLinkType(String linkType, String className) {
		linkClasses.put(linkType, new LinkableDef(className,
				new HashMap<String, String>(), linkType,
				new HashMap<String, Object>()));
	}

	public void addNodeType(LinkableDef nodeDef) {
		nodeClasses.put(nodeDef.getName(), nodeDef);
	}

	public void addCodeletType(CodeletDef codeletDef) {
		codelets.put(codeletDef.getName(), codeletDef);
	}

	public void addNodeType(String nodeType, String className) {
		nodeClasses.put(nodeType, new LinkableDef(className,
				new HashMap<String, String>(), nodeType,
				new HashMap<String, Object>()));
	}

	/**
	 * @param name
	 * @return
	 */
	public DecayStrategy getDecayStrategy(String name) {
		DecayStrategy d = null;
		StrategyDef sd = decayStrategies.get(name);
		if (sd == null) {
			sd = decayStrategies.get(defaultDecayType);
			logger.log(Level.WARNING, "Strategy " + defaultExciteType
					+ " does not exist. Default used instead.", LidaTaskManager.getActualTick());
		}
		d = (DecayStrategy) sd.getInstance();
		return d;
	}

	public String getDefaultLinkType() {
		return defaultLinkType;
	}

	public String getDefaultNodeType() {
		return defaultNodeType;
	}

	public ExciteStrategy getExciteStrategy(String name) {
		ExciteStrategy d = null;
		StrategyDef sd = exciteStrategies.get(name);
		if (sd == null) {
			sd = exciteStrategies.get(defaultExciteType);
			logger.log(Level.WARNING, "Strategy " + name
					+ " does not exist. Default used instead.", LidaTaskManager.getActualTick());
		}
		d = (ExciteStrategy) sd.getInstance();
		return d;
	}
	
	public Strategy getStrategy(String name) {
		Strategy d = null;
		StrategyDef sd = strategies.get(name);
		if (sd != null) {
			d = sd.getInstance();
		}
		return d;
	}

	public Link getLink(Link oLink) {
		return getLink(defaultLinkType,oLink.getSource(),oLink.getSink(),oLink.getType(),defaultDecayType,defaultExciteType,oLink.getActivation());
	}

	public Link getLink(Link oLink, String linkT) {
		LinkableDef linkDef = linkClasses.get(linkT);		
		if (linkDef == null) {
			logger.log(Level.WARNING, "LinkName " + linkT
					+ " does not exist.", LidaTaskManager.getActualTick());
			return null;
		}
		String decayB = linkDef.getDefeaultStrategies().get("decay");
		String exciteB = linkDef.getDefeaultStrategies().get("excite");
		if(decayB == null){
			decayB=defaultDecayType;
		}
		if(exciteB == null){
			exciteB=defaultExciteType;
		}
		
		return getLink(linkT,oLink.getSource(),oLink.getSink(),oLink.getType(),decayB,exciteB,oLink.getActivation());
	}

	public Link getLink(Linkable source, Linkable sink, LinkType type,double activation) {
		return getLink(defaultLinkType,source,sink,type,defaultDecayType,defaultExciteType,activation);
	}
	public Link getLink(Linkable source, Linkable sink, LinkType type) {
		return getLink(defaultLinkType,source,sink,type,defaultDecayType,defaultExciteType,0.0);
	}

	public Link getLink(String linkT, Linkable source, Linkable sink, LinkType type){
		LinkableDef linkDef = linkClasses.get(linkT);		
		if (linkDef == null) {
			logger.log(Level.WARNING, "LinkName " + linkT
					+ " does not exist.", LidaTaskManager.getActualTick());
			return null;
		}
		String decayB = linkDef.getDefeaultStrategies().get("decay");
		String exciteB = linkDef.getDefeaultStrategies().get("excite");
		if(decayB == null){
			decayB=defaultDecayType;
		}
		if(exciteB == null){
			exciteB=defaultExciteType;
		}
		
		return getLink(linkT,source,sink,type,decayB,exciteB,0.0);		
	}
		public Link getLink(String linkT, Linkable source, Linkable sink, LinkType type,String decayStrategy,
				String exciteStrategy,double activation) {
		Link l = null;
		try {
			LinkableDef linkDef = linkClasses.get(linkT);
			if (linkDef == null) {
				logger.log(Level.WARNING, "LinkName " + linkT
						+ " does not exist.", LidaTaskManager.getActualTick());
				return null;
			}

			String className = linkDef.getClassName();
			l = (Link) Class.forName(className).newInstance();
			l.setSource(source);
			l.setSink(sink);
			l.setType(type);
			l.setActivation(activation);

//			String decayB = linkDef.getDefeaultStrategies().get("decay");
//			String exciteB = linkDef.getDefeaultStrategies().get("excite");
			
			setActivatibleStrategies(decayStrategy, exciteStrategy, l);	

			l.init(linkDef.getParams());
			
		} catch (InstantiationException e) {
			logger.log(Level.WARNING, "Error creating Link.", LidaTaskManager
					.getActualTick());
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Error creating Link.", LidaTaskManager
					.getActualTick());
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Error creating Link.", LidaTaskManager
					.getActualTick());
		}
		return l;
	}

	/**
	 * Creates a default node with the default behaviors and 0 activation
	 * @return
	 */
	public Node getNode() {
		return getNode(defaultNodeType,defaultDecayType, defaultExciteType, "",0.0);
	}

	/**
	 * Creates a copy of the supplied node with the default behaviors.  
	 * Note that the new node is of a default type regardless
	 * of the node passed in the parameter.
	 * @param oNode
	 * @return
	 */
	public Node getNode(Node oNode) {
		return getNode(oNode, defaultNodeType,defaultDecayType, defaultExciteType);
	}

	/**
	 * Creates a copy of the supplied node with the default behaviors.  
	 * The type of the new node is based on the argument. Note that the behaviors of the new node
	 * are based on those node passed in the argument.  if the node type does not have default behaviors
	 * then the default behaviors are used.
	 * @param oNode
	 * @param nodeType
	 * @return
	 * 
	 */
	public Node getNode(Node oNode, String nodeType) {
		LinkableDef nodeDef = nodeClasses.get(nodeType);		
		if (nodeDef == null) {
			logger.log(Level.WARNING, "nodeType " + nodeType
					+ " does not exist.", LidaTaskManager.getActualTick());
			return null;
		}
		String decayB = nodeDef.getDefeaultStrategies().get("decay");
		String exciteB = nodeDef.getDefeaultStrategies().get("excite");
		if(decayB == null){
			decayB=defaultDecayType;
		}
		if(exciteB == null){
			exciteB=defaultExciteType;
		}
		return getNode(oNode, nodeType, decayB, exciteB);
	}

	/**
	 * Creates a copy of oNode with the specified decay and excite strategies.  The type of the new node
	 * will be the default node type.
	 * @param oNode
	 * @param decayStrategy
	 * @param exciteStrategy
	 * @return
	 */
	public Node getNode(Node oNode, String decayStrategy, String exciteStrategy) {
		return getNode(oNode, defaultNodeType, decayStrategy, exciteStrategy);
	}

	/**
	 * Creates a copy of oNode with specified node type, decay and excite strategies.
	 * TODO: use strategies not behaviors
	 * 
	 * @param oNode
	 * @param nodeType
	 * @param decayStrategy
	 * @param exciteStrategy
	 * @return
	 */
	public Node getNode(Node oNode, String nodeType, String decayStrategy,
			String exciteStrategy) {
		Node n = getNode(nodeType,  decayStrategy, exciteStrategy, oNode.getLabel(),oNode.getActivation());
		n.setReferencedNode(oNode.getReferencedNode());
		n.setId(oNode.getId());

		return n;
	}

	/**
	 * Creates new node of specified type. Uses strategies based on specified node type, or the default strategies
	 * if the node type has no strategies defined.
	 * @param nodeType
	 * @return
	 */
	public Node getNode(String nodeType) {
		return getNode(nodeType, "");
	}

	/**
	 * Creates new node of specified type with specified label.  Uses strategies based on specified node type, or the default strategies
	 * if the node type has no strategies defined. 
	 * @param nodeType
	 * @return
	 */
	public Node getNode(String nodeType, String nodeLabel) {

		LinkableDef nodeDef = nodeClasses.get(nodeType);		
		if (nodeDef == null) {
			logger.log(Level.WARNING, "nodeType " + nodeType
					+ " does not exist.", LidaTaskManager.getActualTick());
			return null;
		}
		String decayB = nodeDef.getDefeaultStrategies().get("decay");
		String exciteB = nodeDef.getDefeaultStrategies().get("excite");
		if(decayB == null){
			decayB=defaultDecayType;
		}
		if(exciteB == null){
			exciteB=defaultExciteType;
		}

		Node n = getNode(nodeType, decayB, exciteB, nodeLabel,0.0);
		return n;
	}

	/**
	 * Creates a new node of specified type, strategies, label, and initial activation.
	 * 
	 * @param nodeType 
	 * @param decayStrategy
	 * @param exciteStrategy
	 * @param nodeLabel
	 * @param activation
	 * @return
	 */
	public Node getNode(String nodeType, String decayStrategy,
			String exciteStrategy, String nodeLabel, double activation) {
		Node n = null;
		try {
			LinkableDef nodeDef = nodeClasses.get(nodeType);
			if (nodeDef == null) {
				logger.log(Level.WARNING, "NodeName " + nodeType
						+ " does not exist.", LidaTaskManager.getActualTick());
				return null;
			}

			String className = nodeDef.getClassName();
			n = (Node) Class.forName(className).newInstance();

			n.setLabel(nodeLabel);
			n.setId(nodeIdCount++);
			n.setActivation(activation);
			
			setActivatibleStrategies(decayStrategy, exciteStrategy, n);	
			n.init(nodeDef.getParams());
			
		} catch (InstantiationException e) {
			logger.log(Level.WARNING, "Error creating Node.", LidaTaskManager
					.getActualTick());
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Error creating Node.", LidaTaskManager
					.getActualTick());
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Error creating Node.", LidaTaskManager
					.getActualTick());
		}
		return n;
	}

	/**
	 * @param decayStrategy
	 * @param exciteStrategy
	 * @param activatible
	 */
	private void setActivatibleStrategies(String decayStrategy,
			String exciteStrategy, Activatible activatible) {
		DecayStrategy decayB = getDecayStrategy(decayStrategy);
		activatible.setDecayStrategy(decayB);
		ExciteStrategy exciteB = getExciteStrategy(exciteStrategy);
		activatible.setExciteStrategy(exciteB);
	}

	/**
	 * @param defaultLinkType
	 *            the defaultLinkType to set
	 */
	public void setDefaultLinkType(String defaultLinkType) {
		if (linkClasses.containsKey(defaultLinkType)){
			this.defaultLinkType = defaultLinkType;
		}
	}
		
	/**
	 * @param defaultNodeType
	 *            the defaultNodeType to set
	 */
	public void setDefaultNodeType(String defaultNodeType) {
		if (nodeClasses.containsKey(defaultNodeType)){
			this.defaultNodeType = defaultNodeType;
		}
	}

	/**
	 * @return the defaultDecayType
	 */
	public String getDefaultDecayType() {
		return defaultDecayType;
	}

	/**
	 * @param defaultDecayType the defaultDecayType to set
	 */
	public void setDefaultDecayType(String defaultDecayType) {
		if(decayStrategies.containsKey(defaultDecayType)){
			this.defaultDecayType = defaultDecayType;
		}
	}

	/**
	 * @return the defaultExciteType
	 */
	public String getDefaultExciteType() {
		return defaultExciteType;
	}

	/**
	 * @param defaultExciteType the defaultExciteType to set
	 */
	public void setDefaultExciteType(String defaultExciteType) {
		if (exciteStrategies.containsKey(defaultExciteType)){
			this.defaultExciteType = defaultExciteType;
		}
	}
	
	public Codelet getCodelet(String codeletName, String decayStrategy, String exciteStrategy, 
							  int ticksPerStep, double activation, Map<String,?extends Object> params){
		Codelet cod = null;
		try {
			CodeletDef codeletDef = codelets.get(codeletName);
			if (codeletDef == null) {
				logger.log(Level.WARNING, "CodeletName " + codeletName
						+ " does not exist.", LidaTaskManager.getActualTick());
			}

			String className = codeletDef.getClassName();
			cod = (Codelet) Class.forName(className).newInstance();

			cod.setNumberOfTicksPerStep(ticksPerStep);
			cod.setActivation(activation);
			setActivatibleStrategies(decayStrategy, exciteStrategy, cod);
			
			if (params != null){
				cod.init(params);
			}else{//Use the default parameters in the factoriesData.xml file
				cod.init(codeletDef.getParams());
			}
			
			
		} catch (InstantiationException e) {
			logger.log(Level.WARNING, "Error creating codelet.", LidaTaskManager
					.getActualTick());
		} catch (IllegalAccessException e) {
			logger.log(Level.WARNING, "Error creating codelet.", LidaTaskManager
					.getActualTick());
		} catch (ClassNotFoundException e) {
			logger.log(Level.WARNING, "Error creating codelet.", LidaTaskManager
					.getActualTick());
		}
		return cod;
	}
	public Codelet getCodelet(String codeletName, int ticksPerStep, double activation, Map<String,?extends Object> params){
		CodeletDef codeletDef = codelets.get(codeletName);
		if (codeletDef == null) {
			logger.log(Level.WARNING, "CodeletName " + codeletName
					+ " does not exist.", LidaTaskManager.getActualTick());
			return null;
		}
		String decayB = codeletDef.getDefeaultStrategies().get("decay");
		String exciteB = codeletDef.getDefeaultStrategies().get("excite");
		if(decayB == null){
			decayB=defaultDecayType;
		}
		if(exciteB == null){
			exciteB=defaultExciteType;
		}
	
		return getCodelet(codeletName,decayB,exciteB,ticksPerStep,activation,params);
	}
	
	public void addAction(LidaAction action){
		actionsCache.put(action.getId(),action);
	}
	
	public LidaAction getAction(long id){
		return actionsCache.get(id);
	}
	
}// class