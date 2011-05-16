package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.memphis.ccrg.lida.episodicmemory.LocalAssociationListener;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.MockFrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener;
import edu.memphis.ccrg.lida.workspace.CueBackgroundTask;

public class AgentXmlFactoryTest {

	private Document dom;
	private AgentXmlFactory factory;

	@Before
	public void setUp() throws Exception {
		factory = new AgentXmlFactory();
		dom = null;
		// String xml ="<lida><taskspawners>" +
		// "<taskspawner name='defaultTS'>" +
		// "<class>edu.memphis.ccrg.lida.framework.tasks.TaskSpawnerImpl</class>"
		// +
		// "</taskspawner>" +
		// "</taskspawners>" +
		// "</lida>";
		//		
		// Element docEle = parseDomElement(xml);
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
		String xml = "	<lida><taskmanager> \n"
				+ "<param name=\"taskManager.tickDuration\" type=\"int\">50 </param> \n"
				+ "<param name=\"taskManager.maxNumberOfThreads\" type=\"int\"> 100</param>\n"
				+ "</taskmanager></lida>";

		Element docEle = parseDomElement(xml);
		TaskManager tm = factory.getTaskManager(docEle);
		assertTrue(tm != null);
		assertEquals(50, tm.getTickDuration());

		xml = "	<lida><taskmanager> \n" + "</taskmanager></lida>";

		docEle = parseDomElement(xml);
		tm = factory.getTaskManager(docEle);
		assertTrue(tm != null);
		assertEquals(1, tm.getTickDuration());

		xml = "	<lida><taskmanager> \n"
				+ "<param name=\"taskManager.tickDuration\">50 </param> \n"
				+ "<param name=\"taskManager.maxNumberOfThreads\"> 100</param>\n"
				+ "</taskmanager></lida>";

		docEle = parseDomElement(xml);
		tm = factory.getTaskManager(docEle);
		assertTrue(tm != null);
		assertEquals(50, tm.getTickDuration());

		xml = "	<lida><taskmanager> \n"
				+ "<param name=\"taskManager.tickDuration\" type=\"double\">50 </param> \n"
				+ "<param name=\"taskManager.maxNumberOfThreads\" type=\"int\"> 100</param>\n"
				+ "</taskmanager></lida>";

		docEle = parseDomElement(xml);
		tm = factory.getTaskManager(docEle);
		assertTrue(tm != null);
		assertEquals(1, tm.getTickDuration());

	}

	@Test
	public void testGetTaskSpawner() {
		String xml = "<taskspawner name='defaultTS'>"
				+ "<class>edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner</class>"
				+ "<param name=\"param1\">50</param> \n"
				+ "<param name=\"param2\" type='int'>50 </param> \n"
				+ "</taskspawner>";

		Element docEle = parseDomElement(xml);
		TaskManager tm = new TaskManager(10, 10);
		Map<String, TaskSpawner> spawners = new HashMap<String, TaskSpawner>();
		factory.getTaskSpawner(docEle, tm, spawners);
		assertEquals(1, spawners.size());
		TaskSpawner ts = (TaskSpawner) spawners.get("defaultTS");
		assertTrue(ts != null);
		assertEquals("50", ts.getParam("param1", null));
		assertEquals(50, ts.getParam("param2", null));
	}

	@Test
	public void testGetTaskSpawners() {
		String xml = "<lida><taskspawners>"
				+ "<taskspawner name='defaultTS'>"
				+ "<class>edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner</class>"
				+ "<param name=\"param1\">50</param> \n"
				+ "<param name=\"param2\" type='int'>50 </param> \n"
				+ "</taskspawner>"
				+ "<taskspawner name='nodefaultTS'>"
				+ "<class>edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner</class>"
				+ "<param name=\"param1\">40</param> \n"
				+ "<param name=\"param2\" type='int'>4 </param> \n"
				+ "</taskspawner>" + "</taskspawners></lida>";

		Element docEle = parseDomElement(xml);
		TaskManager tm = new TaskManager(10, 10);
		Map<String, TaskSpawner> spawners = factory.getTaskSpawners(docEle, tm);
		assertEquals(2, spawners.size());
		TaskSpawner ts = (TaskSpawner) spawners.get("defaultTS");
		assertTrue(ts != null);
		assertEquals("50", ts.getParam("param1", null));
		assertEquals(50, ts.getParam("param2", null));
		ts = (TaskSpawner) spawners.get("nodefaultTS");
		assertTrue(ts != null);
		assertEquals("40", ts.getParam("param1", null));
		assertEquals(4, ts.getParam("param2", null));
	}

