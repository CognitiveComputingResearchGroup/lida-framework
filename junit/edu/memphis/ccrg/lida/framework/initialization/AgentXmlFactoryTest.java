/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.initialization;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.memphis.ccrg.lida.episodicmemory.LocalAssociationListener;
import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.AgentImpl;
import edu.memphis.ccrg.lida.framework.FrameworkModule;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.AgentXmlFactory.TaskData;
import edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule;
import edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer;
import edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer2;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.MockFrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.ns.PerceptualAssociativeMemoryNS;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener;
import edu.memphis.ccrg.lida.workspace.Workspace;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl;

public class AgentXmlFactoryTest {

	private AgentXmlFactory factory;
	private double epsilon = 10e-9;

	@Before
	public void setUp() throws Exception {
		factory = new AgentXmlFactory();
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * @param xml
	 * @return
	 */
	private Element parseDomElement(String xml) {
		Document dom = XmlUtils.parseXmlString(xml);
		Element docEle = dom.getDocumentElement();
		return docEle;
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
		TaskManager tm = new TaskManager(10, 10,-1,null);
		Map<String, TaskSpawner> spawners = new HashMap<String, TaskSpawner>();
		factory.getTaskSpawner(docEle, tm, spawners);
		assertEquals(1, spawners.size());
		TaskSpawner ts = (TaskSpawner) spawners.get("defaultTS");
		assertTrue(ts != null);
		assertEquals("50", ts.getParam("param1", ""));
		assertEquals(50, (int) ts.getParam("param2", 0));
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
		TaskManager tm = new TaskManager(10, 10,-1,null);
		Map<String, TaskSpawner> spawners = factory.getTaskSpawners(docEle, tm);
		assertEquals(2, spawners.size());
		TaskSpawner ts = (TaskSpawner) spawners.get("defaultTS");
		assertTrue(ts != null);
		assertEquals("50", ts.getParam("param1", ""));
		assertEquals(50, (int) ts.getParam("param2", 0));
		ts = (TaskSpawner) spawners.get("nodefaultTS");
		assertTrue(ts != null);
		assertEquals("40", ts.getParam("param1", ""));
		assertEquals(4, (int) ts.getParam("param2", 0));
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
		// TODO fix test
		List<AgentXmlFactory.TaskData> toRun = new ArrayList<AgentXmlFactory.TaskData>();

		FrameworkModule module = factory.getModule(moduleElement, toAssociate,
				toInitialize, taskSpawners, toRun);

		assertTrue(module != null);
		assertEquals(ModuleName.PerceptualAssociativeMemory, module
				.getModuleName());
		assertEquals(ts, module.getAssistingTaskSpawner());
		assertEquals(0.7, module.getParam("pam.Upscale", 0.0), epsilon);
		assertEquals("PamNodeImpl", module.getParam("pam.newNodeType", ""));
		assertEquals(module, toAssociate.get(0)[0]);
		assertEquals("Workspace", toAssociate.get(0)[1]);
		assertEquals(module, toInitialize.get(0)[0]);
		assertEquals(
				"edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInitalizer",
				toInitialize.get(0)[1]);
	}

	@Test
	public void testGetModule0() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		List<Object[]> toInitialize = new ArrayList<Object[]>();
		String xml = "<module name='PerceptualAssociativeMemory'>"
				+ "<class>edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule</class>"
				+ "<submodules>"
				+ "<module name=\"NewEpisodicBuffer\">"
				+ "<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>"
				+ "<param name=\"eParam\" type=\"double\">0.01 </param>"
				+ "<taskspawner>fancyTS</taskspawner>"
				+ "</module>"
				+ "<module name=\"NewPerceptualBuffer\">"
				+ "<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>"
				+ "<param name=\"pParam\" type=\"double\">0.02 </param>"
				+ "<taskspawner>superFancyTS</taskspawner>"
				+ "<initialTasks>"
				+ "<task name=\"updateCsmBackground\">"
				+ "<tasktype>UpdateCsmBackgroundTask</tasktype>"
				+ "<ticksperrun>55</ticksperrun>"
				+ "</task>"
				+ "</initialTasks>"
				+ "<initializerclass>edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer</initializerclass>"
				+ "</module>"
				+ "</submodules>"
				+ "<associatedmodule>Apple</associatedmodule>"
				+ "<param name='pam.Upscale' type='double'>.7 </param>"
				+ "<param name='pam.Downscale' type='double'>.6 </param>"
				+ "<param name='pam.Selectivity' type='double'>.5 </param>"
				+ "<param name='pam.newNodeType' type='string'>PamNodeImpl</param>"
				+ "<param name='pam.newLinkType' type='string'>PamLinkImpl</param>"
				+ "<taskspawner>defaultTS</taskspawner>"
				+ "<initialTasks>"
				+ "<task name=\"cueBackground\">"
				+ "<tasktype>CueBackgroundTask</tasktype>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<param name=\"taskParameter\"  type=\"double\">0.4</param>	"
				+ "</task>"
				+ "<task name=\"fooBar\">"
				+ "<tasktype>MockFrameworkTask</tasktype>"
				+ "<ticksperrun>5</ticksperrun>"
				+ "</task>"
				+ "</initialTasks>"

				+ "<initializerclass>edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInitalizer</initializerclass>"
				+ "</module>";
		Map<String, TaskSpawner> taskSpawners = new HashMap<String, TaskSpawner>();
		MockTaskSpawner ts = new MockTaskSpawner();
		MockTaskSpawner fancyts = new MockTaskSpawner();
		MockTaskSpawner superFancyTS = new MockTaskSpawner();
		taskSpawners.put("defaultTS", ts);
		taskSpawners.put("fancyTS", fancyts);
		taskSpawners.put("superFancyTS", superFancyTS);
		Element moduleElement = parseDomElement(xml);

		List<AgentXmlFactory.TaskData> toRun = new ArrayList<AgentXmlFactory.TaskData>();

		// **Code being tested**
		FrameworkModule module = factory.getModule(moduleElement, toAssociate,
				toInitialize, taskSpawners, toRun);

		// **Verification**
		// Main Module attributes
		assertTrue(module != null);
		assertEquals(ModuleName.PerceptualAssociativeMemory, module
				.getModuleName());
		assertEquals(ts, module.getAssistingTaskSpawner());

		assertEquals(0.7, module.getParam("pam.Upscale", 0.0), epsilon);
		assertEquals("PamNodeImpl", module.getParam("pam.newNodeType", ""));
		assertEquals(5, (int) module.getParam("eParam", 5));

		// Submodules
		FrameworkModule eBuffer = module.getSubmodule(ModuleName
				.getModuleName("NewEpisodicBuffer"));
		assertTrue(eBuffer instanceof WorkspaceBufferImpl);
		assertEquals(0.01, eBuffer.getParam("eParam", 0.0), epsilon);
		assertEquals(999, (int) eBuffer.getParam("pam.Upscale", 999));
		assertEquals(fancyts, eBuffer.getAssistingTaskSpawner());

		FrameworkModule pBuffer = module.getSubmodule(ModuleName
				.getModuleName("NewPerceptualBuffer"));
		assertTrue(pBuffer instanceof WorkspaceBufferImpl);
		assertEquals(0.02, pBuffer.getParam("pParam", 0.0), epsilon);
		assertEquals(999, (int) pBuffer.getParam("pam.Upscale", 999));
		assertEquals(superFancyTS, pBuffer.getAssistingTaskSpawner());

		assertEquals(pBuffer, toInitialize.get(0)[0]);
		assertEquals(
				"edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer",
				toInitialize.get(0)[1]);
		assertEquals(module, toInitialize.get(1)[0]);
		assertEquals(
				"edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInitalizer",
				toInitialize.get(1)[1]);

		// Tasks
		// Collection<FrameworkTask> outerTasks =
		// module.getAssistingTaskSpawner().getRunningTasks();
		// assertEquals(2, outerTasks.size());
		// FrameworkTask cueBackgroundTask = ts.tasks.get(0);
		// assertTrue(cueBackgroundTask instanceof CueBackgroundTask);
		// assertEquals(15, cueBackgroundTask.getTicksPerRun());
		// assertEquals(0.4, cueBackgroundTask.getParam("taskParameter", 0.0),
		// epsilon);
		// assertEquals(5, cueBackgroundTask.getParam("pam.Selectivity", 5));
		// FrameworkTask mockTask = ts.tasks.get(1);
		// assertTrue(mockTask instanceof MockFrameworkTask);
		// assertEquals(5, mockTask.getTicksPerRun());
		// assertEquals(10, mockTask.getParam("taskParameter", 10));
		// assertEquals(5, mockTask.getParam("pam.Selectivity", 5));

		// Collection<FrameworkTask> noInnerTasks =
		// eBuffer.getAssistingTaskSpawner().getRunningTasks();
		// assertEquals(0, noInnerTasks.size());
		//		
		// Collection<FrameworkTask> innerTasks =
		// pBuffer.getAssistingTaskSpawner().getRunningTasks();
		// assertEquals(1, innerTasks.size());
		// FrameworkTask updateCsmTask = superFancyTS.tasks.get(0);
		// assertTrue(updateCsmTask instanceof UpdateCsmBackgroundTask);
		// assertEquals(55, updateCsmTask.getTicksPerRun());

		AgentXmlFactory.TaskData td = toRun.get(0);
		assertEquals("cueBackground", td.name);
		assertEquals("CueBackgroundTask", td.tasktype);
		assertEquals(15, td.ticksPerRun);
		assertEquals(ts, td.taskSpawner);

		td = toRun.get(1);
		assertEquals("fooBar", td.name);
		assertEquals("MockFrameworkTask", td.tasktype);
		assertEquals(5, td.ticksPerRun);
		assertEquals(ts, td.taskSpawner);

		assertEquals(3, toRun.size());
		td = toRun.get(2);
		assertEquals("updateCsmBackground", td.name);
		assertEquals("UpdateCsmBackgroundTask", td.tasktype);
		assertEquals(55, td.ticksPerRun);
		assertEquals(superFancyTS, td.taskSpawner);

		// assertEquals(cueBackgroundTask, toAssociate.get(0)[0]);
		// assertEquals("Workspace", toAssociate.get(0)[1]);
		//		
		// assertEquals(mockTask, toAssociate.get(1)[0]);
		// assertEquals("Ryan", toAssociate.get(1)[1]);
		//		
		// assertEquals(updateCsmTask, toAssociate.get(2)[0]);
		// assertEquals("GlobalWorkspace", toAssociate.get(2)[1]);
		//		
		// assertEquals(updateCsmTask, toAssociate.get(3)[0]);
		// assertEquals("EpisodicMemory", toAssociate.get(3)[1]);
		//		
		// Associations
		assertEquals(module, toAssociate.get(0)[0]);
		assertEquals("Apple", toAssociate.get(0)[1]);
	}

