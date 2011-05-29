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
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
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
import edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule;
import edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer;
import edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer2;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.MockFrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemory;
import edu.memphis.ccrg.lida.sensorymotormemory.SensoryMotorMemoryListener;
import edu.memphis.ccrg.lida.workspace.CueBackgroundTask;
import edu.memphis.ccrg.lida.workspace.UpdateCsmBackgroundTask;
import edu.memphis.ccrg.lida.workspace.Workspace;
import edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl;

public class AgentXmlFactoryTest {

	private AgentXmlFactory factory;

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
	public void testGetModule0() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		List<Object[]> toInitialize = new ArrayList<Object[]>();
		String xml = "<module name='PerceptualAssociativeMemory'>"
				+ "<class>edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule</class>"
				+ "<submodules>" 
				+		"<module name=\"NewEpisodicBuffer\">" +
						"<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>" +
						"<param name=\"eParam\" type=\"double\">0.01 </param>" +
						"<taskspawner>fancyTS</taskspawner>" +
						"</module>" +						
						"<module name=\"NewPerceptualBuffer\">" +
						"<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>" +
						"<param name=\"pParam\" type=\"double\">0.02 </param>" +
						"<taskspawner>superFancyTS</taskspawner>" +
						"<initialTasks>" +
						"<task name=\"updateCsmBackground\">" +
						"<class>edu.memphis.ccrg.lida.workspace.UpdateCsmBackgroundTask</class>" +
						"<ticksperrun>55</ticksperrun>" +
						"<associatedmodule>GlobalWorkspace</associatedmodule>" +
						"<associatedmodule>EpisodicMemory</associatedmodule>" +
						"</task>" +
						"</initialTasks>" +						
						"<initializerclass>edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer</initializerclass>"+
						"</module>" 
				+ "</submodules>"		
				+ "<associatedmodule>Apple</associatedmodule>"
				+ "<param name='pam.Upscale' type='double'>.7 </param>"
				+ "<param name='pam.Downscale' type='double'>.6 </param>"
				+ "<param name='pam.Selectivity' type='double'>.5 </param>"
				+ "<param name='pam.newNodeType' type='string'>PamNodeImpl</param>"
				+ "<param name='pam.newLinkType' type='string'>PamLinkImpl</param>"
				+ "<taskspawner>defaultTS</taskspawner>"
				+"<initialTasks>" +
						"<task name=\"cueBackground\">" +
						"<class>edu.memphis.ccrg.lida.workspace.CueBackgroundTask</class>" +
						"<ticksperrun>15</ticksperrun>" +
						"<associatedmodule>Workspace</associatedmodule>" +
						"<param name=\"taskParameter\"  type=\"double\">0.4</param>	" +
						"</task>" +
						"<task name=\"fooBar\">" +
						"<class>edu.memphis.ccrg.lida.framework.tasks.MockFrameworkTask</class>" +
						"<ticksperrun>5</ticksperrun>" +
						"<associatedmodule>Ryan</associatedmodule>" +
						"</task>" +
						"</initialTasks>"
				
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
		
		//**Code being tested**
		FrameworkModule module = factory.getModule(moduleElement, toAssociate, toInitialize,
				taskSpawners);
		
		//**Verification**
		//Main Module attributes
		assertTrue(module != null);
		assertEquals(ModuleName.PerceptualAssociativeMemory, module.getModuleName());
		assertEquals(ts, module.getAssistingTaskSpawner());
		
		assertEquals(0.7, module.getParam("pam.Upscale", null));
		assertEquals("PamNodeImpl", module.getParam("pam.newNodeType", null));
		assertEquals(5, module.getParam("eParam", 5));
		
		//Submodules
		FrameworkModule eBuffer = module.getSubmodule(ModuleName.getModuleName("NewEpisodicBuffer"));
		assertTrue(eBuffer instanceof WorkspaceBufferImpl);
		assertEquals(0.01, eBuffer.getParam("eParam", null));
		assertEquals(999, eBuffer.getParam("pam.Upscale", 999));
		assertEquals(fancyts, eBuffer.getAssistingTaskSpawner());
		
