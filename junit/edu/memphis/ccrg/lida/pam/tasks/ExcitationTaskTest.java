package edu.memphis.ccrg.lida.pam.tasks;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.pam.PamNode;
import edu.memphis.ccrg.lida.pam.PamNodeImpl;
import edu.memphis.ccrg.lida.pam.tasks.AddToPerceptTask;
import edu.memphis.ccrg.lida.pam.tasks.ExcitationTask;

public class ExcitationTaskTest{
	
	private PamNode pamNode;
	
	/*
	 * Used to make another excitation call
	 */
	private  MockPAM pam;
	
	/*
	 * For threshold task creation
	 */
	private MockTaskSpawner taskSpawner;
	@Before
	public void setUp() throws Exception {
		pamNode = new PamNodeImpl();
		pam = new MockPAM();
		taskSpawner= new MockTaskSpawner();
	}
	
	@Test
	public void test(){
		pam.setPerceptThreshold(1.0);
		pamNode.setExciteStrategy(new DefaultExciteStrategy());
		ExcitationTask excite= new ExcitationTask(pamNode, 0.5, 1, pam, taskSpawner);
		
		excite.call();
		assertTrue(pamNode.getActivation()== 0.5 + Activatible.DEFAULT_ACTIVATION);
		assertTrue(pam.testGetSink().getActivation()== 0.5 + Activatible.DEFAULT_ACTIVATION);
		assertTrue(TaskStatus.FINISHED == excite.getStatus() );
	 
	}
	@Test
	public void testTaskSpawner(){
		pam.setPerceptThreshold(0.4);
		pamNode.setExciteStrategy(new DefaultExciteStrategy());
		ExcitationTask excite= new ExcitationTask(pamNode, 0.5, 1, pam, taskSpawner);
		
		excite.call();
		assertTrue(pamNode.getActivation()== 0.5 + Activatible.DEFAULT_ACTIVATION);
		assertTrue(pam.testGetSink().getActivation()== 0.5 + Activatible.DEFAULT_ACTIVATION);
		
		Collection<FrameworkTask> tasks= taskSpawner.getRunningTasks(); 
		for(FrameworkTask tsk: tasks){
			assertTrue(tsk instanceof AddToPerceptTask);
		}
		 
		assertTrue(TaskStatus.FINISHED == excite.getStatus() );
	 
	}

}
