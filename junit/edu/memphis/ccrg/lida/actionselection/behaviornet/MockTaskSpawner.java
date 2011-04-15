/**
 * 
 */
package edu.memphis.ccrg.lida.actionselection.behaviornet;

import java.util.Collection;

import edu.memphis.ccrg.lida.framework.tasks.LidaTask;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl;
import edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager;
import edu.memphis.ccrg.lida.framework.tasks.TaskSpawner;

/**
 * @author Javier Snaider, Ryan J. McCall
 *
 */
public class MockTaskSpawner extends LidaTaskImpl implements TaskSpawner {

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.LidaTaskImpl#runThisLidaTask()
	 */
	@Override
	protected void runThisLidaTask() {
		// not implemented
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#addTask(edu.memphis.ccrg.lida.framework.tasks.LidaTask)
	 */
	@Override
	public void addTask(LidaTask task) {
		try {
			task.setControllingTaskSpawner(this);
			task.call();
		} catch (Exception e) {
			// not implemented catch block
			e.printStackTrace();
		}

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#cancelTask(edu.memphis.ccrg.lida.framework.tasks.LidaTask)
	 */
	@Override
	public boolean cancelTask(LidaTask task) {
		// not implemented method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#getSpawnedTasks()
	 */
	@Override
	public Collection<LidaTask> getRunningTasks() {
		// not implemented method stub
		return null;
	}


	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#receiveFinishedTask(edu.memphis.ccrg.lida.framework.tasks.LidaTask)
	 */
	@Override
	public void receiveFinishedTask(LidaTask task) {
		// not implemented method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#setInitialTasks(java.util.Collection)
	 */
	@Override
	public void addTasks(Collection<? extends LidaTask> initialTasks) {
		// not implemented method stub

	}

	/* (non-Javadoc)
	 * @see edu.memphis.ccrg.lida.framework.tasks.TaskSpawner#setTaskManager(edu.memphis.ccrg.lida.framework.tasks.LidaTaskManager)
	 */
	@Override
	public void setTaskManager(LidaTaskManager taskManager) {
		// not implemented method stub

	}

	@Override
	public boolean containsTask(LidaTask t) {
		// not implemented method stub
		return false;
	}

	@Override
	public String toString() {
		return null;
	}

}
