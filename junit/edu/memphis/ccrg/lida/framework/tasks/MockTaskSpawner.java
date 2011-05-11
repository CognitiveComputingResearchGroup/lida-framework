package edu.memphis.ccrg.lida.framework.tasks;


public class MockTaskSpawner extends TaskSpawnerImpl {
	
	public FrameworkTask lastReceived;
	@Override
	public void receiveFinishedTask(FrameworkTask task) {
		lastReceived = task;
	}

}
