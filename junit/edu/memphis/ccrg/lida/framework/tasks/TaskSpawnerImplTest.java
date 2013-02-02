/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TaskSpawnerImplTest {

	private TaskSpawnerImpl taskSpawner;
	private MockTaskManager tm;
	private MockFrameworkTask task1;
	private boolean initCalled;
	private double epsilon = 10e-9;

	@Before
	public void setUp() throws Exception {
		tm = new MockTaskManager(10, 10, -1);
		taskSpawner = new TaskSpawnerImpl();
		taskSpawner.setTaskManager(tm);
		task1 = null;
		initCalled = false;
	}

	@Test
	public void testAddTasks() {
		task1 = new MockFrameworkTask(10);
		MockFrameworkTask task2 = new MockFrameworkTask(20);
		List<FrameworkTask> tasks = new ArrayList<FrameworkTask>();
		tasks.add(task1);
		tasks.add(task2);
		taskSpawner.addTasks(tasks);

		assertEquals(taskSpawner, task1.getControllingTaskSpawner());
		assertEquals(TaskStatus.RUNNING, task1.getTaskStatus());
		assertTrue(taskSpawner.getTasks().contains(task1));
		assertTrue(tm.tasks.contains(task1));

		assertEquals(taskSpawner, task2.getControllingTaskSpawner());
		assertEquals(TaskStatus.RUNNING, task2.getTaskStatus());
		assertTrue(taskSpawner.getTasks().contains(task2));
		assertTrue(tm.tasks.contains(task2));
	}

	@Test
	public void testAddTask() {
		task1 = new MockFrameworkTask(10);
		taskSpawner.addTask(task1);
		assertEquals(taskSpawner, task1.getControllingTaskSpawner());
		assertEquals(TaskStatus.RUNNING, task1.getTaskStatus());
		assertTrue(taskSpawner.getTasks().contains(task1));
		assertEquals(1, taskSpawner.getTasks().size());
		assertEquals(task1, tm.task);
		assertEquals(10, tm.ticks);

		taskSpawner.addTask(task1);
		assertTrue(taskSpawner.getTasks().contains(task1));
		assertEquals(1, taskSpawner.getTasks().size());
	}

	@Test
	public void testReceiveFinishedTaskRUNNING() {
		task1 = new MockFrameworkTask(10);
		// RUNNING
		taskSpawner.addTask(task1);
		tm.task = null;
		tm.ticks = 0L;
		taskSpawner.receiveFinishedTask(task1);
		assertEquals(TaskStatus.RUNNING, task1.getTaskStatus());
		assertTrue(taskSpawner.getTasks().contains(task1));
		assertEquals(1, taskSpawner.getTasks().size());
		assertEquals(task1, tm.task);
		assertEquals(10, tm.ticks);

	}

	@Test
	public void testReceiveFinishedTaskFINISHED_WITH_RESULTS() {

		task1 = new MockFrameworkTask(10);

		// FINISHED_WITH_RESULTS
		taskSpawner = new TaskSpawnerImpl() {
			@Override
			public void processResults(FrameworkTask task) {
				assertEquals(task1, task);
			}
		};
		taskSpawner.setTaskManager(tm);

		taskSpawner.addTask(task1);
		task1.setTaskStatus(TaskStatus.FINISHED_WITH_RESULTS);
		tm.task = null;
		tm.ticks = 0L;

		taskSpawner.receiveFinishedTask(task1);

		assertEquals(TaskStatus.FINISHED_WITH_RESULTS, task1.getTaskStatus());
		assertTrue(!taskSpawner.getTasks().contains(task1));
		assertEquals(0, taskSpawner.getTasks().size());
		assertEquals(null, tm.task);
		assertEquals(0L, tm.ticks);
	}

	@Test
	public void testReceiveFinishedTaskFINISHED() {

		task1 = new MockFrameworkTask(10);

		taskSpawner = new TaskSpawnerImpl() {
			@Override
			public void processResults(FrameworkTask task) {
				assertTrue(false);
			}
		};
		taskSpawner.setTaskManager(tm);

		taskSpawner.addTask(task1);
		task1.setTaskStatus(TaskStatus.FINISHED);
		tm.task = null;
		tm.ticks = 0L;

		taskSpawner.receiveFinishedTask(task1);

		assertEquals(TaskStatus.FINISHED, task1.getTaskStatus());
		assertTrue(!taskSpawner.getTasks().contains(task1));
		assertEquals(0, taskSpawner.getTasks().size());
		assertEquals(null, tm.task);
		assertEquals(0L, tm.ticks);
	}

	@Test
	public void testReceiveFinishedTaskWAITING() {
		task1 = new MockFrameworkTask(10);
		// RUNNING
		taskSpawner.addTask(task1);
		task1.setTaskStatus(TaskStatus.RUNNING);
		tm.task = null;
		tm.ticks = 0L;
		taskSpawner.receiveFinishedTask(task1);
		assertEquals(TaskStatus.RUNNING, task1.getTaskStatus());
		assertTrue(taskSpawner.getTasks().contains(task1));
		assertEquals(1, taskSpawner.getTasks().size());
		assertEquals(task1, tm.task);
		assertEquals(10, tm.ticks);
	}

	@Test
	public void testReceiveFinishedTaskCANCEL() {

		task1 = new MockFrameworkTask(10);

		taskSpawner = new TaskSpawnerImpl() {
			@Override
			public void processResults(FrameworkTask task) {
				assertTrue(false);
			}
		};
		taskSpawner.setTaskManager(tm);

		taskSpawner.addTask(task1);
		task1.setTaskStatus(TaskStatus.CANCELED);
		tm.task = null;
		tm.ticks = 0L;

		taskSpawner.receiveFinishedTask(task1);

		assertEquals(TaskStatus.CANCELED, task1.getTaskStatus());
		assertTrue(!taskSpawner.getTasks().contains(task1));
		assertEquals(0, taskSpawner.getTasks().size());
		assertEquals(null, tm.task);
		assertEquals(0L, tm.ticks);
	}

	@Test
	public void testgetTasks() {
		task1 = new MockFrameworkTask(10);
		MockFrameworkTask task2 = new MockFrameworkTask(20);
		List<FrameworkTask> tasks = new ArrayList<FrameworkTask>();
		tasks.add(task1);
		tasks.add(task2);

		assertEquals(0, taskSpawner.getTasks().size());

		taskSpawner.addTasks(tasks);
		assertEquals(2, taskSpawner.getTasks().size());

		assertEquals(taskSpawner, task1.getControllingTaskSpawner());
		assertEquals(TaskStatus.RUNNING, task1.getTaskStatus());
		assertTrue(taskSpawner.getTasks().contains(task1));
		assertTrue(tm.tasks.contains(task1));

		assertEquals(taskSpawner, task2.getControllingTaskSpawner());
		assertEquals(TaskStatus.RUNNING, task2.getTaskStatus());
		assertTrue(taskSpawner.getTasks().contains(task2));
		assertTrue(tm.tasks.contains(task2));
	}

	@Test
	public void testCancelTask() {
		task1 = new MockFrameworkTask(10);
		MockFrameworkTask task2 = new MockFrameworkTask(20);
		List<FrameworkTask> tasks = new ArrayList<FrameworkTask>();
		tasks.add(task1);
		tasks.add(task2);
		taskSpawner.addTasks(tasks);
		assertEquals(2, taskSpawner.getTasks().size());
		taskSpawner.cancelTask(task1);
		assertEquals(task1, tm.cancelTask);
		assertEquals(1, taskSpawner.getTasks().size());
		assertTrue(!taskSpawner.containsTask(task1));
	}

	@Test
	public void testContainsTask() {
		task1 = new MockFrameworkTask(10);
		assertTrue(!taskSpawner.containsTask(task1));
		taskSpawner.addTask(task1);
		assertEquals(taskSpawner, task1.getControllingTaskSpawner());
		assertTrue(taskSpawner.containsTask(task1));

		taskSpawner.addTask(task1);
		assertTrue(taskSpawner.getTasks().contains(task1));
		assertEquals(1, taskSpawner.getTasks().size());
	}

}
