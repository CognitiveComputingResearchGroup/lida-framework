package edu.memphis.ccrg.lida.proceduralmemory;


import org.junit.Before;
import static org.junit.Assert.*;

import edu.memphis.ccrg.lida.actionselection.AgentAction;
import edu.memphis.ccrg.lida.actionselection.AgentActionImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
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
		
		final AgentAction a = new AgentActionImpl() {
			@Override
			public void performAction() {
			}
		};
		
		ProceduralMemoryListener l = new ProceduralMemoryListener() {	
			@Override
			public void receiveBehavior(Behavior behavior) {
				assertEquals(behavior.getAction(), a);
				System.out.println("was run");
			}
		};
		
		pm.addListener(l);
		
		Scheme s = new SchemeImpl("foo", a);
		pm.createInstantiation(s);
	}

}
