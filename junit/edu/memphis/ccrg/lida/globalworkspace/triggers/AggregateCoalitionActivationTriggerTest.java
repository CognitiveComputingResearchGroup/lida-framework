package edu.memphis.ccrg.lida.globalworkspace.triggers;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.framework.mockclasses.MockGlobalWorkspaceImpl;
import edu.memphis.ccrg.lida.framework.shared.LinkCategoryNode;
import edu.memphis.ccrg.lida.framework.shared.LinkImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.globalworkspace.GlobalWorkspace;

public class AggregateCoalitionActivationTriggerTest {

	AggregateCoalitionActivationTrigger trigger;
	Set<Coalition> coalitions;
	Map<String, Object> parameters;
	GlobalWorkspace gw;
	Coalition coalition, coalition2, coalition3;
	NodeStructureImpl ns = new NodeStructureImpl();
	NodeStructureImpl ns2 = new NodeStructureImpl();
	NodeImpl n1, n2, n3;
	LinkImpl l;

	@Before
	public void setUp() throws Exception {

		trigger = new AggregateCoalitionActivationTrigger();
		coalitions = new HashSet<Coalition>();
		parameters = new HashMap<String, Object>();
		gw = new MockGlobalWorkspaceImpl();

		n1 = new NodeImpl();
		n2 = new NodeImpl();
		n3 = new NodeImpl();
		n1.setId(1);
		n2.setId(2);
		n3.setId(3);

		n1.setActivation(0.5);
		n2.setActivation(0.6);
		n3.setActivation(0.7);
		l = new LinkImpl(n1, n2, LinkCategoryNode.CHILD);
		l.setActivation(0.5);

		ns.addDefaultNode(n1);
		ns.addDefaultNode(n2);
		ns.addDefaultNode(n3);
		ns.addDefaultLink(l);

		ns2.addDefaultNode(n1);
		ns2.addDefaultNode(n2);
		ns2.addDefaultLink(l);

		coalition = new CoalitionImpl(ns, 0.8);
		coalition2 = new CoalitionImpl(ns2, 0.01);
		coalition3 = new CoalitionImpl(ns, 0.4);

		coalitions.add(coalition);
		coalitions.add(coalition2);
		coalitions.add(coalition3);

	}

	@Test
	public void testCheckForTriggerCondition() {
		
		parameters.put("threshold", 0.5);
		trigger.init(parameters, gw);

		System.out
				.println("Testing CheckForTriggerCondition method. See console...");
		trigger.checkForTriggerCondition(coalitions);

	}
	
	@Test
	public void testInit() {

		parameters.put("threshold", 0.5);
		trigger.init(parameters, gw);
		
		assertEquals("Problem with Init",0.5,trigger.threshold);
		assertEquals("Problem with Init",gw,trigger.gw);

	}
	

}
