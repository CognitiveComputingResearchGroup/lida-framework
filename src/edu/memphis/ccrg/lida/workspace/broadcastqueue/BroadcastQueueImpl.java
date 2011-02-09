/*******************************************************************************
 * Copyright (c) 2009, 2010 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.broadcastqueue;

import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.LidaModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.ModuleName;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;

/**
 * This implementation stores incoming conscious broadcasts. There is a limit on
 * the queue's capacity.
 * 
 * @author Ryan J McCall
 * TODO Extend WorkspaceBufferImpl since BroadcastQueue extends workspace buffer?
 */
public class BroadcastQueueImpl extends LidaModuleImpl implements BroadcastQueue, BroadcastListener {

	private static final Logger logger = Logger.getLogger(BroadcastQueueImpl.class.getCanonicalName());

	private Queue<NodeStructure> broadcastQueue;
	
	private double lowerActivationBound;
	
	private static final int DEFAULT_QUEUE_CAPACITY = 20;
	private int broadcastQueueCapacity = DEFAULT_QUEUE_CAPACITY;
	

	public BroadcastQueueImpl() {
		super(ModuleName.BroadcastQueue);
		broadcastQueue = new ConcurrentLinkedQueue<NodeStructure>();
		broadcastQueue.add(new NodeStructureImpl());
	}
	
	@Override
	public synchronized void setLowerActivationBound(double lowerActivationBound) {
		this.lowerActivationBound = lowerActivationBound;
	}

	@Override
	public void addListener(ModuleListener listener) {
	}

	@Override
	public void init() {
		Double d = (Double) getParam("removableThreshold", 0.0);
		setLowerActivationBound(d);
		broadcastQueueCapacity = (Integer) getParam("workspace.broadcastQueueCapacity",DEFAULT_QUEUE_CAPACITY);
	}
	
	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		synchronized (this) {
			ProcessBroadcastTask task = new ProcessBroadcastTask(((NodeStructure) bc).copy());		
			taskSpawner.addTask(task);
		}
	}
	
	private class ProcessBroadcastTask extends LidaTaskImpl{		
		private NodeStructure broadcast;
		public ProcessBroadcastTask(NodeStructure broadcast) {
			super();
			this.broadcast = broadcast;
		}
		@Override
		protected void runThisLidaTask() {
			broadcastQueue.offer(broadcast);
			// Keep the buffer at a fixed size
			while (broadcastQueue.size() > broadcastQueueCapacity){
				broadcastQueue.poll();// remove oldest
			}
			setTaskStatus(LidaTaskStatus.FINISHED);
		}	
	}

	@Override
	public void learn(BroadcastContent content) {
		// Not applicable
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getModuleContent(Object... params) {
		return Collections.unmodifiableCollection(broadcastQueue);
	}

	@Override
	public void decayModule(long ticks) {
		super.decayModule(ticks);
		logger.log(Level.FINER, "Decaying Broadcast Queue", LidaTaskManager.getCurrentTick());
		for (NodeStructure ns : broadcastQueue) {
			ns.setLowerActivationBound(lowerActivationBound);
			ns.decayNodeStructure(ticks);
			//TODO lowerActivationBound parameter for decayNodeStructure??
		}
	}

}