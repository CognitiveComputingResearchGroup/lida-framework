package edu.memphis.ccrg.lida.proceduralmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.AgentAction;
import edu.memphis.ccrg.lida.actionselection.AgentActionImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.framework.shared.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class SchemeImplTest{

	private Scheme s;
	private AgentAction a;
	private Node node1, node2, node3;
	
	private static ElementFactory factory = ElementFactory.getInstance();
	
	@Before
	public void setUp() throws Exception {		
		a = new AgentActionImpl() {
			@Override
			public void performAction() {}
		};

		s = new SchemeImpl("1", a);
		node1 = factory.getNode();
		node2 = factory.getNode();
		node3 = factory.getNode();
		
	}
	
	@Test
	public void test1(){
		assertEquals("1", s.getLabel());
		assertEquals(a, s.getAction());
		
		s = new SchemeImpl();
		s.setLabel("1");
		assertEquals("1", s.getLabel());
		
		s.setAction(a);
		assertEquals(a, s.getAction());
	}
	@Test
	public void test2(){
		AgentAction foo = new AgentActionImpl() {
			@Override public void performAction() {}};
		s.setAction(foo);
		assertEquals(foo, s.getAction());
		
		NodeStructure ns = new NodeStructureImpl();
		s.setAddingResult(ns);
		assertEquals(ns, s.getAddingResult());
		
		s.setContext(ns);
		assertEquals(ns, s.getContext());
		
		s.setDeletingResult(ns);
		assertEquals(ns, s.getDeletingResult());
		
		assertFalse(s.isInnate());
		s.setInnate(true);
		assertTrue(s.isInnate());
	}
	@Test
	public void test3(){		
		assertTrue(0 == s.getExecutions());
		assertTrue(0.0 == s.getReliability());
		assertFalse(s.isReliable());
		
		s.actionSuccessful();
		
		assertTrue(0 == s.getExecutions());
		assertTrue(0.0 == s.getReliability());
		assertFalse(s.isReliable());
		
		s.actionExecuted();
		s.actionExecuted();
		s.actionExecuted();
		s.actionExecuted();
		
		assertTrue(4 == s.getExecutions());
		assertTrue(0.25 == s.getReliability());
		assertFalse(s.isReliable());
		
		s.actionSuccessful();
		s.actionSuccessful();
		s.actionSuccessful();
		
		assertTrue(s.isReliable());
	}
	@Test
	public void testEquals(){
		//TODO
//		Scheme s2 = new SchemeImpl();
//		s2.setId(2);
//		
//		Scheme sSame = new SchemeImpl();
//		sSame.setId(s2.getId());
//		
//		assertEquals(s2, sSame);
//		assertEquals(s2.hashCode(), sSame.hashCode());
//		
//		assertFalse(s.equals(s2));
	}
	@Test
	public void testGetInstantiation(){
		s.setActivation(0.99);
		NodeStructure context = new NodeStructureImpl();
		context.addDefaultNode(node1);
		s.setContext(context);
		
		NodeStructure deleting = new NodeStructureImpl();
		deleting.addDefaultNode(node2);
		s.setDeletingResult(deleting);
		
		NodeStructure adding = new NodeStructureImpl();
		adding.addDefaultNode(node3);
		s.setAddingResult(adding);
		
		Behavior b = s.getInstantiation();
		assertEquals(a, b.getAction());
		assertTrue(0.99 == b.getActivation());
		assertEquals(s, b.getGeneratingScheme());
		
		assertTrue(b.containsContextCondition(node1));
		assertTrue(b.containsDeletingItem(node2));
		assertTrue(b.containsAddingItem(node3));
	}

}
