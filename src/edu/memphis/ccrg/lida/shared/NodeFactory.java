package edu.memphis.ccrg.lida.shared;

import java.util.HashMap;
import java.util.Map;
import edu.memphis.ccrg.lida.shared.strategies.BasicExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.DecayBehavior;
import edu.memphis.ccrg.lida.shared.strategies.ExciteBehavior;
import edu.memphis.ccrg.lida.shared.strategies.LinearDecayCurve;

/**
 * @author Javier Snaider
 * 
 */
public class NodeFactory {

	private static NodeFactory instance;

	/**
	 * This static method returns the instance of the factory. 
	 * Implements the Singleton pattern.
	 * @return
	 */
	public static NodeFactory getInstance() {
		if (instance == null) {
			instance = new NodeFactory();
		}
		return instance;
	}

	private String DefaultNodeClassName;
	private String DefaultLinkClassName;
	/**
	 * @param defaultNodeType the defaultNodeType to set
	 */
	public void setDefaultNodeType(String defaultNodeType) {
		DefaultNodeType = defaultNodeType;
	}

	/**
	 * @param defaultLinkType the defaultLinkType to set
	 */
	public void setDefaultLinkType(String defaultLinkType) {
		DefaultLinkType = defaultLinkType;
	}

	private String DefaultNodeType;
	private String DefaultLinkType;
	//
	private DecayBehavior defaultDecay;
	private ExciteBehavior defaultExcite;
	//
	private Map<String, String> nodeClass = new HashMap<String, String>();
	private Map<String, String> linkClass = new HashMap<String, String>();

	private Map<String, DecayBehavior> decays = new HashMap<String, DecayBehavior>();
	private Map<String, ExciteBehavior> excites = new HashMap<String, ExciteBehavior>();

	private NodeFactory() {
		DefaultNodeClassName = "edu.memphis.ccrg.lida.shared.NodeImpl";
		DefaultLinkClassName = "edu.memphis.ccrg.lida.shared.LinkImpl";
		DefaultNodeType = "NodeImpl";
		DefaultLinkType = "LinkImpl";
		defaultDecay = new LinearDecayCurve();
		defaultExcite = new BasicExciteBehavior();
		nodeClass.put(DefaultNodeType, DefaultNodeClassName);
		nodeClass.put(DefaultLinkType, DefaultLinkClassName);
		nodeClass.put("PamNodeImpl", "edu.memphis.ccrg.lida.perception.PamNodeImpl");
	}

	public void addDecayBehavior(String name, DecayBehavior decay) {
		decays.put(name, decay);
	}

	public void addExciteBehavior(String name, ExciteBehavior excite) {
		excites.put(name, excite);
	}

	public void addLinkType(String linkType, String className) {
		linkClass.put(linkType, className);
	}

	public void addNodeType(String nodeType, String className) {
		nodeClass.put(nodeType, className);
	}

	/**
	 * @param name
	 * @return
	 */
	public DecayBehavior getDecayBehavior(String name) {
		DecayBehavior d = decays.get(name);
		if (d == null) {
			d = defaultDecay;
		}
		return d;
	}

	public ExciteBehavior getExciteBehavior(String name) {
		ExciteBehavior d = excites.get(name);
		if (d == null) {
			d = defaultExcite;
		}
		return d;
	}

