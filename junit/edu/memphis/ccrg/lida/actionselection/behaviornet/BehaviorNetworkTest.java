package edu.memphis.ccrg.lida.actionselection.behaviornet;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.BehaviorNetwork;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

public class BehaviorNetworkTest {
	
	private BehaviorNetwork bNetwork;
	private TaskSpawner ts;
	private FrameworkTask backgroundTask;

	@Before
	public void setUp() throws Exception {
		bNetwork = new BehaviorNetwork();
		ts = new MockTaskSpawner();
		bNetwork.setAssistingTaskSpawner(ts);
		bNetwork.init();
		backgroundTask = ts.getTasks().iterator().next();
	}

	@Test
	public void testRunEmpty(){
		try {
			backgroundTask.call();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}
	
	@Test
	public void testReceiveBehavior() {
	}

	@Test
	public void testSelectBehavior() {
	}

	@Test
	public void testDecayModule() {
	}

}
