package edu.memphis.ccrg.lida.globalworkspace;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodeletModule;
import edu.memphis.ccrg.lida.attentioncodelets.BasicAttentionCodelet;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockBroadcastListener;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;

/**
 * This is a JUnit class which can be used to test methods of the GlobalWorkspaceImpl class
 * @author Siminder Kaur
 */

public class GlobalWorkspaceImplTest {

	Queue<Coalition> coalitions = new ConcurrentLinkedQueue<Coalition>();
	GlobalWorkspaceImpl gw = new GlobalWorkspaceImpl();
	Coalition coalition, coalition2, coalition3;
	NodeStructureImpl ns = new NodeStructureImpl();
	NodeStructureImpl ns2 = new NodeStructureImpl();
	NodeImpl n1, n2, n3;
	LinkImpl l;
	AttentionCodeletModule attnModule = new AttentionCodeletModule();
	MockTaskSpawner ts;
	MockBroadcastListener listener;
	AttentionCodelet codelet = new BasicAttentionCodelet();

	@Before
	public void setUp() throws Exception {
		new PerceptualAssociativeMemoryImpl();
		ts = new MockTaskSpawner();
		listener = new MockBroadcastListener();

		n1 = new NodeImpl();
		n2 = new NodeImpl();
		n3 = new NodeImpl();
		n1.setId(1);
		n2.setId(2);
		n3.setId(3);
		l = new LinkImpl(n1, n2, PerceptualAssociativeMemoryImpl.NONE);

		ns.addDefaultNode(n1);
		ns.addDefaultNode(n2);
		ns.addDefaultNode(n3);
		ns.addDefaultLink(l);

		ns2.addDefaultNode(n1);
		ns2.addDefaultNode(n2);
		ns2.addDefaultLink(l);

		coalition = new CoalitionImpl(ns, 0.8,codelet);
		coalition2 = new CoalitionImpl(ns2, 0.9,codelet);
		coalition3 = new CoalitionImpl(ns, 0.1,codelet);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetModuleContent() {
		gw.addCoalition(coalition);
		gw.addCoalition(coalition2);
		coalitions.add(coalition);
		coalitions.add(coalition2);
		Collection<Coalition> content = (Collection<Coalition>) gw.getModuleContent();

		if (content.size() == 2) {
			for (Coalition c : content) {

				if (!coalitions.contains(c)) {
					fail("Problem with getModuleContent");

				}
			}
		} else
			fail("Problem with getModuleContent");
	}

	@Test
	public void testInit() {
		gw.setAssociatedModule(attnModule, ModuleUsage.TO_WRITE_TO);
		gw.addCoalition(coalition);
		gw.setAssistingTaskSpawner(ts);
		gw.addBroadcastListener(listener);
		gw.init();

		assertTrue("Problem with init", ts.getRunningTasks().size() > 0);

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testDecayModule() {
		gw.addCoalition(coalition);
		coalition.setActivation(0.8);

		gw.taskManagerDecayModule(10);
		assertTrue("Problem with decayModule", coalition.getActivation() < 0.8);

		gw.taskManagerDecayModule(1000);
		Collection<Coalition> content = (Collection<Coalition>) gw.getModuleContent();
		assertTrue("Problem with decayModule", content.isEmpty());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void testAddCoalition() {
		gw.addCoalition(coalition);
		Collection<Coalition> content = (Collection<Coalition>) gw.getModuleContent();
		assertTrue("Problem with AddCoalition", !content.isEmpty());

	}

	@Test
	public void testTriggerBroadcast() {

		System.out.println("Testing triggerBroadcast() method. See console...");

		gw.addCoalition(coalition);
		gw.addCoalition(coalition2);
		gw.addCoalition(coalition3);

		coalition.setActivation(0.8);
		coalition2.setActivation(0.9);
		coalition3.setActivation(0.1);

		gw.setAssociatedModule(attnModule, ModuleUsage.TO_WRITE_TO);
		gw.setAssistingTaskSpawner(ts);
		gw.addBroadcastListener(listener);

		gw.triggerBroadcast(null);
	}

}
