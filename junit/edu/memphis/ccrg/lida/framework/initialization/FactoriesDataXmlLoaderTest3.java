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

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

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

public class FactoriesDataXmlLoaderTest3 {

	private FactoriesDataXmlLoader loader;
	private static ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		loader = new FactoriesDataXmlLoader();
	}

	@Test
	public void testLoadFactoriesData() {
		Properties p = new Properties();
		p.setProperty("lida.elementfactory.data",
				"testData/testFactoriesData.xml");

		loader.loadFactoriesData(p);

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

}
