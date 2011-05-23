/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.memphis.ccrg.lida.framework.shared.ElementFactory;

/**
 * 
 * Loads the factoriesData.xml file which configures the factories of the
 * framework i.e. what strategies are used by the objects created by the
 * factory, the types of node, links, and codelets that can be created as well.
 * 
 * @author Javier Snaider
 * 
 */
public class FactoriesDataXmlLoader {

	private static final String DEFAULT_XML_FILE_PATH = "configs/factoriesData.xml";
	private static final String DEFAULT_SCHEMA_FILE_PATH = "edu/memphis/ccrg/lida/framework/initialization/config/LidaFactories.xsd";
	private static final Logger logger = Logger
			.getLogger(FactoriesDataXmlLoader.class.getCanonicalName());

	private static ElementFactory nfactory = ElementFactory.getInstance();

	/**
	 * Loads {@link ElementFactory} with object types specified in
	 * {@link Properties}
	 * 
	 * @param properties
	 *            {@link Properties}
	 */
	public void loadFactoriesData(Properties properties) {
		String fileName = properties.getProperty("lida.elementFactory",
				DEFAULT_XML_FILE_PATH);
		Document dom = XmlUtils
				.parseXmlFile(fileName, DEFAULT_SCHEMA_FILE_PATH);
		parseDocument(dom);
	}

	void parseDocument(Document dom) {
		// get the root element
		Element docEle = dom.getDocumentElement();
		Map<String, StrategyDef> strategies = getStrategies(docEle);
		Map<String, LinkableDef> nodes = getLinkables(docEle, "nodes", "node",
				strategies);
		Map<String, LinkableDef> links = getLinkables(docEle, "links", "link",
				strategies);
		Map<String, CodeletDef> codelets = getCodelets(docEle, strategies);
		fillNodes(nodes);
		fillLinks(links);
		fillStrategies(strategies);
		fillCodelets(codelets);
	}

	private void fillNodes(Map<String, LinkableDef> nodes) {
		for (LinkableDef ld : nodes.values()) {
			nfactory.addNodeType(ld);
		}
	}

	private void fillLinks(Map<String, LinkableDef> links) {
		for (LinkableDef ld : links.values()) {
			nfactory.addLinkType(ld);
		}
	}

	private void fillStrategies(Map<String, StrategyDef> strategies) {
		for (StrategyDef sd : strategies.values()) {
			if (sd.getType().equalsIgnoreCase("decay")) {
				nfactory.addDecayStrategy(sd.getName(), sd);
			} else if (sd.getType().equalsIgnoreCase("excite")) {
				nfactory.addExciteStrategy(sd.getName(), sd);
			}
			nfactory.addStrategy(sd.getName(), sd);
		}
	}

	private void fillCodelets(Map<String, CodeletDef> codelets) {
		for (CodeletDef cd : codelets.values()) {
			nfactory.addCodeletType(cd);
		}
	}

	Map<String, StrategyDef> getStrategies(Element element) {
		Map<String, StrategyDef> strat = new HashMap<String, StrategyDef>();
		List<Element> list = XmlUtils.getChildrenInGroup(element,
				"strategies", "strategy");
		if (list != null && list.size() > 0) {
			for (Element e : list) {
				StrategyDef strategy = getStrategyDef(e);
				strat.put(strategy.getName(), strategy);
			}
		}
		return strat;
	}

	StrategyDef getStrategyDef(Element e) {
		StrategyDef strategy = new StrategyDef();
		String className = XmlUtils.getTextValue(e, "class");
		String name = e.getAttribute("name");
		String type = e.getAttribute("type");
		boolean fweight = Boolean.parseBoolean(e.getAttribute("flyweight"));
		Map<String, Object> params = XmlUtils.getTypedParams(e);

		strategy.setClassName(className.trim());
		strategy.setName(name.trim());
		strategy.setType(type.trim());
		strategy.setFlyWeight(fweight);
		strategy.setParams(params);

		return strategy;
	}

	Map<String, LinkableDef> getLinkables(Element element, String groupName,
			String childName, Map<String, StrategyDef> strategies) {
		Map<String, LinkableDef> linkables = new HashMap<String, LinkableDef>();
		List<Element> list = XmlUtils.getChildrenInGroup(element, groupName,
				childName);
		if (list != null && list.size() > 0) {
			for (Element e : list) {
				LinkableDef linkable = getLinkable(e, strategies);
				linkables.put(linkable.getName(), linkable);
			}
		}
		return linkables;
	}

	LinkableDef getLinkable(Element e, Map<String, StrategyDef> strategies) {
		LinkableDef node = new LinkableDef();
		String className = XmlUtils.getTextValue(e, "class");
		String name = e.getAttribute("name");
		Map<String, String> strat = new HashMap<String, String>();
		List<String> list = XmlUtils.getChildrenValues(e, "defaultstrategy");
		checkStrategies(list, strategies);
		for (String s : list) {
			StrategyDef bd = strategies.get(s);
			String type = bd.getType();
			if (strat.containsKey(type)) {
				logger.log(Level.WARNING, "Cannot add strategy " + s
						+ " a strategy of type: " + type + " already exists");
			} else {
				strat.put(type, s);
			}
		}

		Map<String, Object> params = XmlUtils.getTypedParams(e);
		node.setClassName(className.trim());
		node.setName(name.trim());
		node.setParams(params);
		node.setDefaultStrategies(strat);
		return node;
	}

	Map<String, CodeletDef> getCodelets(Element docEle,
			Map<String, StrategyDef> strategies) {
		Map<String, CodeletDef> codels = new HashMap<String, CodeletDef>();
		List<Element> list = XmlUtils.getChildrenInGroup(docEle, "codelets",
				"codelet");
		if (list != null && list.size() > 0) {
			for (Element e : list) {
				CodeletDef codelet = getCodelet(e, strategies);
				if (codelet != null) {
					codels.put(codelet.getName(), codelet);
				}
			}
		}
		return codels;
	}

	CodeletDef getCodelet(Element e, Map<String, StrategyDef> strategies) {
		CodeletDef codelet = null;
		String className = XmlUtils.getTextValue(e, "class");
		String name = e.getAttribute("name");
		Map<String, String> behav = new HashMap<String, String>();
		List<String> list = XmlUtils.getChildrenValues(e, "defaultstrategy");
		checkStrategies(list, strategies);
		for (String s : list) {
			StrategyDef bd = strategies.get(s);
			behav.put(bd.getType(), s);
		}

		Map<String, Object> params = XmlUtils.getTypedParams(e);

		codelet = new CodeletDef();
		codelet.setClassName(className.trim());
		codelet.setName(name.trim());
		codelet.setParams(params);
		codelet.setDefaultStrategies(behav);
		return codelet;
	}

	void checkStrategies(List<String> strat, Map<String, StrategyDef> strategies) {
		Iterator<String> it = strat.iterator();
		String b;
		while (it.hasNext()) {
			b = it.next();
			if (!strategies.containsKey(b)) {
				logger.log(Level.WARNING, b
						+ " is not a defined Strategy. It is excluded", 0L);
				it.remove();
			}
		}
	}
}
