package edu.memphis.ccrg.lida.pam;

import junit.framework.TestCase;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.pam.tasks.PropagationTask;

/**
 * A propagation task excites a node and a link.  
 * The link connects the source of the activation to the node.
 * @author Ryan J McCall, Usef
 *
 */

public class PropagationTaskTest extends TestCase{
	private PamNode source;
	private PamNode sink;
	private PamLink link;
	
	/*
	 * Used to make another excitation call
	 */
	private MockPAM pam;
	private LidaElementFactory factory = LidaElementFactory.getInstance();
	/*
	 * For threshold task creation
	 */
	private MockTaskSpawner taskSpawner;
	
	@Override
	public void setUp() throws Exception {
		
		source =  (PamNode) LidaElementFactory.getInstance().getNode(PamNodeImpl.factoryName);
		sink   =  (PamNode) LidaElementFactory.getInstance().getNode(PamNodeImpl.factoryName);
		link  = (PamLink) factory.getLink(PamLinkImpl.factoryName, source, sink, LinkCategoryNode.NONE);
		
			 
		pam = new MockPAM();
		taskSpawner= new MockTaskSpawner();
	}
	
	public void test(){
		double perceptThreshold = 1.0;
		double linkActivation = 0.1;
		double sourceActivation = 0.1;
		double sinkActivation = 0.1;
		pam.setPerceptThreshold(perceptThreshold);
		link.setActivation(linkActivation);
		source.setActivation(sourceActivation);
		sink.setActivation(sinkActivation);
//		System.out.println(link.getActivation() + " " +link.getTotalActivation());
		PropagationTask excite= new PropagationTask(source, link, sink, 0.1, pam, taskSpawner );
		excite.call();
	 
		assertTrue(link.getActivation() > 0.1);
		assertTrue(sink.getActivation() > 0.1);
		assertTrue(source.getActivation() == 0.1);
		assertTrue(LidaTaskStatus.FINISHED == excite.getStatus() );
	 
		assertEquals(sink, pam.testGetSink());
//		System.out.println(link.getActivation() + " " +link.getTotalActivation());
		assertFalse(pam.isOverPerceptThreshold(link));
		assertFalse(pam.isOverPerceptThreshold(sink));
		 
	 
	}
	
	public void test2(){
		double perceptThreshold = 0.15;
		double linkActivation = 0.1;
		double sourceActivation = 0.1;
		double sinkActivation = 0.1;
		pam.setPerceptThreshold(perceptThreshold);
		link.setActivation(linkActivation);
		source.setActivation(sourceActivation);
		sink.setActivation(sinkActivation);
//		System.out.println(link.getActivation() + " " +link.getTotalActivation());
		PropagationTask excite= new PropagationTask(source, link, sink, 0.5, pam, taskSpawner );
		excite.call();
	 
		assertTrue(link.getActivation() > 0.1);
		assertTrue(sink.getActivation() > 0.1);
		assertTrue(source.getActivation() == 0.1);
		assertTrue(LidaTaskStatus.FINISHED == excite.getStatus() );
	 
		assertEquals(sink, pam.testGetSink());
//		System.out.println(link.getActivation() + " " +link.getTotalActivation());
		assertTrue(pam.isOverPerceptThreshold(link));
		assertTrue(pam.isOverPerceptThreshold(sink));
		assertTrue(taskSpawner.getRunningTasks().size() != 0);
		 
	 
	}
 
}