	@Test
	public void testGetModule1() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		List<Object[]> toInitialize = new ArrayList<Object[]>();
		String xml = "<module name='PerceptualAssociativeMemory'>"
				+ "<class>edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule</class>"
				+ "<submodules>"
				+ "<module name=\"NewEpisodicBuffer\">"
				+ "<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>"
				+ "<submodules>"
				+ "<module name=\"NewPerceptualBuffer\">"
				+ "<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>"
				+ "<param name=\"pParam\" type=\"double\">0.02 </param>"
				+ "<taskspawner>superFancyTS</taskspawner>"
				+ "<initialTasks>"
				+ "<task name=\"updateCsmBackground\">"
				+ "<tasktype>UpdateCsmBackgroundTask</tasktype>"
				+ "<ticksperrun>55</ticksperrun>"
				+ "</task>"
				+ "</initialTasks>"
				+ "<initializerclass>edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer</initializerclass>"
				+ "</module>"
				+ "</submodules>"
				+ "<param name=\"eParam\" type=\"double\">0.01 </param>"
				+ "<taskspawner>fancyTS</taskspawner>"
				+ "</module>"
				+ "</submodules>"
				+ "<associatedmodule>Apple</associatedmodule>"
				+ "<param name='pam.Upscale' type='double'>.7 </param>"
				+ "<param name='pam.Downscale' type='double'>.6 </param>"
				+ "<param name='pam.Selectivity' type='double'>.5 </param>"
				+ "<param name='pam.newNodeType' type='string'>PamNodeImpl</param>"
				+ "<param name='pam.newLinkType' type='string'>PamLinkImpl</param>"
				+ "<taskspawner>defaultTS</taskspawner>"
				+ "<initialTasks>"
				+ "<task name=\"cueBackground\">"
				+ "<tasktype>CueBackgroundTask</tasktype>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<param name=\"taskParameter\"  type=\"double\">0.4</param>	"
				+ "</task>"
				+ "<task name=\"fooBar\">"
				+ "<tasktype>MockFrameworkTask</tasktype>"
				+ "<ticksperrun>5</ticksperrun>"
				+ "</task>"
				+ "</initialTasks>"

