package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;

public class AgentXmlFactoryTest {

	private Document dom;
	private AgentXmlFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new AgentXmlFactory();
		dom=null;
		String xml ="<lida><taskspawners>" +
				"<taskspawner name='defaultTS'>" +
				"<class>edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl</class>" +
				"</taskspawner>" +
				"</taskspawners>" +
				"</lida>";
		
		Element docEle = parseDomElement(xml);
		factory.getTaskSpawners(docEle);

	}

	@After
	public void tearDown() throws Exception {
	}

	
	/**
	 * @param xml
	 * @return
	 */
	private Element parseDomElement(String xml) {
		// get the factory
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		try {
			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();
			
			// parse using builder to get DOM representation of the XML file
			dom = db.parse(new ByteArrayInputStream(xml.getBytes()));
		} catch (Exception e) {
			assertTrue(false);
		}
		Element docEle = dom.getDocumentElement();
		return docEle;
	}


	@Test
	public void testGetAgent() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseXmlFile() {
		fail("Not yet implemented");
	}

	@Test
	public void testParseDocument() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTaskManager() {
		String xml ="	<lida><taskmanager> \n" +
		"<param name=\"taskManager.tickDuration\" type=\"int\">50 </param> \n" +
		"<param name=\"taskManager.maxNumberOfThreads\" type=\"int\"> 100</param>\n" +
		"</taskmanager></lida>";

		Element docEle = parseDomElement(xml);
		TaskManager tm = factory.getTaskManager(docEle);
		assertTrue (tm!=null);
		assertEquals(50,tm.getTickDuration());
		
		xml ="	<lida><taskmanager> \n" +
		"</taskmanager></lida>";

		docEle = parseDomElement(xml);
		tm = factory.getTaskManager(docEle);
		assertTrue (tm!=null);
		assertEquals(1,tm.getTickDuration());
		
		xml ="	<lida><taskmanager> \n" +
		"<param name=\"taskManager.tickDuration\">50 </param> \n" +
		"<param name=\"taskManager.maxNumberOfThreads\"> 100</param>\n" +
		"</taskmanager></lida>";

		docEle = parseDomElement(xml);
		tm = factory.getTaskManager(docEle);
		assertTrue (tm!=null);
		assertEquals(50,tm.getTickDuration());

		xml ="	<lida><taskmanager> \n" +
		"<param name=\"taskManager.tickDuration\" type=\"double\">50 </param> \n" +
		"<param name=\"taskManager.maxNumberOfThreads\" type=\"int\"> 100</param>\n" +
		"</taskmanager></lida>";

		docEle = parseDomElement(xml);
		tm = factory.getTaskManager(docEle);
		assertTrue (tm!=null);
		assertEquals(1,tm.getTickDuration());

	}

	@Test
	public void testGetTaskSpawners() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetModules() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetModule() {
		String xml ="<lida><module name='PerceptualAssociativeMemory'>" +
				"<class>edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule</class>" +
				"<param name='pam.Upscale' type='double'>.7 </param>" +
				"<param name='pam.Downscale' type='double'>.6 </param>" +
				"<param name='pam.Selectivity' type='double'>.5 </param>" +
				"<param name='pam.newNodeType' type='string'>PamNodeImpl</param>" +
				"<param name='pam.newLinkType' type='string'>PamLinkImpl</param>" +
				"<taskspawner>defaultTS</taskspawner>" +
				"<initializerclass>edu.memphis.ccrg.lida.example.framework.initialization.PamInitializer</initializerclass>" +
				"</module></lida>";

		Element docEle = parseDomElement(xml);
		FrameworkModule module = factory.getModule(docEle);
		assertTrue(module!=null);
		
	}

	@Test
	public void testGetTasks() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetAssociatedModules() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetListeners() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetListener() {
		fail("Not yet implemented");
	}

	@Test
	public void testAssociateModules() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitializeModules() {
		fail("Not yet implemented");
	}

}