		FrameworkModule pBuffer = module.getSubmodule(ModuleName.getModuleName("NewPerceptualBuffer"));
		assertTrue(pBuffer instanceof WorkspaceBufferImpl);
		assertEquals(0.02, pBuffer.getParam("pParam", null));
		assertEquals(999, pBuffer.getParam("pam.Upscale", 999));
		assertEquals(superFancyTS, pBuffer.getAssistingTaskSpawner());
		
		assertEquals(pBuffer, toInitialize.get(0)[0]);
		assertEquals("edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer",toInitialize.get(0)[1]);
		assertEquals(module, toInitialize.get(1)[0]);
		assertEquals("edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInitalizer",toInitialize.get(1)[1]);
		
		//Tasks
		Collection<FrameworkTask> outerTasks = module.getAssistingTaskSpawner().getRunningTasks();
		assertEquals(2, outerTasks.size());
		FrameworkTask cueBackgroundTask = ts.tasks.get(0);
		assertTrue(cueBackgroundTask instanceof CueBackgroundTask);
		assertEquals(15, cueBackgroundTask.getTicksPerStep());
		assertEquals(0.4, cueBackgroundTask.getParam("taskParameter", null));
		assertEquals(5, cueBackgroundTask.getParam("pam.Selectivity", 5));
		FrameworkTask mockTask = ts.tasks.get(1);
		assertTrue(mockTask instanceof MockFrameworkTask);
		assertEquals(5, mockTask.getTicksPerStep());
		assertEquals(10, mockTask.getParam("taskParameter", 10));
		assertEquals(5, mockTask.getParam("pam.Selectivity", 5));
		
		Collection<FrameworkTask> noInnerTasks = eBuffer.getAssistingTaskSpawner().getRunningTasks();
		assertEquals(0, noInnerTasks.size());	
		
		Collection<FrameworkTask> innerTasks = pBuffer.getAssistingTaskSpawner().getRunningTasks();
		assertEquals(1, innerTasks.size());	
		FrameworkTask updateCsmTask = superFancyTS.tasks.get(0);
		assertTrue(updateCsmTask instanceof UpdateCsmBackgroundTask);
		assertEquals(55, updateCsmTask.getTicksPerStep());
		
		//Associations
		assertEquals(cueBackgroundTask, toAssociate.get(0)[0]);
		assertEquals("Workspace", toAssociate.get(0)[1]);
		
		assertEquals(mockTask, toAssociate.get(1)[0]);
		assertEquals("Ryan", toAssociate.get(1)[1]);
		
		assertEquals(updateCsmTask, toAssociate.get(2)[0]);
		assertEquals("GlobalWorkspace", toAssociate.get(2)[1]);
		
		assertEquals(updateCsmTask, toAssociate.get(3)[0]);
		assertEquals("EpisodicMemory", toAssociate.get(3)[1]);
		
