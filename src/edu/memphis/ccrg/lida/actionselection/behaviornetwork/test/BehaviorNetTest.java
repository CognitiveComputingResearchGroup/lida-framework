/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.behaviornetwork.test;

import java.util.HashMap;
import java.util.Map;

import edu.memphis.ccrg.lida.actionselection.ActionSelectionDriver;
import edu.memphis.ccrg.lida.actionselection.ActionSelectionListener;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.Behavior;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorImpl;
import edu.memphis.ccrg.lida.actionselection.behaviornetwork.main.BehaviorNetworkImpl;
import edu.memphis.ccrg.lida.actionselection.triggers.ActionSelectionTrigger;
import edu.memphis.ccrg.lida.actionselection.triggers.NoActionSelectionOccurringTrigger;
import edu.memphis.ccrg.lida.actionselection.triggers.TriggerTask;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeFactory;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;

/**
 * @author Ryan J McCall, Javier Snaider
 */
public class BehaviorNetTest implements ActionSelectionListener{

	public static void main(String[] args) {
		new BehaviorNetTest().run();				
	}

	private NodeFactory factory = NodeFactory.getInstance();
	private BehaviorNetworkImpl behaviorNet = new BehaviorNetworkImpl();
	
	private void run() {
		
		behaviorNet.addActionSelectionListener(this);
		
		//LidaTaskManager ltm = new LidaTaskManager(0, 100);
		//ltm.resumeSpawnedTasks();
		//ActionSelectionDriver taskSpawner = new ActionSelectionDriver(1, ltm);
		//NoActionSelectionOccurringTrigger t = new NoActionSelectionOccurringTrigger();
		//t.setUp(new HashMap<String,Object>(),behaviorNet, taskSpawner);
		//taskSpawner.addActionSelectionTrigger(t);
		
		//taskSpawner.start();		
		TaskSpawner mockTS = new MockTaskSpawner();
		behaviorNet.setTaskSpawner(mockTS);
		
		Map<String, Double> params = new HashMap<String, Double>();
		//params.put("", value)		
		behaviorNet.init(params);
		
		Node near = getNewNode("near");
		Node far = getNewNode("far");
		Node standing = getNewNode("standing");
		Node sitting = getNewNode("sitting");
		
		Node thirsty = getNewNode("thirsty");
		Node drunk = getNewNode("drunk");
		Node hungry = getNewNode("hungry");
		Node full = getNewNode("full");
		
		Node right = getNewNode("right");
		Node left = getNewNode("left");
		
		Node banana = getNewNode("banana");
		Node cerveza = getNewNode("cerveza");
		Node table = getNewNode("table");
		
		
		long grabAction = 1L;
		long goLeftAction = 2L;
		long goRightAction = 3L;
		long drinkAction = 4L;
		long eatAction = 5L;
		long standAction = 6L;
		
		Behavior b = getNewBehavior("eat", eatAction, full, hungry, banana);
		behaviorNet.receiveBehavior(b);
		
		b = getNewBehavior("drink", drinkAction, drunk, thirsty, cerveza);
		behaviorNet.receiveBehavior(b);
		
		b = getNewBehavior("turn left", goLeftAction, banana, standing);
		behaviorNet.receiveBehavior(b);
		
		b = getNewBehavior("turn right", goRightAction, cerveza, standing);
		behaviorNet.receiveBehavior(b);

		b = getNewBehavior("stand", standAction, standing, sitting);
		behaviorNet.receiveBehavior(b);
		
		//broadcasts
		NodeStructure bc = getBroadcast(thirsty, sitting);
		runOneStep(bc);
		
		bc = getBroadcast(thirsty, standing);
		runOneStep(bc);

        bc = getBroadcast(thirsty, cerveza);
		runOneStep(bc);
	}
	
	public NodeStructure getBroadcast(Node... broadcastNodes){
		NodeStructure bc = new NodeStructureImpl();
		for(Node n: broadcastNodes){
			n.setActivation(0.9);
			bc.addNode(n);
		}
		return bc;
	}
	
	private int step = 0;
	public void runOneStep(NodeStructure bc){
		System.out.println("\nRun: " + step++);
		behaviorNet.receiveBroadcast((BroadcastContent) bc);
		behaviorNet.selectAction();
		behaviorNet.decayModule(10);
	}
	
	public Behavior getNewBehavior(String label, long actionId, Node result, Node...context){
		Behavior b = new BehaviorImpl(actionId);
		b.setLabel(label);
		b.addToAddingList(result);
		for(Node n: context)
			b.addContextCondition(n);
		return b;
	}
	
	public Node getNewNode(String label){
		Node n = factory.getNode("NodeImpl", label);
		n.setActivation(0.0);
		return n;
	}
	
	@Override
	public void receiveActionId(long id) {
		System.out.println("Result: Received action " + id);
	}

}
