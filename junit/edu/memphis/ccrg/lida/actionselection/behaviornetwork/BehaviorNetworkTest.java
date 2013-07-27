package edu.memphis.ccrg.lida.actionselection.behaviornetwork;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.ActionImpl;
import edu.memphis.ccrg.lida.actionselection.Behavior;
import edu.memphis.ccrg.lida.framework.mockclasses.MockTaskSpawner;
import edu.memphis.ccrg.lida.framework.shared.activation.Activatible;
import edu.memphis.ccrg.lida.framework.shared.ns.ElementFactory;
import edu.memphis.ccrg.lida.framework.shared.ns.Node;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTask;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.ns.ProceduralMemory;
import edu.memphis.ccrg.lida.proceduralmemory.ns.ProceduralMemoryImpl;
import edu.memphis.ccrg.lida.proceduralmemory.ns.ProceduralMemoryImpl.ConditionType;

/**
 * Test for {@link BehaviorNetwork} class.
 * 
 * @author Ryan J. McCall
 * @author Javier Snaider
 */
public class BehaviorNetworkTest {

	private static final ElementFactory factory = ElementFactory.getInstance();
	private double epsilon = 10e-9;

	private TaskSpawner ts;
	private ProceduralMemory pm;
	private BehaviorNetwork bNetwork;
	private MockActionSelectionListener listener;
	/*
	 * passActivationFromSchemes(); passActivationAmongBehaviors();
	 * attemptActionSelection();
	 */
	private FrameworkTask backgroundTask;
	private Map<String, Object> params;
	private Scheme s1, s2, s3, s4, s5;
	private Node n1, n2, n3, n4, n5;

	@Before
	public void setUp() throws Exception {
		bNetwork = new BehaviorNetwork();

		listener = new MockActionSelectionListener();
		bNetwork.addListener(listener);

		ts = new MockTaskSpawner();
		bNetwork.setAssistingTaskSpawner(ts);

		params = new HashMap<String, Object>();
		params.put("actionselection.broadcastExcitationFactor", 1.0);
		params.put("actionselection.successorExcitationFactor", 0.5);
		params.put("actionselection.predecessorExcitationFactor", 0.25);
		params.put("actionselection.conflictorExcitationFactor", 0.1);
		params.put("actionselection.contextSatisfactionThreshold", 0.5);
		params.put("actionselection.initialCandidateThreshold", 1.0);
		params.put("actionselection.candidateThresholdDecayName",
				"defaultDecay");
		// Warning: #init() must be called only once because it creates a
		// background task each time it runs
		bNetwork.init(params);
		backgroundTask = ts.getTasks().iterator().next();

		// schemes
		pm = new ProceduralMemoryImpl();
		s1 = pm.getNewScheme(new ActionImpl());
		s2 = pm.getNewScheme(new ActionImpl());
		s3 = pm.getNewScheme(new ActionImpl());
		s4 = pm.getNewScheme(new ActionImpl());
		s5 = pm.getNewScheme(new ActionImpl());

		// nodes
		n1 = factory.getNode();
		n2 = factory.getNode();
		n3 = factory.getNode();
		n4 = factory.getNode();
		n5 = factory.getNode();
	}

