/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.pam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.AgentImpl;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.GlobalInitializer;
import edu.memphis.ccrg.lida.framework.shared.ns.Link;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.pam.ns.BasicPamInitializer;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNS;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNSImpl;

public class BasicPamInitializerTest {

	private Agent agent;
	private PerceptualAssociativeMemoryNS pam;
	private BasicPamInitializer initializer;
	private Map<String, Object> params;
	private GlobalInitializer globalInitializer;

	@Before
	public void setUp() {
		agent = new AgentImpl(null);
		pam = new PerceptualAssociativeMemoryNSImpl();
		pam.setModuleName(ModuleName.PerceptualAssociativeMemory);

		initializer = new BasicPamInitializer();
		params = new HashMap<String, Object>();
		globalInitializer = GlobalInitializer.getInstance();
		globalInitializer.clearAttributes();
	}

	// <param name="nodes">red,blue,square,circle</param>
	@Test
	public void test1() {
		initializer.initModule(pam, agent, params);

		assertEquals(0, pam.getNodes().size() - pam.getLinkCategories().size());
	}

	@Test
	public void test2() {
		params.put("nodes", "");
		initializer.initModule(pam, agent, params);

		assertEquals(0, pam.getNodes().size() - pam.getLinkCategories().size());
	}

	@Test
	public void test3() {
		params.put("nodes", ",,,,,,,");
		initializer.initModule(pam, agent, params);

		assertEquals(0, pam.getNodes().size() - pam.getLinkCategories().size());
	}

	@Test
	public void test4() {
		params.put("nodes", "red,");
		initializer.initModule(pam, agent, params);

		assertEquals(1, pam.getNodes().size() - pam.getLinkCategories().size());
		assertEquals(pam.getNode("red"), globalInitializer.getAttribute("red"));
	}

	@Test
	public void test5() {
		params.put("nodes", ",red");
		initializer.initModule(pam, agent, params);

		assertEquals(1, pam.getNodes().size() - pam.getLinkCategories().size());
		assertEquals(pam.getNode("red"), globalInitializer.getAttribute("red"));
	}

	@Test
	public void test6() {
		params.put("nodes", ",red");
		initializer.initModule(pam, agent, params);

		assertEquals(1, pam.getNodes().size() - pam.getLinkCategories().size());
		assertEquals(pam.getNode("red"), globalInitializer.getAttribute("red"));
	}

	// <param name="links"> goodHealth:health,fairHealth:health

	@Test
	public void test7() {
		params.put("nodes", " red , blue ");
		params.put("links", "red:blue");
		initializer.initModule(pam, agent, params);

		assertEquals(2, pam.getNodes().size() - pam.getLinkCategories().size());
		Node red = pam.getNode("red");
		assertNotNull(red);
		assertEquals(red, globalInitializer.getAttribute("red"));

		Node blue = pam.getNode("blue");
		assertNotNull(blue);
		assertEquals(blue, globalInitializer.getAttribute("blue"));

		assertEquals(1, pam.getLinks().size());

		for (Link l : pam.getLinks()) {
			assertEquals(red, l.getSource());
			assertEquals(blue, l.getSink());
		}
	}

	@Test
	public void test8() {
		params.put("nodes", " red , blue ");
		params.put("links", "");
		initializer.initModule(pam, agent, params);

		assertEquals(2, pam.getNodes().size() - pam.getLinkCategories().size());
		Node red = pam.getNode("red");
		assertNotNull(red);
		assertEquals(red, globalInitializer.getAttribute("red"));

		Node blue = pam.getNode("blue");
		assertNotNull(blue);
		assertEquals(blue, globalInitializer.getAttribute("blue"));

		assertEquals(0, pam.getLinks().size());
	}

	@Test
	public void test9() {
		params.put("links", "red:blue");
		initializer.initModule(pam, agent, params);

		assertEquals(0, pam.getNodes().size() - pam.getLinkCategories().size());

		assertEquals(0, pam.getLinks().size());
	}

	@Test
	public void test10() {
		params.put("nodes", "  , blue ");
		params.put("links", ":blue");
		initializer.initModule(pam, agent, params);

		assertEquals(1, pam.getNodes().size() - pam.getLinkCategories().size());

		Node blue = pam.getNode("blue");
		assertNotNull(blue);
		assertEquals(blue, globalInitializer.getAttribute("blue"));

		assertEquals(0, pam.getLinks().size());
	}

}
