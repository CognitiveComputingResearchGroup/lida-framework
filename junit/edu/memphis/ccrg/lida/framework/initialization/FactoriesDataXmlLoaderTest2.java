/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Linkable;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy;
import edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy;
import edu.memphis.ccrg.lida.framework.strategies.Strategy;
import edu.memphis.ccrg.lida.framework.tasks.Codelet;
import edu.memphis.ccrg.lida.pam.ns.PamLinkImpl;
import edu.memphis.ccrg.lida.pam.ns.PamNodeImpl;
import edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.BasicStructureBuildingCodelet;

public class FactoriesDataXmlLoaderTest2 {

	private FactoriesDataXmlLoader loader;
	private static ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		loader = new FactoriesDataXmlLoader();
	}

	@Test
	public void testParseDocument() {
		String xml = "<LidaFactories>"
				+ "<strategies>"
				+ "	<strategy flyweight=\"true\" name=\"excite1\" type=\"excite\">"
				+ "<class>edu.memphis.ccrg.lida.framework.strategies.LinearExciteStrategy</class>"
				+ "<param name=\"m\" type=\"double\">1.0</param>"
				+ "</strategy>"

				+ "<strategy flyweight=\"false\" name=\"decay1\" type=\"decay\">"
				+ "<class>edu.memphis.ccrg.lida.framework.strategies.LinearDecayStrategy</class>"
				+ "<param name=\"d\" type=\"string\">hello</param>"
				+ "</strategy>"
				+ "</strategies>"

				+ "<nodes>"
				+ "<node name=\"node1\">"
				+ "<class>edu.memphis.ccrg.lida.pam.PamNodeImpl</class>"
				+ "<defaultstrategy>strategy1</defaultstrategy>"
				+ "<defaultstrategy>strategy2</defaultstrategy>"
				+ "<defaultstrategy>strategy3</defaultstrategy>"
				+ "<param name=\"learnable.baseLevelDecayStrategy\" type=\"string\">slowDecay</param>"
				+ "<param name=\"baseLevelExciteStrategy\" type=\"string\">slowExcite</param>"
				+ "<param name=\"baseLevelRemovalThreshold\" type=\"double\">0.0</param>"
				+ "<param name=\"baseLevelActivation\" type=\"double\">0.1</param> "
				+ "</node>"
				+ "<node name=\"node2\">"
				+ "<class>edu.memphis.ccrg.lida.pam.PamNodeImpl2</class>"
				+ "<defaultstrategy>strategy4</defaultstrategy>"
				+ "<defaultstrategy>strategy2</defaultstrategy>"
				+ "<param name=\"param1\" type=\"string\">slowDecay</param>"
				+ "</node>"
				+ "</nodes>"

				+ "<links>"
				+ "<link name=\"link1\">"
				+ "<class>edu.memphis.ccrg.lida.pam.PamLinkImpl</class>"
				+ "<defaultstrategy>strategy1</defaultstrategy>"
				+ "<defaultstrategy>strategy2</defaultstrategy>"
				+ "<defaultstrategy>strategy3</defaultstrategy>"
				+ "<param name=\"learnable.baseLevelDecayStrategy\" type=\"string\">slowDecay</param>"
				+ "<param name=\"baseLevelExciteStrategy\" type=\"string\">slowExcite</param>"
				+ "<param name=\"baseLevelRemovalThreshold\" type=\"double\">0.0</param>"
				+ "<param name=\"baseLevelActivation\" type=\"double\">0.1</param> "
				+ "</link>"
				+ "</links>"

				+ "<tasks><task name=\"topleft\">"
				+ "<class>edu.memphis.ccrg.lida.workspace.structurebuildingcodelets.BasicStructureBuildingCodelet</class>"
				+ "<ticksperrun>1</ticksperrun>"
				+ "<defaultstrategy>strategy1</defaultstrategy>"
				+ "<defaultstrategy>strategy2</defaultstrategy>"
				+ "<defaultstrategy>strategy3</defaultstrategy>"
				+ "<param name=\"param\" type=\"int\">10</param>"
				+ "</task>"
				+ "<task name=\"bottomright\">"
				+ "<class>edu.memphis.ccrg.lida.example.genericlida.featuredetectors.Another</class>"
				+ "<ticksperrun>1</ticksperrun>"
				+ "<defaultstrategy>strategy1</defaultstrategy>"
				+ "<param name=\"param1\" type=\"string\">hi</param>"
				+ "</task>" + "</tasks>"

				+ "</LidaFactories>";
		Document d = parseDocument(xml);

		loader.parseDocument(d);

		assertTrue(factory.containsStrategy("excite1"));
		assertTrue(factory.containsStrategy("decay1"));
		assertTrue(factory.containsNodeType("node1"));
		assertTrue(factory.containsNodeType("node2"));
		assertTrue(factory.containsLinkType("link1"));
		assertTrue(factory.containsTaskType("topleft"));
		assertTrue(factory.containsTaskType("bottomright"));

		Strategy s = factory.getStrategy("excite1");
		assertTrue(s instanceof LinearExciteStrategy);
		s = factory.getStrategy("decay1");
		assertTrue(s instanceof LinearDecayStrategy);

		Linkable l = factory.getNode("node1");
		assertTrue(l instanceof PamNodeImpl);
		l = factory.getNode("node2");
		assertNull(l);
		Node n = new NodeImpl();
		n.setId(304593);
		Node n1 = new NodeImpl();
		n1.setId(3043);
		l = factory.getLink("link1", n, n1, new PamNodeImpl());
		assertTrue(l instanceof PamLinkImpl);

		Codelet c = (Codelet) factory.getFrameworkTask("topleft", null);
		assertTrue(c instanceof BasicStructureBuildingCodelet);

		c = (Codelet) factory.getFrameworkTask("bottomright", null);
		assertNull(c);
	}

	/**
	 * @param xml
	 * @return
	 */
	private Document parseDocument(String xml) {
		Document dom = null;
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			dom = db.parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
		return dom;
	}

}