	@Test
	public void testPassFromSchemes0() {
		params.put("actionselection.broadcastExcitationFactor", 0.10);
		bNetwork.init(params);

		n1.setActivation(1.0);
		n2.setActivation(0.49);
		n3.setActivation(0.0);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s2.addCondition(n2, ConditionType.CONTEXT);
		s3.addCondition(n3, ConditionType.CONTEXT);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);
		Behavior b3 = factory.getBehavior(s3);

		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);
		bNetwork.receiveBehavior(b3);

		bNetwork.passActivationFromSchemes();

		assertEquals(0.1, b1.getActivation(), epsilon);
		assertEquals(0.049, b2.getActivation(), epsilon);
		assertEquals(0.0, b3.getActivation(), epsilon);

		bNetwork.passActivationFromSchemes();

		assertEquals(0.2, b1.getActivation(), epsilon);
		assertEquals(0.098, b2.getActivation(), epsilon);
		assertEquals(0.0, b3.getActivation(), epsilon);
	}

	@Test
	public void testPassAmongBehaviors() {
		n1.setActivation(0.0);
		n2.setActivation(0.5);
		n3.setActivation(0.0);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s2.addCondition(n2, ConditionType.CONTEXT);
		s3.addCondition(n1, ConditionType.ADDINGLIST);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);
		Behavior b3 = factory.getBehavior(s3);
		b3.setActivation(1.0);

		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);
		bNetwork.receiveBehavior(b3);

		bNetwork.passActivationAmongBehaviors();

		assertEquals(0.5, b1.getActivation(), epsilon);
		assertEquals(0.0, b2.getActivation(), epsilon);
		assertEquals(1.0, b3.getActivation(), epsilon);
	}

	@Test
	public void testPassAmongBehaviors1() {
		n1.setActivation(0.0);
		n2.setActivation(0.5);
		n3.setActivation(0.0);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s2.addCondition(n1, ConditionType.ADDINGLIST);
		s3.addCondition(n1, ConditionType.ADDINGLIST);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);
		Behavior b3 = factory.getBehavior(s3);
		b2.setActivation(0.5);
		b3.setActivation(1.0);

		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);
		bNetwork.receiveBehavior(b3);

		bNetwork.passActivationAmongBehaviors();

		assertTrue(0.75 <= b1.getActivation());
		assertTrue(0.5 <= b2.getActivation());
		assertEquals(1.0, b3.getActivation(), epsilon);
	}

	@Test
	public void testPassAmongBehaviors2() {
		n1.setActivation(0.0);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s2.addCondition(n1, ConditionType.ADDINGLIST);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);
		b1.setActivation(0.5);
		b2.setActivation(0.0);

		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);

		bNetwork.passActivationAmongBehaviors();

		assertTrue(0.5 <= b1.getActivation());
		assertEquals(0.125, b2.getActivation(), epsilon);
	}

	@Test
	public void testPassToConflictor() {
		n1.setActivation(0.5);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s2.addCondition(n1, ConditionType.DELETINGLIST);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);

		b1.setActivation(0.5);
		b2.setActivation(1.0);

		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);

		bNetwork.passActivationAmongBehaviors();

		assertEquals(0.5, b1.getActivation(), epsilon);
		assertEquals(0.95, b2.getActivation(), epsilon);
	}

	@Test
	public void testPassToConflictor1() {
		n1.setActivation(0.5);
		n2.setActivation(0.5);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s1.addCondition(n2, ConditionType.DELETINGLIST);

		s2.addCondition(n2, ConditionType.CONTEXT);
		s2.addCondition(n1, ConditionType.DELETINGLIST);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);

		b1.setActivation(0.5);
		b2.setActivation(1.0);

		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);

		bNetwork.passActivationAmongBehaviors();

		assertEquals(0.4, b1.getActivation(), epsilon);
		assertEquals(1.0, b2.getActivation(), epsilon);
	}

	@Test
	public void testAttemptActionSelection() {
		n1.setActivation(0.1);
		n2.setActivation(0.1);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s1.addCondition(n2, ConditionType.DELETINGLIST);

		s2.addCondition(n2, ConditionType.CONTEXT);
		s2.addCondition(n1, ConditionType.DELETINGLIST);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);

		b1.setActivation(0.5);
		b2.setActivation(1.0);
		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);

		bNetwork.attemptActionSelection();

		assertNull(listener.action);
		assertEquals(0.9, bNetwork.getCandidateThreshold(), epsilon);// threshold
																		// was
																		// reduced

		bNetwork.attemptActionSelection();

		// the behaviors' context conditions are not satisfied
		assertNull(listener.action);
		assertEquals(0.8, bNetwork.getCandidateThreshold(), epsilon);// threshold
																		// was
																		// reduced
	}

	@Test
	public void testAttemptActionSelection2() {
		n1.setActivation(0.6);
		n2.setActivation(0.6);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s1.addCondition(n2, ConditionType.DELETINGLIST);

		s2.addCondition(n2, ConditionType.CONTEXT);
		s2.addCondition(n1, ConditionType.DELETINGLIST);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);

		b1.setActivation(0.5);
		b2.setActivation(1.0);
		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);

		bNetwork.attemptActionSelection();

		assertNull(listener.action);
		assertEquals(0.9, bNetwork.getCandidateThreshold(), epsilon);// threshold
																		// was
																		// reduced

		bNetwork.attemptActionSelection();

		assertEquals(b2.getAction(), listener.action);
		assertEquals(0.0, b2.getActivation(), epsilon);
		assertEquals(1.0, bNetwork.getCandidateThreshold(), epsilon);
	}

	@Test
	public void testRunEmpty() {
		assertEquals(1.0, bNetwork.getCandidateThreshold(), epsilon);
		runBackgroundTask();
		// 1 decay of 0.1
		assertEquals(0.9, bNetwork.getCandidateThreshold(), epsilon);
	}

	private void runBackgroundTask() {
		try {
			backgroundTask.call();
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue(false);
		}
	}

	@Test
	public void testRunEmpty1() {
		assertEquals(1.0, bNetwork.getCandidateThreshold(), epsilon);
		for (int i = 0; i < 11; i++) {
			runBackgroundTask();
		}
		// 11 decays of 0.1
		assertEquals(0.0, bNetwork.getCandidateThreshold(), epsilon);
	}

	@Test
	public void testReceiveBehavior() {
		s1.setActivation(1.0);
		s2.setActivation(0.5);
		s3.setActivation(0.0);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);
		Behavior b3 = factory.getBehavior(s3);
		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);
		bNetwork.receiveBehavior(b3);

		Collection<Behavior> behaviors = bNetwork.getBehaviors();
		assertTrue(behaviors.contains(b1));
		assertTrue(behaviors.contains(b2));
		assertTrue(behaviors.contains(b3));
	}

	@Test
	public void testPassFromSchemes() {
		n1.setActivation(1.0);
		n2.setActivation(0.49);
		n3.setActivation(0.0);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s2.addCondition(n2, ConditionType.CONTEXT);
		s3.addCondition(n3, ConditionType.CONTEXT);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);
		Behavior b3 = factory.getBehavior(s3);

		assertEquals(Activatible.DEFAULT_ACTIVATION, b1.getActivation(),
				epsilon);
		assertEquals(Activatible.DEFAULT_ACTIVATION, b2.getActivation(),
				epsilon);
		assertEquals(Activatible.DEFAULT_ACTIVATION, b3.getActivation(),
				epsilon);

		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);
		bNetwork.receiveBehavior(b3);
		runBackgroundTask();

		assertNull(listener.action);
		assertEquals(1.0, b1.getActivation(), epsilon);
		assertEquals(0.49, b2.getActivation(), epsilon);
		assertEquals(0.0, b3.getActivation(), epsilon);

		runBackgroundTask();

		// b1 was selected
		assertSame(b1.getAction(), listener.action);
		assertEquals(0.0, b1.getActivation(), epsilon);
		assertEquals(0.98, b2.getActivation(), epsilon);
		assertEquals(0.0, b3.getActivation(), epsilon);
		assertEquals(1.0, bNetwork.getCandidateThreshold(), epsilon);// threshold
																		// was
																		// reset
	}

	@Test
	public void testDecayModule() {
		n1.setActivation(1.0);
		n2.setActivation(0.49);
		n3.setActivation(0.0);
		s1.addCondition(n1, ConditionType.CONTEXT);
		s2.addCondition(n2, ConditionType.CONTEXT);
		s3.addCondition(n3, ConditionType.CONTEXT);
		Behavior b1 = factory.getBehavior(s1);
		Behavior b2 = factory.getBehavior(s2);
		Behavior b3 = factory.getBehavior(s3);

		bNetwork.receiveBehavior(b1);
		bNetwork.receiveBehavior(b2);
		bNetwork.receiveBehavior(b3);

		runBackgroundTask();

		assertNull(listener.action);
		assertEquals(1.0, b1.getActivation(), epsilon);
		assertEquals(0.49, b2.getActivation(), epsilon);
		assertEquals(0.0, b3.getActivation(), epsilon);

		bNetwork.decayModule(1);

		Collection<Behavior> behaviors = bNetwork.getBehaviors();
		assertEquals(2, behaviors.size());
		assertEquals(0.9, b1.getActivation(), epsilon);
		assertEquals(0.39, b2.getActivation(), epsilon);
	}
}