	@Test
	public void testGetModules() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetModule() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		List<Object[]> toInitialize = new ArrayList<Object[]>();
		String xml = "<module name='PerceptualAssociativeMemory'>"
				+ "<class>edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule</class>"
				+ "<associatedmodule>Workspace</associatedmodule>"
				+ "<param name='pam.Upscale' type='double'>.7 </param>"
				+ "<param name='pam.Downscale' type='double'>.6 </param>"
				+ "<param name='pam.Selectivity' type='double'>.5 </param>"
				+ "<param name='pam.newNodeType' type='string'>PamNodeImpl</param>"
				+ "<param name='pam.newLinkType' type='string'>PamLinkImpl</param>"
				+ "<taskspawner>defaultTS</taskspawner>"
				+ "<initializerclass>edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInitalizer</initializerclass>"
				+ "</module>";
		Map<String, TaskSpawner> taskSpawners = new HashMap<String, TaskSpawner>();
		MockTaskSpawner ts = new MockTaskSpawner();
		taskSpawners.put("defaultTS", ts);
		Element moduleElement = parseDomElement(xml);
		
		FrameworkModule module = factory.getModule(moduleElement, toAssociate,toInitialize,
				taskSpawners);
		
		assertTrue(module != null);
		assertEquals(ModuleName.PerceptualAssociativeMemory, module
				.getModuleName());
		assertEquals(ts, module.getAssistingTaskSpawner());
		assertEquals(0.7, module.getParam("pam.Upscale", null));
		assertEquals("PamNodeImpl", module.getParam("pam.newNodeType", null));
		assertEquals(module, toAssociate.get(0)[0]);
		assertEquals("Workspace", toAssociate.get(0)[1]);
		assertEquals(module, toInitialize.get(0)[0]);
		assertEquals("edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInitalizer",toInitialize.get(0)[1]);
	}

	@Test
	public void testGetTasks() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		String xml = "<module><initialTasks>"
				+ "<task name=\"cueBackground\">	"
				+ "<class>edu.memphis.ccrg.lida.workspace.CueBackgroundTask</class>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<associatedmodule>Ryan</associatedmodule>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>"
				+ "<task name=\"otherTask\">	"
				+ "<class>edu.memphis.ccrg.lida.framework.tasks.MockFrameworkTask</class>"
				+ "<ticksperrun>10</ticksperrun>"
				+ "<associatedmodule>Workspace5</associatedmodule>"
				+ "<param name=\"mock.param\"  type=\"int\">5</param>"
				+ "</task>" + "</initialTasks></module>";
		Element docEle = parseDomElement(xml);
		List<FrameworkTask> tasks = factory.getTasks(docEle, toAssociate);
		assertTrue(tasks != null);
		FrameworkTask task = tasks.get(0);
		assertTrue(task instanceof CueBackgroundTask);
		assertEquals(15, task.getTicksPerStep());
		assertEquals(task, toAssociate.get(0)[0]);
		assertEquals("Ryan", toAssociate.get(0)[1]);
		assertEquals(0.4, task.getParam("workspace.actThreshold", null));
		task = tasks.get(1);
		assertTrue(task instanceof MockFrameworkTask);
		assertEquals(10, task.getTicksPerStep());
		assertEquals(task, toAssociate.get(1)[0]);
		assertEquals("Workspace5", toAssociate.get(1)[1]);
		assertEquals(5, task.getParam("mock.param", null));
	}

	@Test
	public void testGetTask() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		String xml = "<task name=\"cueBackground\">	"
				+ "<class>edu.memphis.ccrg.lida.workspace.CueBackgroundTask</class>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<associatedmodule>Workspace5</associatedmodule>"
				+ "<associatedmodule>Ryan</associatedmodule>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>";
		Element docEle = parseDomElement(xml);
		FrameworkTask task = factory.getTask(docEle, toAssociate);
		assertTrue(task != null);
		assertTrue(task instanceof CueBackgroundTask);
		assertEquals(15, task.getTicksPerStep());
		assertEquals(task, toAssociate.get(0)[0]);
		assertEquals("Workspace5", toAssociate.get(0)[1]);
		assertEquals(task, toAssociate.get(1)[0]);
		assertEquals("Ryan", toAssociate.get(1)[1]);
		assertEquals(0.4, task.getParam("workspace.actThreshold", null));

		toAssociate.clear();
		xml = "<task name=\"cueBackground\">	"
				+ "<class>edu.memphis.ccrg.lida.workspace.CueBackgroundTask</class>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>";
		docEle = parseDomElement(xml);
		task = factory.getTask(docEle, toAssociate);
		assertTrue(task != null);
		assertTrue(task instanceof CueBackgroundTask);
		assertEquals(15, task.getTicksPerStep());
		assertEquals(0.4, task.getParam("workspace.actThreshold", null));
	}

	@Test
	public void testGetAssociatedModules() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		String xml = "<module>	"
				+ "<associatedmodule>Workspace5</associatedmodule>"
				+ "<associatedmodule>Ryan</associatedmodule>" + "</module>";
		Element docEle = parseDomElement(xml);
		Initializable initializable = new MockFrameworkTask();
		factory.getAssociatedModules(docEle, initializable, toAssociate);
		assertEquals(initializable, toAssociate.get(0)[0]);
		assertEquals("Workspace5", toAssociate.get(0)[1]);
		assertEquals(initializable, toAssociate.get(1)[0]);
		assertEquals("Ryan", toAssociate.get(1)[1]);
	}

	@Test
	public void testGetListeners() {
		String xml = "<lida>"
				+ "<listeners>"
				+ "<listener>"
				+ "<listenertype>edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener</listenertype>"
				+ "<modulename>SensoryMotorMemory</modulename>"
				+ "<listenername>SensoryMemory</listenername>"
				+ "</listener>"
				+ "<listener>"
				+ "<listenertype>edu.memphis.ccrg.lida.episodicmemory.LocalAssociationListener</listenertype>"
				+ "<modulename>TransientEpisodicMemory</modulename>"
				+ "<listenername>Workspace</listenername>"
				+ "</listener>"
				+ "<listener>"
				+ "<listenertype>edu.memphis.ccrg.lida.episodicmemory.LocalAssociationListener</listenertype>"
				+ "<modulename>TransientEpisodicMemory</modulename>"
				+ "<listenername>PerceptualAssociativeMemory</listenername>"
				+ "</listener>" + "</listeners>" + "</lida>";

		Element docEle = parseDomElement(xml);
		FrameworkModule topModule = new MockFrameworkModule();
		MockFrameworkModule smm = new MockFrameworkModule();
		smm.setModuleName(ModuleName.SensoryMotorMemory);
		topModule.addSubModule(smm);

		MockFrameworkModule tem = new MockFrameworkModule();
		tem.setModuleName(ModuleName.TransientEpisodicMemory);
		topModule.addSubModule(tem);

		FrameworkModule listener = new MockSMListener();
		listener.setModuleName(ModuleName.SensoryMemory);
		topModule.addSubModule(listener);

		FrameworkModule listener2 = new MockSMListener();
		listener2.setModuleName(ModuleName.Workspace);
		topModule.addSubModule(listener2);

		FrameworkModule listener3 = new MockSMListener();
		listener3.setModuleName(ModuleName.PerceptualAssociativeMemory);
		topModule.addSubModule(listener3);

		factory.getListeners(docEle, topModule);
		assertTrue(!smm.listeners.isEmpty());
		assertEquals(listener, smm.listeners.get(0));
		assertTrue(!tem.listeners.isEmpty());
		assertEquals(2, tem.listeners.size());
		assertEquals(listener2, tem.listeners.get(0));
		assertEquals(listener3, tem.listeners.get(1));
	}

	@Test
	public void testGetListener1() {
		String xml = "<listener>"
				+ "<listenertype>edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener</listenertype>"
				+ "<modulename>SensoryMotorMemory</modulename>"
				+ "<listenername>SensoryMemory</listenername>" + "</listener>";
		Element docEle = parseDomElement(xml);
		FrameworkModule topModule = new MockFrameworkModule();
		MockFrameworkModule smm = new MockFrameworkModule();
		smm.setModuleName(ModuleName.SensoryMotorMemory);
		FrameworkModule listener = new MockSMListener();
		listener.setModuleName(ModuleName.SensoryMemory);
		topModule.addSubModule(smm);
		topModule.addSubModule(listener);

		factory.getListener(docEle, topModule);
		assertTrue(!smm.listeners.isEmpty());
		assertEquals(listener, smm.listeners.get(0));
	}

	@Test
	public void testGetListener2() {
		String xml = "<listener>"
				+ "<listenertype>edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener</listenertype>"
				+ "<modulename>SensoryMotorMemory</modulename>"
				+ "<listenername>SensoryMemory</listenername>" + "</listener>";
		Element docEle = parseDomElement(xml);
		FrameworkModule topModule = new MockFrameworkModule();
		MockFrameworkModule smm = new MockFrameworkModule();
		smm.setModuleName(ModuleName.Environment);
		FrameworkModule listener = new MockSMListener();
		listener.setModuleName(ModuleName.SensoryMemory);
		topModule.addSubModule(smm);
		topModule.addSubModule(listener);

		factory.getListener(docEle, topModule);
		assertTrue(smm.listeners.isEmpty());
	}

	@Test
	public void testGetListener3() {
		String xml = "<listener>"
				+ "<listenertype>edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener</listenertype>"
				+ "<modulename>SensoryMotorMemory</modulename>"
				+ "<listenername>SensoryMemory</listenername>" + "</listener>";
		Element docEle = parseDomElement(xml);
		FrameworkModule topModule = new MockFrameworkModule();
		MockFrameworkModule smm = new MockFrameworkModule();
		smm.setModuleName(ModuleName.SensoryMotorMemory);
		FrameworkModule listener = new MockSMListener();
		listener.setModuleName(ModuleName.SensoryMemory);
		topModule.addSubModule(smm);

		factory.getListener(docEle, topModule);
		assertTrue(smm.listeners.isEmpty());
	}

	@Test
	public void testGetListener4() {
		String xml = "<listener>"
				+ "<listenertype>edu.memphis.ccrg.lida.actionselection.ActionSelectionListener</listenertype>"
				+ "<modulename>SensoryMotorMemory</modulename>"
				+ "<listenername>SensoryMemory</listenername>" + "</listener>";
		Element docEle = parseDomElement(xml);
		FrameworkModule topModule = new MockFrameworkModule();
		MockFrameworkModule smm = new MockFrameworkModule();
		smm.setModuleName(ModuleName.SensoryMotorMemory);
		FrameworkModule listener = new MockSMListener();
		listener.setModuleName(ModuleName.SensoryMemory);
		topModule.addSubModule(smm);
		topModule.addSubModule(listener);

		factory.getListener(docEle, topModule);
		assertTrue(smm.listeners.isEmpty());
	}

	@Test
	public void testGetListener5() {
		String xml = "<listener>" + "<listenertype>errorname</listenertype>"
				+ "<modulename>SensoryMotorMemory</modulename>"
				+ "<listenername>SensoryMemory</listenername>" + "</listener>";
		Element docEle = parseDomElement(xml);
		FrameworkModule topModule = new MockFrameworkModule();
		MockFrameworkModule smm = new MockFrameworkModule();
		smm.setModuleName(ModuleName.SensoryMotorMemory);
		FrameworkModule listener = new MockSMListener();
		listener.setModuleName(ModuleName.SensoryMemory);
		topModule.addSubModule(smm);
		topModule.addSubModule(listener);

		factory.getListener(docEle, topModule);
		assertTrue(smm.listeners.isEmpty());
	}

	@Test
	public void testAssociateModules() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitializeModules() {
		fail("Not yet implemented");
	}

	private class MockSMListener extends MockFrameworkModule implements
			SensoryMotorMemoryListener, LocalAssociationListener {

		public void receiveActuatorCommand(Object command) {
		}

		@Override
		public void receiveLocalAssociation(NodeStructure association) {

		}

	}
}
