/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.memphis.ccrg.lida.framework.ModuleName;

public class FactoriesDataXmlLoaderTest {

	private FactoriesDataXmlLoader loader;

	private Map<String, StrategyDef> strategies;

	@Before
	public void setUp() throws Exception {
		loader = new FactoriesDataXmlLoader();

		strategies = new HashMap<String, StrategyDef>();
		StrategyDef def = new StrategyDef("className1", "strategy1", null,
				"excite", true);
		strategies.put("strategy1", def);
		def = new StrategyDef("className2", "strategy2", null, "decay", false);
		strategies.put("strategy2", def);
		def = new StrategyDef("className4", "strategy4", null, "decay", false);
		strategies.put("strategy4", def);
	}

	@Test
	public void testGetStrategies() {
		String xml = "<LidaFactories><strategies><strategy flyweight=\"true\" name=\"defaultExcite\" type=\"excite\">"
				+ "<class>edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy</class>"
				+ "<param name=\"m\" type=\"double\">1.0</param>"
				+ "</strategy>"
				+ "<strategy flyweight=\"false\" name=\"defaultDecay\" type=\"decay\">"
				+ "<class>edu.memphis.ccrg.lida.framework.strategies.DefaultDecay</class>"
				+ "<param name=\"d\" type=\"string\">hello</param>"
				+ "</strategy>" + "</strategies>" + "</LidaFactories>";
		Element e = parseDomElement(xml);
		Map<String, StrategyDef> strats = loader.getStrategies(e);
		assertEquals(2, strats.size());

		StrategyDef def = strats.get("defaultExcite");
		assertNotNull(def);
		assertEquals(true, def.isFlyWeight());
		assertEquals(
				"edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy",
				def.getClassName());
		assertEquals("defaultExcite", def.getName());
		assertEquals(1.0, def.getParams().get("m"));

		def = strats.get("defaultDecay");
		assertNotNull(def);
		assertEquals(false, def.isFlyWeight());
		assertEquals("edu.memphis.ccrg.lida.framework.strategies.DefaultDecay",
				def.getClassName());
		assertEquals("defaultDecay", def.getName());
		assertEquals("hello", def.getParams().get("d"));
	}

	@Test
	public void testGetStrategyDef() {
		String xml = "<strategy flyweight=\"true\" name=\"defaultExcite\" type=\"excite\">"
				+ "<class>edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy</class>"
				+ "<param name=\"m\" type=\"double\">1.0</param>"
				+ "</strategy>";
		Element e = parseDomElement(xml);
		StrategyDef def = loader.getStrategyDef(e);

		assertNotNull(def);
		assertEquals(true, def.isFlyWeight());
		assertEquals(
				"edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy",
				def.getClassName());
		assertEquals("defaultExcite", def.getName());
		assertEquals(1.0, def.getParams().get("m"));

		xml = "<strategy flyweight=\"false\" name=\"defaultDecay\" type=\"decay\">"
				+ "<class>edu.memphis.ccrg.lida.framework.strategies.DefaultDecay</class>"
				+ "<param name=\"d\" type=\"string\">hello</param>"
				+ "</strategy>";

		e = parseDomElement(xml);
		def = loader.getStrategyDef(e);

		assertNotNull(def);
		assertEquals(false, def.isFlyWeight());
		assertEquals("edu.memphis.ccrg.lida.framework.strategies.DefaultDecay",
				def.getClassName());
		assertEquals("defaultDecay", def.getName());
		assertEquals("hello", def.getParams().get("d"));
	}

