/*******************************************************************************
 * Copyright (c) 2009, 2011 The University of Memphis.  All rights reserved. 
 * This program and the accompanying materials are made available 
 * under the terms of the LIDA Software Framework Non-Commercial License v1.0 
 * which accompanies this distribution, and is available at
 * http://ccrg.cs.memphis.edu/assets/papers/2010/LIDA-framework-non-commercial-v1.0.pdf
 *******************************************************************************/
package edu.memphis.ccrg.lida.framework.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class FrameworkTaskImplTest {

	private MockFrameworkTask task1;
	private MockTaskSpawner taskSpawner;
	private double epsilon = 10e-9;

	@Before
	public void setUp() throws Exception {
		taskSpawner = new MockTaskSpawner();
		task1 = new MockFrameworkTask(10, taskSpawner);
	}

	@Test
	public void testCall() {
		task1.call();
		assertTrue(task1.wasRun);
		assertEquals(task1, taskSpawner.lastReceived);
	}

	@Test
	public void testTaskName() {
		assertEquals(MockFrameworkTask.class.getSimpleName() + "["
				+ task1.getTaskId() + "]", task1.toString());
	}

	@Test
	public void testSetTaskStatus() {
		assertEquals(TaskStatus.RUNNING, task1.getTaskStatus());

		task1.setTaskStatus(TaskStatus.FINISHED_WITH_RESULTS);
		assertEquals(TaskStatus.FINISHED_WITH_RESULTS, task1.getTaskStatus());

		task1.setTaskStatus(TaskStatus.CANCELED);
		assertEquals(TaskStatus.CANCELED, task1.getTaskStatus());

		task1.setTaskStatus(TaskStatus.FINISHED_WITH_RESULTS);
		assertEquals(TaskStatus.CANCELED, task1.getTaskStatus());
	}

	@Test
	public void testGetTaskId() {
		MockFrameworkTask task2 = new MockFrameworkTask(10, taskSpawner);
		assertNotSame(task1.getTaskId(), task2.getTaskId());
	}

	@Test
	public void testGetTicksPerRun() {
		assertEquals(10, task1.getTicksPerRun());
	}

	@Test
	public void testStopRunning() {
		task1.stopRunning();
		assertEquals(TaskStatus.CANCELED, task1.getTaskStatus());
	}

	@Test
	public void testGetControllingTaskSpawner() {
		assertEquals(taskSpawner, task1.getControllingTaskSpawner());
	}

	@Test
	public void testToString() {
		assertEquals(MockFrameworkTask.class.getSimpleName() + "["
				+ task1.getTaskId() + "]", task1.toString());
	}

}
