package edu.memphis.ccrg.lida.attentioncodelets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockAttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.WorkspaceImpl;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * This is a JUnit class which can be used to test methods of the AttentionCodeletModule class.
 * 
 * @author Ryan McCall
 * 
 */
public class AttentionCodeletModuleTest{
	
	private AttentionCodeletModule attentionModule;
	private TaskSpawner taskSpawner;
	private WorkspaceBuffer csm;
	private WorkspaceImpl workspace;
	private MockGlobalWorkspaceImpl globalWorkspace;
	private Node node1,node2;
	private Link link1;
	private NodeStructure ns;
	private MockAttentionCodeletImpl codelet;
	private static final ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		attentionModule = new AttentionCodeletModule();
		taskSpawner = new MockTaskSpawner();
		attentionModule.setAssistingTaskSpawner(taskSpawner);
		
		csm = new WorkspaceBufferImpl();
		csm.setModuleName(ModuleName.CurrentSituationalModel);
		workspace = new WorkspaceImpl();
		workspace.addSubModule(csm);
		
		globalWorkspace= new MockGlobalWorkspaceImpl();
		
		ns = new NodeStructureImpl();
		node1 = factory.getNode();
		node2 = factory.getNode();
		PamNode category = (PamNode) factory.getNode("PamNodeImpl");
		link1 = factory.getLink(node1,node2,category);
		ns.addDefaultNode(node1);
		ns.addDefaultNode(node2);
		ns.addDefaultLink(link1);	
		
		csm.addBufferContent((WorkspaceContent) ns);
		
		codelet = new MockAttentionCodeletImpl();
		codelet.setSoughtContent(ns);	
	
		codelet.setAssociatedModule(csm, ModuleUsage.TO_READ_FROM);
		codelet.setAssociatedModule(globalWorkspace, ModuleUsage.TO_WRITE_TO);
	}
	
	@Test
	public void testReceiveBroadcast() {	
		assertEquals(0, attentionModule.getAssistingTaskSpawner().getRunningTasks().size());
		
		attentionModule.receiveBroadcast((BroadcastContent)ns);
		
		assertEquals(1, attentionModule.getAssistingTaskSpawner().getRunningTasks().size());
		//more testing when fully implemented
	}

	@Test
	public void testGetDefaultCodelet() {
		//Without associated modules 
		AttentionCodeletImpl codelet = (AttentionCodeletImpl) attentionModule.getDefaultCodelet();		
		assertTrue(codelet instanceof BasicAttentionCodelet);
		codelet.setSoughtContent(ns);
		try{
			codelet.runThisFrameworkTask();
			assertTrue(false);
		}catch(NullPointerException e){
		}
		
		//with associated modules
		attentionModule.setAssociatedModule(workspace, null);
		attentionModule.setAssociatedModule(globalWorkspace, null);
		
		codelet = (AttentionCodeletImpl) attentionModule.getDefaultCodelet();		
		assertTrue(codelet instanceof BasicAttentionCodelet);
		
		assertEquals(null, globalWorkspace.coalition);
		
		codelet.setSoughtContent(ns);
		codelet.runThisFrameworkTask();
		
		assertNotNull(globalWorkspace.coalition);
	}
	

	@Test
	public void testGetCodeletString() {
		AttentionCodelet codelet = attentionModule.getCodelet("foo");
		assertEquals(null, codelet);
		
		factory.addCodeletType("coolCodelet", MockAttentionCodeletImpl.class.getCanonicalName());
		codelet = attentionModule.getCodelet("coolCodelet");
		assertTrue(codelet instanceof MockAttentionCodeletImpl);
	}

	@Test
	public void testGetCodeletStringMapOfStringObject() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("arg0", 10.0);
		params.put("name", "Javier");
		
		AttentionCodelet codelet = attentionModule.getCodelet("BasicAttentionCodelet", params);
		assertEquals(10.0, codelet.getParam("arg0", null));
		assertEquals("Javier", codelet.getParam("name", null));
	}
	

	@Test
	public void testGetDefaultCodeletMapOfStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testRunAttentionCodelet() {
		
		System.out.println("Testing runAttentionCodelet() method. See console...");		
		attentionModule.addCodelet(codelet);
	}	
	
	@Test
	public void testGetModuleContent() {
		fail("Not yet implemented");
	}

	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	@Test
	public void testAttentionCodeletModule() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetDefaultCodeletType() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddCodelet() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceivePreafference() {
		fail("Not yet implemented");
	}

	@Test
	public void testLearn() {
		fail("Not yet implemented");
	}

	@Test
	public void testToString() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddListener() {
		fail("Not yet implemented");
	}

	@Test
	public void testDecayModule() {
		fail("Not yet implemented");
	}
}