	@Test
	public void testGetLinkables() {
		String xml = "<LidaFactories>"
				+ "<nodes>"
				+ "<node name=\"PamNodeImpl\">"
				+ "<class>edu.memphis.ccrg.lida.pam.PamNodeImpl</class>"
				+ "<defaultstrategy>strategy1</defaultstrategy>"
				+ "<defaultstrategy>strategy2</defaultstrategy>"
				+ "<defaultstrategy>strategy3</defaultstrategy>"
				+ "<param name=\"learnable.baseLevelDecayStrategy\" type=\"string\">slowDecay</param>"
				+ "<param name=\"learnable.baseLevelExciteStrategy\" type=\"string\">slowExcite</param>"
				+ "<param name=\"learnable.baseLevelRemovalThreshold\" type=\"double\">0.0</param>"
				+ "<param name=\"learnable.baseLevelActivation\" type=\"double\">0.1</param> "
				+ "</node>" + "<node name=\"NodeImpl\">"
				+ "<class>edu.memphis.ccrg.lida.pam.PamNodeImpl2</class>"
				+ "<defaultstrategy>strategy4</defaultstrategy>"
				+ "<defaultstrategy>strategy2</defaultstrategy>"
				+ "<param name=\"param1\" type=\"string\">slowDecay</param>"
				+ "</node>" + "</nodes></LidaFactories>";

		Element e = parseDomElement(xml);

		Map<String, LinkableDef> linkables = loader.getLinkables(e, "nodes",
				"node", strategies);

		assertEquals(2, linkables.size());
		LinkableDef def = linkables.get("PamNodeImpl");
		assertNotNull(def);
		assertEquals("edu.memphis.ccrg.lida.pam.PamNodeImpl", def
				.getClassName());
		assertEquals("PamNodeImpl", def.getName());
		assertEquals("strategy1", def.getDefaultStrategies().get("excite"));
		assertEquals("strategy2", def.getDefaultStrategies().get("decay"));
		assertFalse(def.getDefaultStrategies().containsKey("strategy3"));
		assertEquals(4, def.getParams().size());
		assertEquals("slowDecay", def.getParams().get(
				"learnable.baseLevelDecayStrategy"));

		def = linkables.get("NodeImpl");
		assertNotNull(def);
		assertEquals("edu.memphis.ccrg.lida.pam.PamNodeImpl2", def
				.getClassName());
		assertEquals("NodeImpl", def.getName());
		assertEquals("strategy4", def.getDefaultStrategies().get("decay"));
		assertFalse(def.getDefaultStrategies().containsKey("strategy3"));
		assertEquals(1, def.getParams().size());
		assertEquals("slowDecay", def.getParams().get("param1"));
	}

	@Test
	public void testGetLinkable() {
		String xml = "<node name=\"PamNodeImpl\">"
				+ "<class>edu.memphis.ccrg.lida.pam.PamNodeImpl</class>"
				+ "<defaultstrategy>strategy1</defaultstrategy>"
				+ "<defaultstrategy>strategy2</defaultstrategy>"
				+ "<defaultstrategy>strategy3</defaultstrategy>"
				+ "<param name=\"learnable.baseLevelDecayStrategy\" type=\"string\">slowDecay</param>"
				+ "<param name=\"learnable.baseLevelExciteStrategy\" type=\"string\">slowExcite</param>"
				+ "<param name=\"learnable.baseLevelRemovalThreshold\" type=\"double\">0.0</param>"
				+ "<param name=\"learnable.baseLevelActivation\" type=\"double\">0.1</param> </node>";

		Element e = parseDomElement(xml);

		LinkableDef def = loader.getLinkable(e, strategies);

		assertNotNull(def);
		assertEquals("edu.memphis.ccrg.lida.pam.PamNodeImpl", def
				.getClassName());
		assertEquals("PamNodeImpl", def.getName());
		assertEquals("strategy1", def.getDefaultStrategies().get("excite"));
		assertEquals("strategy2", def.getDefaultStrategies().get("decay"));
		assertFalse(def.getDefaultStrategies().containsKey("strategy3"));
		assertEquals(4, def.getParams().size());
		assertEquals("slowDecay", def.getParams().get(
				"learnable.baseLevelDecayStrategy"));
	}

