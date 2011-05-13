/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.pam;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Properties;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.AgentStarter;
import edu.memphis.ccrg.lida.framework.initialization.ConfigUtils;
import edu.memphis.ccrg.lida.framework.initialization.FactoriesDataXmlLoader;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.LinkCategory;
import edu.memphis.ccrg.lida.framework.shared.Node;

/**
 * @author Usef Faghihi
 */
public class PamLinkImplTest{
	
	private Node node1;
	private Node node2;
	private LinkCategory linkCategory;
	private PamLinkImpl link1,link2, link3;
	
	private static ElementFactory factory = ElementFactory.getInstance();
	
	@BeforeClass
	public static void setUpBeforeClass(){
		factory = ElementFactory.getInstance();
		FactoriesDataXmlLoader factoryLoader = new FactoriesDataXmlLoader();
		Properties prop = ConfigUtils.loadProperties(AgentStarter.DEFAULT_PROPERTIES_PATH);
		factoryLoader.loadFactoriesData(prop);
	}
 
	@Before
	public void setUp() throws Exception {
		new PerceptualAssociativeMemoryImpl();
		
		node1 = factory.getNode();
		node2 = factory.getNode();
		linkCategory= PerceptualAssociativeMemoryImpl.NONE;	
		link1 = (PamLinkImpl) factory.getLink("PamLinkImpl", node1, node2, linkCategory);
		link2 = (PamLinkImpl) factory.getLink("PamLinkImpl", node1, node2, linkCategory);
		link3 = (PamLinkImpl) factory.getLink("PamLinkImpl", node2, node1, linkCategory);	
	}

	/**
	 * {@link edu.memphis.ccrg.lida.pam.PamLinkImpl#equals(java.lang.Object)}.
	 */
	@Test
	public void testEquals() {
		assertTrue(link1.equals(link2));
		assertTrue(link2.equals(link1));
		assertFalse(link1.equals(link3));
		assertFalse(link3.equals(link2));
	}
	
	/**
	 * {@link edu.memphis.ccrg.lida.pam.PamLinkImpl#equals(Object)}
	 */
	@Test
	public void testHashCode(){		
		assertEquals(link1.hashCode(), link2.hashCode());
	}

}
