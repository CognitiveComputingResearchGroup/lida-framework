package edu.memphis.ccrg.lida.proceduralmemory;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;

import edu.memphis.ccrg.lida.actionselection.AgentAction;
import edu.memphis.ccrg.lida.actionselection.AgentActionImpl;

public class ProceduralMemoryImplTest{

	private ProceduralMemory pm;

	@Before
	public void setUp() throws Exception {
		pm = new ProceduralMemoryImpl();
	}

	public void testAddScheme() {
		Scheme s = new SchemeImpl();
		pm.addScheme(s);

		assertTrue(pm.containsScheme(s));
		assertTrue(pm.getSchemeCount() == 1);

		pm.addScheme(s);
		assertTrue(pm.containsScheme(s));
		assertTrue(pm.getSchemeCount() == 1);

		s = new SchemeImpl();
		pm.addScheme(s);
		assertTrue(pm.containsScheme(s));
		assertTrue(pm.getSchemeCount() == 2);
	}

	public void testSetSchemeActivationStrategy() {
		SchemeActivationStrategy b = new BasicSchemeActivationStrategy(pm);
		pm.setSchemeActivationStrategy(b);
		assertEquals(b, pm.getSchemeActivationStrategy());
	}

	public void testSendInstantiatedScheme() {
		MockProceduralMemoryListener listener = new MockProceduralMemoryListener();
		pm.addListener(listener);
		AgentAction a = new AgentActionImpl(){
			@Override
			public void performAction() {
			}
		};
		Scheme s = new SchemeImpl("foo", a);
		
		pm.createInstantiation(s);
		
		assertEquals(listener.behavior.getAction(), a);
		assertEquals(listener.behavior.getLabel(), "foo");
	}

}