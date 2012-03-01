package edu.memphis.ccrg.lida.proceduralmemory;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.Action;
import edu.memphis.ccrg.lida.actionselection.ActionImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Condition;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ExtendedId;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeImpl;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.RootableNode;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.Coalition;
import edu.memphis.ccrg.lida.globalworkspace.CoalitionImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ProceduralMemoryImpl.ConditionType;

public class ProceduralMemoryImplTest {

	private static final ElementFactory factory = ElementFactory.getInstance();
	private ProceduralMemoryImpl pm;
	private Scheme s1, s2, s3;
	private Condition c1, c2, c3, c4;
	private MockTaskSpawner mockTs;
	
	@Before
	public void setUp() throws Exception {
		mockTs = new MockTaskSpawner();
		pm = new ProceduralMemoryImpl();
		pm.setAssistingTaskSpawner(mockTs);
		c1 = factory.getNode("NodeImpl","c1");
		c2 = factory.getNode("RootableNodeImpl","c2");
		c3 = factory.getNode("NodeImpl","c3");
		c4 = factory.getNode("RootableNodeImpl","c4");
		
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
	public void testGetNewScheme(){
		Action a = new ActionImpl("action1");		
		Scheme s = pm.getNewScheme(a);
		assertNotNull(s);
		assertEquals(a, s.getAction());
		assertTrue(pm.containsScheme(s));
	}
	
	@Test
	public void testGetNewScheme2(){
		Action a = new ActionImpl("action1");		
		Scheme s = pm.getNewScheme(a);
		assertNotNull(s);
		assertEquals(a, s.getAction());
		assertTrue(pm.containsScheme(s));
		
		s.addCondition(c1, ConditionType.CONTEXT);	
		s.addCondition(c2, ConditionType.ADDINGLIST);
		
		pm.addCondition(c3);
		Node duplicateCondition = new NodeImpl();
		duplicateCondition.setId(((ExtendedId) c3.getConditionId()).getSourceNodeId());
		s.addCondition(duplicateCondition, ConditionType.CONTEXT);
		Condition actual = s.getContextCondition(c3.getConditionId());
		assertEquals(c3, actual);
		assertNotSame(duplicateCondition, actual);
	}

	@Test
	public void testShouldInstantiate() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("proceduralMemory.schemeSelectionThreshold", 0.9);
		params.put("proceduralMemory.goalOrientedness", 0.0);		
		pm.init(params);
		
		c1.setActivation(0.89);
		c2.setActivation(0.89);
		s1.addCondition(c1,ConditionType.CONTEXT);
		s1.addCondition(c2, ConditionType.ADDINGLIST);
		NodeStructure ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertFalse(pm.shouldInstantiate(s1, ns));
		
		c1.setActivation(0.9);
		c2.setActivation(0.89);
		s1.addCondition(c1,ConditionType.CONTEXT);
		s1.addCondition(c2, ConditionType.ADDINGLIST);
		ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertTrue(pm.shouldInstantiate(s1, ns));
		
		params.put("proceduralMemory.schemeSelectionThreshold", 0.9);
		params.put("proceduralMemory.goalOrientedness", 1.0);		
		pm.init(params);
		
		c1.setActivation(1.0);
		c2.setActivation(0.0);
		((RootableNode) c2).setDesirability(0.89);
		s1.addCondition(c1,ConditionType.CONTEXT);
		s1.addCondition(c2, ConditionType.ADDINGLIST);
		ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertFalse(pm.shouldInstantiate(s1, ns));
		
		c1.setActivation(1.0);
		c2.setActivation(0.0);
		((RootableNode) c2).setDesirability(0.9);
		s1.addCondition(c1,ConditionType.CONTEXT);
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
		assertEquals(s1, listener.behavior.getGeneratingScheme());
	}

	@Test
	public void testReceiveBroadcast() {
		NodeStructure ns = new NodeStructureImpl();
		c1.setActivation(0.5);
		ns.addDefaultNode((Node) c1);
		Coalition coal = new CoalitionImpl(ns, null);
		
		pm.receiveBroadcast(coal);
		
		assertEquals(1, mockTs.tasks.size());
		assertEquals(0, pm.getBroadcastBuffer().getLinkableCount());
		assertEquals(0, pm.getConditionPool().size());
	}
	
	@Test
	public void testReceiveBroadcast1() {
		NodeStructure ns = new NodeStructureImpl();
		c1.setActivation(0.5);
		ns.addDefaultNode((Node) c1);
		Coalition coal = new CoalitionImpl(ns, null);
		s1.addCondition(c1, ConditionType.CONTEXT);
		
		pm.receiveBroadcast(coal);
		
		NodeStructure bb = pm.getBroadcastBuffer();
		assertEquals(1, mockTs.tasks.size());
		assertEquals(1, bb.getLinkableCount());
		assertEquals(1, pm.getConditionPool().size());
		assertEquals(c1,bb.getNode(((Node)c1).getId()));
	}
	
	@Test
	public void testActivateSchemes() {
		fail("Not yet implemented");
	}

	@Test
	public void testLearn() {
	}


	@Test
	public void testDecayModule() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveScheme() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsScheme() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSchemeCount() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetSchemes() {
		fail("Not yet implemented");
	}

}
