package edu.memphis.ccrg.lida.attentioncodelets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockAttentionCodeletImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBuffer;
import edu.memphis.ccrg.lida.workspace.workspaceBuffer.WorkspaceBufferImpl;

/**
 * This is a JUnit class which can be used to test methods of the AttentionCodeletModule class
 * @author Ryan McCall
 */
public class AttentionCodeletModuleTest{
	
	private AttentionCodeletModule attnModule;
	private TaskSpawner taskSpawner;
	private WorkspaceBuffer csm;
	private GlobalWorkspace globalWorkspace;
	private Node node1,node2;
	private Link link1;
	private NodeStructure ns;
	private MockAttentionCodeletImpl codelet;
	private static final ElementFactory factory = ElementFactory.getInstance();

	@Before
	public void setUp() throws Exception {
		attnModule = new AttentionCodeletModule();
		taskSpawner = new MockTaskSpawner();
		attnModule.setAssistingTaskSpawner(taskSpawner);
		
		globalWorkspace= new GlobalWorkspaceImpl();
		csm = new WorkspaceBufferImpl();
		
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
		attnModule.receiveBroadcast((BroadcastContent)ns);
		
		assertEquals(1, attnModule.getAssistingTaskSpawner().getRunningTasks().size());
		//more testing when fully implemented
	}

	@Test
	public void testGetNewAttentionCodelet() {
		
		AttentionCodelet codelet = attnModule.getDefaultCodelet(null);		
		assertTrue(codelet instanceof BasicAttentionCodelet);
//		codelet.
	}

	@Test
	public void testRunAttentionCodelet() {
		
		System.out.println("Testing runAttentionCodelet() method. See console...");		
		attnModule.addCodelet(codelet);
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
	public void testGetDefaultCodeletMapOfStringObject() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetDefaultCodelet() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCodeletString() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCodeletStringMapOfStringObject() {
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