				+ "<initializerclass>edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInitalizer</initializerclass>"
				+ "</module>";
		Map<String, TaskSpawner> taskSpawners = new HashMap<String, TaskSpawner>();
		MockTaskSpawner ts = new MockTaskSpawner();
		MockTaskSpawner fancyts = new MockTaskSpawner();
		MockTaskSpawner superFancyTS = new MockTaskSpawner();
		taskSpawners.put("defaultTS", ts);
		taskSpawners.put("fancyTS", fancyts);
		taskSpawners.put("superFancyTS", superFancyTS);
		Element moduleElement = parseDomElement(xml);

		List<AgentXmlFactory.TaskData> toRun = new ArrayList<AgentXmlFactory.TaskData>();

		// **Code being tested**
		FrameworkModule module = factory.getModule(moduleElement, toAssociate,
				toInitialize, taskSpawners, toRun);

		// **Verification**
		// Main Module attributes
		assertTrue(module != null);
		assertEquals(ModuleName.PerceptualAssociativeMemory, module
				.getModuleName());
		assertEquals(ts, module.getAssistingTaskSpawner());

		assertEquals(0.7, module.getParam("pam.Upscale", 0.0), epsilon);
		assertEquals("PamNodeImpl", module.getParam("pam.newNodeType", ""));
		assertEquals(5, (int) module.getParam("eParam", 5));

