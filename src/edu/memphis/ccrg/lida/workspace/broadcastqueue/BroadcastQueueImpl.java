/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.broadcastqueue;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.Link;
import edu.memphis.ccrg.lida.framework.shared.Node;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

/**
 * This implementation stores incoming conscious broadcasts. There is a limit on
 * the queue's capacity.
 * 
 * @author Ryan J McCall
 * 
 */
public class BroadcastQueueImpl extends LidaModuleImpl implements BroadcastQueue, BroadcastListener {

	private static Logger logger = Logger.getLogger(BroadcastQueueImpl.class.getCanonicalName());

	private Queue<NodeStructure> broadcastQueue = new ConcurrentLinkedQueue<NodeStructure>();
	private volatile int broadcastQueueCapacity;
	private double lowerActivationBound;
	private static final int DEFAULT_QUEUE_CAPACITY = 20;

	public BroadcastQueueImpl(int capacity) {
		super(ModuleName.BroadcastQueue);
		broadcastQueueCapacity = capacity;
		broadcastQueue.add(new NodeStructureImpl());
	}

	public BroadcastQueueImpl() {
		this(DEFAULT_QUEUE_CAPACITY);
	}

	@Override
	public synchronized void receiveBroadcast(BroadcastContent bc) {
		NodeStructure ns=((NodeStructure) bc).copy();
		broadcastQueue.offer(ns);
		// Keep the buffer at a fixed size
		while (broadcastQueue.size() > broadcastQueueCapacity){
			broadcastQueue.poll();// remove oldest
		}
	}

	@Override
	public void learn() {
		// Not applicable
	}

	public Collection<NodeStructure> getModuleContentCollection() {
		return Collections.unmodifiableCollection(broadcastQueue);
	}

	public boolean addLink(Link l) {
		return false;
	}

	public boolean addNode(Node n) {
		return false;
	}

	public boolean deleteLink(Link l) {
		return false;
	}

	public boolean deleteNode(Node n) {
		return false;
	}

	@Override
	public Object getModuleContent(Object... params) {
		return Collections.unmodifiableCollection(broadcastQueue);
	}

	public void mergeIn(NodeStructure ns) {
	}

	@Override
	public void decayModule(long ticks) {
		for (NodeStructure ns : broadcastQueue) {
			Collection<Node> nodes = ns.getNodes();
			for (Node n : nodes) {
				n.decay(ticks);
				if (n.getActivation() <= lowerActivationBound) {
					ns.removeNode(n);
				}
			}
		}
	}

	public void setLowerActivationBound(double lowerActivationBound) {
		this.lowerActivationBound = lowerActivationBound;
	}

	public void addListener(ModuleListener listener) {
	}

	public void init(Map<String, ?> p) {
		lidaProperties=p;
		Object o = getParam("workspace.broadcastQueueCapacity",DEFAULT_QUEUE_CAPACITY);
		if(o != null)
			broadcastQueueCapacity = (Integer) o;
		else
			logger.warning("could not load broadcast queue capacity");
	}
}// class