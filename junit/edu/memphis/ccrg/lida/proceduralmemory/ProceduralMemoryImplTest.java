package edu.memphis.ccrg.lida.proceduralmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionImpl;
import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.ns.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ns.ProceduralMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ns.SchemeImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ns.ProceduralMemoryImpl.ConditionType;

/**
 * @author Ryan J. McCall
 * @author Javier Snaider
 * 
 */
public class ProceduralMemoryImplTest {

	private static final ElementFactory factory = ElementFactory.getInstance();
	private static final double epsilon = 10e-9;
	private ProceduralMemoryImpl pm;
	private Scheme s1, s2, s3;
	private Node c1, c3, c2, c4;
	private MockTaskSpawner mockTs;

	/**
	 */
	@Before
	public void setUp() {
		mockTs = new MockTaskSpawner();
		pm = new ProceduralMemoryImpl();
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("proceduralMemory.schemeSelectionThreshold", 0.9);
		params.put("proceduralMemory.contextWeight", 1.0);
		params.put("proceduralMemory.addingListWeight", 1.0);
		pm.init(params);

		pm.setAssistingTaskSpawner(mockTs);
		c1 = factory.getNode("NodeImpl", "c1");
		c2 = factory.getNode("NodeImpl", "c2");
		c3 = factory.getNode("NodeImpl", "c3");
		c4 = factory.getNode("NodeImpl", "c4");

		Action a = new ActionImpl("action1");
		s1 = pm.getNewScheme(a);
		s1.setLabel("s1");

		a = new ActionImpl("action2");
		s2 = pm.getNewScheme(a);
		s2.setLabel("s2");

		a = new ActionImpl("action3");
		s3 = pm.getNewScheme(a);
		s3.setLabel("s3");
	}

	@Test
	public void testLearn() {
		// TODO when implemented
	}

	@Test
	public void testDecayModule() {
		pm.addCondition(c1);
		NodeStructure ns = new NodeStructureImpl();
		c1.setActivation(1.0);
		ns.addDefaultNode(c1);
		Coalition coal = new CoalitionImpl(ns, null);
		pm.receiveBroadcast(coal);
		Node bufferNode = pm.getBroadcastBuffer().getNode(c1.getId());
		assertEquals(1.0, bufferNode.getActivation(), epsilon);

		pm.decayModule(5);

		assertEquals(0.5, bufferNode.getActivation(), epsilon);

		// TODO add to this test after learning implemented
	}

	@Test
	public void testGetNewScheme() {
		Action a = new ActionImpl("action1");
		Scheme s = pm.getNewScheme(a);
		assertNotNull(s);
		assertSame(a, s.getAction());
		assertTrue(pm.containsScheme(s));
	}

	@Test
	public void testGetNewScheme0() {
		Scheme s = pm.getNewScheme(null);
		assertNull(s);
	}

	@Test
	public void testGetNewScheme1() {
		Action a = new ActionImpl("action1");
		Scheme s = pm.getNewScheme(a);
		Scheme s0 = pm.getNewScheme(a);
		assertNotSame(s, s0);
		assertSame(s.getAction(), s0.getAction());
	}

	@Test
	public void testGetNewScheme2() {
		Action a = new ActionImpl("action1");
		SchemeImpl s = (SchemeImpl) pm.getNewScheme(a);
		assertNotNull(s);
		assertSame(a, s.getAction());
		assertTrue(pm.containsScheme(s));

		s.addCondition(c1, ConditionType.CONTEXT);
		s.addCondition(c2, ConditionType.ADDINGLIST);

		// Try PM with C3 before adding a duplicate of C3 to the Scheme
		pm.addCondition(c3);
		Node duplicateCondition = new NodeImpl();
		duplicateCondition.setId(c3.getId());
		s.addCondition(duplicateCondition, ConditionType.CONTEXT);

		// Actual should be C3
		Condition actual = s.getContextCondition(c3.getConditionId());
		assertSame(c3, actual);
		assertNotSame(duplicateCondition, actual);
	}

