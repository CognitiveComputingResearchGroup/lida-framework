package edu.memphis.ccrg.lida.pam;

import java.util.Collection;

import junit.framework.TestCase;
import edu.memphis.ccrg.lida.framework.mockclasses.MockPAM;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.strategies.DefaultExciteStrategy;
import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.pam.tasks.AddToPerceptTask;
import edu.memphis.ccrg.lida.pam.tasks.ExcitationTask;

public class ExcitationTaskTest extends TestCase {
	
	private PamNode pamNode;
	
	/*
	 * Used to make another excitation call
	 */
	private  MockPAM pam;
	
	/*
	 * For threshold task creation
	 */
	private MockTaskSpawner taskSpawner;
	
	@Override
	public void setUp() throws Exception {
		pamNode = new PamNodeImpl();
		pam = new MockPAM();
		taskSpawner= new MockTaskSpawner();
	}
	
	
	public void test(){
		pam.setPerceptThreshold(1.0);
		pamNode.setExciteStrategy(new DefaultExciteStrategy());
		ExcitationTask excite= new ExcitationTask(pamNode, 0.5, 1, pam, taskSpawner);
		
		excite.call();
		assertTrue(pamNode.getActivation() == 0.5);
		assertTrue(pam.testGetSink().getActivation() == 0.5);
		assertTrue(LidaTaskStatus.FINISHED == excite.getStatus() );
	 
	}
	
	public void testTaskSpawner(){
		pam.setPerceptThreshold(0.4);
		pamNode.setExciteStrategy(new DefaultExciteStrategy());
		ExcitationTask excite= new ExcitationTask(pamNode, 0.5, 1, pam, taskSpawner);
		
		excite.call();
		assertTrue(pamNode.getActivation() == 0.5);
		assertTrue(pam.testGetSink().getActivation() == 0.5);
		
		Collection<LidaTask> tasks= taskSpawner.getRunningTasks(); 
		for(LidaTask tsk: tasks){
			assertTrue(tsk instanceof AddToPerceptTask);
		}
		 
		assertTrue(LidaTaskStatus.FINISHED == excite.getStatus() );
	 
	}

}
