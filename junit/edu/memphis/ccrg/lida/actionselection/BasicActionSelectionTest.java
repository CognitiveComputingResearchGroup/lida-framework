package edu.memphis.ccrg.lida.actionselection;

import static org.junit.Assert.*;

import java.util.Collection;
import java.util.LinkedList;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.BehaviorImpl;
import edu.memphis.ccrg.lida.framework.mockclasses.MockSensoryMotorMemory;
import edu.memphis.ccrg.lida.proceduralmemory.Scheme;
import edu.memphis.ccrg.lida.proceduralmemory.SchemeImpl;

public class BasicActionSelectionTest {
	
	BasicActionSelection as;
	Behavior behav1,behav2;	
	MockSensoryMotorMemory smm = new MockSensoryMotorMemory();
	Scheme scheme1,scheme2 ;
		
	AgentAction action1 = new AgentActionImpl() {
		@Override
		public void performAction() {
		}
	};
	
	AgentAction action2 = new AgentActionImpl() {
		@Override
		public void performAction() {
		}
	};
	
	@Before
	public void setUp() throws Exception {				
		
		scheme1= new SchemeImpl("scheme1",action1);
		scheme2= new SchemeImpl("scheme1",action2);
		
		as = new BasicActionSelection();
		
		behav1 = new BehaviorImpl();
		behav2 = new BehaviorImpl();
				
		behav1 = scheme1.getInstantiation();
		behav2 = scheme2.getInstantiation();
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetModuleContent() {
		as.receiveBehavior(behav1);
		Collection<Behavior> content = (Collection<Behavior>) as.getModuleContent("behaviors");
		
		assertTrue("Problem with GetModuleContent", content.size()>0);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testReceiveBehavior() {
		as.receiveBehavior(behav1);
		as.receiveBehavior(behav2);
		Collection<Behavior> content = (Collection<Behavior>) as.getModuleContent("behaviors");
		
		assertTrue("Problem with ReceiveBehavior", content.size()>1);
	}

	@Test
	public void testSelectAction() {
		as.receiveBehavior(behav1);
		as.receiveBehavior(behav2);
		
		as.addListener(smm);		
		as.selectAction();
		
		assertTrue("Problem with SelectAction",smm.actionReceived());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testGetState() {
		as.receiveBehavior(behav1);
		as.receiveBehavior(behav2);
		
		Object[] state = (Object[])as.getState();
		Collection<Behavior> content = (Collection<Behavior>)state[0];
		
		assertTrue("Problem with GetState", content.size()>1);
	}

	@SuppressWarnings("unchecked")
	@Test
	public void testSetState() {
		Collection<Behavior> behaviors = new LinkedList<Behavior>();
		behaviors.add(behav1);
		behaviors.add(behav2);
		
		as.setState(behaviors);
		
		Object[] state = (Object[])as.getState();
		Collection<Behavior> content = (Collection<Behavior>)state[0];
		
		assertTrue("Problem with GetState", content.size()>1);
		
	}

}