	@Test
	public void testShouldInstantiate() {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("proceduralMemory.schemeSelectionThreshold", 0.9);
		pm.init(params);

		// SchemeImpl.setContextWeight(1.0);
		// SchemeImpl.setAddingListWeight(0.0);

		// Scheme has insufficiently activated conditions
		c1.setActivation(0.89);
		c2.setActivation(0.89);
		s1.addCondition(c1, ConditionType.CONTEXT);
		s1.addCondition(c2, ConditionType.ADDINGLIST);
		NodeStructure ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertFalse(pm.shouldInstantiate(s1, ns));

		// Scheme has sufficient activation from context condition
		c1.setActivation(0.9);
		c2.setActivation(0.89);
		s1.addCondition(c1, ConditionType.CONTEXT);
		s1.addCondition(c2, ConditionType.ADDINGLIST);
		ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertTrue(pm.shouldInstantiate(s1, ns));

		// Switch goal orientedness
		params.put("proceduralMemory.schemeSelectionThreshold", 0.9);
		params.put("proceduralMemory.contextWeight", 0.0);
		params.put("proceduralMemory.addingListWeight", 1.0);
		pm.init(params);

		// Insufficient
		c1.setActivation(0.89);
		c2.setActivation(0.0);
		c2.setIncentiveSalience(0.89);
		s1.addCondition(c1, ConditionType.CONTEXT);
		s1.addCondition(c2, ConditionType.ADDINGLIST);
		ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertFalse(pm.shouldInstantiate(s1, ns));

		// Sufficient
		c1.setActivation(1.0);
		c2.setActivation(0.0);
		c2.setIncentiveSalience(0.9);
		s1.addCondition(c1, ConditionType.CONTEXT);
		s1.addCondition(c2, ConditionType.ADDINGLIST);
		ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertTrue(pm.shouldInstantiate(s1, ns));
	}

	@Test
	public void testCreateInstantiation() {
		MockProceduralMemoryListener listener = new MockProceduralMemoryListener();
		pm.addListener(listener);

		pm.createInstantiation(s1);
		assertEquals(1, listener.behaviors.size());
		assertSame(s1, listener.behaviors.get(0).getScheme());
	}

	@Test
	public void testReceiveBroadcast() {
		NodeStructure ns = new NodeStructureImpl();
		c1.setActivation(0.5);
		ns.addDefaultNode((Node) c1);
		Coalition coal = new CoalitionImpl(ns, null);
		// wont add c1 because c1 is not in the condition pool
		pm.receiveBroadcast(coal);

		assertEquals(1, mockTs.tasks.size());
		assertEquals(0, pm.getBroadcastBuffer().getLinkableCount());
		assertEquals(0, pm.getConditionPool().size());
	}

	@Test
	public void testReceiveBroadcast1() {
		// Condition c1 exists, not in buffer, copy of c1 comes in broadcast
		NodeStructure ns = new NodeStructureImpl();
		Node bNode = new NodeImpl();
		bNode.setId(c1.getId());
		bNode.setActivation(0.2);
		ns.addDefaultNode(bNode);
		Coalition coal = new CoalitionImpl(ns, null);
		s1.addCondition(c1, ConditionType.CONTEXT);

		pm.receiveBroadcast(coal);

		NodeStructure bb = pm.getBroadcastBuffer();
		assertEquals(1, mockTs.tasks.size());
		assertEquals(1, bb.getLinkableCount());
		assertEquals(1, pm.getConditionPool().size());
		Node actualBB = bb.getNode(c1.getId());
		assertSame(c1, actualBB);
		Node actualCondition = (Node) pm.getConditionPool().iterator().next();
		assertSame(c1, actualCondition);
		// Activation should match broadcast
		assertEquals(0.2, actualBB.getActivation(), epsilon);
		assertEquals(0.2, actualCondition.getActivation(), epsilon);
	}

	@Test
	public void testReceiveBroadcast2() {
		// Activation should be updated by broadcast
		c1.setActivation(0.1);
		s1.addCondition(c1, ConditionType.CONTEXT);
		assertEquals(0.1, pm.getConditionPool().iterator().next()
				.getActivation(), epsilon);

		NodeStructure ns = new NodeStructureImpl();
		Node bNode = new NodeImpl();
		bNode.setId(c1.getId());
		bNode.setActivation(0.2);
		ns.addDefaultNode(bNode);
		Coalition coal = new CoalitionImpl(ns, null);

		pm.receiveBroadcast(coal);

		NodeStructure bb = pm.getBroadcastBuffer();
		assertEquals(1, mockTs.tasks.size());
		assertEquals(1, bb.getLinkableCount());
		assertEquals(1, pm.getConditionPool().size());
		Node actual = bb.getNode(((Node) c1).getId());
		// Both buffers only contain C1
		assertSame(c1, actual);
		Condition c1Condition = pm.getConditionPool().iterator().next();
		assertSame(c1, c1Condition);
		// Activation has been updated by broadcast
		assertEquals(0.2, actual.getActivation(), epsilon);
		assertEquals(0.2, c1Condition.getActivation(), epsilon);
	}

