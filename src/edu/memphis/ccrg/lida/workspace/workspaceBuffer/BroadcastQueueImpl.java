/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspaceBuffer;

import java.util.Collections;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
import edu.memphis.ccrg.lida.framework.shared.NodeStructureImpl;
import edu.memphis.ccrg.lida.framework.tasks.FrameworkTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.TaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskStatus;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastContent;
import edu.memphis.ccrg.lida.globalworkspace.BroadcastListener;
import edu.memphis.ccrg.lida.workspace.WorkspaceContent;

/**
 * The BroadcastQueue is the data structure storing the recent contents of
 * consciousness. It is a submodule of the Workspace. There is a limit on the
 * queue's capacity and on the amount of activation {@link Linkable}s must have
 * to remain in the queue.
 * 
 * @author Ryan J McCall
 */
public class BroadcastQueueImpl extends FrameworkModuleImpl implements
		WorkspaceBuffer, BroadcastListener {

	private static final Logger logger = Logger
			.getLogger(BroadcastQueueImpl.class.getCanonicalName());

	private LinkedList<NodeStructure> broadcastQueue;

	private static final int DEFAULT_QUEUE_CAPACITY = 20;
	private int broadcastQueueCapacity = DEFAULT_QUEUE_CAPACITY;

	public BroadcastQueueImpl() {
		broadcastQueue = new LinkedList<NodeStructure>();
		broadcastQueue.add(new NodeStructureImpl());
	}

	@Override
	public void init() {
		broadcastQueueCapacity = (Integer) getParam(
				"workspace.broadcastQueueCapacity", DEFAULT_QUEUE_CAPACITY);
	}

	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		synchronized (this) {
			ProcessBroadcastTask task = new ProcessBroadcastTask(
					((NodeStructure) bc).copy());
			taskSpawner.addTask(task);
		}
	}

	private class ProcessBroadcastTask extends FrameworkTaskImpl {
		private NodeStructure broadcast;

		public ProcessBroadcastTask(NodeStructure broadcast) {
			super();
			this.broadcast = broadcast;
		}

		@Override
		protected synchronized void runThisFrameworkTask() {
			broadcastQueue.offer(broadcast);
			// Keep the buffer at a fixed size
			while (broadcastQueue.size() > broadcastQueueCapacity) {
				broadcastQueue.poll();// remove oldest
			}
			setTaskStatus(TaskStatus.FINISHED);
		}
		@Override
		public String toString() {
			return BroadcastQueueImpl.class.getSimpleName() + "Broadcast";
		}
	}

	@Override
	public Object getModuleContent(Object... params) {
		return Collections.unmodifiableCollection(broadcastQueue);
	}
	@Override
	public void addBufferContent(WorkspaceContent content) {
		broadcastQueue.add(content);
	}

	@Override
	public WorkspaceContent getBufferContent(Map<String, Object> params) {
		Integer index = (Integer) params.get("position");
		if(index == null){
			return null;
		}
		return (WorkspaceContent) broadcastQueue.get(index);
	}

	@Override
	public void decayModule(long ticks) {
		logger.log(Level.FINER, "Decaying Broadcast Queue", TaskManager
				.getCurrentTick());
		synchronized(this){
			for (NodeStructure ns : broadcastQueue) {
				ns.decayNodeStructure(ticks);
				if (ns.getNodeCount() == 0) {
					broadcastQueue.remove(ns);
				}
			}
		}
	}

	@Override
	public void learn(BroadcastContent content) {
		// Not applicable
	}

	@Override
	public void addListener(ModuleListener listener) {
		// Not applicable
	}

}