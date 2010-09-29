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
 * @author Ryan J McCall
 */
public class BehaviorNetTest implements ActionSelectionListener{

	public static void main(String[] args) {
		new BehaviorNetTest().run();				
	}

	private void run() {
		BehaviorNetworkImpl behaviorNet = new BehaviorNetworkImpl();
		behaviorNet.addActionSelectionListener(this);
		
		LidaTaskManager ltm = new LidaTaskManager(0, 100);
		ltm.resumeSpawnedTasks();
		ActionSelectionDriver taskSpawner = new ActionSelectionDriver(1, ltm);
		NoActionSelectionOccurringTrigger t = new NoActionSelectionOccurringTrigger();
		t.setUp(new HashMap<String,Object>(),behaviorNet, taskSpawner);
		taskSpawner.addActionSelectionTrigger(t);
		
		taskSpawner.start();		
		behaviorNet.setTaskSpawner(taskSpawner);
		
		Map<String, Double> params = new HashMap<String, Double>();
		//params.put("", value)		
		behaviorNet.init(params);
		
		NodeFactory factory = NodeFactory.getInstance();
		NodeStructure bc = new NodeStructureImpl();
		Node near = factory.getNode("NodeImpl", "near");
		Node far = factory.getNode("NodeImpl", "far");
		Node banana = factory.getNode("NodeImpl", "banana");
		Node have = factory.getNode("NodeImpl", "have");
		Node table = factory.getNode("NodeImpl", "table");
		
		far.setActivation(1.0);
		banana.setActivation(1.0);
		table.setActivation(1.0);
		
		bc.addNode(far);
		bc.addNode(banana);
		bc.addNode(table);
		
		long grab = 1L;
		Behavior b = new BehaviorImpl(grab);
		b.setLabel("grab");
		b.addContextCondition(near);
		b.addContextCondition(table);
		b.addContextCondition(banana);
		b.addToAddingList(banana);
		b.addToAddingList(have);
		behaviorNet.receiveBehavior(b);
		
		long walk = 2L;
		b = new BehaviorImpl(walk);
		b.setLabel("walk");
		b.addContextCondition(far);
		b.addToAddingList(near);
		behaviorNet.receiveBehavior(b);
		
		int timesToSendBroadcast = 10;
		for(int i = 0; i < timesToSendBroadcast; i++)
			behaviorNet.receiveBroadcast((BroadcastContent) bc);
	}

	@Override
	public void receiveActionId(long id) {
		System.out.println("Received action " + id);
	}

}