	@Test
	public void testReceiveBroadcast3() {
		// Activation should NOT be updated by broadcast
		c1.setActivation(0.3);
		s1.addCondition(c1, ConditionType.CONTEXT);
		assertEquals(0.3, pm.getConditionPool().iterator().next()
				.getActivation(), epsilon);

		NodeStructure ns = new NodeStructureImpl();
		Node bNode = new NodeImpl();
		bNode.setId(c1.getId());
		bNode.setActivation(0.2);
		ns.addDefaultNode(bNode);
		Coalition coal = new CoalitionImpl(ns, null);

		pm.receiveBroadcast(coal);

		NodeStructure bb = pm.getBroadcastBuffer();
		assertEquals(1, mockTs.tasks.size());
		assertEquals(1, bb.getLinkableCount());
		assertEquals(1, pm.getConditionPool().size());
		Node actualBB = bb.getNode(((Node) c1).getId());
		// Both buffers only contain C1
		assertSame(c1, actualBB);
		Condition c1Condition = pm.getConditionPool().iterator().next();
		assertSame(c1, c1Condition);
		// Activation has been updated by broadcast
		assertEquals(0.3, c1Condition.getActivation(), epsilon);
		assertEquals(0.3, actualBB.getActivation(), epsilon);
	}

	@Test
	public void testReceiveBroadcast4() {
		// Activation should be updated by broadcast
		// Incentive salience should be updated by broadcast
		c2.setActivation(0.1);
		c2.setIncentiveSalience(0.2);
		s1.addCondition(c2, ConditionType.CONTEXT);
		Condition stored = pm.getConditionPool().iterator().next();
		assertEquals(0.1, stored.getActivation(), epsilon);
		assertEquals(0.2, stored.getIncentiveSalience(), epsilon);

		NodeStructure ns = new NodeStructureImpl();
		Node bNode = new NodeImpl();
		bNode.setId(c2.getId());
		bNode.setActivation(0.2);
		bNode.setIncentiveSalience(0.3);
		ns.addNode(bNode, "NodeImpl");
		Coalition coal = new CoalitionImpl(ns, null);

		pm.receiveBroadcast(coal);

		NodeStructure bb = pm.getBroadcastBuffer();
		assertEquals(1, mockTs.tasks.size());
		assertEquals(1, bb.getLinkableCount());
		assertEquals(1, pm.getConditionPool().size());
		Node bufferNode = bb.getNode(((Node) c2).getId());
		// Both buffers only contain C1
		assertSame(c2, bufferNode);
		Condition c2Condition = pm.getConditionPool().iterator().next();
		assertSame(c2, c2Condition);
		// Activation has been updated by broadcast
		assertEquals(0.2, bufferNode.getActivation(), epsilon);
		assertEquals(0.2, c2Condition.getActivation(), epsilon);
		assertEquals(0.3, c2Condition.getIncentiveSalience(),
				epsilon);
		assertEquals(0.3, bufferNode.getIncentiveSalience(),
				epsilon);
	}

	@Test
	public void testReceiveBroadcast5() {
		// Activation should be updated by broadcast
		// Incentive salience should NOT be updated by broadcast
		c2.setActivation(0.1);
		c2.setIncentiveSalience(0.2);
		s1.addCondition(c2, ConditionType.CONTEXT);
		Condition stored = pm.getConditionPool().iterator().next();
		assertEquals(0.1, stored.getActivation(), epsilon);
		assertEquals(0.2, stored.getIncentiveSalience(), epsilon);

		NodeStructure ns = new NodeStructureImpl();
		Node bNode = new NodeImpl();
		bNode.setId(c2.getId());
		bNode.setActivation(0.21);
		bNode.setIncentiveSalience(0.001);
		ns.addNode(bNode, "NodeImpl");
		Coalition coal = new CoalitionImpl(ns, null);

		pm.receiveBroadcast(coal);

		NodeStructure bb = pm.getBroadcastBuffer();
		assertEquals(1, mockTs.tasks.size());
		assertEquals(1, bb.getLinkableCount());
		assertEquals(1, pm.getConditionPool().size());
		Node bufferNode = bb.getNode(((Node) c2).getId());
		// Both buffers only contain C1
		assertSame(c2, bufferNode);
		Condition c2Condition = pm.getConditionPool().iterator().next();
		assertSame(c2, c2Condition);
		// Activation has been updated by broadcast
		assertEquals(0.21, bufferNode.getActivation(), epsilon);
		assertEquals(0.21, c2Condition.getActivation(), epsilon);
		assertEquals(0.2, c2Condition.getIncentiveSalience(),
				epsilon);
		assertEquals(0.2, bufferNode.getIncentiveSalience(),
				epsilon);
	}

