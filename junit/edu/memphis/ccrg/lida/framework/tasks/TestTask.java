package edu.memphis.ccrg.lida.framework.tasks;

public class TestTask extends FrameworkTaskImpl{
	public TestTask(int i, TaskSpawner taskSpawner) {
		super(i, taskSpawner);
	}
	public TestTask(int i) {
		super(i);
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
