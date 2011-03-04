/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;

/**
 * 
 * Loads the factoriesData.xml file which configures the factories of the framework i.e.
 * what strategies are used by the objects created by the factory, the types of node,
 * links, and codelets that can be created as well.
 * 
 * @author Javier Snaider
 *
 */
public class LidaFactoriesXMLLoader {
	
	private static final String DEFAULT_XML_FILE_PATH = "configs/factoriesData.xml";
	private static final String DEFAULT_SCHEMA_FILE_PATH = "configs/LidaFactories.xsd";
	private static final Logger logger = Logger.getLogger(LidaFactoriesXMLLoader.class.getCanonicalName());
	private Document dom;
	private static LidaElementFactory nfactory = LidaElementFactory.getInstance();
	private Map<String, StrategyDef> strategies;
	private Map<String, LinkableDef> nodes;
	private Map<String, LinkableDef> links;
	private Map<String, CodeletDef> codelets;

	/**
	 * Loads {@link LidaElementFactory} with object types specified in {@link Properties}
	 * @param properties {@link Properties}
	 */
	public void loadFactoriesData(Properties properties) {
		String fileName = properties.getProperty("lida.factories",
				DEFAULT_XML_FILE_PATH);
		parseXmlFile(fileName);
		parseDocument();
		fillFactories();
	}

	private void fillFactories() {
		for(LinkableDef ld:nodes.values()){
			nfactory.addNodeType(ld);
		}
		
		for(LinkableDef ld:links.values()){
			nfactory.addLinkType(ld);
		}
				
		for(StrategyDef sd:strategies.values()){
			if (sd.getType().equalsIgnoreCase("decay")){
				nfactory.addDecayStrategy(sd.getName(), sd);
			}else if (sd.getType().equalsIgnoreCase("excite")){
				nfactory.addExciteStrategy(sd.getName(), sd);
			}
			nfactory.addStrategy(sd.getName(), sd);
		}
		for(CodeletDef cd:codelets.values()){
			nfactory.addCodeletType(cd);
		}
	}

	private void parseXmlFile(String fileName) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
			if (XmlUtils.validateXmlFile(fileName, DEFAULT_SCHEMA_FILE_PATH)){
				// parse using builder to get DOM representation of the XML file
				dom = db.parse(fileName);
			}else{
				logger.log(Level.WARNING, "Xml file invalid, no factories data was loaded.", LidaTaskManager.getCurrentTick());
			}
		}catch (Exception e) {
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);
			pw.close();
			logger.log(Level.WARNING, sw.getBuffer().toString(), 0L);
		}
	}

	
	private void parseDocument() {
		// get the root element
		Element docEle = dom.getDocumentElement();
		strategies = getStrategies(docEle);
		nodes = getLinkables(docEle, "nodes", "node");
		links = getLinkables(docEle, "links", "link");
		codelets = getCodelets(docEle);

		// LidaTaskManager tm= getTaskManager(docEle);
		// lida=new LidaImpl(tm);

	}

	private Map<String, StrategyDef> getStrategies(Element element) {
		Map<String, StrategyDef> strat = new HashMap<String, StrategyDef>();
		List<Element> list = getCollectionElements(element, "strategies",
				"strategy");
		if (list != null && list.size() > 0) {
			for (Element e : list) {
				StrategyDef strategy = getStrategyDef(e);
				strat.put(strategy.getName(), strategy);
			}
		}
		return strat;
	}

	private StrategyDef getStrategyDef(Element e) {
		StrategyDef strategy = new StrategyDef();
		String className = XmlUtils.getTextValue(e, "class");
		String name = e.getAttribute("name");
		String type = e.getAttribute("type");
		boolean fweight=Boolean.parseBoolean(e.getAttribute("flyweight"));
		Map<String,Object> params = XmlUtils.getTypedParams(e);

		strategy.setClassName(className);
		strategy.setName(name);
		strategy.setType(type);
		strategy.setFlyWeight(fweight);
		strategy.setParams(params);

		return strategy;
	}

	private Map<String, LinkableDef> getLinkables(Element element,
			String groupName, String childName) {
		Map<String, LinkableDef> linkables = new HashMap<String, LinkableDef>();
		List<Element> list = getCollectionElements(element, groupName,
				childName);
		if (list != null && list.size() > 0) {
			for (Element e : list) {
				LinkableDef linkable = getLinkable(e);
				linkables.put(linkable.getName(), linkable);
			}
		}
		return linkables;
	}

	private LinkableDef getLinkable(Element e) {
		LinkableDef node = new LinkableDef();
		String className = XmlUtils.getTextValue(e, "class");
		String name = e.getAttribute("name");
		Map<String,String> strat = new HashMap<String,String>();
		List<String> list=	XmlUtils.getChildrenValues(e, "defaultstrategy");
		checkStrategies(list);
		for(String s:list){
			StrategyDef bd=strategies.get(s);
			strat.put(bd.getType(), s);
		}

		Map<String,Object> params = XmlUtils.getTypedParams(e);
		node.setClassName(className);
		node.setName(name);
		node.setParams(params);
		node.setDefaultStrategies(strat);
		return node;
	}

	private Map<String, CodeletDef> getCodelets(Element docEle) {
		Map<String, CodeletDef> codels = new HashMap<String, CodeletDef>();
		List<Element> list = getCollectionElements(docEle, "codelets",
				"codelet");
		if (list != null && list.size() > 0) {
			for (Element e : list) {
				CodeletDef codelet = getCodelet(e);
				if (codelet != null) {
					codels.put(codelet.getName(), codelet);
				}
			}
		}
		return codels;
	}

	private CodeletDef getCodelet(Element e) {
		CodeletDef codelet = null;
		String className = XmlUtils.getTextValue(e, "class");
		String name = e.getAttribute("name");
		String type = e.getAttribute("type");
		char codeletType = evaluateType(type);
		Map<String,String> behav = new HashMap<String,String>();
		List<String> list=	XmlUtils.getChildrenValues(e, "defaultstrategy");
		checkStrategies(list);
		for(String s:list){
			StrategyDef bd=strategies.get(s);
			behav.put(bd.getType(), s);
		}

		Map<String,Object> params = XmlUtils.getTypedParams(e);

		if (codeletType != 0) {
			codelet = new CodeletDef();
			codelet.setClassName(className);
			codelet.setName(name);
			codelet.setType(codeletType);
			codelet.setParams(params);
			codelet.setDefaultStrategies(behav);
		}else{
			logger.log(Level.WARNING,name+ " has an invalid type. It is not loaded.");
		}
		return codelet;
	}

	private char evaluateType(String type) {
		char codeletType = 0;
		if ("SBC".equalsIgnoreCase(type) || "SBcodelet".equalsIgnoreCase(type)) {
			codeletType = 'B';
		} else if ("FD".equalsIgnoreCase(type)
				|| "FeatureDetector".equalsIgnoreCase(type)) {
			codeletType = 'F';
		} else if ("AC".equalsIgnoreCase(type)
				|| "AttentionCodelet".equalsIgnoreCase(type)) {
			codeletType = 'A';
		} else {
			logger.log(Level.WARNING, type + ": Codelet Type invalid", 0L);
		}
		return codeletType;
	}

	private void checkStrategies(List<String> strat) {
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

	private List<Element> getCollectionElements(Element e, String groupName,
			String childName) {
		NodeList nl = e.getElementsByTagName(groupName);
		if (nl != null && nl.getLength() > 0) {
			Element bhvrsElement = (Element) nl.item(0);
			List<Element> list = XmlUtils.getChildren(bhvrsElement, childName);
			return list;
		}
		return null;
	}
}
