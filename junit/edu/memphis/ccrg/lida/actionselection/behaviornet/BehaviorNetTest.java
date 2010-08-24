package edu.memphis.ccrg.lida.actionselection.behaviornet;

import org.junit.Before;
import org.junit.Test;

import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorNetworkImpl;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;

import junit.framework.TestCase;


public class BehaviorNetTest extends TestCase{
	
	NodeFactory factory = NodeFactory.getInstance();
	
	@Before
	public void setUp(){
		
	}
	
	@Test
	public void test1(){
		Behavior a = new BehaviorImpl(1, 10);
		Behavior b = new BehaviorImpl(2, 20);
		
		BehaviorNetworkImpl behaviorNet = new BehaviorNetworkImpl();
		behaviorNet.receiveBehavior(a);
		behaviorNet.receiveBehavior(b);
		
		a = behaviorNet.getBehavior(1, 10);
		b = behaviorNet.getBehavior(2, 20);
		
		assertEquals("nothingA1", a.getPreconditionCount(), 0);
		assertEquals("nothingB1", b.getPreconditionCount(), 0);
		//
		assertEquals("nothingA2", a.getSuccessorCount(), 0);
		assertEquals("nothingB2", b.getSuccessorCount(), 0);
		//
		assertEquals("nothingA3", a.getPredecessorCount(), 0);
		assertEquals("nothingB3", b.getPredecessorCount(), 0);
		//
		assertEquals("nothingA4", a.getConflictorCount(), 0);
		assertEquals("nothingB4", b.getConflictorCount(), 0);
	}//method
	
	public Node gimmeNode(String label){
		return factory.getNode("NodeImpl", label);
	}
	
	@Test
	public void test2(){
		Behavior a = new BehaviorImpl(1, 10);
		Behavior b = new BehaviorImpl(2, 20);
		
		Node apple = gimmeNode("apple");
		a.addPrecondition(apple);
		b.addDeleteCondition(apple);
		
		BehaviorNetworkImpl behaviorNet = new BehaviorNetworkImpl();
		behaviorNet.receiveBehavior(a);
		behaviorNet.receiveBehavior(b);
		
		a = behaviorNet.getBehavior(1, 10);
		b = behaviorNet.getBehavior(2, 20);
		
		assertEquals("appleA1", a.getPreconditionCount(), 1);
		assertEquals("appleB1", b.getPreconditionCount(), 0);
		//
		assertEquals("appleA2", a.getSuccessorCount(), 0);
		assertEquals("appleB2", b.getSuccessorCount(), 0);
		//
		assertEquals("appleA3", a.getPredecessorCount(), 0);
		assertEquals("appleB3", b.getPredecessorCount(), 0);
		//
		assertEquals("appleA4", a.getConflictorCount(), 1);
		assertEquals("appleC4", a.getConflictors(apple).get(0), b);
		assertEquals("appleB4", b.getConflictorCount(), 0);
		
	}//method
	
	@Test
	public void test3(){
		Behavior a = new BehaviorImpl(1, 10);
		Behavior b = new BehaviorImpl(2, 20);
		
		Node banana = gimmeNode("banana");
		a.addPrecondition(banana);
		b.addAddCondition(banana);
		
		BehaviorNetworkImpl behaviorNet = new BehaviorNetworkImpl();
		behaviorNet.receiveBehavior(a);
		behaviorNet.receiveBehavior(b);
		
		a = behaviorNet.getBehavior(1, 10);
		b = behaviorNet.getBehavior(2, 20);
		
		assertEquals("bananaA1", a.getPreconditionCount(), 1);
		assertEquals("bananaB1", b.getPreconditionCount(), 0);
		//
		assertEquals("bananaA2", a.getSuccessorCount(), 0);
		assertEquals("bananaB2", b.getSuccessorCount(), 1);
		assertEquals("c2", b.getSuccessors(banana).get(0), a);
		//
		assertEquals("bananaA3", a.getPredecessorCount(), 1);
		assertEquals("c3", a.getPredecessors(banana).get(0), b);
		assertEquals("bananaB3", b.getPredecessorCount(), 0);
		//
		assertEquals("bananaA4", a.getConflictorCount(), 0);
		assertEquals("bananaB4", b.getConflictorCount(), 0);
		
	}//method
	
	@Test
	public void test4(){
		Behavior a = new BehaviorImpl(1, 10);
		Behavior b = new BehaviorImpl(2, 20);
		
		Node banana = gimmeNode("banana");
		a.addPrecondition(banana);
		b.addAddCondition(banana);
		
		Node apple = gimmeNode("apple");
		//TODO
		
		BehaviorNetworkImpl behaviorNet = new BehaviorNetworkImpl();
		behaviorNet.receiveBehavior(a);
		behaviorNet.receiveBehavior(b);
		
		a = behaviorNet.getBehavior(1, 10);
		b = behaviorNet.getBehavior(2, 20);
		
		assertEquals("bananaA1", a.getPreconditionCount(), 1);
		assertEquals("bananaB1", b.getPreconditionCount(), 0);
		//
		assertEquals("bananaA2", a.getSuccessorCount(), 0);
		assertEquals("bananaB2", b.getSuccessorCount(), 1);
		assertEquals("c2", b.getSuccessors(banana).get(0), a);
		//
		assertEquals("bananaA3", a.getPredecessorCount(), 1);
		assertEquals("c3", a.getPredecessors(banana).get(0), b);
		assertEquals("bananaB3", b.getPredecessorCount(), 0);
		//
		assertEquals("bananaA4", a.getConflictorCount(), 0);
		assertEquals("bananaB4", b.getConflictorCount(), 0);
		
	}//method

}//class