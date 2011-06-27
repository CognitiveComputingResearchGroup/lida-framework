/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.workspace.workspacebuffers;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import edu.memphis.ccrg.lida.framework.FrameworkModuleImpl;
import edu.memphis.ccrg.lida.framework.ModuleListener;
import edu.memphis.ccrg.lida.framework.shared.Linkable;
import edu.memphis.ccrg.lida.framework.shared.NodeStructure;
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

	private static final int DEFAULT_QUEUE_CAPACITY = 20;
	private int broadcastQueueCapacity = DEFAULT_QUEUE_CAPACITY;
	private LinkedList<NodeStructure> broadcastQueue = new LinkedList<NodeStructure>();

	/**
	 * Default constructor
	 */
	public BroadcastQueueImpl() {
	}

	@Override
	public void init() {
		int desired = (Integer) getParam("workspace.broadcastQueueCapacity",
				DEFAULT_QUEUE_CAPACITY);
		if (desired > 0) {
			broadcastQueueCapacity = desired;
		} else {
			logger.log(Level.WARNING, "Capacity must be greater than 0.",
					TaskManager.getCurrentTick());
		}
	}

	@Override
	public void receiveBroadcast(BroadcastContent bc) {
		if (bc != null) {
			synchronized (this) {
				ProcessBroadcastTask task = new ProcessBroadcastTask(
						((NodeStructure) bc).copy());
				taskSpawner.addTask(task);
			}
		} else {
			logger.log(Level.WARNING, "received null broadcast", TaskManager
					.getCurrentTick());
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
			broadcastQueue.addFirst(broadcast);
			// Keep the buffer at a fixed size
			while (broadcastQueue.size() > broadcastQueueCapacity) {
				broadcastQueue.removeLast();// remove oldest
			}
			setTaskStatus(TaskStatus.FINISHED);
		}
	}

	@Override
	public Object getModuleContent(Object... params) {
		return Collections.unmodifiableList(broadcastQueue);
	}

	@Override
	public void addBufferContent(WorkspaceContent content) {
		broadcastQueue.addFirst(content);
	}

	@Override
	public WorkspaceContent getBufferContent(Map<String, Object> params) {
		if (params != null) {
			Object index = params.get("position");
			if (index instanceof Integer) {
				Integer i = (Integer) index;
				if (i > -1 && i < broadcastQueue.size()) {
					return (WorkspaceContent) broadcastQueue.get(i);
				}
			}
		}
		return null;
	}

	@Override
	public void decayModule(long ticks) {
		logger.log(Level.FINER, "Decaying Broadcast Queue", TaskManager
		.getCurrentTick());
		synchronized(this){
			Iterator<NodeStructure> itr = broadcastQueue.iterator();
			while(itr.hasNext()){
				NodeStructure ns = itr.next();
				ns.decayNodeStructure(ticks);
				if (ns.getNodeCount() == 0) {
					itr.remove();
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