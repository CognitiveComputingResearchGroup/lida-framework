package edu.memphis.ccrg.lida.globalworkspace;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodelet;
import edu.memphis.ccrg.lida.attentioncodelets.AttentionCodeletModule;
import edu.memphis.ccrg.lida.attentioncodelets.BasicAttentionCodelet;
import edu.memphis.ccrg.lida.framework.Agent;
import edu.memphis.ccrg.lida.framework.initialization.ModuleUsage;
import edu.memphis.ccrg.lida.framework.mockclasses.MockBroadcastListener;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.pam.PerceptualAssociativeMemoryImpl;

/**
 * This is a JUnit class which can be used to test methods of the GlobalWorkspaceInitalizer class
 * @author Siminder Kaur
 */
public class GlobalWorkspaceInitalizerTest {

	Map<String, Object> params;
	GlobalWorkspace globalWksp;
	Agent lida;
	GlobalWorkspaceInitalizer initializer = new GlobalWorkspaceInitalizer();
	Queue<Coalition> coalitions = new ConcurrentLinkedQueue<Coalition>();
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
		params = new HashMap<String, Object>();
		globalWksp = new GlobalWorkspaceImpl();
		params.put("globalWorkspace.delayNoBroadcast", 1000);
		params.put("globalWorkspace.delayNoNewCoalition", 500);
		params.put("globalWorkspace.aggregateActivationThreshold", 0.9);
		params.put("globalWorkspace.individualActivationThreshold", 0.3);

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

	@Test
	public void testInitModule() {
		
		System.out.println("Testing InitModule() method. See console...");
		
		globalWksp.setAssociatedModule(attnModule, ModuleUsage.TO_WRITE_TO);
		globalWksp.addCoalition(coalition);
		globalWksp.addCoalition(coalition2);
		globalWksp.addCoalition(coalition3);
		coalition.setActivation(0.2);
		coalition2.setActivation(0.3);
		globalWksp.setAssistingTaskSpawner(ts);
		globalWksp.addBroadcastListener(listener);
		initializer.initModule(globalWksp, lida, params);
		globalWksp.triggerBroadcast(null);
	}

}