	public Link getLink(Link oLink) {
		Link l = null;
		try {
			l = (Link) Class.forName(DefaultLinkClassName).newInstance();
			l.setSource(oLink.getSource());
			l.setSink(oLink.getSink());
			l.setType(oLink.getType());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return l;
	}

	public Link getLink(Link oLink, String linkT) {
		Link l = null;
		try {
			l = (Link) Class.forName(linkClass.get(linkT)).newInstance();
			l.setSource(oLink.getSource());
			l.setSink(oLink.getSink());
			l.setType(oLink.getType());
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return l;
	}

	public Link getLink(Linkable source, Linkable sink, LinkType type) {
		Link l = null;
		try {
			l = (Link) Class.forName(DefaultLinkClassName).newInstance();
			l.setSource(source);
			l.setSink(sink);
			l.setType(type);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return l;
	}

	public Link getLink(String linkT, Linkable source, Linkable sink,
			LinkType type) {
		Link l = null;
		try {
			l = (Link) Class.forName(linkClass.get(linkT)).newInstance();
			l.setSource(source);
			l.setSink(sink);
			l.setType(type);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return l;
	}

	public Node getNode() {
		Node n = null;
		try {
			n = (Node) Class.forName(DefaultNodeClassName).newInstance();
			n.setExciteBehavior(defaultExcite);
			n.setDecayBehavior(defaultDecay);

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return n;
	}

	public Node getNode(Node oNode) {
		Node n = null;
		try {
			n = (Node) Class.forName(DefaultNodeClassName).newInstance();
			n.setActivation(oNode.getActivation());
			n.setReferencedNode(oNode.getReferencedNode());
			n.setExciteBehavior(oNode.getExciteBehavior());
			n.setDecayBehavior(oNode.getDecayBehavior());
			n.setId(oNode.getId());
			n.setLabel(oNode.getLabel());

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return n;
	}

	public Node getNode(Node oNode, String nodeType) {
		Node n = null;
		try {
			n = (Node) Class.forName(nodeClass.get(nodeType)).newInstance();
			n.setActivation(oNode.getActivation());
			n.setReferencedNode(oNode.getReferencedNode());
			n.setExciteBehavior(oNode.getExciteBehavior());
			n.setDecayBehavior(oNode.getDecayBehavior());
			n.setId(oNode.getId());
			n.setLabel(oNode.getLabel());

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return n;
	}

	public Node getNode(Node oNode, String decayBehavior, String exciteBehavior) {
		Node n = null;
		try {
			n = (Node) Class.forName(DefaultNodeClassName).newInstance();
			n.setActivation(oNode.getActivation());
			n.setReferencedNode(oNode.getReferencedNode());
			n.setExciteBehavior(excites.get(exciteBehavior));
			n.setDecayBehavior(decays.get(decayBehavior));
			n.setId(oNode.getId());
			n.setLabel(oNode.getLabel());

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return n;
	}

	public Node getNode(Node oNode, String nodeType, String decayBehavior,
			String exciteBehavior) {
		Node n = null;
		try {
			n = (Node) Class.forName(nodeClass.get(nodeType)).newInstance();
			n.setActivation(oNode.getActivation());
			n.setReferencedNode(oNode.getReferencedNode());
			n.setExciteBehavior(excites.get(exciteBehavior));
			n.setDecayBehavior(decays.get(decayBehavior));
			n.setId(oNode.getId());
			n.setLabel(oNode.getLabel());

		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return n;
	}

	public Node getNode(String nodeType) {
		Node n = null;
		try {
			n = (Node) Class.forName(nodeClass.get(nodeType)).newInstance();
			n.setExciteBehavior(defaultExcite);
			n.setDecayBehavior(defaultDecay);
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return n;
	}

	public Node getNode(String nodeType, long id) {
		Node n = getNode(nodeType);
		n.setId(id);
		return n;
	}

	public Node getNode(String nodeType, String nodeLabel) {
		Node n = getNode(nodeType);
		n.setLabel(nodeLabel);
		return n;
	}
	
	public Node getNode(String nodeType, long id, String nodeLabel) {
		Node n = getNode(nodeType);
		n.setId(id);
		n.setLabel(nodeLabel);		
		return n;
	}

	public Node getNode(String nodeType, String decayBehavior,
			String exciteBehavior) {
		Node n = null;
		try {
			n = (Node) Class.forName(nodeClass.get(nodeType)).newInstance();
			n.setExciteBehavior(excites.get(exciteBehavior));
			n.setDecayBehavior(decays.get(decayBehavior));
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
		}
		return n;
	}

	public void setDefaultDecay(DecayBehavior defaultDecay) {
		this.defaultDecay = defaultDecay;
	}

	public void setDefaultExcite(ExciteBehavior defaultExcite) {
		this.defaultExcite = defaultExcite;
	}

	public void setDefaultNode(String NodeDefaultName) {
		this.DefaultNodeClassName = NodeDefaultName;
	}
	
	public void setDefaultLink(String LinkDefaultName) {
		this.DefaultLinkClassName = LinkDefaultName;
	}

	public String getDefaultNodeType() {
		return DefaultNodeType;
	}

	public String getDefaultLinkType() {
		return DefaultLinkType;
	}

}//class