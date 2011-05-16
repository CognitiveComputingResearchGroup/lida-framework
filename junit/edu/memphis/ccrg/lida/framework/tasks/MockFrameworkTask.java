package edu.memphis.ccrg.lida.framework.tasks;

public class MockFrameworkTask extends FrameworkTaskImpl{
	public MockFrameworkTask(int i, TaskSpawner taskSpawner) {
		super(i, taskSpawner);
	}
	public MockFrameworkTask(int i) {
		super(i);
	}
	public MockFrameworkTask() {
		super(10);
	}
	@Override
	public String toString() {
		return "testTask";
	}
	public boolean wasRun;
	@Override
	protected void runThisFrameworkTask() {
		wasRun = true;
	}
}