	@Test
	public void testGetCodelets() {
		String xml = "<LidaFactories><tasks><task name=\"topleft\">"
				+ "<class>edu.memphis.ccrg.lida.example.genericlida.featuredetectors.TopLeftDetector</class>"
				+ "<ticksperrun>9</ticksperrun>"
				+ "<defaultstrategy>strategy1</defaultstrategy>"
				+ "<defaultstrategy>strategy2</defaultstrategy>"
				+ "<defaultstrategy>strategy3</defaultstrategy>"
				+ "<associatedmodule function=\"hi\">Environment</associatedmodule>"
				+ "<associatedmodule function=\"hola\">Workspace</associatedmodule>"
				+ "<param name=\"param\" type=\"int\">10</param>"
				+ "</task>"
				+ "<task name=\"bottomright\">"
				+ "<class>edu.memphis.ccrg.lida.example.genericlida.featuredetectors.Another</class>"
				+ "<ticksperrun>9</ticksperrun>"
				+ "<defaultstrategy>strategy1</defaultstrategy>"
				+ "<param name=\"param1\" type=\"string\">hi</param>"
				+ "</task>" + "</tasks></LidaFactories>";

		Element e = parseDomElement(xml);

		Map<String, FrameworkTaskDef> codelets = loader.getTasks(e, strategies);
		assertEquals(2, codelets.size());

		FrameworkTaskDef def = codelets.get("topleft");
		assertNotNull(def);
		assertEquals(
				"edu.memphis.ccrg.lida.example.genericlida.featuredetectors.TopLeftDetector",
				def.getClassName());
		assertEquals("topleft", def.getName());
		assertEquals("strategy1", def.getDefaultStrategies().get("excite"));
		assertEquals("strategy2", def.getDefaultStrategies().get("decay"));
		assertFalse(def.getDefaultStrategies().containsKey("strategy3"));
		assertEquals(1, def.getParams().size());
		assertEquals(10, def.getParams().get("param"));
		assertEquals(9, def.getTicksPerRun());
		Map<ModuleName, String> assocMod = def.getAssociatedModules();
		assertEquals("hi", assocMod.get(ModuleName.Environment));
		assertEquals("hola", assocMod.get(ModuleName.Workspace));

		def = codelets.get("bottomright");
		assertNotNull(def);
		assertEquals(
				"edu.memphis.ccrg.lida.example.genericlida.featuredetectors.Another",
				def.getClassName());
		assertEquals("bottomright", def.getName());
		assertEquals("strategy1", def.getDefaultStrategies().get("excite"));
		assertFalse(def.getDefaultStrategies().containsKey("strategy3"));
		assertEquals(1, def.getParams().size());
		assertEquals("hi", def.getParams().get("param1"));
		assertEquals(9, def.getTicksPerRun());
	}

	@Test
	public void testGetCodelet() {
		String xml = "<task name=\"topleft\">"
				+ "<class>edu.memphis.ccrg.lida.example.genericlida.featuredetectors.TopLeftDetector</class>"
				+ "<ticksperrun>9</ticksperrun>"
				+ "<defaultstrategy>strategy1</defaultstrategy>"
				+ "<defaultstrategy>strategy2</defaultstrategy>"
				+ "<defaultstrategy>strategy3</defaultstrategy>"
				+ "<param name=\"param\" type=\"int\">10</param>" + "</task>";

		Element e = parseDomElement(xml);

		FrameworkTaskDef def = loader.getTaskDef(e, strategies);
		assertNotNull(def);
		assertEquals(
				"edu.memphis.ccrg.lida.example.genericlida.featuredetectors.TopLeftDetector",
				def.getClassName());
		assertEquals("topleft", def.getName());
		assertEquals("strategy1", def.getDefaultStrategies().get("excite"));
		assertEquals("strategy2", def.getDefaultStrategies().get("decay"));
		assertFalse(def.getDefaultStrategies().containsKey("strategy3"));
		assertEquals(1, def.getParams().size());
		assertEquals(10, def.getParams().get("param"));
		assertEquals(9, def.getTicksPerRun());
	}

	@Test
	public void testCheckStrategies() {
		List<String> desiredStrategies = new ArrayList<String>();
		desiredStrategies.add("strategy1");
		desiredStrategies.add("strategy2");
		desiredStrategies.add("strategy3");

		loader.checkStrategies(desiredStrategies, strategies);

		assertEquals(2, desiredStrategies.size());
		assertFalse(desiredStrategies.contains("strategy3"));
	}

	private Element parseDomElement(String xml) {
		Document dom = XmlUtils.parseXmlString(xml);
		Element docEle = dom.getDocumentElement();
		return docEle;
	}
}
