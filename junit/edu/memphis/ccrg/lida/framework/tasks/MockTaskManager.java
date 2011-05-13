package edu.memphis.ccrg.lida.framework.tasks;

import java.util.ArrayList;
import java.util.List;

public class MockTaskManager extends TaskManager {

	public FrameworkTask task;
	public FrameworkTask cancelTask;
	public long ticks;
	public List<FrameworkTask> tasks = new ArrayList<FrameworkTask>();

	
	public MockTaskManager(int tickDuration, int maxPoolSize) {
		super(tickDuration, maxPoolSize);
	
	}

	@Override
	public boolean scheduleTask(FrameworkTask task, long inXTicks){
		this.task = task;
		ticks = inXTicks;
		tasks.add(task);
		return true;
		
	}
	@Override
	public boolean cancelTask(FrameworkTask task){
		cancelTask=task;
		return true;
	}
}