		// Submodules
		FrameworkModule eBuffer = module.getSubmodule(ModuleName
				.getModuleName("NewEpisodicBuffer"));
		assertTrue(eBuffer instanceof WorkspaceBufferImpl);
		assertEquals(0.01, eBuffer.getParam("eParam", 0.0), epsilon);
		assertEquals(999, (int) eBuffer.getParam("pam.Upscale", 999));
		assertEquals(fancyts, eBuffer.getAssistingTaskSpawner());

		FrameworkModule pBuffer = module.getSubmodule(ModuleName
				.getModuleName("NewPerceptualBuffer"));
		assertEquals(null, pBuffer);

		pBuffer = eBuffer.getSubmodule(ModuleName
				.getModuleName("NewPerceptualBuffer"));
		assertTrue(pBuffer instanceof WorkspaceBufferImpl);
		assertEquals(0.02, pBuffer.getParam("pParam", 0.0), epsilon);
		assertEquals(999, (int) pBuffer.getParam("pam.Upscale", 999));
		assertEquals(superFancyTS, pBuffer.getAssistingTaskSpawner());

		assertEquals(pBuffer, toInitialize.get(0)[0]);
		assertEquals(
				"edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer",
				toInitialize.get(0)[1]);
		assertEquals(module, toInitialize.get(1)[0]);
		assertEquals(
				"edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInitalizer",
				toInitialize.get(1)[1]);

		// Tasks

		AgentXmlFactory.TaskData td = toRun.get(0);
		assertEquals("cueBackground", td.name);
		assertEquals("CueBackgroundTask", td.tasktype);
		assertEquals(15, td.ticksPerRun);
		assertEquals(ts, td.taskSpawner);

