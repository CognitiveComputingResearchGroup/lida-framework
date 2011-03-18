package edu.memphis.ccrg.lida.proceduralmemory;

import junit.framework.TestCase;

import org.junit.Before;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.actionselection.LidaActionImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.framework.shared.LidaElementFactory;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;

public class SchemeImplTest extends TestCase {

	private Scheme s;
	private LidaAction a;
	private Node node1, node2, node3;
	
	private static LidaElementFactory factory = LidaElementFactory.getInstance();
	
	@Override
	@Before
	public void setUp() throws Exception {		
		a = new LidaActionImpl() {
			@Override
			public void performAction() {}
		};

		s = new SchemeImpl("1", 1, a);
		node1 = factory.getNode();
		node2 = factory.getNode();
		node3 = factory.getNode();
		
	}
	
	public void test1(){
		assertEquals("1", s.getLabel());
		assertEquals(1, s.getId());
		assertEquals(a, s.getAction());
		
		s = new SchemeImpl();
		s.setLabel("1");
		assertEquals("1", s.getLabel());
		s.setId(1);
		assertEquals(1, s.getId());
		s.setAction(a);
		assertEquals(a, s.getAction());
	}
	
	public void test2(){
		LidaAction foo = new LidaActionImpl() {
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
	
	public void testEquals(){
		Scheme s2 = new SchemeImpl();
		s2.setId(2);
		
		Scheme sSame = new SchemeImpl();
		sSame.setId(s2.getId());
		
		assertEquals(s2, sSame);
		assertEquals(s2.hashCode(), sSame.hashCode());
		
		assertFalse(s.equals(s2));
	}
	
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
		assertEquals(0.99, b.getActivation());
		assertEquals(s, b.getGeneratingScheme());
		
		assertTrue(b.containsContextCondition(node1));
		assertTrue(b.containsDeletingItem(node2));
		assertTrue(b.containsAddingItem(node3));
	}

}
