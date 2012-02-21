package edu.memphis.ccrg.lida.proceduralmemory;

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
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.shared.RootableNode;

public class ProceduralMemoryImplTest {

	private static final ElementFactory factory = ElementFactory.getInstance();
	private ProceduralMemory pm;
	private Scheme s1, s2, s3;
	private Condition c1, c2, c3, c4;
	
	@Before
	public void setUp() throws Exception {
		pm = new ProceduralMemoryImpl();
		c1 = factory.getNode("NodeImpl","c1");
		c2 = factory.getNode("RootableNodeImpl","c2");
		c3 = factory.getNode("NodeImpl","c3");
		c4 = factory.getNode("RootableNodeImpl","c4");
		s1 = new SchemeImpl("s1", new ActionImpl("action1"));
		s2 = new SchemeImpl("s2", new ActionImpl("action2"));
		s3 = new SchemeImpl("s3", new ActionImpl("action3"));
	}

	@Test
	public void testShouldInstantiate() {
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("proceduralMemory.schemeSelectionThreshold", 0.9);
		params.put("proceduralMemory.goalOrientedness", 0.0);		
		pm.init(params);
		
		c1.setActivation(0.89);
		c2.setActivation(0.89);
		s1.addContextCondition(c1);
		s1.addToAddingList(c2);
		NodeStructure ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertFalse(pm.shouldInstantiate(s1, ns));
		
		c1.setActivation(0.9);
		c2.setActivation(0.89);
		s1.addContextCondition(c1);
		s1.addToAddingList(c2);
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
		s1.addContextCondition(c1);
		s1.addToAddingList(c2);
		ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertFalse(pm.shouldInstantiate(s1, ns));
		
		c1.setActivation(1.0);
		c2.setActivation(0.0);
		((RootableNode) c2).setDesirability(0.9);
		s1.addContextCondition(c1);
		s1.addToAddingList(c2);
		ns = new NodeStructureImpl();
		ns.addDefaultNode((Node) c1);
		ns.addDefaultNode((Node) c2);
		assertTrue(pm.shouldInstantiate(s1, ns));
	}
	
	@Test
	public void testSendInstantiatedScheme() {
		MockProceduralMemoryListener listener = new MockProceduralMemoryListener();
		pm.addListener(listener);
		Action a = new ActionImpl();
		Scheme s = new SchemeImpl("foo", a);
		
		pm.createInstantiation(s);
		
		assertEquals(listener.behavior.getAction(), a);
		assertEquals(listener.behavior.getLabel(), "foo");
	}

	@Test
	public void testAddScheme() {
		fail("Not yet implemented");
	}
	
	@Test
	public void testAddSchemes() {
		fail("Not yet implemented");
	}

	@Test
	public void testAddCondition() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetCondition() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveBroadcast() {
		fail("Not yet implemented");
	}

	@Test
	public void testLearn() {
		fail("Not yet implemented");
	}

	@Test
	public void testActivateSchemes() {
		fail("Not yet implemented");
	}


	@Test
	public void testCreateInstantiation() {
		fail("Not yet implemented");
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