		assertEquals(module, toAssociate.get(4)[0]);
		assertEquals("Apple", toAssociate.get(4)[1]);				
	}	

	@Test
	public void testGetModule1() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		List<Object[]> toInitialize = new ArrayList<Object[]>();
		String xml = "<module name='PerceptualAssociativeMemory'>"
				+ "<class>edu.memphis.ccrg.lida.framework.mockclasses.MockFrameworkModule</class>"
				+ "<submodules>" 
				+	"<module name=\"NewEpisodicBuffer\">" +
					"<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>" +
					"<submodules>" +  
							"<module name=\"NewPerceptualBuffer\">" +
							"<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>" +
							"<param name=\"pParam\" type=\"double\">0.02 </param>" +
							"<taskspawner>superFancyTS</taskspawner>" +
							"<initialTasks>" +
							"<task name=\"updateCsmBackground\">" +
							"<class>edu.memphis.ccrg.lida.workspace.UpdateCsmBackgroundTask</class>" +
							"<ticksperrun>55</ticksperrun>" +
							"<associatedmodule>GlobalWorkspace</associatedmodule>" +
							"<associatedmodule>EpisodicMemory</associatedmodule>" +
							"</task>" +
							"</initialTasks>" +						
							"<initializerclass>edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer</initializerclass>"+
							"</module>" +
					"</submodules>" + 
					"<param name=\"eParam\" type=\"double\">0.01 </param>" +
					"<taskspawner>fancyTS</taskspawner>" +
					"</module>" 
				+ "</submodules>"		
				+ "<associatedmodule>Apple</associatedmodule>"
				+ "<param name='pam.Upscale' type='double'>.7 </param>"
				+ "<param name='pam.Downscale' type='double'>.6 </param>"
				+ "<param name='pam.Selectivity' type='double'>.5 </param>"
				+ "<param name='pam.newNodeType' type='string'>PamNodeImpl</param>"
				+ "<param name='pam.newLinkType' type='string'>PamLinkImpl</param>"
				+ "<taskspawner>defaultTS</taskspawner>"
				+"<initialTasks>" +
						"<task name=\"cueBackground\">" +
						"<class>edu.memphis.ccrg.lida.workspace.CueBackgroundTask</class>" +
						"<ticksperrun>15</ticksperrun>" +
						"<associatedmodule>Workspace</associatedmodule>" +
						"<param name=\"taskParameter\"  type=\"double\">0.4</param>	" +
						"</task>" +
						"<task name=\"fooBar\">" +
						"<class>edu.memphis.ccrg.lida.framework.tasks.MockFrameworkTask</class>" +
						"<ticksperrun>5</ticksperrun>" +
						"<associatedmodule>Ryan</associatedmodule>" +
						"</task>" +
						"</initialTasks>"
				
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
		
		//**Code being tested**
		FrameworkModule module = factory.getModule(moduleElement, toAssociate, toInitialize,
				taskSpawners);
		
		//**Verification**
		//Main Module attributes
		assertTrue(module != null);
		assertEquals(ModuleName.PerceptualAssociativeMemory, module.getModuleName());
		assertEquals(ts, module.getAssistingTaskSpawner());
		
		assertEquals(0.7, module.getParam("pam.Upscale", null));
		assertEquals("PamNodeImpl", module.getParam("pam.newNodeType", null));
		assertEquals(5, module.getParam("eParam", 5));
		
		//Submodules
		FrameworkModule eBuffer = module.getSubmodule(ModuleName.getModuleName("NewEpisodicBuffer"));
		assertTrue(eBuffer instanceof WorkspaceBufferImpl);
		assertEquals(0.01, eBuffer.getParam("eParam", null));
		assertEquals(999, eBuffer.getParam("pam.Upscale", 999));
		assertEquals(fancyts, eBuffer.getAssistingTaskSpawner());
		
		FrameworkModule pBuffer = module.getSubmodule(ModuleName.getModuleName("NewPerceptualBuffer"));
		assertEquals(null, pBuffer);
		
		pBuffer = eBuffer.getSubmodule(ModuleName.getModuleName("NewPerceptualBuffer"));
		assertTrue(pBuffer instanceof WorkspaceBufferImpl);
		assertEquals(0.02, pBuffer.getParam("pParam", null));
		assertEquals(999, pBuffer.getParam("pam.Upscale", 999));
		assertEquals(superFancyTS, pBuffer.getAssistingTaskSpawner());
		
		assertEquals(pBuffer, toInitialize.get(0)[0]);
		assertEquals("edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer",toInitialize.get(0)[1]);
		assertEquals(module, toInitialize.get(1)[0]);
		assertEquals("edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceInitalizer",toInitialize.get(1)[1]);
		
		//Tasks
		Collection<FrameworkTask> outerTasks = module.getAssistingTaskSpawner().getRunningTasks();
		assertEquals(2, outerTasks.size());
		FrameworkTask cueBackgroundTask = ts.tasks.get(0);
		assertTrue(cueBackgroundTask instanceof CueBackgroundTask);
		assertEquals(15, cueBackgroundTask.getTicksPerStep());
		assertEquals(0.4, cueBackgroundTask.getParam("taskParameter", null));
		assertEquals(5, cueBackgroundTask.getParam("pam.Selectivity", 5));
		FrameworkTask mockTask = ts.tasks.get(1);
		assertTrue(mockTask instanceof MockFrameworkTask);
		assertEquals(5, mockTask.getTicksPerStep());
		assertEquals(10, mockTask.getParam("taskParameter", 10));
		assertEquals(5, mockTask.getParam("pam.Selectivity", 5));
		
		Collection<FrameworkTask> noInnerTasks = eBuffer.getAssistingTaskSpawner().getRunningTasks();
		assertEquals(0, noInnerTasks.size());	
		
		Collection<FrameworkTask> innerTasks = pBuffer.getAssistingTaskSpawner().getRunningTasks();
		assertEquals(1, innerTasks.size());	
		FrameworkTask updateCsmTask = superFancyTS.tasks.get(0);
		assertTrue(updateCsmTask instanceof UpdateCsmBackgroundTask);
		assertEquals(55, updateCsmTask.getTicksPerStep());
		
		//Associations
		assertEquals(cueBackgroundTask, toAssociate.get(0)[0]);
		assertEquals("Workspace", toAssociate.get(0)[1]);
		
		assertEquals(mockTask, toAssociate.get(1)[0]);
		assertEquals("Ryan", toAssociate.get(1)[1]);
		
		assertEquals(updateCsmTask, toAssociate.get(2)[0]);
		assertEquals("GlobalWorkspace", toAssociate.get(2)[1]);
		
		assertEquals(updateCsmTask, toAssociate.get(3)[0]);
		assertEquals("EpisodicMemory", toAssociate.get(3)[1]);
		
		assertEquals(module, toAssociate.get(4)[0]);
		assertEquals("Apple", toAssociate.get(4)[1]);				
	}	

	
	@Test
	public void testGetModules() {
		List<Object[]> toAssociate = new ArrayList<Object[]>();
		List<Object[]> toInitialize = new ArrayList<Object[]>();
		String xml = "<lida><submodules> " +
				"<module name=\"NewEpisodicBuffer\">" +
		"<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>" +
		"<param name=\"eParam\" type=\"double\">0.01 </param>" +
		"<taskspawner>fancyTS</taskspawner>" +
		"</module>" +
		
		"<module name=\"NewPerceptualBuffer\">" +
		"<class>edu.memphis.ccrg.lida.workspace.workspacebuffers.WorkspaceBufferImpl</class>" +
		"<param name=\"pParam\" type=\"double\">0.02 </param>" +
		"<taskspawner>superFancyTS</taskspawner>" +
		"<initialTasks>" +
		"<task name=\"updateCsmBackground\">" +
		"<class>edu.memphis.ccrg.lida.workspace.UpdateCsmBackgroundTask</class>" +
		"<ticksperrun>55</ticksperrun>" +
		"<associatedmodule>GlobalWorkspace</associatedmodule>" +
		"<associatedmodule>EpisodicMemory</associatedmodule>" +
		"</task>" +
		"</initialTasks>" +						
		"<initializerclass>edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer</initializerclass>"+
		"</module>" +
		"</submodules></lida>";
		
		Map<String, TaskSpawner> taskSpawners = new HashMap<String, TaskSpawner>();
		MockTaskSpawner ts = new MockTaskSpawner();
		MockTaskSpawner fancyts = new MockTaskSpawner();
		MockTaskSpawner superFancyTS = new MockTaskSpawner();
		taskSpawners.put("defaultTS", ts);
		taskSpawners.put("fancyTS", fancyts);
		taskSpawners.put("superFancyTS", superFancyTS);		
		Element moduleElement = parseDomElement(xml);
		
		//**Code being tested**
		List<FrameworkModule> modules = factory.getModules(moduleElement, toAssociate, toInitialize,
				taskSpawners);
		
		assertEquals(2, modules.size());
		
		//Submodules
		FrameworkModule eBuffer = modules.get(0);
		assertTrue(eBuffer instanceof WorkspaceBufferImpl);
		assertEquals(ModuleName.getModuleName("NewEpisodicBuffer"), eBuffer.getModuleName());
		assertEquals(0.01, eBuffer.getParam("eParam", null));
		assertEquals(999, eBuffer.getParam("pam.Upscale", 999));
		assertEquals(fancyts, eBuffer.getAssistingTaskSpawner());
		
		
		FrameworkModule pBuffer = modules.get(1);
		assertTrue(pBuffer instanceof WorkspaceBufferImpl);
		assertEquals(ModuleName.getModuleName("NewPerceptualBuffer"), pBuffer.getModuleName());
		assertEquals(0.02, pBuffer.getParam("pParam", null));
		assertEquals(999, pBuffer.getParam("pam.Upscale", 999));
		assertEquals(superFancyTS, pBuffer.getAssistingTaskSpawner());
		
		assertEquals(pBuffer, toInitialize.get(0)[0]);
		assertEquals("edu.memphis.ccrg.lida.framework.mockclasses.MockInitializer",toInitialize.get(0)[1]);
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
		
		toAssoc.add(new Object[]{sub1, "TestName", null});
		toAssoc.add(new Object[]{task1, "EpisodicBuffer", ModuleUsage.TO_READ_FROM});
		
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
		MockFrameworkModule module2 = new MockFrameworkModule();
		
		Map<String, Object> p1 = new HashMap<String, Object>();
		p1.put("arg0", 10.0);
		p1.put("name", "Javier");
		
		Map<String, Object> p2 = new HashMap<String, Object>();
		p2.put("arg1", 20.0);
		p2.put("name1", "Ryan");
	
		List<Object[]> toInit = new ArrayList<Object[]>();
		toInit.add(new Object[]{module1, MockInitializer.class.getCanonicalName(), p1});
		toInit.add(new Object[]{module2, MockInitializer2.class.getCanonicalName(), p2});
		
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
		p.setProperty("lida.factory.data", "testData/shortagent.xml");
		
		Agent a = factory.getAgent(p);
		
		assertNotNull(a);
		FrameworkModule workspace = a.getSubmodule(ModuleName.Workspace);
		assertTrue(workspace instanceof Workspace);
		FrameworkModule pam = a.getSubmodule(ModuleName.PerceptualAssociativeMemory);
		assertTrue(pam instanceof PerceptualAssociativeMemory);
		TaskManager tm = a.getTaskManager();
		assertNotNull(tm);
		assertEquals(50, tm.getTickDuration());
		
		p.setProperty("lida.factory.data", "testDatasdfgwe3/shortagent.xml");
		a = factory.getAgent(p);
		assertEquals(null, a);
		
		p.setProperty("lida.factory.data", "testData/agentbad.xml");
		a = factory.getAgent(p);
		assertEquals(null, a);
		
		p = new Properties();
		a = factory.getAgent(p);
		
		assertNotNull(a);
		workspace = a.getSubmodule(ModuleName.Workspace);
		assertTrue(workspace instanceof Workspace);
		pam = a.getSubmodule(ModuleName.PerceptualAssociativeMemory);
		assertTrue(pam instanceof PerceptualAssociativeMemory);
		tm = a.getTaskManager();
		assertNotNull(tm);
		assertEquals(50, tm.getTickDuration());
	}

	@Test
	public void testParseDocument() {		
		Document dom = XmlUtils.parseXmlFile("testData/shortagent.xml","edu/memphis/ccrg/lida/framework/initialization/config/LidaXMLSchema.xsd");
		assertNotNull(dom);
		
		Agent a = factory.parseDocument(dom);
		
		assertNotNull(a);
		FrameworkModule workspace = a.getSubmodule(ModuleName.Workspace);
		assertTrue(workspace instanceof Workspace);
		FrameworkModule pam = a.getSubmodule(ModuleName.PerceptualAssociativeMemory);
		assertTrue(pam instanceof PerceptualAssociativeMemory);
		TaskManager tm = a.getTaskManager();
		
		assertNotNull(tm);
		assertEquals(50, tm.getTickDuration());
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

