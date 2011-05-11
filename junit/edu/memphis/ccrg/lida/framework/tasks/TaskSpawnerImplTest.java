package edu.memphis.ccrg.lida.framework.tasks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class TaskSpawnerImplTest {

	private TaskSpawnerImpl taskSpawner;
	private MockTaskManager tm;
	
	@Before
	public void setUp() throws Exception {
		tm = new MockTaskManager(10, 10);
		taskSpawner=new TaskSpawnerImpl();
		taskSpawner.setTaskManager(tm);
	}

	@Test
	public void testAddTasks() {
		TestTask task1 = new TestTask(10);
		TestTask task2 = new TestTask(20);
		List<FrameworkTask> tasks = new ArrayList<FrameworkTask>();
		tasks.add(task1);
		tasks.add(task2);
		taskSpawner.addTasks(tasks);
		
		assertEquals(taskSpawner, task1.getControllingTaskSpawner());
		assertEquals(TaskStatus.RUNNING, task1.getTaskStatus());
		assertTrue(taskSpawner.getRunningTasks().contains(task1));
		assertTrue(tm.tasks.contains(task1));
		
		assertEquals(taskSpawner, task2.getControllingTaskSpawner());
		assertEquals(TaskStatus.RUNNING, task2.getTaskStatus());
		assertTrue(taskSpawner.getRunningTasks().contains(task2));
		assertTrue(tm.tasks.contains(task2));
	}

	@Test
	public void testAddTask() {
		TestTask task1 = new TestTask(10);
		taskSpawner.addTask(task1);
		assertEquals(taskSpawner, task1.getControllingTaskSpawner());
		assertEquals(TaskStatus.RUNNING, task1.getTaskStatus());
		assertTrue(taskSpawner.getRunningTasks().contains(task1));
		assertEquals(task1, tm.task);
		assertEquals(10, tm.ticks);
	}
	
	//TODO test adding same task twice

	@Test
	public void testRunTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testReceiveFinishedTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemoveTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testProcessResults() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetRunningTasks() {
		fail("Not yet implemented");
	}

	@Test
	public void testCancelTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testContainsTask() {
		fail("Not yet implemented");
	}

	@Test
	public void testInitMapOfStringQ() {
		fail("Not yet implemented");
	}

	@Test
	public void testInit() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetParam() {
		fail("Not yet implemented");
	}

}
