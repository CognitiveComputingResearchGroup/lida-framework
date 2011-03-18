package edu.memphis.ccrg.lida.proceduralmemory;

import junit.framework.TestCase;

import org.junit.Before;

import edu.memphis.ccrg.lida.actionselection.LidaAction;
import edu.memphis.ccrg.lida.actionselection.LidaActionImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
public class ProceduralMemoryImplTest extends TestCase {

	private ProceduralMemory pm;

	@Override
	@Before
	public void setUp() throws Exception {
		pm = new ProceduralMemoryImpl();
	}

	public void testAddScheme() {
		Scheme s = new SchemeImpl();
		s.setId(100);
		pm.addScheme(s);

		assertTrue(pm.containsScheme(s));
		assertTrue(pm.getSchemeCount() == 1);

		pm.addScheme(s);
		assertTrue(pm.containsScheme(s));
		assertTrue(pm.getSchemeCount() == 1);

		s.setId(101);
		pm.addScheme(s);
		assertTrue(pm.containsScheme(s));
		assertTrue(pm.getSchemeCount() == 2);
	}

	public void testSetSchemeActivationBehavior() {
		SchemeActivationBehavior b = new BasicSchemeActivationBehavior(pm);
		pm.setSchemeActivationBehavior(b);
		assertEquals(b, pm.getSchemeActivationBehavior());
	}

	public void testSendInstantiatedScheme() {
		
		final LidaAction a = new LidaActionImpl() {
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
		
		Scheme s = new SchemeImpl("foo", 1, a);
		pm.sendInstantiatedScheme(s);
	}

}