		td = toRun.get(1);
		assertEquals("fooBar", td.name);
		assertEquals("MockFrameworkTask", td.tasktype);
		assertEquals(5, td.ticksPerRun);
		assertEquals(ts, td.taskSpawner);

		assertEquals(3, toRun.size());
		td = toRun.get(2);
		assertEquals("updateCsmBackground", td.name);
		assertEquals("UpdateCsmBackgroundTask", td.tasktype);
		assertEquals(55, td.ticksPerRun);
		assertEquals(superFancyTS, td.taskSpawner);

		// Associations
		assertEquals(module, toAssociate.get(0)[0]);
		assertEquals("Apple", toAssociate.get(0)[1]);
	}

	@Test
	public void testGetModules() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		List<Object[]> toInitialize = new ArrayList<Object[]>();
		String xml = "<lida><submodules> "
				+ "<module name=\"NewEpisodicBuffer\">"
				+ "<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>"
				+ "<param name=\"eParam\" type=\"double\">0.01 </param>"
				+ "<taskspawner>fancyTS</taskspawner>"
				+ "</module>"
				+

				"<module name=\"NewPerceptualBuffer\">"
				+ "<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>"
				+ "<param name=\"pParam\" type=\"double\">0.02 </param>"
				+ "<taskspawner>superFancyTS</taskspawner>"
				+ "<initialTasks>"
				+ "<task name=\"updateCsmBackground\">"
				+ "<tasktype>UpdateCsmBackgroundTask</tasktype>"
				+ "<ticksperrun>55</ticksperrun>"
				+ "</task>"
				+ "</initialTasks>"
				+ "<initializerclass>edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer</initializerclass>"
				+ "</module>" + "</submodules></lida>";

		Map<String, TaskSpawner> taskSpawners = new HashMap<String, TaskSpawner>();
		MockTaskSpawner ts = new MockTaskSpawner();
		MockTaskSpawner fancyts = new MockTaskSpawner();
		MockTaskSpawner superFancyTS = new MockTaskSpawner();
		taskSpawners.put("defaultTS", ts);
		taskSpawners.put("fancyTS", fancyts);
		taskSpawners.put("superFancyTS", superFancyTS);
		Element moduleElement = parseDomElement(xml);

		List<AgentXmlFactory.TaskData> toRun = new ArrayList<AgentXmlFactory.TaskData>();

		// **Code being tested**
		List<FrameworkModule> modules = factory.getModules(moduleElement,
				toAssociate, toInitialize, taskSpawners, toRun);

		assertEquals(2, modules.size());

		// Submodules
		FrameworkModule eBuffer = modules.get(0);
		assertTrue(eBuffer instanceof WorkspaceBufferImpl);
		assertEquals(ModuleName.getModuleName("NewEpisodicBuffer"), eBuffer
				.getModuleName());
		assertEquals(0.01, eBuffer.getParam("eParam", 0.0), epsilon);
		assertEquals(999, (int) eBuffer.getParam("pam.Upscale", 999));
		assertEquals(fancyts, eBuffer.getAssistingTaskSpawner());

		FrameworkModule pBuffer = modules.get(1);
		assertTrue(pBuffer instanceof WorkspaceBufferImpl);
		assertEquals(ModuleName.getModuleName("NewPerceptualBuffer"), pBuffer
				.getModuleName());
		assertEquals(0.02, pBuffer.getParam("pParam", 0.0), epsilon);
		assertEquals(999, (int) pBuffer.getParam("pam.Upscale", 999));
		assertEquals(superFancyTS, pBuffer.getAssistingTaskSpawner());

		assertEquals(pBuffer, toInitialize.get(0)[0]);
		assertEquals(
				"edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer",
				toInitialize.get(0)[1]);
	}

	@Test
	public void testGetTasks() {
		String xml = "<module><initialTasks>"
				+ "<defaultticksperrun>5</defaultticksperrun>"
				+ "<task name=\"cueBackground\">	"
				+ "<tasktype>CueBackgroundTask</tasktype>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>" + "<task name=\"otherTask\">	"
				+ "<tasktype>MockFrameworkTask</tasktype>"
				+ "<param name=\"mock.param\"  type=\"int\">5</param>"
				+ "</task>" + "</initialTasks></module>";

		TaskSpawner ts = new MockTaskSpawner();
		Element docEle = parseDomElement(xml);
		List<AgentXmlFactory.TaskData> toRun = factory.getTasks(docEle, ts);
		assertTrue(toRun != null);
		AgentXmlFactory.TaskData td = toRun.get(0);
		assertEquals("cueBackground", td.name);
		assertEquals("CueBackgroundTask", td.tasktype);
		assertEquals(15, td.ticksPerRun);
		assertEquals(ts, td.taskSpawner);
		assertEquals(.4, td.params.get("workspace.actThreshold"));

		td = toRun.get(1);
		assertEquals("otherTask", td.name);
		assertEquals("MockFrameworkTask", td.tasktype);
		assertEquals(5, td.ticksPerRun);
		assertEquals(ts, td.taskSpawner);
		assertEquals(5, td.params.get("mock.param"));
	}

	@Test
	public void testGetTasks2() {
		String xml = "<module><initialTasks>"
				+ "<task name=\"cueBackground\">	"
				+ "<tasktype>CueBackgroundTask</tasktype>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>" + "<task name=\"otherTask\">	"
				+ "<tasktype>MockFrameworkTask</tasktype>"
				+ "<ticksperrun>10</ticksperrun>"
				+ "<param name=\"mock.param\"  type=\"int\">5</param>"
				+ "</task>" + "</initialTasks></module>";

		TaskSpawner ts = new MockTaskSpawner();
		Element docEle = parseDomElement(xml);
		List<AgentXmlFactory.TaskData> toRun = factory.getTasks(docEle, ts);
		assertTrue(toRun != null);
		AgentXmlFactory.TaskData td = toRun.get(0);
		assertEquals("cueBackground", td.name);
		assertEquals("CueBackgroundTask", td.tasktype);
		assertEquals(15, td.ticksPerRun);
		assertEquals(ts, td.taskSpawner);
		assertEquals(.4, td.params.get("workspace.actThreshold"));

		td = toRun.get(1);
		assertEquals("otherTask", td.name);
		assertEquals("MockFrameworkTask", td.tasktype);
		assertEquals(10, td.ticksPerRun);
		assertEquals(ts, td.taskSpawner);
		assertEquals(5, td.params.get("mock.param"));
	}

	@Test
	public void testGetTask() {
		String xml = "<task name=\"cueBackground\">	"
				+ "<tasktype>CueBackgroundTask</tasktype>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>";
		Element docEle = parseDomElement(xml);

		AgentXmlFactory.TaskData td = factory.getTask(docEle, null);

		assertEquals("cueBackground", td.name);
		assertEquals("CueBackgroundTask", td.tasktype);
		assertEquals(15, td.ticksPerRun);
		assertNull(td.taskSpawner);
		assertEquals(.4, td.params.get("workspace.actThreshold"));

	}

	@Test
	public void testGetTask2() {
		String xml = "<task name=\"cueBackground\">	"
				+ "<tasktype>CueBackgroundTask</tasktype>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>";
		Element docEle = parseDomElement(xml);

		AgentXmlFactory.TaskData td = factory.getTask(docEle, 10);

		assertEquals("cueBackground", td.name);
		assertEquals("CueBackgroundTask", td.tasktype);
		assertEquals(10, td.ticksPerRun);
		assertNull(td.taskSpawner);
		assertEquals(.4, td.params.get("workspace.actThreshold"));

	}

	@Test
	public void testGetTask3() {
		String xml = "<task name=\"cueBackground\">	"
				+ "<tasktype>CueBackgroundTask</tasktype>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>";
		Element docEle = parseDomElement(xml);

		AgentXmlFactory.TaskData td = factory.getTask(docEle, 10);

		assertEquals("cueBackground", td.name);
		assertEquals("CueBackgroundTask", td.tasktype);
		assertEquals(15, td.ticksPerRun);
		assertNull(td.taskSpawner);
		assertEquals(.4, td.params.get("workspace.actThreshold"));

	}

	@Test
	public void testGetTasksDefaultTicksPerRun() {
		String xml = "<initialTasks>"
				+ "<defaultticksperrun>5</defaultticksperrun>"
				+ "<task name=\"cueBackground\">	"
				+ "<tasktype>CueBackgroundTask</tasktype>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>" + "<task name=\"otherTask\">	"
				+ "<tasktype>MockFrameworkTask</tasktype>"
				+ "<ticksperrun>10</ticksperrun>"
				+ "<param name=\"mock.param\"  type=\"int\">5</param>"
				+ "</task>" + "</initialTasks>";

		Element docEle = parseDomElement(xml);
		int tpr = factory.getTasksDefaultTicksPerRun(docEle);
		assertEquals(5, tpr);
	}

	@Test
	public void testGetTasksDefaultTicksPerRun2() {
		String xml = "<initialTasks>"
				+ "<task name=\"cueBackground\">	"
				+ "<tasktype>CueBackgroundTask</tasktype>"
				+ "<ticksperrun>15</ticksperrun>"
				+ "<param name=\"workspace.actThreshold\"  type=\"double\">0.4</param>"
				+ "</task>" + "<task name=\"otherTask\">	"
				+ "<tasktype>MockFrameworkTask</tasktype>"
				+ "<ticksperrun>10</ticksperrun>"
				+ "<param name=\"mock.param\"  type=\"int\">5</param>"
				+ "</task>" + "</initialTasks>";

		Element docEle = parseDomElement(xml);
		int tpr = factory.getTasksDefaultTicksPerRun(docEle);
		assertEquals(0, tpr);
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
		MockFrameworkModule topModule = new MockFrameworkModule();

		MockFrameworkModule sub1 = new MockFrameworkModule();
		sub1.setModuleName(ModuleName.ActionSelection);
		topModule.addSubModule(sub1);

		MockFrameworkModule sub2 = new MockFrameworkModule();
		sub2.setModuleName(ModuleName.addModuleName("TestName"));
		topModule.addSubModule(sub2);

		MockFrameworkModule sub3 = new MockFrameworkModule();
		sub3.setModuleName(ModuleName.EpisodicBuffer);
		topModule.addSubModule(sub3);

		MockFrameworkTask task1 = new MockFrameworkTask();

		List<Object[]> toAssoc = new ArrayList<Object[]>();

		toAssoc.add(new Object[] { sub1, "TestName", null });
		toAssoc.add(new Object[] { task1, "EpisodicBuffer",
				ModuleUsage.TO_READ_FROM });

		factory.associateModules(toAssoc, topModule);

		assertEquals(sub2, sub1.associatedModule);
		assertEquals(ModuleUsage.NOT_SPECIFIED, sub1.moduleUsage);

		assertEquals(sub3, task1.associatedModule);
		assertEquals(ModuleUsage.TO_READ_FROM, task1.moduleUsage);
	}

	@Test
	public void testInitializeModules() {
		Agent a = new AgentImpl(null);
		MockFrameworkModule module1 = new MockFrameworkModule();
		// module1.setModuleName(ModuleName.ActionSelection);
		MockFrameworkModule module2 = new MockFrameworkModule();
		// module2.setModuleName(ModuleName.CurrentSituationalModel);

		Map<String, Object> p1 = new HashMap<String, Object>();
		p1.put("arg0", 10.0);
		p1.put("name", "Javier");

		Map<String, Object> p2 = new HashMap<String, Object>();
		p2.put("arg1", 20.0);
		p2.put("name1", "Ryan");

		List<Object[]> toInit = new ArrayList<Object[]>();
		toInit.add(new Object[] { module1,
				MockInitializer.class.getCanonicalName(), p1 });
		toInit.add(new Object[] { module2,
				MockInitializer2.class.getCanonicalName(), p2 });

		factory.initializeModules(a, toInit);

		assertEquals(module1, MockInitializer.module);
		assertEquals(a, MockInitializer.agent);
		assertEquals(10.0, MockInitializer.params.get("arg0"));
		assertEquals("Javier", MockInitializer.params.get("name"));

		assertEquals(module2, MockInitializer2.module);
		assertEquals(a, MockInitializer2.agent);
		assertEquals(20.0, MockInitializer2.params.get("arg1"));
		assertEquals("Ryan", MockInitializer2.params.get("name1"));
	}

	@Test
	public void testGetAgent() {
		Properties p = new Properties();
		p.setProperty("lida.agentdata", "testData/shortagent.xml");

		Agent a = factory.getAgent(p);

		assertNotNull(a);
		FrameworkModule workspace = a.getSubmodule(ModuleName.Workspace);
		assertTrue(workspace instanceof Workspace);
		FrameworkModule pam = a
				.getSubmodule(ModuleName.PerceptualAssociativeMemory);
		assertTrue(pam instanceof PerceptualAssociativeMemoryNS);
		TaskManager tm = a.getTaskManager();
		assertNotNull(tm);
		assertEquals(50, tm.getTickDuration());

		p.setProperty("lida.agentdata", "testDatasdfgwe3/shortagent.xml");
		a = factory.getAgent(p);
		assertEquals(null, a);

		p.setProperty("lida.agentdata", "testData/agentbad.xml");
		a = factory.getAgent(p);
		assertEquals(null, a);

		p = new Properties();
		a = factory.getAgent(p);

		assertNotNull(a);
		workspace = a.getSubmodule(ModuleName.Workspace);
		assertTrue(workspace instanceof Workspace);
		pam = a.getSubmodule(ModuleName.PerceptualAssociativeMemory);
		assertTrue(pam instanceof PerceptualAssociativeMemoryNS);
		tm = a.getTaskManager();
		assertNotNull(tm);
		assertEquals(50, tm.getTickDuration());
	}

	@Test
	public void testParseDocument() {
		Document dom = XmlUtils
				.parseXmlFile("testData/shortagent.xml",
						"edu/memphis/ccrg/lida/framework/initialization/config/LidaXMLSchema.xsd");
		assertNotNull(dom);

		Agent a = factory.parseDocument(dom);

		assertNotNull(a);
		FrameworkModule workspace = a.getSubmodule(ModuleName.Workspace);
		assertTrue(workspace instanceof Workspace);
		FrameworkModule pam = a
				.getSubmodule(ModuleName.PerceptualAssociativeMemory);
		assertTrue(pam instanceof PerceptualAssociativeMemoryNS);
		TaskManager tm = a.getTaskManager();

		assertNotNull(tm);
		assertEquals(50, tm.getTickDuration());
	}

	@Test
	public void testInitializeTasks() {
		ElementFactory elFact = ElementFactory.getInstance();
		FrameworkTaskDef taskDef = new FrameworkTaskDef(
				"edu.memphis.ccrg.lida.framework.tasks.MockFrameworkTask", 1,
				new HashMap<String, String>(), "mockTask",
				new HashMap<String, Object>(),
				new HashMap<ModuleName, String>());
		elFact.addFrameworkTaskType(taskDef);
		List<TaskData> toRun = new ArrayList<TaskData>();
		Map<ModuleName, FrameworkModule> modulesMap = new HashMap<ModuleName, FrameworkModule>();

		MockTaskSpawner ts1 = new MockTaskSpawner();
		TaskData td = new TaskData("task1", "mockTask", 5, null);
		td.taskSpawner = ts1;
		toRun.add(td);

		MockTaskSpawner ts2 = new MockTaskSpawner();
		td = new TaskData("task2", "mockTask", 6, null);
		td.taskSpawner = ts2;
		toRun.add(td);

		factory.initializeTasks(null, toRun);

		assertEquals(1, ts1.tasks.size());
		assertEquals(5, ts1.tasks.get(0).getTicksPerRun());

		assertEquals(1, ts2.tasks.size());
		assertEquals(6, ts2.tasks.get(0).getTicksPerRun());
	}

	private class MockSMListener extends MockFrameworkModule implements
			SensoryMotorMemoryListener, LocalAssociationListener {

		@Override
		public void receiveActuatorCommand(Object command) {
		}

		@Override
		public void receiveLocalAssociation(NodeStructure association) {

		}

	}
}