	@Test
	public void testActivateSchemes() {
		// 1 context is fulfilled
		MockProceduralMemoryListener listener = new MockProceduralMemoryListener();
		pm.addListener(listener);
		c1.setActivation(1.0);
		s1.addCondition(c1, ConditionType.CONTEXT);
		NodeStructure ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		Coalition coal = new CoalitionImpl(ns, null);
		pm.receiveBroadcast(coal);
		pm.activateSchemes();

		assertSame(s1, listener.behaviors.get(0).getScheme());
		assertEquals(1, listener.behaviors.size());
	}

	@Test
	public void testActivateSchemes1() {
		// 1 scheme with 1 adding desired
		// 1 scheme with 1 context activated
		MockProceduralMemoryListener listener = new MockProceduralMemoryListener();
		pm.addListener(listener);
		c1.setActivation(1.0);
		s1.addCondition(c1, ConditionType.CONTEXT);

		c2.setIncentiveSalience(1.0);
		s2.addCondition(c2, ConditionType.ADDINGLIST);

		NodeStructure ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addNode(c2, c2.getFactoryType());
		Coalition coal = new CoalitionImpl(ns, null);
		pm.receiveBroadcast(coal);
		pm.activateSchemes();

		Collection<Scheme> actuals = new ArrayList<Scheme>();
		for (Behavior b : listener.behaviors) {
			actuals.add(b.getScheme());
		}

		assertTrue(actuals.contains(s1));
		assertTrue(actuals.contains(s2));
		assertEquals(2, actuals.size());
	}

	@Test
	public void testActivateSchemes2() {
		// multiple context and adding for the same scheme
		MockProceduralMemoryListener listener = new MockProceduralMemoryListener();
		pm.addListener(listener);
		c1.setActivation(1.0);
		c3.setActivation(1.0);
		s1.addCondition(c1, ConditionType.CONTEXT);
		s1.addCondition(c3, ConditionType.CONTEXT);
		c2.setIncentiveSalience(1.0);
		c4.setIncentiveSalience(1.0);
		s1.addCondition(c2, ConditionType.ADDINGLIST);
		s1.addCondition(c4, ConditionType.ADDINGLIST);

		NodeStructure ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c3);
		ns.addNode(c2, c2.getFactoryType());
		ns.addNode(c4, c4.getFactoryType());
		Coalition coal = new CoalitionImpl(ns, null);
		pm.receiveBroadcast(coal);
		pm.activateSchemes();

		assertSame(s1, listener.behaviors.get(0).getScheme());
		assertEquals(1, listener.behaviors.size());
	}

	@Test
	public void testRemoveScheme() {
		s1.addCondition(c1, ConditionType.CONTEXT);
		s1.addCondition(c2, ConditionType.ADDINGLIST);
		pm.removeScheme(s1);
		assertFalse(pm.containsScheme(s1));

		// should not have a reference to the scheme anymore
		MockProceduralMemoryListener listener = new MockProceduralMemoryListener();
		pm.addListener(listener);
		NodeStructure ns = new NodeStructureImpl();
		c1.setActivation(1.0);
		c2.setIncentiveSalience(1.0);
		ns.addDefaultNode((Node) c1);
		ns.addNode(c2, c2.getFactoryType());
		Coalition coal = new CoalitionImpl(ns, null);

		pm.receiveBroadcast(coal);
		;
		pm.activateSchemes();

		assertEquals(0, listener.behaviors.size());
	}

	@Test
	public void testContainsScheme() {
		assertTrue(pm.containsScheme(s1));
		assertTrue(pm.containsScheme(s2));
		assertTrue(pm.containsScheme(s3));
	}

	@Test
	public void testGetSchemeCount() {
		assertEquals(3, pm.getSchemeCount());
		pm.getNewScheme(new ActionImpl());
		assertEquals(4, pm.getSchemeCount());
	}

	@Test
	public void testGetSchemes() {
		Collection<Scheme> schms = pm.getSchemes();
		assertEquals(3, schms.size());
		assertTrue(schms.contains(s1));
		assertTrue(schms.contains(s2));
		assertTrue(schms.contains(s3));
	}

}